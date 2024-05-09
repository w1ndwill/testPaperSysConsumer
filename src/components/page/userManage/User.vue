<template>
    <div>
        <!-- <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item> <i class="el-icon-lx-cascades"></i> 用户数据管理 </el-breadcrumb-item>
            </el-breadcrumb>
        </div> -->
        <div class="container">
            <div class="handle-box">
                <el-radio-group v-model="showType" class="handle-del mr10">
                    <el-radio-button label="列表"></el-radio-button>
                    <el-radio-button label="卡片"></el-radio-button>
                </el-radio-group>
                <el-button type="primary" icon="el-icon-lx-add" class="handle-del mr10" @click="addUserDialog">添加用户</el-button>
                <el-button type="warning" icon="el-icon-delete" v-if="showType === '列表'" class="handle-del mr10" @click="delAllSelection"
                    >批量删除</el-button
                >
                <el-select v-model="query.isAdmin" placeholder="用户类型" @change="handleSearch" class="handle-select mr10" clearable>
                    <el-option key="admin" label="管理员" value="Y"></el-option>
                    <el-option key="user" label="教师" value="N"></el-option>
                </el-select>
                <el-select v-model="query.sex" placeholder="性别" @change="handleSearch" class="handle-select mr10" clearable>
                    <el-option key="nan" label="男" value="1"></el-option>
                    <el-option key="nv" label="女" value="0"></el-option>
                </el-select>
                <el-input
                    v-model="query.userName"
                    placeholder="用户名"
                    @keyup.enter.native="handleSearch"
                    class="handle-input mr10"
                ></el-input>
                <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
                <el-pagination
                    background
                    class="pagination-card"
                    v-if="showType === '卡片'"
                    layout="total, prev, pager, next, jumper"
                    :current-page="query.page"
                    :page-size="query.pageSize"
                    :total="pageTotal"
                    @current-change="handlePageChange"
                    @size-change="handleSizeChange"
                ></el-pagination>
            </div>
            <el-table
                :data="tableData"
                v-loading="loading"
                v-if="showType === '列表'"
                border
                class="table"
                ref="multipleTable"
                header-cell-class-name="table-header"
                @selection-change="handleSelectionChange"
            >
                <el-table-column type="selection" width="55" align="center" checked="true"></el-table-column>
                <el-table-column type="index" width="55" align="center" label="序号"></el-table-column>
                <el-table-column label="用户头像" align="center" width="100">
                    <template slot-scope="scope">
                        <el-image class="table-td-thumb" :src="scope.row.photo" :preview-src-list="[scope.row.photo]">
                            <div slot="error" class="image-slot">
                                <!-- <i class="el-icon-picture-outline"></i> -->
                                <!-- <img src="../../assets/img/img.jpg" class="user-avator" alt /> -->
                                <!--加载失败时候就加载默认图标, -->
                                <i class="el-icon-s-custom"></i>
                            </div>
                        </el-image>
                    </template>
                </el-table-column>
                <el-table-column :show-overflow-tooltip="true" prop="userName" label="用户名" width="80" align="center"></el-table-column>
                <el-table-column label="用户类型" align="center" width="80">
                    <template slot-scope="scope" align="center">
                        <el-tag :type="scope.row.isAdmin === '管理员' ? '' : scope.row.isAdmin === '教师' ? 'success' : ''" width="10">{{
                            scope.row.isAdmin
                        }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="职位" align="center" width="100">
                    <template slot-scope="scope" align="center">
                        <el-tag :type="scope.row.job === '1' ? '' : scope.row.job === '2' ? 'success' : 'warning'">
                            {{
                                scope.row.job === 1 ? '普通教师' :
                                    scope.row.job === 2 ? '系主任' :
                                        '院长'
                            }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="sex" label="性别" align="center" width="50"></el-table-column>
                <el-table-column :show-overflow-tooltip="true" prop="userPhone" label="手机号" align="center"></el-table-column>
                <el-table-column :show-overflow-tooltip="true" prop="email" label="邮箱" align="center"></el-table-column>
                <!-- <el-table-column label="账户余额">
                    <template slot-scope="scope">￥{{ scope.row.money }}</template>
                </el-table-column> -->

                <!-- <el-table-column prop="address" label="地址"></el-table-column> -->
                <!-- <el-table-column label="用户状态" align="center" width="80">
                    <template slot-scope="scope">
                        <el-tag :type="scope.row.deleted === '正常' ? 'success' : scope.row.deleted === '已删除' ? 'danger' : ''">{{
                            scope.row.deleted
                        }}</el-tag>
                    </template>
                </el-table-column> -->
                <!-- <el-table-column prop="date" label="注册时间"></el-table-column> -->

                <el-table-column label="操作" width="120" align="center">
                    <template slot-scope="scope">
                        <el-dropdown
                            ><!-- trigger="click" -->
                            <span class="el-dropdown-link">
                                操作<i class="el-icon-lx-qrcode"></i><i class="el-icon-caret-bottom el-icon--right"></i>
                            </span>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item class="clearfix">
                                    <el-button
                                        type="primary"
                                        icon="el-icon-edit"
                                        @click="handleEdit(scope.$index, scope.row)"
                                        class="btn-width"
                                        >编辑</el-button
                                    >
                                </el-dropdown-item>
                                <el-dropdown-item class="clearfix">
                                    <el-button
                                        type="warning"
                                        icon="el-icon-delete"
                                        @click="handleDelete(scope.$index, scope.row)"
                                        class="btn-width"
                                        >删除</el-button
                                    >
                                </el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination
                    background
                    v-if="showType === '列表'"
                    layout="total, sizes, prev, pager, next, jumper"
                    :current-page="query.page"
                    :page-size="query.pageSize"
                    :page-sizes="[5, 7, 10, 15, 20, 40, 100]"
                    :total="pageTotal"
                    @current-change="handlePageChange"
                    @size-change="handleSizeChange"
                ></el-pagination>
            </div>
            <!-- 显示卡片 -->
            <div class="container my-container" v-if="showType === '卡片'">
                <el-card class="box-card my-card" v-for="(user, index) in tableData" :key="index" shadow="hover">
                    <div slot="header" class="clearfix">
                        <span
                            >用户类型：
                            <el-tag :type="user.isAdmin === '管理员' ? '' : user.isAdmin === '教师' ? 'success' : ''" width="10">{{
                                user.isAdmin
                            }}</el-tag>
                        </span>
                        <el-button
                            style="float: right; padding: 3px 0; width: 30px; height: 30px"
                            type="warning"
                            icon="el-icon-delete"
                            @click="handleDelete(index, user)"
                            class="btn-width"
                        ></el-button>
                        <el-button
                            style="float: right; padding: 3px 0; width: 30px; height: 30px"
                            type="primary"
                            icon="el-icon-edit"
                            @click="handleEdit(index, user)"
                            class="btn-width"
                        ></el-button>
                    </div>
                    <div class="text item">
                        <el-row>
                            <el-col :span="14">
                                <el-form ref="userCard" label-width="58px">
                                    <el-form-item label="用户名:">
                                        <span>{{ user.userName }}</span>
                                    </el-form-item>
                                    <el-form-item label="性别:">
                                        <span>{{ user.sex === '1' ? '男' : user.sex === '0' ? '女' : user.sex }}</span>
                                    </el-form-item>
                                    <el-form-item label="手机号:">
                                        <span>{{ user.userPhone }}</span>
                                    </el-form-item>
                                    <el-form-item label="邮箱:">
                                        <span>{{ user.email }}</span>
                                    </el-form-item>
                                </el-form>
                            </el-col>
                            <el-col :span="10">
                                <el-image class="card-img" :src="user.photo" :preview-src-list="[user.photo]">
                                    <div slot="error" class="image-slot">
                                        <!-- <i class="el-icon-picture-outline"></i> -->
                                        <img src="../../../assets/img/img.jpg" class="card-img" alt />
                                        <!--加载失败时候就加载默认图标, -->
                                        <!-- <i class="el-icon-s-custom"></i> -->
                                    </div>
                                </el-image>
                            </el-col>
                        </el-row>
                    </div>
                </el-card>
            </div>
        </div>

        <!-- user弹出框 -->
        <el-dialog v-dialogDrag :title="title" :visible.sync="userVisible" width="45%" center :close-on-click-modal="false">
            <el-row>
                <el-form ref="user" :model="user" label-width="100px">
                    <el-col :span="16">
                        <el-form-item label="用户名:" prop="userName" :rules="rules.Required">
                            <el-input v-model="user.userName" placeholder="请输入用户名"></el-input>
                        </el-form-item>
                        <el-form-item label="性别:" prop="sex" :rules="rules.pleaseSelect">
                            <el-select v-model="user.sex" placeholder="请选择性别" style="width: 100%">
                                <el-option key="1" label="男" value="1"></el-option>
                                <el-option key="0" label="女" value="0"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="密码:" prop="password" :rules="title === '添加用户' ? rules.password : rules.reSetPassword">
                            <el-input
                                type="password"
                                :placeholder="title === '添加用户' ? '请输入密码' : '重置密码'"
                                v-model="user.password"
                                show-password
                            ></el-input>
                        </el-form-item>
<!--                        <el-form-item label="用户权限:" prop="isAdmin" :rules="rules.pleaseSelect">-->
<!--                            <el-select v-model="user.isAdmin" placeholder="请选择用户职位" style="width: 100%" clearable>-->
<!--                                <el-option key="Y" label="管理员" value="Y"></el-option>-->
<!--                                <el-option key="N" label="教师" value="N"></el-option>-->
<!--                            </el-select>-->
<!--                        </el-form-item>-->
                        <el-form-item label="用户职位:" prop="job" :rules="rules.pleaseSelect" v-if ="this.currentjob - user.job === 1 || 0">
                            <el-select v-model="user.job" placeholder="请选择用户职位" style="width: 100%" clearable>
                                <el-option v-if="user.job === 3 || user.job === 2" key=3 label="院长" value=3></el-option>
                                <el-option v-if="user.job === 2 || user.job === 1" key=2 label="系主任" value=2></el-option>
                                <el-option v-if="user.job === 1 || user.job === 2" key=1 label="教师" value=1></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="手机号:" prop="userPhone" :rules="rules.phone">
                            <el-input
                                v-model="user.userPhone"
                                maxlength="11"
                                :disabled="title === '编辑用户'"
                                placeholder="请输入手机号"
                            ></el-input>
                        </el-form-item>
                        <el-form-item label="邮箱:" prop="email" :rules="rules.email">
                            <el-input type="email" v-model="user.email" placeholder="请输入邮箱"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item prop="photo" :rules="rules.photo" style="margin-left: -80px">
                            <userPhoto ref="photo" :serverPath="'http://localhost:9527/upload/photo'" v-model="user.photo" />
                        </el-form-item>
                    </el-col>
                </el-form>
            </el-row>

            <span slot="footer" class="dialog-footer" style="text-algin: center">
                <el-button @click="clearBtn()">取 消</el-button>
                <el-button type="primary" @click="save">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import { query, get, edit, del, add } from '@/api/user';
import userPhoto from '../myComponents/UpLoadPhoto';
import {baseURL} from '@/utils/request';
import md5 from 'js-md5';
import rules from '@/utils/rules';
import { value } from 'lodash/seq';
export default {
    components: {
        userPhoto
    },
    name: 'userTable',
    data() {
        return {
            rules,
            loading: true,
            showType: '列表',
            currentjob: localStorage.getItem('job'),
            query: {
                userId: localStorage.getItem('userId'),
                isAdmin: '',
                sex: '',
                userName: '',
                page: 1,
                pageSize: 7
            },
            user: {
                userId: '',
                userName: '',
                isAdmin: '',
                userPhone: '',
                photo: '',
                email: '',
                password: '',
                sex: '',
                job: ''
            },
            tableData: [],
            multipleSelection: [],
            delList: [],
            userVisible: false,
            title: '',
            pageTotal: 0,
            form: {},
            idx: -1,
            id: -1
        };
    },
    created() {
        this.getData();
    },
    watch: {
        showType(val) {
            if (val === '卡片') {
                this.query.pageSize = 9;
                this.handleSearch();
            } else {
                this.query.pageSize = 7;
                this.handleSearch();
            }
        },
        userVisible(val) {
            this.$nextTick(() => {
                this.$refs.user.clearValidate();
            });
        }
    },
    methods: {
        // 获取 easy-mock 的模拟数据
        getData() {
            this.loading = true;
            query(this.query)
                .then((res) => {
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
                    this.tableData = res.dataList;
                    this.pageTotal = res.pageTotal || 0;
                    this.tableData.forEach((user)=>{
                        user.photo = baseURL()+user.photo;
                    })
                })
                .catch(() => {
                    this.loading = false;
                    this.$message.error('服务器歇菜了,请待会再试!');
                });
        },
        // 触发搜索按钮
        handleSearch() {
            this.$set(this.query, 'page', this.query.page || 1);
            this.getData();
        },
        addUserDialog() {
            this.title = '添加用户';
            this.user = {};
            this.userVisible = true;
        },
        // 编辑操作
        handleEdit(index, row) {
            this.title = '编辑用户';
            this.idx = index;
            this.user = row;
            this.userVisible = true;
        },
        clearBtn() {
            this.handleSearch();
            this.userVisible = false;
        },
        // 保存编辑
        save() {
            this.$refs['user'].validate((check) => { //校验
                if (check) {
                    if (this.title === '添加用户') {
                        this.addUser();
                    } else if (this.title === '编辑用户') {
                        this.editUser();
                    }
                }
            });
        },
        addUser() {
            add(this.getUser(this.user))
                .then((res) => {
                    if (res.success) {
                        this.$message.success(`添加成功`);
                        this.userVisible = false;
                        this.handleSearch();
                        this.user = {};
                    } else {
                        this.$message.warning(res.msg);
                    }
                })
                .catch((err) => {
                    this.$message.error('服务器歇菜了,请稍后重试!!');
                });
        },
        getUser(user) {
            let obj = new Object();
            obj.userId = user.userId;
            obj.userName = user.userName;
            obj.userPhone = user.userPhone;
            obj.photo = user.photo;
            obj.email = user.email;
            obj.password = md5(user.password);
            obj.isAdmin = user.isAdmin;
            obj.sex = user.sex;
            obj.job = user.job;
            return obj;
        },
        editUser() {
            if (this.user.deleted === '正常') this.user.deleted = 1;
            if (this.user.job === '1') {
                this.user.isAdmin = 'N';
            }
            if (this.user.job === '2') {
                this.user.isAdmin = 'Y';
            }
            if (this.user.job === '3') {
                this.user.isAdmin = 'Y';
            }
            this.user.sex === '男' ? (this.user.sex = 1) : this.user.sex === '女' ? (this.user.sex = 0) : this.user.sex;
            if (this.user.password && this.user.password.length > 0) {
                this.user.password = md5(this.user.password);
            }
            if(this.user.photo.indexOf("http")>=0){
                this.user.photo = this.user.photo.substring(22,this.user.photo.length)
            }
            console.log('this.user==>', this.user)
            edit(this.user)
                .then((res) => {
                    this.userVisible = false;
                    if (res) {
                        // if (this.user.isAdmin === 'Y') {
                        //     this.user.isAdmin = '管理员';
                        // } else {
                        //     this.user.isAdmin = '教师';
                        // }
                        // if (this.user.deleted == '1') {
                        //     this.user.deleted = '正常';
                        // } else {
                        //     this.user.deleted = '已删除';
                        // }
                        this.$message.success(`修改成功`);
                        // this.$set(this.tableData, this.idx, this.user);
                        this.handleSearch();
                        this.user = {};
                    } else {
                        this.$message.error(`修改失败`);
                    }
                })
                .catch(() => {
                    this.$message.error(`服务器歇菜了,请稍后再试~!!`);
                });
        },
        // 删除操作
        handleDelete(index, row) {
            // 二次确认删除
            if (row.isAdmin === '管理员') {
                this.$confirm('该用户是管理员,确定要删除吗?', '警告', {
                    type: 'error'
                })
                    .then(() => {
                        this.delUser(index, row);
                    })
                    .catch((err) => {
                        console.log('delUserErr==>', err);
                    });
            } else {
                this.$confirm('确定要删除吗？', '提示', {
                    type: 'warning'
                })
                    .then(() => {
                        this.delUser(index, row);
                    })
                    .catch((err) => {
                        console.log('delUserErr==>', err);
                    });
            }
        },
        delUser(index, row) {
            del({userId:row.userId})
                .then((res) => {
                    if (res) {
                        this.tableData.splice(index, 1);
                        console.log('index==>', index);
                        this.$message.warning('删除成功');
                        this.pageTotal--;
                    } else {
                        this.$message.error('删除失败');
                    }
                })
                .catch(() => {
                    this.$message.error('删除失败');
                });
        },
        // 多选操作
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        delAllSelection() {
            const length = this.multipleSelection.length;
            if (!length || length === 0) {
                this.$message.warning('请至少选择一条数据进行删除!!');
                return;
            }
            let userId = '';
            let str = '';
            for (let i = 0; i < this.multipleSelection.length; i++) {
                str += this.multipleSelection[i].userName + ' ';
                for (let index = 0; index < this.tableData.length; index++) {
                    if (
                        this.multipleSelection[i].userId === this.tableData[index].userId &&
                        this.multipleSelection[i].userPhone === this.tableData[index].userPhone
                    ) {
                        userId += this.multipleSelection[i].userId + ',';
                        this.delList.push(index);
                    }
                }
            }
            this.user.userId = userId.substring(0, userId.length - 1);
            this.$confirm(`确定要删除${str}吗？`, '警告', {
                type: 'warning'
            }).then(() => {
                del({userId:this.user.userI})
                    .then((res) => {
                        if (res) {
                            this.$message.warning(`成功删除了${str}`);
                            // for (let index = 0; index < this.delList.length; index++) {
                            //     this.tableData.splice(this.delList[index], 1);
                            //     this.pageTotal--;
                            // }
                            this.handleSearch();
                            this.multipleSelection = [];
                            this.user = {};
                        }
                    })
                    .catch(() => {});
            });
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
        }
    }
};
</script>

<style scoped>
.pagination-card {
    position: relative;
    float: right;
}
.my-card {
    width: 31%;
    height: 300px;
    float: left;
    margin: 10px;
}
.my-container {
    height: 950px;
}
.card-img {
    border-radius: 10px;
    display: block;
    margin: auto;
    margin-top: -13px;
    margin-right: -13px;
    width: 100%;
    height: 170px;
}
.handle-box {
    margin-bottom: 20px;
}

.handle-select {
    width: 120px;
}

.btn-width {
    width: 100px;
}
.handle-input {
    width: 200px;
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
</style>
