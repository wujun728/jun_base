<template>
  <div>
    <template v-for="(item, index) in options">
      <template v-if="values.includes(item.value)">
        <span
          v-if="item.elTagType == 'default' || item.elTagType == ''"
          :key="item.value"
          :index="index"
          :class="item.elTagClass"
          >{{ item.label + ' ' }}</span
        >
        <el-tag
          v-else
          :disable-transitions="true"
          :key="item.value + ''"
          :index="index"
          :type="item.elTagType"
          :class="item.elTagClass"
          >{{ item.label + ' ' }}</el-tag
        >
      </template>
    </template>
    <template v-if="unmatch && showValue">
      {{ handleArray(unmatchArray) }}
    </template>
  </div>
</template>

<script setup lang="ts">
type Option = {
  elTagType?:
    | 'success'
    | 'warning'
    | 'info'
    | 'primary'
    | 'danger'
    | 'default'
    | ''
  elTagClass?: string
  value: string
  label: string
}
interface PropsType {
  options: Option[]
  value?: string | number
  showValue?: boolean
}
// // 记录未匹配的项
const unmatchArray = ref<StrNum[]>([])
const { options, value, showValue = false } = defineProps<PropsType>()
const values = computed(() => {
  if (value !== null && typeof value !== 'undefined') {
    return Array.isArray(value) ? value : [String(value)]
  } else {
    return []
  }
})

const unmatch = computed(() => {
  unmatchArray.value = []
  if (value !== null && typeof value !== 'undefined') {
    // 传入值为非数组
    if (!Array.isArray(value)) {
      if (options.some((v) => v.value == value)) return false
      unmatchArray.value.push(value)
      return true
    }
    // 传入值为Array
    value.forEach((item) => {
      if (!options.some((v) => v.value == item)) unmatchArray.value.push(item)
    })
    return true
  }
  // 没有value不显示
  return false
})

function handleArray(array: StrNum[]) {
  if (array.length === 0) return ''
  return array.reduce((pre, cur) => {
    return pre + ' ' + cur
  })
}
</script>

<style scoped>
.el-tag + .el-tag {
  margin-left: 10px;
}
</style>
