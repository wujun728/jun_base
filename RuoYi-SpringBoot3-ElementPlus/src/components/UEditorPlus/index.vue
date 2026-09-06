<template>
  <div class="u-editor-content" style="width: 100%">
    <vue-ueditor-wrap
      @ready="ready"
      :editor-id="props.id"
      :config="editorConfig"
      :editorDependencies="['ueditor.config.js', 'ueditor.all.js']"
    />
  </div>
</template>
<script setup>
window.UEDITOR_HOME_URL = "/admin/UEditorPlus/";
import { VueUeditorWrap } from "vue-ueditor-wrap";
import { getToken } from "../../utils/auth";

const props = defineProps({
  modelValue: {
    type: String,
    default: "",
  },
  placeholder: {
    type: String,
    default: "",
  },
  // serverUrl 服务器上传地址
  serverUrl: {
    type: String,
    default: import.meta.env.VITE_APP_BASE_API + "/ueditor/controller",
  },

  // 必须得有，如果要在一个页面内写两个或以上的组件，需要用id去区分
  id: {
    type: String,
    default: "",
  },
});

const { proxy } = getCurrentInstance();
const emits = defineEmits();

function ready(editor) {
  if (props.modelValue) {
    editor.setContent(props.modelValue);
  } else if (props.placeholder) {
    editor.setContent('<p style="color:#a8abb2">' + props.placeholder + "</p>");
  }

  function emitModelValue() {
    emits(
      "update:modelValue",
      editor.getPlainTxt() == props.placeholder + "\n"
        ? ""
        : editor.getContent()
    );
  }

  editor.addListener("focus", function () {
    if (editor.getPlainTxt() == props.placeholder + "\n") {
      editor.setContent("");
    }
  });
  editor.addListener("contentchange", emitModelValue);
}

const editorConfig = {
  zIndex: 999999999,
  // 图片限制最大10M
  imageMaxSize: 10485760,
  // 富文本输入框高度
  initialFrameHeight: 500,
  // 富文本输入框宽度
  initialFrameWidth: "100%",
  // 初始化样式 编辑区自定义样式，如果自定义，最好给 p 标签如下的行高，要不输入中文时，会有跳动感
  initialStyle:
    "body p{line-height:1.8em; margin: 0 ;} h1,h2,h3,h4,blockquote{margin: 0 ;} body table{margin: 0 ;}",
  autoFloatEnabled: false,
  // 获取上传配置路径
  // configUrl: import.meta.env.VITE_APP_BASE_API + '/UEditor/',
  // 上传服务路径
  serverUrl: props.serverUrl,
  // 必须配置域名,否则发版后获取不到页面资源
  UEDITOR_HOME_URL: location.origin + "/admin/UEditorPlus/",
  // 配置请求头token
  serverHeaders: {
    // Authorization: `Bearer + ${getToken()}`,
    // 看具体情况需不需要token
    // Token: getToken(),
  },
  // 上传图片配置
  imageConfig: {
    // 禁止在线管理
    disableOnline: true,
  },
};
</script>

<style lang="scss" scoped>
.u-editor-content {
  :deep(.edui-default) {
    .edui-editor {
      border-top: none;
    }
  }

  :deep(.edui-editor-toolbarbox) {
    position: sticky;
    top: 0;
    background-color: #fff;
    z-index: 1100;
    border-top: 1px solid #eee;
  }

  :deep(body) {
    margin: 0 !important;

    * {
      margin: 0;
    }
  }
}
</style>
