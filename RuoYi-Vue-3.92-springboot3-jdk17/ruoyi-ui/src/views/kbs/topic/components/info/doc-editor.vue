<template>
  <!-- 文档编辑器 -->
  <div class="editor-wrapper">
    <el-row :gutter="24">
      <!-- 大纲目录 -->
      <el-col :span="6">
        <div class="catalogue">
          <div class="catalogue-title">大纲</div>
          <div class="catalogue-list">
            <ul class="ul-list">
              <li
                v-for="(item, index) in processedDirectory"
                :key="item.id"
                :class="{ active: activeIndex === index,  'is-parent': item.isParent }"
                @click="handleItemClick(index, item.id)"
                :style="{ paddingLeft: (item.level - 0) * 15 + 'px' }"
              >
                <span class="icon-container" v-if="item.isParent">
                  <i class="el-icon-caret-bottom"></i>
                </span>
                <span class="text-container">{{ item.text }}</span>
              </li>
            </ul>
          </div>
        </div>
      </el-col>
      <!-- 编辑器 -->
      <el-col :span="18">
        <div class="editor-body">
          <toolbar style="border-bottom: 1px solid #f1f1f1" :editor="editor" :defaultConfig="toolbarConfig" :mode="mode" />
          <el-input v-model="docData.name" size="medium" placeholder="请输入文章标题" style="padding: 12px 0;" />
          <editor
            class="editor-style"
            v-model="docData.content"
            :defaultConfig="editorConfig"
            :mode="mode"
            @onCreated="onCreated"
            @onChange="currentInputValueChange"
          />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Vue from "vue";
import "@wangeditor/editor/dist/css/style.css";
import { Editor, Toolbar } from "@wangeditor/editor-for-vue";
import { Node, Editor as SlateEditor, Transforms } from "slate";
import { getToken } from "@/utils/auth";
import menuConf from "./menu-conf";

export default Vue.extend({
  components: { Editor, Toolbar },
  data() {
    return {
      editor: null,
      // 工具栏配置
      toolbarConfig: {
        insertKeys: {
          index: 99,
          keys: ["custom-uplod-file"],
        },
        excludeKeys: ["codeBlock", "fullScreen"],
      },
      // 编辑器配置
      editorConfig: {
        placeholder: "请输入内容...",
        readOnly: false,
        autoFocus: true,
        scroll: true,
        MENU_CONF: menuConf, // 自定义菜单配置
      },
      mode: "default", // or 'simple'
      docData: {
        name: "",
        content: "",
      },
      editorRef: null,
      directory: [],
      activeIndex: 0,
      scrollContainer: null,
      token: getToken(),
    };
  },
  props: {
    open: false,
  },
  watch: {
    // 监听标题变化并更新树节点名
    "docData.name"(newVal) {
      if (newVal) {
        this.$emit("update-node", newVal);
      }
    },
  },
  computed: {
    /** 处理动态的大纲层级关系 */
    processedDirectory() {
      const directory = [...this.directory];
      if (directory.length === 0) return [];
      const firstNode = directory[0];
      const resetLevel = firstNode.level === 1 ? 0 : firstNode.level - 1;
      return directory.map((item) => {
        return {
          ...item,
          level: item.level - resetLevel,
        };
      });
    },
  },
  methods: {
    /** 创建编辑器 */
    onCreated(editor) {
      this.editor = Object.seal(editor); // 一定要用 Object.seal() ，否则会报错
      this.editorRef = this.editor;
      const editorDom = editor.getEditableContainer();
      this.scrollContainer = editorDom.querySelector(".w-e-scroll") || editorDom;
      // 设置滚动监听
      this.scrollContainer.addEventListener("scroll", this.handleScroll(), {
        passive: true,
      });
      setTimeout(() => {
        // 获取编辑器内容初始化大纲
        this.directory = this.generateTableOfContents();
      }, 300);
    },
    /** 编辑时，初始化文档内容 */
    initData(val) {
      this.docData.name = val?.name;
      this.docData.content = val?.content;
    },
    /** 获取文档内容 */
    getDocData() {
      return {
        name: this.docData.name || "无标题文档",
        content: this.docData.content || "",
      };
    },
    /** 监听滚动方法 */
    handleScroll() {
      if (!this.scrollContainer) return;
      const headings = this.scrollContainer.querySelectorAll("h1, h2, h3, h4, h5");
      const scrollTop = this.scrollContainer.scrollTop;
      const containerHeight = this.scrollContainer.clientHeight;
      let closestIndex = 0;
      let minDistance = Infinity;

      headings.forEach((heading, index) => {
        const headingTop = heading.offsetTop - this.scrollContainer.offsetTop;
        const distance = Math.abs(headingTop - (scrollTop + containerHeight * 0.3));
        if (distance < minDistance) {
          minDistance = distance;
          closestIndex = index;
        }
      });
      this.activeIndex = closestIndex;
    },
    /** 内容滚动（跟编辑器高度有关） */
    scrollToSection(id) {
      if (!this.editorRef || !this.scrollContainer) return;
      const target = this.scrollContainer.querySelector(`#${id}`);
      if (!target) return;
      const targetTop = target.offsetTop - this.scrollContainer.offsetTop;
      const topOffset = 20;
      this.scrollContainer.scrollTo({
        top: targetTop - topOffset,
        behavior: "smooth",
      });
    },
    /** 编辑器内容变化时触发 */
    currentInputValueChange(editor) {
      this.setImgToken();
      this.setVideoToken();
      this.directory = this.generateTableOfContents();
    },
    /** 点击大纲标题 */
    handleItemClick(index, id) {
      this.activeIndex = index;
      this.scrollToSection(id);
    },
    /** 生成大纲 */
    generateTableOfContents() {
      if (!this.editorRef) return [];
      const editor = this.editorRef;
      const headings = editor.getElemsByTypePrefix("header");
      const directory = headings.map((node, index) => {
        if (!node.id) {
          const id = `header-${index}-${Date.now()}`;
          const path = SlateEditor.path(editor, node);
          Transforms.setNodes(editor, { id }, { at: path });
        }
        const text = Node.string(node);
        return {
          id: node.id,
          text,
          level: parseInt(node.type.replace("header", "")),
          isParent: false,
        };
      });
      /** 标记哪些项是父级 */
      if (directory.length > 0) {
        for (let i = 0; i < directory.length - 1; i++) {
          const current = directory[i];
          const next = directory[i + 1];
          if (next.level > current.level) {
            current.isParent = true;
          }
        }
      }
      return directory;
    },
    /** 动态为 img 标签加 token */
    setImgToken() {
      if (!this.editorRef) return [];
      this.$nextTick(() => {
        const imgs = this.editorRef.getEditableContainer().querySelectorAll("img");
        imgs.forEach((img) => {
          img.src = this.getAuthorization(img.src);
        });
      });
    },
    /** 动态为 video 标签加 token */
    setVideoToken() {
      if (!this.editorRef) return [];
      this.$nextTick(() => {
        const videos = this.editorRef.getEditableContainer().querySelectorAll("video source");
        videos.forEach((video) => {
          video.src = this.getAuthorization(video.src);
        });
      });
    },
    /** 获取授权信息 */
    getAuthorization(src) {
      if (!src || src.includes("data:image/") || src.includes("data:video/") || src.includes("Authorization=Bearer")) {
        return src;
      }
      src += (src.includes("?") ? "&" : "?") + "Authorization=Bearer " + this.token;
      return src;
    },
  },
  beforeDestroy() {
    // 销毁滚动容器
    if (this.scrollContainer) {
      this.scrollContainer.removeEventListener("scroll", this.handleScroll);
    }
    let editor = this.editor;
    if (editor == null || editor.isDestroyed) return;
    editor.destroy(); // 销毁编辑器实例，释放资源
    this.editor = null; // 清除引用，防止内存泄漏
  },
});
</script>

<style src="@wangeditor/editor/dist/css/style.css"></style>
<style scoped lang="scss">
.editor-wrapper {
  display: flex;
}
.editor-body {
  overflow: hidden;
}

.catalogue {
  box-sizing: border-box;
  border-top: none;
  display: flex;
  flex-direction: column;
  height: calc(88vh - 90px);
}
.catalogue-title {
  border-bottom: 1px solid #f1f1f1;
  height: 40px;
  padding: 10px;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  color: #606266;
  font-weight: bold;
  font-size: 14px;
  margin-bottom: 5px;
}
.catalogue-list {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
  font-size: 12px;
  color: #909399;
  padding: 10px 0;
}
.ul-list {
  margin: 0;
  list-style-type: none;
  padding-left: 15px;
  li {
    display: flex;
    align-items: center;
    line-height: 30px;
    cursor: pointer;

    .icon-container {
      flex-shrink: 0;
      width: 16px;
      height: 16px;
      display: inline-flex;
      align-items: center;
      justify-content: center;
      margin-right: 4px;
      color: #c0c4cc;
      transition: color 0.3s;
    }

    .icon-container i {
      font-size: 12px;
    }

    .text-container {
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      flex-grow: 1;
      min-width: 0;
    }
    &:hover {
      background-color: #f8fbff;
    }

    &.is-parent::before {
      content: "";
      display: inline-block;
      color: #909399;
      margin-left: -18px;
      transition: color 0.3s;
    }

    &.is-parent.active::before {
      color: #409eff;
    }
  }
  li:hover {
    color: #409eff;
  }
}
.active {
  color: #409eff;
}
::v-deep .el-input__inner {
  border: none !important;
  outline: none !important;
  box-shadow: none !important;
  background-color: transparent !important;
  padding: 8px !important;
  font-size: 24px !important;
  color: #333 !important;
  font-weight: bold;
}
::v-deep .el-input__inner::placeholder {
  color: #999 !important;
  font-size: 24px !important;
  font-weight: normal;
}

::v-deep .editor-style {
  height: calc(88vh - 230px);
  border-top: none;
  border-left: none;
  overflow-y: auto;
  will-change: scroll-position;
}
::v-deep .w-e-text-container {
  min-height: 100%;
}
// 滚动条样式
::-webkit-scrollbar {
  width: 3px;
  height: 3px;
}
::-webkit-scrollbar-track {
  background-color: #eee;
}
::-webkit-scrollbar-thumb {
  background-color: #ccc;
  border-radius: 3px;
}
::-webkit-scrollbar-thumb:hover {
  background-color: #aaa;
}
</style>