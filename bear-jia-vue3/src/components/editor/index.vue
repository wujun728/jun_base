<template>
  <div class="editor">
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
import { onBeforeUnmount, ref, shallowRef, onMounted, watch } from 'vue';
import { Editor, Toolbar } from '@wangeditor/editor-for-vue';
import { message } from 'ant-design-vue';
import { getToken } from '@/utils/auth.js';

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

// 响应式数据
const editorRef = ref(null);
const editor = ref(null);
const currentValue = ref(props.value || '');
const uploadImgUrl = ref(`${import.meta.env.VITE_APP_BASE_API || ''}/common/upload`);
const headers = reactive({
  Authorization: 'Bearer ' + getToken(),
  Accept: 'application/json, text/plain, */*',
});

// 获取 store
const appStore = useAppStore();

// 替换 Vuex store 的使用
const app = computed(() => appStore);

// 监听 value 变化
watch(
    () => props.value,
    (val) => {
      if (val !== currentValue.value) {
        currentValue.value = val === null ? '' : val;
        if (editor.value) {
          editor.value.txt.html(currentValue.value);
        }
      }
    },
    {immediate: true}
);

// 初始化编辑器
const init = () => {
  editor.value = new E(editorRef.value);
  // 代码高亮
  editor.value.highlight = hljs;

  // 高度设置
  if (props.height) {
    editor.value.config.height = props.height;
  }

  // z-index.vue
  editor.value.config.zIndex = 0;

  // 自定义提示信息
  editor.value.config.customAlert = (s, t) => {
    switch (t) {
      case 'success':
        message.success(s);
        break;
      case 'info':
        message.info(s);
        break;
      case 'warning':
        message.warning(s);
        break;
      case 'error':
        message.error(s);
        break;
      default:
        message.info(s);
        break;
    }
  };

  // 图片上传配置
  editor.value.config.uploadImgMaxSize = 1024 * 1024 * props.imageSize;
  editor.value.config.uploadImgServer = uploadImgUrl.value;
  editor.value.config.uploadImgHeaders = headers;
  editor.value.config.uploadFileName = 'file';
  editor.value.config.uploadImgHooks = {
    customInsert: (insertImgFn, result) => {
      insertImgFn(result.url);
    },
  };

  // 视频上传配置
  editor.value.config.uploadVideoMaxSize = 1024 * 1024 * props.videoSize;
  editor.value.config.uploadVideoServer = uploadImgUrl.value;
  editor.value.config.uploadVideoHeaders = headers;
  editor.value.config.uploadVideoName = 'file';
  editor.value.config.uploadVideoHooks = {
    customInsert: (insertVideoFn, result) => {
      insertVideoFn(result.url);
    },
  };

  // 数据双向绑定
  editor.value.config.onchange = (newHtml) => {
    currentValue.value = newHtml;
    emit('input', newHtml);
    emit('update:value', newHtml);
  };

  // 创建编辑器
  editor.value.create();
  editor.value.txt.html(currentValue.value);

  // 只读模式
  if (props.readOnly) {
    editor.value.disable();
  }
};

// 处理上传错误
const handleUploadError = () => {
  message.error('图片插入失败');
};

// 在组件挂载时初始化
onMounted(() => {
  init();
});
</script>

<style lang="less" scoped>
.editor {
  border: 1px solid var(--border-color-base);
  border-radius: 6px;
  overflow: hidden;

  // 编辑器工具栏样式
  :deep(.w-e-toolbar) {
    background: var(--table-header-bg);
    border-bottom: 1px solid var(--border-color-split);

    .w-e-menu {
      &:hover {
        background: var(--hover-bg);
      }

      &.w-e-active {
        background: var(--primary-color);
        color: #fff;
      }
    }
  }

  // 编辑器内容区域样式
  :deep(.w-e-text-container) {
    background: var(--component-background);

    .w-e-text {
      padding: 12px;
      min-height: 200px;
      line-height: 1.6;
      color: var(--text-color) !important;

      // 占位符样式
      &.placeholder::before {
        color: var(--text-disabled);
        font-style: italic;
      }

      // 内容样式
      p {
        margin: 8px 0;
      }

      h1, h2, h3, h4, h5, h6 {
        margin: 16px 0 8px 0;
        font-weight: 600;
      }

      ul, ol {
        margin: 8px 0;
        padding-left: 20px;
      }

      blockquote {
        margin: 16px 0;
        padding: 12px 16px;
        background: var(--hover-bg);
        border-left: 4px solid var(--primary-color);
      }

      code {
        padding: 2px 4px;
        background: var(--hover-bg);
        border-radius: 3px;
        color: var(--primary-color);
      }

      pre {
        margin: 16px 0;
        padding: 12px;
        background: var(--hover-bg);
        border-radius: 6px;
        overflow-x: auto;
        border: 1px solid var(--border-color-split);
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
          border: 1px solid var(--border-color-split);
          text-align: left;
        }

        th {
          background: var(--table-header-bg);
          font-weight: 600;
        }
      }
    }
  }

  // 聚焦状态
  &:focus-within {
    border-color: #1890ff;
    box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
  }
}
</style>
