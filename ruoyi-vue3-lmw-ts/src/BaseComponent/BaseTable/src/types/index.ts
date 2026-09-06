import { ElTableColumn, TableProps } from 'element-plus'

export type ElTableColumnProps = Partial<
  InstanceType<typeof ElTableColumn>['$props']
>
export interface TableItem extends ElTableColumnProps {
  hide?: boolean
  slotName?: string
  merges?: TableItem[]
  slotNames?: 'default' | 'header' | 'filterIcon'
  permission?: string
  isDict?: boolean
  prop: string
}
export interface BaseTableProps<T extends anyObj = anyObj> {
  border?: boolean
  dataList?: any[]
  tableItem: TableItem[]
  tableListener?: anyObj
  showChoose?: boolean
  showIndex?: boolean
  pagination?: boolean
  listCount?: number
  paginationInfo?: {
    pageNum: number
    pageSize: number
  }
  elTableConfig?: Partial<TableProps<T>>
  showExpand?: boolean
  align?: 'left' | 'center' | 'right'
  paginationLayout?: string
  hideItems?: Ref<string[]> | string[]
  maxHeight?: number
  selectionConfig?: Partial<InstanceType<typeof ElTableColumn>['$props']>
}
export interface TableColumnProps {
  item: TableItem
  align?: 'left' | 'center' | 'right'
  hideItems?: Ref<string[]> | string[]
}
