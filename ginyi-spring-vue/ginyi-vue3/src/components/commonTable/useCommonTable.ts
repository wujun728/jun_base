import {reactive, ref} from "vue";
import {PaginationInfo, PaginationSizeOption} from "naive-ui";

export const useCommonTable = (getListApi: Function) => {

    const tableTotal = ref<number>(0)
    const tableDataList = ref<Array<any>>([])
    const tableLoading = ref<boolean>(true)
    const tablePagination = reactive<PaginationInfo | any>({
        page: 1,
        pageSize: 10,
        pageSizes: [10, 30, 50, 100] as Array<number | PaginationSizeOption>,
        itemCount: 0,
        size: "small",
        showQuickJumper: true,
        showSizePicker: true,
        prefix(info: PaginationInfo) {
            return `总共 ${info.itemCount} 条`
        }
    })

    const onPageChange = (page: number) => {
        tablePagination.page = page
        getDataList()
    }
    const onPageSizeChange = (pageSize: number) => {
        tablePagination.pageSize = pageSize
        getDataList()

    }

    /**
     * 获取列表数据
     * @param data
     */
    const getDataList = (data: any = {}) => {
        tableLoading.value = true
        getListApi(data, {
            page: tablePagination.page,
            pageSize: tablePagination.pageSize
        }).then((res: any) => {
            tableDataList.value = res.data.list
            tablePagination.itemCount = res.data.count
            tableLoading.value = false
        }).catch((e: any) => {
            tableLoading.value = true
        })
    }

    return {
        tableTotal,
        tableDataList,
        tablePagination,
        tableLoading,
        onPageChange,
        onPageSizeChange,
        getDataList
    }
}