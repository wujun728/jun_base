<script setup>
import { computed, watch } from 'vue'
import { listMenu } from '@/api/system/menu'
import getBaseFormConfig from './config/genInfoFormConfig'
import getComputedConfig from '@/hooks/getPageConfig'

const props = defineProps({
  info: {
    type: Object,
    default: () => ({}),
  },
  tables: {
    type: Array,
    default: () => [],
  },
})
const emits = defineEmits(['update:info'])
const proxy = inject('proxy')
const menuOptions = ref([])
const subColumns = ref([])
const subTables = ref([])
const infoColumns = ref([])
const hideItems = ref([''])
const treeHideItems = ['treeCode', 'treeParentCode', 'treeName']
const subHideItems = ['subTableName', 'subTableFkName']
const baseFormRef = ref(null)
const getMenuTreeselect = () => {
  listMenu().then((response) => {
    menuOptions.value = proxy.handleTree(response.data, 'menuId')
  })
}

// 递归查找 menuId和parentId相同的项目
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
  // 遍历顶层数据
  for (const topLevelNode of data) {
    const parent = searchParents(topLevelNode)
    if (parent) {
      return parent
    }
  }
  // 若在整个数据结构中都没有找到对应id的父节点，则返回null
  return null
}
const filterTree = (node, value) => {
  if (node.menuName.includes(value)) return true
  const parentId = node.parentId
  if (parentId === 0) return false
  // 根据 parentId去查找data 找到 menuId等于parentId的节点
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
  if (props.info.genType !== '1') {
    hideItems.value.push('genPath')
  }
  if (props.info.tplCategory !== 'tree') {
    hideItems.value.push(...treeHideItems)
  }
  if (props.info.tplCategory !== 'sub') {
    hideItems.value.push(...subHideItems)
  }
  baseFormConfig.hideItems = hideItems
  return getComputedConfig(baseFormConfig, dictMap)
})
const setSubTableColumns = (value) => {
  for (const item in props.tables) {
    const name = props.tables[item].tableName
    if (value === name) {
      subColumns.value = props.tables[item].columns.map((column) => {
        return {
          label: column.columnName + '：' + column.columnComment,
          value: column.columnName,
        }
      })
      break
    }
  }
}
watch(
  () => props.tables,
  () => {
    subTables.value = props.tables.map((table) => {
      return {
        label: table.tableName + '：' + table.tableComment,
        value: table.tableName,
      }
    })
  }
)
watch(
  () => props.info.subTableName,
  (val) => {
    setSubTableColumns(val)
  }
)
watch(
  () => props.info.columns,
  () => {
    infoColumns.value = props.info.columns.map((column) => {
      return {
        label: column.columnName + '：' + column.columnComment,
        value: column.columnName,
      }
    })
  }
)
const init = () => {
  getMenuTreeselect()
}
const handleValueChange = (value) => {
  emits('update:info', value)
}
init()

defineExpose({
  baseFormRef,
})
</script>
<template>
  <div class="genInfoForm">
    <BaseForm
      ref="baseFormRef"
      v-bind="baseFormConfigComputed"
      :data="info"
      @update:data="handleValueChange"
    >
      <template #treeCodeHeader>
        <div class="itemHeader">其他信息</div>
      </template>
      <template #subTableNameHeader>
        <div class="itemHeader">关联信息</div>
      </template>
    </BaseForm>
  </div>
</template>

<style scoped lang="scss">
.itemHeader {
  line-height: 32px;
  font-weight: 700;
  font-size: 18px;
  margin-bottom: 18px;
  color: var(--el-color-primary);
}
</style>
