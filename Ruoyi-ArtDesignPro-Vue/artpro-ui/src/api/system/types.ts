export type Query = Record<string, unknown>
export type Entity = Record<string, any>

export interface PageResult<T = Entity> {
  rows: T[]
  total: number
}

export interface TreeNode {
  id: number
  label: string
  children?: TreeNode[]
}
