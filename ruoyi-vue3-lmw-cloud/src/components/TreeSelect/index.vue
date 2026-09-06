<template>
  <div class="el-tree-select">
    <el-select
      style="width: 100%"
      v-model="valueId"
      ref="treeSelectRef"
      :filterable="true"
      :clearable="true"
      @clear="clearHandle"
      :filter-method="selectFilterData"
      :placeholder="placeholder"
    >
      <el-option :value="valueId" :label="valueTitle">
        <el-tree
          id="tree-option"
          ref="selectTreeRef"
          :accordion="accordion"
          :data="options"
          :props="objMap"
          :node-key="objMap.value"
          :expand-on-click-node="false"
          :default-expanded-keys="defaultExpandedKey"
          :filter-node-method="filterNode"
          @node-click="handleNodeClick"
        ></el-tree>
      </el-option>
    </el-select>
  </div>
</template>

<script setup>
const proxy = inject('proxy')

const props = defineProps({
  /* 配置项 */
  objMap: {
    type: Object,
    default: () => {
      return {
        value: 'id', // ID字段名
        label: 'label', // 显示名称
        children: 'children', // 子级字段名
      }
    },
  },
  /* 自动收起 */
  accordion: {
    type: Boolean,
    default: () => {
      return false
    },
  },
  /**当前双向数据绑定的值 */
  value: {
    type: [String, Number],
    default: '',
  },
  /**当前的数据 */
  options: {
    type: Array,
    default: () => [],
  },
  /**输入框内部的文字 */
  placeholder: {
    type: String,
    default: '',
  },
})

const emit = defineEmits(['update:value'])
const treeSelectRef = ref(null)
const selectTreeRef = ref(null)
const valueId = computed({
  get: () => props.value,
  set: (val) => {
    emit('update:value', val)
  },
})
const valueTitle = ref('')
const defaultExpandedKey = ref([])

function initHandle() {
  nextTick(() => {
    const selectedValue = valueId.value
    if (selectedValue !== null && typeof selectedValue !== 'undefined') {
      const node = selectTreeRef.value?.getNode(selectedValue)
      if (node) {
        valueTitle.value = node.data[props.objMap.label]
        selectTreeRef.value?.setCurrentKey(selectedValue) // 设置默认选中
        defaultExpandedKey.value = [selectedValue] // 设置默认展开
      }
    } else {
      clearHandle()
    }
  })
}
function handleNodeClick(node) {
  valueTitle.value = node[props.objMap.label]
  valueId.value = node[props.objMap.value]
  defaultExpandedKey.value = []
  treeSelectRef.value.blur()
  selectFilterData('')
}
function selectFilterData(val) {
  selectTreeRef.value?.filter(val)
}
function filterNode(value, data) {
  if (!value) return true
  return data[props.objMap['label']].indexOf(value) !== -1
}
function clearHandle() {
  valueTitle.value = ''
  valueId.value = ''
  defaultExpandedKey.value = []
  clearSelected()
}
function clearSelected() {
  const allNode = document.querySelectorAll('#tree-option .el-tree-node')
  allNode.forEach((element) => element.classList.remove('is-current'))
}

onMounted(() => {
  initHandle()
})

watch(valueId, () => {
  initHandle()
})
</script>

<style lang="scss" scoped>
.el-scrollbar .el-scrollbar__view .el-select-dropdown__item {
  padding: 0;
  height: auto;
}

.el-select-dropdown__item.selected {
  font-weight: normal;
}

ul li .el-tree .el-tree-node__content {
  height: auto;
  padding: 0 20px;
  box-sizing: border-box;
}
</style>
