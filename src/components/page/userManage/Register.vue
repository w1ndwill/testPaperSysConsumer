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
    </div>
</template>

<script>
// import userPhoto from './UpLoadPhoto';
import { register } from '@/api/login';
import md5 from 'js-md5';
import rules from '@/utils/rules';
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
            loading: false
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
                    this.user.password = md5(this.user.password);
                    register(this.user)
                        .then((res) => {
                            if (res.success) {
                                this.loading.close();
                                this.$confirm('注册成功,是否立即跳转到登录?', '信息', {
                                    type: 'success'
                                }).then(() => {
                                    this.$router.push('/login');
                                });
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
</style>