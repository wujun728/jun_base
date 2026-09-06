<template>
  <div>
    <template v-for="(item, index) in options">
      <template v-if="values.includes(item.value)">
        <!-- <span
          v-if="
            (item.elTagType == 'default' || item.elTagType == '') &&
            (item.elTagClass == '' || item.elTagClass == null)
          "
          :key="item.value"
          :index="index"
          :class="item.elTagClass"
          >{{ item.label + ' ' }}</span
        >
        <a-tag
          v-else
          :disable-transitions="true"
          :key="item.value + ''"
          :index="index"
          :color="getColor(item.elTagType)"
          :bordered="false"
          :class="item.elTagClass"
          >{{ item.label + ' ' }}
        </a-tag> -->

        <span
          class="dictTag"
          :key="item.value"
          :index="index"
          :class="item.elTagType"
        >
          <SvgIcon iconClass="circle" size="8"></SvgIcon>
          <span class="ml5">
            {{ item.label + ' ' }}
          </span>
        </span>
      </template>
    </template>
    <template v-if="unmatch && showValue">
      {{ handleArray(unmatchArray) }}
    </template>
  </div>
</template>

<script setup>
// // 记录未匹配的项
const unmatchArray = ref([])

const props = defineProps({
  // 数据
  options: {
    type: Array,
    default: null,
  },
  // 当前的值
  value: [Number, String, Array],
  // 当未找到匹配的数据时，显示value
  showValue: {
    type: Boolean,
    default: true,
  },
})

const values = computed(() => {
  if (props.value !== null && typeof props.value !== 'undefined') {
    return Array.isArray(props.value) ? props.value : [String(props.value)]
  } else {
    return []
  }
})

const unmatch = computed(() => {
  unmatchArray.value = []
  if (props.value !== null && typeof props.value !== 'undefined') {
    // 传入值为非数组
    if (!Array.isArray(props.value)) {
      if (props.options.some((v) => v.value == props.value)) return false
      unmatchArray.value.push(props.value)
      return true
    }
    // 传入值为Array
    props.value.forEach((item) => {
      if (!props.options.some((v) => v.value == item))
        unmatchArray.value.push(item)
    })
    return true
  }
  // 没有value不显示
  return false
})

function handleArray(array) {
  if (array.length === 0) return ''
  return array.reduce((pre, cur) => {
    return pre + ' ' + cur
  })
}

function getColor(type) {
  if (type === 'primary') return 'blue'
  if (type === 'success') return 'green'
  if (type === 'info') return 'gray'
  if (type === 'warning') return 'yellow'
  if (type === 'danger') return 'red'
  return type
}
</script>

<style scoped lang="scss">
.a-tag + .a-tag {
  margin-left: 10px;
}
.dictTag {
  display: flex;
  align-items: center;
}
.primary {
  color: #1677ff;
}
.success {
  color: #4aa785;
}
.info {
  color: #8a8cd9;
}
.warning {
  color: #ffc555;
}
.danger {
  color: #e94d41;
}
</style>
