<script setup>
import { listDbColumn, listDbTable } from '@modules/online/api/db';
import { reactive } from 'vue';
const total = ref(0);
const queryParams = ref({
    pageNum: 1,
    pageSize: 10,
})
const tables = ref([])
const columns = reactive({})
function getList() {
    listDbTable(queryParams.value).then(res => {
        tables.value = res.rows
        total.value = res.total
    })
}

function collapseChange(name) {
    columns[name] = []
    listDbColumn(name).then(res => {
        columns[name] = res.rows
    })
}
function expandChange(row, expandedRows) {
    collapseChange(row.TABLE_NAME)
}
getList()
</script>

<template>
    <div class="app-container">
        <el-card shadow="never">
            <el-table :data="tables" border style="width: 100%" @expand-change="expandChange">
                <el-table-column type="expand">
                    <template #default="props">
                        <el-table :data="columns[props.row.TABLE_NAME]" border height="300">
                            <el-table-column label="COLUMN_NAME" prop="COLUMN_NAME" />
                            <el-table-column label="COLUMN_TYPE" prop="COLUMN_TYPE" />
                            <el-table-column label="COLUMN_COMMENT" prop="COLUMN_COMMENT" />
                        </el-table>
                    </template>
                </el-table-column>
                <el-table-column label="TABLE_NAME" prop="TABLE_NAME" />
                <el-table-column label="TABLE_COLLATION" prop="TABLE_COLLATION" />
                <el-table-column label="TABLE_COMMENT" prop="TABLE_COMMENT" />
                <el-table-column label="CREATE_TIME" prop="CREATE_TIME" />
                <el-table-column label="UPDATE_TIME" prop="UPDATE_TIME" />
            </el-table>
            <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
                v-model:limit="queryParams.pageSize" @pagination="getList" />
        </el-card>
    </div>
</template>