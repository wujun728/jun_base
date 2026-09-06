<script setup lang="ts">
import { debounce } from 'lodash-es'
import BaseTable from '@/BaseComponent/BaseTable/index'
import emitter from '@/utils/hsj/bus'
import businessStore from '@/store/business/businessStore'
import { getInfo } from '@/api/business/main/index'
import to from '@/utils/to'
import DictCpn from './dictCpn.vue'
import { interceptor } from '@/store/business/businessStore'
import useStorage from '@/utils/hsj/useStorage'
import { VueDraggable } from 'vue-draggable-plus'
import { PageContentProps, Emits } from './types/types'
import { proxy } from '@/utils/provide'
const {
  contentConfig,
  tableListener = {},
  pageName,
  firstSendOption,
  autoSend = true,
  autoDesc = true,
  descConfig = {
    orderByColumn: 'createTime',
    isAsc: 'desc',
  },
  otherRequestOption = {},
  piniaConfig = {
    listConfig: {
      listKey: 'rows',
      countKey: 'total',
    },
    handleList: (list: any[]) => list,
  },
  idKey,
  requestBaseUrl = '/',
  requestUrl = 'list',
  showEdit = true,
  showDelete = true,
  dictMap = {},
  headerButtons = [
    'refresh',
    'add',
    'edit',
    'delete',
    'columnDisplay',
    'comSearch',
  ],
  tableSelected = [],
  permission = {},
  handleEditShow = (_backData: any) => true,
  handleDeleteShow = (_backData: any) => true,
  delUrl,
  maxHeightDecrement = 0,
  tableHideItems = [],
  cacheKey = '',
} = defineProps<PageContentProps>()
const emit = defineEmits<Emits>()
const slots = defineSlots()
const store = businessStore()
const isLoading = ref(false)
const baseTableRef = useTemplateRef('baseTableRef')
const searchDatas = ref<anyObj>({})
const paginationInfo = ref({
  pageNum: 1,
  pageSize: contentConfig?.defaultPageSize || 50,
})

if (contentConfig?.pagination) {
  paginationInfo.value = {
    pageNum: 1,
    pageSize: contentConfig?.defaultPageSize || 50,
  }
}
// 所有的搜索条件的汇总
const finalSearchData = computed<anyObj>(() => {
  if (contentConfig.pagination) {
    return { ...searchDatas.value, ...paginationInfo.value }
  } else {
    return { ...searchDatas.value }
  }
})

watch(
  () => paginationInfo.value,
  (newValue, oldValue) => {
    // 当pageSize发生变化时将pageNum设置成第一页
    if (newValue.pageSize !== oldValue.pageSize) {
      paginationInfo.value.pageNum = 1
    }
    debounceSend(finalSearchData.value)
  }
)

const send = async (searchInfo: anyObj) => {
  isLoading.value = true
  emit('beforeSend', searchInfo)
  // 调用获取list的Store
  let isSuccess = await store.getList(
    {
      pageName: pageName,
      requestUrl: requestUrl,
      queryInfo: {
        ...otherRequestOption,
        ...searchInfo,
      },
      cacheKey: cacheKey,
      requestBaseUrl: requestBaseUrl,
    },
    piniaConfig.listConfig,
    piniaConfig.handleList
  )
  if (isSuccess) {
    emit('afterSend', store.pageListData(pageName, cacheKey))
  }
  isLoading.value = false
}
const debounceSend = debounce(send, 66)
// 表格数据
const dataList = computed(() => {
  const list = store.pageListData(pageName, cacheKey)
  return list ? list : []
})
// 总数量
const listCount = computed(() => {
  return store.listCount(pageName, cacheKey) ?? 0
})
const showPageSearch = computed(() => {
  return store.pageSearchControl[`${pageName}${cacheKey}SearchShow`]
})
// 删除按钮
const deleteRow = async (delData: anyObj) => {
  isLoading.value = true
  let id: StrNum
  if (Array.isArray(delData)) {
    const ids = delData.map((item) => {
      if (idKey) {
        return item[idKey]
      }
      return item[pageName + 'Id'] ?? item.id
    })
    id = ids.toString()
  } else {
    if (idKey) {
      id = delData[idKey]
    } else {
      id = delData[pageName + 'Id'] ?? delData.id
    }
  }
  if (id || id === 0) {
    await to(
      store.deletDataAction({
        id,
        pageName: pageName,
        requestUrl: requestUrl,
        requestBaseUrl: requestBaseUrl,
        delUrl: delUrl,
      })
    )
    await to(send(finalSearchData.value))
  } else {
    proxy.$modal.notifyWarning('未获取到有效Id')
  }
  isLoading.value = false
}

// 编辑按钮
const editClick = async (item: anyObj, type?: any) => {
  isLoading.value = true
  // 取出当前点击这一行数据的id 优先props传入的idKey
  let id
  if (idKey) {
    id = item[idKey]
  } else {
    id = item[pageName + 'Id'] ?? item.id
  }
  if (id || id === 0) {
    // 拼接getInfo请求的url地址
    let url = `${requestBaseUrl}${interceptor(pageName)}/${id}`
    let [res] = await to(getInfo(url))
    if (res?.data) {
      emit('editBtnClick', res.data, type, res)
    }
  } else {
    proxy.$modal.notifyWarning('未获取到有效Id')
  }
  isLoading.value = false
}

const addClick = () => {
  emit('addClick')
}

// 其他的插槽
const exceptSlot = ['todo']
// 用于收集contentConfig的所有插槽名称
const collectObjectsWithSlotName = (
  data: BaseTableItem[] | BaseTableItem,
  collectedObjects: BaseTableItem[] = []
) => {
  if (Array.isArray(data)) {
    // 如果当前层级是数组
    data.forEach((item) => {
      collectObjectsWithSlotName(item, collectedObjects)
    })
  } else if (typeof data === 'object' && data !== null) {
    // 如果当前层级是对象
    if ('slotName' in data && !exceptSlot.includes(data.slotName!)) {
      collectedObjects.push(data) // 当前对象有 slotName 属性，将其加入结果数组
    }
    Object.values(data).forEach((value) => {
      collectObjectsWithSlotName(value, collectedObjects)
    })
  }
  return collectedObjects
}
// 所有插槽名称
let otherSlot = collectObjectsWithSlotName(contentConfig?.tableItem)
// 排序发生变化触发的函数
const sortChange = (shortData: { column: any; prop: string; order: any }) => {
  const { order, prop } = shortData
  let isAsc: 'desc' | 'asc' | undefined
  if (order === 'ascending') {
    isAsc = 'asc'
  } else if (order === 'descending') {
    isAsc = 'desc'
  }
  let orderObj: OrderType = {
    orderByColumn: void 0,
    isAsc: void 0,
  }
  if (isAsc) {
    orderObj = {
      orderByColumn: prop,
      isAsc,
    }
  } else if (descConfig && autoDesc) {
    // 如果没有排序就使用默认的查询条件的排序属性
    orderObj = { ...descConfig }
  }
  searchDatas.value = Object.assign({ ...searchDatas.value }, { ...orderObj })
  send(finalSearchData.value)
}
// 页面数据刷新函数
const refresh = () => {
  send(finalSearchData.value)
}
// 页面的mitt监听事件
const mittFunc = (searchFormData: any) => {
  searchDatas.value = Object.assign({}, searchDatas.value, searchFormData)
  // searchLoading是pageSearch的loading效果，这里删除是为了防止代入到查询条件中
  if (searchDatas.value.hasOwnProperty('searchLoading')) {
    delete searchDatas.value.searchLoading
  }
  // resetPaginationInfo是用于判断是否将pageNum重置到第一页再进行搜索
  if (searchDatas.value.hasOwnProperty('resetPaginationInfo')) {
    delete searchDatas.value.resetPaginationInfo
  }
  if (searchFormData.resetPaginationInfo) {
    paginationInfo.value.pageNum = 1
  }
  send(finalSearchData.value).finally(() => {
    if (searchFormData.searchLoading) searchFormData.searchLoading.value = false
  })
  // 网络请求完毕，loading设置成false
}
// table的最大高度
const maxHeight = ref(500)
// 页面pageSearch的高度
let currentSearchHeight = 0
// 动态计算maxHeigth
const mittResize = (searchHeight?: any) => {
  if (typeof searchHeight === 'number') {
    currentSearchHeight = searchHeight
  }
  // 获取头部的高度
  const header = document.getElementsByClassName('el-header')[0]
  // 计算公式为 视口高度-搜索栏高度-marginTop-marginBottom-elTable的borderButtom
  let viewportHeight = window.innerHeight - currentSearchHeight - 34
  // 如果header存在会再减去header的高度，因为某些布局没有header
  if (header) {
    viewportHeight -= header.clientHeight
  }
  maxHeight.value = viewportHeight - maxHeightDecrement
  // console.log(maxHeight.value)
}
// 判断页面是否已经进行监听过，主要用于页面keep-alive后防止多次监听使用
let isListen = false
// 页面事件监听
const onListener = () => {
  if (!isListen) {
    isListen = true
    emitter.on(`search${pageName}Info`, mittFunc)
    emitter.on(`change${pageName}Size`, mittResize)
    window.addEventListener('resize', mittResize)
  }
}
// 取消页面监听事件
const offListener = () => {
  emitter.off(`search${pageName}Info`)
  emitter.off(`change${pageName}Size`)
  window.removeEventListener('resize', mittResize)
  isListen = false
}
// 记录哪些列需要展示
const columnChecked = ref<string[]>([])
let filterArr = ref<string[]>([])
const propsTableHideItems = computed(() => {
  let hideItems = contentConfig.hideItems || []
  if (isRef(hideItems)) {
    hideItems = hideItems.value
  }
  return [...new Set([...tableHideItems, ...hideItems])]
})
let userHideItems = [...propsTableHideItems.value]
const setHideColumnStorage = () => {
  const hidenColumns = useStorage.get('hidenColumns')
  if (hidenColumns) {
    hidenColumns[pageName] = userHideItems
    useStorage.set('hidenColumns', hidenColumns)
  } else {
    useStorage.set('hidenColumns', {
      [pageName]: userHideItems,
    })
  }
}
const onChangeShowColumn = (
  checked: boolean,
  prop: string | string[],
  handleUser: boolean
) => {
  const isArray = Array.isArray(prop)
  if (!isArray) {
    if (checked) {
      filterArr.value = filterArr.value.filter((item) => item !== prop)
    } else {
      filterArr.value = [...new Set([...filterArr.value, prop])]
    }
  }
  // 是否为用户点击
  if (handleUser) {
    if (checked) {
      userHideItems = userHideItems.filter((item) => item !== prop)
    } else if (!isArray) {
      userHideItems = [...new Set([...userHideItems, prop])]
    }
    setHideColumnStorage()
  }
  emit('onChangeShowColumn', filterArr.value)
}
watch(
  () => contentConfig,
  () => {
    let hidenColumns = useStorage.get('hidenColumns')
    let tableHides = []
    // 判断用户是否对该页面进行过设置
    if (hidenColumns && hidenColumns[pageName]) {
      userHideItems = hidenColumns[pageName]
      tableHides = userHideItems
      onChangeShowColumn(false, tableHides, false)
    } else {
      tableHides = [
        ...new Set([...userHideItems, ...propsTableHideItems.value]),
      ]
    }
    contentConfig.tableItem.forEach((item) => {
      if (item.prop) {
        if (tableHides.includes(item.prop)) {
          filterArr.value.push(item.prop)
          onChangeShowColumn(false, item.prop, false)
        } else {
          columnChecked.value.push(item.prop)
        }
      }
    })
  },
  {
    immediate: true,
  }
)
// 用于隐藏页面的pageSearch组件
const triggerShowSearch = () => {
  store.handlePageSearch(pageName, cacheKey)
}
// 多选后的编辑按钮点击
const editMoreClick = () => {
  emit('editMoreClick')
}
// 用于判断父组件使用是否有某插槽
const hasSlot = (arr: string[]) => {
  return arr.some((key) => slots.hasOwnProperty(key))
}

const columnsFilter = () => {
  // 列的权限判断
  const tableItem = contentConfig.tableItem.filter((item) => {
    if (!item.permission) return true
    return proxy.hasPermi(item.permission)
  })
  // 排序
  const orderColumns: Record<string, string[]> = useStorage.get('orderColumns')
  if (!orderColumns) {
    contentConfig.tableItem = tableItem
    return
  }
  const pageColumns = orderColumns[pageName]
  if (!pageColumns) {
    contentConfig.tableItem = tableItem
    return
  }
  const itemMap = new Map(tableItem.map((item) => [item.prop, item]))
  // 使用 map 方法根据 sortOrder 重新排列 items
  const sortedItems = pageColumns.map((key) => itemMap.get(key)!)
  // 找出不在 sortOrder 中的项
  const remainingItems = tableItem.filter(
    (item) => !pageColumns.includes(item.prop!)
  )
  // 将已排序的项和剩余的项合并
  const sortedTableItem = [...sortedItems, ...remainingItems]
  contentConfig.tableItem = sortedTableItem
}

const dragUpdate = () => {
  // 重新渲染列表
  const getListName = `${pageName}List`
  const list = store.listInfo[getListName]
  store.$patch((state) => {
    state.listInfo[getListName] = [...list]
  })
  const tableItem = contentConfig.tableItem
  // 本地存储排序
  const order = []
  for (const item of tableItem) {
    order.push(item.prop)
  }
  const orderColumns = useStorage.get('orderColumns')
  if (orderColumns) {
    orderColumns[pageName] = order
    useStorage.set('orderColumns', orderColumns)
  } else {
    useStorage.set('orderColumns', {
      [pageName]: order,
    })
  }
}
const handleDefaultSort = () => {
  const orderColumns = useStorage.get('orderColumns')
  const pageColumns = orderColumns[pageName]
  if (pageColumns) {
    delete orderColumns[pageName]
    useStorage.set('orderColumns', orderColumns)
    proxy.$tab.refreshPage()
  }
}
const init = () => {
  columnsFilter()
}

onMounted(() => {
  // 判断是否需要自动排序
  if (autoDesc) {
    let shortData: anyObj = {}
    // 默认使用elTableConfig的defaultSort，再使用外界传入了descConfig
    if (contentConfig?.elTableConfig?.defaultSort) {
      const sort = contentConfig.elTableConfig.defaultSort
      shortData.orderByColumn = sort.prop
      if (sort.order === 'ascending') {
        shortData.isAsc = 'asc'
      } else if (sort.order === 'descending') {
        shortData.isAsc = 'desc'
      }
    } else if (descConfig) {
      shortData = descConfig
    }
    for (const [key, value] of Object.entries(shortData)) {
      searchDatas.value[key] = value
    }
  }
  if (autoSend) {
    let obj = {
      ...firstSendOption,
    }
    obj = Object.assign({}, obj, finalSearchData.value)
    send({
      ...obj,
    })
  }
  mittResize()
  onListener()
})
onBeforeUnmount(() => {
  offListener()
})
onActivated(() => {
  onListener()
})
onDeactivated(() => {
  offListener()
})

init()
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
  <div class="page-content" v-loading="isLoading">
    <BaseTable
      ref="baseTableRef"
      @sortChange="sortChange"
      v-model:paginationInfo="paginationInfo"
      :dataList="dataList"
      :listCount="listCount"
      :tableListener="tableListener"
      :maxHeight="maxHeight"
      v-bind="contentConfig"
    >
      <!-- 操作按钮 -->
      <template #header>
        <el-scrollbar class="headerScroll">
          <div class="tableHeader">
            <div v-if="hasSlot(['handleLeft']) || headerButtons.length !== 0">
              <div class="flex">
                <el-button
                  v-if="headerButtons.includes('refresh')"
                  type="info"
                  color="#40485b"
                  @click="refresh"
                >
                  <SvgIcon iconClass="refresh" :size="13"></SvgIcon>
                </el-button>
                <el-button
                  type="primary"
                  class="order5"
                  v-if="headerButtons.includes('add')"
                  v-hasPermi="[permission.add]"
                  @click="addClick"
                >
                  <SvgIcon :size="14" iconClass="plus"></SvgIcon>
                  <span class="ml6">添加</span>
                </el-button>
                <el-button
                  type="primary"
                  class="order10"
                  v-if="headerButtons.includes('edit')"
                  :disabled="tableSelected.length === 0"
                  v-hasPermi="[permission.edit]"
                  @click="editMoreClick"
                >
                  <SvgIcon :size="14" iconClass="pencil"></SvgIcon>
                  <span class="ml6">编辑</span>
                </el-button>
                <el-popconfirm
                  v-if="
                    headerButtons.includes('delete') && hasPermi(permission.del)
                  "
                  title="确定删除选中记录？"
                  confirm-button-text="确认"
                  cancel-button-text="取消"
                  confirmButtonType="danger"
                  :hide-after="0"
                  @confirm="deleteRow(tableSelected)"
                >
                  <template #reference>
                    <el-button
                      class="order15"
                      type="danger"
                      :disabled="tableSelected.length === 0"
                    >
                      <SvgIcon :size="14" iconClass="trash"></SvgIcon>
                      <span class="ml6">删除</span>
                    </el-button>
                  </template>
                </el-popconfirm>
                <slot name="handleLeft"></slot>
              </div>
            </div>
            <div v-if="hasSlot(['handleRight']) || headerButtons.length !== 0">
              <div
                class="table-search-button-group"
                v-if="
                  headerButtons.includes('columnDisplay') ||
                  headerButtons.includes('comSearch')
                "
              >
                <el-dropdown
                  v-if="headerButtons.includes('columnDisplay')"
                  :max-height="380"
                  :hide-on-click="false"
                  popper-class="columnDisplay"
                >
                  <el-button
                    class="table-search-button-item"
                    :class="
                      headerButtons.includes('columnDisplay')
                        ? 'right-border'
                        : ''
                    "
                    plain
                  >
                    <SvgIcon size="14" iconClass="el-icon-Grid" />
                  </el-button>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-checkbox-group v-model="columnChecked">
                        <VueDraggable
                          ref="el"
                          v-model="contentConfig.tableItem"
                          :animation="150"
                          ghostClass="ghost"
                          @update="dragUpdate"
                        >
                          <el-dropdown-item
                            v-for="(item, idx) in contentConfig.tableItem"
                            :key="idx"
                          >
                            <el-checkbox
                              v-if="item.prop"
                              @change="
                                onChangeShowColumn(
                                  $event as boolean,
                                  item.prop,
                                  true
                                )
                              "
                              size="small"
                              :value="item.prop"
                              >{{ item.label }}
                            </el-checkbox>
                          </el-dropdown-item>
                        </VueDraggable>
                      </el-checkbox-group>
                      <el-dropdown-item divided class="dropBtnItem">
                        <div class="dropBtns">
                          <el-button size="small" @click="handleDefaultSort">
                            恢复默认排序
                          </el-button>
                        </div>
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
                <el-tooltip
                  v-if="headerButtons.includes('comSearch')"
                  :content="showPageSearch ? '关闭搜索' : '展开搜索'"
                  placement="top"
                >
                  <el-button
                    class="table-search-button-item"
                    @click="triggerShowSearch"
                    plain
                  >
                    <SvgIcon size="14" iconClass="el-icon-Search" />
                  </el-button>
                </el-tooltip>
              </div>
              <slot name="handleRight"></slot>
            </div>
          </div>
        </el-scrollbar>
      </template>

      <template #expand="{ backData }">
        <div>
          <slot name="expand" :backData="backData"></slot>
        </div>
      </template>
      <template #todo="{ backData }">
        <div class="todo flexCenter">
          <slot name="todoSlot" :backData="backData"></slot>
          <el-button
            class="edit order5"
            v-if="showEdit && handleEditShow(backData)"
            v-hasPermi="[permission.edit]"
            type="primary"
            size="small"
            @click="editClick(backData)"
          >
            <SvgIcon :size="12" iconClass="pencil"></SvgIcon>
            <span class="ml6">编辑</span>
          </el-button>
          <el-popconfirm
            title="确定删除选中记录？"
            confirm-button-text="确认"
            cancel-button-text="取消"
            confirmButtonType="danger"
            :hide-after="0"
            @confirm="deleteRow(backData)"
            v-if="
              hasPermi(permission.del) &&
              showDelete &&
              handleDeleteShow(backData)
            "
          >
            <template #reference>
              <el-button class="del order10" type="danger" size="small">
                <SvgIcon :size="12" iconClass="trash"></SvgIcon>
                <span class="ml6">删除</span>
              </el-button>
            </template>
          </el-popconfirm>
        </div>
      </template>
      <template
        v-for="item in otherSlot"
        :key="item.prop"
        #[item.slotName]="{ backData, currentItem }"
      >
        <template v-if="item.slotName">
          <slot
            :name="item.slotName"
            :backData="backData"
            :currentItem="currentItem"
          ></slot>
        </template>
        <template v-if="item.isDict">
          <DictCpn
            :value="backData[item.prop]"
            :options="dictMap[item.prop!]"
          ></DictCpn>
        </template>
      </template>
      <template
        v-for="item in otherSlot"
        :key="item.prop"
        #[`${item.slotName}Header`]="{ backData }"
      >
        <slot :name="item.slotName + 'Header'" :backData="backData"></slot>
      </template>
    </BaseTable>
  </div>
</template>

<style scoped lang="scss">
.todo {
  flex-wrap: wrap;
  :deep(.el-button) {
    margin: 4px;
  }
}
.headerScroll {
  width: 100%;
}
.tableHeader {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: none;
  font-size: 14px;
  padding: 13px 0px;
}

.table-search-button-group {
  display: flex;
  margin-left: 12px;
  border: 1px solid var(--el-border-color);
  border-radius: var(--el-border-radius-base);
  overflow: hidden;
  button:focus,
  button:active {
    color: #000;
    background-color: var(--ba-bg-color-overlay);
  }
  button:hover {
    color: #000;
    background-color: var(--el-color-info-light-7);
  }
  .table-search-button-item {
    height: 30px;
    border: none;
    border-radius: 0;
  }
  .el-button + .el-button {
    margin: 0;
  }
  .right-border {
    border-right: 1px solid var(--el-border-color);
  }
  :deep(.el-button:focus-visible) {
    outline: none;
    outline-offset: 0;
  }
}
.dropBtns {
  padding: 5px 16px;
}
html.dark {
  .table-search-button-group {
    button:focus,
    button:active {
      background-color: var(--el-color-info-dark-2);
    }
    button:hover {
      background-color: var(--el-color-info-light-7);
    }
    button {
      background-color: #898a8d;
      el-icon {
        color: white !important;
      }
    }
  }
}
</style>
<style lang="scss">
.columnDisplay {
  .el-dropdown-menu__item {
    padding: 0;
  }
  .el-checkbox {
    width: 100%;
    padding: 5px 16px;
    height: 32px;
  }
  .dropBtnItem {
    background-color: transparent !important;
  }
}
</style>
