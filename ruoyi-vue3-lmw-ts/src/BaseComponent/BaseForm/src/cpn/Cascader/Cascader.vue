<script setup lang="ts">
import { computed, ref } from 'vue'
import type {
  CascaderNodeValue,
  CascaderOption,
  CascaderValue,
} from 'element-plus/es/components/cascader-panel/src/types'
import type { FormItemTool, OptionItem } from '../../types/index.ts'
import { getOptions } from '../../utils/index.ts'
import { buildPlaceholder } from '../shared/form.ts'

/** OptionItem.label 可为 number；el-cascader 的 CascaderOption 要求 label 为 string */
function toCascaderOptions(items: OptionItem[]): CascaderOption[] {
  return items.map(toCascaderOption)
}

function toCascaderOption(opt: OptionItem): CascaderOption {
  const { label, value, children, ...rest } = opt
  const node: CascaderOption = { ...rest }
  if (label !== undefined && label !== null) node.label = String(label)
  if (value !== undefined) node.value = value as CascaderNodeValue
  if (children?.length) node.children = toCascaderOptions(children)
  return node
}

type Props = {
  item: FormItemTool<'cascader'>
  allDisabled: boolean
  inputId?: string
}
const props = defineProps<Props>()
const value = defineModel<CascaderValue | null | undefined>('value')
const cascaderRef = ref()

const cascaderOptions = computed(() =>
  toCascaderOptions(getOptions(props.item))
)

const placeholder = computed(() =>
  buildPlaceholder(
    props.allDisabled,
    '请选择',
    props.item.label,
    props.item.config?.placeholder as string | undefined
  )
)
const getRef = () => cascaderRef.value
defineExpose({
  getRef,
})
</script>
<template>
  <el-cascader
    clearable
    ref="cascaderRef"
    v-model="value"
    :id="props.inputId"
    :disabled="props.allDisabled"
    :placeholder="placeholder"
    :options="cascaderOptions"
    v-bind="props.item.config"
    v-on="props.item.eventFunction ?? {}"
  >
    <template v-for="slotName in props.item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData"> </slot>
    </template>
  </el-cascader>
</template>

<style scoped lang="scss"></style>
