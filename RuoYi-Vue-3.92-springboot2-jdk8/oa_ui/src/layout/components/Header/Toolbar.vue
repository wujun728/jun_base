<template>
  <div class="tool-bar breadcrumb">
    <breadcrumb></breadcrumb>
    <!-- <div class="flex top" :class="helpShow ? 'left' : 'mr0'">
      <el-tooltip class="item" effect="dark" content="帮助中心搜索" placement="bottom">
        <span class="iconfont iconjishiben-sousuo" v-if="!searchShow" @click="searchShow = true"></span>
      </el-tooltip>
      <el-input
        v-if="searchShow"
        size="small"
        placeholder="请输入内容"
        v-model="keyword"
        clearable
        @blur="handleConfirm"
        @keyup.native.stop.prevent.enter="handleConfirm"
      >
        <el-button
          class="search"
          size="small"
          slot="suffix"
          @click="handleConfirm()"
          type="text"
          icon="el-icon-search"
        ></el-button>
      </el-input>
      <el-tooltip
        class="item"
        effect="dark"
        :content="helpShow ? '收起帮助中心' : '展开帮助中心'"
        placement="bottom"
        v-if="this.$store.state.settings.helpShow"
      >
        <span class="iconfont" :class="helpShow ? 'iconcebian-shouqi' : 'iconcebian-zhankai'" @click="openHelp"></span>"
      </el-tooltip>
    </div> -->

    <div class="right-bar-wrapper">
      <!-- 扫码配置App -->
      <!-- <configapp /> -->
      <!-- 记事本 -->
      <notepad />
      <!-- 消息通知 -->
      <headerNotice />
      <!--个人中心-->
      <personal />
    </div>
  </div>
</template>

<script>
import { help } from '@/layout/components/index'
import { getAccount } from '@/utils/format'

export default {
  name: 'headerToolbar',
  components: {
    breadcrumb:() => import('./breadcrumb'),
    configapp: () => import('../ConfigApp/index'),
    headerNotice: () => import('../Notice/index'),
    notepad: () => import('../Notepad/notepad'),
    personal: () => import('../Personal/index'),
  },
  props: [
    'searchShow',
    'helpIsShow',
    'openOrCloseHelp',
    'handleSearchConfirm'
  ],
  computed: {

  },
  data() {
    return {
      helpShow: false,
      keyword: '',
    }
  },
  watch: {

  },
  methods: {
    openHelp() {
      this.helpShow = this.helpIsShow ? false : true
      this.$emit('openOrCloseHelp', this.helpShow)
    },
    handleConfirm() {
      this.$emit('handleSearchConfirm', this.keyword)
    }
  }
}
</script>

<style lang="scss" scoped>
@keyframes fadenum {
  0% {
    margin-right: 0px;
  }
  100% {
    margin-right: 360px;
  }
}
.left {
  animation: fadenum 0.5s;
  animation-fill-mode: forwards;
}
.mr0 {
  margin-right: 0px;
}
.iconcebian-zhankai {
  cursor: pointer;
  margin-left: 17px;
  font-size: 20px;
  height: 30px;
  line-height: 30px;
}
.iconcebian-shouqi {
  cursor: pointer;
  margin-left: 20px;
  font-size: 20px;
  height: 30px;
  line-height: 30px;
}
.iconjishiben-sousuo {
  cursor: pointer;
  font-size: 20px;
  height: 30px;
  line-height: 30px;
}

.breadcrumb {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 20;
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  height: 52px;
  padding-right: 15px;
  background-color: #fff;
  // box-shadow: 0px 2px 4px 2px rgba(0, 0, 0, 0.06);

  .iconfont {
    color: #909399;
  }
}

.right-bar-wrapper {
  display: flex;
  // width: 220px;
  align-items: center;
  padding-right: 5px;

  ::v-deep.iconfont {
    font-size: 20px !important;
    color: #606266;
  }

  .config-app {
  }
  .notepad {
    margin: 0 19px;
  }
  .notice {
    margin-right: 30px;
  }
}
</style>


