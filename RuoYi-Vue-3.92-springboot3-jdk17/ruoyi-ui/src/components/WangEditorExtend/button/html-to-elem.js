/**
 * 解析 HTML 字符串，生成“附件”元素
 * @param domElem HTML 对应的 DOM Element
 * @param children 子节点
 * @param editor editor 实例
 * @returns “附件”元素，如上文的 myResume
 */
function parseAttachmentHtml(domElem, children, editor) {

    // 从 DOM element 中获取“附件”的信息
    const link = domElem.getAttribute('data-link') || ''
    const fileName = domElem.getAttribute('data-fileName') || ''

    // 生成“附件”元素
    const myResume = {
        type: 'attachment',
        link,
        fileName,
        children: [{ text: '' }], // void node 必须有 children ，其中有一个空字符串
    }

    return myResume
}

const parseHtmlConf = {
    selector: 'span[data-w-e-type="attachment"]', // CSS 选择器，匹配特定的 HTML 标签
    parseElemHtml: parseAttachmentHtml,
}

export default parseHtmlConf