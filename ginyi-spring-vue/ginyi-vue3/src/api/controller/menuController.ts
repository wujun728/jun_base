import useRequest from "@/api/useRequest";
import {IPage} from "@/interface/modules/system";

export class menuController {
    /**
     * 获取路由列表
     */
    static getRouterList(): Promise<any> {
        return useRequest({
            url: "/api/menu/getRouterList",
            method: "get",
        })
    }

    /**
     * 获取菜单列表
     * @param data
     * @param pagination
     */
    static list(data: any, pagination?: IPage): Promise<any> {
        return useRequest({
            url: "/api/menu/list",
            method: "post",
            params: pagination,
            data
        })
    }

    /**
     * 获取部门详情
     * @param menuId
     */
    static getMenuDetailsById(menuId: number | string): Promise<any> {
        return useRequest({
            url: `/api/menu/getMenuByMenuId/${menuId}`,
            method: "get"
        })
    }

    /**
     * 新增部门
     * @param data
     */
    static add(data: any): Promise<any> {
        return useRequest({
            url: "/api/menu/add",
            method: "post",
            data
        })
    }

    /**
     * 更新部门
     * @param data
     */
    static edit(data: any): Promise<any> {
        return useRequest({
            url: "/api/menu/update",
            method: "post",
            data
        })
    }

    /**
     * 删除部门
     * @param menuId
     */
    static deleteById(menuId: number | string): Promise<any> {
        return useRequest({
            url: `/api/menu/delete/${menuId}`,
            method: "post"
        })
    }

    /**
     * 批量删除菜单
     * @param data
     */
    static deleteByIds(data: Array<number | string>): Promise<any> {
        return useRequest({
            url: "/api/menu/delete",
            method: "post",
            data
        })
    }

    /**
     * 更新菜单状态
     * @param data
     */
    static updateStatus(data: any): Promise<any> {
        return useRequest({
            url: "/api/menu/updateStatus",
            method: "post",
            data
        })
    }
}