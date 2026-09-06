<template>
  <BaseContainer>
    <view class="content">
      <view class="cr-position-header">
        <view class="status_bar"></view>
        <default-nav-bar :is-right="true" :default-title="data.defaultTitle" :is-show-title="data.isShowTitle"
          :right-data="data.rightIcon" @handleNarItem="handleNarItem">
        </default-nav-bar>
      </view>
      <view class="details-content plr10">
        <view class="details-title">{{ data.default.title }}</view>
      </view>
      <view class="details" @click="goEdit">
        <BaseRichEditor :content="data.content" readOnly v-if="data.content" />
      </view>
      <drop-down ref="dropDownRef" :list-data="forumMeus" @btn-click="dropDownItem"></drop-down>
      <ba-tree-picker ref="treePickerRef" :multiple="false" @select-change="selectChange" title="移动至"
        :localdata="data.treeData" valueKey="id" textKey="name" childrenKey="children" :border="true" />
      <global-index />
    </view>
  </BaseContainer>
</template>

<script setup>
import BaseContainer from "@/components/BaseContainer/index.vue";
import defaultNavBar from '@/components/defaultNavBar/index'
import globalIndex from '@/components/globalIndex/index.vue'
import dropDown from '@/pages/forum/components/dropDown.vue'
import baTreePicker from '@/components/baTreePicker/index.vue'
import BaseRichEditor from "@/components/BaseRichEditor/index.vue";
import { ref, reactive, onMounted } from "vue"
import message from '@/utils/message'
const data = reactive({
  rightIcon: [
    { type: 1, icon: 'icon-gengduo1', types: 'icon' }
  ],
  default: {},
  content: '',
  treeData: [],
  id: null,
  tab: '',
  pid: '',
  defaultTitle: '',
  isShowTitle: false
})

const forumMeus = reactive([
  { name: '编辑', id: 1, icon: 'icon-gongzuohuibao-bianji' },
  { name: '移动至', id: 2, icon: 'icon-jishiben-yidongzhi' },
  { name: '删除', id: 3, icon: 'icon-shanchu1' },
])

import { userMemorialInfoApi, userMemorialCateTreeApi, userMemorialEditApi, userMemorialDeleteApi } from '@/api/user'

import { onLoad } from '@dcloudio/uni-app'
onLoad((e) => {
  if (e.id) {
    data.id = e.id
    data.tab = e.tab
    data.pid = e.pid
    getMemorialInfo(e.id)
    getCateTree()
  }
})

// 获取记事本详情
const getMemorialInfo = (id) => {
  userMemorialInfoApi(id).then(res => {
    data.default = res.data || {}
    if (res.data.content) {
      data.content = res.data.content
      // data.content = res.data.content.replace(/<img/gi, '<img style="max-width:100%;height:auto" ')
    }
    if (res.data.parent) {
      data.defaultTitle = res.data.parent.name
    } else {
      data.defaultTitle = '记事本'
    }
    data.isShowTitle = true
  }).catch((error) => {
    message.error(error.message)
  })
}


// 获取记事本分类详情
const getCateTree = () => {
  userMemorialCateTreeApi().then(res => {
    data.treeData = res.data.tree || []
  }).catch((error) => {
    message.error(error.message)
  })
}


const dropDownRef = ref(null)
const handleNarItem = (e) => {
  dropDownRef.value.openDropdown()
}

import { clickNavigateTo, delayedReLaunch, showModal } from '@/utils/helper'
const treePickerRef = ref(null)
const dropDownItem = (e) => {
  if (e.id === 2) {
    treePickerRef.value.show()
  } else if (e.id === 3) {
    showModal('确定要删除这条内容吗').then(() => {
      getMemorialDelete(data.default.id, data.default.pid)
    }).catch(() => {
      console.log('取消')
    })
  } else {
    let url = ''

    if (data.tab === 'newes') {
      url = `/pages/users/memorandum/create?tab=${data.tab}&pid=&id=${data.id}`
    } else {
      url = `/pages/users/memorandum/create?tab=${data.tab}&pid=${data.default.pid}&id=${data.id}`
    }
    clickNavigateTo(url)
  }
}

const goEdit = () => {
  let url = ''
  url = `/pages/users/memorandum/create?tab=${data.tab}&pid=&id=${data.id}`
  clickNavigateTo(url)
}

// 选后回调
const selectChange = (e) => {
  const res = {
    title: data.default.title,
    content: data.default.content,
    pid: e[0]
  }
  setMemorialEdit(data.default.id, res)
}

// 获取记事本移动至
const setMemorialEdit = (id, datas) => {
  userMemorialEditApi(id, datas).then(res => {
    message.success('移动成功')
    delayedReLaunch('/pages/users/memorandum/index')
  }).catch((error) => {
    message.error(error.message)
  })
}

// 获取记事本删除
const getMemorialDelete = (id, pid) => {
  userMemorialDeleteApi(id).then(res => {
    message.success(res.message)
    let url = ''
    if (data.tab === 'newes') {
      url = `/pages/users/memorandum/index?tab=${data.tab}&id=`
    } else {
      url = `/pages/users/memorandum/index?tab=${data.tab}&id=${pid}`
    }
    delayedReLaunch(url)
  }).catch((error) => {
    message.error(error.message)
  })
}
</script>

<style>
page {
  background-color: #fff;
}
</style>

<style scoped lang="scss">
.content {
  height: var(--full-vh);
  position: relative;
  display: flex;
  flex-direction: column;

  .cr-position-header {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
  }

  .details-content {
    margin-top: calc(var(--status-bar-height) + 44px);

    .details-title {
      padding: 40rpx 0;
      color: $uni-text-color;
      font-size: 36rpx;
      font-weight: $uni-default-font-weight;
      border-bottom: 1px solid $uni-line-style-color-three;
    }


  }

  .details {
    flex: 1;
    display: flex;
    flex-direction: column;
  }

}
</style>