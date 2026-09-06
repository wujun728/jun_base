<template>
    <div>
        <CommonTable :columns="columns"
                     :pagination="tablePagination"
                     :loading="tableLoading"
                     :dataList="tableDataList"
                     :labelField="'title'"
                     :actionWidth="140"
                     :rowKey="(row) => row.postId"
                     :scrollX="1000"
                     @onPageChange="onPageChange"
                     @onPageSizeChange="onPageSizeChange"
                     @onEvent="onEvent">
            <template #query>
                <PostQueryForm @onQuery="onQuery" @onReset="onReset"/>
            </template>
        </CommonTable>
        <PostEditForm ref="postEditFormRef"/>
    </div>
</template>

<script lang="ts">
import {defineComponent, onMounted, onUnmounted, ref, watch} from "vue";
import CommonTable from "@/components/commonTable/index.vue";
import {useCommonTable} from "@/components/commonTable/useCommonTable";
import {menuController, postController} from "@/api";
import {columns} from "@/views/pages/system/position/columns";
import PostQueryForm from "@/views/pages/system/position/postQueryForm.vue";
import PostEditForm from "@/views/pages/system/position/postEditForm.vue";
import {eventBus} from "@/config/eventBus";
import {tableActionEnums} from "@/enums/tableActionEnums";

export default defineComponent({
    components: {
        CommonTable, PostQueryForm, PostEditForm
    },
    setup() {
        const {
            tableDataList,
            tablePagination,
            tableLoading,
            onPageChange,
            onPageSizeChange,
            getDataList
        } = useCommonTable(postController.list)
        const postEditFormRef = ref(null)

        // @ts-ignore
        watch(() => postEditFormRef?.value?.modalLoading, () => getDataList(), {deep: true})


        const onEvent = (value: any) => {
            switch (value.type) {
                case tableActionEnums.ADD:
                    // @ts-ignore
                    postEditFormRef?.value?.onAdd()
                    break
                case tableActionEnums.EDIT:
                    // @ts-ignore
                    postEditFormRef?.value?.onEdit(value.row, value.row.roleId)
                    break
                case tableActionEnums.DELETE:
                    // @ts-ignore
                    postEditFormRef?.value?.onDeleteById(value.row.roleId)
                    break
                case tableActionEnums.BATCH_DELETE:
                    // @ts-ignore
                    postEditFormRef?.value?.onDeleteByIds(value.data)
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
            eventBus.on("handlePostStatusSwitchClick", (row: any) => {
                postController.updateStatus({
                    postId: row.postId,
                    status: [0, "0"].includes(row.status) ? "1" : "0"
                }).then(res => {
                    window.$message.success(res.msg)
                    getDataList()
                })
            })
        })

        onUnmounted(() => {
            eventBus.off("handlePostStatusSwitchClick",() => {})
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
            postEditFormRef
        }
    }
})
</script>
