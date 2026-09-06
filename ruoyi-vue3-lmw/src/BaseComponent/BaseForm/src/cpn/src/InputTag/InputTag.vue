<script setup>
import { nextTick, ref } from 'vue'
const props = defineProps({
  item: {
    type: Object,
    required: true,
  },
  allDisabled: {
    type: Boolean,
  },
})
const emits = defineEmits()
const value = defineModel('value')
const elRef = ref(null)
const getRef = () => {
  return elRef.value
}
const handleTagClick = (value, index) => {
  editIndex.value = index
  editValue.value = value
  nextTick(() => {
    allInputRefs.value[value].focus()
  })
}
const allInputRefs = ref({})
const setItemRef = (el, type) => {
  if (el) {
    allInputRefs.value[type] = el
  }
}
const editIndex = ref(-1)
const editValue = ref('')
const keyEnter = (value) => {
  allInputRefs.value[value].blur()
}
const handleBlur = (index) => {
  const values = [...value.value]
  values[index] = editValue.value
  value.value = values
  editValue.value = ''
  editIndex.value = -1
  nextTick(() => {
    elRef.value.focus()
  })
}
defineExpose({
  getRef,
})
</script>
<template>
  <el-input-tag
    ref="elRef"
    :disabled="allDisabled"
    :placeholder="'请输入' + item.label"
    v-model="value"
    v-bind="item.config"
    v-on="item.eventFunction || {}"
  >
    <template #tag="{ value, index }" v-if="!item.slotNames?.includes('tag')">
      <div @click="handleTagClick(value, index)" v-show="index !== editIndex">
        <span>{{ value }}</span>
      </div>
      <el-input
        v-show="index === editIndex"
        :ref="(el) => setItemRef(el, value)"
        size="small"
        v-model="editValue"
        @keyup.enter="keyEnter(value)"
        @blur="handleBlur(index)"
      >
      </el-input>
    </template>

    <template v-for="slotName in item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData"> </slot>
    </template>
  </el-input-tag>
</template>

<style scoped lang="scss"></style>
