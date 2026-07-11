import { Boot } from "@wangeditor/editor";
import UploadFile from "@/components/WangEditorExtend/button/upload-file.js";
import renderElemConf from "@/components/WangEditorExtend/button/render-elem.js";
import elemToHtmlConf from "@/components/WangEditorExtend/button/elem-to-html.js";
import parseHtmlConf from "@/components/WangEditorExtend/button/html-to-elem.js";
import withAttachment from "@/components/WangEditorExtend/button/with-attachment.js";

export default {

    state: {
        registerStatus: false,
    },

    mutations: {
        SET_REGISTER_STATUS(state, registerStatus) {
            state.registerStatus = registerStatus;
        }
    },

    actions: {
        registerModule({ commit, state }) {
            if (state.registerStatus) return;
            const menuConf = {
                key: "custom-uplod-file", // 定义唯一的 menu key
                factory: () => new UploadFile(this), // 使用箭头函数以避免 `this` 问题
            };
            const module = {
                editorPlugin: withAttachment, // 使用 withAttachment 包装 editor
                menus: [menuConf],
                renderElems: [renderElemConf],
                elemsToHtml: [elemToHtmlConf],
                parseElemsHtml: [parseHtmlConf],
                // 其他配置项...
            };
            Boot.registerModule(module);
            commit('SET_REGISTER_STATUS', true);
        }
    }

}