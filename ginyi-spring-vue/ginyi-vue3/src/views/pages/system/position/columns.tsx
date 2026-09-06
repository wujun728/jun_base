import {DataTableColumns, NSwitch, NTag} from "naive-ui";
import {useCommonColumns} from "@/hooks/useCommonColums";
import {h} from "vue";
import {eventBus} from "@/config/eventBus";

const {useRenderStateById} = useCommonColumns()
/**
 * 状态的点击事件
 * @param row
 */
const handleSwitchClick = (row: any) => {
    eventBus.emit("handlePostStatusSwitchClick", row)
}
export const columns: DataTableColumns<any> = [
    {
        title: "岗位名称",
        key: "postName"
    },
    {
        title: "岗位编码",
        key: "postCode"
    },
    {
        title: "状态",
        key: "status",
        render: (row) => (
            h(NSwitch, {
                value: row.status === "0",
                onClick: () => handleSwitchClick(row)
            }, {
                checked: () => "正常",
                unchecked: () => "禁用"
            })
        )
    },
    {
        title: "排序",
        key: "sort",
    },
    {
        title: "创建时间",
        key: "createTime",
        width: 200
    }
]