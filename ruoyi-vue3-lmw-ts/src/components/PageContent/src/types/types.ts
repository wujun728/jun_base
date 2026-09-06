type ButtonsType =
  | 'refresh'
  | 'add'
  | 'edit'
  | 'delete'
  | 'columnDisplay'
  | 'comSearch'
interface ContentConfig extends BaseTableProps {
  defaultPageSize?: number
}
export interface PageContentProps {
  contentConfig: ContentConfig
  pageName: string
  tableListener?: anyObj
  firstSendOption?: anyObj
  autoSend?: boolean
  autoDesc?: boolean
  descConfig?: OrderType
  otherRequestOption?: anyObj
  piniaConfig?: {
    listConfig: {
      listKey: string
      countKey: string
    }
    handleList: (list: any[]) => any[]
  }
  idKey?: string
  requestBaseUrl?: string
  requestUrl?: string
  showEdit?: boolean
  showDelete?: boolean
  dictMap?: anyObj
  headerButtons?: ButtonsType[]
  tableSelected?: any[]
  permission?: anyObj
  handleEditShow?: (_row: any) => boolean
  handleDeleteShow?: (_row: any) => boolean
  delUrl?: string
  maxHeightDecrement?: number
  tableHideItems?: string[]
  cacheKey?: string
}
export type Emits = {
  addClick: []
  editBtnClick: [data: any, type: any, res: any]
  beforeSend: [any]
  afterSend: [any]
  onChangeShowColumn: [string[]]
  editMoreClick: []
}
