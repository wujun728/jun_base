<template>
    <div>
        <CommonTable :columns="columns"
                     :pagination="tablePagination"
                     :loading="tableLoading"
                     :dataList="tableDataList"
                     :labelField="'title'"
                     :actionWidth="220"
                     :rowKey="(row) => row.menuId"
                     :scrollX="2000"
                     :actionColData="actionCol"
                     @onPageChange="onPageChange"
                     @onPageSizeChange="onPageSizeChange"
                     @onEvent="onEvent">
            <template #query>
                <MenuQueryForm @onQuery="onQuery" @onReset="onReset"/>
            </template>
        </CommonTable>
        <MenuEditForm ref="menuEditFormRef"/>
    </div>
</template>

<script lang="ts">
import {defineComponent, onMounted, onUnmounted, ref, watch} from "vue";
import CommonTable from "@/components/commonTable/index.vue";
import {useCommonTable} from "@/components/commonTable/useCommonTable";
import {menuController} from "@/api";
import {columns} from "@/views/pages/system/menu/columns";
import MenuQueryForm from "@/views/pages/system/menu/menuQueryForm.vue";
import MenuEditForm from "@/views/pages/system/menu/menuEditForm.vue";
import {eventBus} from "@/config/eventBus";
import {tableActionEnums} from "@/enums/tableActionEnums";

enum actionEnums {
    // 新增子项
    ADD_CHILD = "add_child",
}

export default defineComponent({
    components: {
        CommonTable, MenuQueryForm, MenuEditForm
    },
    setup() {
        const {
            tableDataList,
            tablePagination,
            tableLoading,
            onPageChange,
            onPageSizeChange,
            getDataList
        } = useCommonTable(menuController.list)
        const menuEditFormRef = ref(null)
        // 操作列
        const actionCol = ref<Array<any>>([
            {
                title: "新增子项",
                colorType: "success",
                actionType: actionEnums.ADD_CHILD,
            }
        ])

        // @ts-ignore
        watch(() => menuEditFormRef?.value?.modalLoading, () => getDataList(), {deep: true})

        const onEvent = (value: any) => {
            switch (value.type) {
                case tableActionEnums.ADD:
                    // @ts-ignore
                    menuEditFormRef?.value?.onAdd()
                    break
                case tableActionEnums.EDIT:
                    // @ts-ignore
                    menuEditFormRef?.value?.onEdit(value.row)
                    break
                case tableActionEnums.DELETE:
                    // @ts-ignore
                    menuEditFormRef?.value?.onDeleteById(value.row.menuId)
                    break
                case tableActionEnums.BATCH_DELETE:
                    // @ts-ignore
                    menuEditFormRef?.value?.onDeleteByIds(value.data)
                    break
                case actionEnums.ADD_CHILD:
                    if(value.row.menuType === "F"){
                        window.$message.error("按钮无法新增子项！")
                        return
                    }
                    // @ts-ignore
                    menuEditFormRef?.value?.addChildMenu(value.row)
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
            eventBus.on("handleMenuStatusSwitchClick", (row: any) => {
                menuController.updateStatus({
                    menuId: row.menuId,
                    status: [0, "0"].includes(row.status) ? "1" : "0"
                }).then(res => {
                    window.$message.success(res.msg)
                    getDataList()
                })
            })
        })

        onUnmounted(() => {
            eventBus.off("handleMenuStatusSwitchClick",() => {})
        })

        return {
            columns,
            tableDataList,
            tablePagination,
            tableLoading,
            onPageChange,
            onPageSizeChange,
            actionCol,
            onEvent,
            onQuery,
            onReset,
            menuEditFormRef
        }
    }
})
</script>
