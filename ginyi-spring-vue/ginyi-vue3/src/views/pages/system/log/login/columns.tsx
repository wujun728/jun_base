import {DataTableColumns, NTag} from "naive-ui";
import {useCommonColumns} from "@/hooks/useCommonColums";

const {useRenderSuccessById} = useCommonColumns()


export const columns: DataTableColumns<any> = [
    {
        title: "用户名称",
        key: "userName",
    },
    {
        title: "登录地址",
        key: "ipaddr"
    },
    {
        title: "登录地点",
        key: "loginLocation",
    },
    {
        title: "浏览器",
        key: "browser",
    },
    {
        title: "操作系统",
        key: "os",
    },
    {
        title: "登录状态",
        key: "status",
        render: (row) => (
            <NTag type={row.status === "0" ? "success" : "error"}>
                {useRenderSuccessById(row.status)}
            </NTag>
        )
    },
    {
        title: "操作信息",
        key: "msg",
        ellipsis: {
            tooltip: true
        }
    },
    {
        title: "登录日期",
        key: "createTime",
        width: 200
    }
]