<template>
    <div>
        <!-- <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item> <i class="el-icon-lx-cascades"></i> 题库数据管理 </el-breadcrumb-item>
            </el-breadcrumb>
        </div> -->
        <div class="container">
            <div class="handle-box">
                <el-button type="primary" icon="el-icon-lx-add" class="handle-del mr10" @click="addQueBankDialog">添加题库</el-button>
                <el-button type="warning" icon="el-icon-delete" class="handle-del mr10" @click="delAllSelection" v-if="getIsAdmin()"
                    >批量删除</el-button
                >

                <el-select v-model="query.queBankCourseId" @change="handleSearch" placeholder="按课程名查询" clearable>
                    <el-option
                        class="handle-del mr10"
                        v-for="course in courseList"
                        :key="course.courseId"
                        :label="course.courseName"
                        :value="course.courseId"
                    ></el-option>
                </el-select>
                <el-input
                    v-model="query.queBankName"
                    placeholder="题库名"
                    @keyup.enter.native="handleSearch"
                    class="handle-input mr10"
                ></el-input>
                <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
            </div>
            <el-table
                :data="tableData"
                v-loading="loading"
                border
                class="table"
                ref="multipleTable"
                header-cell-class-name="table-header"
                @selection-change="handleSelectionChange"
            >
                <el-table-column type="selection" width="55" align="center" checked="true"></el-table-column>
                <el-table-column type="index" width="55" align="center" label="序号"></el-table-column>
                <el-table-column
                    :show-overflow-tooltip="true"
                    prop="queBankName"
                    label="题库名"
                    width="120"
                    align="center"
                ></el-table-column>
                <el-table-column
                    :show-overflow-tooltip="true"
                    prop="courseName"
                    label="所属课程"
                    width="120"
                    align="center"
                ></el-table-column>
                <el-table-column :show-overflow-tooltip="true" prop="createBy" label="创建人" width="100" align="center"></el-table-column>
                <el-table-column :show-overflow-tooltip="true" prop="queBankDescribe" label="描述"></el-table-column>
                <el-table-column label="操作" width="120" align="center">
                    <template slot-scope="scope">
                        <el-dropdown
                            ><!-- trigger="click" -->
                            <span class="el-dropdown-link">
                                操作<i class="el-icon-lx-qrcode"></i><i class="el-icon-caret-bottom el-icon--right"></i>
                            </span>
                            <el-dropdown-menu slot="dropdown" v-if="getIsAdmin()">
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
                            <el-dropdown-menu slot="dropdown" v-else-if="getUserName() === scope.row.createBy">
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

        <!-- queBank弹出框 -->
        <el-dialog v-dialog-drag :title="title" :visible.sync="queBankVisible" width="50%" center :close-on-click-modal="false">
            <el-form ref="queBank" :model="queBank" label-width="100px">
                <el-form-item label="题库名:" prop="queBankName" :rules="rules.Required">
                    <el-input v-model="queBank.queBankName" placeholder="请输入题库名"></el-input>
                </el-form-item>
                <el-form-item label="所属课程:" v-if="title === '添加题库'" prop="courseId" :rules="rules.pleaseSelect">
                    <el-select
                        v-model="queBank.courseId"
                        @change="courseNameChange"
                        placeholder="请选择所属课程"
                        style="width: 100%"
                        clearable
                    >
                        <el-option
                            v-for="course in courseList"
                            :key="course.courseId"
                            :label="course.courseName"
                            :value="course.courseId"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="题库描述:">
                    <el-input type="textarea" rows="10" v-model="queBank.queBankDescribe" placeholder="请输入题库描述"></el-input>
                </el-form-item>
            </el-form>

            <span slot="footer" class="dialog-footer">
                <el-button @click="clearBtn()">取 消</el-button>
                <el-button type="primary" @click="save">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import { getCourse, query, get, edit, del, add, UntieQuestion } from '../../../api/queBank';
import power from '@/utils/power';
import rules from '@/utils/rules';

export default {
    mixins: [power],
    name: 'basetable',
    data() {
        return {
            rules,
            loading: true,
            query: {
                userId: localStorage.getItem('userId'), //当前用户ID
                queBankCourseId: '',
                queBankName: '',
                page: 1,
                pageSize: 7
            },
            queBank: {
                queBankId: '',
                queBankName: '',
                courseId: '',
                queBankDescribe: '',
                isChangeQueBankName: 'F'
            },
            temQueBank: {},
            temQueBankName: '',
            temCourseName: '',
            isUntieQuestion: false,
            tableData: [],
            multipleSelection: [],
            delList: [],
            delIdList: {
                queBankId: ''
            },
            queBankVisible: false,
            title: '',
            pageTotal: 0,
            form: {},
            idx: -1,
            id: -1,
            courseList: []
        };
    },
    created() {
        this.getData();
    },
    watch: {
        queBankVisible() {
            this.$nextTick(() => {
                this.$refs['queBank'].clearValidate();
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
                                localStorage.removeItem('ms_username'); //清除用户名
                                this.$router.push('/login');
                            }
                        });
                    }
                    let isAdmin = this.getIsAdmin(); //是否是管理员
                    let userName = this.getUserName(); //当前登录用户
                    this.tableData = res.dataList.filter((item) => { // 过滤
                        return item.createBy === userName; //创建人等于当前用户ID
                    });
                    this.pageTotal = this.tableData.length; //总数
                })
                .catch(() => {
                    this.loading = false;
                    this.$message.error('服务器歇菜了,请待会再试!');
                });

            /**
             * 获取课程列表
             */
            getCourse().then((res) => {
                this.courseList = res;
            });
        },
        // 触发搜索按钮
        handleSearch() {
            this.$set(this.query, 'page', this.query.page || 1);
            this.getData();
        },
        addQueBankDialog() {
            this.title = '添加题库';
            this.queBank = {};
            this.queBankVisible = true;
        },
        // 编辑操作
        handleEdit(index, row) {
            this.title = '编辑题库';
            this.idx = index;
            this.temQueBank = row;
            this.temQueBankName = row.queBankName;
            this.temCourseName = row.courseId;
            this.queBank = row;
            this.queBankVisible = true;
        },
        clearBtn() {
            this.handleSearch();
            this.queBankVisible = false;
        },

        // 保存编辑
        save() {
            this.$refs['queBank'].validate((chek) => { // 校验
                if (chek) {
                    if (this.title === '添加题库') {
                        this.addQueBank();
                    } else if (this.title === '编辑题库') {
                        this.editQueBank();
                    }
                }
            });
        },
        addQueBank() {
            add(this.getQueBank(this.queBank))
                .then((res) => {
                    if (res.success) {
                        this.$message.success('添加题库成功..');
                        // this.tableData.push(this.queBank);
                        // this.pageTotal++;
                        this.queBankVisible = false;
                        this.handleSearch();
                        this.queBank = {};
                    } else {
                        this.$message.warning(res.msg);
                    }
                })
                .catch((err) => {
                    this.$message.error('服务器歇菜了,请稍后重试!!');
                });
        },
        getQueBank(queBank) {
            let obj = new Object();
            obj.queBankId = queBank.queBankId; //题库ID
            obj.queBankName = queBank.queBankName; //题库名
            obj.courseId = queBank.courseId; //课程ID
            obj.queBankDescribe = queBank.queBankDescribe; //描述
            obj.createBy = queBank.createBy; //创建人
            // obj.createBy = queBank.getItem('userId'); //创建人
            return obj;
        },
        editQueBank() {
            // this.courseList.forEach((course) => {
            //     if (this.queBank.courseId === course.courseName) {
            //         this.queBank.courseId = course.courseId;
            //     }
            // });
            this.queBank.isChangeQueBankName = 'F';
            if (this.queBank.queBankName !== this.temQueBankName) {
                this.queBank.isChangeQueBankName = 'T';
            }
            /**
             *
             * 解绑
             */
            if (this.isUntieQuestion) { //解绑
                this.isUntieQuestion = false;
                this.UntieQuestion();
            }

            /**
             * 修改题库信息
             */
            edit(this.queBank)
                .then((res) => {
                    if (res.success) {
                        // this.courseList.forEach((course) => {
                        //     if (this.queBank.courseId === course.courseId) {
                        //         this.queBank.courseName = course.courseName;
                        //     }
                        // });
                        this.queBankVisible = false;
                        this.$message.success(`修改成功`);
                        // this.$set(this.tableData, this.idx, this.queBank);
                        this.handleSearch();
                        this.queBank = {};
                    } else {
                        this.queBank.queBankName = this.temQueBankName;
                        this.$message.warning(res.msg);
                    }
                })
                .catch(() => {
                    this.queBank.queBankName = this.temQueBankName;
                    this.$message.error(`服务器歇菜了,请稍后重试!!`);
                });
        },
        // 删除操作
        handleDelete(index, row) {
            // 二次确认删除
            this.$confirm('确定要删除吗？', '提示', {
                type: 'warning'
            })
                .then(() => {
                    this.delQueBank(index, row);
                })
                .catch((err) => {
                    console.log('delqueBankErr==>', err);
                });
        },
        delQueBank(index, row) {
            this.delIdList.queBankId = row.queBankId;
            del(this.delIdList)
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
                    this.$message.error('服务器歇菜了,请稍后重试!!');
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
            let queBankId = '';
            let str = '';
            console.log(this.multipleSelection);
            for (let i = 0; i < this.multipleSelection.length; i++) {
                str += this.multipleSelection[i].queBankName + ' ';
                for (let index = 0; index < this.tableData.length; index++) {
                    if (this.multipleSelection[i].queBankId === this.tableData[index].queBankId) {
                        queBankId += this.multipleSelection[i].queBankId + ',';
                        this.delList.push(index);
                    }
                }
            }
            this.queBank.queBankId = queBankId.substring(0, queBankId.length - 1);
            this.$confirm(`确定要删除这${length}个题库,并且解绑关联的试题吗？`, '警告', {
                type: 'error'
            }).then(() => {
                this.delIdList.queBankId = this.queBank.queBankId;
                del(this.delIdList)
                    .then((res) => {
                        if (res) {
                            this.$message.warning(`成功删除了${str}`);
                            // for (let index = 0; index < this.delList.length; index++) {
                            //     this.tableData.splice(this.delList[index], 1);
                            //     this.pageTotal--;
                            // }
                            this.handleSearch();
                            this.multipleSelection = [];
                            this.queBank = {};
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
        },
        /**
         * 解绑题库中的所有题目
         */
        courseNameChange() {
            if (this.title === '添加题库') {
                return;
            }
            this.$confirm(`如果你切换所属课程,那么将与已联系的题目解绑,且不可逆,你确定吗?`, '确认解绑?', {
                confirmButtonText: '确认解绑',
                cancelButtonText: '取消',
                type: 'warning'
            })
                .then(() => {
                    this.isUntieQuestion = true;
                })
                .catch(() => {
                    this.queBank.courseId = this.temCourseName;
                });
        },
        UntieQuestion() {
            UntieQuestion({ queBankId: this.queBank.queBankId })
                .then((res) => {
                    if (res.success) {
                        this.$message.success('解绑成功');
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

<style scoped>
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
    width: 300px;
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
