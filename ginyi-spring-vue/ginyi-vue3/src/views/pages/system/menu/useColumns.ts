import {useStaticDict} from "@/dictionary/useStaticDict";

export const useColumns = () => {
    const {menuTypeDict} = useStaticDict()

    const useRenderMenuType = (menuType: string) => {
        const temp = menuTypeDict.value.filter(type => {
            return type.value === menuType
        })
        return temp.length !== 0 ? temp[0].label : undefined;
    }


    return {
        useRenderMenuType
    }
}