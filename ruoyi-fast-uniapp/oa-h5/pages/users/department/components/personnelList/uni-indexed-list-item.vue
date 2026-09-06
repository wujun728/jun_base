<template>
  <view>
    <view v-if="loaded || list.itemIndex < 15" class="uni-indexed-list__title-wrapper">
      <text v-if="list.items && list.items.length > 0" class="uni-indexed-list__title">{{ list.key }}</text>
    </view>
    <view v-if="(loaded || list.itemIndex < 15) && list.items && list.items.length > 0" class="uni-indexed-list__list">
      <view v-for="(item, index) in list.items" :key="index" class="uni-indexed-list__item"
        hover-class="uni-indexed-list__item--hover">
        <view class="uni-indexed-list__item-container" @click="onClick(item)">
          <view class="uni-indexed-list__item-border"
            :class="{'uni-indexed-list__item-border--last':index===list.items.length-1}">
            <view v-if="showSelect" style="margin-right: 20rpx;">
              <uni-icons :type="checkedArr.includes(item.id) ? 'checkbox-filled' : 'circle'"
                :color="checkedArr.includes(item.id) ? '#007aff' : '#C0C0C0'" size="24" />
            </view>

            <view class="uni-indexed-list__item-content">

              <avatar :src="item.card.avatar" :radius="8" :auto-size="false" :width="80" :height="80">
              </avatar>
              <view class="item-content-info">
                <text class="text">{{ item.name }}</text>
                <text class="caption" v-if="item.card.job">{{ item.card.job.name }}</text>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
  import avatar from '@/components/avatar/index.vue'
  import {
    toRefs,
    ref,
    computed,
    watch,
    reactive
  } from 'vue'
  import {
    useStore
  } from 'vuex'
  import message from '@/utils/message'
  const store = useStore()

  const props = defineProps({
    loaded: {
      type: Boolean,
      default: false
    },
    idx: {
      type: Number,
      default: 0
    },
    list: {
      type: Object,
      default () {
        return {}
      }
    },
    showSelect: {
      type: Boolean,
      default: false
    },
    mode: {
      type: String,
      default: 'selector'
    },
    isChecked: {
      type: Number,
      default: 0
    }
  })
  const {
    loaded,
    idx,
    list,
    showSelect,
    mode,
    isChecked
  } = toRefs(props)
  let checkedArr = ref([])
  let selectPeopleArr = ref([])
  const userList = reactive({
    user: []
  })
  const emit = defineEmits(['itemClick'])

  // 默认选中id
  const getSelectIds = computed(() => {
    return store.state.app.depSelectIds
  })
  // 选中人的列表
  const getSelectPeople = computed(() => {
    return store.state.app.depSelectPeople
  })

  // 数据监听
  watch([getSelectIds, getSelectPeople], (newvalue, oldvalue) => {
    checkedArr.value = newvalue[0]
    selectPeopleArr.value = newvalue[1]
    // 判断数据加载问题
    userList.user = newvalue[1]

  }, {
    immediate: true,
    deep: true
  });

  //人员选中
  const onClick = (item) => {

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
        userList.user = userList.user.filter(val => val.id !== item.id)

      } else {
        // 判断是否为单选
        if (mode.value === 'selector') {
          if (selectPeopleArr.value.length > 0) {
            message.error('只能选择一个人员')
            return false
          }
        }
        userList.user.push(item)
        checkedArr.value.push(item.id)
      }
      let plainArr = [...checkedArr.value];
      let selectArr = [...userList.user];

      let matchingItems = selectArr.filter(item => plainArr.includes(item.id)); // userList.user中的元素以匹配这些ID
      store.commit('setDepSelectIds', checkedArr.value)

      // store.commit('setDepSelectPeople', Array.from(new Set(matchingItems)))
      store.commit('setDepSelectPeople', selectArr)
    }
  }
</script>

<style lang="scss" scoped>
  .uni-indexed-list__list {
    background-color: $uni-bg-color;
    /* #ifndef APP-NVUE */
    display: flex;
    /* #endif */
    flex-direction: column;
  }

  .uni-indexed-list__item {
    font-size: 14px;
    /* #ifndef APP-NVUE */
    display: flex;
    /* #endif */
    flex: 1;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    padding-bottom: 20rpx;
  }

  .uni-indexed-list__item-container {
    padding-left: 15px;
    flex: 1;
    position: relative;
    /* #ifndef APP-NVUE */
    display: flex;
    box-sizing: border-box;
    /* #endif */
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    /* #ifdef H5 */
    cursor: pointer;
    /* #endif */
  }

  .uni-indexed-list__item-border {
    flex: 1;
    position: relative;
    /* #ifndef APP-NVUE */
    display: flex;
    box-sizing: border-box;
    /* #endif */
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    height: 50px;
    padding: 25px;
    padding-left: 0;
  }

  .uni-indexed-list__item-border--last {
    border-bottom-width: 0px;
  }

  .uni-indexed-list__item-content {
    flex: 1;
    font-size: 14px;
    display: flex;
    align-items: center;

    .item-content-info {
      padding-left: 20rpx;

      uni-text {
        display: block;
      }

      .text {
        font-size: 28rpx;
        color: #2B2C32;
      }

      .caption {
        padding-top: 10rpx;
        font-size: 24rpx;
        color: #909399;
      }
    }

  }

  .uni-indexed-list {
    /* #ifndef APP-NVUE */
    display: flex;
    /* #endif */
    flex-direction: row;
  }

  .uni-indexed-list__title-wrapper {
    /* #ifndef APP-NVUE */
    display: flex;
    width: 100%;
    /* #endif */
    padding-left: 10rpx;
  }

  .uni-indexed-list__title {
    padding: 6px 12px;
    line-height: 24px;
    font-size: 32rpx;
    font-weight: 500;
    color: #909399;
  }
</style>