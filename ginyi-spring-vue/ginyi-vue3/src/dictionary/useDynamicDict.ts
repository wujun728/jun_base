import {deptController, menuController, postController, roleController} from "@/api";
import {useRemoveEmptyChildrenField} from "@/hooks/useTree";
import {ref} from "vue";


export const useDynamicDict = () => {

    // 部门字典
    const deptDict = ref<Array<any>>([]);
    // 岗位字典
    const postDict = ref<Array<any>>([]);
    // 角色字典
    const roleDict = ref<Array<any>>([]);
    // 菜单字典
    const menuDict = ref<Array<any>>([]);

    /**
     * 获取部门字典
     */
    (async () => {
        if (deptDict.value.length === 0) {
            const {data} = await deptController.list({})
            deptDict.value = useRemoveEmptyChildrenField(data.list)
        }
    })();

    /**
     * 获取角色字典
     */
    (async () => {
        if (roleDict.value.length === 0) {
            const {data} = await roleController.list({})
            roleDict.value = data.list
        }
    })();

    /**
     * 获取岗位字典
     */
    (async () => {
        if (postDict.value.length === 0) {
            const {data} = await postController.list({})
            postDict.value = data.list
        }
    })();

    /**
     * 获取菜单字典
     */
    (async () => {
        if (menuDict.value.length === 0) {
            const {data} = await menuController.list({filterButton: "0"})
            menuDict.value = useRemoveEmptyChildrenField(data.list)
        }
    })();


    return {
        deptDict,
        postDict,
        roleDict,
        menuDict
    }
}

