<script setup>
import { computed, useTemplateRef } from 'vue'
import TableColumn from './TableColumn.vue'
const props = defineProps({
  border: {
    type: Boolean,
    default: true,
  },
  dataList: {
    // 数据
    type: Array,
    default: () => [],
  },
  tableItem: {
    // 每列的名字
    type: Array,
    default: () => [],
  },
  tableListener: {
    type: Object,
    default: () => {
      return {}
    },
  },
  showChoose: {
    // 是否展示复选框
    type: Boolean,
    default: false,
  },
  showIndex: {
    // 是否展示序号
    type: Boolean,
    default: false,
  },
  pagination: {
    // 是否显示分页
    type: Boolean,
    default: true,
  },
  listCount: {
    //总条数
    type: Number,
    default: 0,
  },
  paginationInfo: {
    // 分页的页码和偏移
    type: Object,
    default: () => ({ pageNum: 1, pageSize: 50 }),
  },
  elTableConfig: {
    type: Object,
    default: () => {
      return {}
    },
  },
  showExpand: {
    type: Boolean,
    default: false,
  },
  align: {
    type: String,
    default: 'center',
  },
  paginationLayout: {
    type: String,
    default: 'total, sizes, prev, pager, next, jumper',
  },
  hideItems: {
    type: [Array, Object],
    default: () => ({}),
  },
  maxHeight: {
    type: [Number, String],
  },
  selectionConfig: {
    type: Object,
    default: () => ({}),
  },
})
const emit = defineEmits(['update:paginationInfo', 'sortChange'])
const elTableRef = useTemplateRef('elTableRef')
const headerRef = useTemplateRef('headerRef')
const footerRef = useTemplateRef('footerRef')
const slots = useSlots()
const handleCurrentChange = (pageNum) => {
  elTableRef.value.setScrollTop(0)
  emit('update:paginationInfo', { ...props.paginationInfo, pageNum })
}
const sortChange = (order) => {
  elTableRef.value.setScrollTop(0)
  emit('sortChange', order)
}

const handleSizeChange = (pageSize) => {
  elTableRef.value.setScrollTop(0)
  emit('update:paginationInfo', {
    ...props.paginationInfo,
    pageSize,
  })
}
const capitalizeFirstLetter = (str) => {
  return str.charAt(0).toUpperCase() + str.slice(1)
}
const hasSlot = (slots, arr) => {
  return arr.some((key) => slots.hasOwnProperty(key))
}

const maxHeight = computed(() => {
  let headerHeight = 0
  const footerHeight = footerRef.value?.clientHeight ?? 0
  if (headerRef.value) {
    headerHeight = headerRef.value.clientHeight
  }
  if (props.maxHeight) {
    return props.maxHeight - headerHeight - footerHeight
  } else {
    const viewportHeight =
      window.innerHeight - 260 - headerHeight - footerHeight
    return viewportHeight
  }
})
let expandAll = false
// 使用递归写法比设置default-expand-all来控制折叠和展开性能高很多
const setUnFoldAll = (children, unfold) => {
  for (const key in children) {
    elTableRef.value.toggleRowExpansion(children[key], unfold)
    if (children[key].children) {
      setUnFoldAll(children[key].children, unfold)
    }
  }
}

const unFoldAll = (...arg) => {
  if (arg) {
    expandAll = arg[0]
    setUnFoldAll(props.dataList, expandAll)
  } else {
    expandAll = !expandAll
    setUnFoldAll(props.dataList, expandAll)
  }
}
const isSmall = window.isSmallScreen
const paginationLayoutComputed = computed(() => {
  if (!isSmall) {
    return props.paginationLayout
  } else {
    return 'total, prev, pager, next'
  }
})
const otherSlots = ref([])
const filterSlot = () => {
  const filter = ['handleLeft', 'handleRight']
  const slotNames = Object.keys(slots)
  otherSlots.value = slotNames.filter((name) => !filter.includes(name))
}
watch(
  () => props.tableItem,
  () => {
    filterSlot()
  },
  {
    immediate: true,
  }
)

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
      v-if="hasSlot($slots, ['handleLeft', 'handleRight'])"
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
      :maxHeight="maxHeight > 300 ? maxHeight : 300"
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
            v-for="slotName in otherSlots"
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
        >
        </el-pagination>
      </slot>
    </div>
  </div>
</template>

<style scoped lang="scss">
.header {
  position: relative;
  overflow-x: auto;
  box-sizing: border-box;
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  max-width: 100%;
  background-color: var(--ba-bg-color-overlay);
  border: 1px solid var(--ba-border-color);
  border-bottom: none;
  padding: 13px 15px;
  font-size: 14px;
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
.baseTable {
  :deep(
    .el-table__body-wrapper .el-table-column--selection > .cell,
    .el-table__header-wrapper .el-table-column--selection > .cell
  ) {
    display: block;
  }
}
</style>
