<script setup>
import { computed, watch } from 'vue'

const props = defineProps({
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
    default: () => ({ pageNum: 1, pageSize: 10 }),
  },
  paginationLayout: {
    type: String,
    default: 'prev, pager, next',
  },
  hideItems: {
    type: [Array, Object],
    default: () => ({}),
  },
  showBacktop: {
    type: Boolean,
    default: true,
  },
  visibilityHeight: {
    type: Number,
    default: 100,
  },
  selectionConfig: {
    type: Object,
    default: () => ({}),
  },
  tableLayout: {
    type: Object,
    default: () => ({
      xl: 6,
      lg: 8,
      md: 12,
      sm: 12,
      xs: 24,
    }),
  },
})
const emits = defineEmits([
  'cardHeaderClick',
  'selectionChange',
  'update:paginationInfo',
])
let headerItem = []
let centerItem = []
let footerItem = []
const filterTableItems = () => {
  props.tableItem.forEach((item) => {
    if (item.mobileSlot === 'header') {
      headerItem.push({
        ...item,
        slotName: item.slotName ?? item.prop + 'Header',
      })
    }
    if (item.mobileSlot === 'footer' || item.prop === 'todo') {
      footerItem.push({
        ...item,
        slotName: item.slotName ?? item.prop + 'Slot',
      })
    }
    if (!item.mobileSlot) {
      centerItem.push({
        ...item,
        slotName: item.slotName ?? item.prop + 'Content',
      })
    }
  })
}
watch(
  () => props.tableItem,
  () => {
    filterTableItems()
  },
  {
    immediate: true,
  }
)
const handleToTop = () => {
  const main = document.querySelector('.el-main')
  main?.scrollTo(0, 0)
}

const setFooterShow = (el) => {
  const child = el.querySelectorAll('*')
  if (child.length <= 1) {
    el.setAttribute('hiden', '')
  } else {
    el.removeAttribute('hiden')
  }
}
const vShowFooter = {
  mounted: (el) => {
    setFooterShow(el)
  },
  updated: (el) => {
    setFooterShow(el)
  },
}
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
const cardHeaderClick = (row) => {
  emits('cardHeaderClick', row)
}
const selectionChange = () => {
  emits('selectionChange', checkList.value)
}
const hasSlot = (slots, arr) => {
  return arr.some((key) => slots.hasOwnProperty(key))
}
const checkList = ref([])
const isCheckAll = computed(() => {
  return checkList.value.length === props.dataList.length
})
const checkAll = () => {
  if (isCheckAll.value) {
    checkList.value = []
  } else {
    checkList.value = props.dataList
  }
}
defineExpose({
  checkAll,
})
</script>
<template>
  <div class="mobileContent">
    <div
      class="mb12 header"
      v-if="hasSlot($slots, ['handleLeft', 'handleRight'])"
    >
      <el-scrollbar>
        <div class="headerContent">
          <slot name="header">
            <div class="handleRight">
              <slot name="handleLeft"></slot>
            </div>
            <div class="handleRight">
              <slot name="handleRight"></slot>
            </div>
          </slot>
        </div>
      </el-scrollbar>
    </div>
    <div class="dataList" v-if="dataList.length !== 0">
      <el-checkbox-group v-model="checkList" @change="selectionChange">
        <el-row :gutter="12">
          <el-col v-for="row in dataList" v-bind="tableLayout" class="mb12">
            <div class="data-card overlayColor">
              <template v-for="field in headerItem">
                <div
                  class="card-header"
                  v-if="!isHiddenItem(field) && !field.hide"
                >
                  <div class="order-number">
                    <slot
                      :name="field.slotName + 'Header'"
                      :backData="Object.assign(row, { column: field })"
                    >
                      <div class="label">{{ field.label }}：</div>
                    </slot>
                    <slot
                      :name="field.slotName"
                      :backData="Object.assign(row, { column: field })"
                    >
                      <div class="value" @click="cardHeaderClick(row)">
                        {{ row[field.prop] }}
                      </div>
                    </slot>
                  </div>
                  <el-checkbox
                    class="checkSize"
                    v-if="showChoose"
                    :value="row"
                    :disabled="
                      selectionConfig.selectable &&
                      !selectionConfig.selectable(row)
                    "
                  />
                </div>
              </template>
              <div class="info-list">
                <div class="info-row">
                  <template v-for="field in centerItem" :key="field.prop">
                    <div
                      v-if="!isHiddenItem(field) && !field.hide"
                      class="info-item"
                      :class="{
                        'full-width':
                          field.width >= 160 || field.minWidth >= 160,
                      }"
                    >
                      <template v-if="field.prop !== 'todo'">
                        <slot
                          :name="field.slotName + 'Header'"
                          :backData="Object.assign(row, { column: field })"
                        >
                          <span class="label"> {{ field.label }}： </span>
                        </slot>
                        <slot
                          :name="field.slotName"
                          :backData="Object.assign(row, { column: field })"
                        >
                          <span class="value">{{ row[field.prop] }}</span>
                        </slot>
                      </template>
                    </div>
                  </template>
                </div>
              </div>
              <div class="card-footer mobileFooter" v-show-footer>
                <template v-for="field in footerItem">
                  <slot
                    :name="field.slotName"
                    :backData="Object.assign(row, { column: field })"
                    v-if="!isHiddenItem(field)"
                  ></slot>
                </template>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-checkbox-group>
    </div>
    <el-empty class="overlayColor" v-else description="暂无数据" />
    <div
      class="footer lmw-pagination-footer"
      v-if="pagination && listCount > paginationInfo.pageSize"
    >
      <span class="mr2"> {{ listCount }}条 </span>
      <el-pagination
        @size-change="handleToTop"
        @current-change="handleToTop"
        v-model:current-page="paginationInfo.pageNum"
        v-model:page-size="paginationInfo.pageSize"
        :layout="paginationLayout"
        :total="listCount"
        :pager-count="5"
        background
        hide-on-single-page
      >
      </el-pagination>
    </div>
    <el-backtop
      target=".el-main"
      :right="10"
      :bottom="100"
      :visibility-height="visibilityHeight"
    />
  </div>
</template>

<style scoped lang="scss">
.header {
  border: 1px solid var(--ba-border-color);
  background-color: var(--ba-bg-color-overlay);
  font-size: 14px;
  padding: 12px 12px 0 12px;
}
.headerContent {
  padding-bottom: 12px;
}
.data-card {
  border-radius: 12px;
  line-height: 32px;
  border: 1px solid var(--ba-border-color);
  overflow: hidden;
  height: 100%;
}
.checkSize {
  :deep(.el-checkbox__inner) {
    height: 18px;
    width: 18px;
    &:after {
      left: 5px;
      width: 5px;
      height: 10px;
    }
  }
}
// 头部样式
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  border-bottom: 1px solid var(--el-border-color);
  .order-number {
    display: flex;
    font-size: 15px;
    overflow: hidden;
    .label {
      max-width: 90px;
      color: var(--el-text-color-primary);
      font-weight: 500;
    }
    .value {
      color: var(--el-color-primary);
      font-weight: 500;
      flex: 1;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      max-width: 200px;
      cursor: pointer;
    }
  }
}
// 信息列表样式
.info-list {
  padding: 16px;
  .info-row {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
    gap: 12px 24px;
  }
  .info-item {
    display: flex;
    align-items: center;
    font-size: 14px;
    line-height: 1.5;
    white-space: nowrap;
    overflow: hidden;
    &.full-width {
      grid-column: 1 / -1;
    }
    .label {
      color: #666;
      margin-right: 4px;
    }
    .value {
      color: var(--el-text-color-primary);
      flex: 1;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }
  }
}
// 底部操作区
.card-footer {
  display: flex;
  justify-content: flex-end;
  flex-wrap: wrap;
  padding: 10px 12px;
  :deep(.el-button) {
    margin: 4px !important;
  }
}
// 响应式处理
@media screen and (max-width: 768px) {
  .info-list {
    padding: 12px;
    .info-row {
      grid-template-columns: repeat(2, 1fr);
      gap: 8px;
    }
  }
  .info-item {
    font-size: 13px;
  }
  .card-footer {
    padding: 8px 12px;
    flex-wrap: wrap;
  }
}
// 超窄屏幕处理
@media screen and (max-width: 375px) {
  .info-list .info-row {
    grid-template-columns: 1fr;
  }
}
.footer {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  flex-wrap: wrap;
  :deep(.el-pager) {
    li {
      margin: 0 1px;
    }
  }
  :deep(.btn-prev) {
    margin: 0 2px;
  }
  :deep(.btn-next) {
    margin: 0 0 0 2px;
  }
  :deep(.el-pagination) {
    padding: 0px;
    --el-pagination-button-height: 28px;
    --el-pagination-button-width: 28px;
  }
  background-color: var(--ba-bg-color-overlay);
  padding: 8px;
}
</style>
