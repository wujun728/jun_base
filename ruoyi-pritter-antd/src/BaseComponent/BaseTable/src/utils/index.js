export const capitalizeFirstLetter = (str) => {
  return str.charAt(0).toUpperCase() + str.slice(1)
}

const COLUMN_META_KEYS = new Set([
  'prop',
  'label',
  'slotName',
  'merges',
  'hide',
  'permission',
  'isDict',
  'mobileSlot',
  'isHidden',
  'slotNames',
  'minWidth',
  'width',
])

const normalizeColumnWidth = (item) => {
  const raw = item.width ?? item.minWidth
  if (raw === undefined || raw === null || raw === false) {
    return undefined
  }
  const width = Number(raw)
  return Number.isFinite(width) ? width : undefined
}

export const getColumnKey = (column) => column.key ?? column.dataIndex

export const applyColumnWidths = (columns, widthMap = {}) => {
  return columns.map((column) => {
    if (column.children?.length) {
      return {
        ...column,
        children: applyColumnWidths(column.children, widthMap),
      }
    }

    const key = getColumnKey(column)
    const width = key ? (widthMap[key] ?? column.width) : column.width

    return {
      ...column,
      ...(width !== undefined ? { width } : {}),
      resizable: width !== undefined,
    }
  })
}

export const isHiddenItem = (item, hideItems) => {
  if (item.hide || item.isHidden) return true
  if (isRef(hideItems)) {
    return Array.isArray(hideItems.value) && hideItems.value.includes(item.prop)
  }
  if (Array.isArray(hideItems)) {
    return hideItems.includes(item.prop)
  }
  return false
}

export const getColumnBind = (item, align) => {
  const bind = {
    key: item.prop,
    align: item.align || align,
  }

  for (const [key, value] of Object.entries(item)) {
    // if (COLUMN_META_KEYS.has(key) || value === undefined || value === false) {
    //   continue
    // }
    bind[key] = value
  }

  return bind
}

const buildColumn = (item, align) => {
  const bind = getColumnBind(item, align)
  const width = normalizeColumnWidth(item)

  if (width !== undefined) {
    bind.width = width
  } else {
    delete bind.width
  }

  return {
    title: item.label,
    dataIndex: item.prop,
    slotName: item.slotName,
    columnItem: item,
    resizable: width !== undefined,
    ellipsis: item.ellipsis !== false,
    ...bind,
  }
}

export const buildTableColumns = (tableItem, hideItems, align, showIndex) => {
  const columns = []

  if (showIndex) {
    columns.push({
      title: '序号',
      key: '__index',
      width: 60,
      align,
    })
  }

  for (const item of tableItem) {
    if (isHiddenItem(item, hideItems) || item.hide) continue

    if (item.merges?.length) {
      columns.push({
        title: item.label,
        key: `${item.prop}-group`,
        slotName: item.slotName,
        columnItem: item,
        children: item.merges
          .filter((merge) => !isHiddenItem(merge, hideItems) && !merge.hide)
          .map((merge) => buildColumn(merge, align)),
      })
      continue
    }

    columns.push(buildColumn(item, align))
  }

  return columns
}

export const getTableScrollX = (tableItem, showIndex, widthMap = {}) => {
  let total = showIndex ? (widthMap.__index ?? 55) : 0
  let hasExplicitWidth = showIndex

  for (const item of tableItem) {
    const width = widthMap[item.prop] ?? normalizeColumnWidth(item)
    if (width !== undefined) {
      total += width
      hasExplicitWidth = true
    }
  }

  if (!hasExplicitWidth) {
    return undefined
  }

  return total || 'max-content'
}

export const hasSlot = (slots, names) => {
  return names.some((key) => Object.prototype.hasOwnProperty.call(slots, key))
}

export const collectExpandableKeys = (rows, rowKey = 'id') => {
  const keys = []
  const walk = (list) => {
    list.forEach((row) => {
      if (row.children?.length) {
        keys.push(row[rowKey])
        walk(row.children)
      }
    })
  }
  walk(rows)
  return keys
}
