import { showDialog } from './dialog';

export default class UploadFile {
  constructor(vueInstance) {
    this.vueInstance = vueInstance;
    this.title = "上传文件"; // 自定义菜单标题
    this.iconSvg = `<svg t="1749520165624" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1505" width="200" height="200"><path d="M857.6 956.8H166.4c-54.4 0-102.4-48-102.4-105.6v-182.4c0-19.2 12.8-32 32-32s32 12.8 32 32v182.4c0 22.4 16 41.6 38.4 41.6h694.4c19.2 0 38.4-19.2 38.4-41.6v-182.4c0-19.2 12.8-32 32-32s32 12.8 32 32v182.4c-3.2 57.6-48 105.6-105.6 105.6z" fill="#333333" p-id="1506"></path><path d="M512 764.8c-19.2 0-32-12.8-32-32v-640c0-19.2 12.8-32 32-32s32 12.8 32 32v640c0 19.2-12.8 32-32 32z" fill="#333333" p-id="1507"></path><path d="M720 326.4c-9.6 0-16-3.2-22.4-9.6L512 131.2l-185.6 185.6c-12.8 12.8-32 12.8-44.8 0s-12.8-32 0-44.8L489.6 64c12.8-12.8 32-12.8 44.8 0l208 208c12.8 12.8 12.8 32 0 44.8-6.4 6.4-12.8 9.6-22.4 9.6z" fill="#333333" p-id="1508"></path></svg>`;// 可选 菜单图标
    this.tag = "button"; // 自定义菜单的 HTML 标签
  }
  // 获取菜单执行时的 value，返回空字符串或 false
  getValue(editor) {
    return ""; // 默认返回空
  }

  // 菜单是否需要激活
  isActive(editor) {
    return false;  
  }

  // 菜单是否需要禁用 
  isDisabled(editor) {
    return false; // 默认不禁用
  }

  // 点击菜单时触发的函数
  exec(editor) {
    if (this.isDisabled(editor)) return;

    // 调用 Element UI 弹窗
    showDialog(
      { editor },
      (uploadedFile) => {
        const fileUrl = uploadedFile.downloadUrl;
        const fileList = uploadedFile.fileList;
        let nodes = [];

        fileList.forEach((item) => {
          const node = {
            type: 'attachment',
            link: fileUrl + item.fileId,
            fileName: item.name,
            children: [{ text: '' }]  // void 元素必须有一个 children ，其中只有一个空字符串，重要！！！
              }
          nodes.push(node)
        });
        editor.insertNode(nodes); // 插入结构化节点
      }
    );
  }
}