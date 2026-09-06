export const definedProps = {
    /**
     * 模型标题
     */
    title: {
        type: String,
        default: "标题"
    },
    /**
     * 模型预设
     */
    preset: {
        type: String,
        default: "dialog"
    },
    /**
     * 确定按钮文本内容
     */
    positiveText: {
        type: String,
        default: "确认"
    },
    /**
     * 取消按钮文本内容
     */
    negativeText: {
        type: String,
        default: "取消"
    },
    show: {
        type: Boolean,
        default: false
    }
}