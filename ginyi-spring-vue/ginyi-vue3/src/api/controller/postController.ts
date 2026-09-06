import useRequest from "@/api/useRequest";
import {IPage} from "@/interface/modules/system";

export class postController {
    /**
     * 获取岗位列表
     * @param data
     * @param pagination
     */
    static list(data: any, pagination?: IPage): Promise<any> {
        return useRequest({
            url: "/api/post/list",
            method: "post",
            params: pagination,
            data
        })
    }

    /**
     * 获取岗位详情
     * @param postId
     */
    static getPostDetailsById(postId: number | string): Promise<any> {
        return useRequest({
            url: `/api/post/getPostByRoleId/${postId}`,
            method: "get"
        })
    }

    /**
     * 新增岗位
     * @param data
     * @param pagination
     */
    static add(data: any): Promise<any> {
        return useRequest({
            url: "/api/post/add",
            method: "post",
            data
        })
    }

    /**
     * 更新岗位
     * @param data
     */
    static edit(data: any): Promise<any> {
        return useRequest({
            url: "/api/post/update",
            method: "post",
            data
        })
    }

    /**
     * 删除岗位
     * @param userId
     */
    static deleteById(userId: number | string): Promise<any> {
        return useRequest({
            url: `/api/post/delete/${userId}`,
            method: "post"
        })
    }

    /**
     * 批量删除岗位
     * @param data
     */
    static deleteByIds(data: Array<number | string>): Promise<any> {
        return useRequest({
            url: "/api/post/delete",
            method: "post",
            data
        })
    }

    /**
     * 更新岗位状态
     * @param data
     */
    static updateStatus(data: any): Promise<any> {
        return useRequest({
            url: "/api/post/updateStatus",
            method: "post",
            data
        })
    }

}