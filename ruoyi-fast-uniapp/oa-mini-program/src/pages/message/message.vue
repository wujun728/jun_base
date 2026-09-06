<script lang="ts" setup>
import { forward } from '@/utils/router';
import { useInit } from '@/hooks/useInit';
import { unreadCountFourInOne } from '@/service/api/homePage';

import tips from '../../static/icon/tips.png'
import internalRemind from '../../static/icon/internalRemind.png'
import reportRemind from '../../static/icon/reportRemind.png'
// import scheduleRemind from '../../static/icon/scheduleRemind.png'
import projectAllocation from '../../static/icon/projectAllocation.png'
import projectFllow from '../../static/icon/projectFllow.png'
import redPoint from '../../static/icon/redPoint.png'

onShow(() => {
  noreadNum()
  const { pageName, pagePath, pageQuery } = useInit();
  console.log(pageName, pagePath, pageQuery, 'pageName,pagePath, pageQuery');
});

const remindList = reactive<any>([
  { title: '温馨提示', src: tips, noread: undefined, noreadTis: 'tipCount', name: 'tips', id: 1 },
  { title: '内部通知', src: internalRemind, noread: undefined, noreadTis: 'noticeCount', name: 'internalRemind', id: 2 },
  { title: '汇报提醒', src: reportRemind, noread: undefined, noreadTis: 'reportCount', name: 'reportRemind', id: 3 },
  // { title: '日程提醒', src: scheduleRemind, noread: undefined, noreadTis: 'scheduleCount', name: 'scheduleRemind', id: 4 },
])
const commission = reactive<any>([
  { title: '项目分配', src: projectAllocation, noread: undefined, noreadTis: 'assignCount', name: 'projectAllocation', id: 5 },
  { title: '项目跟进', src: projectFllow, noread: undefined, noreadTis: 'trackCount', name: 'projectFllow', id: 6 }
])
async function noreadNum() {
  const data = await unreadCountFourInOne({})
  // eslint-disable-next-line array-callback-return
  remindList.map(item => {
    item.noread = data[item.noreadTis]
  })
  // eslint-disable-next-line array-callback-return
  commission.map(item => {
    item.noread = data[item.noreadTis]
  })
  console.log(remindList, commission);

}
function click(item) {
  console.log(item);
  forward('messageSecond', {
    a: item.id
  });
}

</script>

<template>
  <view class="content">
    <view class="title">消息提醒</view>
    <view style="display: flex;flex-direction: column;">
      <view v-for="(item, index) in remindList" :key="index" class="remind" @click="click(item)">
        <view style="display: flex;align-items: center;justify-content: center;position: relative;">
          <img :src=item.src alt="" style="width: 70rpx;height: 70rpx;">
          <img v-if="item.noread > 0" :src=redPoint alt=""
            style="width: 14rpx;height: 14rpx;position: absolute;right: -7rpx;top: -7rpx;">
        </view>
        <view style="margin-left: 20rpx;">
          <view style="font-family: Source Han Sans CN;font-weight: 500;font-size: 30rpx;color: #333333;">
            {{ item.title }}
          </view>
          <view style="font-family: Source Han Sans CN;font-weight: 400;font-size: 22rpx;color: #666666;">
            您有{{ item.noread }} 条{{ item.title }}未读
          </view>
        </view>
      </view>
    </view>
    <!-- <view class="title" style="margin-top: 20rpx;">代办事项</view>
    <view style="display: flex;flex-direction: column;">
      <view v-for="(item, index) in commission" :key="index" class="remind" @click="click(item)">
        <view style="display: flex;align-items: center;justify-content: center;position: relative;">
          <img :src=item.src alt="" style="width: 70rpx;height: 70rpx;">
          <img v-if="item.noread > 0" :src=redPoint alt=""
            style="width: 14rpx;height: 14rpx;position: absolute;right: -7rpx;top: -7rpx;">
        </view>
        <view style="margin-left: 20rpx;">
          <view style="font-family: Source Han Sans CN;font-weight: 500;font-size: 30rpx;color: #333333;">
            {{ item.title }}
          </view>
          <view style="font-family: Source Han Sans CN;font-weight: 400;font-size: 22rpx;color: #666666;">
            您有{{ item.noread }} 条{{ item.title }}未读
          </view>
        </view>
      </view>
    </view> -->
  </view>
  <tabbar :selected="2" />
</template>

<style scoped lang="scss">
.content {
  height: 100vh;
  background: #f6f6f6;
  padding: 20rpx;

  .title {
    font-family: Source Han Sans CN;
    font-weight: bold;
    font-size: 30rpx;
    color: #333333;
  }

  .remind {
    height: 120rpx;
    background: #FFFFFF;
    border-radius: 4rpx;
    display: flex;
    align-items: center;
    margin-top: 20rpx;
    padding: 0 20rpx;
  }
}
</style>