import {useStaticDict} from "@/dictionary/useStaticDict";

export const useColumns = () => {
    const {operationType} = useStaticDict()

    const useRenderOperationType = (operation: string) => {
        const temp = operationType.value.filter(type => {
            return type.value === operation
        })
        return temp.length !== 0 ? temp[0].label : undefined;
    }


    return {
        useRenderOperationType
    }
}