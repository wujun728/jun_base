<script setup>
import { ref } from 'vue'
const props = defineProps({
  paginationInfo: {
    // 分页的页码和偏移
    type: Object,
    default: () => ({ pageNum: 1, pageSize: 5000 }),
  },
  elTableConfig: {
    type: Object,
    default: () => {
      return {}
    },
  },
  maxHeight: {
    type: [Number, String],
  },
  pagination: {
    type: Boolean,
    default: true,
  },
  tableListener: {
    type: Object,
    default: () => {
      return {}
    },
  },
  dataList: {
    // 数据
    type: Array,
    default: () => [],
  },
  tableItem: {
    type: Array,
    default: () => [],
  },
  pageSizes: {
    type: Array,
    default: () => [500, 1000, 2000, 5000, 10000],
  },
  listCount: {
    type: Number,
  },
  sortBy: {
    type: Object,
    default: () => {},
  },
  isLoading: {
    type: Boolean,
    default: false,
  },
})
const emit = defineEmits(['update:paginationInfo', 'sortChange'])
const elTableRef = useTemplateRef('elTableRef')
const headerRef = useTemplateRef('headerRef')
const footerRef = useTemplateRef('footerRef')

const handleCurrentChange = (pageNum) => {
  elTableRef.value.scrollTo({ scrollLeft: 0, scrollTop: 0 })
  emit('update:paginationInfo', { ...props.paginationInfo, pageNum })
}
const handleSizeChange = (pageSize) => {
  elTableRef.value.scrollTo({ scrollLeft: 0, scrollTop: 0 })
  emit('update:paginationInfo', {
    ...props.paginationInfo,
    pageSize,
  })
}
const columnSort = (order) => {
  elTableRef.value.scrollTo({ scrollLeft: 0, scrollTop: 0 })
  emit('sortChange', order)
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

const hasSlot = (slots, arr) => {
  return arr.some((key) => slots.hasOwnProperty(key))
}
const expandedRowKeys = ref([])
let expandAll = false
const setUnFoldAll = (children, unfold) => {
  for (const item of children) {
    const idKey = props.elTableConfig.rowKey ?? 'id'
    expandedRowKeys.value.push(item[idKey])
    if (item.children) {
      setUnFoldAll(item.children, unfold)
    }
  }
}
const unFoldAll = () => {
  expandAll = !expandAll
  if (expandAll) {
    setUnFoldAll(props.dataList)
  } else {
    expandedRowKeys.value = []
  }
}
defineExpose({
  unFoldAll,
})
</script>

<template>
  <div class="baseTableV2">
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
    <el-auto-resizer>
      <template #default="{ width }">
        <el-table-v2
          ref="elTableRef"
          v-model:expanded-row-keys="expandedRowKeys"
          :columns="tableItem"
          :data="dataList"
          :width="width"
          :height="NaN"
          :maxHeight="maxHeight"
          :sort-by="sortBy"
          fixed
          @columnSort="columnSort"
          v-on="tableListener"
          v-bind="elTableConfig"
        >
          <template #overlay v-if="isLoading">
            <div
              class="el-loading-mask"
              style="
                display: flex;
                align-items: center;
                justify-content: center;
              "
            >
              <SvgIcon
                class="is-loading"
                color="var(--el-color-primary)"
                :size="26"
                iconClass="el-icon-Loading"
              ></SvgIcon>
            </div>
          </template>
        </el-table-v2>
      </template>
    </el-auto-resizer>

    <div class="footer" v-if="pagination" ref="footerRef">
      <slot name="footer">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="paginationInfo.pageNum"
          :page-size="paginationInfo.pageSize"
          :page-sizes="[20, 50, 100, 200, 300]"
          :layout="paginationLayoutComputed"
          :total="listCount"
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
  :deep(.el-pagination) {
    display: flex;
    flex-wrap: wrap;
    flex-direction: row;
    justify-content: flex-end;
    box-sizing: border-box;
    width: 100%;
    max-width: 100%;
    background-color: var(--ba-bg-color-overlay);
    padding: 13px 15px;
  }
}
.btns {
  display: flex;
}
.baseTableV2 {
  :deep(.el-table-v2__main) {
    position: initial;
  }
  :deep(.el-table-v2__overlay) {
    width: 100%;
    height: 100%;
  }
}
</style>
