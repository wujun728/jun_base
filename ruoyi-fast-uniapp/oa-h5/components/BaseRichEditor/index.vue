<!-- 使用本组件之前请使用 BaseContainer 容器包裹整个页面，为编辑器提供相关CSS变量 -->
<!-- 编辑器默认宽度100%，高度使用flex: 1自动填充，可以通过传入 style 或者 class 来修改默认样式 -->
<!-- CSS 需要用 :deep 来进行样式穿透， 如 :deep(.editor-custom) .... 这样 -->
 <!-- 如果需要传递富文本内容，请使用 v-if 进行判断，当数据获取完成后再渲染编辑器，否则编辑器在初始化之后再设置内容会造成自动 focus -->
<template>
  <view :id="randomEditorID" class="editor-container" :viewMethodCallJson="viewMethodCallJson"
    :change:viewMethodCallJson="hybird.viewMethodCall">
    <view class="editor-inner-container" :style="style" :class="class">
      <view class="editor-second-container">
        <view class="editor-toolbar"></view>
        <view class="editor"></view>
      </view>
    </view>
  </view>
  <ToolBar v-show="toolbarVisible && !readOnly" @save="handleSaveBtnClick" />
</template>

<script lang="ts">
import { ref, watch } from 'vue';
import ToolBar from './components/ToolBar.vue';
import { CommandType, EDITOR_BLUR, EDITOR_FOCUS, EDITOR_RUN_COMMAND } from './constant';
import { useEvent } from './hooks/common';

export default {
  components: {
    ToolBar
  },
  props: {
    content: {
      type: String,
      default: ""
    },
    style: {
      type: Object,
      default: () => ({})
    },
    class: {
      type: String,
      default: ""
    },
    placeholder: {
      type: String,
      default: ""
    },
    toolbarVisible: {
      type: Boolean,
      default: true
    },
    readOnly: {
      type: Boolean,
      default: false
    }
  },
  emits: {
    change: (value: string) => true,
    save: () => true
  },
  setup(props) {
    const { content, placeholder, readOnly } = toRefs(props);

    // 页面存在多个editor时，需要使用不同的id来区分
    const randomEditorID = "editor-" + Math.random().toString(36).substring(2, 15);

    const viewMethodCallJson = ref<string>(null);

    const handleCallViewMethod = (method: string, args: any) => {
      const randomNethodPrefix = Math.random().toString(36).substring(2, 15);
      viewMethodCallJson.value = JSON.stringify({
        method: `${randomNethodPrefix}-${method}`,
        args
      });
    }

    const handleSaveBtnClick = () => {
      handleCallViewMethod("emitContentChange", null);
    }

    const handleRunEditorCommand = (command: CommandType, ...args: any[]) => {
      // @ts-ignore: 这里通过接收子组件发送的命令和命令参数在编辑器上统一执行命令
      switch (command) {
        case CommandType.FONT_SIZE:
          handleCallViewMethod("setFontSize", args);
          break;
        case CommandType.FONT_STYLE:
          handleCallViewMethod("setFontStyle", args);
          break;
        case CommandType.FONT_COLOR:
          handleCallViewMethod("setFontColor", args);
          break;
        case CommandType.HIGHLIGHT_COLOR:
          handleCallViewMethod("setHighlightColor", args);
          break;
        case CommandType.HEADER_LEVEL:
          handleCallViewMethod("setHeaderLevel", args);
          break;
        case CommandType.REDO:
          handleCallViewMethod("handleRedo", args);
          break;
        case CommandType.UNDO:
          handleCallViewMethod("handleUndo", args);
          break;
        case CommandType.ALIGN:
          handleCallViewMethod("setAlign", args);
          break;
        case CommandType.LIST:
          handleCallViewMethod("setList", args);
          break;
        case CommandType.INSERT_IMAGE:
          handleCallViewMethod("insertImage", args);
          break;
      }
    };

    const handleEditorFocus = (e: Event) => {
      uni.$emit(EDITOR_FOCUS, e);
    };

    const handleEditorBlur = (e: Event) => {
      uni.$emit(EDITOR_BLUR, e);
    };

    // 在数据层调用初始化方法，以便传递初始化参数
    onMounted(() => {
      handleCallViewMethod("initEditor", {
        randomEditorID,
        placeholder: placeholder.value,
        content: content.value,
        readOnly: readOnly.value
      });
    });

    onUnmounted(() => {
      handleCallViewMethod("destroyEditor", null);
    });

    useEvent(EDITOR_RUN_COMMAND, handleRunEditorCommand);

    return {
      viewMethodCallJson,
      randomEditorID,

      handleRunEditorCommand,
      handleEditorFocus,
      handleEditorBlur,
      handleSaveBtnClick
    };
  },
  methods: {
    handleHybirdCall({ method, options }: { method: string, options?: any }) {
      this[method]?.(options);
    },
    handleContentChange(content: string) {
      this.$emit("change", content);
      this.$nextTick(() => {
        this.$emit("save");
      });
    }
  }
}



</script>

<script lang="renderjs" module="hybird">

const loadLib = () => {
  return new Promise((resolve, reject) => {
    if (window.wangEditor) return resolve(null);
    const script = document.createElement("script");
    script.src = "https://registry.npmmirror.com/@wangeditor/editor/5.1.23/files/dist/index.js";
    script.onload = () => resolve(null);
      script.onerror = () => reject(new Error("Failed to load wangEditor"));
      document.head.appendChild(script);

      const link = document.createElement("link");
      link.rel = "stylesheet";
      link.href = "https://registry.npmmirror.com/@wangeditor/editor/5.1.23/files/dist/css/style.css";
    document.head.appendChild(link);
  })
}

// @ts-ignore
export default {
  data() {
    return {
      hybirdData: {
        editor: null,
        placeholder: " ",
        content: "",
        isMounted: false,
        randomEditorID: null,
        readOnly: false
      },
    };
  },
  created() {
    this.loadTask = loadLib();
  },
  methods: {
    initEditor(options) {
      if (this.$editor) return;
      const { randomEditorID, placeholder, content, readOnly } = options;
      this.hybirdData.randomEditorID = randomEditorID;
      this.hybirdData.placeholder = placeholder;
      this.hybirdData.content = content;
      this.hybirdData.readOnly = readOnly;
      this.loadTask.then(() => {
        const { createEditor, createToolbar } = window.wangEditor;

        const editorConfig = {
          placeholder,
          html: content,
          autoFocus: false,
          readOnly,
          onFocus: () => {
            this.callMethod("handleEditorFocus");
          },
          onBlur: () => {
            this.callMethod("handleEditorBlur");
          }
        }
        this.$editor = createEditor({
          selector: `#${randomEditorID} .editor`,
          html: this.hybirdData.content,
          config: editorConfig,
          mode: 'default',
        });

        this.$toolbar = createToolbar({
          selector: `#${randomEditorID} .editor-toolbar`,
          editor: this.$editor,
          mode: 'default',
          config: {
            toolbarKeys: [
              "bold",
              "underline",
              "italic",
              "through",
              "headerSelect",
              "fontSize",
              "color",
              "bgColor",
              "justifyJustify",
              "justifyLeft",
              "justifyCenter",
              "justifyRight",
              "numberedList",
              "bulletedList",
              "todo",
              "insertImage",
              "undo",
              "redo"
            ]
          }
        });
      });
    },
    callMethod(method, options) {
      this.$ownerInstance.callMethod("handleHybirdCall", {
        method,
        options
      });
    },
    viewMethodCall(json) {
      if (!json) return;
      const { method: randomMethod, args } = JSON.parse(json);
      const method = randomMethod.split("-")[1];
      this[method]?.(args);
    },
    setFontSize([fontSize]) {
      const toolbar = this.$toolbar;
      const editor = this.$editor;
      editor.focus();
      toolbar.menus.fontSize.exec(editor, fontSize + "px");
    },
    setFontStyle([fontStyle, isRemove]) {
      const toolbar = this.$toolbar;
      const editor = this.$editor;
      editor.focus();
      const value = toolbar.menus[fontStyle].getValue(editor);
      toolbar.menus[fontStyle].exec(editor, value);
    },
    setFontColor([fontColor]) {
      const isRemove = !fontColor;
      const editor = this.$editor;
      editor.focus();
      if (isRemove) {
        editor.removeMark("color");
      } else {
        editor.addMark("color", fontColor);
      }
    },
    setHighlightColor([highlightColor]) {
      const isRemove = !highlightColor;
      const editor = this.$editor;
      editor.focus();
      if (isRemove) {
        editor.removeMark("bgColor");
      } else {
        editor.addMark("bgColor", highlightColor);
      }
    },
    setHeaderLevel([headerLevel]) {
      const editor = this.$editor;
      editor.focus();
      wangEditor.SlateTransforms.setNodes(editor, {
        type: headerLevel,
      });
    },
    handleRedo() {
      const toolbar = this.$toolbar;
      const editor = this.$editor;
      editor.focus();
      toolbar.menus.redo.exec(editor);
    },
    handleUndo() {
      const toolbar = this.$toolbar;
      const editor = this.$editor;
      editor.focus();
      toolbar.menus.undo.exec(editor);
    },
    setAlign([align]) {
      const toolbar = this.$toolbar;
      const editor = this.$editor;
      editor.focus();

      let menu = null;

      switch (align) {
        case "left":
          menu = toolbar.menus.justifyLeft;
          break;
        case "center":
          menu = toolbar.menus.justifyCenter;
          break;
        case "right":
          menu = toolbar.menus.justifyRight;
          break;
        case "justify":
          menu = toolbar.menus.justifyJustify;
          break;
      }

      if (!menu) return;

      const value = menu.getValue(editor)
      menu.exec(editor, value)
    },
    setList([list]) {
      const editor = this.$editor;
      const toolbar = this.$toolbar;
      editor.focus();
      let menu = null;
      switch (list) {
        case "ordered":
          menu = toolbar.menus.numberedList;
          break;
        case "bullet":
          menu = toolbar.menus.bulletedList;
          break;
        case "check":
          menu = toolbar.menus.todo;
          break;
      }

      if (!menu) return;

      const value = menu.getValue(editor)
      menu.exec(editor, value)
    },
    insertImage([imgInfo]) {
      const editor = this.$editor;
      const toolbar = this.$toolbar;
      toolbar.menus.insertImage.insertImage(editor, imgInfo.src);
    },
    emitContentChange() {
      this.$ownerInstance.callMethod("handleContentChange", this.$editor.getHtml());
    },
    destroyEditor() {
      this.$editor.destroy();
      this.$editor = null;
      this.$toolbar = null;
    }
  }
}
</script>

<style scoped lang="scss">
.editor-container {
  flex: 1;
}

.editor-inner-container {
  height: 100%;
  position: relative;
  --bottom-size: calc(var(--bottom-area-height) + 80rpx);
  --w-e-textarea-bg-color: transparent;
}

.editor-second-container {
  position: absolute;
  top: 0;
  left: 10rpx;
  right: 10rpx;
  bottom: var(--bottom-size);
}

.editor {
  height: 100%;
}

.editor-toolbar {
  display: none;
}

:deep(.w-e-bar.w-e-hover-bar.w-e-bar-show) {

  .w-e-bar-item:nth-child(4),
  .w-e-bar-item:nth-child(5) {
    display: none;
  }
}

:deep(.w-e-text-container) {

  blockquote,
  li,
  p,
  td,
  th,
  .w-e-toolbar * {
    line-height: 1.1;
  }

  p {
    margin: 0 !important;
  }

}

:deep(div[data-slate-editor]) {
  *:first-child {
    margin-top: 18px !important;
  }
}
</style>