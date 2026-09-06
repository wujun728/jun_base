import useRequest from "@/api/useRequest";
import {IPage} from "@/interface/modules/system";

export class roleController {
    /**
     * 获取角色列表
     * @param data
     * @param pagination
     */
    static list(data: any, pagination?: IPage): Promise<any> {
        return useRequest({
            url: "/api/role/list",
            method: "post",
            params: pagination,
            data
        })
    }

    /**
     * 获取角色详情
     * @param roleId
     */
    static getRoleDetailsById(roleId: number | string): Promise<any> {
        return useRequest({
            url: `/api/role/getRoleByRoleId/${roleId}`,
            method: "get"
        })
    }

    /**
     * 新增角色
     * @param data
     * @param pagination
     */
    static add(data: any): Promise<any> {
        return useRequest({
            url: "/api/role/add",
            method: "post",
            data
        })
    }

    /**
     * 更新角色
     * @param data
     */
    static edit(data: any): Promise<any> {
        return useRequest({
            url: "/api/role/update",
            method: "post",
            data
        })
    }

    /**
     * 删除角色
     * @param userId
     */
    static deleteById(userId: number | string): Promise<any> {
        return useRequest({
            url: `/api/role/delete/${userId}`,
            method: "post"
        })
    }

    /**
     * 批量删除角色
     * @param data
     */
    static deleteByIds(data: Array<number | string>): Promise<any> {
        return useRequest({
            url: "/api/role/delete",
            method: "post",
            data
        })
    }

    /**
     * 更新角色状态
     * @param data
     */
    static updateStatus(data: any): Promise<any> {
        return useRequest({
            url: "/api/role/updateStatus",
            method: "post",
            data
        })
    }

}