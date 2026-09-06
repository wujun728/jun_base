<template>
  <ArtForm
    ref="genInfoForm"
    v-model="infoModel"
    :items="formItems"
    :rules="rules"
    :show-submit="false"
    :show-reset="false"
    label-width="150px">
    <template #genPath>
      <el-input v-model="infoModel.genPath">
        <template #append>
          <el-dropdown>
            <el-button type="primary">
              最近路径快速选择
              <el-icon class="el-icon--right"><ArrowDown /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="infoModel.genPath = '/'">
                  恢复默认的生成基础路径
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
      </el-input>
    </template>
    <template #view>
      <el-checkbox v-model="infoModel.view">生成详情页</el-checkbox>
    </template>
    <template #genType>
      <el-radio-group v-model="infoModel.genType">
        <el-radio value="0">zip压缩包</el-radio>
        <el-radio value="1">自定义路径</el-radio>
      </el-radio-group>
    </template>
    <template #parentMenuId>
      <el-tree-select
        v-model="infoModel.parentMenuId"
        :data="menuOptions"
        row-key="menuId"
        value-key="menuId"
        :props="{ label: 'menuName', children: 'children' }"
        placeholder="请选择系统菜单"
        check-strictly />
    </template>
  </ArtForm>

  <template v-if="infoModel.tplCategory == 'tree'">
    <h4 class="form-header">其他信息</h4>
    <div class="px-4 pb-4">
      <el-row :gutter="12">
        <el-col :span="12">
          <el-form-item label="树编码字段">
            <el-select v-model="infoModel.treeCode" placeholder="请选择">
              <el-option
                v-for="(column, index) in infoModel.columns"
                :key="index"
                :label="`${column.columnName}：${column.columnComment}`"
                :value="column.columnName!" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="树父编码字段">
            <el-select v-model="infoModel.treeParentCode" placeholder="请选择">
              <el-option
                v-for="(column, index) in infoModel.columns"
                :key="index"
                :label="`${column.columnName}：${column.columnComment}`"
                :value="column.columnName!" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="树名称字段">
            <el-select v-model="infoModel.treeName" placeholder="请选择">
              <el-option
                v-for="(column, index) in infoModel.columns"
                :key="index"
                :label="`${column.columnName}：${column.columnComment}`"
                :value="column.columnName!" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </div>
  </template>

  <template v-if="infoModel.tplCategory == 'sub'">
    <h4 class="form-header">关联信息</h4>
    <div class="px-4 pb-4">
      <el-row :gutter="12">
        <el-col :span="12">
          <el-form-item label="关联子表的表名">
            <el-select
              v-model="infoModel.subTableName"
              placeholder="请选择"
              @change="subSelectChange">
              <el-option
                v-for="(table, index) in tables"
                :key="index"
                :label="`${table.tableName}：${table.tableComment}`"
                :value="table.tableName!" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="子表关联的外键名">
            <el-select v-model="infoModel.subTableFkName" placeholder="请选择">
              <el-option
                v-for="(column, index) in subColumns"
                :key="index"
                :label="`${column.columnName!}：${column.columnComment!}`"
                :value="column.columnName!" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </div>
  </template>
</template>

<script setup lang="ts">
import type { SysMenu } from '@/types/api/system/menu'
import type { GenTable, GenTableColumn } from '@/types/api/tool/gen'

import { ArrowDown } from '@element-plus/icons-vue'

import ArtForm from '@/components/core/forms/art-form/index.vue'
import { handleTree } from '@/utils/sys/ruoyi'

import { listMenu } from '../api'

const formRef = useTemplateRef<InstanceType<typeof ArtForm>>('genInfoForm')

const props = defineProps({
  tables: {
    type: Array as PropType<GenTable[]>,
    default: null
  }
})

const infoModel = defineModel<GenTable>('info', {
  default: () => ({ view: false }) as GenTable
})

const subColumns = ref<GenTableColumn[]>([])
const menuOptions = ref<SysMenu[]>([])

const formItems = computed(() => [
  {
    key: 'tplCategory',
    label: '生成模板',
    type: 'select',
    span: 12,
    props: {
      options: [
        { value: 'crud', label: '单表（增删改查）' },
        { value: 'tree', label: '树表（增删改查）' },
        { value: 'sub', label: '主子表（增删改查）' }
      ]
    }
  },
  {
    key: 'tplWebType',
    label: '前端类型',
    type: 'select',
    span: 12,
    props: {
      options: [
        { value: 'element-ui', label: 'Vue2 Element UI 模版' },
        { value: 'element-plus', label: 'Vue3 Element Plus 模版' },
        {
          value: 'element-plus-typescript',
          label: 'Vue3 Element Plus TypeScript 模版'
        }
      ]
    }
  },
  {
    key: 'packageName',
    label: '生成包路径',
    type: 'input',
    span: 12,
    tooltip: '生成在哪个java包下，例如 com.ruoyi.system'
  },
  {
    key: 'moduleName',
    label: '生成模块名',
    type: 'input',
    span: 12,
    tooltip: '可理解为子系统名，例如 system'
  },
  {
    key: 'businessName',
    label: '生成业务名',
    type: 'input',
    span: 12,
    tooltip: '可理解为功能英文名，例如 user'
  },
  {
    key: 'functionName',
    label: '生成功能名',
    type: 'input',
    span: 12,
    tooltip: '用作类描述，例如 用户'
  },
  {
    key: 'formColNum',
    label: '表单布局',
    type: 'select',
    span: 12,
    tooltip: '选择表单的栅格布局方式',
    props: {
      options: [
        { value: 1, label: '单列' },
        { value: 2, label: '双列' },
        { value: 3, label: '三列' }
      ]
    }
  },
  {
    key: 'view',
    label: '扩展功能',
    span: 12,
    useSlot: true,
    slotName: 'view'
  },
  {
    key: 'genType',
    label: '生成代码方式',
    span: 12,
    tooltip: '默认为zip压缩包下载，也可以自定义生成路径',
    useSlot: true,
    slotName: 'genType'
  },
  {
    key: 'parentMenuId',
    label: '上级菜单',
    span: 12,
    tooltip: '分配到指定菜单下，例如 系统管理',
    useSlot: true,
    slotName: 'parentMenuId'
  },
  {
    key: 'genPath',
    label: '自定义路径',
    span: 24,
    tooltip: '填写磁盘绝对路径，若不填写，则生成到当前Web项目下',
    useSlot: true,
    slotName: 'genPath',
    hidden: infoModel.value?.genType !== '1'
  }
])

const rules = ref({
  tplCategory: [{ required: true, message: '请选择生成模板', trigger: 'blur' }],
  packageName: [
    { required: true, message: '请输入生成包路径', trigger: 'blur' }
  ],
  moduleName: [
    { required: true, message: '请输入生成模块名', trigger: 'blur' }
  ],
  businessName: [
    { required: true, message: '请输入生成业务名', trigger: 'blur' }
  ],
  functionName: [
    { required: true, message: '请输入生成功能名', trigger: 'blur' }
  ]
})

async function validateForm() {
  if (!formRef.value) return
  return await formRef.value.validate()
}

function subSelectChange(value: string) {
  if (infoModel.value) {
    infoModel.value.subTableFkName = ''
  }
}

function setSubTableColumns(value: string) {
  for (const item of props.tables || []) {
    if (value === item.tableName) {
      subColumns.value = item.columns || []
      break
    }
  }
}

async function getMenuTreeselect() {
  const response = await listMenu()
  menuOptions.value = handleTree(response, 'menuId')
}

onMounted(() => {
  getMenuTreeselect()
})

watch(
  () => infoModel.value?.tplCategory,
  (val) => {
    if (val !== 'sub' && infoModel.value) {
      infoModel.value.subTableName = ''
      infoModel.value.subTableFkName = ''
    }
  }
)

watch(
  () => infoModel.value?.subTableName,
  (val) => {
    if (val) {
      infoModel.value.subTableFkName = ''
      setSubTableColumns(val)
    }
  }
)

watch(
  () => infoModel.value?.tplWebType,
  (val) => {
    infoModel.value.tplWebType = 'element-plus-typescript'
  }
)

defineExpose({
  validateForm
})
</script>
