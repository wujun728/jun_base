import emitter from '@/utils/bus'

export function usePageListeners(
  props,
  { send, searchDatas, paginationInfo, finalSearchData }
) {
  const maxTableHeight = ref(500)
  let currentSearchHeight = 0
  let isListening = false

  const updateTableHeight = (searchHeight) => {
    if (typeof searchHeight === 'number') {
      currentSearchHeight = searchHeight
    }

    const header = document.querySelector('.layout-header')
    let height = window.innerHeight - currentSearchHeight - 34

    if (header) {
      height -= header.clientHeight
    }

    maxTableHeight.value = height - props.maxHeightDecrement
  }

  const handleSearch = (searchFormData) => {
    Object.assign(searchDatas.value, searchFormData)
    delete searchDatas.value.searchLoading
    delete searchDatas.value.resetPaginationInfo

    if (searchFormData.resetPaginationInfo) {
      paginationInfo.value.pageNum = 1
    }

    send(finalSearchData.value).finally(() => {
      searchFormData.searchLoading &&
        (searchFormData.searchLoading.value = false)
    })
  }

  const startListening = () => {
    if (isListening) return

    isListening = true
    emitter.on(`search${props.pageName}Info`, handleSearch)
    emitter.on(`change${props.pageName}Size`, updateTableHeight)
    window.addEventListener('resize', updateTableHeight)
  }

  const stopListening = () => {
    emitter.off(`search${props.pageName}Info`)
    emitter.off(`change${props.pageName}Size`)
    window.removeEventListener('resize', updateTableHeight)
    isListening = false
  }

  return {
    maxTableHeight,
    updateTableHeight,
    startListening,
    stopListening,
  }
}
