import { Component } from "vue";
import { getSchema, getSchemas } from "./Schema";

export default function SchemaComponent(value: string, component: Component) {
    return function (target: any, attr: any) {
        target.schema[attr].components[value] = component
    }
}


export function getSchemaComponent(target: any, prop: string, elName: string, ...args: any[]): Component {
    const schema = getSchema(target, prop)
    const component = schema.components[elName]
    return component(args)
}

/**
 * 获取某个类的所有标签名为elName的组件
 * @param target 类
 * @param elName 标签名
 * @returns 组件列表
 */
export function getSchemaComponents(target: any, elName: string): { name: string, attr: string, component: (...args: any[]) => Component }[] {
    const schema = getSchemas(target)
    return schema.map(s => ({ name: s.name, component: s.components[elName], attr: s.attr })).filter(s => s.component)
}

