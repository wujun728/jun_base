<template>
  <div class="toolbarNodeBtnList" :class="[dir]">
    <template v-for="item in list">
      <div v-if="item === 'back'" class="toolbarBtn" :class="{
        disabled: readonly || backEnd
      }" @click="$bus.$emit(EXEC_COMMAND, 'BACK')">
        <span class="icon xmind-iconfont iconhoutui-shi"></span>
        <span class="text">回退</span>
      </div>
      <div v-if="item === 'forward'" class="toolbarBtn" :class="{
        disabled: readonly || forwardEnd
      }" @click="$bus.$emit(EXEC_COMMAND, 'FORWARD')">
        <span class="icon xmind-iconfont iconqianjin1"></span>
        <span class="text">前进</span>
      </div>
      <div v-if="item === 'siblingNode'" class="toolbarBtn" :class="{
        disabled: activeNodes.length <= 0 || hasRoot || hasGeneralization
      }" @click="$bus.$emit(EXEC_COMMAND, 'INSERT_NODE')">
        <span class="icon xmind-iconfont iconjiedian"></span>
        <span class="text">同级节点</span>
      </div>
      <div v-if="item === 'childNode'" class="toolbarBtn" :class="{
        disabled: activeNodes.length <= 0 || hasGeneralization
      }" @click="$bus.$emit(EXEC_COMMAND, 'INSERT_CHILD_NODE')">
        <span class="icon xmind-iconfont icontianjiazijiedian"></span>
        <span class="text">子节点</span>
      </div>
      <div v-if="item === 'deleteNode'" class="toolbarBtn" :class="{
        disabled: activeNodes.length <= 0
      }" @click="$bus.$emit(EXEC_COMMAND, 'REMOVE_NODE')">
        <span class="icon xmind-iconfont iconshanchu"></span>
        <span class="text">删除节点</span>
      </div>
      <div v-if="item === 'image'" class="toolbarBtn" :class="{
        disabled: activeNodes.length <= 0
      }" @click="$bus.$emit(SHOW_NODE_IMAGE)">
        <span class="icon xmind-iconfont iconimage"></span>
        <span class="text">图片</span>
      </div>
      <div v-if="item === 'link'" class="toolbarBtn" :class="{
        disabled: activeNodes.length <= 0
      }" @click="$bus.$emit(SHOW_NODE_LINK)">
        <span class="icon xmind-iconfont iconchaolianjie"></span>
        <span class="text">超链接</span>
      </div>
      <div v-if="item === 'note'" class="toolbarBtn" :class="{
        disabled: activeNodes.length <= 0
      }" @click="$bus.$emit(SHOW_NODE_NOTE)">
        <span class="icon xmind-iconfont iconflow-Mark"></span>
        <span class="text">备注</span>
      </div>
      <div v-if="item === 'tag'" class="toolbarBtn" :class="{
        disabled: activeNodes.length <= 0
      }" @click="$bus.$emit(SHOW_NODE_TAG)">
        <span class="icon xmind-iconfont iconbiaoqian"></span>
        <span class="text">标签</span>
      </div>
      <div v-if="item === 'summary'" class="toolbarBtn" :class="{
        disabled: activeNodes.length <= 0 || hasRoot || hasGeneralization
      }" @click="$bus.$emit(EXEC_COMMAND, 'ADD_GENERALIZATION')">
        <span class="icon xmind-iconfont icongaikuozonglan"></span>
        <span class="text">概要</span>
      </div>

    </template>
  </div>
</template>

<script>
import {
  BACK_FORWARD,
  EXEC_COMMAND,
  MODE_CHANGE,
  NODE_ACTIVE,
  SHOW_NODE_IMAGE,
  SHOW_NODE_LINK,
  SHOW_NODE_NOTE,
  SHOW_NODE_TAG
} from '../event-constant';

export default {
  props: {
    dir: {
      type: String,
      default: 'h' // h（水平排列）、v（垂直排列）
    },
    list: {
      type: Array,
      default() {
        return []
      }
    }
  },
  data() {
    return {
      EXEC_COMMAND,
      SHOW_NODE_IMAGE,
      SHOW_NODE_LINK,
      SHOW_NODE_NOTE,
      SHOW_NODE_TAG,


      activeNodes: [],
      backEnd: true,
      forwardEnd: true,
      readonly: false,
      isFullDataFile: false,
      timer: null,
      isInPainter: false,

      listenEventMap: {
        [MODE_CHANGE]: this.onModeChange,
        [NODE_ACTIVE]: this.onNodeActive,
        [BACK_FORWARD]: this.onBackForward,
      },
    }
  },
  computed: {
    hasRoot() {
      return (
        this.activeNodes.findIndex(node => {
          return node.isRoot
        }) !== -1
      )
    },
    hasGeneralization() {
      return (
        this.activeNodes.findIndex(node => {
          return node.isGeneralization
        }) !== -1
      )
    }
  },
  created() {
    Object.entries(this.listenEventMap)
      .forEach(([eventName, handler]) => {
        this.$bus.$on(eventName, handler);
      });
  },
  beforeDestroy() {
    Object.entries(this.listenEventMap)
      .forEach(([eventName, handler]) => {
        this.$bus.$off(eventName, handler);
      });
  },
  methods: {

    // 监听模式切换
    onModeChange(mode) {
      this.readonly = mode === 'readonly'
    },

    // 监听节点激活
    onNodeActive(...args) {
      this.activeNodes = [...args[1]]
    },

    // 监听前进后退
    onBackForward(index, len) {
      this.backEnd = index <= 0
      this.forwardEnd = index >= len - 1
    },

  }
}
</script>

<style lang="scss">
.toolbarNodeBtnList {
  display: flex;

  .toolbarBtn {
    display: flex;
    justify-content: center;
    flex-direction: column;
    cursor: pointer;
    margin-right: 20px;

    &:last-of-type {
      margin-right: 0;
    }

    &:hover {
      &:not(.disabled) {
        .icon {
          background: #f5f5f5;
        }
      }
    }

    &.active {
      .icon {
        background: #f5f5f5;
      }
    }

    &.disabled {
      color: #bcbcbc;
      cursor: not-allowed;
      pointer-events: none;
    }

    .icon {
      display: flex;
      height: 26px;
      width: auto;
      background: #fff;
      border-radius: 4px;
      border: 1px solid #e9e9e9;
      justify-content: center;
      flex-direction: column;
      text-align: center;
      padding: 0 5px;
    }

    .text {
      margin-top: 3px;
    }
  }

  &.v {
    display: block;
    width: 120px;
    flex-wrap: wrap;

    .toolbarBtn {
      flex-direction: row;
      justify-content: flex-start;
      margin-bottom: 10px;
      width: 100%;
      margin-right: 0;

      &:last-of-type {
        margin-bottom: 0;
      }

      .icon {
        margin-right: 10px;
      }

      .text {
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    }
  }
}
</style>
