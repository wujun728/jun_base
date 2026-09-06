<template>
    <div>
        <CommonTable :columns="columns"
                     :pagination="tablePagination"
                     :loading="tableLoading"
                     :dataList="tableDataList"
                     :labelField="'title'"
                     :actionWidth="140"
                     :rowKey="(row) => row.id"
                     :scrollX="1200"
                     :buttonConfig="buttonConfig"
                     :showActionCol="false"
                     @onPageChange="onPageChange"
                     @onPageSizeChange="onPageSizeChange"
                     @onEvent="onEvent">
        </CommonTable>
    </div>
</template>

<script lang="ts">
import {defineComponent, onMounted, reactive} from "vue";
import CommonTable from "@/components/commonTable/index.vue";
import {useCommonTable} from "@/components/commonTable/useCommonTable";
import {logController} from "@/api";
import {IButtonType} from "@/interface/modules/system";
import {columns} from "@/views/pages/system/log/login/columns";

export default defineComponent({
    name: "LogLogin",
    components: {
        CommonTable
    },
    setup() {
        const {
            tableDataList,
            tablePagination,
            tableLoading,
            onPageChange,
            onPageSizeChange,
            getDataList
        } = useCommonTable(logController.getLoginLogList)
        const buttonConfig = reactive<IButtonType>({
            addButton: {
                type: "add",
                title: "新增数据",
                actionType: 0,
                show: false,
                colorType: "primary",
            },
            editButton: {
                type: "edit",
                title: "编辑",
                actionType: 1,
                show: false,
                colorType: "primary",
            },
            deleteButton: {
                type: "delete",
                title: "强制退出",
                actionType: 2,
                show: false,
                colorType: "error",
            },
            batchDeleteButton: {
                type: "batchDelete",
                colorType: "error",
                actionType: 3,
                show: false,
                title: "批量强制退出",
            }
        })

        const onEvent = (value: any) => {

        }

        onMounted(() => {
            getDataList()
        })

        return {
            columns,
            tableDataList,
            tablePagination,
            tableLoading,
            onPageChange,
            onPageSizeChange,
            buttonConfig,
            onEvent
        }
    }
})

</script>

