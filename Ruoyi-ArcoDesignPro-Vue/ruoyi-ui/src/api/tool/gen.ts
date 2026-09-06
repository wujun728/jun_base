import axios from 'axios';

export interface GenTableRecord {
    createBy?: string;
    createTime?: string;
    updateBy?: string;
    updateTime?: string;
    tableId?: number;
    tableName?: string;
    tableComment?: string;
    className?: string;
    tplCategory?: string;
    packageName?: string;
    moduleName?: string;
    businessName?: string;
    functionName?: string;
    functionAuthor?: string;
    genType?: string;
    genPath?: string;
    options?: string;
    crud?: boolean;
    tree?: boolean;
}

export interface ColumnRow {
    createBy?: any;
    createTime?: any;
    updateBy?: any;
    updateTime?: any;
    remark?: any;
    columnId: number;
    tableId: number;
    columnName: string;
    columnComment: string;
    columnType: string;
    javaType: string;
    javaField: string;
    isPk: string;
    isIncrement: string;
    isRequired?: string | boolean;
    isInsert?: string | boolean;
    isEdit?: string | boolean;
    isList?: string | boolean;
    isQuery?: string | boolean;
    queryType: string;
    htmlType: string;
    dictType: string;
    sort: number;
    pk: boolean;
    superColumn: boolean;
    insert: boolean;
    edit: boolean;
    usableColumn: boolean;
    required: boolean;
    list: boolean;
    query: boolean;
    increment: boolean;
    capJavaField: string;
}

export interface GenTableDetailInfo {
    createBy?: any;
    createTime?: any;
    updateBy?: any;
    updateTime?: any;
    remark?: any;
    tableId: number;
    tableName: string;
    tableComment: string;
    subTableName?: any;
    subTableFkName?: any;
    className: string;
    tplCategory: string;
    packageName: string;
    moduleName: string;
    businessName: string;
    functionName: string;
    functionAuthor: string;
    genType?: string;
    genPath: string;
    pkColumn?: any;
    subTable?: any;
    columns: ColumnRow[];
    options: string;
    treeCode?: any;
    treeParentCode?: any;
    treeName?: any;
    parentMenuId: string;
    parentMenuName?: any;
    crud: boolean;
    tree: boolean;
    sub: boolean;
    params?: any;
}

export interface GenTableDetail {
    rows: ColumnRow[];
    info: GenTableDetailInfo;
    tables?: GenTableDetailInfo[];
}

// 查询生成表数据
export function listTable(query: any) {
    return axios({
        url: '/tool/gen/list',
        method: 'get',
        params: query
    })
}

// 查询db数据库列表
export function listDbTable(query: any) {
    return axios({
        url: '/tool/gen/db/list',
        method: 'get',
        params: query
    })
}

// 查询表详细信息
export function getGenTable(tableId: number | string) {
    return axios({
        url: `/tool/gen/${tableId}`,
        method: 'get'
    })
}

// 修改代码生成信息
export function updateGenTable(data: GenTableRecord) {
    return axios({
        url: '/tool/gen',
        method: 'put',
        data
    })
}

// 导入表
export function importTable(data: any) {
    return axios({
        url: '/tool/gen/importTable',
        method: 'post',
        params: data
    })
}

// 预览生成代码
export function previewTable(tableId: number | string) {
    return axios({
        url: `/tool/gen/preview/${tableId}`,
        method: 'get'
    })
}

// 删除表数据
export function delTable(tableId: number | string) {
    return axios({
        url: `/tool/gen/${tableId}`,
        method: 'delete'
    })
}

// 生成代码（自定义路径）
export function genCode(tableName: string) {
    return axios({
        url: `/tool/gen/genCode/${tableName}`,
        method: 'get'
    })
}


// 同步数据库
export function syncDb(tableName: string) {
    return axios({
        url: `/tool/gen/synchDb/${tableName}`,
        method: 'get'
    })
}

