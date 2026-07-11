import Vue from 'vue';
import CustomUploadDialog from './customUploadDialog.vue';

/**
 * 使用 Element UI 的 el-dialog 显示弹窗
 */
export const showDialog = (props, onConfirm) => {
    const wrapper = document.createElement('div');
    document.body.appendChild(wrapper);

    const app = new Vue({
        render(h) {
            return h(CustomUploadDialog, {
                props: {
                    editor: props.editor
                },
                on: {
                    confirm(file) {
                        onConfirm(file);
                        app.$destroy();
                        wrapper.remove();
                    },
                    close() {
                        app.$destroy();
                        wrapper.remove();
                    }
                }
            });
        }
    });

    app.$mount(wrapper);
};
