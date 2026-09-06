<script setup lang="ts">
import { ref } from 'vue'
import { getOptions } from '../../utils/index.ts'
import type { FormItemTool } from '../../types/index.ts'

type Props = {
  item: FormItemTool<'tree'>
  allDisabled: boolean
}
const props = defineProps<Props>()
const treeRef = ref()
const getRef = () => treeRef.value
defineExpose({
  getRef,
})
</script>
<template>
  <el-tree
    ref="treeRef"
    :data="getOptions(props.item)"
    :style="{
      width: '100%',
    }"
    v-bind="props.item.config"
    v-on="props.item.eventFunction ?? {}"
  >
    <template v-for="slotName in props.item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData"> </slot>
    </template>
  </el-tree>
</template>

<style scoped lang="scss"></style>
