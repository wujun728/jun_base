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
    eventBus.emit("handleDeptStatusSwitchClick", row)
}

export const columns: DataTableColumns<any> = [
    {
        title: "部门名称",
        key: "deptName"
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
    }
]