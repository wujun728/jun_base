<script setup>
import { listMenu } from '@/api/system/menu'
import getBaseFormConfig from './config/genInfoFormConfig'
import getComputedConfig from '@/hooks/getPageConfig'

const info = defineModel('info', { type: Object, default: () => ({}) })

const props = defineProps({
  tables: {
    type: Array,
    default: () => [],
  },
})

const proxy = inject('proxy')
const menuOptions = ref([])
const subColumns = ref([])
const subTables = ref([])
const infoColumns = ref([])
const hideItems = ref([''])
const treeHideItems = ['treeCode', 'treeParentCode', 'treeName']
const subHideItems = ['subTableName', 'subTableFkName']
const baseFormRef = useTemplateRef('baseFormRef')

const getMenuTreeselect = () => {
  listMenu().then((response) => {
    menuOptions.value = proxy.handleTree(response.data, 'menuId')
  })
}

const findParentById = (idToFind, data = menuOptions.value) => {
  function searchParents(node) {
    if (node.menuId === idToFind) {
      return node
    }
    if (node.children) {
      for (const child of node.children) {
        const parent = searchParents(child)
        if (parent) {
          return parent
        }
      }
    }
    return null
  }
  for (const topLevelNode of data) {
    const parent = searchParents(topLevelNode)
    if (parent) {
      return parent
    }
  }
  return null
}

const filterTree = (node, value) => {
  if (node.menuName.includes(value)) return true
  const parentId = node.parentId
  if (parentId === 0) return false
  const parent = findParentById(parentId)
  if (parent) {
    return filterTree(parent, value)
  }
}

const dictMap = {
  parentMenuId: menuOptions,
  subTableName: subTables,
  subTableFkName: subColumns,
  treeParentCode: infoColumns,
  treeName: infoColumns,
  treeCode: infoColumns,
}

const otherConfig = {
  filterNodeMethod: (value, data) => {
    if (!value) return true
    return filterTree(data, value)
  },
}

const baseFormConfig = getBaseFormConfig(otherConfig)
const baseFormConfigComputed = computed(() => {
  hideItems.value = []
  if (info.value.genType !== '1') {
    hideItems.value.push('genPath')
  }
  if (info.value.tplCategory !== 'tree') {
    hideItems.value.push(...treeHideItems)
  }
  if (info.value.tplCategory !== 'sub') {
    hideItems.value.push(...subHideItems)
  }
  baseFormConfig.hideItems = hideItems
  return getComputedConfig(baseFormConfig, dictMap)
})

const setSubTableColumns = (value) => {
  for (const item in props.tables) {
    const name = props.tables[item].tableName
    if (value === name) {
      subColumns.value = props.tables[item].columns.map((column) => ({
        label: column.columnName + '：' + column.columnComment,
        value: column.columnName,
      }))
      break
    }
  }
}

watch(
  () => props.tables,
  () => {
    subTables.value = props.tables.map((table) => ({
      label: table.tableName + '：' + table.tableComment,
      value: table.tableName,
    }))
  }
)

watch(
  () => info.value.subTableName,
  (val) => {
    setSubTableColumns(val)
  }
)

watch(
  () => info.value.columns,
  () => {
    infoColumns.value = (info.value.columns || []).map((column) => ({
      label: column.columnName + '：' + column.columnComment,
      value: column.columnName,
    }))
  }
)

const handleValueChange = (value) => {
  info.value = value
}

getMenuTreeselect()

defineExpose({
  baseFormRef,
})
</script>

<template>
  <div class="gen-info-form">
    <BaseForm
      ref="baseFormRef"
      v-bind="baseFormConfigComputed"
      :data="info"
      @update:data="handleValueChange"
    >
      <template #treeCodeHeader>
        <div class="item-header">其他信息</div>
      </template>
      <template #subTableNameHeader>
        <div class="item-header">关联信息</div>
      </template>
    </BaseForm>
  </div>
</template>

<style scoped lang="scss">
.item-header {
  line-height: 32px;
  font-weight: 700;
  font-size: 18px;
  margin-bottom: 18px;
  color: #1677ff;
}
</style>
