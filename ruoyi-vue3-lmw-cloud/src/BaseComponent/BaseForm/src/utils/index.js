export const getOptions = (item) => {
  return item.options?.value ?? item.options ?? []
}
