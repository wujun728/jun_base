<script setup>
const props = defineProps({
  item: {
    type: Object,
    default: () => {},
  },
  align: {
    type: String,
    default: 'center',
  },
  hideItems: {
    type: [Array, Object],
    default: () => ({}),
  },
})
const isHiddenItem = (item) => {
  let flag = false
  if (isRef(props.hideItems)) {
    if (props.hideItems.value.includes(item.prop)) {
      flag = true
    }
  } else if (Array.isArray(props.hideItems)) {
    if (props.hideItems.includes(item.prop)) {
      flag = true
    }
  }
  return flag
}

const capitalizeFirstLetter = (str) => {
  return str.charAt(0).toUpperCase() + str.slice(1)
}
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
          <template #[`${merge.slotName}Header`]="{ backData }">
            <slot :name="`${merge.slotName}Header`" :backData="backData">
              {{ merge.label }}
            </slot>
          </template>
          <template #[`${merge.slotName}`]="{ backData }">
            <slot
              :name="merge.slotName"
              :backData="backData"
              :currentItem="merge"
            >
              <template v-if="merge.prop">
                {{ backData[merge.prop] }}
              </template>
            </slot>
          </template>

          <template v-for="slotName in merge.slotNames" #[slotName]="slotData">
            <slot
              :name="`${merge.prop}` + capitalizeFirstLetter(slotName)"
              :backData="slotData"
            ></slot>
          </template>
        </TableColumn>
      </template>

      <slot
        :name="item.slotName"
        :backData="scope.row"
        :currentItem="item"
        v-else
      >
        <template v-if="item.prop">
          {{ scope.row[item.prop] }}
        </template>
      </slot>
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
