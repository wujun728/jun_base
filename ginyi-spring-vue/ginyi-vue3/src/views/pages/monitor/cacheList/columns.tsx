import {DataTableColumns, NButton, NSpace} from "naive-ui";
import {eventBus} from "@/config/eventBus";
import {h} from "vue";

/**
 * 查看详情的点击事件
 * @param row
 */
const handleClickDetails = (row: any) => {
    eventBus.emit("handleClickDetails", row)
}

/**
 * 删除的点击事件
 * @param row
 */
const handleClickDelete = (row: any) => {
    eventBus.emit("handleClickDelete", row)
}

export const columns: DataTableColumns<any> = [
    {
        title: "键名",
        key: "key",
        ellipsis: {
            tooltip: true
        }
    },
    {
        title: "操作",
        key: "action",
        width: 160,
        render: (row) => (
            h(NSpace, null, () => [
                h(NButton, {
                    type: "primary", size: "small", onClick: () => handleClickDetails(row)}, {default: () => "查看详情"}),
                h(NButton, {type: "error", size: "small", onClick: () => handleClickDelete(row)}, {default: () => "删除"})
            ])
        )
    }
]