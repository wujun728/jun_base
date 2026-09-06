<script setup>
import BaseTableV2 from '@/BaseComponent/BaseTableV2/index'
import emitter from '@/utils/hsj/bus'
import businessStore from '@/store/business/businessStore'
import { antiShake } from '@/utils/hsj/utils'
import { interceptor } from '@/store/business/businessStore'
import { getInfo } from '@/api/business/main/index'
import to from '@/utils/to'
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
  // header需要显示哪些按钮
  headerButtons: {
    type: Array,
    default: () => ['refresh', 'add', 'comSearch'],
  },
  // 权限
  permission: {
    type: Object,
    default: () => ({}),
  },
  // 如果该组件计算的table的maxHeight不符合预期，那么可以通过这个参数来调整
  maxHeightDecrement: {
    type: Number,
    default: 0,
  },
})
const emit = defineEmits([
  'addClick',
  'beforeSend',
  'afterSend',
  'triggerShowSearch',
  'editBtnClick',
])
const store = businessStore()
const proxy = inject('proxy')
const isLoading = ref(false)
const baseTabelRef = ref(null)
const searchDatas = ref({})
const paginationInfo = ref({})

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
    // 当pageSize发生变化时将pageName设置成第一页
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
  const list = store.pageListData(props.pageName)
  return list ? list : []
})
// 总数量
const listCount = computed(() => {
  return store[`listCount`](props.pageName)
})
const showPageSearch = computed(() => {
  return store.pageSearchControl[`${props.pageName}SearchShow`]
})

const addClick = () => {
  emit('addClick')
}

// 排序发生变化触发的函数
const sortChange = (shortData) => {
  let isAsc = ''
  let orderByColumn = 'createTime'
  if (shortData.order === 'ascending') {
    isAsc = 'asc'
  } else if (shortData.order === 'descending') {
    isAsc = 'desc'
  }
  if (shortData.prop) {
    orderByColumn = shortData.prop
  }
  let orderObj = {}
  if (isAsc && isAsc != '') {
    orderObj = {
      orderByColumn,
      isAsc,
    }
  } else {
    // 如果没有排序就使用默认的查询条件的排序属性
    for (const [key, value] of Object.entries(props.descConfig)) {
      searchDatas.value[key] = value
    }
  }
  searchDatas.value = Object.assign(
    {},
    { ...searchDatas.value },
    { ...orderObj }
  )
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
  // 计算公式为 视口高度-搜索栏高度-margin
  let viewportHeight = window.innerHeight - currentSearchHeight - 34
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

// 用于隐藏页面的pageSearch组件
const triggerShowSearch = () => {
  store.handlePageSearch(props.pageName)
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
  props.contentConfig.tableItem = tableItem
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
const init = () => {
  columnsFilter()
}

onMounted(() => {
  if (props.autoDesc) {
    for (const [key, value] of Object.entries(props.descConfig)) {
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
  dataList,
  editClick,
  deleteRow,
})
</script>

<template>
  <div class="page-content">
    <BaseTableV2
      ref="baseTabelRef"
      @sortChange="sortChange"
      v-model:paginationInfo="paginationInfo"
      :dataList="dataList"
      :listCount="listCount"
      :tableListener="tableListener"
      :maxHeight="maxHeight"
      :isLoading="isLoading"
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
          <slot name="handleLeft"></slot>
        </div>
      </template>
      <template
        v-if="hasSlot($slots, ['handleRight']) || headerButtons.length !== 0"
        #handleRight
      >
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
        <slot name="handleRight"></slot>
      </template>
    </BaseTableV2>
  </div>
</template>

<style scoped lang="scss">
.page-content {
  :deep(.el-table-v2__header) {
    border-top: var(--el-table-border);
    border-bottom: var(--el-table-border);
    .el-table-v2__header-cell {
      color: var(--el-text-color-primary);
    }
  }
  :deep(.el-table-v2__body) {
    .el-table-v2__cell-text {
      color: var(--el-table-text-color);
    }
  }
}
</style>
