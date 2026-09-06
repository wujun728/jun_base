import {computed, ref} from "vue";

enum actionEnum {
    ADD = "新增",
    EDIT = "编辑",
}

export const useCommonModal = (name: string,
                               formDefaultValue: any = {},
                               addApi: Function,
                               editApi: Function,
                               deleteApi?: Function,
                               batchDeleteApi?: Function,
                               getDetailsById?: Function) => {

    const modalShow = ref<boolean>(false)
    const modalLoading = ref<boolean>(false)
    const modalTitle = ref<string | undefined>(undefined)
    const modalFormRef = ref(undefined)
    const modalForm = ref<any>({...formDefaultValue})

    /**
     * 新增
     */
    const onAdd = () => {
        modalShow.value = true
        modalLoading.value = false
        modalTitle.value = actionEnum.ADD
        modalForm.value = {...formDefaultValue}
    }

    /**
     * 编辑
     * @param row
     * @param id
     */
    const onEdit = (row: any, id: number | string) => {
        modalShow.value = true
        modalLoading.value = false
        modalTitle.value = actionEnum.EDIT
        if(getDetailsById && id){
            getDetailsById(id).then((res: any) => {
                modalForm.value = {...res.data}
            })
        }else {
            modalForm.value = {...row}
        }
    }


    /**
     * 保存
     * 返回 Promise，解决函数执行完后需要续写逻辑的需求
     */
    const onSubmit = (): Promise<any> => {
        return new Promise((resolve, reject) => {
            // @ts-ignore
            modalFormRef.value?.validate(err => {
                if (!err) {
                    if (modalTitle.value === actionEnum.ADD) {
                        addApi(modalForm.value).then((res: any) => {
                            window.$message.success(res.msg)
                            modalLoading.value = true
                            resolve(null)
                        })
                    }
                    if (modalTitle.value === actionEnum.EDIT) {
                        editApi(modalForm.value).then((res: any) => {
                            window.$message.success(res.msg)
                            modalLoading.value = true
                            resolve(null)
                        })
                    }
                } else {
                    modalShow.value = true
                }
            })
        })
    }

    /**
     * 删除
     * 返回 Promise，解决函数执行完后需要续写逻辑的需求
     */
    const onDeleteById = (id: number | string) => {
        return new Promise((resolve, reject) => {
            modalLoading.value = false
            window.$dialog.error({
                title: "温馨提醒",
                content: "删除操作不可逆，是否继续？",
                positiveText: "确定",
                negativeText: "取消",
                onPositiveClick: () => {
                    deleteApi && deleteApi(id).then((res: any) => {
                        window.$message.success(res.msg)
                        modalLoading.value = true
                        resolve(null)
                    })
                }
            })
        })
    }

    /**
     * 批量删除
     * 返回 Promise，解决函数执行完后需要续写逻辑的需求
     */
    const onDeleteByIds = (ids: Array<number | string>) => {
        return new Promise((resolve, reject) => {
            modalLoading.value = false
            window.$dialog.error({
                title: "温馨提醒",
                content: "删除操作不可逆，是否继续？",
                positiveText: "确定",
                negativeText: "取消",
                onPositiveClick: () => {
                    batchDeleteApi && batchDeleteApi(ids).then((res: any) => {
                        window.$message.success(res.msg)
                        modalLoading.value = true
                        resolve(null)
                    })
                }
            })
        })
    }

    return {
        onAdd,
        onEdit,
        onSubmit,
        onDeleteById,
        onDeleteByIds,
        modalForm,
        modalFormRef,
        modalShow,
        modalLoading,
        modalTitle: computed(() => {
            return modalTitle.value + name
        }),
    }
}
