// 添加日期范围
export default function addDateRange(
    params: any,
    dateRange: Array<string>,
    propName: any = undefined
) {
    const search = params;
    search.params =
        typeof search.params === 'object' &&
        search.params !== null &&
        !Array.isArray(search.params)
            ? search.params
            : {};
    dateRange = Array.isArray(dateRange) ? dateRange : [];
    if (typeof propName === 'undefined') {
        // eslint-disable-next-line prefer-destructuring
        search.params[`begin${'Time'}`] = dateRange[0];
        // eslint-disable-next-line prefer-destructuring
        search.params[`end${'Time'}`] = dateRange[1];
    } else {
        // eslint-disable-next-line prefer-destructuring
        search.params[`begin${propName}`] = dateRange[0];
        // eslint-disable-next-line prefer-destructuring
        search.params[`end${propName}`] = dateRange[1];
    }
    return search;
}

// 转换字符串，undefined,null等转化为""
export function parseStrEmpty(str: any) {
    if (!str || str === "undefined" || str === "null") {
        return "";
    }
    return str;
}

/**
 * 参数处理
 * @param {*} params  参数
 */
export function tansParams(params: any) {
    let result = ''
    Object.keys(params).forEach((propName) => {
        const value = params[propName];
        const part = `${encodeURIComponent(propName)}=`;
        if (value !== null && value !== "" && typeof (value) !== "undefined") {
            if (typeof value === 'object') {
                Object.keys(value).forEach((key) => {
                    if (value[key] !== null && value[key] !== "" && typeof (value[key]) !== 'undefined') {
                        const param = `${propName}[${key}]`;
                        const subPart = `${encodeURIComponent(param)}=`;
                        result += `${subPart + encodeURIComponent(value[key])}&`;
                    }
                })
            } else {
                result += `${part + encodeURIComponent(value)}&`;
            }
        }
    })
    return result
}

// 验证是否为blob格式
export function blobValidate(data: any) {
    return data.type !== 'application/json'
}

// 回显数据字典
export function selectDictLabel(dictOptions: Array<any>, value: string) {
    if (value === undefined) {
        return "";
    }
    const actions = [];
    dictOptions.forEach((element) => {
        if (element.dictValue === value) {
            actions.push(element.dictLabel);
        }
    })
    if (actions.length === 0) {
        actions.push(value);
    }
    return actions.join('');
}

// 回显数据字典（字符串、数组）
export function selectDictLabels(dictOptions: Array<any>, value: string | Array<string>, separator: string) {
    if (value === undefined || value === null) {
        return "";
    }
    if (value instanceof String) {
        value = value.split(",");
    }
    const actions = [];
    dictOptions.forEach((element) => {
        if (value.includes(element.dictValue)) {
            actions.push(element.dictLabel);
        }
    })
    if (actions.length === 0) {
        actions.push(`${value}${separator}`);
    }
    return actions.join('');
}

export interface TreeNode {
    id: any;
    parentId: any;
    childrenList: TreeNode[];
    [key: string]: any;
}

/**
 * 构造树型结构数据
 * @param {*} data 数据源
 * @param {*} id id字段 默认 'id'
 * @param {*} parentId 父节点字段 默认 'parentId'
 * @param {*} children 孩子节点字段 默认 'children'
 */
export function handleTree(
    data: any[],
    id?: string,
    parentId?: string,
    children?: string
): TreeNode[] {
    const config: {
        id: string;
        parentId: string;
        childrenList: string;
    } = {
        id: id || 'id',
        parentId: parentId || 'parentId',
        childrenList: children || 'children',
    };
    const childrenListMap: { [key: string]: TreeNode[] } = {};
    const nodeIds: { [key: string]: TreeNode } = {};
    const tree: TreeNode[] = [];
    data.forEach((d) => {
        const p = d[config.parentId];
        if (childrenListMap[p] == null) {
            childrenListMap[p] = [];
        }
        nodeIds[d[config.id]] = d;
        childrenListMap[p].push(d);
    })
    data.forEach((d) => {
        const p = d[config.parentId];
        if (nodeIds[p] == null) {
            tree.push(d);
        }
    })

    function adaptToChildrenList(o: TreeNode) {
        if (childrenListMap[o[config.id]] !== null) {
            o[config.childrenList] = childrenListMap[o[config.id]];
        }
        if (o[config.childrenList]) {
            o[config.childrenList].forEach((c: any) => {
                adaptToChildrenList(c);
            });
        }
    }
    tree.forEach(t => {
        adaptToChildrenList(t)
    })

    return tree;
}
