<template>
    <div class="login-wrap">
        <el-dialog v-dialogDrag :visible="true" width="30%" custom-class='my-dailog' :show-close="false" :modal="false" :before-close="()=>{return false}">
            <!-- <div class="ms-login"> -->
                <div class="ms-title">试卷管理系统</div>
                <el-form :model="param" :rules="rules" ref="login" label-width="0px" class="ms-content">
                    <el-form-item prop="userPhone">
                        <el-input v-model="param.userPhone" placeholder="请输入手机号" @keyup.enter.native="submitForm()">
                            <el-button slot="prepend" icon="el-icon-lx-people"></el-button>
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input type="password" show-password placeholder="请输入密码" v-model="param.password"  @keyup.enter.native="submitForm()">
                            <el-button slot="prepend" icon="el-icon-lx-lock"></el-button>
                        </el-input>
                    </el-form-item>
                    <el-form-item >
                        <el-row style="left: 70px">
                            <!-- <el-col style="width: 70px">
                                <label for="rememberMe">记住密码</label>
                                <el-checkbox v-model="param.rememberMe" />
                            </el-col> -->
                            <el-col style="width: 40px; margin-left: 30px">
                                <a href="/#/register"> 注册? </a>
                            </el-col>
                            <el-col style="width: 70px; margin-left: 40px">
                                <a href="#" @click="()=> reSetPassVisible = true"> 忘记密码? </a>
                            </el-col>
                        </el-row>
                    </el-form-item>
                    <div class="login-btn">
                        <el-button type="primary"   @click="submitForm()">登录</el-button>
                    </div>
                    <!-- <p class="login-tips">Tips : 用户名和密码随便填。</p> -->
                </el-form>
            <!-- </div> -->
        </el-dialog>
        <el-dialog :title="'重置密码'" :visible.sync="reSetPassVisible" width="80%" center  top="20px" >
        <smsChangePassword/>
        </el-dialog>
    </div>
</template>

<script>
import { login } from '@/api/login';
import md5 from 'js-md5';
import {baseURL} from '@/utils/request';
import smsChangePassword from '../myComponents/sendSmsChangePassword'
export default {
      components:{
    smsChangePassword
  },
    data: function () {
        return {
            loading: false,
            visible: true,
            reSetPassVisible:false,
            param: {
                userPhone: '17877184843',
                password: 'qq2536',
                // rememberMe: true
            },
            paramMD5: {
                userPhone: '',
                password: '',
                // rememberMe: ''
            },
            rules: {
                userPhone: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
                password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
            }
        };
    },
    methods: {
        submitForm() {
            this.$refs.login.validate((check) => {
                //调用上面的rules检验
                if (check) {
                    //登录
                    this.loading = this.$loading({
                        lock: true,
                        text: '登陆中...',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    this.paramMD5.password = md5(this.param.password);
                    this.paramMD5.userPhone = this.param.userPhone;
                    // this.paramMD5.rememberMe = this.param.rememberMe;
                    login(this.paramMD5)
                        .then((res) => {
                            
                            if (res) {
                                setTimeout(() => {
                                    this.loading.close();
                                }, 500);
                                //登录成功
                                localStorage.setItem('ms_username', res.userName);
                                localStorage.setItem('isAdmin', res.isAdmin); //管理员 or 教师
                                localStorage.setItem('userId', res.userId);
                                localStorage.setItem('phone', res.userPhone);
                                localStorage.setItem('photo', baseURL()+res.photo);
                                localStorage.setItem('email', res.email);
                                localStorage.setItem('sex', res.sex);
                                this.$router.push('/home');
                                this.$message.success('登录成功');
                            } else {
                                this.loading.close();
                                //登录失败
                                this.$message.error('用户名或者密码错误!!!');
                            }
                        })
                        .catch(() => {
                            this.loading.close();
                            this.$message.error('服务器歇菜了,请稍后重试!!!');
                        });
                } else {
                    this.$message.error('请输入账号和密码');
                    return false;
                }
            });
        } //,
        // checkuserPhone() {
        //     console.log('userPhone ==>', this.param.userPhone);
        //     return true;
        // },
        // checkPassword() {
        //     console.log('Password ==>', this.param.password);
        //     return true;
        // }
    }
};
</script>

<style scoped>
.my-dailog{
    border-radius: 100px;
}
.login-wrap {
    position: relative;
    width: 100%;
    height: 100%;
    background-image: url(../../../assets/img/login-bg.jpg);
    background-size: 100%;
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
.ms-login {
    position: absolute;
    left: 50%;
    top: 50%;
    width: 350px;
    margin: -190px 0 0 -175px;
    border-radius: 5px;
    background: rgba(255, 255, 255, 0.7);
    overflow: hidden;
}
.ms-content {
    padding: 30px 30px;
}
.login-btn {
    text-align: center;
}
.login-btn button {
    width: 100%;
    height: 36px;
    margin-bottom: 10px;
}
.login-tips {
    font-size: 12px;
    line-height: 30px;
    color: #fff;
}
</style>