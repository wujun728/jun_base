<template>
  <div class="wangeditor-wrapper">
    <!-- 工具栏 -->
    <Toolbar
      :editor="editorRef"
      :defaultConfig="toolbarConfig"
      :mode="mode"
    />
    <!-- 编辑器 -->
    <Editor
      :style="{ height: editorHeight + 'px', 'overflow-y': 'hidden' }"
      v-model="valueHtml"
      :defaultConfig="editorConfig"
      :mode="mode"
      @onCreated="handleCreated"
      @onChange="handleChange"
      @onDestroyed="handleDestroyed"
      @onFocus="handleFocus"
      @onBlur="handleBlur"
      @customAlert="handleAlert"
    />
  </div>
</template>

<script setup>
import '@wangeditor/editor/dist/css/style.css';
import { onBeforeUnmount, ref, shallowRef, onMounted, watch, nextTick } from 'vue';
import { Editor, Toolbar } from '@wangeditor/editor-for-vue';
import { message } from 'ant-design-vue';
import { getToken } from '@/utils/auth.js';

// 检测是否是暗黑模式
const isDarkMode = () => {
  return document.body.classList.contains('dark-theme');
};

// 应用暗黑模式样式到编辑器
const applyDarkModeStyles = (editor) => {
  if (!editor) return;

  nextTick(() => {
    try {
      // 获取编辑器 DOM 元素
      const editorDom = editor.getEditableContainer();
      if (editorDom) {
        // 设置编辑区域背景色
        editorDom.style.backgroundColor = isDarkMode() ? 'var(--component-background)' : '';
        editorDom.style.color = isDarkMode() ? 'var(--text-color)' : '';
      }

      // 获取工具栏 DOM 元素
      const toolbarDom = document.querySelector('.w-e-toolbar');
      if (toolbarDom && isDarkMode()) {
        toolbarDom.style.backgroundColor = 'var(--table-header-bg)';
      }
    } catch (e) {
      console.warn('Failed to apply dark mode styles to editor:', e);
    }
  });
};

// 定义 Props
const props = defineProps({
  value: {
    type: String,
    default: '',
  },
  height: {
    type: Number,
    default: 400,
  },
  readOnly: {
    type: Boolean,
    default: false,
  },
  placeholder: {
    type: String,
    default: '请输入内容...',
  },
  imageSize: {
    type: Number,
    default: 5, // MB
  },
  videoSize: {
    type: Number,
    default: 50, // MB
  },
});

// 定义 Emits
const emit = defineEmits(['update:value', 'change']);

// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef();

// 内容 HTML
const valueHtml = ref(props.value || '');

// 编辑器高度
const editorHeight = ref(props.height);

// 模式
const mode = ref('default'); // 或 'simple'

// 工具栏配置
const toolbarConfig = {
  excludeKeys: props.readOnly ? [] : undefined, // 只读模式下排除所有工具
};

// 编辑器配置
const editorConfig = {
  placeholder: props.placeholder,
  readOnly: props.readOnly,
  MENU_CONF: {
    // 配置上传图片
    uploadImage: {
      server: `${import.meta.env.VITE_APP_BASE_API}/common/upload`,
      fieldName: 'file',
      maxFileSize: props.imageSize * 1024 * 1024, // 5M
      maxNumberOfFiles: 10,
      allowedFileTypes: ['image/*'],
      headers: {
        Authorization: 'Bearer ' + getToken(),
      },
      customInsert(res, insertFn) {
        // res 即服务端的返回结果
        if (res.code === 200) {
          // 从 res 中找到 url alt href ，然后插入图片
          insertFn(res.url, res.fileName, res.url);
        } else {
          message.error(res.msg || '图片上传失败');
        }
      },
    },
    // 配置上传视频
    uploadVideo: {
      server: `${import.meta.env.VITE_APP_BASE_API}/common/upload`,
      fieldName: 'file',
      maxFileSize: props.videoSize * 1024 * 1024, // 50M
      maxNumberOfFiles: 3,
      allowedFileTypes: ['video/*'],
      headers: {
        Authorization: 'Bearer ' + getToken(),
      },
      customInsert(res, insertFn) {
        // res 即服务端的返回结果
        if (res.code === 200) {
          // 从 res 中找到 url ，然后插入视频
          insertFn(res.url, res.fileName);
        } else {
          message.error(res.msg || '视频上传失败');
        }
      },
    },
  },
};

// 监听 props.value 变化
watch(
  () => props.value,
  (newVal) => {
    if (newVal !== valueHtml.value) {
      valueHtml.value = newVal || '';
    }
  },
  { immediate: true }
);

// 编辑器回调函数
const handleCreated = (editor) => {
  editorRef.value = editor; // 记录 editor 实例，重要！

  // 应用暗黑模式样式
  applyDarkModeStyles(editor);

  // 监听主题变化
  const observer = new MutationObserver((mutations) => {
    mutations.forEach((mutation) => {
      if (mutation.attributeName === 'class') {
        applyDarkModeStyles(editor);
      }
    });
  });

  observer.observe(document.body, { attributes: true });

  // 保存 observer 以便组件销毁时断开连接
  editor._themeObserver = observer;
};

const handleChange = (editor) => {
  const html = editor.getHtml();
  emit('update:value', html);
  emit('change', html);
};

const handleDestroyed = (editor) => {
  console.log('destroyed', editor);
};

const handleFocus = (editor) => {
  console.log('focus', editor);
};

const handleBlur = (editor) => {
  console.log('blur', editor);
};

const handleAlert = (info, type) => {
  if (type === 'error') {
    message.error(info);
  } else if (type === 'warning') {
    message.warning(info);
  } else {
    message.info(info);
  }
};

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value;
  if (editor == null) return;

  // 断开主题观察器连接
  if (editor._themeObserver) {
    editor._themeObserver.disconnect();
  }

  editor.destroy();
});
</script>

<style lang="less" scoped>
.wangeditor-wrapper {
  border: 1px solid var(--border-color-base);
  border-radius: 6px;
  overflow: hidden;
  background: var(--component-background);
}

// 编辑器整体样式
:deep(.w-e-toolbar) {
  border-bottom: 1px solid var(--border-color-split) !important;
  background: var(--table-header-bg) !important;
  background-color: var(--table-header-bg) !important;

  .w-e-bar-item {
    &:hover {
      background: var(--hover-bg) !important;
    }
  }

  // 工具栏按钮
  .w-e-bar-item button {
    color: var(--text-color) !important;
    background: transparent !important;
    background-color: transparent !important;
  }

  // 工具栏分隔符
  .w-e-bar-item-menus-container {
    background: var(--table-header-bg) !important;
  }

  // 工具栏分隔线
  .w-e-bar-divider {
    background-color: var(--border-color-split) !important;
  }
}

:deep(.w-e-text-container) {
  background: var(--component-background) !important;
  background-color: var(--component-background) !important;

  // 编辑区域滚动容器
  .w-e-scroll {
    background: var(--component-background) !important;
    background-color: var(--component-background) !important;
  }

  // 编辑区域
  [data-slate-editor],
  [data-slate-editor="true"] {
    background: var(--component-background) !important;
    background-color: var(--component-background) !important;
    color: var(--text-color) !important;
  }

  // 占位符
  .w-e-text-placeholder {
    color: var(--text-disabled) !important;
  }

  p, span, div {
    color: var(--text-color) !important;
  }

  h1, h2, h3, h4, h5, h6 {
    margin: 16px 0 8px 0;
    font-weight: 600;
    color: var(--text-color) !important;
  }

  ul, ol {
    margin: 8px 0;
    padding-left: 20px;
    color: var(--text-color) !important;
  }

  blockquote {
    margin: 16px 0;
    padding: 12px 16px;
    background: var(--hover-bg) !important;
    border-left: 4px solid var(--primary-color) !important;
    color: var(--text-color) !important;
  }

  code {
    padding: 2px 4px;
    background: var(--hover-bg) !important;
    border-radius: 3px;
    color: var(--primary-color) !important;
  }

  pre {
    margin: 16px 0;
    padding: 12px;
    background: var(--hover-bg) !important;
    border-radius: 6px;
    overflow-x: auto;
    border: 1px solid var(--border-color-split) !important;
    color: var(--text-color) !important;
  }

  img {
    max-width: 100%;
    height: auto;
    border-radius: 4px;
    margin: 8px 0;
  }

  video {
    max-width: 100%;
    height: auto;
    border-radius: 4px;
    margin: 8px 0;
  }

  table {
    width: 100%;
    border-collapse: collapse;
    margin: 16px 0;

    th, td {
      padding: 8px 12px;
      border: 1px solid var(--border-color-split) !important;
      text-align: left;
      color: var(--text-color) !important;
    }

    th {
      background: var(--table-header-bg) !important;
      font-weight: 600;
    }
  }
}

// 暗黑模式特殊处理 - 确保组件级别覆盖
:global(.dark-theme) {
  .wangeditor-wrapper {
    background: var(--component-background) !important;
    border-color: var(--border-color-base) !important;
  }

  :deep(.w-e-toolbar) {
    background: var(--table-header-bg) !important;
    background-color: var(--table-header-bg) !important;
    border-bottom-color: var(--border-color-split) !important;
  }

  :deep(.w-e-text-container) {
    background: var(--component-background) !important;
    background-color: var(--component-background) !important;

    .w-e-scroll,
    [data-slate-editor],
    [data-slate-editor="true"] {
      background: var(--component-background) !important;
      background-color: var(--component-background) !important;
    }
  }
}
</style>

<style lang="less">
// ========== WangEditor 暗黑模式全局样式 ==========
// 必须使用全局样式来覆盖 WangEditor 的默认样式和弹出层样式
.dark-theme {
  // ========== 工具栏样式 ==========
  .w-e-toolbar {
    background-color: var(--table-header-bg) !important;
    border-color: var(--border-color-split) !important;
  }

  .w-e-bar {
    background-color: var(--table-header-bg) !important;
    border-color: var(--border-color-split) !important;
  }

  // 工具栏按钮
  .w-e-bar-item {
    button {
      color: var(--text-color) !important;
    }

    &:hover {
      background-color: var(--hover-bg) !important;
    }
  }

  // 工具栏分隔符
  .w-e-bar-divider {
    background-color: var(--border-color-split) !important;
  }

  // 工具栏下拉菜单触发器
  .w-e-bar-item-group .w-e-bar-item-menus-container {
    background-color: var(--dropdown-bg) !important;
    border-color: var(--border-color-base) !important;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3) !important;
  }

  // 工具栏选择器菜单
  .w-e-select-list {
    background-color: var(--dropdown-bg) !important;
    border-color: var(--border-color-base) !important;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3) !important;

    .w-e-select-list-item {
      color: var(--text-color) !important;

      &:hover {
        background-color: var(--hover-bg) !important;
      }

      &.selected {
        background-color: var(--primary-color) !important;
        color: #fff !important;
      }
    }
  }

  // ========== 编辑区域样式 ==========
  .w-e-text-container {
    background-color: var(--component-background) !important;
    border-color: var(--border-color-split) !important;
  }

  // 编辑区域内容
  .w-e-text-container [data-slate-editor="true"] {
    background-color: var(--component-background) !important;
    color: var(--text-color) !important;
  }

  // 占位符文本
  .w-e-text-placeholder {
    color: var(--text-disabled) !important;
  }

  // ========== 下拉菜单和弹出层 ==========
  .w-e-drop-panel {
    background-color: var(--dropdown-bg) !important;
    border-color: var(--border-color-base) !important;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3) !important;

    // 面板内的输入框
    input {
      background-color: var(--input-bg) !important;
      border-color: var(--input-border) !important;
      color: var(--text-color) !important;

      &::placeholder {
        color: var(--text-disabled) !important;
      }

      &:focus {
        border-color: var(--primary-color) !important;
      }
    }

    // 面板内的按钮
    button {
      background-color: var(--primary-color) !important;
      border-color: var(--primary-color) !important;
      color: #fff !important;

      &:hover {
        background-color: var(--primary-color-hover) !important;
        border-color: var(--primary-color-hover) !important;
      }
    }

    // 面板标签
    label {
      color: var(--text-color) !important;
    }

    // 面板标题
    .title {
      color: var(--text-color) !important;
    }
  }

  // ========== 模态框 ==========
  .w-e-modal {
    background-color: var(--modal-bg) !important;
    border-color: var(--modal-border) !important;
    box-shadow: var(--modal-shadow) !important;

    // 模态框头部
    .w-e-modal-header {
      background-color: var(--modal-bg-elevated) !important;
      border-color: var(--border-color-split) !important;
      color: var(--text-color) !important;
    }

    // 模态框标题
    .title {
      color: var(--text-color) !important;
    }

    // 关闭按钮
    .close-btn {
      color: var(--text-secondary) !important;

      &:hover {
        color: var(--text-color) !important;
      }
    }

    // 模态框内容
    .w-e-modal-content {
      background-color: var(--modal-bg) !important;
      color: var(--text-color) !important;

      // 标签
      label {
        color: var(--text-color) !important;
      }

      // 输入框
      input, textarea {
        background-color: var(--modal-input-bg) !important;
        border-color: var(--input-border) !important;
        color: var(--text-color) !important;

        &::placeholder {
          color: var(--text-disabled) !important;
        }

        &:focus {
          border-color: var(--primary-color) !important;
        }
      }

      // 选择框
      select {
        background-color: var(--modal-input-bg) !important;
        border-color: var(--input-border) !important;
        color: var(--text-color) !important;
      }
    }

    // 模态框底部
    .w-e-modal-footer {
      border-color: var(--border-color-split) !important;

      button {
        background-color: var(--primary-color) !important;
        border-color: var(--primary-color) !important;
        color: #fff !important;

        &:hover {
          background-color: var(--primary-color-hover) !important;
          border-color: var(--primary-color-hover) !important;
        }

        &.cancel {
          background-color: transparent !important;
          border-color: var(--border-color-base) !important;
          color: var(--text-color) !important;

          &:hover {
            border-color: var(--primary-color) !important;
            color: var(--primary-color) !important;
          }
        }
      }
    }
  }

  // ========== 悬浮工具栏（选中文字后弹出）==========
  .w-e-hover-bar {
    background-color: var(--dropdown-bg) !important;
    border-color: var(--border-color-base) !important;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3) !important;

    .w-e-bar-item {
      button {
        color: var(--text-color) !important;
      }

      &:hover {
        background-color: var(--hover-bg) !important;
      }
    }
  }

  // ========== 表格相关 ==========
  .w-e-text-container {
    table {
      border-color: var(--border-color-split) !important;

      td, th {
        border-color: var(--border-color-split) !important;
        background-color: var(--component-background) !important;
        color: var(--text-color) !important;
      }

      th {
        background-color: var(--table-header-bg) !important;
      }
    }
  }

  // ========== 代码块 ==========
  .w-e-text-container {
    pre {
      background-color: var(--hover-bg) !important;
      border-color: var(--border-color-split) !important;

      code {
        color: var(--text-color) !important;
        background-color: transparent !important;
      }
    }
  }

  // ========== 链接 ==========
  .w-e-text-container {
    a {
      color: var(--primary-color) !important;

      &:hover {
        color: var(--primary-color-hover) !important;
      }
    }
  }

  // ========== 引用块 ==========
  .w-e-text-container {
    blockquote {
      background-color: var(--hover-bg) !important;
      border-left-color: var(--primary-color) !important;
      color: var(--text-color) !important;
    }
  }

  // ========== 列表 ==========
  .w-e-text-container {
    ul, ol {
      color: var(--text-color) !important;
    }
  }

  // ========== 分割线 ==========
  .w-e-text-container {
    hr {
      border-color: var(--border-color-split) !important;
    }
  }

  // ========== 全屏模式 ==========
  .w-e-full-screen-container {
    background-color: var(--bg-color) !important;

    .w-e-toolbar {
      background-color: var(--table-header-bg) !important;
    }

    .w-e-text-container {
      background-color: var(--component-background) !important;
    }
  }

  // ========== 颜色选择器 ==========
  .w-e-panel-content-color {
    background-color: var(--dropdown-bg) !important;
    border-color: var(--border-color-base) !important;

    .color-block {
      border-color: var(--border-color-split) !important;
    }

    .clear-color {
      color: var(--text-color) !important;
      background-color: var(--hover-bg) !important;

      &:hover {
        background-color: var(--active-bg) !important;
      }
    }
  }

  // ========== 上传区域 ==========
  .w-e-panel-content-upload {
    background-color: var(--dropdown-bg) !important;

    .upload-btn {
      background-color: var(--hover-bg) !important;
      border-color: var(--border-color-base) !important;
      color: var(--text-color) !important;

      &:hover {
        border-color: var(--primary-color) !important;
        color: var(--primary-color) !important;
      }
    }
  }

  // ========== 工具提示 ==========
  .w-e-tooltip {
    background-color: var(--dropdown-bg) !important;
    color: var(--text-color) !important;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3) !important;

    &::after {
      border-top-color: var(--dropdown-bg) !important;
    }
  }

  // ========== SVG 图标颜色 ==========
  .w-e-bar-item svg,
  .w-e-toolbar svg,
  .w-e-hover-bar svg {
    fill: var(--text-color) !important;
  }

  // 活跃状态的图标
  .w-e-bar-item.w-e-bar-item-active svg {
    fill: var(--primary-color) !important;
  }
}
</style>
