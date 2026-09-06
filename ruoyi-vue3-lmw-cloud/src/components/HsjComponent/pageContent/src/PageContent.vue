<script setup>
import BaseTable from '@/BaseComponent/BaseTable/index'
import emitter from '@/utils/hsj/bus'
import businessStore from '@/store/business/businessStore'
import { getInfo } from '@/api/business/main/index'
import to from '@/utils/to'
import DictCpn from './dictCpn.vue'
import { interceptor } from '@/store/business/businessStore'
import useStorage from '@/utils/hsj/useStorage'
import { antiShake } from '@/utils/hsj/utils'
import { VueDraggable } from 'vue-draggable-plus'
const props = defineProps({
  // table的配置
  contentConfig: {
    type: Object,
    default: () => {},
  },
  // table的事件监听
  tableListener: {
    type: Object,
    default: () => {},
  },
  // 页面名称与PageSearch和PageDialog的一致，每个页面必须唯一
  pageName: {
    type: String,
    required: true,
  },
  // 页面第一次进入的查询条件
  firstSendOption: {
    type: Object,
  },
  // 是否自动发送查询请求
  autoSend: {
    type: Boolean,
    default: true,
  },
  // 是否自动排序
  autoDesc: {
    type: Boolean,
    default: true,
  },
  // 排序的参数
  descConfig: {
    type: Object,
    default: () => {
      return {
        orderByColumn: 'createTime',
        isAsc: 'desc',
      }
    },
  },
  // 其他查询条件
  otherRequestOption: {
    type: Object,
    default: () => {},
  },
  piniaConfig: {
    type: Object,
    default: () => {
      return {
        // 接口请求到数据后从哪些字段中读取数据和总条数
        listConfig: { listKey: 'rows', countKey: 'total' },
        // 接口请求完毕后需要对请求到的list进行哪些二次处理
        handleList: (list) => list,
      }
    },
  },
  // 删除和getInfo接口所需要传入的id需要从row的哪个字段来取
  idKey: {
    type: String,
  },
  // 列表请求地址
  // 规则是 requestBaseUrl+ interceptor(pageName) + requestUrl
  requestBaseUrl: {
    type: String,
    default: '/',
  },
  requestUrl: {
    type: String,
    default: 'list',
  },
  // 显示编辑按钮
  showEdit: {
    type: Boolean,
    default: true,
  },
  // 显示编辑删除
  showDelete: {
    type: Boolean,
    default: true,
  },
  // 列表的数据字典反显用
  dictMap: {
    type: Object,
    default: () => {
      return {}
    },
  },
  // header需要显示哪些按钮
  headerButtons: {
    type: Array,
    default: () => [
      'refresh',
      'add',
      'edit',
      'delete',
      'columnDisplay',
      'comSearch',
    ],
  },
  tableSelected: {
    type: Array,
    default: () => [],
  },
  // 权限
  permission: {
    type: Object,
    default: () => ({}),
  },
  // 当你使用了todo的插槽后，会显示编辑和删除两个按钮。
  // 如果你需要根据列表的状态来控制这两个按钮可以传入这个函数
  handleEditShow: {
    type: Function,
    default: () => {
      return true
    },
  },
  // 同上
  handleDeleteShow: {
    type: Function,
    default: () => {
      return true
    },
  },
  // 删除的url 注意只能配置前一部分url
  // 例如 page/list/1 这个id 1 不能在这里配置，还是需要用过配置idKey来从row中读取
  delUrl: {
    type: String,
  },
  // 如果该组件计算的table的maxHeight不符合预期，那么可以通过这个参数来调整
  maxHeightDecrement: {
    type: Number,
    default: 0,
  },
  // table需要隐藏的列
  tableHideItems: {
    type: Array,
    default: () => [],
  },
  // 当页面会存在打开多个的时候会出现缓存问题，可以用这个区分缓存
  cacheKey: {
    type: [String, Number],
    default: '',
  },
})
const emit = defineEmits([
  'addClick',
  'editBtnClick',
  'beforeSend',
  'afterSend',
  'triggerShowSearch',
  'onChangeShowColumn',
  'selectionChange',
  'editMoreClick',
])
const store = businessStore()
const proxy = inject('proxy')
const isLoading = ref(false)
const baseTabelRef = ref(null)
const searchDatas = ref({})
const paginationInfo = ref({
  pageNum: 1,
  pageSize: props.contentConfig?.defaultPageSize || 50,
})

if (props.contentConfig?.pagination) {
  paginationInfo.value = {
    pageNum: 1,
    pageSize: props.contentConfig?.defaultPageSize || 50,
  }
}
// 所有的搜索条件的汇总
const finalSearchData = computed(() => {
  if (props.contentConfig.pagination) {
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
    antiShakeSend(finalSearchData.value)
  }
)

const send = async (searchInfo) => {
  isLoading.value = true
  emit('beforeSend', searchInfo)
  // 调用获取list的Store
  let isSuccess = await store.getList(
    {
      pageName: props.pageName,
      requestUrl: props.requestUrl,
      queryInfo: {
        ...props.otherRequestOption,
        ...searchInfo,
      },
      cacheKey: props.cacheKey,
      requestBaseUrl: props.requestBaseUrl,
    },
    props.piniaConfig.listConfig,
    props.piniaConfig.handleList
  )
  if (isSuccess) {
    emit('afterSend', store.pageListData(props.pageName))
  }
  isLoading.value = false
}
const antiShakeSend = antiShake(send, 66)
// 表格数据
const dataList = computed(() => {
  const list = store.pageListData(props.pageName, props.cacheKey)
  return list ? list : []
})
// 总数量
const listCount = computed(() => {
  return store.listCount(props.pageName, props.cacheKey) ?? 0
})
const showPageSearch = computed(() => {
  return store.pageSearchControl[`${props.pageName}${props.cacheKey}SearchShow`]
})
// 删除按钮
const deleteRow = async (delData) => {
  isLoading.value = true
  let id = ''
  if (Array.isArray(delData)) {
    const ids = delData.map((item) => {
      return item[props.idKey] ?? item[props.pageName + 'Id'] ?? item.id
    })
    id = ids.toString()
  } else {
    id = delData[props.idKey] ?? delData[props.pageName + 'Id'] ?? delData.id
  }
  if (id || id === 0) {
    await to(
      store.deletDataAction({
        id,
        pageName: props.pageName,
        requestUrl: props.requestUrl,
        requestBaseUrl: props.requestBaseUrl,
        delUrl: props.delUrl,
      })
    )
    await to(send(finalSearchData.value))
  } else {
    proxy.$modal.notifyWarning('未获取到有效Id')
  }
  isLoading.value = false
}

// 编辑按钮
const editClick = async (item, type) => {
  isLoading.value = true
  // 取出当前点击这一行数据的id 优先props传入的idKey
  let id = item[props.idKey] ?? item[props.pageName + 'Id'] ?? item.id
  if (id || id === 0) {
    // 拼接getInfo请求的url地址
    let url = `${props.requestBaseUrl}${interceptor(props.pageName)}/${id}`
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
const collectObjectsWithSlotName = (data, collectedObjects = []) => {
  if (Array.isArray(data)) {
    // 如果当前层级是数组
    data.forEach((item) => {
      collectObjectsWithSlotName(item, collectedObjects)
    })
  } else if (typeof data === 'object' && data !== null) {
    // 如果当前层级是对象
    if ('slotName' in data && !exceptSlot.includes(data.slotName)) {
      collectedObjects.push(data) // 当前对象有 slotName 属性，将其加入结果数组
    }
    Object.values(data).forEach((value) => {
      collectObjectsWithSlotName(value, collectedObjects)
    })
  }
  return collectedObjects
}
// 所有插槽名称
let otherSlot = collectObjectsWithSlotName(props.contentConfig?.tableItem)
// 排序发生变化触发的函数
const sortChange = (shortData) => {
  const { order, prop } = shortData
  let isAsc = void 0
  if (order === 'ascending') {
    isAsc = 'asc'
  } else if (order === 'descending') {
    isAsc = 'desc'
  }
  let orderObj = {
    orderByColumn: void 0,
    isAsc: void 0,
  }
  if (isAsc) {
    orderObj = {
      orderByColumn: prop,
      isAsc,
    }
  } else if (props.descConfig && props.autoDesc) {
    // 如果没有排序就使用默认的查询条件的排序属性
    for (const [key, value] of Object.entries(props.descConfig)) {
      orderObj[key] = value
    }
  }
  searchDatas.value = Object.assign({ ...searchDatas.value }, { ...orderObj })
  send(finalSearchData.value)
}
// 页面数据刷新函数
const refresh = () => {
  send(finalSearchData.value)
}
// 页面的mitt监听事件
const mittFunc = async (searchFormData) => {
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
  await send(finalSearchData.value)
  // 网络请求完毕，loading设置成false
  if (searchFormData.searchLoading) searchFormData.searchLoading.value = false
}
// table的最大高度
const maxHeight = ref(500)
// 页面pageSearch的高度
let currentSearchHeight = 0
// 动态计算maxHeigth
const mittResize = (searchHeight) => {
  if (typeof searchHeight === 'number') {
    currentSearchHeight = searchHeight
  }
  // 获取头部的高度
  const header = document.getElementsByClassName('el-header')[0]
  // 获取分页的高度
  const pagination = document.getElementsByClassName('lmw-pagination-footer')[0]
  // 计算公式为 视口高度-搜索栏高度-marginTop-marginBottom-elTable的borderButtom
  let viewportHeight = window.innerHeight - currentSearchHeight - 16 - 16 - 1
  // 如果header存在会再减去header的高度，因为某些布局没有header
  if (header) {
    viewportHeight -= header.clientHeight
  }
  // 分页存在，减去分页的高度
  if (pagination) {
    viewportHeight -= pagination.clientHeight
  }
  maxHeight.value = viewportHeight - props.maxHeightDecrement
}
// 判断页面是否已经进行监听过，主要用于页面keep-alive后防止多次监听使用
let isListen = false
// 页面事件监听
const onListener = () => {
  if (!isListen) {
    isListen = true
    emitter.on(`search${props.pageName}Info`, mittFunc)
    emitter.on(`change${props.pageName}Size`, mittResize)
    window.addEventListener('resize', mittResize)
  }
}
// 取消页面监听事件
const offListener = () => {
  emitter.off(`search${props.pageName}Info`)
  emitter.off(`change${props.pageName}Size`)
  window.removeEventListener('resize', mittResize)
  isListen = false
}
// 记录哪些列需要展示
const columnChecked = ref([])
let filterArr = ref([])
const propsTableHideItems = computed(() => {
  let hideItems = props.contentConfig.hideItems || []
  if (isRef(hideItems)) {
    hideItems = hideItems.value
  }
  return [...new Set([...props.tableHideItems, ...hideItems])]
})
let userHideItems = [...propsTableHideItems.value]
const setHideColumnStorage = () => {
  const hidenColumns = useStorage.get('hidenColumns')
  if (hidenColumns) {
    hidenColumns[props.pageName] = userHideItems
    useStorage.set('hidenColumns', hidenColumns)
  } else {
    useStorage.set('hidenColumns', {
      [props.pageName]: userHideItems,
    })
  }
}
const onChangeShowColumn = (checked, prop, handleUser) => {
  if (checked) {
    filterArr.value = filterArr.value.filter((item) => item !== prop)
  } else {
    filterArr.value = [...new Set([...filterArr.value, prop])]
  }
  // 是否为用户点击
  if (handleUser) {
    if (checked) {
      userHideItems = userHideItems.filter((item) => item !== prop)
    } else {
      userHideItems = [...new Set([...userHideItems, prop])]
    }
    setHideColumnStorage()
  }
  emit('onChangeShowColumn', filterArr.value)
}
watch(
  () => props.contentConfig,
  () => {
    let hidenColumns = useStorage.get('hidenColumns')
    let tableHides = []
    // 判断用户是否对该页面进行过设置
    if (hidenColumns && hidenColumns[props.pageName]) {
      userHideItems = hidenColumns[props.pageName]
      tableHides = userHideItems
      onChangeShowColumn(false, tableHides, false)
    } else {
      tableHides = [
        ...new Set([...userHideItems, ...propsTableHideItems.value]),
      ]
    }
    props.contentConfig.tableItem.forEach((item) => {
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
  store.handlePageSearch(props.pageName, props.cacheKey)
}
// 多选后的编辑按钮点击
const editMoreClick = () => {
  emit('editMoreClick')
}
// 用于判断父组件使用是否有某插槽
const hasSlot = (slots, arr) => {
  return arr.some((key) => slots.hasOwnProperty(key))
}

const columnsFilter = () => {
  // 列的权限判断
  const tableItem = props.contentConfig.tableItem.filter((item) => {
    if (!item.permission) return true
    return proxy.hasPermi(item.permission)
  })
  // 排序
  const orderColumns = useStorage.get('orderColumns')
  if (!orderColumns) {
    props.contentConfig.tableItem = tableItem
    return
  }
  const pageColumns = orderColumns[props.pageName]
  if (!pageColumns) {
    props.contentConfig.tableItem = tableItem
    return
  }
  const itemMap = new Map(tableItem.map((item) => [item.prop, item]))
  // 使用 map 方法根据 sortOrder 重新排列 items
  const sortedItems = pageColumns.map((key) => itemMap.get(key))
  // 找出不在 sortOrder 中的项
  const remainingItems = tableItem.filter(
    (item) => !pageColumns.includes(item.prop)
  )
  // 将已排序的项和剩余的项合并
  const sortedTableItem = [...sortedItems, ...remainingItems]
  props.contentConfig.tableItem = sortedTableItem
}

const dragUpdate = () => {
  // 重新渲染列表
  const getListName = `${props.pageName}List`
  const list = store.listInfo[getListName]
  store.$patch((state) => {
    state.listInfo[getListName] = [...list]
  })
  const tableItem = props.contentConfig.tableItem
  // 本地存储排序
  const order = []
  for (const item of tableItem) {
    order.push(item.prop)
  }
  const orderColumns = useStorage.get('orderColumns')
  if (orderColumns) {
    orderColumns[props.pageName] = order
    useStorage.set('orderColumns', orderColumns)
  } else {
    useStorage.set('orderColumns', {
      [props.pageName]: order,
    })
  }
}
const init = () => {
  columnsFilter()
}

onMounted(() => {
  // 判断是否需要自动排序
  if (props.autoDesc) {
    let shortData = {}
    // 默认使用elTableConfig的defaultSort，再使用外界传入了descConfig
    if (props.contentConfig?.elTableConfig?.defaultSort) {
      const sort = props.contentConfig.elTableConfig.defaultSort
      shortData.orderByColumn = sort.prop
      if (sort.order === 'ascending') {
        shortData.isAsc = 'asc'
      } else if (sort.order === 'descending') {
        shortData.isAsc = 'desc'
      }
    } else if (props.descConfig) {
      shortData = props.descConfig
    }
    for (const [key, value] of Object.entries(shortData)) {
      searchDatas.value[key] = value
    }
  }
  if (props.autoSend) {
    let obj = {
      ...props.firstSendOption,
    }
    obj = Object.assign({}, obj, finalSearchData.value)
    send({
      ...obj,
    })
  }
  mittResize()
  onListener()
})
onUnmounted(() => {
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
  baseTabelRef,
  deleteRow,
  editClick,
  dataList,
})
</script>

<template>
  <div class="page-content" v-loading="isLoading">
    <BaseTable
      ref="baseTabelRef"
      @sortChange="sortChange"
      v-model:paginationInfo="paginationInfo"
      :dataList="dataList"
      :listCount="listCount"
      :tableListener="tableListener"
      :maxHeight="maxHeight"
      v-bind="contentConfig"
    >
      <!-- 操作按钮 -->
      <template
        v-if="hasSlot($slots, ['handleLeft']) || headerButtons.length !== 0"
        #handleLeft
      >
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
            v-if="headerButtons.includes('delete') && hasPermi(permission.del)"
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
      </template>
      <template
        v-if="hasSlot($slots, ['handleRight']) || headerButtons.length !== 0"
        #handleRight
      >
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
                headerButtons.includes('columnDisplay') ? 'right-border' : ''
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
                        @change="onChangeShowColumn($event, item.prop, true)"
                        size="small"
                        :value="item.prop"
                        >{{ item.label }}
                      </el-checkbox>
                    </el-dropdown-item>
                  </VueDraggable>
                </el-checkbox-group>
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
            <SvgIcon :size="10" iconClass="pencil"></SvgIcon>
            <span class="ml6">编辑</span>
          </el-button>
          <el-popconfirm
            title="确定删除选中记录？"
            confirm-button-text="确认"
            cancel-button-text="取消"
            confirmButtonType="danger"
            :hide-after="0"
            @confirm="deleteRow(backData)"
            v-if="hasPermi(permission.del)"
          >
            <template #reference>
              <el-button
                class="del order10"
                type="danger"
                size="small"
                v-if="showDelete && handleDeleteShow(backData)"
              >
                <SvgIcon :size="10" iconClass="trash"></SvgIcon>
                <span class="ml6">删除</span>
              </el-button>
              <span></span>
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
            :options="dictMap[item.prop]"
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
}
</style>
