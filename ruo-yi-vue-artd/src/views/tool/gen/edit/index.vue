<template>
  <el-card>
    <el-tabs v-model="activeName">
      <el-tab-pane label="基本信息" name="basic">
        <BasicInfoForm ref="basicInfo" v-model="info" />
      </el-tab-pane>
      <el-tab-pane label="字段信息" name="columnInfo">
        <VueDraggable
          target="tbody"
          handle=".allowDrag"
          v-model="columns"
          :animation="150">
          <el-table
            ref="dragTable"
            :data="columns"
            row-key="columnId"
            :max-height="tableHeight">
            <el-table-column
              label="序号"
              type="index"
              fixed="left"
              width="50"
              class-name="allowDrag" />
            <el-table-column
              label="字段列名"
              prop="columnName"
              fixed="left"
              width="120"
              :show-overflow-tooltip="true"
              class-name="allowDrag" />
            <el-table-column label="字段描述" width="120">
              <template #default="scope">
                <el-input v-model="scope.row.columnComment"></el-input>
              </template>
            </el-table-column>
            <el-table-column
              label="物理类型"
              prop="columnType"
              width="120"
              :show-overflow-tooltip="true" />
            <el-table-column label="Java类型" width="120">
              <template #default="scope">
                <el-select v-model="scope.row.javaType">
                  <el-option label="Long" value="Long" />
                  <el-option label="String" value="String" />
                  <el-option label="Integer" value="Integer" />
                  <el-option label="Double" value="Double" />
                  <el-option label="BigDecimal" value="BigDecimal" />
                  <el-option label="Date" value="Date" />
                  <el-option label="Boolean" value="Boolean" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="java属性" width="120">
              <template #default="scope">
                <el-input v-model="scope.row.javaField"></el-input>
              </template>
            </el-table-column>

            <el-table-column label="插入" width="50">
              <template #default="scope">
                <el-checkbox
                  true-value="1"
                  false-value="0"
                  v-model="scope.row.isInsert"></el-checkbox>
              </template>
            </el-table-column>
            <el-table-column label="编辑" width="50">
              <template #default="scope">
                <el-checkbox
                  true-value="1"
                  false-value="0"
                  v-model="scope.row.isEdit"></el-checkbox>
              </template>
            </el-table-column>
            <el-table-column label="列表" width="50">
              <template #default="scope">
                <el-checkbox
                  true-value="1"
                  false-value="0"
                  v-model="scope.row.isList"></el-checkbox>
              </template>
            </el-table-column>
            <el-table-column label="查询" width="50">
              <template #default="scope">
                <el-checkbox
                  true-value="1"
                  false-value="0"
                  v-model="scope.row.isQuery"></el-checkbox>
              </template>
            </el-table-column>
            <el-table-column label="查询方式" width="100">
              <template #default="scope">
                <el-select v-model="scope.row.queryType">
                  <el-option label="=" value="EQ" />
                  <el-option label="!=" value="NE" />
                  <el-option label=">" value="GT" />
                  <el-option label=">=" value="GTE" />
                  <el-option label="<" value="LT" />
                  <el-option label="<=" value="LTE" />
                  <el-option label="LIKE" value="LIKE" />
                  <el-option label="BETWEEN" value="BETWEEN" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="必填" width="50">
              <template #default="scope">
                <el-checkbox
                  true-value="1"
                  false-value="0"
                  v-model="scope.row.isRequired"></el-checkbox>
              </template>
            </el-table-column>
            <el-table-column label="显示类型" width="150">
              <template #default="scope">
                <el-select v-model="scope.row.htmlType">
                  <el-option label="文本框" value="input" />
                  <el-option label="文本域" value="textarea" />
                  <el-option label="下拉框" value="select" />
                  <el-option label="单选框" value="radio" />
                  <el-option label="复选框" value="checkbox" />
                  <el-option label="日期控件" value="datetime" />
                  <el-option label="图片上传" value="imageUpload" />
                  <el-option label="文件上传" value="fileUpload" />
                  <el-option label="富文本控件" value="editor" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="字典类型" width="120">
              <template #default="scope">
                <el-select
                  v-model="scope.row.dictType"
                  clearable
                  filterable
                  placeholder="请选择">
                  <el-option
                    v-for="dict in dictOptions"
                    :key="dict.dictType"
                    :label="dict.dictName"
                    :value="dict.dictType">
                    <span style="float: left">{{ dict.dictName }}</span>
                    <span style="float: right; color: #8492a6; font-size: 13px">
                      {{ dict.dictType }}
                    </span>
                  </el-option>
                </el-select>
              </template>
            </el-table-column>
          </el-table>
        </VueDraggable>
      </el-tab-pane>
      <el-tab-pane label="生成信息" name="genInfo">
        <GenInfoForm ref="genInfo" v-model:info="info" :tables="tables" />
      </el-tab-pane>
    </el-tabs>
    <el-form label-width="100px">
      <div style="text-align: center; margin-left: -100px; margin-top: 10px">
        <el-button type="primary" @click="submitForm()">提交</el-button>
        <el-button @click="close()">返回</el-button>
      </div>
    </el-form>
  </el-card>
</template>

<script setup lang="ts" name="GenEdit">
import type { GenTableInfoResult } from '@/types/api/tool/gen'

import { VueDraggable } from 'vue-draggable-plus'

import { useWorktabStore } from '@/store/modules/worktab'

import {
  getGenTable,
  updateGenTable,
  optionselect as getDictOptionselect
} from './api'
import BasicInfoForm from './components/basic-info-form.vue'
import GenInfoForm from './components/gen-info-form.vue'

const worktabStore = useWorktabStore()

const basicInfo =
  useTemplateRef<InstanceType<typeof BasicInfoForm>>('basicInfo')
const genInfo = useTemplateRef<InstanceType<typeof GenInfoForm>>('genInfo')

const route = useRoute()
const router = useRouter()

const activeName = ref<string>('columnInfo')
const tableHeight = ref<string>(
  document.documentElement.scrollHeight - 245 + 'px'
)
const tables = ref<any[]>([])
const columns = ref<any[]>([])
const dictOptions = ref<any[]>([])
const info = ref<Record<string, any>>({})

/** 提交按钮 */
async function submitForm() {
  if (!basicInfo.value || !genInfo.value) {
    ElMessage.error('表单实例不存在')
    return
  }
  try {
    await basicInfo.value.validateForm()
    await genInfo.value.validateForm()
  } catch (error) {
    ElMessage.error('表单校验未通过，请重新检查提交内容')
    return
  }

  const genTable = Object.assign({}, info.value)
  genTable.columns = columns.value
  genTable.params = {
    genView: info.value.view ? '1' : '0',
    treeCode: info.value.treeCode,
    treeName: info.value.treeName,
    treeParentCode: info.value.treeParentCode,
    parentMenuId: info.value.parentMenuId
  }
  updateGenTable(genTable).then((res) => {
    ElMessage.success('修改成功')
    close()
  })
}

function getFormPromise(form: any): Promise<boolean> {
  return new Promise((resolve) => {
    form.validate((res: boolean) => {
      resolve(res)
    })
  })
}

function close() {
  const currentPath = route.path
  worktabStore.removeTab(currentPath)
  const genTab = worktabStore.getTab('/tool/gen')

  if (genTab) {
    router.push({ path: genTab.path, query: { t: Date.now() } })
  }
}

async function init() {
  const dictRes = await getDictOptionselect()
  dictOptions.value = dictRes

  const tableId = route.params && route.params.tableId
  const tableRes = await getGenTable(Number(tableId))
  columns.value = tableRes.rows
  info.value = tableRes.info
  tables.value = tableRes.tables
}

// 拖动排序
onMounted(async () => {
  await init()
})
</script>
