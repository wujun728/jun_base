const isRangeDateItem = (item) => {
  const type = item.type?.toLowerCase()
  return (
    item.isRange ||
    item.config?.range ||
    type === 'daterange' ||
    (item.config?.picker && String(item.config.picker).includes('range'))
  )
}

export default (item, colLayout) => {
  let layout = {}
  const type = item.type?.toLowerCase()
  if (type === 'datepicker' || type === 'daterange') {
    if (isRangeDateItem(item)) {
      layout = {
        xxl: 6,
        xl: 8,
        lg: 8,
        md: 10,
        sm: 12,
        xs: 24,
      }
      return item.layout || layout || colLayout
    }
  }
  layout = {
    xxl: 4,
    xl: 6,
    lg: 6,
    md: 8,
    sm: 12,
    xs: 24,
  }
  return item.layout || colLayout || layout
}
