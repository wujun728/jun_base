import {useStaticDict} from "@/dictionary/useStaticDict";
import {useDynamicDict} from "@/dictionary/useDynamicDict";
import {useArraySeparator, useFindParentNodes, useTreeToArray} from "@/hooks/useTree";

export const useColumns = () => {
    const {statusDict, sexDict} = useStaticDict()
    const {deptDict, roleDict, postDict} = useDynamicDict()

    const useRenderSexById = (sexId: string) => {
        const temp = sexDict.value.filter(sex => {
            return sex.value === sexId
        })
        return temp.length !== 0 ? temp[0].label : undefined;
    }

    const useRenderDeptNameById = (deptId: string | number) => {
        const dataList = useFindParentNodes("deptId", deptId, "children", deptDict.value)
        return useArraySeparator(dataList as Array<any>, "deptName")
    }

    const useRenderRoleNameByIds = (roleIds: Array<string | number>) => {
        const temp = roleDict.value.filter(role => {
            return roleIds.includes(role.roleId)
        })
        if (temp.length !== 0) {
            return temp.map(role => role.roleName)
        }
    }

    const useRenderPostNameByIds = (postIds: Array<string | number>) => {
        const temp = postDict.value.filter(post => {
            return postIds.includes(post.postId)
        })
        if (temp.length !== 0) {
            return temp.map(post => post.postName)
        }
    }


    return {
        useRenderSexById,
        useRenderDeptNameById,
        useRenderRoleNameByIds,
        useRenderPostNameByIds
    }
}