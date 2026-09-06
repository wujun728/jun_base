interface DialogConfig extends BaseFormProps {}

export interface DialogProps {
  pageName: string
  infoInit?: anyObj
  otherInfo?: anyObj
  width?: StrNum
  top?: string
  dialogConfig: DialogConfig
  defaultData?: anyObj
  idKey?: string
  sendIdKey?: string
  isEditMore?: boolean
  maxHeight?: StrNum
  search?: () => void
  requestBaseUrl?: string
  beforeSaveFun?: () => Promise<void>
}
export interface Emits {}
