import {App} from 'vue';
import {focus} from "@/directives/focus";
import {loading} from "@/directives/loading";
import {drag} from "@/directives/draggable"

/**
 * 注册全局自定义指令
 * 注意：需要在web-types.json声明一下，避免编译器警告
 * @param app
 */
export const initDirectives = (app: App) => {
    // 自动获取焦点
    app.directive('focus', focus);
    app.directive('loading', loading);
    app.directive('drag', drag);
}
