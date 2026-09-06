import useRequest from "@/api/useRequest";
import {IPage} from "@/interface/modules/system";

export class userController {
    /**
     * 用户登录
     * @param data
     */
    static login(data: any): Promise<any> {
        return useRequest({
            url: "/api/user/login",
            method: "post",
            data
        })
    }

    /**
     * 退出登录
     * @param data
     */
    static logout(): Promise<any> {
        return useRequest({
            url: "/api/user/logout",
            method: "post",
        })
    }

    /**
     * 获取验证码
     */
    static captcha(): Promise<any> {
        return useRequest({
            url: "/api/verify/captcha",
            method: "get"
        })
    }

    /**
     * 用户列表
     */
    static getUserList(data: any, pagination?: IPage): Promise<any> {
        return useRequest({
            url: "/api/user/list",
            method: "post",
            params: pagination,
            data
        })
    }

    /**
     * 获取用户详情
     * @param userId
     */
    static getUserById(userId: number | string): Promise<any> {
        return useRequest({
            url: `/api/user/getUserByUserId/${userId}`,
            method: "get"
        })
    }


    /**
     * 新增用户
     * @param data
     */
    static add(data: any): Promise<any> {
        return useRequest({
            url: "/api/user/add",
            method: "post",
            data
        })
    }


    /**
     * 更新用户
     * @param data
     */
    static edit(data: any): Promise<any> {
        return useRequest({
            url: "/api/user/update",
            method: "post",
            data
        })
    }

    /**
     * 更新用户状态
     * @param data
     */
    static updateStatus(data: any): Promise<any> {
        return useRequest({
            url: "/api/user/updateStatus",
            method: "post",
            data
        })
    }

    /**
     * 删除用户
     * @param userId
     */
    static deleteById(userId: number | string): Promise<any> {
        return useRequest({
            url: `/api/user/delete/${userId}`,
            method: "post"
        })
    }

    /**
     * 批量删除用户
     * @param data
     */
    static deleteByIds(data: Array<number | string>): Promise<any> {
        return useRequest({
            url: "/api/user/delete",
            method: "post",
            data
        })
    }

}