import { Component } from "vue";

export default function Schema(name: string): (target: any, attr: any) => void;
export default function Schema(options: SchemaType<any>): (target: any, attr: any) => void;
export default function Schema(value: SchemaType<any> | string) {
    return function (target: any, attr: any) {
        if (target.schema == undefined) target.schema = {}
        if (typeof value === 'string') target.schema[attr] = { name: value, components: {}, attr };
        else {
            target.schema[attr] = value;
        }
    }
}
export interface SchemaType<T> {
    name: string,
    attr: keyof T & string,
    components: { [key: string]: (...args: any[]) => Component }
}
export function getSchema<T extends Object, P extends keyof T & string>(target: T | (new (...args: any[]) => T), prop: P): SchemaType<T> {
    const constructor = typeof target === 'function' ? target : target.constructor
    const schemaObj = constructor.prototype.schema
    if (!!schemaObj) {
        const schema: SchemaType<T> = schemaObj[prop]
        if (!!schema) return schema
        else throw new Error(`${constructor.name}'s ${prop} have not @schema`)
    } else {
        throw new Error(`${constructor.name} have not @schema`)
    }
}

export function getSchemas<T extends Object, P extends keyof T & string>(target: T | (new (...args: any[]) => T), ...props: P[]): SchemaType<T>[] {
    const constructor = typeof target === 'function' ? target : target.constructor
    const schemaObj: { [key: string]: SchemaType<T> } = constructor.prototype.schema
    if (!!schemaObj) {
        if (props.length > 0) {
            return props.map((prop) => schemaObj[prop]).filter(schema => !!schema)
        } else {
            return Object.values(schemaObj)
        }
    } else {
        throw new Error(`${constructor.name} have not @schema`)
    }
}

export function getSchemaName(target: any, prop: string): string {
    const schema = getSchema(target, prop)
    return schema.name
}
