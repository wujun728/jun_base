<script setup>
import { getOptions } from '../../../utils/index.js'

const props = defineProps({
  item: { type: Object, required: true },
  allDisabled: { type: Boolean },
})
const value = defineModel('value')
const radioRef = useTemplateRef('radioRef')

const groupConfig = computed(() => props.item.config ?? {})
const getRef = () => radioRef.value
defineExpose({ getRef })
</script>

<template>
  <a-radio-group
    v-if="item.isGroup"
    ref="radioRef"
    v-model:value="value"
    :disabled="allDisabled"
    v-bind="groupConfig"
    :id="item.field"
    v-on="item.eventFunction || {}"
  >
    <a-radio
      v-for="option in getOptions(item)"
      :key="option.key ?? option.value"
      :value="option.value"
    >
      {{ option.label }}
    </a-radio>
    <template v-for="slotName in item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData" />
    </template>
  </a-radio-group>
  <a-radio-group
    v-else
    v-model:value="value"
    :disabled="allDisabled"
    v-bind="groupConfig"
    :id="item.field"
    v-on="item.eventFunction || {}"
  >
    <a-radio
      v-for="option in getOptions(item)"
      :key="option.key ?? option.value"
      :value="option.value"
    >
      {{ option.label }}
    </a-radio>
  </a-radio-group>
</template>
