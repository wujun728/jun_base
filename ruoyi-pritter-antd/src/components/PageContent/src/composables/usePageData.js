import businessStore from '@/store/business/businessStore'

function debounce(fn, wait) {
  let timer
  const debounced = (...args) => {
    clearTimeout(timer)
    timer = setTimeout(() => fn(...args), wait)
  }
  debounced.cancel = () => clearTimeout(timer)
  return debounced
}

export function usePageData(props, emit) {
  const store = businessStore()
  const isLoading = ref(false)
  const searchDatas = ref({})

  const paginationInfo = ref({
    pageNum: 1,
    pageSize: props.contentConfig?.defaultPageSize || 50,
  })

  const finalSearchData = computed(() => {
    const base = { ...searchDatas.value }
    return props.contentConfig.pagination
      ? { ...base, ...paginationInfo.value }
      : base
  })

  const tableBindProps = computed(() => props.contentConfig || {})

  const dataList = computed(
    () => store.pageListData(props.pageName, props.cacheKey) || []
  )

  const listCount = computed(
    () => store.listCount(props.pageName, props.cacheKey) ?? 0
  )

  const showPageSearch = computed(
    () => store.pageSearchControl[`${props.pageName}${props.cacheKey}SearchShow`]
  )

  const send = async (searchInfo) => {
    isLoading.value = true
    emit('beforeSend', searchInfo)

    const isSuccess = await store.getList(
      {
        pageName: props.pageName,
        requestUrl: props.requestUrl,
        queryInfo: { ...props.otherRequestOption, ...searchInfo },
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

  const debounceSend = debounce(send, 66)

  watch(
    () => paginationInfo.value,
    (next, prev) => {
      if (next.pageSize !== prev.pageSize) {
        paginationInfo.value.pageNum = 1
      }
      debounceSend(finalSearchData.value)
    }
  )

  const refresh = () => send(finalSearchData.value)

  const sortChange = (sorter) => {
    const sortItem = Array.isArray(sorter) ? sorter[0] : sorter
    const { field, order } = sortItem || {}

    let orderObj = { orderByColumn: undefined, isAsc: undefined }

    if (order === 'ascend') {
      orderObj = { orderByColumn: field, isAsc: 'asc' }
    } else if (order === 'descend') {
      orderObj = { orderByColumn: field, isAsc: 'desc' }
    } else if (props.descConfig && props.autoDesc) {
      orderObj = { ...props.descConfig }
    }

    searchDatas.value = { ...searchDatas.value, ...orderObj }
    send(finalSearchData.value)
  }

  const applyDefaultSort = () => {
    if (!props.autoDesc) return

    const tableConfig = props.contentConfig?.tableConfig
    let sortData = {}

    if (tableConfig?.defaultSort) {
      const { field, order } = tableConfig.defaultSort
      sortData.orderByColumn = field
      if (order === 'ascend') sortData.isAsc = 'asc'
      else if (order === 'descend') sortData.isAsc = 'desc'
    } else if (props.descConfig) {
      sortData = { ...props.descConfig }
    }

    Object.assign(searchDatas.value, sortData)
  }

  const triggerShowSearch = () => {
    store.handlePageSearch(props.pageName, props.cacheKey)
  }

  return {
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
  }
}
