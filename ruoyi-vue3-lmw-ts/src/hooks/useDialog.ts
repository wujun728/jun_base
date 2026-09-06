import { ref } from 'vue'

// 使用对话框
export default function useDialog<T = any>(
  addCallBack?: () => void,
  editCallback?: (item: T, type: any, res: any) => void,
  addTitle = '添加',
  editTitle = '编辑'
) {
  const dialogRef = ref()
  const infoInit = ref<any>({})
  const addClick = () => {
    if (dialogRef.value) {
      dialogRef.value.title = addTitle
      dialogRef.value.dialogVisible = true
      infoInit.value = {}
    }
    addCallBack && addCallBack()
  }
  const editBtnClick = (item: T, type: any, res: { data: T }) => {
    infoInit.value = { ...item }
    if (dialogRef.value) {
      dialogRef.value.title = editTitle
      dialogRef.value.dialogVisible = true
    }
    editCallback && editCallback(item, type, res)
  }
  return { dialogRef, infoInit, addClick, editBtnClick }
}
