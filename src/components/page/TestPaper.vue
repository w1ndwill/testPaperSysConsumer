<template>
    <div>
        <div id="testPaper">
            <div class="container">
                <!-- 搜索栏 -->
                <div class="handle-box">
                    <el-button @click="addTpDialog('手动生成试卷')" class="handle-del mr10" icon="el-icon-lx-add" type="primary">
                        手动生成试卷
                    </el-button>
                    <el-button @click="autoAddTpDialog('自动生成试卷')" class="handle-del mr10" icon="el-icon-lx-add" type="primary">
                        自动生成试卷
                    </el-button>
                    <el-dropdown>
                        <el-button @click="asToWord('false','false')" class="handle-del mr10" icon="el-icon-lx-forward" type="primary">
                            导出试卷
                        </el-button>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item >
                                <el-button @click="asToWord('false','false')"  icon="el-icon-lx-forward" type="text">
                                    不含答案
                                </el-button>
                            </el-dropdown-item>
                            <el-dropdown-item >
                                <el-button @click="asToWord('false','true')"  icon="el-icon-lx-forward" type="text">
                                    包含答案
                                </el-button>
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                    <el-button @click="delAllSelection" class="handle-del mr10" icon="el-icon-delete" type="warning" v-if="getIsAdmin()">
                        批量删除
                    </el-button>
                    <el-select
                        v-model="query.tpCourseId"
                        placeholder="所属课程"
                        class="handle-select mr10 query-margin"
                        clearable
                        @change="handleSearch"
                    >
                        <el-option
                            v-for="course in courseList"
                            :key="course.courseId"
                            :label="course.courseName"
                            :value="course.courseId"
                        ></el-option>
                    </el-select>
                    <el-checkbox
                        border
                        label="自己创建的"
                        class="handle-select mr8 query-margin"
                        @change="handleSearch"
                        v-model="isMy"
                        v-show="false"
                    ></el-checkbox>
                    <el-input
                        @keyup.enter.native="handleSearch"
                        class="handle-input mr10"
                        style="margin-left:10px"
                        placeholder="试卷名"
                        v-model="query.tpName"
                    ></el-input>
                    <el-button @click="handleSearch" icon="el-icon-search" type="primary">搜索</el-button>
                </div>
                <!-- 表格 -->
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
                        label="试卷名"
                        prop="tpName"
                        width="150"
                    ></el-table-column>
                    <el-table-column
                        :show-overflow-tooltip="true"
                        align="center"
                        label="所属课程"
                        prop="tpCourseName"
                        width="150"
                    ></el-table-column>
                    <el-table-column
                        :show-overflow-tooltip="true"
                        align="center"
                        label="创建人"
                        prop="tpCreateByName"
                        width="150"
                    ></el-table-column>
                    <!-- <el-table-column
                        :show-overflow-tooltip="true"
                        align="center"
                        label="包含知识点列表"
                        prop="tpKnowledgeNames"
                        width="100"
                    ></el-table-column> -->
                    <el-table-column :show-overflow-tooltip="true" label="试卷描述" prop="tpDescribe"></el-table-column>
                    <el-table-column align="center" label="操作" width="120">
                        <template slot-scope="scope">
                            <el-dropdown
                                ><!-- trigger="click" -->
                                <span class="el-dropdown-link">
                                    操作<i class="el-icon-lx-qrcode"></i><i class="el-icon-caret-bottom el-icon--right"></i>
                                </span>
                                <el-dropdown-menu slot="dropdown">
                                    <el-dropdown-item class="clearfix" v-if="getIsAdmin()">
                                        <el-button
                                            @click="handleEdit(scope.$index, scope.row)"
                                            class="btn-width"
                                            icon="el-icon-edit"
                                            type="primary"
                                            >编辑试卷
                                        </el-button>
                                    </el-dropdown-item>
                                    <el-dropdown-item class="clearfix" v-else-if="getUserName() === scope.row.tpCreateByName">
                                        <el-button
                                            @click="handleEdit(scope.$index, scope.row)"
                                            class="btn-width"
                                            icon="el-icon-edit"
                                            type="primary"
                                            >编辑试卷
                                        </el-button>
                                    </el-dropdown-item>
                                    <el-dropdown-item class="clearfix">
                                        <el-button
                                            @click="previewTp(scope.$index, scope.row)"
                                            class="btn-width"
                                            icon="el-icon-lx-attention"
                                            type="primary"
                                            >预览试卷
                                        </el-button>
                                    </el-dropdown-item>
                                    <el-dropdown-item class="clearfix" v-if="getIsAdmin()">
                                        <el-button
                                            @click="handleDelete(scope.$index, scope.row)"
                                            class="btn-width"
                                            icon="el-icon-delete"
                                            type="warning"
                                            >删除试卷
                                        </el-button>
                                    </el-dropdown-item>
                                    <el-dropdown-item class="clearfix" v-else-if="getUserName() === scope.row.tpCreateByName">
                                        <el-button
                                            @click="handleDelete(scope.$index, scope.row)"
                                            class="btn-width"
                                            icon="el-icon-delete"
                                            type="warning"
                                            >删除试卷
                                        </el-button>
                                    </el-dropdown-item>
                                    <el-dropdown-item class="clearfix">
                                        <el-button
                                            @click="handleSee(scope.$index, scope.row)"
                                            class="btn-width"
                                            icon="el-icon-lx-attention"
                                            type="primary"
                                            >知识点
                                        </el-button>
                                    </el-dropdown-item>
                                </el-dropdown-menu>
                            </el-dropdown>
                        </template>
                    </el-table-column>
                </el-table>
                <!-- 分页插件 -->
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

            <!-- 添加试卷弹出框 -->
            <el-dialog v-dialogDrag :title="title" :visible.sync="tpVisible" center width="50%" :close-on-click-modal="false">
                <el-form :model="tp" label-width="100px" ref="tp">
                    <el-form-item label="试卷名:" prop="tpName" :rules="rules.Required">
                        <el-input placeholder="请输入试卷名" v-model="tp.tpName"></el-input>
                    </el-form-item>
                    <el-form-item label="所属课程:" prop="tpCourseId" :rules="rules.pleaseSelect">
                        <el-select v-model="tp.tpCourseId" placeholder="所属课程" style="width: 100%" class="handle-select mr10">
                            <el-option
                                v-for="course_ in courseList"
                                :key="course_.courseId"
                                :label="course_.courseName"
                                :value="course_.courseId"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <template v-if="title === '自动生成试卷'">
                        <el-form-item label="选择题数量:" prop="choiceNo" :rules="rules.Required">
                            <el-input-number
                                v-model="tp.choiceNo"
                                @change="numberChange"
                                style="width: 100%"
                                :min="20"
                                :max="50"
                            ></el-input-number>
                        </el-form-item>
                        <el-form-item label="判断题数量:" prop="boolNo" :rules="rules.Required">
                            <el-input-number
                                v-model="tp.boolNo"
                                @change="numberChange"
                                style="width: 100%"
                                :min="10"
                                :max="40"
                            ></el-input-number>
                        </el-form-item>
                        <el-form-item label="简答题数量:" prop="simpleNo" :rules="rules.Required">
                            <el-input-number
                                v-model="tp.simpleNo"
                                @change="numberChange"
                                style="width: 100%"
                                :min="2"
                                :max="20"
                            ></el-input-number>
                        </el-form-item>
                    </template>
                    <el-form-item label="试卷描述:">
                        <el-input placeholder="请输入试卷描述" rows="6" type="textarea" v-model="tp.tpDescribe"></el-input>
                    </el-form-item>
                </el-form>
                <span class="dialog-footer" slot="footer">
                    <el-button @click="clearBtn()">取 消</el-button>
                    <el-button @click="save" type="primary">生成试卷</el-button>
                </span>
            </el-dialog>
        </div>
        <!-- 题目列表抽屉 -->
        <div id="question">
            <el-drawer
                :before-close="() => (queDrawerVisible = false)"
                ref="drawer"
                :title="queDrawerTitle"
                :visible.sync="queDrawerVisible"
                direction="rtl"
                size="70%"
            >
                <question ref="queDrawer" :isHidden="true" />
                <div class="drawer-footer">
                    <el-button @click="() => (queDrawerVisible = false)">取 消</el-button>
                    <el-button type="warning" @click="$refs.queDrawer.clearSelection()">清空选中</el-button>
                    <el-button type="primary" @click="createTestpaper" v-if="queDrawerTitle === '手动生成试卷'">生成试卷</el-button>
                    <el-button type="primary" @click="addQueToTp" v-if="queDrawerTitle === '添加试卷新题目'">确认添加</el-button>
                </div>
            </el-drawer>
        </div>
        <!-- 试卷预览弹出框 -->
        <div id="tpExamole">
            <el-dialog  :visible.sync="tpExamoleVisible" center width="50%">
                <div style="margin-left: 200px">
                    <el-button @click="saveTp" type="primary" icon="el-icon-lx-save">保存修改</el-button>
                    <!-- <el-button @click="asToWord('true')" type="primary" icon="el-icon-lx-forward">导出试卷</el-button> -->
                    <el-dropdown style="margin-left:10px">
                        <el-button @click="asToWord('true','false')" class="handle-del mr10" icon="el-icon-lx-forward" type="primary">
                            导出试卷
                        </el-button>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item >
                                <el-button @click="asToWord('true','false')"  icon="el-icon-lx-forward" type="text">
                                    不含答案
                                </el-button>
                            </el-dropdown-item>
                            <el-dropdown-item >
                                <el-button @click="asToWord('true','true')"  icon="el-icon-lx-forward" type="text">
                                    包含答案
                                </el-button>
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                    <el-button @click="addTpDialog('添加试卷新题目')" type="primary" icon="el-icon-lx-add">添加题目</el-button>
                </div>

                <h2 style="text-align: center">{{ testPaperName }}</h2>
                <template> 学校_______________ 班级_________________ 学号________________ 姓名____________________</template>
                <br />
                <el-collapse accordion>
                    <el-collapse-item :disabled="boolList.length === 0">
                        <template slot="title">
                            一、判断题,共【{{ boolList.length }}】题<i
                                :class="boolList.length === 0 ? 'el-icon-lx-attentionforbidfill' : 'el-icon-lx-attentionfill'"
                            ></i>
                        </template>
                        <el-card v-for="(bool, index) in boolList" :key="index">
                            <div slot="header" class="clearfix">
                                <b>题目{{ index + 1 }}:</b>{{ bool.queContent }}
                                <el-button style="float: right; padding: 3px 0" type="text" @click="delQue('bool', bool.queId)"
                                    >删除</el-button
                                >
                            </div>
                            <b>参考答案:</b>{{ bool.queAnswer }} <br />
                            <b>参考解析:</b>{{ bool.queAnswerDescribe }} <br />
                        </el-card>
                    </el-collapse-item>
                    <el-collapse-item name="2" :disabled="choiceList.length === 0">
                        <template slot="title">
                            二、选择题,共【{{ choiceList.length }}】题<i
                                :class="choiceList.length === 0 ? 'el-icon-lx-attentionforbidfill' : 'el-icon-lx-attentionfill'"
                            ></i>
                        </template>
                        <el-card v-for="(choice, index) in choiceList" :key="index">
                            <div slot="header" class="clearfix">
                                <b>题目{{ index + 1 }}:</b>{{ choice.queContent }}
                                <el-button style="float: right; padding: 3px 0" type="text" @click="delQue('choice', choice.queId)"
                                    >删除</el-button
                                >
                            </div>
                            <b>A:</b>{{ choice.queA }}<br />
                            <b>B:</b>{{ choice.queB }}<br />
                            <b>C:</b>{{ choice.queC }}<br />
                            <b>D:</b>{{ choice.queD }}<br />
                            <b>参考答案:</b>{{ choice.queAnswer }}<br />
                            <b>参考解析:</b>{{ choice.queAnswerDescribe }}<br />
                        </el-card>
                    </el-collapse-item>
                    <el-collapse-item name="3" :disabled="simpleList.length === 0">
                        <template slot="title">
                            三、简答题,共【{{ simpleList.length }}】题<i
                                :class="simpleList.length === 0 ? 'el-icon-lx-attentionforbidfill' : 'el-icon-lx-attentionfill'"
                            ></i>
                        </template>
                        <el-card v-for="(simple, index) in simpleList" :key="index">
                            <div slot="header" class="clearfix">
                                <b>题目{{ index + 1 }}:</b>{{ simple.queContent }}
                                <el-button style="float: right; padding: 3px 0" type="text" @click="delQue('simple', simple.queId)"
                                    >删除</el-button
                                >
                            </div>
                            <b>参考答案:</b>{{ simple.queAnswer }}<br />
                            <b>参考解析:</b>{{ simple.queAnswerDescribe }}<br />
                        </el-card>
                    </el-collapse-item>
                </el-collapse>
            </el-dialog>
        </div>
        <!-- 知识点列表抽屉 -->
        <div id="knowledge">
            <el-drawer
                :before-close="() => (knowledgeDrawerVisible = false)"
                ref="knowledgeDrawer"
                :title="knowledgeDrawerTitle"
                :visible.sync="knowledgeDrawerVisible"
                direction="rtl"
                size="70%"
            >
                <showKnowledge :data="knowledgeData" />
            </el-drawer>
        </div>
    </div>
</template>

<script>
import { add, del, edit, query, getCourse, getQueByIds } from '@/api/testPaper';
import question from './queManage/Question';
import AsToWord from '@/utils/AsToWord';
import power from '@/utils/power';
import showKnowledge from './CourseManage/showKnowledge';
import rules from '@/utils/rules';
export default {
    mixins: [AsToWord, power],
    components: {
        question,
        showKnowledge
    },
    watch: {
        tpVisible() {
            this.$nextTick(() => {
                this.$refs['tp'].clearValidate();
            });
        }
    },
    data() {
        return {
            rules,
            loading: true,
            courseList: [],
            isMy: true,
            query: {
                tpName: '',
                userId: localStorage.getItem('userId'),
                tpCourseId: '',
                tpCreateBy: '',
                page: 1,
                pageSize: 7
            },
            tp: {},
            queDrawerTitle: '',
            queDrawerVisible: false,
            temTpName: '',
            tableData: [],
            multipleSelection: [],
            delList: [],
            tpVisible: false,
            tpExamoleVisible: false,
            title: '',
            pageTotal: 0,
            form: {},
            idx: -1,
            id: -1,
            queIds: [],
            knowledgeDrawerVisible: false,
            knowledgeDrawerTitle: '',
            knowledgeData: []
        };
    },
    created() {
        // this.getData();
        this.handleSearch();
    },
    mounted() {
        this.isMy = this.$route.query.isMy;
    },
    methods: {
        //======================= 试卷操作方法 ==============================================
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
                    console.log("res.datalist"+res.dataList);
                    this.tableData = res.dataList;
                    this.pageTotal = res.pageTotal || 0;
                })
                .catch(() => {
                    this.loading = false;
                    this.$message.error('服务器歇菜了,请稍后再试!');
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
            this.query.tpCreateBy = this.isMy ? localStorage.getItem('userId') : ''; //是否只查询自己创建的
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
        addTpDialog(title) {
            this.queDrawerTitle = title;
            this.queDrawerVisible = true;
            this.tp.isAutoAdd = false;
        },
        // 添加自动生成试卷弹出框
        autoAddTpDialog(title) {
            this.title = title;
            this.tp = {};
            this.tp.boolNo = 25;
            this.tp.choiceNo = 35;
            this.tp.simpleNo = 2;
            this.tp.isAutoAdd = true;
            this.tpVisible = true;
        },
        // 编辑试卷
        handleEdit(index, row) {
            this.title = '编辑试卷';
            this.idx = index;
            this.tp = row;
            //保存当前的试卷名
            this.temTpName = this.tp.tpName;
            this.tpVisible = true;
        },
        // 取消按钮
        clearBtn() {
            this.handleSearch();
            this.tpVisible = false;
        },
        // 保存编辑/添加
        save() {
            this.$refs['tp'].validate((check) => {
                if (check) {
                    if (this.title === '自动生成试卷' || this.title === '手动生成试卷') {
                        this.addTestPaper();
                    } else if (this.title === '编辑试卷') {
                        this.tpVisible = false;
                        this.isChangeTpName = 'F';
                        this.editTestPaper();
                    }
                }
            });
        },
        // 添加试卷
        addTestPaper() {
            this.tp.tpCreateBy = localStorage.getItem('userId');
            // add(this.getTp(this.tp)).then((res) => {
            add(this.tp)
                .then((res) => {
                    if (res.success) {
                        this.tpVisible = false;
                        this.$message.success(res.msg);
                        this.handleSearch();
                        this.tpVisible = false;
                        this.tp = {};
                    } else {
                        this.$message.warning(res.msg);
                    }
                })
                .catch(() => {
                    this.$message.error('服务器歇菜了,请稍后再试!!');
                    this.tpVisible = false;
                });
        },
        // 获取试卷实体类
        getTp(tp) {
            let obj = new Object();
            // obj.tpId = tp.tpId;
            obj.tpCourseId = tp.tpCourseId;
            obj.tpCreateBy = tp.tpCreateBy;
            obj.tpName = tp.tpName;
            obj.tpDescribe = tp.tpDescribe;
            return obj;
        },
        // 编辑试卷
        editTestPaper() {
            if (this.tp.tpName !== this.temTpName) {
                this.tp.isChangeTpName = 'T';
            }
            edit(this.tp)
                .then((res) => {
                    if (res.success) {
                        this.$message.success(`修改试卷成功~`);
                        this.$set(this.tableData, this.idx, this.tp);
                    } else {
                        this.tp.tpName = this.temTpName;
                        this.$message.warning(res.msg);
                    }
                })
                .catch(() => {
                    this.tp.tpName = this.temTpName;
                    this.$message.error(`服务器歇菜了,请稍后重试!!`);
                });
        },
        // 删除试卷
        handleDelete(index, row) {
            // 二次确认删除
            this.$confirm('确定删除该试卷吗？', '提示', {
                type: 'warning'
            }).then(() => {
                del({ tpId: row.tpId })
                    .then((res) => {
                        if (res.success) {
                            this.tableData.splice(index, 1);
                            this.$message.warning('删除成功');
                            this.pageTotal--;
                        } else {
                            this.$message.warning(res.msg);
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
            let tpId = '';
            let str = '';
            for (let i = 0; i < this.multipleSelection.length; i++) {
                str += this.multipleSelection[i].tpName + ' ';
                for (let index = 0; index < this.tableData.length; index++) {
                    if (this.multipleSelection[i].tpId === this.tableData[index].tpId) {
                        tpId += this.multipleSelection[i].tpId + ',';
                        this.delList.push(index);
                    }
                }
            }
            this.$confirm(`确定要删除这${length}张试卷吗？`, '警告', {
                type: 'warning'
            }).then(() => {
                del({ tpId: tpId.substring(0, tpId.length - 1) })
                    .then((res) => {
                        if (res.success) {
                            this.$message.warning(`成功删除了${length}条数据`);
                            // for (let index = 0; index < this.delList.length; index++) {
                            //     this.tableData.splice(this.delList[index], 1);
                            //     this.pageTotal--;
                            // }
                            this.handleSearch();
                            this.multipleSelection = [];
                            this.tp = {};
                        } else {
                            this.$message.warning(res.msg);
                        }
                    })
                    .catch(() => {
                        this.$message.error('服务器歇菜了,请稍候再试!!');
                    });
            });
        },
        // 刷新题目数字输入框
        numberChange() {
            this.$forceUpdate();
        },
        /**
         * 生成试卷
         */
        createTestpaper() {
            this.queDrawerVisible = false;
            let multipleSelection = this.$refs.queDrawer.multipleSelection;
            let ids = '';
            let knowledgeIds;
            if (multipleSelection && multipleSelection.length > 0) {
                multipleSelection.forEach((question) => {
                    if (question.queId !== undefined) {
                        ids += question.queId + ',';
                    }
                    if (question.knowledgeIds !== undefined) {
                        knowledgeIds += question.knowledgeIds + ',';
                    }
                });
                this.tp.tpQueIds = ids.substring(0, ids.length - 1);
                this.tp.tpKnowledgeIds = knowledgeIds.substring(0, knowledgeIds.length - 1);
                this.title = '手动生成试卷';
                this.tp.isAutoAdd = false;
                this.tpVisible = true;
            } else {
                this.$message.warning('请选择题目组成试卷~~');
            }
        },
        /**
         * 通过试卷的id获取试卷的题目列表
         */
         async getQueByIds(row) {
            this.loading = true;
            getQueByIds({ queIds: row.tpQueIds })
                .then((res) => {
                    if (res.success) {
                        this.boolList = res.boolList;
                        this.choiceList = res.choiceList;
                        this.simpleList = res.simpleList;
                        this.testPaperName = row.tpName;
                        this.exportFileName = row.tpName;
                        this.loading = false;
                    } else {
                        this.$message.warning(res.msg);
                        this.loading = false;
                    }
                })
                .catch((error) => {
                    this.$message.error('服务器歇菜了,请稍后再试!!');
                    this.loading = false;
                });
        },
        /**
         * 导出试卷
         */
        asToWord(isPreView, serverFileName) {
            if (serverFileName === 'false') {
                this.serverFileName = 'queTemplate1.docx';   //不包含参考答案
            } else {
                this.serverFileName = 'queTemplate2.docx';   // 包含参考答案
            }
            if (isPreView === 'true') {  
                this.exportWord();    // 预览导出
                return;
            } else if (this.multipleSelection.length !== 1 ) {
                this.$message.warning('只能选择一张试卷进行导出!!');
                return;
            }
            this.getQueByIds(this.multipleSelection[0]);
            setTimeout(() => {
                this.exportWord();   // 选中导出
            }, 100);
           
        },
        /**
         * 预览试卷
         */
        previewTp(index, row) {
            // 保存当行数据
            this.tp = row;
            //保存当前的试卷名
            this.temTpName = this.tp.tpName;
            // 获取试卷的题目
            this.getQueByIds(row);
            this.tpExamoleVisible = true;
            // 将试卷的id也存下来,方便后面修改试卷的题目
            this.queIds = row.tpQueIds.split(',');
        },
        /**
         * 修改试卷
         */
        delQue(type, queId) {
            this.queIds.forEach((item, index) => {
                if (item === queId) {
                    this.queIds.splice(index, 1);
                }
            });
            switch (type) {
                case 'choice':
                    this.choiceList.forEach((choice, index) => {
                        if (choice.queId === queId) {
                            this.choiceList.splice(index, 1);
                        }
                    });
                    break;
                case 'bool':
                    this.boolList.forEach((bool, index) => {
                        if (bool.queId === queId) {
                            this.boolList.splice(index, 1);
                        }
                    });
                    break;
                case 'simple':
                    this.simpleList.forEach((simple, index) => {
                        if (simple.queId === queId) {
                            this.simpleList.splice(index, 1);
                        }
                    });
                    break;
                default:
                    break;
            }
        },
        /**
         * 保存修改后的试卷
         */
        saveTp() {
            this.tp.tpQueIds = this.queIds.toString();
            this.editTestPaper();
        },
        /**
         * 添加题目给试卷
         */
        addQueToTp() {
            this.queDrawerVisible = false;
            this.$refs.queDrawer.multipleSelection.forEach((que) => {
                this.queIds.push(que.queId);
                switch (que.queType) {
                    case 'choice':
                        this.choiceList.push(que);
                        break;
                    case 'bool':
                        this.boolList.push(que);
                        break;
                    case 'simple':
                        this.simpleList.push(que);
                        break;
                    default:
                        break;
                }
            });
        },

        /**
         * 查看知识点
         */
        handleSee(index, row) {
            this.knowledgeDrawerTitle = `【${row.tpName}】知识点列表`;
            this.knowledgeData = row.knowledgeList;
            this.knowledgeDrawerVisible = true;
        }
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
</style>
