import useRequest from "@/api/useRequest";
import {IPage} from "@/interface/modules/system";

export class logController {
    /**
     * 获取操作日志
     * @param data
     * @param pagination
     */
    static getOperationLogList(data: any, pagination?: IPage): Promise<any> {
        return useRequest({
            url: "/api/log/getOperationLogList",
            method: "get",
            params: pagination,
        })
    }

    /**
     * 获取登录日志
     * @param data
     * @param pagination
     */
    static getLoginLogList(data: any, pagination?: IPage): Promise<any> {
        return useRequest({
            url: "/api/log/getLoginLogList",
            method: "get",
            params: pagination,
        })
    }

}