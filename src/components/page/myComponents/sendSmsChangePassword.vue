<template>
    <div>
        <el-steps :active="active" finish-status="success" align-center>
            <el-step icon="el-icon-lx-mobilefill" title="验证手机号"></el-step>
            <el-step icon="el-icon-lx-comment" title="发送验证码"></el-step>
            <el-step icon="el-icon-finished" title="重置密码"></el-step>
        </el-steps>
        <el-card class="box-card" style="height: 450px" shadow="hover">
            <div slot="header" class="clearfix" style="height: 20px">
                <el-button style="float: left; padding: 3px 0; font-size: 20px" type="text" @click="last"
                    ><i class="el-icon-back"></i>上一步</el-button
                >
                <el-button
                    style="float: right; padding: 3px 0; font-size: 20px"
                    type="text"
                    @click="next"
                    :disabled="nextDisable"
                    id="nextBtn"
                    >下一步<i class="el-icon-right"></i
                ></el-button>
            </div>
            <div v-if="active === 0">
                <el-form class="myForm" ref="change" :model="change">
                    <h2>确认信息</h2>
                    <el-form-item label="用户名:" class="input-width" prop="userName">
                        <el-input
                            type="text"
                            :readonly="info.isReadonly"
                            v-model="info.userName"
                            placeholder="请输入需重置的用户名"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="手机号:" class="input-width" prop="phone">
                        <el-input
                            type="text"
                            :readonly="info.isReadonly"
                            maxlength="11"
                            v-model="info.phone"
                            placeholder="请输入关联手机号"
                        ></el-input>
                    </el-form-item>
                </el-form>
            </div>
            <div v-if="active === 1">
                <el-form class="myForm">
                    <h2>发送验证码</h2>
                    <el-form-item label="手机号:" class="input-width-validate" prop="phone">
                        <el-input type="text" readonly v-model="info.phone" placeholder="请输入关联手机号"></el-input>
                    </el-form-item>
                    <el-button class="btn-width-sms" type="primary" :disabled="btnDisabled" @click="sendSmsBtn">{{ btnText }}</el-button>
                    <el-form-item label="验证码:" class="input-width-validate" prop="validate">
                        <el-input type="text" v-model="info.validate" placeholder="请输入6位验证码"></el-input>
                    </el-form-item>
                    <el-button class="btn-width-sms" type="primary" @click="checkValidateBtn">验证</el-button>
                </el-form>
            </div>
            <div v-if="active === 2">
                <el-form :model="change" class="myForm" ref="change">
                    <h2>重置密码</h2>
                    <el-form-item class="input-width" label="新密码:" prop="newPassword">
                        <el-input placeholder="请输入新密码" type="password" v-model="info.newPassword"></el-input>
                    </el-form-item>
                    <el-form-item class="input-width" label="确认密码:" prop="rePassword">
                        <el-input placeholder="请确认新密码" type="password" v-model="info.rePassword"></el-input>
                    </el-form-item>
                    <el-button @click="reSetPassword" class="btn-width" type="primary">修改</el-button>
                    <el-input class="btn-width" type="reset">重置</el-input>
                </el-form>
            </div>
            <div v-if="active === 3" class="myForm" style="color: #2de45a; margin-top: 100px">
                <h1>重置成功~</h1>
            </div>
        </el-card>
    </div>
</template>
<script>
import { sendSms, checkValidate, reSetPsw } from '@/api/user';
import CryptoJS from 'crypto-js'; // 导入 CryptoJS 库
import md5 from 'js-md5';
export default {
    props: {
        userName: String,
        phone: String,
        isReadonly: Boolean,
        userId: String
    },

    data() {
        // var checkPhone = (rule, value, callback) => {
        //     if (value === '') {
        //         callback(new Error('请输入手机号'));
        //     } else {
        //         callback();
        //     }
        // };
        return {
            active: 0,
            btnText: '发送验证码',
            btnDisabled: false,
            nextDisable: false,
            totalTime: 60, // 秒
            info: {
                userName: this._props.userName,
                phone: this._props.phone,
                isReadonly: this._props.isReadonly,
                validate: '',
                userId: this._props.userId,
                newPassword: '',
                rePassword: ''
            },
            change: {}
            // rules: {
            //     userName: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
            //     phone: [{ validator: checkPhone, trigger: 'blur' }]
            // }
        };
    },

    methods: {
        /**
         * 下一步
         */
        next() {
            if (this.active > 3) {
                return;
            }
            if (this.active === 0) {
                if (this.info.userName === undefined || this.info.userName.length === 0) {
                    this.$message.warning('请输入用户名');
                    return;
                }
                var reg = /^1[3|4|5|7|8][0-9]{9}$/; //验证规则
                if (!reg.test(this.info.phone)) {
                    this.$message.warning('请输入正确的手机号');
                    return;
                }
            }
            this.active++;
            this.nextDisable = true;
        },
        /**
         * 上一步
         */
        last() {
            if (--this.active < 0) this.active = 0;
            if (this.active === 0) {
                this.nextDisable = false;
            }
            this.totalTime = -1;
            setTimeout(() => {
                this.btnText = '发送验证码';
            }, 1100);
        },
        sendSmsBtn() {
            this.btnDisabled = true;
            this.btnText = `${this.totalTime}s后重新发送`;
            sendSms(this.info)
                .then((res) => {
                    if (res.success) {
                        this.$message.success('短信发送成功,请注意查收~');
                    } else {
                        this.$message.warning(res.msg);
                    }
                })
                .catch(() => {
                    this.$message.error('服务器歇菜了，请稍后重试！！');
                });
            let clock = window.setInterval(() => {
                this.totalTime--;
                this.btnText = `${this.totalTime}s后重新发送`;
                if (this.totalTime < 0) {
                    //当倒计时小于0时清除定时器
                    window.clearInterval(clock); //关闭
                    this.btnText = '重新发送验证码';
                    this.totalTime = 60;
                    this.btnDisabled = false;
                }
            }, 1000);
        },
        checkValidateBtn() {
            if (this.info.validate.length === 6) {
                checkValidate(this.info)
                    .then((res) => {
                        if (res) {
                            this.nextDisable = false;
                            this.$message.success('验证码正确');
                            setTimeout(() => {
                                document.getElementById('nextBtn').click();
                            }, 100);
                        } else {
                            this.nextDisable = true;
                            this.$message.warning('验证码错误,请重新输入!!');
                        }
                    })
                    .catch(() => {
                        this.$message.error('服务器歇菜了,请稍后再试!!');
                    });
            } else {
                this.nextDisable = true;
                this.$message.warning('请输入6位验证码~');
            }
        },
        reSetPassword() {
            if (this.info.newPassword.length === 0) {
                this.$message.warning('请输入密码');
                return;
            }
            if (this.info.rePassword !== this.info.newPassword) {
                this.$message.warning('两次密码不一致');
                return;
            }
            this.info.newPassword = CryptoJS.SHA256(this.info.newPassword).toString();
            this.info.rePassword = CryptoJS.SHA256(this.info.rePassword).toString();
            reSetPsw(this.info)
                .then((res) => {
                    this.active = 3;
                    this.info.newPassword = '';
                    this.info.rePassword = '';
                    if (res.success) {
                        this.$notify({
                            title: '温馨提示',
                            message: '密码已修改，请牢记你的密码',
                            duration: 0,
                            type: 'success'
                        });
                    } else {
                        this.$message.warning(res.msg);
                    }
                })
                .catch(() => {
                    this.$message.error('服务器歇菜了,请稍后再试!!');
                });
        }
    }
};
</script>
<style  scoped>
.myForm {
    text-align: center;
    /* margin-top: 70px; */
    margin-bottom: 70px;
}
.input-width {
    width: 40%;
    margin-left: 30%;
}
.input-width-validate {
    margin-left: 30%;
    width: 25%;
    float: left;
}
.btn-width-sms {
    width: 15%;
    float: left;
    margin-top: 32px;
    /* margin: 10px; */
}
.btn-width {
    width: 150px;
    margin: 10px;
}
</style>