<template>
    <div>
        <CommonTable :columns="columns"
                     :pagination="tablePagination"
                     :loading="tableLoading"
                     :dataList="tableDataList"
                     :labelField="'title'"
                     :actionWidth="140"
                     :rowKey="(row) => row.roleId"
                     :scrollX="1000"
                     @onPageChange="onPageChange"
                     @onPageSizeChange="onPageSizeChange"
                     @onEvent="onEvent">>
            <template #query>
                <RoleQueryForm @onQuery="onQuery" @onReset="onReset"/>
            </template>
        </CommonTable>
        <RoleEditForm ref="roleEditFormRef"/>
    </div>
</template>

<script lang="ts">
import {defineComponent, onMounted, onUnmounted, ref, watch} from "vue";
import CommonTable from "@/components/commonTable/index.vue";
import {columns} from "@/views/pages/system/role/columns";
import {useCommonTable} from "@/components/commonTable/useCommonTable";
import {roleController} from "@/api";
import RoleQueryForm from "@/views/pages/system/role/roleQueryForm.vue";
import RoleEditForm from "@/views/pages/system/role/roleEditForm.vue";
import {eventBus} from "@/config/eventBus";
import {tableActionEnums} from "@/enums/tableActionEnums";

export default defineComponent({
    components: {
        CommonTable, RoleQueryForm, RoleEditForm
    },
    setup() {
        const {
            tableDataList,
            tablePagination,
            tableLoading,
            onPageChange,
            onPageSizeChange,
            getDataList
        } = useCommonTable(roleController.list)
        const roleEditFormRef = ref(null)

        // @ts-ignore
        watch(() => roleEditFormRef?.value?.modalLoading, () => getDataList(), {deep: true})


        const onEvent = (value: any) => {
            switch (value.type) {
                case tableActionEnums.ADD:
                    // @ts-ignore
                    roleEditFormRef?.value?.onAdd()
                    break
                case tableActionEnums.EDIT:
                    // @ts-ignore
                    roleEditFormRef?.value?.onEdit(value.row, value.row.roleId)
                    break
                case tableActionEnums.DELETE:
                    // @ts-ignore
                    roleEditFormRef?.value?.onDeleteById(value.row.roleId)
                    break
                case tableActionEnums.BATCH_DELETE:
                    // @ts-ignore
                    roleEditFormRef?.value?.onDeleteByIds(value.data)
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
            eventBus.on("handleRoleStatusSwitchClick", (row: any) => {
                roleController.updateStatus({
                    roleId: row.roleId,
                    status: [0, "0"].includes(row.status) ? "1" : "0"
                }).then(res => {
                    window.$message.success(res.msg)
                    getDataList()
                })
            })
        })

        onUnmounted(() => {
            eventBus.off("handleRoleStatusSwitchClick",() => {})
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
            roleEditFormRef
        }
    }
})

</script>
