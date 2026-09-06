<template>
  <view class="organization">
    <view class="content">

      <view class="cr-position-header">
        <!-- 非h5页面默认工具栏高度获取 -->
        <view class="status_bar"></view>
        <default-nav-bar :index="0" :is-border="true" :default-title="navBarTitle" @goBackChange="goBackChange"
          :is-right="false" :right-data="rightIcon" @handleNarItem="handleNarItem">
        </default-nav-bar>
      </view>
      <view class="organization-con" :style="{paddingBottom: bottomH}">

        <list :tree-data="data.treeData" :show-select="showSelect" :mode="data.mode" :is-checked="data.isChecked"
          :show-person="data.showPerson" :is-custom-users="data.isCustomUsers" :only-oneself="data.onlyOneself"
          @handleDep="handleDep"></list>
        <empty :index="9" :title="`暂无数据，快去添加吧！`" v-if="data.treeData[0]&&data.treeData[0].user.length==0"></empty>
      </view>
    </view>

    <select-bottom-bar v-if="showSelect" :show-person="data.showPerson" :is-checked="data.isChecked"
      @handleOk="handleOk"></select-bottom-bar>

  </view>
</template>

<script setup>
  import empty from '@/components/empty/index.vue'
  import defaultNavBar from '@/components/defaultNavBar/index'
  import { ref, reactive, nextTick, computed, watch } from 'vue'
  import list from './components/list.vue'
  import selectBottomBar from '@/pages/users/department/components/selectBottomBar.vue'
  import { clickNavigateTo, getFindIndex } from '@/utils/helper'
  import { useStore } from 'vuex'
  import { onLoad } from '@dcloudio/uni-app'
  import { decryptQuery } from '@/utils/cryptoCode'
  import { navigateToDepartment } from '@/utils/autoload'

  const popupRef = ref(null)
  const store = useStore()
  const search = ref('')
  const navBarTitle = ref('')
  const showSelect = ref(true)
  const bottomH = ref('124rpx')
  const rightIcon = reactive([
    { type: 1, icon: 'icon-sousuo' }
  ])
  /**
   * mode = multiSelector 多选  
   * mode = selector 单选 默认单选
   * isSelect = 0 是否需要选中至少一个成员/部门
   * showPerson = true 是否展示人员 
   * onlyOneself = true 是否可选自己
   * backNumber = -1 自定义返回的层级，主要用于首次加载返回值设定
   * isChecked = 0 是否禁止已有的成员
   * isCustomUsers = false 是否自定义选择成员
   */

  let data = reactive({
    tree: computed(() => store.state.app.frameTree),
    treeData: [],
    optionsId: '',
    mode: 'selector',
    isSelect: 0,
    showPerson: true,
    onlyOneself: true,
    backNumber: 1,
    isChecked: 0,
    isCustomUsers: false,

  })

  // 页面加载完成
  onLoad(e => {
    if (e.item) {
      // 参数解密
      let options = decryptQuery(e.item)

      // 展示select
      if (options.isShow && options.isShow == 'false') {
        showSelect.value = false
        bottomH.value = 0
      }
      // 单选/多选
      if (options.mode && options.mode == 'multiSelector') {
        data.mode = options.mode
      }
      // 是否需要选中至少一个成员/部门
      if (options.isSelect) {
        data.isSelect = options.isSelect
      }

      // 是否展示成员
      if (options.showPerson && options.showPerson === 'false') {
        data.showPerson = false
      }
      // 是否可选自己
      if (options.onlyOneself && options.onlyOneself === 'false') {
        data.onlyOneself = false
      }
      // 自定义返回的层级
      if (options.backNumber && options.backNumber > 1) {
        data.backNumber = Number(options.backNumber)
      }
      // 是否禁止已有的成员
      if (options.isChecked && options.isChecked == 1) {
        data.isChecked = Number(options.isChecked)
      }
      // 是否自定义选择成员
      if (options.isCustomUsers && options.isCustomUsers === 'true') {
        data.isCustomUsers = true
      }
      data.optionsId = options.id
      frameTreeData()
    } else {
      frameTreeData()
    }
  })

  import { resetSelectDepartment } from '@/utils/autoload'
  //返回的回掉函数
  const goBackChange = () => {
    if (!data.optionsId) {
      if (data.isSelect === 0) {
        resetSelectDepartment()
      }
    }
  }

  const handleDep = (e) => {
    if (e.isUser === 2) {
      let url = ''
      if (!showSelect.value) {
        url = `isShow=false&id=${e.id}`
      } else {
        url =
          `id=${e.id}&isFirst=1&isShow=true&mode=${data.mode}&isSelect=${data.isSelect}&showPerson=${data.showPerson}&onlyOneself=${data.onlyOneself}&isChecked=${data.isChecked}`
      }
      navigateToDepartment(url)
    }
  }

  const frameTreeData = () => {
    if (data.optionsId) {
      nextTick(() => {
        const data_s = nodeChildren(data.tree, Number(data.optionsId))
        data.treeData = data_s
        if (data.treeData[0] && data.treeData[0].label) {
          navBarTitle.value = data.treeData[0].label
        }
      })
    } else {
      data.treeData = data.tree
      if (data.treeData[0] && data.treeData[0].label) {
        navBarTitle.value = data.treeData[0].label
      }

    }
  }

  //点击确认
  const handleOk = () => {
    store.commit('setIsOrgShow', true)
    if (data.optionsId) {
      const url = store.state.app.treeSelectBeforePage
      let len = 1
      if (url) {
        const pages = getCurrentPages(); // 当前页面
        const index = getFindIndex(pages, url, 'route')
        len = pages.length - index - 1
      }
      uni.navigateBack({ delta: len })
    } else {
      uni.navigateBack({ delta: data.backNumber })
    }
  }
  // 页面搜索
  const handleNarItem = () => {

  }

  // 递归获取
  const nodeChildren = (treeData = [], newId, newTreeData = []) => {
    treeData.forEach(value => {
      if (value.id === newId && value.type == 0) {
        newTreeData.push(value)
        return newTreeData
      } else {
        if (value.children) {
          let res = nodeChildren(value.children, newId, newTreeData)
          return res
        }
      }
    })

    return newTreeData
  }
</script>
<style>
  page {
    background-color: #fff;
  }
</style>

<style lang="scss" scoped>
  .organization {
    height: 100vh;

    .cr-position-header {
      position: sticky;
      top: 0;
    }

    .content {
      width: 100vw;
      height: 100vh;

      .organization-con {
        padding-top: 30rpx;
        // padding-bottom: v-bind(bottomH);

        ::v-deep .scroll-per {
          height: 100%;
        }
      }

    }

    .search-content {
      padding-top: 16rpx;

      ::v-deep .uni-easyinput__content {
        border: none;
        background-color: #F0F1F5 !important;
      }
    }
  }
</style>