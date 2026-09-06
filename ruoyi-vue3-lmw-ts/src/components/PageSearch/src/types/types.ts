interface SearchConfig extends BaseFormProps {}

export interface SearchProps {
  searchConfig: SearchConfig
  pageName: string
  initSearch?: anyObj
  otherRequestOption?: anyObj
  showSearch?: boolean
  reset?: () => void
  cacheKey?: string
}
export interface Emits {}
