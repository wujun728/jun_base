<script setup>
import emitter from '@/utils/hsj/bus'
import businessStore from '@/store/business/businessStore'
import { getInfo } from '@/api/business/main/index'
import to from '@/utils/to'
import { interceptor } from '@/store/business/businessStore'
import { antiShake } from '@/utils/hsj/utils'
import DictCpn from './dictCpn.vue'
import { MoblieTable } from '@/BaseComponent/BaseTable/index'
import { collectObjectsWithSlotName, hasSlot } from './utils'
import InfoDialog from './InfoDialog.vue'
import { useSlots } from 'vue'

const props = defineProps({
  // table的配置
  contentConfig: {
    type: Object,
    default: () => {},
  },
  // table的事件监听
  tableListener: {
    type: Object,
    default: () => ({}),
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
    default: '',
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
    default: () => ['add', 'delete'],
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
  getInfoUrl: {
    type: String,
  },
})
const emit = defineEmits([
  'addClick',
  'editBtnClick',
  'beforeSend',
  'afterSend',
  'onChangeShowColumn',
  'selectionChange',
  'editMoreClick',
])
const store = businessStore()
const proxy = inject('proxy')
const isLoading = ref(false)
const baseTableRef = useTemplateRef('baseTableRef')
const searchDatas = ref({})
const paginationInfo = ref({
  pageNum: 1,
  pageSize: props.contentConfig?.defaultPageSize || 10,
})

if (props.contentConfig?.pagination) {
  paginationInfo.value = {
    pageNum: 1,
    pageSize: props.contentConfig?.defaultPageSize || 10,
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
  },
  {
    deep: true,
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
    emit('afterSend', store.pageListData(props.pageName, props.cacheKey))
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
// 删除按钮
const deleteRow = async (delData) => {
  isLoading.value = true
  let id = ''
  if (Array.isArray(delData)) {
    const ids = delData.map((item) => {
      return getId(item)
    })
    id = ids.toString()
  } else {
    id = getId(delData)
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
const getId = (row) => {
  if (!row) {
    return
  }
  return row[props.idKey] ?? row[props.pageName + 'Id'] ?? row.id
}
const getRowInfo = async (row) => {
  let id = getId(row)
  if (id || id === 0) {
    let url = ``
    if (props.getInfoUrl) {
      url = `${props.getInfoUrl}/${id}`
    } else {
      url = `${props.requestBaseUrl}${interceptor(props.pageName)}/${id}`
    }
    // 拼接getInfo请求的url地址
    let [res] = await to(getInfo(url))
    return res
  } else {
    proxy.$modal.notifyWarning('未获取到有效Id')
  }
}
// 编辑按钮
const editClick = async (item, type) => {
  isLoading.value = true
  // 取出当前点击这一行数据的id 优先props传入的idKey
  const res = await getRowInfo(item)
  if (res?.data) {
    emit('editBtnClick', res.data, type, res)
  }
  isLoading.value = false
}

const addClick = () => {
  emit('addClick')
}
// 其他的插槽
const exceptSlot = ['todo']
// 所有插槽名称
const otherSlot = ref([])
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
const visibilityHeight = ref(100)
const mittResize = (searchHeight) => {
  if (typeof searchHeight === 'number') {
    visibilityHeight.value = searchHeight
  }
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

const infoDialog = ref(false)
const infoCurrent = ref({})
const cardHeaderClick = (row) => {
  infoCurrent.value = row
  infoDialog.value = true
}

const columnsFilter = () => {
  // 列的权限判断
  const tableItem = props.contentConfig.tableItem.filter((item) => {
    if (!item.permission) return true
    return proxy.hasPermi(item.permission)
  })
  props.contentConfig.tableItem = tableItem
}
const init = () => {
  columnsFilter()
}
const showHeader = () => {
  const slots = useSlots()
  const btns = props.headerButtons.some(
    (item) => item === 'add' || item === 'delete'
  )
  return hasSlot(slots, ['handleLeft']) || btns
}
watch(
  () => props.contentConfig,
  () => {
    otherSlot.value = collectObjectsWithSlotName(
      props.contentConfig?.tableItem,
      exceptSlot
    )
    init()
  },
  {
    immediate: true,
  }
)
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
defineExpose({
  finalSearchData,
  refresh,
  baseTableRef,
  deleteRow,
  editClick,
  dataList,
  mittResize,
})
</script>

<template>
  <div class="page-content page pt12" v-loading="isLoading">
    <MoblieTable
      ref="baseTableRef"
      v-bind="contentConfig"
      :visibilityHeight="visibilityHeight"
      :dataList="dataList"
      :listCount="listCount"
      :paginationInfo="paginationInfo"
      @cardHeaderClick="cardHeaderClick"
      v-on="tableListener"
    >
      <template v-if="showHeader()" #handleLeft>
        <div class="flex headerBtn">
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
      <template #todo="{ backData }">
        <div class="todo">
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
        #[item.slotName]="{ backData }"
      >
        <template v-if="item.slotName">
          <slot :name="item.slotName" :backData="backData"></slot>
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
    </MoblieTable>
    <InfoDialog
      v-model="infoDialog"
      :row="infoCurrent"
      :config="contentConfig.tableItem"
      :dictMap="dictMap"
    >
      <template
        v-for="item in otherSlot"
        :key="item.prop"
        #[item.slotName]="{ backData }"
      >
        <template v-if="item.slotName">
          <slot :name="item.slotName" :backData="backData"></slot>
        </template>
      </template>
    </InfoDialog>
  </div>
</template>

<style lang="scss" scoped>
.page-content {
  background-color: var(--ba-bg-color);
}
.todo {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-end;
  :deep(.el-button) {
    margin: 4px;
  }
}
</style>
