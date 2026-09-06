<script setup lang="ts">
import { TableColumnProps, TableItem } from './types/index'
const {
  item,
  align = 'center',
  hideItems = [],
} = defineProps<TableColumnProps>()
const isHiddenItem = (item: TableItem) => {
  let flag = false
  if (isRef(hideItems)) {
    if (Array.isArray(hideItems.value) && hideItems.value.includes(item.prop)) {
      flag = true
    }
  } else if (Array.isArray(hideItems)) {
    if (hideItems.includes(item.prop!)) {
      flag = true
    }
  }
  return flag
}

const capitalizeFirstLetter = (str: string) => {
  return str.charAt(0).toUpperCase() + str.slice(1)
}
defineSlots<any>()
</script>
<template>
  <el-table-column
    border
    :align="align"
    v-bind="item"
    v-if="!isHiddenItem(item) && !item.hide"
  >
    <template #header="{ column, index }">
      <slot :name="`${item.slotName}Header`" :backData="{ column, index }">
        {{ item.label }}
      </slot>
    </template>
    <template #default="scope">
      <template v-if="item.merges">
        <TableColumn
          v-for="merge in item.merges"
          :item="merge"
          :hideItems="hideItems"
          :align="align"
        >
          <template v-for="(_, slotName) in $slots" #[slotName]="slotData">
            <slot :name="slotName" v-bind="slotData" />
          </template>
        </TableColumn>
      </template>

      <template v-else>
        <slot
          v-if="item.slotName"
          :name="item.slotName"
          :backData="scope.row"
          :currentItem="item"
        >
          <template v-if="item.prop">
            {{ scope.row[item.prop] }}
          </template>
        </slot>
        <template v-else>
          {{ scope.row[item.prop] }}
        </template>
      </template>
    </template>
    <template v-for="slotName in item.slotNames" #[slotName]="slotData">
      <slot
        :name="`${item.prop}` + capitalizeFirstLetter(slotName)"
        :backData="slotData"
      ></slot>
    </template>
  </el-table-column>
</template>

<style scoped lang="scss"></style>
