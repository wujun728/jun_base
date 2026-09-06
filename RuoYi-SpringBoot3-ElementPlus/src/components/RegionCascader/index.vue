<template>
  <el-cascader
    v-model="selectedValue"
    :options="regionOptions"
    :props="regionProps"
    clearable
    :placeholder="placeholder"
    :lazy="true"
    :lazy-load="loadRegionOptions"
    :disabled="disabled"
    :size="size"
    :style="{ width: width }"
    @change="handleChange"
  />
</template>

<script setup name="RegionCascader">
import { ref, watch, onMounted } from 'vue'
import { listRegion,getRegion } from '@/api/biz/Region'

// 定义组件属性
const props = defineProps({
  // 选中的值
  modelValue: {
    type: Array,
    default: () => []
  },
  // 占位符文本
  placeholder: {
    type: String,
    default: '请选择省市区'
  },
  // 是否禁用
  disabled: {
    type: Boolean,
    default: false
  },
  // 尺寸
  size: {
    type: String,
    default: 'default',
    validator: (value) => ['large', 'default', 'small'].includes(value)
  },
  // 宽度
  width: {
    type: String,
    default: '100%'
  },
  // 是否显示到区县级别（默认显示到区县）
  showDistrict: {
    type: Boolean,
    default: true
  },
  // 控制显示层级（1-3，1=省份，2=城市，3=区县）
  level: {
    type: Number,
    default: 3,
    validator: (value) => [1, 2, 3].includes(value)
  }
})

// 定义事件
const emit = defineEmits(['update:modelValue', 'change', 'change-with-labels'])

// 响应式数据
const selectedValue = ref([])
const regionOptions = ref([])
const selectedLabels = ref([]) // 存储选中的文本标签

// 级联选择器配置
const regionProps = {
  value: 'id',
  label: 'name',
  children: 'children',
  lazy: true,
  lazyLoad: loadRegionOptions
}

// 监听外部值变化
watch(() => props.modelValue, (newValue) => {
  selectedValue.value = newValue || []
  handleChange(selectedValue.value);
}, { immediate: true })

// 监听内部值变化
watch(selectedValue, (newValue) => {
  emit('update:modelValue', newValue)
  emit('change', newValue)
})

// 组件挂载时加载省份数据
onMounted(() => {
  loadProvinceData()
})

/**
 * 加载省份数据
 */
function loadProvinceData() {
  listRegion({ level: 1 }).then(response => {
    regionOptions.value = response.data.map(item => ({
      id: item.id,
      name: item.name,
      level: item.level,
      leaf: false // 省份不是叶子节点
    }))
  }).catch(error => {
    console.error('加载省份数据失败:', error)
  })
}

/**
 * 动态加载省市区数据
 * @param {Object} node - 当前节点
 * @param {Function} resolve - 回调函数
 */
function loadRegionOptions(node, resolve) {
  const { level, data } = node
  
  // 根据当前级别确定查询参数
  let queryParams = {}
  
  if (level === 0) {
    // 加载省份数据
    queryParams = { level: 1 }
  } else if (level === 1 && props.level >= 2) {
    // 加载城市数据
    queryParams = { level: 2, parentId: data.id }
  } else if (level === 2 && props.level >= 3) {
    // 加载区县数据
    queryParams = { level: 3, parentId: data.id }
  }
  
  listRegion(queryParams).then(response => {
    const options = response.data.map(item => ({
      id: item.id,
      name: item.name,
      level: item.level,
      leaf: item.level >= props.level // 根据 level 属性决定叶子节点
    }))
    resolve(options)
  }).catch(error => {
    console.error('加载区域数据失败:', error)
    resolve([])
  })
}

/**
 * 处理选择变化
 * @param {Array} value - 选中的值
 */
function handleChange(value) {
  // 获取选中的文本标签
  getSelectedLabels(value)
  emit('change', value)
  emit('change-with-labels', {
    values: value,
    labels: selectedLabels.value
  })
}

/**
 * 根据选中的值获取对应的文本标签
 * @param {Array} values - 选中的值数组
 */
async function getSelectedLabels(values) {
  if (!values || values.length === 0) {
    selectedLabels.value = []
    return
  }

  const labels = []
  
  try {
    // 获取省份名称
    if (values[0] && props.level >= 1) {
      const provinceResponse = await getRegion( values[0] )
      if (provinceResponse.data) {
        labels.push(provinceResponse.data.name)
      }
    }

    // 获取城市名称
    if (values[1] && props.level >= 2) {
      const cityResponse = await getRegion( values[1] )
      if (cityResponse.data) {
        labels.push(cityResponse.data.name)
      }
    }

    // 获取区县名称
    if (values[2] && props.level >= 3) {
      const districtResponse = await getRegion( values[2] )
      if (districtResponse.data ) {
        labels.push(districtResponse.data.name)
      }
    }

    selectedLabels.value = labels
  } catch (error) {
    console.error('获取选中标签失败:', error)
    selectedLabels.value = []
  }
}

/**
 * 重置选择器
 */
function reset() {
  selectedValue.value = []
  selectedLabels.value = []
}

/**
 * 设置值
 * @param {Array} value - 要设置的值
 */
function setValue(value) {
  selectedValue.value = value || []
  if (value && value.length > 0) {
    getSelectedLabels(value)
  }
}

/**
 * 获取选中的文本标签
 * @returns {Array} 选中的文本标签数组
 */
function getLabels() {
  return selectedLabels.value
}

/**
 * 获取选中的完整信息
 * @returns {Object} 包含值和标签的对象
 */
function getSelectedInfo() {
  return {
    values: selectedValue.value,
    labels: selectedLabels.value,
    text: selectedLabels.value.join('/')
  }
}

// 暴露方法给父组件
defineExpose({
  reset,
  setValue,
  getLabels,
  getSelectedInfo
})
</script>

<style lang="scss" scoped>
.el-cascader {
  width: 100%;
}
</style>
