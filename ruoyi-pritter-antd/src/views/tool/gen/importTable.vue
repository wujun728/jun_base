<script setup>
import { listDbTable, importTable } from '@/api/tool/gen'
import to from '@/utils/to'
import { SearchOutlined, ReloadOutlined } from '@ant-design/icons-vue'

const emit = defineEmits(['ok'])
const proxy = inject('proxy')

const visible = ref(false)
const importLoading = ref(false)
const dbTableList = ref([])
const selectedRowKeys = ref([])
const queryRef = useTemplateRef('queryRef')

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  tableName: undefined,
  tableComment: undefined,
})

const columns = [
  { title: '表名称', dataIndex: 'tableName', ellipsis: true },
  { title: '表描述', dataIndex: 'tableComment', ellipsis: true },
  { title: '创建时间', dataIndex: 'createTime', width: 160 },
  { title: '更新时间', dataIndex: 'updateTime', width: 160 },
]

const rowSelection = computed(() => ({
  selectedRowKeys: selectedRowKeys.value,
  onChange: (keys) => {
    selectedRowKeys.value = keys
  },
}))

const show = () => {
  selectedRowKeys.value = []
  getList()
  visible.value = true
}

const getList = () => {
  listDbTable(queryParams).then((res) => {
    dbTableList.value = res.rows
  })
}

const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

const resetQuery = () => {
  queryParams.tableName = undefined
  queryParams.tableComment = undefined
  handleQuery()
}

const clickRow = (record) => {
  const key = record.tableName
  const index = selectedRowKeys.value.indexOf(key)
  if (index >= 0) {
    selectedRowKeys.value = selectedRowKeys.value.filter((item) => item !== key)
  } else {
    selectedRowKeys.value = [...selectedRowKeys.value, key]
  }
}

const handleImportTable = async () => {
  const tableNames = selectedRowKeys.value.join(',')
  if (!tableNames) {
    proxy.$modal.msgError('请选择要导入的表')
    return
  }
  importLoading.value = true
  const [res] = await to(importTable({ tables: tableNames, tplWebType: 'lmw' }))
  importLoading.value = false
  if (res) {
    proxy.$modal.msgSuccess(res.msg)
    if (res.code === 200) {
      visible.value = false
      emit('ok')
    }
  }
}

defineExpose({ show })
</script>

<template>
  <a-modal
    v-model:open="visible"
    title="导入表"
    :width="getWidth('800px')"
    destroy-on-close
    :confirm-loading="importLoading"
    @ok="handleImportTable"
  >
    <a-form ref="queryRef" :model="queryParams" layout="inline" class="import-form">
      <a-form-item label="表名称" name="tableName">
        <a-input
          v-model:value="queryParams.tableName"
          placeholder="请输入表名称"
          allow-clear
          @press-enter="handleQuery"
        />
      </a-form-item>
      <a-form-item label="表描述" name="tableComment">
        <a-input
          v-model:value="queryParams.tableComment"
          placeholder="请输入表描述"
          allow-clear
          @press-enter="handleQuery"
        />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" @click="handleQuery">
          <SearchOutlined />
          搜索
        </a-button>
        <a-button class="ml6" @click="resetQuery">
          <ReloadOutlined />
          重置
        </a-button>
      </a-form-item>
    </a-form>
    <a-table
      row-key="tableName"
      :columns="columns"
      :data-source="dbTableList"
      :row-selection="rowSelection"
      :pagination="false"
      :scroll="{ y: 260 }"
      size="small"
      :custom-row="(record) => ({ onClick: () => clickRow(record) })"
    />
  </a-modal>
</template>

<style scoped lang="scss">
.import-form {
  margin-bottom: 16px;
}
</style>
