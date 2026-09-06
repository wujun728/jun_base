import {DataTableColumns, NTag} from "naive-ui";
import {useCommonColumns} from "@/hooks/useCommonColums";
import {useColumns} from "@/views/pages/system/log/operation/useColumns";

const {useRenderSuccessById} = useCommonColumns()
const {useRenderOperationType} = useColumns()


export const columns: DataTableColumns<any> = [
    {
        title: "操作人员",
        key: "operationName",
    },
    {
        title: "操作地址",
        key: "operationIp"
    },
    {
        title: "操作地点",
        key: "operationLocation",
    },
    {
        title: "系统模块",
        key: "title",
    },
    {
        title: "操作类型",
        key: "businessType",
        render: (row) => (
            <NTag type={
                row.businessType.toString() === "1" ? "info"
                    : row.businessType.toString() === "2" ? "warning"
                        : row.businessType.toString() === "3" ? "error" : "default"}>
                {useRenderOperationType(row.businessType.toString())}
            </NTag>
        )
    },
    {
        title: "请求方式",
        key: "requestMethod",
    },
    {
        title: "操作状态",
        key: "status",
        render: (row) => (
            <NTag type={row.status.toString() === "0" ? "success" : "error"}>
                {useRenderSuccessById(row.status)}
            </NTag>
        )
    },
    {
        title: "操作时间",
        key: "createTime",
        width: 200
    }
]