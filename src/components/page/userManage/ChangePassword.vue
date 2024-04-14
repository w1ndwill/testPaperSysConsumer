<template>
    <div class="mian">
        <el-tabs style="height: 600px" type="border-card">
            <el-tab-pane>
                <span slot="label"><i class="el-icon-edit"></i> 修改密码</span>
                <el-card shadow="hover" style="height: 508px">
                    <el-form :model="change" class="myForm" ref="change">
                        <h2>修改密码</h2>
                        <el-form-item class="input-width" label="旧密码:" prop="oldPassword" :rules="rules.oldPassword">
                            <el-input placeholder="请输入旧密码" type="password" v-model="change.oldPassword"></el-input>
                        </el-form-item>
                        <el-form-item class="input-width" label="新密码:" prop="newPassword" :rules="rules.newPassword">
                            <el-input placeholder="请输入新密码" type="password" v-model="change.newPassword"></el-input>
                        </el-form-item>
                        <el-form-item class="input-width" label="确认密码:" prop="rePassword" :rules="myRules.rePassword">
                            <el-input placeholder="请确认新密码" type="password" v-model="change.rePassword"></el-input>
                        </el-form-item>
                        <el-button @click="changPassword" class="btn-width" type="primary">修改</el-button>
                        <el-button class="btn-width" type="reset" @click="reSetInput">清空</el-button>
                    </el-form>
                </el-card>
            </el-tab-pane>
            <el-tab-pane>
                <span slot="label"><i class="el-icon-edit"></i><i class="el-icon-lx-mobilefill"></i> 重置密码</span>
                <!-- 使用重置密码组件 -->
                <smsChangePassword :isReadonly="true" :phone="change.phone" :userId="change.userId" :userName="change.userName" />
            </el-tab-pane>
        </el-tabs>
    </div>
</template>
<script>
import { changePsw } from '@/api/login';
import md5 from 'js-md5';
//引入短信发送重置密码页面
import smsChangePassword from '../myComponents/sendSmsChangePassword';
import rules from '@/utils/rules';

export default {
    components: {
        //使用该页面(已经变成组件了)
        smsChangePassword
    },
    data() {
        var checkRePassword = (rule, value, callback) => {
            if (this.change.newPassword.length === 0) {
                this.$refs.change.validateField('newPassword');
                callback();
            } else if (this.change.newPassword !== this.change.rePassword) {
                callback(new Error('两次密码不一致!!!'));
            } else {
                callback();
            }
        };
        return {
            rules,
            active: 0,
            change: {
                userId: localStorage.getItem('userId'),
                phone: localStorage.getItem('phone'),
                userName: localStorage.getItem('ms_username'),
                oldPassword: '',
                newPassword: '',
                rePassword: ''
            },
            changeMD5: {
                userId: localStorage.getItem('userId'),
                oldPassword: '',
                newPassword: '',
                rePassword: ''
            },
            myRules: {
                rePassword: { required: true, validator: checkRePassword, trigger: 'change' }
            }
        };
    },
    methods: {
        next() {
            if (this.active++ > 2) this.active = 0;
        },
        reSetInput() {
            this.$nextTick(() => {
                this.change.oldPassword = '';
                this.change.newPassword = '';
                this.change.rePassword = '';
                setTimeout(() => {
                    this.$refs.change.clearValidate();
                }, 10);
            });
        },
        changPassword() {
            this.$refs['change'].validate((check) => {
                if (check) {
                    (this.changeMD5.oldPassword = md5(this.change.oldPassword)),
                        (this.changeMD5.newPassword = md5(this.change.newPassword)),
                        (this.changeMD5.rePassword = md5(this.change.rePassword));
                    changePsw(this.changeMD5)
                        .then((res) => {
                            if (res.success) {
                                this.$message.success('修改成功~');
                                this.$notify({
                                    title: '温馨提示',
                                    message: '密码已修改，请牢记你的密码',
                                    duration: 0,
                                    type: 'success'
                                });
                                this.$alert('请重新登录', '密码已修改~', {
                                    type: 'success',
                                    confirmButtonText: '确定',
                                    callback: (action) => {
                                        localStorage.removeItem('ms_username');
                                        this.$router.push('/login');
                                    }
                                });
                            } else {
                                this.$message.error(res.msg);
                            }
                        })
                        .catch(() => {
                            this.$message.error('服务器歇菜了，请稍后重试！！');
                        });
                } else {
                    this.$message.warning('请正确填好数据!');
                }
            });
        }
    }
};
</script>
<style scoped>
.myForm {
    text-align: center;
    /* margin-top: 100px; */
}

.input-width {
    width: 40%;
    margin-left: 30%;
}

.btn-width {
    width: 150px;
    margin: 10px;
}
</style>
