import { Ref, ref } from "vue"
import { Page, RequestPageType } from "@/annotation/Api"
import { ApiService } from '@/annotation/Api'
export function usePageTable<T>(target: new (...args: any[]) => T) {
    /** 当前记录数：用于记录查询句柄最后一次调用返回的总记录数 */
    const total = ref<number>(0)

    /** 加载状态：用于记录查询句柄当前是否处于加载中的状态 */
    const loading = ref<boolean>(false)

    /** 列表：用于存放查询句柄返回的列表 */
    const list = ref(new Array<T>()) as Ref<Array<T>>

    /** 查询参数：供查询句柄用查询列表服务 */
    const queryParams = ref(Page(target)) as Ref<RequestPageType<T>>

    /** 服务:用于调用增删改查服务 */
    const service = ApiService(target)

    /**
     * 查询句柄：根据queryParams查询参数调用列表查询服务
     * @returns 列表查询服务的响应
     */
    async function handleQuery() {
        loading.value = true
        const res = await service.list(queryParams.value)
        list.value = res.rows
        total.value = res.total
        loading.value = false
        return res
    }

    /**
     * 重置queryParams查询参数，重置后自动调用查询句柄
     * @returns 查询句柄的响应
     */
    function resetQuery() {
        queryParams.value = Page(target)
        return handleQuery()
    }

    return {
        queryParams,
        handleQuery,
        resetQuery,
        list,
        total,
        loading,
        service
    }

}

export function usePage<T>(target: new (...args: any[]) => T) {
    const {
        queryParams,
        handleQuery,
        resetQuery,
        list,
        total,
        loading,
        service
    } = usePageTable(target)

    /** form表单：用于新增和修改 */
    const form = ref(new target()) as Ref<T>

    /**
     * 重置form表单：重新调用构造函数赋值
     */
    function resetForm() {
        form.value = new target()
    }

    /**
     * 更新form表单：查询并填充form表单
     */
    async function updateForm(id: string | number) {
        const response = await service.get(id);
        form.value = response.data;
    }

    /**
     * 更新句柄：根据form调用更新服务，更新后自动调用查询句柄
     * @returns 更新服务的响应
     */
    async function handleUpdate() {
        const res = await service.update(form.value)
        resetForm()
        handleQuery()
        return res
    }

    /**
     * 新增句柄：根据form调用新增服务，新增后自动调用查询句柄
     * @returns 新增服务的响应
     */
    async function handleAdd() {
        const res = await service.add(form.value)
        resetForm()
        handleQuery()
        return res
    }

    /**
     * 删除句柄：根据id调用删除服务，删除后重新查询
     * @param id 
     * @returns 删除接口的相应
     */
    async function handleDelete(id: string | number | Array<string | number>) {
        const res = await service.del(id)
        handleQuery()
        return res
    }

    /**
     * 导出按钮操作
     */
    async function handleExport() {
        await service.export(queryParams.value)
    }

    return {
        queryParams,
        handleQuery,
        handleUpdate,
        handleAdd,
        resetForm,
        updateForm,
        resetQuery,
        handleDelete,
        handleExport,
        list,
        total,
        loading,
        form,
        service
    }

}
