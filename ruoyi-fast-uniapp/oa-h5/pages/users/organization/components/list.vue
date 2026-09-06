<template>
  <view class="check-per pl10">
    <scroll-view class="scroll-per" scroll-y>
      <template v-if="!isCustomUsers">
        <view class="uni-indexed-list__item-content pr10" v-for="(item, index) in treeData" :key="item.id">
          <!-- 选择用户 -->
          <template v-if="showPerson">
            <view class="display-align user-list" v-for="(items, indexs) in item.user" :key="'key' + indexs" @click="userClick(items, indexs)">
              <view v-if="showSelect" style="margin-right: 20rpx">
                <uni-icons
                  :type="checkedArr.includes(items.id) ? 'checkbox-filled' : 'circle'"
                  :color="checkedArr.includes(items.id) ? '#1890FF' : '#C0C0C0'"
                  size="24"
                />
              </view>
              <image
                class="avatar"
                :src="items.avatar ? items.avatar : '/static/image/default-avatar.png'"
                @error="avatarError(items)"
                mode="aspectFill"
              ></image>
              <view class="item-content-info">
                <text class="text">{{ items.name }}</text>
                <text class="caption" v-if="items.job">{{ items.job.name }}</text>
              </view>
            </view>
          </template>
          <!-- 选择部门 -->
          <view
            class="user-list"
            v-for="(items, indexs) in item.children"
            :key="'dep' + indexs"
            @click.stop="depClick(items, index)"
            v-if="item.children"
          >
            <template v-if="items.type == 0">
              <uni-row class="display-align">
                <uni-col :span="20" class="display-align">
                  <view v-if="showSelect" style="margin-right: 20rpx" @click.stop="changeDepartment(items, indexs)">
                    <uni-icons
                      :type="userList.checkedFrame.includes(items.id) ? 'checkbox-filled' : 'circle'"
                      :color="userList.checkedFrame.includes(items.id) ? '#1890FF' : '#C0C0C0'"
                      size="24"
                    />
                  </view>
                  <view class="avatar dep-icon" mode="">
                    <text class="iconfont icon-zuzhijiagou"></text>
                  </view>
                  <view class="item-content-info">
                    <text class="text">{{ items.label }}</text>
                  </view>
                </uni-col>
                <uni-col :span="4" class="text-right">
                  <template v-if="showPerson">
                    <uni-icons type="right dep-right-icon"></uni-icons>
                  </template>
                  <template v-else>
                    <uni-icons v-if="items.children" type="right dep-right-icon"></uni-icons>
                  </template>
                </uni-col>
              </uni-row>
            </template>
          </view>
        </view>
      </template>
      <!-- 自定义选择成员 -->
      <template v-else>
        <view class="uni-indexed-list__item-content pr10">
          <view class="display-align user-list" v-for="(items, indexs) in customUsers" :key="'key' + indexs" @click="userClick(items, indexs)">
            <view v-if="showSelect" style="margin-right: 20rpx">
              <uni-icons
                :type="checkedArr.includes(items.id) ? 'checkbox-filled' : 'circle'"
                :color="checkedArr.includes(items.id) ? '#1890FF' : '#C0C0C0'"
                size="24"
              />
            </view>
            <image
              class="avatar"
              :src="items.avatar ? items.avatar : '/static/image/default-avatar.png'"
              @error="avatarError(items)"
              mode="aspectFill"
            ></image>
            <view class="item-content-info">
              <text class="text">{{ items.name }}</text>
              <text class="caption" v-if="items.job">{{ items.job.name }}</text>
            </view>
          </view>
        </view>
      </template>
    </scroll-view>
  </view>
</template>

<script setup>
import { ref, toRefs, reactive, computed, watch } from 'vue'
import message from '@/utils/message'
import { clickNavigateTo } from '@/utils/helper'
import { useStore } from 'vuex'
const store = useStore()
const props = defineProps({
  // 是否显示选择按钮
  showSelect: {
    type: Boolean,
    default: false,
  },

  // 是否显示成员
  showPerson: {
    type: Boolean,
    default: true,
  },
  // 是否可选自己
  onlyOneself: {
    type: Boolean,
    default: true,
  },
  mode: {
    type: String,
    default: '',
  },
  isChecked: {
    type: Number,
    default: 0,
  },
  isCustomUsers: {
    type: Boolean,
    default: false,
  },
  treeData: {
    type: Object,
    default: () => {
      return {}
    },
  },
})
const { treeData, showSelect, mode, showPerson, onlyOneself, isChecked, isCustomUsers } = toRefs(props)
const userInfo = computed(() => store.state.app.userInfo)
const customUsers = computed(() => store.state.app.selectCustomUsers)
let checkedArr = ref([])
let selectPeopleArr = ref([])
let userList = reactive({
  isUser: 1,
  user: [],
  dep: [],
  checkedFrame: [],
})

// 默认选中id
const getSelectIds = computed(() => {
  return store.state.app.depSelectIds
})

// 选中人的列表
const getSelectPeople = computed(() => {
  return store.state.app.depSelectPeople
})

// 数据监听
watch(
  [getSelectIds, getSelectPeople],
  (newvalue, oldvalue) => {
    checkedArr.value = newvalue[0]
    // 判断是否选择部门
    userList.checkedFrame = showPerson.value ? [] : checkedArr.value
    selectPeopleArr.value = newvalue[1]
    // 判断数据加载问题
    userList.user = newvalue[1]
  },
  { immediate: true, deep: true }
)

let emit = defineEmits(['handleDep'])

const userClick = (item, index) => {
  // 点击选中人员
  if (showSelect.value) {
    // 判断禁止选择已有成员
    if (isChecked.value === 1) {
      const checkIds = store.state.app.depCheckIds
      if (checkIds.includes(item.id)) {
        return false
      }
    }

    let len = checkedArr.value.indexOf(item.id)
    if (len > -1) {
      checkedArr.value.splice(len, 1)
      const index = userList.user.findIndex((items) => items.id === item.id)
      if (index > -1) {
        userList.user.splice(index, 1)
      }
    } else {
      // 判断是否为单选
      if (mode.value === 'selector') {
        if (selectPeopleArr.value.length > 0) {
          message.error('只能选择一个人员')
          return false
        }
      }
      // 判断是否可选中自己
      if (!onlyOneself.value && userInfo.value.userId === item.id) {
        message.error('不能选择自己')
        return false
      }
      item.name = item.name
      userList.user.push(item)
      checkedArr.value.push(item.id)
    }
    store.commit('setDepSelectIds', checkedArr.value)
    store.commit('setDepSelectPeople', Array.from(new Set(userList.user)))
    userList.isUser = 1
    emit('handleDep', userList.user)
  } else {
    clickNavigateTo(`/pages/user/personal?id=${item.id}`)
  }
}

import defaultAvatar from '/static/image/default-avatar.png'
const avatarError = (item) => {
  item.card.avatar = defaultAvatar
}

// 选择部门
const changeDepartment = (item, index) => {
  if (showSelect.value) {
    if (showPerson.value) {
      depClick(item, index)
      return false
    }

    let len = userList.checkedFrame.indexOf(item.id)
    if (len > -1) {
      userList.checkedFrame.splice(len, 1)
      const index = userList.user.findIndex((items) => items.id == item.id)
      if (index > -1) {
        userList.user.splice(index, 1)
      }
    } else {
      // 判断是否为单选
      if (mode.value === 'selector') {
        if (selectPeopleArr.value.length > 0) {
          message.error('只能选择一个部门')
          return false
        }
      }
      const dep = {
        label: item.label,
        name: item.label,
        id: item.id,
        is_mastart: false,
      }
      userList.user.push(dep)
      userList.checkedFrame.push(item.id)
    }
    store.commit('setDepSelectIds', userList.checkedFrame)
    store.commit('setDepSelectPeople', Array.from(new Set(userList.user)))
    userList.isUser = 1
    emit('handleDep', userList.user)
  }
}

// 点击选中部门
const depClick = (item, index) => {
  if (!showPerson.value && item.childFrame == 0) return false
  if (!showPerson.value && !item.children) return false
  userList.isUser = 2
  userList.index = index
  userList.id = item.id
  userList.dep = item
  emit('handleDep', userList)
}
</script>

<style lang="scss" scoped>
.check-per {
  height: 100%;

  .scroll-per {
    height: calc(70vh - 44px);
  }

  .uni-indexed-list__item-content {
    .user-list {
      margin-bottom: 30rpx;
    }

    .avatar {
      height: 80rpx;
      width: 80rpx;
      border-radius: 8rpx;
    }

    .dep-icon {
      background: linear-gradient(203deg, rgba(66, 172, 249, 0.15) 0%, rgba(44, 132, 247, 0.15) 100%);
      text-align: center;
      line-height: 80rpx;

      uni-text {
        font-size: 60rpx;
        color: #318cf8;
      }
    }

    .item-content-info {
      padding-left: 20rpx;

      uni-text {
        display: block;
      }

      .text {
        font-size: 28rpx;
        color: #2b2c32;
      }

      .caption {
        padding-top: 10rpx;
        font-size: 24rpx;
        color: #909399;
      }
    }

    .dep-right-icon {
      color: #ccc !important;
    }

    .item-button {
      background-color: #f0f1f5;
      color: #909399;

      &::after {
        border: none;
      }
    }
  }
}
</style>
