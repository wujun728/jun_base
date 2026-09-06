import {Directive, DirectiveBinding} from "vue";

/**
 * 自动获取输入框焦点
 * 同个页面使用多个v-focus，最后一个生效
 */
export const focus: Directive = {
    mounted(el: HTMLElement, binding: DirectiveBinding) {

        /**
         * 原生输入框 v-focus.native
         */
        if (binding.modifiers?.native) {
            el.focus()
            return;
        }
        /**
         * n-input  v-focus
         */
        const input = el?.childNodes[0]?.childNodes[1]?.childNodes[0];
        // @ts-ignore
        input.focus();
    }
}