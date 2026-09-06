import { getInfo } from '@/api/business/main/index'
import businessStore, { interceptor } from '@/store/business/businessStore'
import to from '@/utils/to'
import modal from '@/plugins/modal'
import { resolveRowId, resolveRowIds } from '../utils/resolveRowId'

export function useTableActions(
  props,
  emit,
  { isLoading, send, finalSearchData }
) {
  const store = businessStore()

  const deleteRow = async (delData) => {
    isLoading.value = true

    const id = Array.isArray(delData)
      ? resolveRowIds(delData, props)
      : resolveRowId(delData, props)

    if (id || id === 0) {
      const [res] = await to(
        store.deletDataAction({
          id,
          pageName: props.pageName,
          requestUrl: props.requestUrl,
          requestBaseUrl: props.requestBaseUrl,
          delUrl: props.delUrl,
        })
      )
      if (res) {
        send(finalSearchData.value)
      }
    } else {
      modal.msgWarning('未获取到有效Id')
    }
    isLoading.value = false
  }

  const editClick = async (item) => {
    isLoading.value = true
    const id = resolveRowId(item, props)

    if (id || id === 0) {
      const url = `${props.requestBaseUrl}${interceptor(props.pageName)}/${id}`
      const [res] = await to(getInfo(url))
      if (res?.data) {
        emit('editBtnClick', res.data, res)
      }
    } else {
      modal.msgWarning('未获取到有效Id')
    }

    isLoading.value = false
  }

  const addClick = () => emit('addClick')

  const editSelected = () => {
    if (props.tableSelected.length !== 1) return
    editClick(props.tableSelected[0])
  }

  return { deleteRow, editClick, editSelected, addClick }
}
