<template>
    <div id="keyManage">

        <!-- 生成密钥对的按钮 -->
        <div class="container">
            <div class="handle-box">
                <el-button type="primary" @click="generateKeyPair">重新生成密钥</el-button>
            </div>
            <el-table
                :data="tableData"
                border
                class="table"
                header-cell-class-name="table-header"
                ref="multipleTable"
                v-loading="loading"
            >
                <el-table-column align="center" checked="true" type="selection" width="55"></el-table-column>
                <el-table-column
                    :show-overflow-tooltip="true"
                    align="center"
                    label="所属人"
                    prop="key_owner"
                    width="150"
                ></el-table-column>
                <el-table-column
                    :show-overflow-tooltip="true"
                    align="center"
                    label="公钥"
                    prop="public_key"
                    width="1000"
                ></el-table-column>

                <el-table-column align="center" label="操作" width="150">
                    <template slot-scope="scope">
                        <el-button
                            @click="copyToClipboard(scope.row.public_key)"
                            class="btn-width"
                            icon="el-icon-copy-document"
                            type="primary"
                        >复制公钥
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>

            <!-- 添加生成密钥弹出框 -->
            <el-dialog :visible.sync="keyDialogVisible" title="您的RSA密钥对为：">
                <div>
                    <h3>您的公钥为：</h3>
                    <el-input
                        type="textarea"
                        :rows="4"
                        :value="keyPair.publicKey"
                        auto-complete="off"
                    ></el-input>
                </div>
                <div slot="footer" class="dialog-footer">
                    <el-button type="danger" @click="savePrivateKey">保存私钥</el-button>
                </div>
            </el-dialog>

            <div class="pagination"> <!-- 分页 -->
            <el-pagination
                background
                layout="total, sizes, prev, pager, next, jumper"
                :current-page="query.page"
                :page-size="query.pageSize"
                :page-sizes="[5, 7, 10, 15, 20, 40, 100]"
                :total="pageTotal"
                @current-change="handlePageChange"
                @size-change="handleSizeChange"
            ></el-pagination>
        </div>
        </div>
    </div>
</template>


<script>
import { query, generate, getAllKeys } from '@/api/Key';
import {jwk2pem} from 'pem-jwk';
import AsToWord from '@/utils/AsToWord';
import power from '@/utils/power';
import rules from '@/utils/rules';

export default {
    data() {
        return {
            tableData: [],
            loading: false,
            query: {

            },
            pageTotal: 0,
            form: {},
            idx: -1,
            id: -1,
            keyDialogVisible: false,
            keyPair: {
                publicKey: '',
                privateKey: '',
            },

        };
    },
    created() {
        this.getData();
    },
    methods: {
        //获取数据
        async getData() {
            let keyOwner = localStorage.getItem('ms_username');
            this.loading = true;
            const res = await query(keyOwner, this.query);
            this.loading = false;
            console.log("res:"+res)
            if (res) {
                this.tableData = [{
                    key_owner: localStorage.getItem('ms_username'),
                    public_key: res
                }]
            }
        },

        async generateKeyPair() {
            let keyOwner = localStorage.getItem('ms_username'); // 假设你将用户名存储在localStorage的'username'键下
            try {
                let res = await generate(keyOwner, false); // 调用后端接口生成密钥对，不覆盖已有密钥
                console.log("res:" + res)
                console.log(" res.status " + res.status)
                if (res.status === "409") { // 如果状态码是409
                    this.$confirm('密钥对已存在，是否覆盖?', '提示', {
                        confirmButtonText: '覆盖',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(async () => { // 用户选择覆盖
                        res = await generate(keyOwner, true); // 用户选择覆盖，再次调用后端接口，这次覆盖已有密钥
                        if (res) {
                            this.keyPair.publicKey = res.publicKey; // 保存公钥
                            this.keyPair.privateKey = res.privateKey; // 保存私钥
                            this.keyDialogVisible = true; // 显示弹出框
                        } else {

                        }
                    }).catch(() => {
                        // 用户选择取消，不做任何操作
                    });
                }
                if (res.status !== "409" && res) {  // 如果状态码是200
                    console.log(123)
                    this.keyPair.publicKey = res.publicKey; // 保存公钥
                    this.keyPair.privateKey = res.privateKey; // 保存私钥
                    this.keyDialogVisible = true; // 显示弹出框
                } else {

                }
            } catch (error) {
                console.error('Error generating key pair:', error);
                this.$message({
                    message: '生成密钥对时出错',
                    type: 'error',
                });
            }
        },


        // 保存私钥
        savePrivateKey() {
            this.$confirm('私钥非常重要，请勿将私钥置于电脑桌面等显眼位置！', '警告', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                // 创建一个 Blob 对象，内容是私钥
                let blob = new Blob([this.keyPair.privateKey] , {type: "text/plain;charset=utf-8"});

                // 创建一个指向 Blob 对象的 URL
                let url = window.URL.createObjectURL(blob);

                // 创建一个隐藏的 <a> 标签，设置 href 为 Blob URL，并设置下载的文件名
                let link = document.createElement('a');
                link.href = url;
                link.download = 'privateKey.pem';
                link.style.display = 'none';

                // 将 <a> 标签添加到 DOM 中
                document.body.appendChild(link);

                // 触发点击事件来开始下载
                link.click();

                // 下载完成后，移除 <a> 标签
                document.body.removeChild(link);

                this.$message({
                    message: '私钥保存成功',
                    type: 'success',
                });
                this.keyDialogVisible = false;
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消下载',
                });
            });
        },

        // 复制公钥到剪贴板
        copyToClipboard(text) {
            navigator.clipboard.writeText(text).then(() => {
                this.$message({
                    message: '公钥已复制到剪贴板',
                    type: 'success',
                });
            }).catch(() => {
                this.$message({
                    message: '复制公钥失败',
                    type: 'error',
                });
            });
        }
    },
}

</script>


<style scoped>
.handle-box {
    margin-bottom: 20px;
}

.btn-width-see {
    width: 180px;
    margin-top: 3px;
}

.btn-width {
    width: 100px;
}

.handle-select {
    width: 120px;
}

.handle-input {
    width: 150px;
    display: inline-block;
}

.table {
    width: 100%;
    font-size: 14px;
}

.red {
    color: #ff0000;
}

.mr10 {
    margin-right: 10px;
}

.table-td-thumb {
    display: block;
    margin: auto;
    width: 40px;
    height: 40px;
}
.drawer-footer {
    margin-left: 20%;
    /* margin-top: -30px; */
}
.drawer-footer button {
    width: 200px;
    text-align: center;
}
.dialog-footer {
    display: flex;
    justify-content: center;
}
</style>