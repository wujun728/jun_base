import {DataTableColumns, NSwitch, NTag} from "naive-ui";
import {useColumns} from "@/views/pages/system/menu/useColumns";
import {useCommonColumns} from "@/hooks/useCommonColums";
import {h} from "vue";
import {eventBus} from "@/config/eventBus";

const {useRenderStateById} = useCommonColumns()
const {useRenderMenuType} = useColumns()
/**
 * 状态的点击事件
 * @param row
 */
const handleSwitchClick = (row: any) => {
    eventBus.emit("handleMenuStatusSwitchClick", row)
}

export const columns: DataTableColumns<any> = [
    {
        title: "菜单名称",
        key: "menuName",
        ellipsis: {
            tooltip: true
        }
    },
    {
        title: "图标",
        key: "icon",
        ellipsis: {
            tooltip: true
        }
    },
    {
        title: "状态",
        key: "status",
        width: 150,
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
        title: "菜单类型",
        key: "menuType",
        render: (row) => (
            <NTag type={row.menuType === "M" ? "primary" : row.menuType === "C" ? "success" : "warning"}>
                {useRenderMenuType(row.menuType)}
            </NTag>
        )
    },
    {
        title: "路由名称",
        key: "name",
        align: "center",
        render: (row) => (
            <span>
                {[undefined, null, ""].includes(row.name) ? "-" : row.name}
            </span>
        )
    },
    {
        title: "权限标识",
        key: "perms",
        width: 200,
        ellipsis: {
            tooltip: true
        },
        align: "center",
        render: (row) => (
            <span>
                {[undefined, null, ""].includes(row.perms) ? "-" : row.perms}
            </span>
        )
    },
    {
        title: "组件路径",
        key: "component",
        width: 300,
        render: (row) => (
            <span>
                {[undefined, null, ""].includes(row.component) ? "-" : row.component}
            </span>
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