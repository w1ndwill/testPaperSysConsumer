<template>
    <div id="Knowledge">
        <div class="container">
            <div class="handle-box">
                <el-button @click="addKnowledgeDialog" class="handle-del mr10" icon="el-icon-lx-add" type="primary">添加知识点 </el-button>
                <el-input
                    @keyup.enter.native="knowledgeHandleSearch"
                    class="handle-input mr10"
                    placeholder="知识点名"
                    v-model="query.knowledgeName"
                ></el-input>
                <el-button @click="knowledgeHandleSearch" icon="el-icon-search" type="primary">搜索</el-button>
            </div>
            <el-table
                :data="tableData"
                @selection-change="knowledgeHandleSelectionChange"
                border
                class="table"
                header-cell-class-name="table-header"
                ref="multipleTable"
                v-loading="loading"
            >
                <el-table-column align="center" label="序号" type="index" width="55"></el-table-column>
                <el-table-column :show-overflow-tooltip="true" label="知识点名字" property="knowledgeName" width="200"></el-table-column>
                <el-table-column :show-overflow-tooltip="true" label="描述" property="knowledgeDescribe"></el-table-column>
                <el-table-column align="center" label="操作" width="180">
                    <template slot-scope="scope">
                        <el-row>
                            <el-button
                                @click="knowledgeHandleEdit(scope.$index, scope.row)"
                                class="btn-width"
                                icon="el-icon-edit"
                                type="text"
                                >编辑
                            </el-button>
                            <el-button
                                @click="knowledgeHandleDelete(scope.$index, scope.row)"
                                class="btn-width,red"
                                icon="el-icon-delete"
                                type="text"
                                style="color: red"
                                >删除
                            </el-button>
                        </el-row>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination
                    :current-page="query.page"
                    :page-size="query.pageSize"
                    :page-sizes="[5, 7, 10, 15, 20, 40, 100]"
                    :total="pageTotal"
                    @current-change="knowledgeHandlePageChange"
                    @size-change="knowledgeHandleSizeChange"
                    background
                    layout="total, sizes, prev, pager, next, jumper"
                ></el-pagination>
            </div>
        </div>
        <!-- knowledge弹出框 -->
        <el-dialog v-dialogDrag :title="title" :visible.sync="knowledgeVisible" center width="50%">
            <el-form :model="knowledge" label-width="100px" ref="knowledge">
                <el-form-item label="知识点名:" prop="knowledgeName" :rules="rules.Required">
                    <el-input placeholder="请输入知识点名" v-model="knowledge.knowledgeName"></el-input>
                </el-form-item>
                <el-form-item label="知识点描述:">
                    <el-input placeholder="请输入知识点描述" rows="10" type="textarea" v-model="knowledge.knowledgeDescribe"></el-input>
                </el-form-item>
            </el-form>
            <span class="dialog-footer" slot="footer">
                <el-button @click="knowledgeClearBtn()">取 消</el-button>
                <el-button @click="knowledgeSave" type="primary">保存知识点</el-button>
            </span>
        </el-dialog>
    </div>
</template>
<script>
import { knowledgeQuery, add, edit, del } from '@/api/knowledge';
import rules from '@/utils/rules';
export default {
    props: {
        courseId: String,
        row: Object
    },
    data() {
        return {
            rules,
            //   ============================↓ knowledge  ↓==================
            loading: true,
            tableData: [],
            knowledgeVisible: false,
            title: '',
            query: {
                courseId: this._props.courseId,
                knowledgeName: '',
                userId: localStorage.getItem('userId'),
                page: 1,
                pageSize: 7
            },
            knowledge: {
                knowledgeId: '',
                knowledgeName: '',
                knowledgeDescribe: '',
                isChangeKnowledgeName: 'F'
            },
            temKnowledgeName: '',
            multipleSelection: [],
            delList: {
                knowledgeId: ''
            },
            pageTotal: 0,
            idx: -1,
            id: -1
        };
    },
    // created() {
    //     this.initKnowledge();
    // },
    watch: {
        knowledgeVisible() {
            this.$nextTick(() => {
                this.$refs.knowledge.clearValidate();
            });
        }
    },
    methods: {
        // ====================== 知识点操作方法 ==============================================
        initKnowledge(row) {
            if (!row) {
                row = this._props.row;
            }
            if (row && row.knowledgeNo <= 0) {
                this.$confirm('该课程还没有添加知识点,是否添加知识点？', '提示', {
                    type: 'warning'
                }).then(() => {
                    this.addKnowledgeDialog();
                });
                this.loading = false;
            } else {
                this.loading = true;
                this.query.courseId = row.courseId;
                this.knowledgeGetData();
            }
        },
        // 查询知识点列表
        knowledgeGetData() {
            knowledgeQuery(this.query)
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
                    if (this.tableData.length === 0 && this.query.page !== 1) {
                        this.query.page = 1;
                        this.knowledgeGetData();
                    }
                })
                .catch(() => {
                    this.loading = false;
                    this.$message.error('服务器歇菜了,请稍后再试!');
                });
        },
        // 触发搜索按钮
        knowledgeHandleSearch() {
            this.$set(this.query, 'page', this.query.page || 1);
            this.knowledgeGetData();
        },
        // 分页导航
        knowledgeHandlePageChange(val) {
            this.query.page = val;
            this.$set(this.query, 'page', val);
            this.knowledgeGetData();
        },
        // 页面条数改变
        knowledgeHandleSizeChange(val) {
            this.$set(this.query, 'pageSize', val);
            this.knowledgeGetData();
        },
        // 多选操作
        knowledgeHandleSelectionChange(val) {
            this.multipleSelection = val;
        },
        // 添加课程弹出框
        addKnowledgeDialog() {
            this.knowledge = {};
            this.title = '添加知识点';
            this.knowledgeVisible = true;
            setTimeout(() => {
                document.elementFromPoint(100, 100).click();
            }, 10);
        },
        // 弹出编辑框
        knowledgeHandleEdit(index, row) {
            this.title = '编辑知识点';
            this.idx = index;
            this.knowledge = row;
            //保存当前的知识点名
            this.temKnowledgeName = row.knowledgeName;
            this.knowledgeVisible = true;
            setTimeout(() => {
                document.elementFromPoint(100, 100).click();
            }, 10);
        },
        // 删除知识点
        knowledgeHandleDelete(index, row) {
            // 二次确认删除
            this.$confirm(`确定将【${row.knowledgeName}】删除吗？`, '提示', {
                type: 'warning'
            }).then(() => {
                this.delList.knowledgeId = row.knowledgeId;
                del(this.delList)
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
            });
        },
        // 取消按钮
        knowledgeClearBtn() {
            this.knowledgeHandleSearch();
            this.knowledgeVisible = false;
        },
        // 保存编辑/添加
        knowledgeSave() {
            this.$refs.knowledge.validate((flag) => {
                if (flag) {
                    if (this.title === '添加知识点') {
                        this.addknowledge();
                        this.knowledgeVisible = false;
                    } else if (this.title === '编辑知识点') {
                        this.knowledgeVisible = false;
                        this.knowledge.isChangeKnowledgeName = 'F';
                        this.editKnowledge();
                    }
                }
            });
        },
        addknowledge() {
            add(this.getKnowlekge(this.knowledge))
                .then((res) => {
                    if (res.success) {
                        this.$message.success('添加成功~');
                        // this.tableData.push(this.knowledge);
                        // this.pageTotal++;
                        this.knowledgeHandleSearch();
                    } else {
                        this.$message.warning(res.msg);
                    }
                })
                .catch(() => {
                    this.$message.error('服务器歇菜了,请稍后再试!!');
                });
        },
        getKnowlekge(knowledge) {
            let obj = new Object();
            obj.courseId = this._props.courseId;
            obj.knowledgeName = knowledge.knowledgeName;
            obj.knowledgeDescribe = knowledge.knowledgeDescribe;
            return obj;
        },
        editKnowledge() {
            if (this.knowledge.knowledgeName !== this.temKnowledgeName) {
                this.knowledge.isChangeKnowledgeName = 'T';
            }
            edit(this.knowledge)
                .then((res) => {
                    if (res.success) {
                        this.$message.success(`修改第 ${this.idx + 1} 行成功`);
                        this.$set(this.tableData, this.idx, this.knowledge);
                        this.knowledge = {};
                    } else {
                        this.knowledge.knowledgeName = this.temKnowledgeName;
                        this.$message.warning(res.msg);
                    }
                })
                .catch(() => {
                    this.knowledge.knowledgeName = this.temKnowledgeName;
                    this.$message.error(`服务器歇菜了,请稍后重试!!`);
                });
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
    max-height: 500px;
    overflow: auto;
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