import {useStaticDict} from "@/dictionary/useStaticDict";

export const useCommonColumns = () => {
    const {statusDict, successDict} = useStaticDict()


    /**
     * 渲染状态字典
     * @param stateId
     */
    const useRenderStateById = (stateId: string) => {
        const temp = statusDict.value.filter(state => {
            return state.value === stateId
        })
        return temp.length !== 0 ? temp[0].label : undefined;
    }

    /**
     * 渲染成功字典
     * @param successId
     */
    const useRenderSuccessById = (successId: string) => {
        const temp = successDict.value.filter(state => {
            return state.value === successId.toString()
        })
        return temp.length !== 0 ? temp[0].label : undefined;
    }

    return {
        useRenderStateById,
        useRenderSuccessById
    }
}