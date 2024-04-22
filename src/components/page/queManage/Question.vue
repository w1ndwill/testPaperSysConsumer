<template>
    <div>
        <div class="container">
            <div class="handle-box">
                <el-button type="primary" v-if="!isHidden" icon="el-icon-lx-add" class="handle-del mr10" @click="addQuestionDialog"
                    >添加题目</el-button
                >
                <el-button
                    type="warning"
                    v-if="!isHidden && getIsAdmin()"
                    icon="el-icon-delete"
                    class="handle-del mr10"
                    @click="delAllSelection"
                    >批量删除</el-button
                >
                <el-select v-model="query.queType" placeholder="题型" @change="queTypeChange" class="handle-select query-margin">
                    <!-- <el-option key="choice" checked label="选择题" value="choice"></el-option>
                    <el-option key="bool" label="判断题" value="bool"></el-option>
                    <el-option key="simple" label="简答题" value="simple"></el-option> -->
                    <el-option v-for="item in queType" :key="item.key" :label="item.val" :value="item.key"></el-option>
                </el-select>
                <el-select
                    v-model="query.queCourseId"
                    placeholder="所属课程"
                    class="handle-select mr10 query-margin"
                    clearable
                    @change="courseChange"
                >
                    <el-option
                        v-for="course in courseList"
                        :key="course.courseId"
                        :label="course.courseName"
                        :value="course.courseId"
                    ></el-option>
                </el-select>
                <el-select
                    v-model="query.queBankId"
                    @click.native="queBankClick"
                    :placeholder="'所属题库'"
                    class="handle-select mr10 query-margin"
                    @change="handleSearch"
                    clearable
                >
                    <el-option
                        v-for="queBank in queBankList"
                        :key="queBank.queBankId"
                        :label="queBank.queBankName"
                        :value="queBank.queBankId"
                    ></el-option>
                </el-select>
                <el-checkbox border label="自己创建的" class="mr8 query-margin" @change="handleSearch" v-model="isMy"></el-checkbox>
                <el-input
                    v-model="query.queContent"
                    placeholder="题目内容"
                    @keyup.enter.native="handleSearch"
                    class="handle-input query-margin"
                ></el-input>
                <el-button style="margin-left: 5px" type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
            </div>
            <!-- row-key="queId"属性可以跨页勾选题目 -->
            <el-table
                :data="tableData"
                v-loading="loading"
                border
                class="table"
                ref="multipleTable"
                row-key="queId"
                header-cell-class-name="table-header"
                key="queTable"
                @selection-change="handleSelectionChange"
            >
                <el-table-column :reserve-selection="true" type="selection" width="55" align="center"></el-table-column>
                <el-table-column type="index" width="55" align="center" label="序号"></el-table-column>
                <el-table-column
                    :show-overflow-tooltip="true"
                    prop="queCourseName"
                    label="所属课程"
                    width="80"
                    align="center"
                ></el-table-column>
                <el-table-column
                    :show-overflow-tooltip="true"
                    prop="queBankName"
                    label="所属题库"
                    width="80"
                    align="center"
                ></el-table-column>
                <el-table-column
                    :show-overflow-tooltip="true"
                    prop="queCreateByName"
                    label="创建人"
                    width="80"
                    align="center"
                ></el-table-column>
                <el-table-column
                    :show-overflow-tooltip="true"
                    prop="knowledgeNames"
                    label="包含知识点"
                    width="180"
                    align="center"
                ></el-table-column>
                <el-table-column
                    sortable
                    :show-overflow-tooltip="true"
                    prop="queDifficulty"
                    align="center"
                    label="难度"
                    width="80"
                ></el-table-column>
                <el-table-column :show-overflow-tooltip="true" prop="queContent" label="题目内容" width="280"></el-table-column>
                <template v-if="query.queType === 'choice'">
                    <el-table-column :show-overflow-tooltip="true" prop="queA" label="选项A" width="80" align="center"></el-table-column>
                    <el-table-column :show-overflow-tooltip="true" prop="queB" label="选项B" width="80" align="center"></el-table-column>
                    <el-table-column :show-overflow-tooltip="true" prop="queC" label="选项C" width="80" align="center"></el-table-column>
                    <el-table-column :show-overflow-tooltip="true" prop="queD" label="选项D" width="80" align="center"></el-table-column>
                </template>
                <el-table-column
                    :show-overflow-tooltip="true"
                    prop="queAnswer"
                    label="参考答案"
                    width="100"
                    v-if="query.queType === 'bool'"
                >
                    <template slot-scope="scope">
                        <el-tag
                            :type="scope.row.queAnswer === '正确【√】' ? 'success' : scope.row.queAnswer === '错误【×】' ? 'warning' : ''"
                            >{{ scope.row.queAnswer }}</el-tag
                        >
                    </template>
                </el-table-column>
                <el-table-column
                    :show-overflow-tooltip="true"
                    prop="queAnswer"
                    label="参考答案"
                    :width="query.queType === 'choice' ? '80' : '180'"
                    v-else
                    :align="query.queType === 'choice' ? 'center' : ''"
                >
                    <template slot-scope="scope">
                        <el-tag
                            v-if="query.queType === 'choice'"
                            :type="
                                scope.row.queAnswer === 'A'
                                    ? 'primary'
                                    : scope.row.queAnswer === 'B'
                                    ? 'warning'
                                    : scope.row.queAnswer === 'C'
                                    ? 'success'
                                    : scope.row.queAnswer === 'D'
                                    ? 'info'
                                    : ''
                            "
                            >{{ scope.row.queAnswer }}</el-tag
                        >
                        {{ query.queType !== 'choice' ? scope.row.queAnswer : '' }}
                    </template>
                </el-table-column>
                <el-table-column :show-overflow-tooltip="true" prop="queAnswerDescribe" label="答案解析" width="280"></el-table-column>

                <el-table-column label="操作" width="180" align="center" v-if="!isHidden">
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
                            <el-dropdown-menu slot="dropdown" v-else-if="getUserName() === scope.row.queCreateByName">
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
            <div class="pagination"> <!-- 分页 -->
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

        <!-- question弹出框 --><!--  style="margin-top:-100px" -->
        <el-dialog
            :title="title"
            :visible.sync="queVisible"
            width="50%"
            style="margin-top: -100px"
            center
            :close-on-click-modal="false"
        >
            <el-form ref="question" :model="question" label-width="100px" style="height: 500px; overflow: auto">
                <el-form-item label="所属课程:" v-if="title === '添加题目'" prop="queCourseId" :rules="rules.pleaseSelect">
                    <el-select
                        v-model="question.queCourseId"
                        style="width: 100%"
                        @change="courseChange('dialog')"
                        placeholder="请选择所属课程"
                    >
                        <el-option
                            v-for="course_ in courseList"
                            :key="course_.courseId"
                            :label="course_.courseName"
                            :value="course_.courseId"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="所属题库:" v-if="title === '添加题目'" prop="queBankId" :rules="rules.pleaseSelect">
                    <el-select
                        v-model="question.queBankId"
                        @change="queBankChange"
                        style="width: 100%"
                        :placeholder="question.queCourseId === '' ? '请先选择所属课程' : '所属题库'"
                    >
                        <el-option
                            v-for="queBank_ in dialogQueBankList"
                            :key="queBank_.queBankId"
                            :label="queBank_.queBankName"
                            :value="queBank_.queBankId"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="题目类型:" prop="queType" :rules="rules.pleaseSelect">
                    <el-select v-model="question.queType" style="width: 100%" :placeholder="'请选择题目类型'">
                        <el-option v-for="item in queType" :key="item.key" :label="item.val" :value="item.key"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="题目难度:" prop="queDifficulty" :rules="rules.Required">
                    <el-input-number v-model="question.queDifficulty" style="width: 100%" :min="1" :max="10"></el-input-number>
                </el-form-item>
                <el-form-item label="知识点列表:" prop="knowledgeIds" :rules="rules.pleaseSelect">
                    <el-input
                        v-model="question.knowledgeIds"
                        @click.native="knowledgeDailogHandle"
                        placeholder="请选择关联的知识点"
                    ></el-input>
                </el-form-item>
                <el-form-item label="题目内容:" prop="queContent" :rules="rules.Required">
                    <el-input
                        type="textarea"
                        :autosize="{ minRows: 2, maxRows: 4 }"
                        v-model="question.queContent"
                        placeholder="请输入题目内容"
                    ></el-input>
                </el-form-item>
                <div v-if="question.queType === 'choice'">
                    <el-form-item label="选项A:" prop="queA" :rules="rules.Required">
                        <el-input
                            type="textarea"
                            :autosize="{ minRows: 1, maxRows: 4 }"
                            v-model="question.queA"
                            placeholder="请输入选项A内容"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="选项B:" prop="queB" :rules="rules.Required">
                        <el-input
                            type="textarea"
                            :autosize="{ minRows: 1, maxRows: 4 }"
                            v-model="question.queB"
                            placeholder="请输入选项B内容"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="选项C:" prop="queC" :rules="rules.Required">
                        <el-input
                            type="textarea"
                            :autosize="{ minRows: 1, maxRows: 4 }"
                            v-model="question.queC"
                            placeholder="请输入选项C内容"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="选项D:" prop="queD" :rules="rules.Required">
                        <el-input
                            type="textarea"
                            :autosize="{ minRows: 1, maxRows: 4 }"
                            v-model="question.queD"
                            placeholder="请输入选项D内容"
                        ></el-input>
                    </el-form-item>
                </div>
                <el-form-item
                    label="参考答案:"
                    prop="queAnswer"
                    :rules="question.queType === 'simple' ? rules.Required : rules.pleaseSelect"
                >
                    <el-select
                        v-model="question.queAnswer"
                        v-if="question.queType === 'choice'"
                        style="width: 100%"
                        placeholder="请选择参考答案"
                    >
                        <el-option key="A" label="选项A" value="A"></el-option>
                        <el-option key="B" label="选项B" value="B"></el-option>
                        <el-option key="C" label="选项C" value="C"></el-option>
                        <el-option key="D" label="选项D" value="D"></el-option>
                    </el-select>
                    <el-select
                        v-else-if="question.queType === 'bool'"
                        v-model="question.queAnswer"
                        placeholder="请选择参考答案"
                        style="width: 100%; color: red"
                    >
                        <el-option style="color: rgb(46, 204, 113)" key="正确【√】" label="正确【√】" value="正确【√】"></el-option>
                        <el-option style="color: red" key="错误【×】" label="错误【×】" value="错误【×】"></el-option>
                    </el-select>
                    <el-input
                        v-else
                        v-model="question.queAnswer"
                        placeholder="请择输入参考答案"
                        type="textarea"
                        :autosize="{ minRows: 2, maxRows: 4 }"
                    ></el-input>
                </el-form-item>
                <el-form-item label="答案解析:">
                    <el-input
                        type="textarea"
                        :autosize="{ minRows: 2, maxRows: 4 }"
                        v-model="question.queAnswerDescribe"
                        placeholder="请输入答案解析"
                    ></el-input>
                </el-form-item>
            </el-form>

            <span slot="footer" class="dialog-footer" style="text-algin: center">
                <el-button @click="clearBtn()">取 消</el-button>
                <el-button type="primary" @click="save">确 定</el-button>
            </span>
        </el-dialog>
        <!-- 知识点列表对话框 -->
        <el-dialog :title="knowledgeTitle" :visible.sync="knowledgeVisible" width="30%" center style="margin-top: -100px">
            <el-tree
                style="height: 500px; overflow: auto"
                :data="treeData"
                show-checkbox
                node-key="knowledgeId"
                ref="tree"
                default-expand-all
                :default-checked-keys="defaultCheckList"
                :props="defaultProps"
                check-on-click-node
            >
            </el-tree>
            <span slot="footer" class="dialog-footer" style="text-algin: center">
                <el-button @click="knowledgeVisible = false">取 消</el-button>
                <el-button @click="saveKnowledge" type="primary">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import { query, get, edit, del, add, getQueBank, getCourse, getKnowledge } from '../../../api/question';
import power from '@/utils/power';
import rules from '@/utils/rules';

export default {
    mixins: [power],
    props: {
        isHidden: Boolean
    },
    name: 'queTable',
    data() {
        return {
            rules,
            loading: true,
            isMy: false,
            query: {
                isHidden: this._props.isHidden,
                userId: localStorage.getItem('userId'),
                queCreateBy: '',
                queContent: '',
                queBankId: '',
                queCourseId: '',
                queType: 'choice',
                page: 1,
                pageSize: 7
            },
            question: {
                queContent: '',
                queType: 'choice',
                queCourseId: '',
                queCourseName: '',
                queBankId: '',
                queBankName: '',
                knowledgeIds: '',
                queAnswer: '',
                queAnswerDescribe: '',
                queCreateBy: localStorage.getItem('userId'),
                queCreateByName: '',
                queDifficulty: 5,
                queA: '',
                queB: '',
                queC: '',
                queD: ''
            },
            queType: [
                { key: 'choice', val: '选择题' },
                { key: 'bool', val: '判断题' },
                { key: 'simple', val: '简答题' }
            ],
            defaultCheckList: [],
            defaultCheckedKeys: [],
            treeData: [
                {
                    courseId: '',
                    knowledgeName: '',
                    children: []
                }
            ],
            defaultProps: {
                children: 'children',
                label: 'knowledgeName'
            },
            temQuestion: {},
            course: {
                courseId: ''
            },
            tableData: [],
            courseList: [],
            queBankList: [],
            knowledgeIdList: '',
            dialogQueBankList: [],
            multipleSelection: [],
            delList: [],
            delIdList: {
                queId: '',
                queType: ''
            },
            queVisible: false,
            knowledgeVisible: false,
            title: '',
            knowledgeTitle: '',
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
        queVisible() {
            this.$nextTick(() => {
                this.$refs['question'].clearValidate();
            });
        }
    },
    methods: {
        // 获取所有数据
        getData() {
            this.loading = true;
            if (this.isMy) {
                this.query.queCreateBy = localStorage.getItem('userId');
            } else {
                this.query.queCreateBy = '';
            }
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
                    let userId = this.getUserId(); //用户id
                    let isAdmin = this.getIsAdmin(); //是否是管理员
                    this.tableData = res.dataList.filter((item1) => { //普通用户
                        console.log('item1.queCreateBy', item1.queCreateBy);
                        console.log('userId', userId);
                        return item1.queCreateBy === userId; // 只显示自己创建的题目
                    });
                    this.pageTotal = this.tableData.length; //总数

                    if (this.tableData.length === 0 && this.query.page !== 1) {
                        this.query.page = 1;
                        this.getData();
                    }
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
        getKnowledgeNames() {
            getKnowledge({ courseId: this.question.queCourseId })
                .then((res) => {
                    if (res.success) {
                        //this.treeData[0].knowledgeId = this.question.queCourseId;
                        this.courseList.forEach((course) => {
                            if (course.courseId === this.question.queCourseId) {
                                this.treeData[0].knowledgeName = course.courseName;
                                this.knowledgeTitle = `【${course.courseName}】知识点列表`;
                            }
                        });
                        this.treeData[0].children = res.data;
                    } else {
                        this.$message.warning(res.msg);
                    }
                })
                .catch(() => {
                    this.$message.error('服务器歇菜了,请稍后再试!!');
                });
        },
        /**
         * 下拉框值改变
         */
        courseChange(val, action) {
            if (val === 'dialog') {
                /**
                 * 获取题库数据列表
                 */
                getQueBank({ courseId: this.question.queCourseId }).then((res) => {
                    this.dialogQueBankList = res;
                });
                if (action !== 'edit') {
                    this.question.queBankId = '';
                    this.question.knowledgeIds = '';
                }

                this.getKnowledgeNames();
            } else {
                this.query.queBankId = '';
                // this.course.courseId = this.query.queCourseId;
                /**
                 * 触发查询按钮
                 */
                this.handleSearch();
                /**
                 * 获取题库列表
                 */
                getQueBank({ courseId: this.query.queCourseId }).then((res) => {
                    this.queBankList = res;
                });
            }
        },
        /**
         * 当题型下拉框值改变时触发查询
         */
        queTypeChange() {
            this.handleSearch();
        },
        // 触发搜索按钮
        handleSearch() {
            this.$set(this.query, 'page', this.query.page || 1);
            this.getData();
        },
        //添加按钮
        addQuestionDialog() { // 添加按钮
            this.title = '添加题目';
            this.question = {};
            // this.question.queDifficulty = 5
            this.question.queContent = '';
            this.question.queA = '';
            this.question.queB = '';
            this.question.queC = '';
            this.question.queD = '';
            this.queVisible = true;
        },
        // 编辑按钮
        handleEdit(index, row) {
            this.title = '编辑题目';
            this.idx = index;
            this.temQuestion = row;
            let ids = row.knowledgeIds.split(',');
            console.log('ids', ids);
            this.defaultCheckList = [];
            ids.forEach((id) => {
                this.defaultCheckList.push(id);
            });
            this.question = row;
            this.knowledgeIdList = this.question.knowledgeIds;
            this.question.knowledgeIds = this.question.knowledgeNames;
            this.getKnowledgeNames();
            this.courseChange('dialog', 'edit');
            this.queVisible = true;
        },
        // 题目取消按钮
        clearBtn() {
            this.handleSearch();
            this.queVisible = false;
        },
        // 保存编辑
        save() {
            this.$refs['question'].validate((check) => {
                if (check) {
                    if (this.title === '添加题目') {
                        this.addQuestion();
                    } else if (this.title === '编辑题目') {
                        this.editQuestion();
                    }
                }
            });
        },
        /**
         * 添加题目
         */
        addQuestion() {
            this.question.knowledgeIds = this.knowledgeIdList;
            add(this.getQuestion(this.question)) // 获取实体类对象
                .then((res) => {
                    if (res.success) {
                        this.queVisible = false;
                        this.$message.success(`添加成功`);
                        this.query.queType = this.question.queType;
                        this.queTypeChange();
                        // this.courseList.forEach((course) => {
                        //     if (course.courseId === this.question.queCourseId) {
                        //         this.question.queCourseName = course.courseName;
                        //     }
                        // });
                        // this.queBankList.forEach((queBank) => {
                        //     if (this.question.queBankId === queBank.queBankId) {
                        //         this.question.queBankName = queBank.queBankName;
                        //     }
                        // });
                        // this.question.queCreateByName = localStorage.getItem('ms_username');
                        // this.tableData.push(this.question);
                        this.pageTotal++; // 添加成功后总数加1
                        // this.question = {};
                    } else {
                        this.$message.warning(res.msg);
                    }
                })
                .catch((err) => {
                    this.$message.error('服务器歇菜了,请稍后重试!!');
                });
        },
        /**
         * 获取实体类对象
         */
        getQuestion(question) {
            let obj = new Object();
            obj.queId = question.queId;
            obj.queName = question.queName;
            obj.queContent = question.queContent;
            obj.queType = question.queType;
            obj.queCourseId = question.queCourseId;
            obj.queBankId = question.queBankId;
            obj.knowledgeIds = question.knowledgeIds;
            obj.queAnswer = question.queAnswer;
            obj.queAnswerDescribe = question.queAnswerDescribe;
            obj.queCreateBy = question.queCreateBy;
            obj.queA = question.queA;
            obj.queB = question.queB;
            obj.queC = question.queC;
            obj.queD = question.queD;
            obj.queDifficulty = question.queDifficulty;
            return obj;
        },
        /**
         * 编辑题目
         */
        editQuestion() {
            this.question.knowledgeIds = this.knowledgeIdList;
            edit(this.question)
                .then((res) => {
                    if (res.success) {
                        this.queVisible = false;
                        this.$message.success(`修改成功`);
                        // this.$set(this.tableData, this.idx, this.question);
                        this.queTypeChange();
                        this.question = {};
                    } else {
                        this.$message.warning(res.msg);
                    }
                })
                .catch(() => {
                    this.$message.error(`修改第 ${this.idx + 1} 行,修改失败`);
                });
        },
        // 删除操作
        handleDelete(index, row) {
            // 二次确认删除
            this.$confirm('确定要删除吗？', '提示', {
                type: 'warning'
            })
                .then(() => {
                    this.delIdList.queId = row.queId;
                    this.delIdList.queType = row.queType;
                    del(this.delIdList)
                        .then((res) => {
                            if (res.success) {
                                this.tableData.splice(index, 1);
                                console.log('index==>', index);
                                this.$message.success('删除成功~');
                                this.pageTotal--;
                            } else {
                                this.$message.warning(res.msg);
                            }
                        })
                        .catch(() => {
                            this.$message.error(res.msg);
                        });
                })
                .catch((err) => {
                    console.log('delQuestionErr==>', err);
                });
        },

        // 多选操作
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        /**
         * 删除多选
         */
        delAllSelection() {
            const length = this.multipleSelection.length;
            if (!length || length === 0) {
                this.$message.warning('请至少选择一条数据进行删除!!');
                return;
            }
            let queId = '';
            let str = '';
            for (let i = 0; i < this.multipleSelection.length; i++) {
                for (let index = 0; index < this.tableData.length; index++) {
                    if (this.multipleSelection[i].queId === this.tableData[index].queId) {
                        queId += this.multipleSelection[i].queId + ',';
                        this.delList.push(index);
                    }
                }
            }
            this.question.queId = queId.substring(0, queId.length - 1);
            this.$confirm(`确定要删除这${length}条数据吗？`, '警告', {
                type: 'warning'
            }).then(() => {
                this.delIdList.queId = this.question.queId;
                this.delIdList.queType = this.query.queType;
                del(this.delIdList)
                    .then((res) => {
                        if (res.success) {
                            this.$message.warning(`成功删除了${length}条数据`);
                            // for (let index = 0; index < this.delList.length; index++) {
                            //     this.tableData.splice(this.delList[index], 1);
                            //     this.pageTotal--;
                            // }
                            this.handleSearch();
                            this.multipleSelection = [];
                            this.question = {};
                        } else {
                            this.$$message.warning(res.msg);
                        }
                    })
                    .catch(() => {
                        this.$message.error('服务器歇菜了，请稍后再试！！');
                    });
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
         * 打开知识点树形对话框
         */
        knowledgeDailogHandle() {
            if (!this.question || !this.question.queCourseId) {
                this.$message.warning('请选选择所属课程!!');
                return;
            }
            this.knowledgeVisible = true;
        },
        /**
         * 保存知识点
         */
        saveKnowledge() {
            let checkList = this.$refs.tree.getCheckedNodes();
            let temIds = '';
            if (checkList && checkList.length > 0) {
                console.log('check==>', checkList);
                let temName = '【';
                checkList.forEach((item) => {
                    if (!item.children) {
                        temName += item.knowledgeName + ',';
                        temIds += item.knowledgeId + ',';
                    }
                });
                this.question.knowledgeIds = temName.substring(0, temName.length - 1) + '】';
                this.question.knowledgeNames = temName.substring(0, temName.length - 1) + '】';
                this.knowledgeIdList = temIds.substring(0, temIds.length - 1);
            }
            // } else {
            //     this.question.knowledgeIds = '';
            //     this.question.knowledgeNames = '';
            //     temIds = ',';
            // }
            this.knowledgeVisible = false;
        },
        clearSelection() {
            this.$refs.multipleTable.clearSelection();
        },
        queBankChange() {
            this.$forceUpdate();
        },

        queBankClick() {
            if (this.query.queCourseId === '') {
                this.$message.warning('请先选择所属课程!!');
            }
        }
    }
};
</script>

<style scoped>
.query-margin {
    margin-left: 5px;
}
.handle-box {
    margin-bottom: 20px;
}

.handle-select {
    width: 130px;
}
.handle-input {
    width: 170px;
    display: inline-block;
}
.table {
    width: 100%;
    font-size: 14px;
}

.btn-width {
    width: 100px;
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
.hidden {
    display: none;
}
</style>
