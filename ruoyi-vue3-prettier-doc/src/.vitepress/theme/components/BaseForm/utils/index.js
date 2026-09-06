export const getOptions = (item) => {
  return item.options?.value ?? item.options ?? []
}
export const capitalizeFirstLetter = (str) => {
  return str.charAt(0).toUpperCase() + str.slice(1)
}
