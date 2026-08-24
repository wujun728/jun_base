<template>
  <div class="edit-container">
    <div id="edit-xmind-container" ref="container" />

    <template v-if="mindMap">
      <!-- 格式工具栏 -->
      <Toolbar v-if="!isZenMode" />

      <!-- 画布和节点的右键菜单 -->
      <Contextmenu :mindMap="mindMap" :isZenMode="isZenMode" />

      <!-- 统计节点数量和字符 -->
      <Count :mindMap="mindMap" v-if="!isZenMode" />

      <!-- 思维导图的缩略地图 -->
      <Navigator v-if="mindMap" :mindMap="mindMap" />

      <!-- 右小角的导航工具栏 -->
      <NavigatorToolbar :mindMap="mindMap" v-if="!isZenMode" />

      <!-- 搜索框 -->
      <Search v-if="mindMap" :mindMap="mindMap" />

      <!-- 文字框输入状态时的格式工具组件 -->
      <!-- 转为 xmind 文件后样式丢失，暂时无解 -->
      <!-- <RichTextToolbar v-if="mindMap" :mindMap="mindMap" /> -->

    </template>
  </div>
</template>

<script>
import MindMap from 'simple-mind-map';
import Export from 'simple-mind-map/src/plugins/Export.js';
import MiniMap from 'simple-mind-map/src/plugins/MiniMap.js'
import Demonstrate from 'simple-mind-map/src/plugins/Demonstrate.js'
import SearchPlugin from 'simple-mind-map/src/plugins/Search.js'
import XmindParse from 'simple-mind-map/src/parse/xmind';
import AssociativeLine from 'simple-mind-map/src/plugins/AssociativeLine.js'
import OuterFrame from 'simple-mind-map/src/plugins/OuterFrame.js'
import RichText from 'simple-mind-map/src/plugins/RichText.js'
import Drag from 'simple-mind-map/src/plugins/Drag.js'
import NodeImgAdjust from 'simple-mind-map/src/plugins/NodeImgAdjust.js'
import Themes from 'simple-mind-map-plugin-themes'

import Contextmenu from './components/Contextmenu.vue';
import Count from './components/Count.vue';
import Navigator from './components/Navigator.vue';
import NavigatorToolbar from './components/NavigatorToolbar.vue';
import Search from './components/Search.vue';
import Toolbar from './components/Toolbar.vue';
// import RichTextToolbar from './components/RichTextToolbar.vue';

import { EDITOR_EVENTS, FORWARD_EDITOR_INNER_EVENTS } from "./event-constant";

import '@/icons/xmind-iconfont.scss';

Themes.init(MindMap);

[
  Export,
  MiniMap,
  Demonstrate,
  SearchPlugin,
  AssociativeLine,
  OuterFrame,
  RichText,
  Drag,
  NodeImgAdjust
].forEach(plugin => {
  MindMap.usePlugin(plugin);
});

/**
 * 使用方法：传入使用 Xmind.parseXmindFile 解析后的 JSON 对象以及文件名
 *          内部会拦截 Ctrl + s 保存的快捷键，然后抛出 save 事件
 *          父组件接收到 save 事件后可以通过 ref 来调用此组件的 getData 方法来获取 Blob 数据来进行处理
 *          也可以在外部直接调用 getData 方法来获取 Blob 数据
 */
export default {
  name: 'XmindEditor',
  components: {
    Contextmenu,
    Count,
    Navigator,
    NavigatorToolbar,
    Search,
    Toolbar,
    // RichTextToolbar
  },
  props: {
    xmindData: {
      type: Object,
      default: () => ({})
    },
    fileName: String,
    size: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      // 组件实例
      mindMap: null,

      // 从云盘远端获取的 Xmind 文件解析之后的 JSON 数据
      xmindJsonData: {},

      // 编辑器要监听的子组件事件，定义在这里，方便之后统一开始和取消监听
      listenEventMap: {
        [EDITOR_EVENTS.EXEC_COMMAND]: this.execCommand,
        [EDITOR_EVENTS.SET_DATA]: this.setData,
        [EDITOR_EVENTS.START_TEXT_EDIT]: this.handleStartTextEdit,
        [EDITOR_EVENTS.END_TEXT_EDIT]: this.handleEndTextEdit,
        [EDITOR_EVENTS.TOGGLE_ZEN_MODE]: this.handleToggleZenMode,
        [EDITOR_EVENTS.CREATE_ASSOCIATIVE_LINE]: this.handleCreateLineFromActiveNode,
        [EDITOR_EVENTS.START_PAINTER]: this.handleStartPainter,
      },

      // 是否开启禅模式
      isZenMode: false
    }
  },
  mounted() {
    this.createMindMapInstance();
    this.startListenEvent();
    this.forwardEvent();

    this.handleKeyBoardSaveShortCut();
  },
  beforeDestroy() {
    this.stopListenEvent();
  },
  methods: {
    // 广播编辑器实例内部的事件给其他子组件使用
    forwardEvent() {
      FORWARD_EDITOR_INNER_EVENTS
        .forEach(eventName => {
          this.mindMap.on(eventName, (...args) => {
            this.$bus.$emit(eventName, ...args);
          });
        });
    },

    // 获取当前 Xmind 数据，并转换为 Blob
    // 返回一个 Promise
    getData() {
      if (this.mindMap.renderer.textEdit.isShowTextEdit()) {
        this.mindMap.renderer.textEdit.hideEditTextBox();
      }
      const data = this.mindMap.getData();
      return XmindParse.transformToXmind(data, this.fileName);
    },

    handleCreateLineFromActiveNode() {
      this.mindMap.associativeLine.createLineFromActiveNode()
    },

    handleStartPainter() {
      this.mindMap.painter.startPainter()
    },

    handleKeyBoardSaveShortCut() {
      this.mindMap.keyCommand.addShortcut('Control+s', () => {
        this.$emit("save");
      })
    },

    startListenEvent() {
      Object.entries(this.listenEventMap)
        .forEach(([eventName, handler]) => {
          this.$bus.$on(eventName, handler);
        });
    },

    stopListenEvent() {
      Object.entries(this.listenEventMap)
        .forEach(([eventName, handler]) => {
          this.$bus.$off(eventName, handler);
        });
    },

    createMindMapInstance() {
      this.mindMap = new MindMap({
        el: this.$refs.container,
        data: this.xmindData,
        openPerformance: this.size > 100, // 开启性能优化
        fit: this.size < 100, // 如果文件大于100kb，开启自适应
        layout: "logicalStructure",
        useLeftKeySelectionRightKeyDrag: false,
        theme: "blueSky",
        openRealtimeRenderOnNodeTextEdit: true
      });
    },

    // 执行 mindMap 实例上的命令
    execCommand(...args) {
      this.mindMap.execCommand(...args)
    },

    setData(data) {
      this.handleShowLoading()
      if (data.root) {
        this.mindMap.setFullData(data);
      } else {
        this.mindMap.setData(data);
      }
      this.mindMap.view.reset();
    },

    handleStartTextEdit() {
      this.mindMap.renderer.startTextEdit()
    },

    handleEndTextEdit() {
      this.mindMap.renderer.endTextEdit()
    },

    handleToggleZenMode() {
      this.isZenMode = !this.isZenMode;
    },
  }
}
</script>

<style scoped lang="scss">
.edit-container {
  position: relative;
}

#edit-xmind-container {
  height: calc(100vh - 60px);
}
</style>
