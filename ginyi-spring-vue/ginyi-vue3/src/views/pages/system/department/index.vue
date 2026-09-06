<template>
    <div>
        <CommonTable :columns="columns"
                     :pagination="tablePagination"
                     :loading="tableLoading"
                     :dataList="tableDataList"
                     :labelField="'title'"
                     :actionWidth="220"
                     :rowKey="(row) => row.deptId"
                     :scrollX="1000"
                     :actionColData="actionCol"
                     @onPageChange="onPageChange"
                     @onPageSizeChange="onPageSizeChange"
                     @onEvent="onEvent">
            <template #query>
                <DeptQueryForm @onQuery="onQuery" @onReset="onReset"/>
            </template>
        </CommonTable>
        <DeptEditForm ref="deptEditFormRef"/>
    </div>
</template>

<script lang="ts">
import {defineComponent, onMounted, onUnmounted, ref, watch} from "vue";
import {useCommonTable} from "@/components/commonTable/useCommonTable";
import {deptController} from "@/api";
import {columns} from "@/views/pages/system/department/columns";
import CommonTable from "@/components/commonTable/index.vue";
import DeptQueryForm from "@/views/pages/system/department/deptQueryForm.vue";
import DeptEditForm from "@/views/pages/system/department/deptEditForm.vue";
import {eventBus} from "@/config/eventBus";
import {tableActionEnums} from "@/enums/tableActionEnums";

enum actionEnums {
    // 新增子项
    ADD_CHILD = "add_child",
}

export default defineComponent({
    components: {
        DeptQueryForm, CommonTable, DeptEditForm
    },
    setup() {
        const {
            tableDataList,
            tablePagination,
            tableLoading,
            onPageChange,
            onPageSizeChange,
            getDataList
        } = useCommonTable(deptController.list)
        const deptEditFormRef = ref(null)
        // 操作列
        const actionCol = ref<Array<any>>([
            {
                title: "新增子项",
                colorType: "success",
                actionType: actionEnums.ADD_CHILD,
            }
        ])

        // @ts-ignore
        watch(() => deptEditFormRef?.value?.modalLoading, () => getDataList(), {deep: true})

        const onEvent = (value: any) => {
            switch (value.type) {
                case tableActionEnums.ADD:
                    // @ts-ignore
                    deptEditFormRef?.value?.onAdd()
                    break
                case tableActionEnums.EDIT:
                    // @ts-ignore
                    deptEditFormRef?.value?.onEdit(value.row)
                    break
                case tableActionEnums.DELETE:
                    // @ts-ignore
                    deptEditFormRef?.value?.onDeleteById(value.row.deptId)
                    break
                case tableActionEnums.BATCH_DELETE:
                    // @ts-ignore
                    deptEditFormRef?.value?.onDeleteByIds(value.data)
                    break
                case actionEnums.ADD_CHILD:
                    // @ts-ignore
                    deptEditFormRef?.value?.addChildDept(value.row)
                    break
            }
        }

        const onQuery = (value: any) => {
            getDataList(value)
        }

        const onReset = (value: any) => {
            getDataList(value)
        }

        onMounted(() => {
            getDataList()
            eventBus.on("handleDeptStatusSwitchClick", (row: any) => {
                deptController.updateStatus({
                    deptId: row.deptId,
                    status: [0, "0"].includes(row.status) ? "1" : "0"
                }).then(res => {
                    window.$message.success(res.msg)
                    getDataList()
                })
            })
        })

        onUnmounted(() => {
            eventBus.off("handleDeptStatusSwitchClick",() => {})
        })

        return {
            columns,
            tableDataList,
            tablePagination,
            tableLoading,
            onPageChange,
            onPageSizeChange,
            onEvent,
            onQuery,
            onReset,
            deptEditFormRef,
            actionCol
        }
    }
})
</script>
