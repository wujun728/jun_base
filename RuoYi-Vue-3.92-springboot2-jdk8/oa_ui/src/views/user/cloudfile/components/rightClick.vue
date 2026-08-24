<template>
  <!-- 列表点击右键弹窗 -->
  <div v-show="menuVisible" id="contextmenu" class="menu">
    <div v-if="configData.type === 1" class="right-item-list">
      <div v-if="configData.data.type !== 1" class="right-item" @click="fileDownLoad(configData.data)">
        {{ $t('public.download') }}
      </div>

      <el-divider />
      <div class="right-item" @click="rightItemClick(8)">
        {{ configData.data.is_collect === 0 ? $t('file.collection') : $t('file.cancelcollection') }}
      </div>
    </div>
    <div v-if="configData.type === 2" class="right-item-list">
      <div v-if="configData.data.type === 1" class="right-item" @click="rightItemClick(1)">
        {{ configData.data.is_shortcut === 0 ? $t('file.setcommon') : $t('file.cancelcommon') }}
      </div>
      <div v-if="configData.data.type !== 1" class="right-item" @click="fileDownLoad(configData.data)">
        {{ $t('public.download') }}
      </div>
      <el-divider />
      <div class="right-item" @click="rightItemClick(8)">
        {{ configData.data.is_collect === 0 ? $t('file.collection') : $t('file.cancelcollection') }}
      </div>
    </div>
    <div v-if="configData.type === 3" class="right-item-list">
      <div v-if="configData.share == 2" class="right-item" @click="fileDownLoad(item)">{{ $t('public.download') }}</div>
      <div
        v-if="configData.share == 1 && configData.data.auth.download === 1"
        class="right-item"
        @click="fileDownLoad(item)"
      >
        {{ $t('public.download') }}
      </div>
      <el-divider v-if="configData.share == 1 && configData.data.auth.download === 1" />
      <div v-if="configData.share == 2" class="right-item" @click="rightItemClick(1)">
        {{ $t('file.sharingsettings') }}
      </div>
      <div v-if="configData.share == 2" class="right-item" @click="rightItemClick(2)">
        {{ $t('file.cancelsharing') }}
      </div>
      <el-divider v-if="configData.share == 2" />
      <div v-if="configData.share == 2" class="right-item" @click="rightItemClick(3)">{{ $t('public.delete') }}</div>
    </div>
    <div v-if="configData.type === 4 || configData.type === 5" class="right-item-list">
      <div v-if="configData.data.type === 1" class="right-item" @click="rightItemClick(1)">
        {{ configData.data.is_shortcut === 0 ? $t('file.setcommon') : $t('file.cancelcommon') }}
      </div>
      <div v-if="configData.data.type !== 1" class="right-item" @click="fileDownLoad(configData.data)">
        {{ $t('public.download') }}
      </div>
      <el-divider />
      <div
        v-if="configData.data.type !== 1 && fileIsImage(configData.data.file_type)"
        class="right-item"
        @click="rightItemClick(7)"
      >
        {{ configData.data.is_share === 0 ? '共享' : '取消共享' }}
      </div>
      <div class="right-item" @click="rightItemClick(8)">
        {{ configData.data.is_collect === 0 ? $t('file.collection') : $t('file.cancelcollection') }}
      </div>
      <el-divider />
      <div class="right-item" @click="rightItemClick(2)">{{ $t('file.moveto') }}</div>
      <div v-if="configData.data.type !== 1" class="right-item" @click="rightItemClick(3)">{{ $t('file.copyto') }}</div>
      <div class="right-item" @click="rightItemClick(4)">{{ $t('file.rename') }}</div>
      <el-divider />
      <div class="right-item" @click="rightItemClick(6)">{{ $t('public.delete') }}</div>
      <div class="right-item" @click="rightItemClick(5)">{{ $t('file.attribute') }}</div>
    </div>
    <div v-if="configData.type === 6" class="right-item-list">
      <div class="right-item" v-if="openTypes.includes(configData.data.file_ext)" @click="rightItemClick(1)">打开</div>
      <div v-if="configData.data.type !== 1" class="right-item" @click="fileDownLoad(configData.data)">
        {{ $t('public.download') }}
      </div>
      <div v-if="configData.data.type !== 1" class="right-item" @click="rightItemClick(10)">分享</div>
      <el-divider v-if="configData.data.type !== 1" />
      <div class="right-item" @click="rightItemClick(2)">{{ $t('file.moveto') }}</div>
      <div
        v-if="configData.data.type === 1 && configData.data.user_id === $store.state.user.userInfo.id"
        class="right-item"
        @click="rightItemClick(9)"
      >
        {{ $t('file.directory') }}
      </div>
      <div v-if="configData.data.type !== 1" class="right-item" @click="rightItemClick(3)">{{ $t('file.copyto') }}</div>
      <div class="right-item" @click="rightItemClick(4)">{{ $t('file.rename') }}</div>
      <el-divider />
      <div class="right-item" @click="rightItemClick(6)">{{ $t('public.delete') }}</div>
      <div class="right-item" @click="rightItemClick(5)">{{ $t('file.attribute') }}</div>
    </div>
    <div v-if="configData.type === 7" class="right-item-list">
      <div class="right-item" @click="rightItemClick(2)">{{ $t('file.completelydelete') }}</div>
      <div class="right-item" @click="rightItemClick(1)">{{ $t('file.restorefile') }}</div>
    </div>
  </div>
</template>

<script>
import file from '@/utils/file'
import helper from '@/libs/helper'
Vue.use(file)
import Vue from 'vue'
export default {
  name: 'RightClick',
  props: {
    configData: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      menuVisible: false,
      openTypes: helper.openType,
      clickData: {
        index: -1,
        row: {}
      }
    }
  },

  methods: {
    rightItemClick(type) {
      this.clickData.index = type
      this.clickData.row = this.configData.data

      this.$emit('handleRightClick', this.clickData)
    },
    rightClick(event) {
      this.menuVisible = false // 先把模态框关死，目的是 第二次或者第n次右键鼠标的时候 它默认的是true
      this.menuVisible = true // 显示模态窗口，跳出自定义菜单栏
      event.preventDefault() // 关闭浏览器右键默认事件
      var menu = document.querySelector('#contextmenu')
      var cha = document.body.clientHeight - event.clientY
      // 防止菜单太靠底，根据可视高度调整菜单出现位置
      if (cha < 150) {
        menu.style.top = event.clientY - 120 + 'px'
      } else {
        menu.style.top = event.clientY - 10 + 'px'
      }
      menu.style.left = event.clientX + 10 + 'px'
      document.addEventListener('click', this.foo) // 给整个document添加监听鼠标事件，点击任何位置执行foo方法
    },
    foo() {
      // 取消鼠标监听事件 菜单栏
      this.menuVisible = false
      document.removeEventListener('click', this.foo) // 关掉监听，
    }
  }
}
</script>

<style lang="scss" scoped>
.menu {
  position: fixed;
  background: #fff;
  min-width: 150px;
  border-radius: 4px;
  border: 1px solid #e6ebf5;
  padding: 12px;
  z-index: 9999;
  color: #606266;
  line-height: 1.4;
  text-align: justify;
  font-size: 14px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  word-break: break-all;
}
</style>
