<script setup>
import { getOptions } from '../../../utils/index.js'

const props = defineProps({
  item: { type: Object, required: true },
  allDisabled: { type: Boolean },
})
const value = defineModel('value')
const checkboxRef = useTemplateRef('checkboxRef')

const groupConfig = computed(() => props.item.config ?? {})
const getRef = () => checkboxRef.value
defineExpose({ getRef })
</script>

<template>
  <div :id="item.field" :class="item.field + 'InnerClass'" class="innerClass">
    <a-checkbox-group
      v-if="item.isGroup"
      ref="checkboxRef"
      v-model:value="value"
      :disabled="allDisabled"
      v-bind="groupConfig"
      v-on="item.eventFunction || {}"
    >
      <a-checkbox
        v-for="option in getOptions(item)"
        :key="option.key ?? option.value"
        :value="option.value"
        v-bind="item.optionConfig ?? {}"
      >
        {{ option.label }}
      </a-checkbox>
      <template v-for="slotName in item.slotNames" #[slotName]="slotData">
        <slot :name="slotName" :slotData="slotData" />
      </template>
    </a-checkbox-group>
    <template v-else>
      <a-checkbox
        v-for="option in getOptions(item)"
        :key="option.key ?? option.value"
        v-model:checked="value"
        :disabled="allDisabled"
        :id="item.field"
        v-bind="groupConfig"
        v-on="item.eventFunction || {}"
      >
        {{ option.label }}
      </a-checkbox>
    </template>
  </div>
</template>

<style scoped lang="scss">
.innerClass {
  width: 100%;
}
</style>
