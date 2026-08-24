<template>
  <div class="navigatorContainer">
    <div class="item">
      <el-tooltip effect="dark" content="回到根节点" placement="top">
        <div class="nav-btn xmind-iconfont icondingwei" @click="backToRoot"></div>
      </el-tooltip>
    </div>
    <div class="item">
      <div class="nav-btn xmind-iconfont iconsousuo" @click="showSearch"></div>
    </div>
    <div class="item">
      <MouseAction :mindMap="mindMap"></MouseAction>
    </div>
    <div class="item">
      <el-tooltip effect="dark" :content="openMiniMap
        ? '关闭小地图' : '开启小地图'
        " placement="top">
        <div class="nav-btn xmind-iconfont icondaohang1" @click="toggleMiniMap"></div>
      </el-tooltip>
    </div>
    <div class="item">
      <el-tooltip effect="dark" :content="isReadonly
        ? '切换为编辑模式'
        : '切换为只读模式'
        " placement="top">
        <div class="nav-btn xmind-iconfont" :class="[isReadonly ? 'iconyanjing' : 'iconbianji1']"
          @click="readonlyChange"></div>
      </el-tooltip>
    </div>
    <div class="item">
      <Fullscreen :mindMap="mindMap"></Fullscreen>
    </div>
    <div class="item">
      <Scale :mindMap="mindMap"></Scale>
    </div>
    <div class="item">
      <Demonstrate :mindMap="mindMap"></Demonstrate>
    </div>
  </div>
</template>

<script>
import Scale from './Scale.vue'
import Fullscreen from './Fullscreen.vue'
import MouseAction from './MouseAction.vue'
import Demonstrate from './Demonstrate.vue'
import { SHOW_SEARCH, TOGGLE_MINI_MAP } from '../event-constant';


export default {
  name: 'NavigatorToolbar',
  components: {
    Scale,
    Fullscreen,
    MouseAction,
    Demonstrate
  },
  props: {
    mindMap: {
      type: Object
    }
  },
  data() {
    return {
      openMiniMap: false,
      isReadonly: false
    }
  },
  methods: {

    readonlyChange() {
      this.mindMap.setMode(this.isReadonly ? 'readonly' : 'edit')
    },

    toggleMiniMap() {
      this.openMiniMap = !this.openMiniMap
      this.$bus.$emit(TOGGLE_MINI_MAP, this.openMiniMap)
    },


    showSearch() {
      this.$bus.$emit(SHOW_SEARCH)
    },

    backToRoot() {
      this.mindMap.renderer.setRootNodeCenter()
    },
  }
}
</script>

<style lang="scss" scoped>
.navigatorContainer {
  padding: 0 12px;
  position: fixed;
  right: 20px;
  bottom: 20px;
  background: hsla(0, 0%, 100%, 0.8);
  border-radius: 5px;
  opacity: 0.8;
  height: 44px;
  font-size: 12px;
  display: flex;
  align-items: center;

  .item {
    margin-right: 20px;

    &:last-of-type {
      margin-right: 0;
    }

    a {
      color: #303133;
      text-decoration: none;
    }

    .nav-btn {
      cursor: pointer;
      font-size: 18px;
    }
  }
}

@media screen and (max-width: 590px) {
  .navigatorContainer {
    left: 20px;
    overflow-x: auto;
    overflow-y: hidden;
    height: 60px;
  }
}
</style>
