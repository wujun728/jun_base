import useStorage from '@/utils/useStorage'
import hasPermi from '@/utils/hasPermi'
import tab from '@/plugins/tab'

const HIDDEN_COLUMNS_KEY = 'hidenColumns'
const ORDER_COLUMNS_KEY = 'orderColumns'

function readStorage(key) {
  return useStorage.get(key)
}

function writeStorage(key, value) {
  useStorage.set(key, value)
}

function patchPageStorage(key, pageName, pageValue) {
  const storage = readStorage(key) || {}
  storage[pageName] = pageValue
  writeStorage(key, storage)
}

function removePageStorage(key, pageName) {
  const storage = readStorage(key)
  if (!storage?.[pageName]) return false
  delete storage[pageName]
  writeStorage(key, storage)
  return true
}

export function useColumnDisplay(props, emit, store) {
  const columnDisplayOpen = ref(false)
  const columnDisplayList = ref([])
  const columnDisplayKey = ref(0)
  const columnChecked = ref([])
  const filterArr = ref([])

  const propsTableHideItems = computed(() => {
    let hideItems = props.contentConfig.hideItems || []
    if (isRef(hideItems)) hideItems = hideItems.value
    return [...new Set([...props.tableHideItems, ...hideItems])]
  })

  let userHideItems = [...propsTableHideItems.value]

  const persistHiddenColumns = () => {
    patchPageStorage(HIDDEN_COLUMNS_KEY, props.pageName, userHideItems)
  }

  const onChangeShowColumn = (checked, prop, handleUser) => {
    const isBatch = Array.isArray(prop)

    if (!isBatch) {
      filterArr.value = checked
        ? filterArr.value.filter((item) => item !== prop)
        : [...new Set([...filterArr.value, prop])]
    }

    if (handleUser) {
      if (checked) {
        userHideItems = userHideItems.filter((item) => item !== prop)
      } else if (!isBatch) {
        userHideItems = [...new Set([...userHideItems, prop])]
      }
      persistHiddenColumns()
    }

    emit('onChangeShowColumn', filterArr.value)
  }

  const initColumnVisibility = () => {
    filterArr.value = []
    columnChecked.value = []

    const storedHidden = readStorage(HIDDEN_COLUMNS_KEY)
    let tableHides = []

    if (storedHidden?.[props.pageName]) {
      userHideItems = storedHidden[props.pageName]
      tableHides = userHideItems
      onChangeShowColumn(false, tableHides, false)
    } else {
      tableHides = [...new Set([...userHideItems, ...propsTableHideItems.value])]
    }

    props.contentConfig.tableItem?.forEach((item) => {
      if (!item.prop) return

      if (tableHides.includes(item.prop)) {
        filterArr.value.push(item.prop)
        onChangeShowColumn(false, item.prop, false)
      } else {
        columnChecked.value.push(item.prop)
      }
    })
  }

  watch(() => props.contentConfig, initColumnVisibility, { immediate: true })

  watch(columnDisplayOpen, (open) => {
    if (!open) return
    columnDisplayList.value = [...(props.contentConfig.tableItem || [])]
    columnDisplayKey.value += 1
  })

  const applyStoredColumnOrder = () => {
    const tableItem = props.contentConfig.tableItem.filter((item) => {
      if (!item.permission) return true
      return hasPermi(item.permission)
    })

    const orderColumns = readStorage(ORDER_COLUMNS_KEY)
    const pageColumns = orderColumns?.[props.pageName]

    if (!pageColumns) {
      props.contentConfig.tableItem = tableItem
      return
    }

    const itemMap = new Map(tableItem.map((item) => [item.prop, item]))
    const sortedItems = pageColumns.map((key) => itemMap.get(key)).filter(Boolean)
    const remainingItems = tableItem.filter(
      (item) => !pageColumns.includes(item.prop)
    )

    props.contentConfig.tableItem = [...sortedItems, ...remainingItems]
  }

  const refreshTableList = () => {
    const listName = `${props.pageName}List`
    const list = store.listInfo[listName]
    store.$patch((state) => {
      state.listInfo[listName] = [...list]
    })
  }

  const dragUpdate = () => {
    props.contentConfig.tableItem = [...columnDisplayList.value]
    refreshTableList()

    const order = columnDisplayList.value.map((item) => item.prop)
    patchPageStorage(ORDER_COLUMNS_KEY, props.pageName, order)

    nextTick(() => {
      columnDisplayKey.value += 1
    })
  }

  const handleDefaultSort = () => {
    if (removePageStorage(ORDER_COLUMNS_KEY, props.pageName)) {
      tab.refreshPage()
    }
  }

  return {
    columnDisplayOpen,
    columnDisplayList,
    columnDisplayKey,
    columnChecked,
    onChangeShowColumn,
    dragUpdate,
    handleDefaultSort,
    applyStoredColumnOrder,
  }
}
