type DictKey = string | null;
type Dict = {
    createBy: string,
    createTime: string,
    updateBy: string | null,
    updateTime: string | null,
    remark: string | null,
    dictName: string,
    dictCode: string,
    dictSort: number,
    dictLabel: string,
    dictValue: string,
    dictType: string,
    cssClass: null | string,
    listClass: 'primary' | 'success' | 'warning' | 'danger' | 'info' | 'text',
    isDefault: "N" | "Y",
    status: "0" | "1",
    default: boolean
}

type DictValue = {
    label: string,
    value: string,
    elTagType: 'primary' | 'success' | 'warning' | 'danger' | 'info' | 'text',
    elTagClass: string | null
}
