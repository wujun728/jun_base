<script lang="ts" setup>
// import salesContractSrc from '../../static/icon/salesContract.png'
// import purchaseContract from '../../static/icon/purchaseContract.png'
// import projectStatistics from '../../static/icon/projectStatistics.png'
import building from '../../static/icon/building.png'
import myReport from '../../static/icon/myReport.png'
import carManage from '../../static/icon/carManage.png'
import mayCar from '../../static/icon/myCar.png'
// import workDay from '../../static/icon/workDay.png'
// import addressBook from '../../static/icon/addressBook.png'
import { forward } from '@/utils/router';
onLoad(() => {
  uni.setNavigationBarColor({
    frontColor: '#000000',
    backgroundColor: '#8daffd',
  })
});
const statisticsList = reactive([
  { name: 'salesContract', src: building, title: '销售合同统计' },
  { name: 'purchaseContract', src: building, title: '采购合同统计' },
  { name: 'projectStatistics', src: building, title: '项目统计' },
  { name: 'building', src: building, title: '汇报统计' },
])
// const statisticsList = reactive([
//   { name: 'salesContract', src: salesContractSrc, title: '销售合同统计' },
//   { name: 'purchaseContract', src: purchaseContract, title: '采购合同统计' },
//   { name: 'projectStatistics', src: projectStatistics, title: '项目统计' },
//   { name: 'building', src: building, title: '汇报统计' },
// ])
const personalOffice = reactive([
  { name: 'myReport', src: myReport, title: '我的汇报' },
  { name: 'carManage', src: carManage, title: '用车管理' },
  { name: 'myCarUse-officeTo', src: mayCar, title: '我的用车' },
  { name: 'addressBook', src: building, title: '通讯录' },
  { name: 'workDay', src: building, title: '工作日程' },
  { name: 'building', src: building, title: '教育用户端' }
])
// const personalOffice = reactive([
//   { name: 'myReport', src: myReport, title: '我的汇报' },
//   { name: 'carManage', src: carManage, title: '用车管理' },
//   { name: 'myCarUse-officeTo', src: mayCar, title: '我的用车' },
//   { name: 'addressBook', src: addressBook, title: '通讯录' },
//   { name: 'workDay', src: workDay, title: '工作日常' },
//   { name: 'building', src: building, title: '教育用户端' }
// ])
function click(item) {
  console.log(item.name);
  forward(item.name)
}
</script>

<template>
  <view class="content">
    <view class="top">
      <!-- <view :class="PhoneStore.isIphoneX.isIphoneX ? 'top-heigh-isLH top' : 'top-heigh-noLH top'"> -->
      <!-- <div :class="PhoneStore.isIphoneX.isIphoneX ? 'title-isLH' : 'title-noLH'">南工应急OA平台</div> -->
      <img src="../../static/img/officeBanner.png" alt="" style="width: 709rpx;height: 258rpx;margin-top: 34rpx;">
    </view>
    <view style="padding: 0 20rpx;">
      <view class="title">报表与统计</view>
      <view class="statistics">
        <wd-grid :column="4" clickable>
          <wd-grid-item v-for="(item, index ) in statisticsList" :key="index" use-icon-slot :text=item.title
            icon-size="36px" @itemclick="click(item)">
            <template #icon>
              <image class="slot-img" :src=item.src />
            </template>
          </wd-grid-item>
        </wd-grid>
      </view>
      <view class="title">个人办公</view>
      <view class="statistics">
        <wd-grid :column="4" clickable>
          <wd-grid-item v-for="(item, index ) in personalOffice" :key="index" use-icon-slot :text=item.title
            icon-size="36px" @itemclick="click(item)">

            <template #icon>
              <image class="slot-img" :src=item.src />
            </template>
          </wd-grid-item>
        </wd-grid>
      </view>
    </view>
    <view class="fixed">
      <view style="font-family: DingTalk JinBuTi;font-weight: 400;font-size: 22rpx;color: #999999;">
        做 中 国 安 全 应 急 科 技 创 新 引 擎 ！
      </view>
      <view style="font-family: DingTalk JinBuTi;font-weight: 400;font-size: 22rpx;color: #999999;">
        China's Emergency Technology Innovation Engine!
      </view>
    </view>
    <tabbar :selected="1" />
  </view>

</template>

<style scoped lang="scss">
.content {
  height: 100vh;
  background: #F6F6F6;
  position: relative;

  .fixed {
    position: fixed;
    bottom: calc(60px + env(safe-area-inset-bottom));
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    width: 100%;
  }

  .statistics {
    :deep(.wd-grid.data-v-31468b8a) {
      border-radius: 14rpx;
      background: #ffffff;
    }

    .slot-img {
      height: 36px;
      width: 36px;
      border-radius: 4px;
    }

    .text {
      color: #ffb300;
      margin-top: 8px;
    }
  }

  .title {
    font-family: Source Han Sans CN;
    font-weight: bold;
    font-size: 30rpx;
    color: #333333;
    margin: 20rpx 0;
  }

  .top-heigh-isLH {
    height: 540rpx;
  }

  .top-heigh-noLH {
    height: 400rpx;
  }

  .top {
    width: 100%;
    // height: 410rpx;
    position: relative;
    background: linear-gradient(0deg, rgba(246, 246, 246, 1) 0%, rgba(126, 164, 253, 0.88) 88%);
    display: flex;
    align-content: center;
    justify-content: center;

    .title-isLH {
      position: absolute;
      top: 108rpx;
      left: 257rpx;
      font-family: PingFang SC;
      font-weight: 600;
      font-size: 32rpx;
      color: #000000;
    }

    .title-noLH {
      position: absolute;
      top: 58rpx;
      left: 257rpx;
      font-family: PingFang SC;
      font-weight: 600;
      font-size: 32rpx;
      color: #000000;
    }
  }
}
</style>