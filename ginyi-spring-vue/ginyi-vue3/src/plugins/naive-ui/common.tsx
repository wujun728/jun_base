import type {App} from "vue";
import {Component, computed, createVNode} from "vue";
import {createDiscreteApi, NIcon} from "naive-ui";
import * as Icons from "@vicons/ionicons5";
import {useSystemStore} from "@/store/modules/useSystemStore";
import {storeToRefs} from "pinia";
import {useLighten} from "@/hooks/useColor";

/**
 * 加延时是为了让 pinia 完成初始化，时间可适当调长些
 */
setTimeout(() => {
    const {getTheme, themeColor} = storeToRefs(useSystemStore())
    const configProviderPropsRef = computed(() => ({
        theme: getTheme.value,
        themeOverrides: {
            common: {
                primaryColor: themeColor?.value,
                primaryColorHover: useLighten(themeColor?.value as string, 6),
                primaryColorPressed: useLighten(themeColor?.value as string, 6),
            },
            LoadingBar: {
                colorLoading: themeColor?.value as string,
            },
        },
    }));
    const {message, dialog, notification, loadingBar} = createDiscreteApi(
        ["message", "dialog", "notification", "loadingBar"],
        {
            configProviderProps: configProviderPropsRef,
        }
    );

    window.$message = message;
    window.$dialog = dialog;
    window.$notification = notification;
    window.$loading = loadingBar;
}, 100)


/**
 * 加载图标，要配合【 renderIcon 】一起使用
 * @param icon
 */
export const useLoadIcon = (icon: string) => {
    return Icons[icon as keyof typeof Icons];
}

/**
 * 渲染icon，不一定要配合【 loadIcon 】，可以单独使用
 * @param icon
 */
export const useRenderIcon = (icon: Component) => {
    return () => {
        return <NIcon component={icon}/>
    }
}

const icon = (props: { icon: string }) => {
    const {icon} = props;
    return createVNode(Icons[icon as keyof typeof Icons]);
};

export function initIcon(app: App<Element>): void {
    app.component('Icon', icon);
}