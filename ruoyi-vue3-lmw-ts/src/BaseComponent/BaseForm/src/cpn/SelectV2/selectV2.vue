<script setup lang="ts">
import { computed, ref } from 'vue'
import { getOptions } from '../../utils/index.ts'
import type { FormItemTool } from '../../types/index.ts'
import { buildPlaceholder, type SelectValue } from '../shared/form.ts'

interface Props {
  item: FormItemTool<'selectV2'>
  allDisabled: boolean
  inputId?: string
}

const props = defineProps<Props>()
const value = defineModel<SelectValue>('value')
const selectV2Ref = ref()

const placeholder = computed(() =>
  buildPlaceholder(
    props.allDisabled,
    '请选择',
    props.item.label,
    props.item.config?.placeholder as string | undefined
  )
)
const getRef = () => selectV2Ref.value
defineExpose({
  getRef,
})
</script>
<template>
  <el-select-v2
    clearable
    ref="selectV2Ref"
    :disabled="props.allDisabled"
    :id="props.inputId"
    v-model="value"
    :placeholder="placeholder"
    :options="getOptions(props.item)"
    v-bind="props.item.config"
    v-on="props.item.eventFunction ?? {}"
  >
    <template v-for="slotName in props.item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData"> </slot>
    </template>
  </el-select-v2>
</template>

<style scoped lang="scss"></style>
