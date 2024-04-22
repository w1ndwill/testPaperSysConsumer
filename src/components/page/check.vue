<template>
    <div id="Check">
        <div class="container">
            <div class="handle-box">
                <el-button type="primary" >发起审核申请</el-button>
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
                    label="申请人"
                    prop="applicant"
                    width="150"
                ></el-table-column>
                <el-table-column
                    :show-overflow-tooltip="true"
                    align="center"
                    label="申请时间"
                    prop="apply_time"
                    width="200"
                ></el-table-column>
                <el-table-column
                    :show-overflow-tooltip="true"
                    align="center"
                    label="审核状态"
                    prop="status"
                    width="300"
                ></el-table-column>
                <el-table-column
                    :show-overflow-tooltip="true"
                    align="center"
                    label="审核意见"
                    prop="audit_opinion"
                    width="500"
                ></el-table-column>
                <el-table-column align="center" label="操作" width="150">
                    <el-dropdown-menu>
                        <el-dropdown-item class="clearfix" v-if="">
                            <el-button
                                @click=""
                                class="btn-width"
                                icon="el-icon-edit"
                                type="primary"
                            >查看审核进程
                            </el-button>
                        </el-dropdown-item>
                        <el-dropdown-item class="clearfix" v-if="">
                            <el-button
                                @click=""
                                class="btn-width"
                                icon="el-icon-edit"
                                type="primary"
                            >审批通过
                            </el-button>
                        </el-dropdown-item>
                        <el-dropdown-item class="clearfix" v-if="">
                            <el-button
                                @click=""
                                class="btn-width"
                                icon="el-icon-edit"
                                type="primary"
                            >审批拒绝
                            </el-button>
                        </el-dropdown-item>
                    </el-dropdown-menu>
                </el-table-column>
            </el-table>
            <div class="pagination">
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

        <el-dialog v-dialog-drag : title="发起试卷审批"  :visible.sync="checkVisible" whdth="50%" center :close-on-click-modal="false" >
            <el-form ref="uploadForm" :model="uploadForm" label-width="80px">
                <el-form-item label="选择文件">
                    <el-upload
                        action="/api/upload"
                        :on-success="handleSuccess"
                        :on-error="handleError"
                        :before-upload="beforeUpload"
                    >
                        <el-button size="small" type="primary">点击上传</el-button>
                    </el-upload>
                </el-form-item>
                <el-form-item label="选择审核人">
                    <el-select v-model="uploadForm.admin" placeholder="请选择审核人">
                        <el-option
                            v-for="item in adminList"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item lebel="备注">
                    <el-input type="textarea" v-model="uploadForm.remark" placeholder="请输入备注" rows="4"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="drawer-footer">
                <el-button type="primary" @click="submitCheck">提交审核</el-button>
                <el-button @click="checkVisible = false">取消</el-button>
            </div>
        </el-dialog>
    </div>
</template>


<script>


import axios from 'axios';

export default {
    data() {
        return {
            tableData: [],
            loading: false,
            query : {
                page: 1,
                limit: 10,
                status: '',
                applicant: '',
            },
            pageTotal : 0,
            form : {
                applicant: '',
                status: '',
            },
            checkVisible: false,
            dialogVisible: false,
        };
    },
    created() {
        // this.getData();
    },
    methods: {
        async getAdminList() {
            try {
                this.loading = true;
                const response = await axios.get('/api/admins');
                this.tableData = response.data;
            } catch (error) {
                console.error(error);
                this.$message.error('获取管理员列表失败');
            } finally {
                this.loading = false;
            }
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