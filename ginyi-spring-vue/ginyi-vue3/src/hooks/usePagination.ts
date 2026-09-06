import {reactive} from "vue";
import {PaginationInfo, PaginationSizeOption} from "naive-ui";

export const usePagination = () => {
    const pagination = reactive<PaginationInfo | any>({
        page: 1,
        pageSize: 5,
        pageSizes: [5, 10, 20, 50] as Array<number | PaginationSizeOption>,
        itemCount: 0,
        size: "small",
        showQuickJumper: true,
        showSizePicker: true,
        prefix(info: PaginationInfo) {
            return `总共 ${info.itemCount} 条`
        }
    })

    const onPageChange = (page: number) => {
        pagination.page = page
    }
    const onPageSizeChange = (pageSize: number) => {
        pagination.pageSize = pageSize
    }

    return {
        pagination
    }
}

