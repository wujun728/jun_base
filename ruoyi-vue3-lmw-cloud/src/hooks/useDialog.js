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
      dialogRef.value.title = title
      dialogRef.value.dialogVisible = true
      infoInit.value = {}
    }
    addCallBack && addCallBack()
  }
  const editBtnClick = (item, type = false, res) => {
    infoInit.value = { ...item }
    if (dialogRef.value) {
      dialogRef.value.title = editTitle
      dialogRef.value.dialogVisible = true
    }
    editCallback && editCallback(item, type, res)
  }
  return [dialogRef, infoInit, addClick, editBtnClick]
}
