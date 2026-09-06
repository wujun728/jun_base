<script setup>
import BaseTable from '@/BaseComponent/BaseTable/index'
import DictCpn from './dictCpn.vue'
import TableHeaderToolbar from './components/TableHeaderToolbar.vue'
import hasPermi from '@/utils/hasPermi'
import { collectSlotItems } from './utils/collectSlotItems'
import { usePageData } from './composables/usePageData'
import { useTableActions } from './composables/useTableActions'
import { useColumnDisplay } from './composables/useColumnDisplay'
import { usePageListeners } from './composables/usePageListeners'

const props = defineProps({
  contentConfig: {
    type: Object,
    required: true,
  },
  pageName: {
    type: String,
    required: true,
  },
  tableListener: {
    type: Object,
    default: () => ({}),
  },
  firstSendOption: {
    type: Object,
    default: () => ({}),
  },
  autoSend: {
    type: Boolean,
    default: true,
  },
  autoDesc: {
    type: Boolean,
    default: true,
  },
  descConfig: {
    type: Object,
    default: () => ({
      orderByColumn: 'createTime',
      isAsc: 'desc',
    }),
  },
  otherRequestOption: {
    type: Object,
    default: () => ({}),
  },
  piniaConfig: {
    type: Object,
    default: () => ({
      listConfig: {
        listKey: 'rows',
        countKey: 'total',
      },
      handleList: (list) => list,
    }),
  },
  idKey: {
    type: String,
    default: '',
  },
  requestBaseUrl: {
    type: String,
    default: '/',
  },
  requestUrl: {
    type: String,
    default: 'list',
  },
  showEdit: {
    type: Boolean,
    default: true,
  },
  showDelete: {
    type: Boolean,
    default: true,
  },
  dictMap: {
    type: Object,
    default: () => ({}),
  },
  headerButtons: {
    type: Array,
    default: () => ['refresh', 'add', 'delete', 'columnDisplay', 'comSearch'],
  },
  tableSelected: {
    type: Array,
    default: () => [],
  },
  permission: {
    type: Object,
    default: () => ({}),
  },
  handleEditShow: {
    type: Function,
    default: () => true,
  },
  handleDeleteShow: {
    type: Function,
    default: () => true,
  },
  delUrl: {
    type: String,
    default: '',
  },
  maxHeightDecrement: {
    type: Number,
    default: 0,
  },
  tableHideItems: {
    type: Array,
    default: () => [],
  },
  cacheKey: {
    type: String,
    default: '',
  },
})

const emit = defineEmits([
  'addClick',
  'editBtnClick',
  'beforeSend',
  'afterSend',
  'onChangeShowColumn',
])

const slots = useSlots()
const baseTableRef = useTemplateRef('baseTableRef')

const {
  store,
  isLoading,
  searchDatas,
  paginationInfo,
  finalSearchData,
  tableBindProps,
  dataList,
  listCount,
  showPageSearch,
  send,
  refresh,
  sortChange,
  applyDefaultSort,
  triggerShowSearch,
} = usePageData(props, emit)

const { deleteRow, editClick, editSelected, addClick } = useTableActions(
  props,
  emit,
  { isLoading, send, finalSearchData }
)

const {
  columnDisplayOpen,
  columnDisplayList,
  columnDisplayKey,
  columnChecked,
  onChangeShowColumn,
  dragUpdate,
  handleDefaultSort,
  applyStoredColumnOrder,
} = useColumnDisplay(props, emit, store)

const { maxTableHeight, updateTableHeight, startListening, stopListening } =
  usePageListeners(props, {
    send,
    searchDatas,
    paginationInfo,
    finalSearchData,
  })

const otherSlot = collectSlotItems(props.contentConfig?.tableItem)

const hasSlot = (names) =>
  names.some((name) => Object.prototype.hasOwnProperty.call(slots, name))

applyStoredColumnOrder()

onMounted(() => {
  applyDefaultSort()

  if (props.autoSend) {
    send({ ...props.firstSendOption, ...finalSearchData.value })
  }

  updateTableHeight()
  startListening()
})

onBeforeUnmount(stopListening)
onActivated(startListening)
onDeactivated(stopListening)

defineExpose({
  finalSearchData,
  refresh,
  baseTableRef,
  deleteRow,
  editClick,
  dataList,
})
</script>

<template>
  <div class="page-content">
    <a-spin :spinning="isLoading">
      <BaseTable
        ref="baseTableRef"
        v-model:pagination-info="paginationInfo"
        :data-list="dataList"
        :list-count="listCount"
        :table-listener="tableListener"
        :maxTableHeight="maxTableHeight"
        v-bind="tableBindProps"
        @sort-change="sortChange"
      >
        <template #header>
          <TableHeaderToolbar
            :header-buttons="headerButtons"
            :permission="permission"
            :table-selected="tableSelected"
            :show-page-search="showPageSearch"
            :has-left-slot="hasSlot(['handleLeft'])"
            :has-right-slot="hasSlot(['handleRight'])"
            v-model:column-display-open="columnDisplayOpen"
            v-model:column-display-list="columnDisplayList"
            v-model:column-checked="columnChecked"
            :column-display-key="columnDisplayKey"
            @refresh="refresh"
            @add="addClick"
            @edit="editSelected"
            @delete="deleteRow"
            @toggle-search="triggerShowSearch"
            @change-column="onChangeShowColumn"
            @drag-update="dragUpdate"
            @reset-sort="handleDefaultSort"
          >
            <template #handleLeft>
              <slot name="handleLeft" />
            </template>
            <template #handleRight>
              <slot name="handleRight" />
            </template>
          </TableHeaderToolbar>
        </template>

        <template #expand="{ backData }">
          <slot name="expand" :back-data="backData" />
        </template>

        <template #todo="{ backData }">
          <div class="todo">
            <slot name="todoSlot" :back-data="backData" />

            <a-button
              v-if="
                showEdit &&
                handleEditShow(backData) &&
                hasPermi(permission.edit)
              "
              class="edit order5"
              type="primary"
              size="small"
              @click="editClick(backData)"
            >
              <template #icon>
                <SvgIcon :size="12" icon-class="pencil" />
              </template>
              <span class="ml6">编辑</span>
            </a-button>

            <a-popconfirm
              v-if="
                hasPermi(permission.del) &&
                showDelete &&
                handleDeleteShow(backData)
              "
              title="确定删除当前记录？"
              ok-text="确认"
              cancel-text="取消"
              @confirm="deleteRow(backData)"
            >
              <a-button class="del order10" type="primary" danger size="small">
                <template #icon>
                  <SvgIcon :size="12" icon-class="trash" />
                </template>
                <span class="ml6">删除</span>
              </a-button>
            </a-popconfirm>
          </div>
        </template>

        <template
          v-for="item in otherSlot"
          :key="item.prop"
          #[item.slotName]="{ backData, currentItem }"
        >
          <slot
            v-if="item.slotName"
            :name="item.slotName"
            :back-data="backData"
            :current-item="currentItem"
          />
          <DictCpn
            v-if="item.isDict"
            :value="backData[item.prop]"
            :options="dictMap[item.prop]"
          />
        </template>

        <template
          v-for="item in otherSlot"
          :key="`${item.prop}-header`"
          #[`${item.slotName}Header`]="{ backData }"
        >
          <slot :name="item.slotName + 'Header'" :back-data="backData" />
        </template>
      </BaseTable>
    </a-spin>
  </div>
</template>

<style scoped lang="scss">
.todo {
  flex-wrap: wrap;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  :deep(.ant-btn) {
    margin: 4px;
  }
}

.flex-center {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
