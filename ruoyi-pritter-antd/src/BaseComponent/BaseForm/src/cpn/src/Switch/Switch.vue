<script setup>
const props = defineProps({
  item: { type: Object, required: true },
  allDisabled: { type: Boolean },
})
const value = defineModel('value')
const switchRef = useTemplateRef('switchRef')

const switchConfig = computed(() => props.item.config ?? {})
const getRef = () => switchRef.value
defineExpose({ getRef })
</script>

<template>
  <a-switch
    ref="switchRef"
    v-model:checked="value"
    :disabled="allDisabled"
    v-bind="switchConfig"
    :id="item.field"
    v-on="item.eventFunction || {}"
  >
    <template v-for="slotName in item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData" />
    </template>
  </a-switch>
</template>
