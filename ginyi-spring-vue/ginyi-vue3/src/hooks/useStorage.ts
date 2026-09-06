import {storeKeyEnums} from "@/enums/storeKeyEnums";

export class storage {

    /**
     * 默认的存储时间（天）
     */
    private static expireTime: number = 7

    /**
     * 设置缓存数据
     * @param key
     * @param value
     * @param expire 过期时间，单位天
     */
    static set(key: string, value: any, expire?: number) {
        const data = JSON.stringify({
            value,
            expire: expire !== undefined ? Date.now() + expire * 60 * 60 * 24 * 7 * 1000 : Date.now() + this.expireTime * 60 * 60 * 24 * 7 * 1000,
        });
        localStorage.setItem(key, data)
    }

    /**
     * 获取缓存数据
     * @param key
     */
    static get(key: string): any {
        const data: any = localStorage.getItem(key)
        if (data) {
            const {value, expire} = JSON.parse(data);
            // 在有效期内直接返回
            if ([null, undefined].includes(expire) || expire >= Date.now()) {
                return value;
            }
            // 清除缓存
            this.remove(key)
            return null
        }
    }

    /**
     * 清除缓存
     * @param key
     */
    static remove(key: string) {
        localStorage.removeItem(key)
    }

    /**
     * 清除缓存，除系统配置外
     */
    static clear() {
        let keys: Array<String> = [];
        for (let i = 0; i < localStorage.length; i++) {
            keys.push(localStorage.key(i) as string)
        }
        keys.map(key => {
            if (key.indexOf(storeKeyEnums.SYSTEM) === -1) {
                this.remove(key as string)
            }
        })
    }
}
