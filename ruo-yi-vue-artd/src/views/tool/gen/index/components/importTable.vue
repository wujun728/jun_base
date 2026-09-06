<template>
  <el-dialog
    title="导入表"
    v-model="visible"
    width="800px"
    top="5vh"
    append-to-body>
    <el-form :model="queryParams" ref="queryRef" :inline="true">
      <el-form-item label="表名称" prop="tableName">
        <el-input
          v-model="queryParams.tableName"
          placeholder="请输入表名称"
          clearable
          style="width: 180px"
          @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="表描述" prop="tableComment">
        <el-input
          v-model="queryParams.tableComment"
          placeholder="请输入表描述"
          clearable
          style="width: 180px"
          @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleQuery">
          搜索
        </el-button>
        <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row>
      <el-table
        ref="tableRef"
        :data="dbTableList"
        @row-click="clickRow"
        @selection-change="handleSelectionChange"
        height="260px">
        <el-table-column type="selection" width="55" />
        <el-table-column
          prop="tableName"
          label="表名称"
          :show-overflow-tooltip="true" />
        <el-table-column
          prop="tableComment"
          label="表描述"
          :show-overflow-tooltip="true" />
        <el-table-column prop="createTime" label="创建时间" />
        <el-table-column prop="updateTime" label="更新时间" />
      </el-table>
      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList" />
    </el-row>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="visible = false">取 消</el-button>
        <el-button type="primary" @click="handleImportTable">确 定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import type { GenQueryParams, GenTable } from '@/types/api/tool/gen'

import { Search, Refresh } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { ElTable } from 'element-plus'

import { importTable, listDbTable } from '../api'

const total = ref(0)
const visible = ref(false)
const tables = ref<string[]>([])
const dbTableList = ref<GenTable[]>([])
const tableRef = useTemplateRef<InstanceType<typeof ElTable>>('tableRef')

const queryParams = reactive<GenQueryParams>({
  pageNum: 1,
  pageSize: 10,
  tableName: undefined,
  tableComment: undefined
})

const emit = defineEmits(['ok'])

function show() {
  getList()
  visible.value = true
}

function clickRow(row: GenTable) {
  ;(tableRef.value as any)?.toggleRowSelection(row)
}

function handleSelectionChange(selection: GenTable[]) {
  tables.value = selection.map((item) => item.tableName!)
}

function getList() {
  listDbTable(queryParams).then((res) => {
    dbTableList.value = res.rows
    total.value = res.total
  })
}

function handleQuery() {
  queryParams.pageNum = 1
  getList()
}

function resetQuery() {
  queryParams.tableName = undefined
  queryParams.tableComment = undefined
  handleQuery()
}

function handleImportTable() {
  const tableNames = tables.value.join(',')
  if (tableNames === '') {
    ElMessage.error('请选择要导入的表')
    return
  }
  importTable({
    tables: tableNames,
    tplWebType: 'element-plus-typescript'
  }).then((res: any) => {
    ElMessage.success(res.msg || '导入成功')
    visible.value = false
    emit('ok')
  })
}

defineExpose({ show })
</script>
