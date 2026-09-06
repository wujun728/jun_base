import type { ColProps } from 'element-plus'
import type { FormItem } from '../types/index'

const DEFAULT_COL_LAYOUT: Partial<ColProps> = {
  xl: 4,
  lg: 6,
  md: 8,
  sm: 12,
  xs: 24,
}

const RANGE_COL_LAYOUT: Partial<ColProps> = {
  xl: 5,
  lg: 8,
  md: 10,
  sm: 12,
  xs: 24,
}

const isDateRange = (item: FormItem): boolean => {
  if (item.type !== 'datepicker') return false
  if (!item.config || typeof item.config !== 'object') return false
  const dateType = (item.config as { type?: unknown }).type
  return typeof dateType === 'string' && dateType.includes('range')
}

export default function resolveLayout(
  item: FormItem,
  colLayout?: Partial<ColProps>
): Partial<ColProps> {
  if (item.layout) return item.layout
  if (colLayout) return colLayout
  return isDateRange(item) ? RANGE_COL_LAYOUT : DEFAULT_COL_LAYOUT
}
