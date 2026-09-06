import {DataTableColumns} from "naive-ui";


export const columns: DataTableColumns<any> = [
    {
        title: "会话编号",
        key: "token",
        width: 200,
        ellipsis: {
            tooltip: true
        }
    },
    {
        title: "登录名称",
        key: "username"
    },
    {
        title: "主机IP",
        key: "ipaddr",
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
        title: "登录时间",
        key: "loginTime",
        width: 200
    }
]