import {DataTableColumns, NTag} from "naive-ui";

export const columns: DataTableColumns<any> = [
    {
        title: "盘符路径",
        key: "dirName",
    },
    {
        title: "盘符类型",
        key: "typeName",
    },
    {
        title: "总大小",
        key: "total",
        render: (row) => (
            <span>{row.total}GB</span>
        )
    },
    {
        title: "已用大小",
        key: "used",
        render: (row) => (
            <span>{row.used}GB</span>
        )
    },
    {
        title: "可用大小",
        key: "free",
        render: (row) => (
            <span>{row.free}GB</span>
        )
    },
    {
        title: "已用百分比",
        key: "usage",
        render: (row) => (
            <span>{row.usage}%</span>
        )
    },
]