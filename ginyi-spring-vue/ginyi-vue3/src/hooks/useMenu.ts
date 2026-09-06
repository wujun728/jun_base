import {useLoadIcon, useRenderIcon} from "@/plugins/naive-ui/common";

/**
 * 重新格式菜单列表，主要是为了渲染 icon 和 删除空children
 * @param menuList
 * @param formatIcon 是否需要处理icon
 */
export const useMenuFormat = (menuList: any, formatIcon?: boolean): any => {
    return menuList.map((menu: any) => {
        if (formatIcon) {
            menu.icon = useRenderIcon(useLoadIcon(menu.icon))
        }
        if (menu.children?.length > 0) {
            useMenuFormat(menu.children, formatIcon)
        } else {
            delete menu.children
        }
        return menu
    })
}