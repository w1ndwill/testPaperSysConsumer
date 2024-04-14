<template>
    <div id="Knowledge">
        <div class="container">
            <el-table
                :data="data"
                border
                class="table"
                header-cell-class-name="table-header"
                ref="multipleTable"
            >
                <el-table-column align="center" label="序号" type="index" width="55"></el-table-column>
                <el-table-column :show-overflow-tooltip="true" label="知识点名字" property="knowledgeName" width="200"></el-table-column>
                <el-table-column :show-overflow-tooltip="true" label="描述" property="knowledgeDescribe"></el-table-column>
            </el-table>
        </div>
    </div>
</template>
<script>
export default {
    props: {
        data: Array
    },
    data() {
        return {};
    }
};
</script>
<style scoped>
.table {
    width: 100%;
    font-size: 14px;
    max-height: 500px;
    overflow:auto;
}
</style>