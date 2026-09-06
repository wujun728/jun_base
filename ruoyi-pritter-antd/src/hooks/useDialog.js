import { ref } from 'vue'

// 使用对话框
export default function useDialog(
  addCallBack,
  editCallback,
  title = '添加',
  editTitle = '编辑'
) {
  const dialogRef = ref()
  const infoInit = ref({})
  const addClick = () => {
    if (dialogRef.value) {
      dialogRef.value.setTitle(title)
      dialogRef.value.openModal()
      infoInit.value = {}
    }
    addCallBack && addCallBack()
  }
  const editBtnClick = (item, res) => {
    infoInit.value = { ...item }
    if (dialogRef.value) {
      dialogRef.value.setTitle(editTitle)
      dialogRef.value.openModal()
    }
    editCallback && editCallback(item, res)
  }
  return [dialogRef, infoInit, addClick, editBtnClick]
}
