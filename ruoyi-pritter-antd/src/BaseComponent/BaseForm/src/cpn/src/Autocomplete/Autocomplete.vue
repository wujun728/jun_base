<script setup>
const props = defineProps({
  item: { type: Object, required: true },
  allDisabled: { type: Boolean },
})
const emits = defineEmits(['keyUpEnter'])
const value = defineModel('value')
const autoRef = useTemplateRef('autoRef')

const autoConfig = computed(() => ({
  allowClear: true,
  placeholder: '请输入' + props.item.label,
  ...props.item.config,
}))

const keyUpEnter = ($event, item) => emits('keyUpEnter', $event, item)
const getRef = () => autoRef.value
defineExpose({ getRef })
</script>

<template>
  <a-auto-complete
    ref="autoRef"
    v-model:value="value"
    class="base-form-autocomplete"
    :disabled="allDisabled"
    v-bind="autoConfig"
    :id="item.field"
    v-on="item.eventFunction || {}"
    @press-enter="keyUpEnter($event, item)"
  >
    <template v-for="slotName in item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData" />
    </template>
  </a-auto-complete>
</template>

<style scoped lang="scss">
.base-form-autocomplete {
  width: 100%;
}
</style>
