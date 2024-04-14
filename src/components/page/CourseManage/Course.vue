<template>
    <div>
        <div id="course">
            <!-- <div class="crumbs">
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item><i class="el-icon-lx-cascades"></i> 课程数据管理</el-breadcrumb-item>
                </el-breadcrumb>
            </div> -->
            <div class="container">
                <div class="handle-box">
                    <el-button @click="addCourseDialog" class="handle-del mr10" icon="el-icon-lx-add" type="primary" v-if="getIsAdmin()">
                        添加课程
                    </el-button>
                    <el-button @click="delAllSelection" class="handle-del mr10" icon="el-icon-delete" type="warning" v-if="getIsAdmin()">
                        批量删除
                    </el-button>
                    <el-input
                        @keyup.enter.native="handleSearch"
                        class="handle-input mr10"
                        placeholder="课程名"
                        v-model="query.courseName"
                    ></el-input>
                    <el-button @click="handleSearch" icon="el-icon-search" type="primary">搜索</el-button>
                </div>
                <el-table
                    :data="tableData"
                    @selection-change="handleSelectionChange"
                    border
                    class="table"
                    header-cell-class-name="table-header"
                    ref="multipleTable"
                    v-loading="loading"
                >
                    <el-table-column align="center" checked="true" type="selection" width="55"></el-table-column>
                    <el-table-column align="center" label="序号" type="index" width="55"></el-table-column>
                    <el-table-column
                        :show-overflow-tooltip="true"
                        align="center"
                        label="课程名"
                        prop="courseName"
                        width="150"
                    ></el-table-column>
                    <el-table-column align="center" label="知识点个数" prop="knowledgeNo" width="100"></el-table-column>
                    <el-table-column :show-overflow-tooltip="true" label="课程描述" prop="courseDescribe"></el-table-column>
                    <el-table-column align="center" label="操作" width="120">
                        <template slot-scope="scope">
                            <el-dropdown
                                ><!-- trigger="click" -->
                                <span class="el-dropdown-link">
                                    操作<i class="el-icon-lx-qrcode"></i><i class="el-icon-caret-bottom el-icon--right"></i>
                                </span>
                                <el-dropdown-menu slot="dropdown">
                                    <el-dropdown-item class="clearfix">
                                        <el-button
                                            @click="handleEdit(scope.$index, scope.row)"
                                            class="btn-width-see"
                                            icon="el-icon-edit"
                                            type="primary"
                                            v-if="getIsAdmin()"
                                            >编辑
                                        </el-button>
                                    </el-dropdown-item>
                                    <el-dropdown-item class="clearfix">
                                        <el-button
                                            @click="handleDelete(scope.$index, scope.row)"
                                            class="btn-width-see"
                                            icon="el-icon-delete"
                                            type="warning"
                                            v-if="getIsAdmin()"
                                            >删除
                                        </el-button>
                                    </el-dropdown-item>
                                    <el-dropdown-item class="clearfix">
                                        <el-button
                                            @click="handleKnowledge(scope.$index, scope.row)"
                                            class="btn-width-see"
                                            icon="el-icon-lx-attention"
                                            type="primary"
                                            >查看知识点
                                        </el-button>
                                    </el-dropdown-item>
                                </el-dropdown-menu>
                            </el-dropdown>
                        </template>
                    </el-table-column>
                </el-table>
                <div class="pagination">
                    <el-pagination
                        :current-page="query.page"
                        :page-size="query.pageSize"
                        :page-sizes="[5, 7, 10, 15, 20, 40, 100]"
                        :total="pageTotal"
                        @current-change="handlePageChange"
                        @size-change="handleSizeChange"
                        background
                        layout="total, sizes, prev, pager, next, jumper"
                    ></el-pagination>
                </div>
            </div>

            <!-- course弹出框 -->
            <el-dialog v-dialogDrag :title="title" :visible.sync="courseVisible" center width="50%" :close-on-click-modal="false">
                <el-form :model="course" label-width="100px" ref="course">
                    <el-form-item label="课程名:" prop="courseName" :rules="rules.Required">
                        <el-input placeholder="请输入课程名" v-model="course.courseName"></el-input>
                    </el-form-item>
                    <el-form-item label="课程描述:" prop="courseDescribe" :rules="rules.Required">
                        <el-input placeholder="请输入课程描述" rows="10"  type="textarea" v-model="course.courseDescribe"></el-input>
                    </el-form-item>
                </el-form>
                <span class="dialog-footer" slot="footer">
                    <el-button @click="clearBtn()">取 消</el-button>
                    <el-button @click="save" type="primary">确 定</el-button>
                </span>
            </el-dialog>
        </div>
        <el-drawer
            :before-close="drawerClose"
            :title="knowledgeDrawer.title"
            :visible.sync="knowledgeDrawerVisible"
            direction="rtl"
            size="70%"
        >
            <knowledge ref="knowledgeDrawer" :courseId="knowledgeDrawer.courseId" :row="knowledgeDrawer.row" />
        </el-drawer>
    </div>
</template>

<script>
import { add, del, edit, query } from '@/api/course';
import knowledge from './Knowledge';
import power from '@/utils/power';   //权限 [管理员/教师]
import rules from '@/utils/rules';

export default {
    mixins: [power],
    components: {
        knowledge
    },
    name: 'basetable',
    watch:{
        courseVisible(){
            this.$nextTick(() => {
                this.$refs['course'].clearValidate();
            });
        }
    },
    data() {
        return {
            rules,
            loading: true,
            query: {
                courseName: '',
                userId: localStorage.getItem('userId'),
                page: 1,
                pageSize: 7
            },
            course: {
                courseId: '',
                courseName: '',
                courseDescribe: '',
                isChangeCourseName: 'F'
            },
            temCourseName: '',
            tableData: [],
            multipleSelection: [],
            delList: [],
            delIdList: {
                courseId: ''
            },
            courseVisible: false,
            knowledgeDrawerVisible: false,
            knowledgeDrawer: {
                courseId: '',
                row: {}
            },
            title: '',
            pageTotal: 0,
            form: {},
            idx: -1,
            id: -1,
            isWait: true
        };
    },
    created() {
        this.getData();
    },
    methods: {
        //======================= 课程操作方法 ==============================================
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
                })
                .catch(() => {
                    this.loading = false;
                    this.$message.error('服务器歇菜了,请稍后再试!');
                });
        },
        // 触发搜索按钮
        handleSearch() {
            this.$set(this.query, 'page', this.query.page || 1);
            this.getData();
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
        // 添加课程弹出框
        addCourseDialog() {
            this.title = '添加课程';
            this.course = {};
            this.courseVisible = true;
        },
        // 编辑课程
        handleEdit(index, row) {
            this.title = '编辑课程';
            this.idx = index;
            this.course = row;
            //保存当前的课程名
            this.temCourseName = this.course.courseName;
            this.courseVisible = true;
        },
        // 取消按钮
        clearBtn() {
            this.handleSearch();
            this.courseVisible = false;
        },
        // 保存编辑/添加
        save() {
            this.$refs['course'].validate((check) => {
                if (check) {
                    if (this.title === '添加课程') {
                        this.addCourse();
                    } else if (this.title === '编辑课程') {
                        this.isChangeCourseName = 'F';
                        this.editCourse();
                    }
                }
            });
        },
        // 添加课程
        addCourse() {
            add(this.getCourse(this.course)).then((res) => {
                if (res.success) {
                    this.$message.success(`添加成功`);
                    // this.course.courseId = res.courseId;
                    // this.tableData.push(this.course);
                    // this.pageTotal++;
                    this.handleSearch();
                    this.course = {};
                    this.courseVisible = false;
                } else {
                    this.$message.error(res.msg);
                }
            }).catch(e=>{
                this.$message.error("服务器歇菜了,清稍后再试!!")
            });
        },
        // 获取课程实体类
        getCourse(course) {
            let obj = new Object();
            // obj.courseId = courseId.courseId;
            obj.courseName = course.courseName;
            obj.courseDescribe = course.courseDescribe;
            return obj;
        },
        // 编辑课程
        editCourse() {
            if (this.course.courseName !== this.temCourseName) {
                this.course.isChangeCourseName = 'T';
            }
            edit(this.course)
                .then((res) => {
                    if (res.success) {
                        this.$message.success(`修改成功`);
                        this.$set(this.tableData, this.idx, this.course);
                        this.course = {};
                        this.courseVisible = false;
                    } else {
                        this.course.courseName = this.temCourseName;
                        this.$message.warning(res.msg);
                    }
                })
                .catch(() => {
                    this.course.courseName = this.temCourseName;
                    this.$message.error(`服务器歇菜了,请稍后重试!!`);
                });
        },
        // 删除课程
        handleDelete(index, row) {
            // 二次确认删除
            this.$confirm('确定将 "课程"和关联"知识点" 一起删除吗？', '提示', {
                type: 'warning'
            }).then(() => {
                this.delIdList.courseId = row.courseId;
                del(this.delIdList)
                    .then((res) => {
                        if (res) {
                            this.tableData.splice(index, 1);
                            this.$message.warning('删除成功');
                            this.pageTotal--;
                        } else {
                            this.$message.error('删除失败');
                        }
                    })
                    .catch(() => {
                        this.$message.error('服务器歇菜了,请稍后重试!!');
                    });
            });
        },
        // 删除多选
        delAllSelection() {
            const length = this.multipleSelection.length;
            if (!length || length === 0) {
                this.$message.warning('请至少选择一条数据进行删除!!');
                return;
            }
            let courseId = '';
            let str = '';
            for (let i = 0; i < this.multipleSelection.length; i++) {
                str += this.multipleSelection[i].courseName + ' ';
                for (let index = 0; index < this.tableData.length; index++) {
                    if (this.multipleSelection[i].courseId === this.tableData[index].courseId) {
                        courseId += this.multipleSelection[i].courseId + ',';
                        this.delList.push(index);
                    }
                }
            }
            this.course.courseId = courseId.substring(0, courseId.length - 1);
            this.$confirm(`确定要删除${str}吗？`, '警告', {
                type: 'warning'
            }).then(() => {
                this.delIdList.courseId = this.course.courseId;
                del(this.delIdList)
                    .then((res) => {
                        if (res) {
                            this.$message.warning(`成功删除了${str}`);
                            this.handleSearch();
                            this.multipleSelection = [];
                            this.course = {};
                        }
                    })
                    .catch(() => {});
            });
        },
        /**
         * 显示知识点抽屉列表
         */
        handleKnowledge(index, row) {
            this.knowledgeDrawer.courseId = row.courseId;
            this.knowledgeDrawer.row = row;
            this.knowledgeDrawer.title = `【${row.courseName}】知识点-列表`;
            this.knowledgeDrawerVisible = true;
            if (this.isWait) {
                setTimeout(() => {
                    //渲染完成后的操作
                    this.$refs.knowledgeDrawer.initKnowledge(row);
                    this.isWait = false;
                }, 200);
            } else {
                //渲染完成后的操作
                this.$refs.knowledgeDrawer.initKnowledge(row);
            }
        },
        /**
         * 关闭抽屉时候去清除表格数据
         */
        drawerClose(close) {
            if (this.$refs.knowledgeDrawer.knowledgeVisible) {
                this.$confirm(`是否关闭【${this.knowledgeDrawer.title}】窗口?`, '确认信息', {
                    distinguishCancelAndClose: true,
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$refs.knowledgeDrawer.tableData = [];
                    this.$refs.knowledgeDrawer.knowledgeVisible = false;
                    close();
                });
            } else {
                this.$refs.knowledgeDrawer.tableData = [];
                close();
            }
        }
    }
};
</script>

<style scoped>
.handle-box {
    margin-bottom: 20px;
}

.btn-width-see {
    width: 150px;
    margin-top: 3px;
}

.btn-width {
    width: 70px;
    display: inline;
    margin: 20 10 10 10;
}

.handle-select {
    width: 120px;
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
