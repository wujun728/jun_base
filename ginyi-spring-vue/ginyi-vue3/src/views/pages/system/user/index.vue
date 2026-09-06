<template>
    <div>
        <CommonTable :columns="columns"
                     :dataList="tableDataList"
                     :labelField="'title'"
                     :rowKey="(row) => row.userId"
                     :actionColData="actionCol"
                     :actionWidth="220"
                     :pagination="tablePagination"
                     :loading="tableLoading"
                     :scrollX="2200"
                     @onPageChange="onPageChange"
                     @onPageSizeChange="onPageSizeChange"
                     @onEvent="onEvent">
            <template #query>
                <UserQueryForm @onQuery="onQuery" @onReset="onReset"/>
            </template>
        </CommonTable>
        <UserEditForm ref="userEditFormRef"/>
    </div>
</template>

<script lang="ts">
import {defineComponent, onMounted, onUnmounted, ref, watch} from "vue";
import CommonTable from "@/components/commonTable/index.vue"
import {columns} from "@/views/pages/system/user/columns";
import {userController} from "@/api";
import UserEditForm from "@/views/pages/system/user/userEditForm.vue";
import UserQueryForm from "@/views/pages/system/user/userQueryForm.vue";
import {useCommonTable} from "@/components/commonTable/useCommonTable";
import {eventBus} from "@/config/eventBus";
import {tableActionEnums} from "@/enums/tableActionEnums";

export default defineComponent({
    components: {
        UserEditForm, CommonTable, UserQueryForm
    },
    setup() {

        const {
            tableDataList,
            tablePagination,
            tableLoading,
            onPageChange,
            onPageSizeChange,
            getDataList
        } = useCommonTable(userController.getUserList)
        const userEditFormRef = ref(null)
        const userId = ref<number | undefined>(undefined)
        // 操作列
        const actionCol = ref<Array<any>>([
            {
                title: "重置密码",
                colorType: "success",
                actionType: 4,
            }
        ])

        // @ts-ignore
        watch(() => userEditFormRef?.value?.modalLoading, () => getDataList(), {deep: true})

        const onEvent = (value: any) => {
            switch (value.type) {
                case tableActionEnums.ADD:
                    // @ts-ignore
                    userEditFormRef?.value?.onAdd()
                    break
                case tableActionEnums.EDIT:
                    // @ts-ignore
                    userEditFormRef?.value?.onEdit(value.row)
                    break
                case tableActionEnums.DELETE:
                    // @ts-ignore
                    userEditFormRef?.value?.onDeleteById(value.row.userId)
                    break
                case tableActionEnums.BATCH_DELETE:
                    // @ts-ignore
                    userEditFormRef?.value?.onDeleteByIds(value.data)
                    break
                // 重置密码
                case 4:
                    window.$message.warning("您点击了重置密码")
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
            eventBus.on("handleUserStatusSwitchClick", (row: any) => {
                userController.updateStatus({
                    userId: row.userId,
                    status: [0, "0"].includes(row.status) ? "1" : "0"
                }).then(res => {
                    window.$message.success(res.msg)
                    getDataList()
                })
            })
        })

        onUnmounted(() => {
            eventBus.off("handleUserStatusSwitchClick",() => {})
        })

        return {
            columns,
            tableDataList,
            tablePagination,
            tableLoading,
            actionCol,
            onPageChange,
            onPageSizeChange,
            onEvent,
            userEditFormRef,
            userId,
            onQuery,
            onReset
        }
    }
})
</script>
