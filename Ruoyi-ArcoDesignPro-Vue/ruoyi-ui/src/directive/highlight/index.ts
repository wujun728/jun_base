import hljs from 'highlight.js' // 导入代码高亮文件
import 'highlight.js/styles/vs2015.css'  // 导入代码高亮样式
import {DirectiveBinding} from 'vue';

function doHighlightBlock(el: HTMLElement, binding: DirectiveBinding) {
  const blocks = el.querySelectorAll('pre code');
  blocks.forEach((block: any) => {
    hljs.highlightBlock(block)
  })
}

export default {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    doHighlightBlock(el, binding);
  },
  updated(el: HTMLElement, binding: DirectiveBinding) {
    doHighlightBlock(el, binding);
  },
};
