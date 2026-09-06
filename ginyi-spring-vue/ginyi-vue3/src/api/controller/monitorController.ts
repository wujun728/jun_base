import useRequest from "@/api/useRequest";

export class monitorController {
    /**
     * 获取服务器信息
     */
    static getServerInfo(): Promise<any> {
        return useRequest({
            url: "/api/monitor/getServerInfo",
            method: "get",
        })
    }

    /**
     * 获取缓存列表
     */
    static getCacheList(): Promise<any> {
        return useRequest({
            url: "/api/monitor/getCacheList",
            method: "get",
        })
    }

    /**
     * 获取缓存数据
     */
    static getCacheValue(data: any): Promise<any> {
        return useRequest({
            url: "/api/monitor/getCacheValue",
            method: "post",
            data: data
        })
    }

    /**
     * 删除缓存
     */
    static removeCache(key: string): Promise<any> {
        return useRequest({
            url: `/api/monitor/removeCache/${key}`,
            method: "post",
        })
    }

}