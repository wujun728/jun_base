/**
 *
 * @param name 子节点 name
 * @param data  要查找的 list
 * @param result  结果集
 * @returns {boolean|*[]}
 */

export const useFindParentName = (name: string, data: Array<any> = [], result: Array<string> = []): Array<string> | undefined => {
    for (let i = 0; i < data.length; i++) {
        const item = data[i];
        if (item.name === name) {
            return result;
        }
        if (item.children?.length > 0) {
            result.push(item.name)
            const temp = useFindParentName(name, item.children, result);
            if (temp !== undefined) {
                return result;
            }
            result = []
        }
    }
}

/**
 * 根据 name 查找匹配的 node
 *
 * @param name
 * @param data
 */
export const useFindNodeByName = (name: string, data: Array<any>): any => {
    for (let i = 0; i < data.length; i++) {
        const item = data[i];
        if (item.name === name) {
            return item;
        } else {
            if (item.children?.length > 0) {
                const temp = useFindNodeByName(name, item.children);
                if (temp) {
                    return temp;
                }
            }
        }
    }
}


/**
 * 根据子节点指定的key和对应的value，向上查找其父级
 * @param key 如 userId: 123 中的 userId
 * @param value 如 userId: 123 中的 123
 * @param childrenField
 * @param data
 * @param result
 */
export const useFindParentNodes = (key: string,
                                   value: string | number,
                                   childrenField: string = "children",
                                   data: Array<any> = [],
                                   result: Array<any> = []): Array<any> | undefined => {
    for (let i = 0; i < data.length; i++) {
        const item = data[i];
        result.push(item);
        if (item[key] === value) {
            return result;
        }
        if (item.children?.length > 0) {
            const temp = useFindParentNodes(key, value, childrenField, item.children, result);
            if (temp !== undefined) {
                // 表示找到了 name 匹配的, 替换 result 中的最后一项 (最后一项保存的是同级别下的上一项)
                if (temp.length === 1) {
                    result[result.length - 1] = temp[0];
                }
                return result;
            }
        }
        result.pop()
    }
}


/**
 * 格式化树
 */
export const useRemoveEmptyChildrenField = (data: Array<any>, childrenField: string = "children"): Array<any> => {
    return data.map((item: any) => {
        if (item[childrenField]?.length > 0) {
            useRemoveEmptyChildrenField(item[childrenField], childrenField)
        } else {
            delete item[childrenField]
        }
        return item
    })
}

/**
 * 数组扁平化
 * @param data
 * @param result
 * @param childrenField
 */
export const useTreeToArray = (data: Array<any>, result: Array<any> = [], childrenField: string = "children"): Array<any> => {
    data.map(item => {
        result.push(item)
        if (item[childrenField]?.length > 0) {
            useTreeToArray(item[childrenField], result)
        }
    })
    return result
}


/**
 * 搭配 useFindParentNodes 使用（先查找出所有的父级节点）
 * 数组分割显示 如 总公司 / 市场部门 / 销售岗
 */

export const useArraySeparator = (data: Array<any>, key: string, separator: string = "/") => {
    let result: string = "";
    data.forEach((item, index) => {
        if (index === data.length - 1) {
            result = `${result} ${item[key]}`
        } else {
            result = `${result} ${item[key]} ${separator}`
        }
    })
    return result;
}

/**
 * 克隆出一个对象的属性
 * @param data
 * @param sourceField
 * @param targetField
 * @param childrenField
 */
export const useFieldClone = (data: Array<any>, sourceField: string, targetField: string, childrenField: string = "children") => {
    const temp =  data.map((item: any) => {
        item[targetField] = item[sourceField]
        if (item[childrenField]?.length > 0) {
            useFieldClone(item[childrenField], sourceField, targetField, childrenField)
        } else {
            delete item[childrenField]
        }
        return item
    })
    return temp
}