export function buildTree<T extends Record<string, any>>(
  rows: T[],
  idKey: string,
  parentKey: string
) {
  const map = new Map<number | string, T>()
  const roots: T[] = []
  rows.forEach((row) => map.set(row[idKey], { ...row, children: [] }))
  map.forEach((row: T) => {
    const parent = map.get(row[parentKey])
    if (parent) parent.children.push(row)
    else roots.push(row)
  })
  return roots as T[]
}

export function flattenTree<T extends Record<string, any>>(rows: T[]): T[] {
  return rows.flatMap((row) => [row, ...flattenTree((row.children || []) as T[])])
}
