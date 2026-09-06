import useRequest from "@/api/useRequest";
import {IPage} from "@/interface/modules/system";

export class deptController {
    /**
     * 获取部门列表
     * @param data
     * @param pagination
     */
    static list(data: any, pagination?: IPage): Promise<any> {
        return useRequest({
            url: "/api/dept/list",
            method: "post",
            params: pagination,
            data
        })
    }

    /**
     * 获取部门详情
     * @param deptId
     */
    static getDeptDetailsById(deptId: number | string): Promise<any> {
        return useRequest({
            url: `/api/dept/getDeptByDeptId/${deptId}`,
            method: "get"
        })
    }

    /**
     * 新增部门
     * @param data
     */
    static add(data: any): Promise<any> {
        return useRequest({
            url: "/api/dept/add",
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
            url: "/api/dept/update",
            method: "post",
            data
        })
    }

    /**
     * 删除部门
     * @param deptId
     */
    static deleteById(deptId: number | string): Promise<any> {
        return useRequest({
            url: `/api/dept/delete/${deptId}`,
            method: "post"
        })
    }

    /**
     * 批量删除部门
     * @param data
     */
    static deleteByIds(data: Array<number | string>): Promise<any> {
        return useRequest({
            url: "/api/dept/delete",
            method: "post",
            data
        })
    }

    /**
     * 更新部门状态
     * @param data
     */
    static updateStatus(data: any): Promise<any> {
        return useRequest({
            url: "/api/dept/updateStatus",
            method: "post",
            data
        })
    }

}