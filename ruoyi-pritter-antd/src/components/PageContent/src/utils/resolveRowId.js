export function resolveRowId(row, { idKey, pageName }) {
  if (idKey) {
    return row[idKey]
  }
  return row[`${pageName}Id`] ?? row.id
}

export function resolveRowIds(rows, options) {
  return rows.map((row) => resolveRowId(row, options)).join(',')
}
