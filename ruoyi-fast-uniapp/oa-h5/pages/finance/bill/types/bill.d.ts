interface BillListFilterContent {
  payType: number,
  inComeAndExpendType: string,
  searchText: string,
  dateRange: string,
  billCateIds: number[][]
}

interface BillFileInfo {
  id: number,
  real_name: string,
  src: string
}

interface BillDetails {
  id: number
  entid: number
  user_id: number
  uid: string
  cate_id: number
  num: string
  edit_time: string
  types: number
  type_id: number
  pay_type: string
  mark: string
  link_id: number
  order_id: number
  link_cate: string
  created_at: string
  updated_at: string
  files: BillFileInfo[]
  attachs: BillFileInfo[]
  cate: {
    id: number
    name: string
    path: Array<any>
  }
  client: {
    customer_name: string
    card: {
      name: string
      avatar: string
    }
  }
  contract: {
    title: string
  }
  client_bill: {
    bill_no: string
    mark: string
  }
  user: {
    name: string
    avatar: string
  }
}

interface BillStatisticCensus {
  income: number,
  expend: number,
  count: number,
  profit: number
}

interface BillStatisticRankItem {
  cate_id: number,
  name: string,
  ratio: number,
  sum: string
}