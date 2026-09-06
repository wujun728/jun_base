import {PiniaPluginContext} from "pinia";
import {storage} from "@/hooks/useStorage";
import {toRaw} from "vue";

/**
 * 做状态持久化
 * @param key
 */
export const piniaPlugin = (key?: string) => {
    const piniaKey: string = 'PiniaKey'
    return (context: PiniaPluginContext) => {
        const {store} = context
        const data = storage.get(`${key ?? piniaKey}-${store.$id}`)
        // 每次 pinia 中的状态发生改变时，都会执行 $subscribe
        store.$subscribe(() => {
            // store.$state 需要转换成普通对象，可以console.log看看
            storage.set(`${key ?? piniaKey}-${store.$id}`, toRaw(store.$state))
        })
        // 即将 data 解构，后重新赋值给 pinia 的 state，实现持久化
        return {
            ...data
        }
    }
}