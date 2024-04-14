<template>
    <div id="info">
        <el-row>
            <el-col :span="12">
                <el-card class="box-card" style="height: 550px" shadow="hover">
                    <div slot="header" class="clearfix">
                        <span style="margin-left: 40%">我的头像</span>
                    </div>
                    <div v-if="isEdit" style="margin: 15% 0 0 28%">
                        <!-- <pre>↓点击图片即可进行更换头像↓</pre> -->
                        <userPhoto ref="photo" :serverPath="'http://localhost:9527/upload/photo'" v-model="user.photo" />
                    </div>
                    <div class="text item" v-if="!isEdit">
                        <el-image
                            style="width: 300px; height: 300px; margin: 10% 0 0 23%; border-radius: 150px"
                            :src="user.photo"
                            :preview-src-list="[user.photo, user.photo]"
                        >
                        </el-image>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="12">
                <el-card class="box-card" style="height: 550px" shadow="hover">
                    <div slot="header" class="clearfix">
                        <span style="margin-left: 40%">基本信息</span>
                    </div>
                    <el-row>
                        <el-form ref="user" :model="user" label-width="100px" style="margin-top: 100px; margin-left: 20%">
                            <el-col :span="16">
                                <el-form-item label="用户名:" prop="userName" :rules="rules.userName">
                                    <el-input v-if="isEdit" v-model="user.userName" placeholder="请输入用户名"></el-input>
                                    <span v-else>{{ user.userName }}</span>
                                </el-form-item>
                                <el-form-item label="性别:" prop="sex" :rules="rules.pleaseSelect">
                                    <el-select v-model="user.sex" placeholder="请选择性别" v-if="isEdit" style="width: 100%">
                                        <el-option key="1" label="男" value="1"></el-option>
                                        <el-option key="0" label="女" value="0"></el-option>
                                    </el-select>
                                    <span v-else>{{ user.sex === '1' ? '男' : user.sex === '0' ? '女' : user.sex }}</span>
                                </el-form-item>
                                <el-form-item label="手机号:" prop="userPhone" :rules="rules.phone">
                                    <el-input v-model="user.userPhone" v-if="isEdit" placeholder="请输入手机号"></el-input>
                                    <span v-else>{{ user.userPhone }}</span>
                                </el-form-item>
                                <el-form-item label="邮箱:" prop="email" :rules="rules.email">
                                    <el-input type="email" v-model="user.email" v-if="isEdit" placeholder="请输入邮箱"></el-input>
                                    <span v-else>{{ user.email }}</span>
                                </el-form-item>
                            </el-col>
                        </el-form>
                    </el-row>
                    <div style="margin-left: 30%">
                        <el-button @click="editbtn(isEdit)">{{ isEdit ? '取 消' : '编 辑' }}</el-button>
                        <el-button type="primary" v-if="isEdit" @click="save">确 定</el-button>
                    </div>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>
<script>
import userPhoto from '../myComponents/UpLoadPhoto';
import { edit } from '@/api/user';
import rules from '@/utils/rules';
export default {
    components: {
        userPhoto
    },
    created() {
        this.user.userName = localStorage.getItem('ms_username');
        // this.user.isAdmin = localStorage.getItem('isAdmin'); //管理员 or 教师
        this.user.userId = localStorage.getItem('userId');
        this.user.userPhone = localStorage.getItem('phone');
        this.user.photo = localStorage.getItem('photo');
        this.user.email = localStorage.getItem('email');
        this.user.sex = localStorage.getItem('sex');
    },
    watch: {
        isEdit() {
            this.$nextTick(() => {
                this.$refs['user'].clearValidate();
            });
        }
    },
    data() {
        return {
            rules,
            isEdit: false,
            user: {
                userName: '',
                userPhone: '',
                photo: '',
                email: '',
                sex: ''
            }
        };
    },
    methods: {
        editbtn(isEdit) {
            this.isEdit = !isEdit;
        },
        save() {
            this.$refs['user'].validate((check) => {
                if (check) {
                    var sex = this.user.sex;
                    this.user.sex = sex === '男' ? 1 : sex === '女' ? 0 : sex;
                    this.editUser();
                }
            });
        },
        editUser() {
            if (this.user.photo.indexOf('http') >= 0) {
                this.user.photo = this.user.photo.substring(22, this.user.photo.length);
            }
            edit(this.user)
                .then((res) => {
                    this.userVisible = false;
                    if (res) {
                        // this.$message.success(`修改成功`);
                        this.$alert('请重新登录', '个人信息已修改~', {
                            type: 'success',
                            confirmButtonText: '确定',
                            callback: (action) => {
                                localStorage.removeItem('ms_username');
                                this.$router.push('/login');
                            }
                        });
                    } else {
                        this.$message.warning(`修改失败`);
                    }
                })
                .catch(() => {
                    this.$message.error(`服务器歇菜了,请稍后再试~!!`);
                });
        }
    }
};
</script>