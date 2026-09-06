<script setup name="GenEdit">
import { getGenTable, updateGenTable } from '@/api/tool/gen'
import { optionselect as getDictOptionselect } from '@/api/system/dict/type'
import BasicInfoForm from './basicInfoForm.vue'
import GenInfoForm from './genInfoForm.vue'

const route = useRoute()
const proxy = inject('proxy')
const basicInfoRef = useTemplateRef('basicInfoRef')
const genInfoRef = useTemplateRef('genInfoRef')

const activeName = ref('columnInfo')
const tableScrollY = ref(400)
const tables = ref([])
const columns = ref([])
const dictOptions = ref([])
const info = ref({})

const javaTypeOptions = ['Long', 'String', 'Integer', 'Double', 'BigDecimal', 'Date', 'Boolean']
const queryTypeOptions = [
  { label: '=', value: 'EQ' },
  { label: '!=', value: 'NE' },
  { label: '>', value: 'GT' },
  { label: '>=', value: 'GTE' },
  { label: '<', value: 'LT' },
  { label: '<=', value: 'LTE' },
  { label: 'LIKE', value: 'LIKE' },
  { label: 'BETWEEN', value: 'BETWEEN' },
]
const htmlTypeOptions = [
  { label: '文本框', value: 'input' },
  { label: '文本域', value: 'textarea' },
  { label: '下拉框', value: 'select' },
  { label: '单选框', value: 'radio' },
  { label: '复选框', value: 'checkbox' },
  { label: '纯数字', value: 'inputNumber' },
  { label: '日期控件', value: 'datetime' },
]

const columnTableColumns = [
  { title: '序号', width: 60, customRender: ({ index }) => index + 1 },
  { title: '字段列名', dataIndex: 'columnName', width: 120, ellipsis: true },
  { title: '字段描述', key: 'columnComment', width: 120 },
  { title: '物理类型', dataIndex: 'columnType', width: 120, ellipsis: true },
  { title: 'Java类型', key: 'javaType', width: 120 },
  { title: 'java属性', key: 'javaField', width: 120 },
  { title: '插入', key: 'isInsert', width: 70, align: 'center' },
  { title: '编辑', key: 'isEdit', width: 70, align: 'center' },
  { title: '列表', key: 'isList', width: 70, align: 'center' },
  { title: '查询', key: 'isQuery', width: 70, align: 'center' },
  { title: '查询方式', key: 'queryType', width: 120 },
  { title: '必填', key: 'isRequired', width: 70, align: 'center' },
  { title: '显示类型', key: 'htmlType', width: 120 },
  { title: '字典类型', key: 'dictType', width: 160 },
]

const toggleCheck = (record, field, checked) => {
  record[field] = checked ? '1' : '0'
}

const updateTableHeight = () => {
  tableScrollY.value = Math.max(window.innerHeight - 280, 300)
}

const submitForm = async () => {
  try {
    await basicInfoRef.value.basicInfoFormRef.validate()
    await genInfoRef.value.baseFormRef.getFormValidate()
    const genTable = Object.assign({}, info.value)
    genTable.columns = columns.value
    genTable.params = {
      treeCode: info.value.treeCode,
      treeName: info.value.treeName,
      treeParentCode: info.value.treeParentCode,
      parentMenuId: info.value.parentMenuId,
    }
    genTable.tplWebType = 'lmw'
    const res = await updateGenTable(genTable)
    proxy.$modal.msgSuccess(res.msg)
    if (res.code === 200) {
      close()
    }
  } catch {
    proxy.$modal.msgError('表单校验未通过，请重新检查提交内容')
  }
}

const close = () => {
  proxy.$tab.closeOpenPage({
    path: '/tool/gen',
    query: { t: Date.now(), pageNum: route.query.pageNum },
  })
}

const tableId = route.params?.tableId
if (tableId) {
  getGenTable(tableId).then((res) => {
    columns.value = res.data.rows
    info.value = res.data.info
    if (info.value.parentMenuId) {
      info.value.parentMenuId = Number(info.value.parentMenuId)
    }
    tables.value = res.data.tables
  })
  getDictOptionselect().then((response) => {
    dictOptions.value = response.data
  })
}

onMounted(() => {
  updateTableHeight()
  window.addEventListener('resize', updateTableHeight)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', updateTableHeight)
})
</script>

<template>
  <a-card class="gen-edit-page default-main">
    <a-tabs v-model:activeKey="activeName">
      <a-tab-pane key="basic" tab="基本信息">
        <BasicInfoForm ref="basicInfoRef" :info="info" />
      </a-tab-pane>

      <a-tab-pane key="columnInfo" tab="字段信息">
        <a-table
          row-key="columnId"
          :columns="columnTableColumns"
          :data-source="columns"
          :pagination="false"
          :scroll="{ x: 1600, y: tableScrollY }"
          size="small"
          bordered
        >
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'columnComment'">
              <a-input v-model:value="record.columnComment" size="small" />
            </template>
            <template v-else-if="column.key === 'javaType'">
              <a-select v-model:value="record.javaType" size="small" style="width: 100%">
                <a-select-option
                  v-for="item in javaTypeOptions"
                  :key="item"
                  :value="item"
                >
                  {{ item }}
                </a-select-option>
              </a-select>
            </template>
            <template v-else-if="column.key === 'javaField'">
              <a-input v-model:value="record.javaField" size="small" />
            </template>
            <template v-else-if="column.key === 'isInsert'">
              <a-checkbox
                :checked="record.isInsert === '1'"
                @change="(e) => toggleCheck(record, 'isInsert', e.target.checked)"
              />
            </template>
            <template v-else-if="column.key === 'isEdit'">
              <a-checkbox
                :checked="record.isEdit === '1'"
                @change="(e) => toggleCheck(record, 'isEdit', e.target.checked)"
              />
            </template>
            <template v-else-if="column.key === 'isList'">
              <a-checkbox
                :checked="record.isList === '1'"
                @change="(e) => toggleCheck(record, 'isList', e.target.checked)"
              />
            </template>
            <template v-else-if="column.key === 'isQuery'">
              <a-checkbox
                :checked="record.isQuery === '1'"
                @change="(e) => toggleCheck(record, 'isQuery', e.target.checked)"
              />
            </template>
            <template v-else-if="column.key === 'queryType'">
              <a-select v-model:value="record.queryType" size="small" style="width: 100%">
                <a-select-option
                  v-for="item in queryTypeOptions"
                  :key="item.value"
                  :value="item.value"
                >
                  {{ item.label }}
                </a-select-option>
              </a-select>
            </template>
            <template v-else-if="column.key === 'isRequired'">
              <a-checkbox
                :checked="record.isRequired === '1'"
                @change="(e) => toggleCheck(record, 'isRequired', e.target.checked)"
              />
            </template>
            <template v-else-if="column.key === 'htmlType'">
              <a-select v-model:value="record.htmlType" size="small" style="width: 100%">
                <a-select-option
                  v-for="item in htmlTypeOptions"
                  :key="item.value"
                  :value="item.value"
                >
                  {{ item.label }}
                </a-select-option>
              </a-select>
            </template>
            <template v-else-if="column.key === 'dictType'">
              <a-select
                v-model:value="record.dictType"
                allow-clear
                show-search
                placeholder="请选择"
                size="small"
                style="width: 100%"
                :options="
                  dictOptions.map((dict) => ({
                    label: dict.dictName,
                    value: dict.dictType,
                  }))
                "
              />
            </template>
          </template>
        </a-table>
      </a-tab-pane>

      <a-tab-pane key="genInfo" tab="生成信息">
        <GenInfoForm ref="genInfoRef" v-model:info="info" :tables="tables" />
      </a-tab-pane>
    </a-tabs>

    <div class="form-actions">
      <a-button type="primary" @click="submitForm">提交</a-button>
      <a-button @click="close">返回</a-button>
    </div>
  </a-card>
</template>

<style scoped lang="scss">
.form-actions {
  display: flex;
  justify-content: center;
  gap: 12px;
  margin-top: 16px;
}
</style>
