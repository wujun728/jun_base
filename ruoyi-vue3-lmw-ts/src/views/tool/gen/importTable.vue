<template>
  <!-- 导入表 -->
  <el-dialog
    title="导入表"
    v-model="visible"
    :width="getWidth('800px')"
    top="5vh"
    draggable
    append-to-body
  >
    <div class="ml15 mr15">
      <el-form class="mt15" :model="queryParams" ref="queryRef" :inline="true">
        <el-form-item label="表名称" prop="tableName">
          <el-input
            v-model="queryParams.tableName"
            placeholder="请输入表名称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="表描述" prop="tableComment">
          <el-input
            v-model="queryParams.tableComment"
            placeholder="请输入表描述"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery"
            >搜索</el-button
          >
          <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <el-row>
        <el-table
          @row-click="clickRow"
          ref="tableRef"
          :data="dbTableList"
          @selection-change="handleSelectionChange"
          height="260px"
        >
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column
            prop="tableName"
            label="表名称"
            :show-overflow-tooltip="true"
          ></el-table-column>
          <el-table-column
            prop="tableComment"
            label="表描述"
            :show-overflow-tooltip="true"
          ></el-table-column>
          <el-table-column prop="createTime" label="创建时间"></el-table-column>
          <el-table-column prop="updateTime" label="更新时间"></el-table-column>
        </el-table>
      </el-row>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="handleImportTable">确 定</el-button>
        <el-button @click="visible = false">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { listDbTable, importTable } from '@/api/tool/gen'
import to from '@/utils/to'
import { proxy } from '@/utils/provide'

const total = ref(0)
const visible = ref(false)
const tables = ref<string[]>([])
const dbTableList = ref([])
const tableRef = useTemplateRef('tableRef')
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  tableName: undefined,
  tableComment: undefined,
})
const queryRef = useTemplateRef('queryRef')
const emit = defineEmits(['ok'])

/** 查询参数列表 */
function show() {
  getList()
  visible.value = true
}
/** 单击选择行 */
function clickRow(row: ImportTableEntry) {
  tableRef.value?.toggleRowSelection(row)
}
/** 多选框选中数据 */
function handleSelectionChange(selection: ImportTableEntry[]) {
  tables.value = selection.map((item) => item.tableName)
}
/** 查询表数据 */
function getList() {
  listDbTable(queryParams).then((res) => {
    dbTableList.value = res.rows
    total.value = res.total
  })
}
/** 搜索按钮操作 */
function handleQuery() {
  queryParams.pageNum = 1
  getList()
}
/** 重置按钮操作 */
function resetQuery() {
  queryRef.value?.resetFields()
  handleQuery()
}
/** 导入按钮操作 */
const importLoading = ref(false)
async function handleImportTable() {
  const tableNames = tables.value.join(',')
  if (tableNames == '') {
    proxy.$modal.msgError('请选择要导入的表')
    return
  }
  importLoading.value = true
  const [res] = await to(importTable({ tables: tableNames, tplWebType: 'lmw' }))
  if (res) {
    proxy.$modal.msgSuccess(res.msg)
    if (res.code === 200) {
      visible.value = false
      emit('ok')
    }
  }
  importLoading.value = false
}

defineExpose({
  show,
})
</script>
