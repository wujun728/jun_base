<script setup>
const props = defineProps({
  item: { type: Object, required: true },
  allDisabled: { type: Boolean },
})
const emits = defineEmits(['keyUpEnter'])
const value = defineModel('value')
const inputRef = useTemplateRef('inputRef')

const isPassword = computed(() => props.item.config?.type === 'password')

const inputConfig = computed(() => {
  const { type, ...rest } = props.item.config || {}
  return {
    allowClear: true,
    placeholder: '请输入' + props.item.label,
    ...rest,
  }
})

const keyUpEnter = ($event, item) => emits('keyUpEnter', $event, item)
const getRef = () => inputRef.value

defineExpose({ getRef })
</script>

<template>
  <a-input-password
    v-if="isPassword"
    ref="inputRef"
    v-model:value="value"
    :disabled="allDisabled"
    v-bind="inputConfig"
    :id="item.field"
    v-on="item.eventFunction || {}"
    @press-enter="keyUpEnter($event, item)"
  />
  <a-input
    v-else
    ref="inputRef"
    v-model:value="value"
    :disabled="allDisabled"
    v-bind="inputConfig"
    :id="item.field"
    v-on="item.eventFunction || {}"
    @press-enter="keyUpEnter($event, item)"
  >
    <template v-for="slotName in item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData" />
    </template>
  </a-input>
</template>
