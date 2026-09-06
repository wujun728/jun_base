<template>
  <view>
    <uni-popup ref="popupRef" type="right" :mask-click="true">
      <view class="slider plr10">
        <view class="title pt120">展现形式</view>
        <view class="schedule-list">
          <view class="left" v-for="(item,index) in data.listData"
            :class="data.activeId == item.style_id ? 'active' : ''" :key="item.style_id" @click="clickStyleItem(item)">
            <image class="image" :src="item.image" mode=""></image>
            <view class="name">{{item.name}}</view>
          </view>

        </view>
        <view class="title schedule-type">业务类型</view>
        <view class="schedule-type-list">
          <view class="type-list-item display-align" v-for="(item,index) in typeData" :key="'type'+index"
            @click="clickTypeItem(item)">
            <view class="iconfont"
              :class="checkedTypes.includes(item.id) ? 'icon-denglu-tongyi' : 'icon-xuanzeanniu-weixuan'"
              :style="{ color: item.color}"></view>
            <view class=" title">{{item.name}}</view>
          </view>
        </view>
      </view>
    </uni-popup>
  </view>
</template>

<script setup>
  import { ref, reactive, toRefs, onMounted } from 'vue'
  import message from '@/utils/message'
  const props = defineProps({
    // 自定义导航栏列表与defaultType为1时，同时使用
    checkedTypes: {
      type: Object,
      default: () => {
        return []
      }
    },
    typeData: {
      type: Object,
      default: () => {
        return []
      }
    }
  })
  const { checkedTypes, typeData } = toRefs(props)
  const emit = defineEmits(["handleItem"]);
  const popupRef = ref(null)
  const styleId = ref(1)
  import day01 from '../../static/image/day.png'
  import day02 from '../../static/image/days.png'
  import day03 from '../../static/image/week.png'
  import day04 from '../../static/image/day1.png'
  const data = reactive({
    tabIndex: 0,
    activeId: 4,
    listData: [
      { image: day04, name: '日程', style_id: 4 },
      { image: day01, name: '日', style_id: 1 },
      { image: day02, name: '3日', style_id: 2 },
      { image: day03, name: '周', style_id: 3 },
    ]
  })

  onMounted(() => {
    data.activeId = uni.getStorageSync('scheduleTypes') + '' || 4
    // emit('handleItem', { type: data.activeId, data: checkedTypes.value })
  })
  // 打开弹出
  const popupOpen = () => {
    popupRef.value.open()
  }

  // 关闭
  const cancel = () => {
    popupRef.value.close()
  }

  // 展现形式选择
  const clickStyleItem = (item) => {
    data.activeId = item.style_id

    uni.setStorageSync('scheduleTypes', JSON.stringify(item.style_id))
    emit('handleItem', { type: data.activeId, data: checkedTypes.value })
    cancel()
    // message.error('目前仅支持单日展示，正在制作中......')
  }

  // 业务类型选择
  const clickTypeItem = (item) => {
    const index = checkedTypes.value.indexOf(item.id)

    if (index > -1) {
      checkedTypes.value.splice(index, 1)
    } else {
      checkedTypes.value.push(item.id)
    }
    emit('handleItem', { type: data.activeId, data: checkedTypes.value })
  }

  defineExpose({ popupOpen })
</script>

<style lang="scss" scoped>
  ::v-deep .uni-popup__wrapper.right {
    padding-top: 0 !important;
  }

  .slider {
    height: 100vh;
    width: calc(100vw - 170rpx);
    background-color: #fff;

    .pt120 {
      // #ifndef APP-PLUS
      padding-top: 40rpx;
      // #endif
      // #ifdef APP-PLUS
      padding-top: 120rpx;
      // #endif
    }

    .title {
      font-size: 26rpx;
      color: $nui-text-color-four;
    }

    .schedule-type {
      border-top: 1px solid #EBEEF5;
      padding: 30rpx 0 40rpx 0;
    }

    .schedule-list {
      width: 100%;
      display: flex;
      justify-content: space-between;
      padding: 40rpx 0;
    }

    // .schedule-list-item {
    //   width: 100%;
    //   height: 82rpx;
    //   padding: 0 32rpx 0 16rpx;
    //   margin-bottom: 20rpx;

    //   &.active {
    //     background-color: rgba(240, 241, 245, 0.5);
    //   }

    // ::v-deep .uni-row {
    //   width: 100%;
    // }

    .left {
      width: 64px;
      height: 64px;
      border-radius: 5px 5px 5px 5px;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;


      &.active {
        background: rgba(66, 172, 249, 0.09);

      }

      .image {
        width: 24px;
        height: 23px;
      }

      .name {
        margin-top: 7px;
        font-family: PingFang SC, PingFang SC;
        font-weight: 400;
        font-size: 11px;
        color: #606266;
      }
    }

    .schedule-type-list {
      .type-list-item {
        font-size: 28rpx;
        color: $uni-text-color;
        font-weight: 500;
        margin-bottom: 36rpx;

        &:last-of-type {
          margin-bottom: 0;
        }

        .iconfont {
          font-size: 38rpx;
          // color: $uni-color-primary;
        }

        .title {
          padding-left: 16rpx;
          color: $uni-text-color;
        }
      }
    }
  }
</style>