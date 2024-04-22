<template>
    <div class="register-wrap">
        <el-dialog
            v-dialogDrag
            :visible="true"
            width="30%"
            custom-class="my-dailog"
            :show-close="false"
            :modal="false"
            :before-close="
                () => {
                    return false;
                }
            "
        >
            <el-row>
                <div class="ms-title">试卷管理系统</div>

                <el-form ref="user" :model="user" label-width="100px" class="form">
                    <el-col :span="18" :offset="2">
                        <el-form-item label="用户名:" class="lableColor" prop="userName" :rules="rules.userName">
                            <el-input v-model="user.userName" placeholder="请输入用户名"></el-input>
                        </el-form-item>
                        <el-form-item label="性别:" class="lableColor" prop="sex" :rules="rules.pleaseSelect">
                            <el-select v-model="user.sex" placeholder="请选择性别" style="width: 100%">
                                <el-option key="1" label="男" value="1"></el-option>
                                <el-option key="0" label="女" value="0"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="密码:" class="lableColor" prop="password" :rules="rules.password">
                            <el-input type="password" :placeholder="'请输入密码'" v-model="user.password"></el-input>
                        </el-form-item>
                        <el-form-item label="手机号:" class="lableColor" prop="userPhone" :rules="rules.phone">
                            <el-input v-model="user.userPhone" placeholder="请输入手机号" maxlength="11"></el-input>
                        </el-form-item>
                        <el-form-item label="邮箱:" class="lableColor" prop="email" :rules="rules.email">
                            <el-input type="email" v-model="user.email" placeholder="请输入邮箱"></el-input>
                        </el-form-item>
                    </el-col>
                    <!-- <el-col :span="6">
                        <userPhoto ref="photo" :upLoadPath="'http://localhost:9527/upload/photo'" :srcUrl="user.photo" />
                    </el-col> -->
                </el-form>
            </el-row>
            <el-row>
                <div class="tip">
                    <label><el-checkbox checked /><a href="#">同意使用协议</a></label
                    >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/#/login"> 登录 </a>
                </div>
                <el-col :span="16" :offset="2">
                    <el-button type="primary" class="register-btn" @click="registerBtn">注 册</el-button>
                </el-col>
            </el-row>
        </el-dialog>
        <!-- 添加生成密钥弹出框 -->
        <el-dialog :visible.sync="keyDialogVisible" title="系统为您自动生成的RSA密钥对为：">
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

    </div>
</template>

<script>
// import userPhoto from './UpLoadPhoto';
import { register } from '@/api/login';
import md5 from 'js-md5';
import rules from '@/utils/rules';
import {generate} from '@/api/Key';

export default {
    // components: {
    //     userPhoto
    // },
    data: function () {
        return {
            rules,
            user: {
                // userName: '',
                // userPhone: '',
                // email: '',
                // password: '',
                // sex: ''
            },
            temPsw: '',
            loading: false,
            keyDialogVisible: false,
            keyPair: {
                publicKey: '',
                privateKey: '',
            }
        };
    },
    methods: {
        registerBtn() {
            this.$refs.user.validate((check) => {
                if (check) {
                    this.loading = this.$loading({
                        lock: true,
                        text: '注册中...',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    this.temPsw = this.user.password;
                    this.user.password = md5(this.user.password); // 密码加密
                    register(this.user)
                        .then((res) => {
                            if (res.success) {
                                this.generateKeyPair();
                                let keyOwner = this.user.userName;
                                generate(keyOwner);
                                this.loading.close();

                                // this.$confirm('注册成功,是否立即跳转到登录?', '信息', {
                                //     type: 'success'
                                // }).then(() => {
                                //     this.$router.push('/login');
                                // });
                            } else {
                                this.user.password = this.temPsw;
                                this.$message(res.msg);
                                this.loading.close();
                            }
                        })
                        .catch((e) => {
                            this.$message.error('服务器歇菜了,请稍后重试!!!');
                            this.loading.close();
                        });
                }
            });
        },
        async generateKeyPair() {
            let keyOwner = this.user.userName; // 假设你将用户名存储在localStorage的'username'键下
            try {
                let res = await generate(keyOwner); // 调用后端接口生成密钥对
                console.log("res:"+res)
                console.log("res.data:"+res.data)
                console.log("res && res.data:"+res && res.data)
                if (res) { // 如果请求成功
                    this.keyPair.publicKey = res.publicKey; // 保存公钥
                    this.keyPair.privateKey = res.privateKey; // 保存私钥
                    console.log("公钥：", this.keyPair.publicKey);
                    this.keyDialogVisible = true; // 显示弹出框
                } else {

                    this.$message({
                        message: '未知错误',
                        type: 'error',
                    });
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
                confirmButtonText: '我已知晓',
                type: 'warning'
            }).then(() => {
                // 创建一个 Blob 对象，内容是私钥
                let blob = new Blob([this.keyPair.privateKey], {type: "text/plain;charset=utf-8"});

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

                this.$confirm('注册成功,是否立即跳转到登录?', '信息', {
                    type: 'success'
                }).then(() => {
                    this.$router.push('/login');
                });
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '下载失败',
                });
            });
        }
    }
};
</script>

<style  >
.tip {
    margin-left: 102px;
    font-size: 18px;
    color: #000;
}
.ms-title {
    width: 100%;
    line-height: 60px;
    text-align: center;
    font-size: 25px;
    font-family: cursive;
    /* color: #fff; */
    color: #000;
    border-bottom: 1px solid #ddd;
}
.form {
    margin-top: 20px;
    text-align: center;
}
.lableColor .el-form-item__label {
    color: #000;
    font-size: 18px;
}
.register-wrap {
    position: relative;
    width: 100%;
    height: 100%;
    background: rgba(105, 103, 103, 0.288);
    background-image: url(../../../assets/img/login-bg.jpg);
    background-size: 100%;
}

.ms-register {
    position: absolute;
    right: 10%;
    top: 10%;
    bottom: 10%;
    width: 35%;
    color: #000;
    /* height: 1000px; */
    /* margin: -190px 0 0 -175px; */
    border-radius: 20px;
    background: rgba(255, 255, 255, 0.7);
    overflow: hidden;
}
.ms-content {
    padding: 30px 30px;
}
.register-btn {
    width: 300px;
    margin-left: 10px;
    text-align: center;
    border-radius: 10px;
    height: 50px;
    margin-top: 20px;
}

.register-tips {
    font-size: 12px;
    line-height: 30px;
    color: #fff;
}

.dialog-footer {
    display: flex;
    justify-content: center;
}
</style>