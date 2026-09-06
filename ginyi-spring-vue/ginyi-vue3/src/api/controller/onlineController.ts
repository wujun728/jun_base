import useRequest from "@/api/useRequest";
import {IPage} from "@/interface/modules/system";

export class onlineController {
    /**
     * 获取在线用户
     * @param data
     * @param pagination
     */
    static getOnlineUserList(data: any, pagination?: IPage): Promise<any> {
        return useRequest({
            url: "/api/online/getOnlineUserList",
            method: "get",
            params: pagination,
        })
    }

    /**
     * 强制退出
     * @param token
     */
    static removeUser(token: any): Promise<any> {
        return useRequest({
            url: `/api/online/removeUser/${token}`,
            method: "post",
        })
    }

    /**
     * 批量强制退出
     * @param data
     */
    static batchRemoveUser(data: any): Promise<any> {
        return useRequest({
            url: "/api/online/removeUser",
            method: "post",
            data
        })
    }

}