<script setup lang="ts">
import TableColumn from './TableColumn.vue'
import { BaseTableProps } from './types/index.ts'
const {
  border = true,
  dataList = [],
  tableItem = [],
  tableListener = {},
  showChoose = false,
  showIndex = false,
  pagination = true,
  paginationInfo = { pageNum: 1, pageSize: 50 },
  elTableConfig = {},
  showExpand = false,
  align = 'center',
  paginationLayout = 'total, sizes, prev, pager, next, jumper',
  hideItems,
  maxHeight,
  selectionConfig,
} = defineProps<BaseTableProps>()

const emit = defineEmits(['update:paginationInfo', 'sortChange'])
const headerRef = useTemplateRef('headerRef')
const elTableRef = useTemplateRef('elTableRef')
const footerRef = useTemplateRef('footerRef')
const slots = defineSlots()
const handleCurrentChange = (pageNum: number) => {
  elTableRef.value?.setScrollTop(0)
  emit('update:paginationInfo', { ...paginationInfo, pageNum })
}
const sortChange = (order: any[]) => {
  elTableRef.value?.setScrollTop(0)
  emit('sortChange', order)
}

const handleSizeChange = (pageSize: number) => {
  elTableRef.value?.setScrollTop(0)
  emit('update:paginationInfo', {
    ...paginationInfo,
    pageSize,
  })
}
const capitalizeFirstLetter = (str: string) => {
  return str.charAt(0).toUpperCase() + str.slice(1)
}
const hasSlot = (slots: any, arr: string[]) => {
  return arr.some((key) => slots.hasOwnProperty(key))
}

const maxHeightComputed = computed(() => {
  // console.log(footerRef.value?.clientWidth)
  let headerHeight = 0
  let footerHeight = 0
  if (footerRef.value?.clientHeight) {
    footerHeight = footerRef.value.clientHeight
  }
  if (headerRef.value) {
    headerHeight = headerRef.value.clientHeight
  }
  if (maxHeight) {
    return maxHeight - headerHeight - footerHeight
  } else {
    const viewportHeight =
      window.innerHeight - 260 - headerHeight - footerHeight
    return viewportHeight
  }
})
let expandAll = false
// 使用递归写法比设置default-expand-all来控制折叠和展开性能高很多
const setUnFoldAll = (children: any, unfold: boolean) => {
  for (const key in children) {
    elTableRef.value?.toggleRowExpansion(children[key], unfold)
    if (children[key].children) {
      setUnFoldAll(children[key].children, unfold)
    }
  }
}

const unFoldAll = (...arg: any) => {
  if (arg) {
    expandAll = arg[0]
    setUnFoldAll(dataList, expandAll)
  } else {
    expandAll = !expandAll
    setUnFoldAll(dataList, expandAll)
  }
}
const isSmall = window.isSmallScreen
const paginationLayoutComputed = computed(() => {
  if (!isSmall) {
    return paginationLayout
  } else {
    return 'total, prev, pager, next'
  }
})
defineExpose({
  elTableRef,
  unFoldAll,
})
</script>

<template>
  <div class="baseTable">
    <div
      class="header"
      ref="headerRef"
      v-if="hasSlot($slots, ['handleLeft', 'handleRight', 'header'])"
    >
      <slot name="header">
        <div class="handle">
          <slot name="handleLeft"></slot>
        </div>
        <div class="handleRight">
          <slot name="handleRight"></slot>
        </div>
      </slot>
    </div>
    <el-table
      class="elTable"
      ref="elTableRef"
      :data="dataList"
      :border="border"
      :maxHeight="maxHeightComputed > 300 ? maxHeightComputed : 300"
      :show-overflow-tooltip="true"
      style="width: 100%"
      stripe
      @sort-change="sortChange"
      v-on="tableListener"
      v-bind="elTableConfig"
    >
      <el-table-column type="expand" v-if="showExpand">
        <template #default="{ row }">
          <slot name="expand" :backData="row"></slot>
        </template>
      </el-table-column>
      <el-table-column
        type="selection"
        width="55"
        :align="align"
        v-bind="selectionConfig"
        v-if="showChoose"
      ></el-table-column>
      <el-table-column
        width="55"
        :align="align"
        label="序号"
        type="index"
        v-if="showIndex"
      ></el-table-column>

      <template v-for="item in tableItem" :key="item.prop">
        <TableColumn :item="item" :align="align" :hideItems="hideItems">
          <template
            v-for="(_value, slotName) in slots"
            #[slotName]="{ backData, currentItem }"
          >
            <slot
              :name="slotName"
              :backData="backData"
              :currentItem="currentItem"
            >
            </slot>
          </template>
          <template v-for="slotName in item.slotNames" #[slotName]="slotData">
            <slot
              :name="`${item.prop}` + capitalizeFirstLetter(slotName)"
              :backData="slotData"
            ></slot>
          </template>
        </TableColumn>
      </template>
    </el-table>
    <div
      class="footer lmw-pagination-footer"
      :class="{
        isSmall: isSmall,
      }"
      v-if="pagination"
      ref="footerRef"
    >
      <slot name="footer">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="paginationInfo.pageNum"
          :page-size="paginationInfo.pageSize"
          :page-sizes="[20, 50, 100, 200, 300]"
          :layout="paginationLayoutComputed"
          :total="listCount"
          :pager-count="isSmall ? 5 : 7"
          background
          hide-on-single-page
        >
        </el-pagination>
      </slot>
    </div>
  </div>
</template>

<style scoped lang="scss">
.header {
  position: relative;
  box-sizing: border-box;
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  max-width: 100%;
  background-color: var(--ba-bg-color-overlay);
  border: 1px solid var(--ba-border-color);
  border-bottom: none;
  font-size: 14px;
  padding: 0 12px;
  .table-header-operate-text {
    margin-left: 6px;
  }
}
.footer {
  background-color: var(--ba-bg-color-overlay);
  :deep(.el-pagination) {
    display: flex;
    flex-wrap: wrap;
    flex-direction: row;
    justify-content: flex-end;
    box-sizing: border-box;
    padding: 13px 15px;
  }
}
.isSmall {
  :deep(.btn-prev) {
    margin: 0 2px;
  }
  :deep(.btn-next) {
    margin: 0 0 0 2px;
  }
  :deep(.el-pager li) {
    margin: 0 1px;
  }
  :deep(.el-pagination) {
    padding: 10px;
  }
}
.btns {
  display: flex;
}
.baseTable {
  :deep(
    .el-table__body-wrapper .el-table-column--selection > .cell,
    .el-table__header-wrapper .el-table-column--selection > .cell
  ) {
    display: block;
  }
}
</style>
