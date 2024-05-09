<template>
    <div id="Check">
        <div class="container">
            <div class="handle-box">
                <el-button type="primary" @click="addCheck">发起审核申请</el-button>
            </div>
            <el-table
                :data="tableData"
                border
                class="table"
                header-cell-class-name="table-header"
                ref="multipleTable"
                v-loading="loading"
                style="width: 92%"
            >
                <el-table-column align="center" checked="true" type="selection" width="55"></el-table-column>
                <el-table-column
                    align="center"
                    label="序号"
                    type="index"
                    width="55"
                    prop="index">
                </el-table-column>
                <el-table-column
                    :show-overflow-tooltip="true"
                    align="center"
                    label="试卷名称"
                    prop="pw_name"
                    width="200"
                ></el-table-column>
                <el-table-column
                    :show-overflow-tooltip="true"
                    align="center"
                    label="课程名称"
                    prop="pw_course"
                    width="100"
                ></el-table-column>
                <el-table-column
                    :show-overflow-tooltip="true"
                    align="center"
                    label="申请人"
                    prop="pw_create_by"
                    width="100"
                    header-class-name="header-style"
                ></el-table-column>
                <el-table-column
                    :show-overflow-tooltip="true"
                    align="center"
                    label="系级审核人"
                    prop="pw_auditor1"
                    width="100"
                    header-class-name="header-style"
                ></el-table-column>
                <el-table-column
                    :show-overflow-tooltip="true"
                    align="center"
                    label="院级审核人"
                    prop="pw_auditor2"
                    width="100"
                    header-class-name="header-style"
                ></el-table-column>
                <el-table-column
                    :show-overflow-tooltip="true"
                    align="center"
                    label="审核状态"
                    prop="pw_status"
                    width="150"
                >
                    <template slot-scope="scope">
                        <el-tag v-if="scope.row.pw_status === '0'" type="info">等待系级审核</el-tag>
                        <el-tag v-else-if="scope.row.pw_status === '10'" type="success">系级审核通过</el-tag>
                        <el-tag v-else-if="scope.row.pw_status === '20'" type="danger">系级审核拒绝</el-tag>
                        <el-tag v-else-if="scope.row.pw_status === '11'" type="info">院级审核通过</el-tag>
                        <el-tag v-else-if="scope.row.pw_status === '12'" type="danger">院级审核拒绝</el-tag>
                    </template>
                </el-table-column>
                <el-table-column
                    :show-overflow-tooltip="true"
                    align="center"
                    label="审核意见"
                    prop="pw_describe"
                    width="200"
                >
                    <template slot-scope="scope">
                        <span v-if="scope.row.pw_describe" v-html="scope.row.pw_describe.split('\n').filter(line => line.trim() !== '').join('<br>')"></span>
                        <span v-else>等待审批</span>
                    </template>
                </el-table-column>
                <el-table-column align="center" label="操作" width="150">
                    <template slot-scope="scope">
                        <el-dropdown
                        >
                            <span class="el-dropdown-link">
                                    操作<i class="el-icon-lx-qrcode"></i><i class="el-icon-caret-bottom el-icon--right"></i>
                            </span>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item class="clearfix" v-if="getUserName() === scope.row.pw_auditor1 || scope.row.pw_auditor2 && previewed">
                                    <el-button
                                        @click="handlePreview(scope.row, )"
                                        class="btn-width"
                                        icon="el-icon-view"
                                        type="primary"
                                    >试题预览
                                    </el-button>
                                </el-dropdown-item>
                                <el-dropdown-item class="clearfix" v-if="getUserName() === scope.row.pw_auditor1 || scope.row.pw_auditor2">
                                    <el-button
                                        @click="handleCheck(scope.row, '1')"
                                        class="btn-width"
                                        icon="el-icon-check"
                                        type="primary"
                                    >审批通过
                                    </el-button>
                                </el-dropdown-item>
                                <el-dropdown-item class="clearfix" v-if="getUserName() === scope.row.pw_auditor1 || scope.row.pw_auditor2">
                                    <el-button
                                        @click="handleCheck(scope.row, '2')"
                                        class="btn-width"
                                        icon="el-icon-close"
                                        type="danger"
                                    >审批拒绝
                                    </el-button>
                                </el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                    </template>
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

        <el-dialog
            v-dialog-drag
            title="发起试卷审批"
            :visible.sync="checkVisible"
            width="50%"
            center
            :close-on-click-modal="false"
        >
            <el-form ref="form" :model="form" label-width="m" >
                <el-form-item label="系级审核">
                    <el-select
                        v-model="form.selectAuditor1"
                        placeholder="请选择系级审核人"
                    >
                        <el-option
                            v-for="user in auditorList"
                            :key="user.userName"
                            :label="user.userName"
                            :value="user.userName"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="院级审核">
                    <el-select
                        v-model="form.selectAuditor2"
                        placeholder="请选择院级审核人"
                    >
                        <el-option
                            v-for="user in auditorList"
                            :key="user.userName"
                            :label="user.userName"
                            :value="user.userName"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="所属课程">
                    <el-select
                        v-model="form.selectCourse"
                        placeholder="请选择所属课程"
                    >
                        <el-option
                            v-for="course_ in courseList"
                            :key="course_.courseName"
                            :label="course_.courseName"
                            :value="course_.courseName"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="选择文件">
                    <div class="custom-file-input">
                        <input type="file" @change="handleFileUpload" />
                        <span class="custom-file-input-text">点击选择文件</span>
                        <span v-if="uploadSuccess" class="upload-success-icon" style="color: green">✔</span>
                        <span v-else class="upload-fail-icon" style="color: red">x</span>
                    </div>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="handleSubmitAudit">提交审核</el-button>
                <el-button @click="checkVisible = false; resetForm()">取消</el-button>
            </div>
        </el-dialog>
        <el-dialog
            title="审批意见"
            :visible.sync="approveDialogVisible"
            width="30%"
            @close="approveDialogVisible = false"
        >
            <el-input
                type="textarea"
                :rows="3"
                placeholder="请输入审批意见"
                v-model="approveRemark"
            ></el-input>
            <span slot="footer" class="dialog-footer">
            <el-button @click="approveDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="handleCheckConfirm">确定</el-button>
        </span>
        </el-dialog>
    </div>
</template>

<script>
import { check, getAdminList, getCourse, query, save, uploadto_server,getPublicKey } from '@/api/check';
import question from '@/components/page/queManage/Question.vue';
import power from '@/utils/power';
import JSEncrypt from 'jsencrypt';


export default {
    mixins: [power],
    computed: {
        question() {
            return question
        }
    },
    data() {
        return {
            tableData: [],
            loading: false,
            query : {
                paperId: '',
                papername: '',
                username: this.getUserName(),
                courseName: '',
                auditor1: '',
                auditor2: '',
                auditStatus: '',
                auditRemark: '',
                file: '',
                iv : '',
                page: 1,
                pageSize: 10,
                status: 0,
            },
            paperId : '',
            pageTotal : 0,
            checkVisible: false,
            approveDialogVisible: false,
            previewed: '0',
            approveRemark: '',
            currentRow: {},
            approvedStatus: '',
            decryptedAESKey: null,
            form : {
                selectCourse: '',
                selectAuditor1: '',
                selectAuditor2: '',
                file: null,
            },
            courseList: [],
            auditorList: [],
            uploadSuccess: false,
            encryptedFileBlob: null,
            remark: '',
        };
    },
    created() {
        this.getData();
    },
    methods: {
        getData() {
            this.loading = true;
            query(this.query)
                .then(res => {
                    this.loading = false;
                    if (res.pageTotal === -1) {
                        //身份验证已过期
                        this.$alert('请重新登录', '身份验证已过期', {
                            type: 'error',
                            confirmButtonText: '确定',
                            callback: (action) => {
                                localStorage.removeItem('ms_username');
                                this.$router.push('/login');
                            }
                        });
                    }
                    this.tableData = res;
                    this.pageTotal = res.pageTotal || 0;
            });
            getAdminList().then(res => {
                this.auditorList = res;
            });
            getCourse().then(res => {
                this.courseList = res;
            });
        },
        addCheck() { // 添加审核
            this.checkVisible = true;
        },
        resetForm() {
            this.form = {
                file: null,
                auditor: '',
                course: '',
                remark: '',
            };
            this.selectCourse = '';
            this.selectAuditor1 = '';
            this.selectAuditor2 = '';
            this.uploadSuccess = false;
        },
        async handleFileUpload(event) {
            const file = event.target.files[0];
            this.papername = file.name;
            try {
                // 生成AES密钥
                const aesKey = await this.generateRandomAESKey();
                // 读取文件内容
                const fileContentArrayBuffer = await this.readFile(file);
                // 加密文件内容
                const { ciphertext, iv } = await this.encryptFileContent(fileContentArrayBuffer, aesKey);
                this.uploadSuccess = true;
                // 将加密后的文件内容和密钥保存到组件的data中
                this.form.file = file;
                this.encryptedFileData = {
                    encrypted: ciphertext,
                    key: aesKey,
                    iv: iv,
                };
                this.encryptedFileBlob = new Blob([ciphertext]);
                this.form.file = this.encryptedFileData;
                // // 假设 `encryptedFileData.encrypted` 是一个 ArrayBuffer 对象，表示加密后的文件内容
                // const encryptedFileBlob = new Blob([this.encryptedFileData.encrypted]);
                //
                // // 创建一个表示该 Blob 的 URL
                // const url = URL.createObjectURL(encryptedFileBlob);
                //
                // // 在新窗口中打开这个 URL
                // window.open(url);
                //
                // // 或者，将其设置为一个 <a> 标签的 href 属性，让用户可以点击下载
                // const downloadLink = document.createElement('a');
                // downloadLink.href = url;
                // downloadLink.download = 'encrypted-file';
                // document.body.appendChild(downloadLink);
                // downloadLink.click();
                // document.body.removeChild(downloadLink);
            } catch (error) {
                console.error('File encryption failed:', error);
                this.uploadSuccess = false;
            }
        },
        async generateRandomAESKey() {
            const aesKey = await window.crypto.subtle.generateKey(
                {
                    name: 'AES-GCM',
                    length: 256, // 可选值：128, 192, 256
                },
                true, // 是否可导出（exportable）
                ['encrypt', 'decrypt'] // 用途
            );
            return aesKey;
        },
        readFile (file) {
            return new Promise((resolve, reject) => {
                const reader = new FileReader();

                reader.onload = () => {
                    resolve(reader.result);
                };
                reader.onerror = reject;
                reader.readAsArrayBuffer(file);
            });
        },
        async encryptFileContent(arrayBuffer, aesKey) { // 加密文件内容
            const iv = window.crypto.getRandomValues(new Uint8Array(12)); // 生成随机12字节IV
            const ciphertext = await window.crypto.subtle.encrypt( // 使用AES-GCM加密
                {
                    name: 'AES-GCM',
                    iv,
                },
                aesKey,
                arrayBuffer
            );
            const ivString = this.arrayBufferToBase64(iv);
            console.log("ivString1:"+ivString)
            return { ciphertext, iv: ivString };
        },

        arrayBufferToBase64(buffer) { // ArrayBuffer转Base64
            let binary = ''; // 用来存储转换后的base64
            let bytes = new Uint8Array(buffer);
            let len = bytes.byteLength;
            for (let i = 0; i < len; i++) {
                binary += String.fromCharCode(bytes[i]);
            }
            return window.btoa(binary);
        },

        async getCryptoKeyString(cryptoKey) { // 获取密钥字符串
            const keyData = await window.crypto.subtle.exportKey('jwk', cryptoKey);
            console.log("keyData:"+keyData.k)
            return keyData.k;
        },

        async handleSubmitAudit() { // 提交审核
            const { ciphertext, iv } = this.encryptedFileData;
            const key = this.encryptedFileData.key;
            const paperkey1 = await this.getCryptoKeyString(key);
            const publicKey = await this.getPublicKey(this.form.selectAuditor1);
            const paperkey = await this.encryptAESKeyWithPublicKey(publicKey, paperkey1);
            console.log("key:"+paperkey)
            console.log("paperkey:"+paperkey)
            const papercreateBy = localStorage.getItem('ms_username');
            const papername = this.papername;
            const uploadResponse = await this.uploadToServer({
                encryptedFileData: this.encryptedFileData,
            });
            if (uploadResponse.success) {
                const saveResponse = await this.save({
                    papername : papername,
                    papercreateBy: papercreateBy,
                    paperurl: uploadResponse.fileUrl,
                    paperkey: paperkey,
                    iv: iv,
                    auditor1: this.form.selectAuditor1,
                    auditor2: this.form.selectAuditor2,
                    course: this.form.selectCourse,
                });
                if (saveResponse.success) {
                    console.log('上传成功');
                    alert('上传成功');
                    this.checkVisible = false;
                    this.getData();
                } else {
                    console.log('上传失败：', uploadResponse.message);
                    alert('上传失败：' + uploadResponse.message);
                    return;
                }
            } else {
                console.log('上传失败：', uploadResponse.message);
                alert('上传失败：' + uploadResponse.message);
                return;
            }
            this.resetForm();
        },

        async uploadToServer({ encryptedFileData }) { // 上传文件到服务器
            const formData = new FormData();
            const encryptedFileBlob = new Blob([encryptedFileData.encrypted]);
            formData.append('file', encryptedFileBlob, this.filename);
            const response = await uploadto_server(formData);
            console.log("response.fileUrl:", response.fileUrl);
            return response;
        },

        async getPublicKey(username) {
            const response = await getPublicKey(username);
            if (response != null) {
                return response.publicKey;
            } else {
                console.log('获取公钥失败：', response.message);
                return null;
            }
        },

        async encryptAESKeyWithPublicKey(publicKey, aesKeyBaseUrl) {
            const encrypt = new JSEncrypt();
            encrypt.setPublicKey(publicKey);
            const encryptedAESKey = encrypt.encrypt(aesKeyBaseUrl);
            return encryptedAESKey;
        },

        // async encryptAESKeyWithRSA(aesKeyString, auditor) {
        //     try {
        //         const aesKeyArrayBuffer = this.base64UrlToArrayBuffer(aesKeyString);
        //         console.log("aesKeyArrayBuffer:"+aesKeyArrayBuffer)
        //         const publicKeyString = await this.getPublicKey(auditor);
        //         const publicKey = await this.base64ToJwk(publicKeyString);
        //         const keyData = await window.crypto.subtle.importKey( // 导入RSA公钥
        //             'jwk',
        //             publicKey,
        //             {
        //                 name: "RSA-OAEP",
        //                 hash: {name: "SHA-256"}
        //             },
        //             true,
        //             ['encrypt']
        //         );
        //         console.log("keyData:"+keyData)
        //         try {
        //             const encryptedKey = await window.crypto.subtle.encrypt( // 使用RSA公钥加密AES密钥
        //                 {
        //                     name: "RSA-OAEP",
        //                 },
        //                 keyData, // RSA公钥
        //                 aesKeyArrayBuffer // AES密钥
        //             );
        //             console.log("encryptedKey:"+encryptedKey)
        //             return encryptedKey;
        //         } catch (encryptError) {
        //             console.error('Encryption error:', encryptError);
        //             console.log(encryptError.error)
        //         }
        //     } catch (error) {
        //         console.error('Error:', error);
        //     }
        // },

        async save({ papername, papercreateBy, paperurl, paperkey, iv, auditor1, auditor2,  course}) { // 保存文件信息
            const formData2 = new FormData();
            formData2.append('papername', papername);
            formData2.append('papercreateBy', papercreateBy);
            formData2.append('paperurl', paperurl);
            formData2.append('paperkey', paperkey);
            formData2.append('iv', iv);
            formData2.append('auditor1', auditor1);
            formData2.append('auditor2', auditor2);
            formData2.append('course', course);
            const response = await save(formData2);
            console.log("response:", response);
            return response;
        },

        handleCheck(row, approvedStatus) {
            console.log("checkpreviewed:"+ this.previewed)
            if (this.previewed !== '1') {
                this.$message.error('请先预览试题');
                return;
            }
            this.approveDialogVisible = true;
            this.currentRow = row;
            this.approvedStatus = approvedStatus;
        },
        async handleCheckConfirm() {
            let approved;
            const auditor = this.getUserName();

            // 判断审批人身份
            if (auditor === this.currentRow.pw_auditor1) {
                approved = this.approvedStatus === '1' ? '10' : '20';
            } else if (auditor === this.currentRow.pw_auditor2) {
                // 只有当pw_status为10时，auditor2才能审批
                if (this.currentRow.pw_status !== '10') {
                    this.$message.error('当前状态下，你没有审批权限');
                    return;
                }
                approved = this.approvedStatus === '1' ? '11' : '12';
            } else {
                this.$message.error('你没有审批权限');
                return;
            }
            const publicKey = await this.getPublicKey(this.currentRow.pw_auditor2);
            console.log("publicKey:"+publicKey)
            console.log("decryptedAESKey:"+this.decryptedAESKey)
            const encryptedAESKey = await this.encryptAESKeyWithPublicKey(publicKey, this.decryptedAESKey); // 使用公钥加密AES密钥
            console.log("encryptedAESKey:"+encryptedAESKey)

            const params = {
                auditor: auditor,
                paperID: this.currentRow.pw_id,
                approved: approved,
                remark: auditor+ ":" + this.approveRemark, // 添加审批意见到params中
                paperKey: encryptedAESKey,
            };

            const response = await check(params);
            if (response.success) {
                this.$message.success('审批成功');
                this.getData();
            } else {
                this.$message.error('审批失败');
            }
            this.approveDialogVisible = false;
        },

        async handlePreview(row) {
            // 从row中获取文件地址和AES密钥
            const privateKeyFile = await this.selectPrivateKeyFile(); // 选择私钥文件
            const privateKeyBuffer = await this.readFile(privateKeyFile); // 读取私钥文件内容
            const privateKey = new TextDecoder().decode(privateKeyBuffer); // 将ArrayBuffer转换为字符串
            console.log("privateKey:"+privateKey)
            const aesKeyString = row.pw_key;
            const decryptedAESKey = await this.decryptAESKeyWithPrivateKey(privateKey, aesKeyString); // 解密AES密钥
            this.decryptedAESKey = decryptedAESKey;
            console.log("decryptedAESKey:"+decryptedAESKey)
            const AESBuffer = await this.importKeyFromBase64Url(decryptedAESKey); // 将AES密钥转换为ArrayBuffer

            const address = row.pw_address;
            let parts = address.split("\\");
            let filename = parts[parts.length - 1];
            const ivString = row.iv;
            const iv = this.base64ToArrayBuffer(ivString)

            // 从服务器获取文件内容
            const response = await fetch(`http://localhost:9527/uploads/${filename}`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const encryptedFileArrayBuffer = await response.arrayBuffer();
            // 使用AES密钥解密文件内容
            const decryptedFileArrayBuffer = await this.decryptFileContent(encryptedFileArrayBuffer, AESBuffer, iv);

            //创建一个表示解密后文件内容的Blob对象
            const decryptedFileBlob = new Blob([decryptedFileArrayBuffer], { type: 'application/msword' });
            // const decryptedFileBlob = new Blob([decryptedFileArrayBuffer], { type: 'application/pdf' });

            // 创建一个表示该Blob的URL
            const url = URL.createObjectURL(decryptedFileBlob);

            // 在新窗口中打开这个URL
            window.open(url, '_blank');
            this.previewed = '1';
        },

        async decryptFileContent(encryptedData, aesKey, iv) {
            try {
                const decryptedContent = await window.crypto.subtle.decrypt(
                    {
                        name: 'AES-GCM',
                        iv: iv
                    },
                    aesKey,
                    encryptedData
                );
                return decryptedContent;
            } catch (error) {
                console.error('Decryption failed:', error);
            }
        },

        selectPrivateKeyFile() {
            return new Promise((resolve, reject) => {
                const input = document.createElement('input');
                input.type = 'file';
                input.onchange = () => {
                    resolve(input.files[0]);
                };
                input.onerror = () => {
                    reject(new Error('File selection was cancelled'));
                };
                input.click();
            });
        },

        async decryptAESKeyWithPrivateKey(privateKey, encryptedAESKey) { // 解密AES密钥
            const decrypt = new JSEncrypt();
            decrypt.setPrivateKey(privateKey); // 设置私钥
            const decryptedAESKey = decrypt.decrypt(encryptedAESKey); // 使用私钥解密AES密钥
            return decryptedAESKey;
        },

        base64ToArrayBuffer(base64) {
            // 将base64字符串转换为二进制数据
            const binaryString = window.atob(base64);
            // 计算二进制数据的字节长度
            const len = binaryString.length;
            // 创建一个新的Uint8Array来存放ArrayBuffer的数据
            const bytes = new Uint8Array(len);
            // 遍历二进制字符串，逐个字符填充到Uint8Array中
            for (let i = 0; i < len; i++) {
                bytes[i] = binaryString.charCodeAt(i);
            }
            // 返回包含Uint8Array的ArrayBuffer
            return bytes.buffer;
        },

        async importKeyFromBase64Url(base64UrlKey) { // 导入Base64URL编码的密钥
            // 将 Base64URL 编码的字符串转换为 ArrayBuffer
            const rawKey = this.base64UrlToArrayBuffer(base64UrlKey);

            // 使用 ArrayBuffer 创建一个新的 CryptoKey 对象
            const cryptoKey = await window.crypto.subtle.importKey(
                'raw', // 导入操作的格式
                rawKey, // 包含密钥数据的 ArrayBuffer 或 ArrayBufferView
                'AES-GCM', // 密钥的算法
                true, // 是否可导出
                ['encrypt', 'decrypt'] // 密钥的用途
            );

            return cryptoKey;
        },

        base64UrlToArrayBuffer(base64Url) { // 将 Base64URL 编码的字符串转换为 ArrayBuffer
            // 将 Base64URL 编码的字符串转换为普通的 Base64 编码
            const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');

            // 将 Base64 编码的字符串解码为二进制字符串
            const binaryString = window.atob(base64);

            // 创建一个新的 ArrayBuffer 来存储二进制数据
            const len = binaryString.length;
            const bytes = new Uint8Array(len);

            // 将二进制字符串的每个字符的字符代码填充到 Uint8Array 中
            for (let i = 0; i < len; i++) {
                bytes[i] = binaryString.charCodeAt(i);
        }
         return bytes.buffer;
        },

        // 分页导航
        handlePageChange(val) {
            this.query.page = val;
            this.$set(this.query, 'page', val);
            this.getData();
        },
        // 页面条数改变
        handleSizeChange(val) {
            this.$set(this.query, 'pageSize', val);
            this.getData();
        },
        // 多选操作
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        addTpDialog(title) {
            this.queDrawerTitle = title
            this.queDrawerVisible = true;
            this.tp.isAutoAdd = false;
        },
    }
};


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
.upload-container {
    display: flex;
    align-items: center;
}
/* 在全局样式文件或当前组件的<style>标签内 */
input[type="file"] {
    display: inline-block;
    width: 100%;
    height: 32px;
    line-height: 32px;
    padding: 0 12px;
    border: 1px solid #DCDFE6;
    border-radius: 4px;
    color: #606266;
    background-color: #fff;
    outline: none;
    cursor: pointer;
    transition: border-color .2s cubic-bezier(.645,.045,.355,1);
}

input[type="file"]:hover {
    border-color: #C0C4CC;
}

input[type="file"]:focus {
    border-color: #409EFF;
    box-shadow: 0 0 0 7px rgba(64,158,255);
}

.custom-file-input {
    position: relative;
    display: inline-block;
    width: 100%;
    height: 32px;
    line-height: 32px;
    cursor: pointer;
}

.custom-file-input input[type="file"] {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    opacity: 0;
    cursor: pointer;
}

.custom-file-input-text {
    display: inline-block;
    width: 100%;
    height: 100%;
    text-align: center;
    line-height: inherit;
    color: #606266;
    background-color: #fff;
    border: 1px solid #DCDFE6;
    border-radius: 4px;
    transition: border-color .2s cubic-bezier(.645,.045,.355,1);
}

.custom-file-input:hover .custom-file-input-text {
    border-color: #C0C4CC;
}

 .header-style {
     background-color: #f5f7fa; /* 背景颜色 */
     color: #606266; /* 文字颜色 */
     font-weight: bold; /* 字体加粗 */
     text-align: center; /* 文字居中 */
 }

</style>