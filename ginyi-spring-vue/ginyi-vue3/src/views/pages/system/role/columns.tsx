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
    eventBus.emit("handleRoleStatusSwitchClick", row)
}

export const columns: DataTableColumns<any> = [
    {
        title: "角色名称",
        key: "roleName"
    },
    {
        title: "角色权限字符串",
        key: "roleKey"
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
        width: 80,
    },
    {
        title: "创建时间",
        key: "createTime",
        width: 200
    }
]