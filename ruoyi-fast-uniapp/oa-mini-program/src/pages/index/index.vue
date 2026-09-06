<script setup lang="ts">
import { useUserInfoStore } from '@/store';
import { getUserInfo } from '@/service/api/user'
import { dataBriefing, todaySchedule } from '@/service/api/homePage'
import xzxs from '@/static/icon/xzxs.png'
import xzkh from '@/static/icon/xzkh.png'
import xzht from '@/static/icon/xzht.png'
import xzxm from '@/static/icon/xzxm.png'
import xzhk from '@/static/icon/xzhk.png'
import xzfk from '@/static/icon/xzfk.png'
import { vehicleBriefing } from '@/service/api/carManage';
onShow(() => {
  getUserInfoFn()
  carStatics()
  dataBriefingList()
  todayScheduleList()
})
onLoad(() => {
  uni.setNavigationBarColor({
    frontColor: '#000000',
    backgroundColor: '#8daffd',
  })
});
const userStore = useUserInfoStore();

let userData = ref<any>({ user: {} })
const dataBriefingData = ref<any>([
  { name: '新增线索', key: 'clueCount', num: 0, background: '#F3F5FC', color: '#7EA4FD', unit: '个', img: xzxs },
  { name: '新增客户', key: 'customerCount', num: 0, background: '#FBF6EF', color: '#DF7700', unit: '个', img: xzkh },
  { name: '新增合同', key: 'contractCount', num: 0, background: '#F3FEF3', color: '#3EB940', unit: '个', img: xzht },
  { name: '新增项目', key: 'projectCount', num: 0, background: '#FFEAEF', color: '#E45453', unit: '个', img: xzxm },
  { name: '新增回款', key: 'collectionAmount', num: 0, background: '#F3F3FF', color: '#2330B9', unit: '万元', img: xzhk },
  { name: '新增付款', key: 'paymentAmount', num: 0, background: '#FFF1E8', color: '#D74D18', unit: '万元', img: xzfk }
])
const dataBr = ref<any>([])
const todayScheduleData = ref<any>([])
const carData = ref<any>([
  { name: '车辆闲置中', key: 'inIdle', num: 0, val: 1, path: '/pages/carManage/carManage' },
  { name: '车辆使用中', key: 'inUse', num: 0, val: 2, path: '/pages/carManage/carManage' },
  { name: '预约待出车', key: 'toBorrow', num: 0, val: 3, path: '/pages/myCarUse/myCarUse' },
  { name: '出车待归还', key: 'toReturn', num: 0, val: 4, path: '/pages/myCarUse1/myCarUse1' }
])
async function getUserInfoFn() {
  const res = await getUserInfo({})
  userData.value = res.user
  userStore.setUserInfo2({
    users: { ...res.user }
  });
}
async function dataBriefingList() {
  const res = await dataBriefing({})
  dataBr.value = res
  // eslint-disable-next-line array-callback-return
  dataBriefingData.value.map(item => {
    item.num = res[item.key]
    if (item.key === 'collectionAmount' || item.key === 'paymentAmount') {
      item.num = (res[item.key] / 10000).toFixed(2)
    }
  })
}
async function todayScheduleList() {
  const res = await todaySchedule({})
  todayScheduleData.value = res
}
async function carStatics() {
  const res = await vehicleBriefing({})
  if (carData.value && carData.value.length > 0) {
    // eslint-disable-next-line array-callback-return
    carData.value.map(item => {
      item.num = res[item.key]
    })
  }
}

function numStatus(val) {
  if (val === 1) {
    return 'blue'
  } else if (val === 2) {
    return 'green'
  } else if (val === 3) {
    return 'orange'
  } else {
    return 'red'
  }
}

function goCar(val) {
  uni.navigateTo({
    url: val.path,
    success(res) {
      res.eventChannel.emit('data', {
        type: val
      })
    },
  })
}
</script>

<template>
  <view class="cont">
    <!-- <view :class="PhoneStore.isIphoneX.isIphoneX ? 'c-t-isLH c-t' : 'c-t-noLH c-t'"> -->
    <view class="c-t">
      <!-- <div :class="PhoneStore.isIphoneX.isIphoneX ? 'title-isLH' : 'title-noLH'">南工应急OA平台</div> -->
      <!-- <view :class="PhoneStore.isIphoneX.isIphoneX ? 'c-t-son-isLH c-t-son' : 'c-t-son-noLH c-t-son'"> -->
      <view class="c-t-son">
        <div class="left">
          <div style="font-family: Source Han Sans CN;font-weight: bold;font-size: 40rpx;color: #333333;">
            {{ userStore.users.users?.name }}
          </div>
          <div style="display: flex;margin-top: 28rpx;">
            <div class="fourTags">安全为先</div>
            <div class="fourTags">技术为本</div>
            <div class="fourTags">信誉为重</div>
            <div class="fourTags">服务为诚</div>
          </div>
          <div class="dis">
            {{ userStore.users.users?.signature }}
          </div>
        </div>
        <div class="right">
          <div class="avtar">
            <img :src=userStore.users.users?.avatar alt="" style="width: 100%;height: 100%;border-radius: 50%;">
          </div>
          <div v-if="userStore.users.users?.badgeImageUrl" class="badge">
            <img :src=userStore.users.users?.badgeImageUrl alt="" style="width: 30rpx;height: 30rpx;">
            <div class="badgeTT">{{ userStore.users.users?.badgeName }}</div>
          </div>
        </div>
      </view>
    </view>
    <view class="main">
      <!-- 车辆 -->
      <div class="m-car">
        <div style="display: flex;align-items: center;justify-content: space-between;padding: 20rpx 20rpx 0 20rpx;">
          <view>
            <div class="main-title">车辆统计</div>
            <div
              style="font-family: Source Han Sans CN;font-weight: 400;font-size: 26rpx;color: #999999;margin-top: 10rpx;">
              车辆使用情况及车辆管理
            </div>
          </view>
          <img src="../../static/icon/carcarcar.png" alt="" style="width: 204rpx;height: 93rpx;">
        </div>
        <div style="display: flex;align-items: center;padding: 20rpx;">
          <div v-for="(item, index) in carData" :key="index"
            style="flex: 1;display: flex;align-items: center;flex-direction: column;padding: 20rpx;"
            :class="numStatus(item.val)" @click="goCar(item)">
            <div :class="numStatus(item.val)">{{ item.num }}</div>
            <div class="carStatus">{{ item.name }}</div>
          </div>
        </div>
      </div>
      <!-- 日程 -->
      <!-- <div v-if="todayScheduleData && todayScheduleData.length > 0" class="m-day">
        <div style="display: flex;align-items: center;position: relative;">
          <div class="main-title">今日日程</div>
          <div
            style="display: flex;align-items: center;justify-content: center;font-family: Source Han Sans CN;font-weight: 400;font-size: 24rpx;color: #666666;position: absolute;right: 20rpx;">
            <div>全部</div>
            <wd-icon name="arrow-right"></wd-icon>
          </div>
        </div>
        <div style="flex: 1;display: flex;flex-direction: column;">
          <div v-for="(item, index) in todayScheduleData" :key="index" class="dayDis">
            <view class="schedulett">{{ item.scheduleTopic }}</view>
            <view class="scheduletime">{{ item.startTime }}-{{ item.endTime }}</view>
          </div>
        </div>
      </div> -->
      <!-- 数据简报 -->
      <div class="m-data">
        <div style="display: flex;align-items: center;padding: 20rpx 20rpx 0 20rpx;position: relative;">
          <div class="main-title">数据简报</div>
          <div
            style="font-family: Source Han Sans CN;font-weight: 400;font-size: 24rpx;color: #666666;position: absolute;right: 20rpx;">
            统计周期：{{ String(dataBr.beginOfPeriod).split(' ')[0] }}-{{ String(dataBr.endOfPeriod).split(' ')[0] }}</div>
        </div>
        <div>
          <wd-grid :column="3" :gutter="10">
            <wd-grid-item v-for="(item, index) in dataBriefingData" :key="index" use-slot>
              <div class="sales" :style="{ background: item.background }">
                <img :src="item.img" alt="" style="width: 66rpx;height: 64rpx;position: absolute;top: 0;right: 0;">
                <div :style="{ color: item.color }" class="sales-num">
                  {{ item.num }}
                  <!-- {{ (item.num / 10000).toFixed(3) }} -->
                  <span style="color: #333333;font-size: 22rpx;">{{ item.unit }}</span>
                </div>
                <div class="sales-name">
                  {{ item.name }}
                </div>
              </div>
            </wd-grid-item>
          </wd-grid>
        </div>
      </div>
    </view>
    <tabbar :selected="0" />
  </view>
</template>

<style scoped lang="scss">
// page {
//   width: 100%;
//   height: 100%;
// }

.cont {
  // display: flex;
  // flex-direction: column;
  // justify-content: center;
  // align-items: center;
  height: 100vh;
  background: #F6F6F6;

  .logo {
    border-radius: 50%;
  }

  .c-t-isLH {
    width: 100%;
    height: 448rpx;
  }

  .c-t-noLH {
    width: 100%;
    height: 402rpx;
  }

  .c-t {
    width: 100%;
    height: 300rpx;
    position: relative;
    background: linear-gradient(0deg, rgba(246, 246, 246, 1) 0%, rgba(126, 164, 253, 0.88) 88%);

    .c-t-son-isLH {
      position: absolute;
      top: 190rpx;
      right: 20rpx;
    }

    .c-t-son-noLH {
      position: absolute;
      top: 150rpx;
      right: 20rpx;
    }

    .c-t-son {

      width: 710rpx;
      height: 258rpx;
      background: #FFFFFF;
      box-shadow: 0rpx 0rpx 5rpx 0rpx rgba(0, 0, 0, 0.13);
      border-radius: 20rpx;
      display: flex;
      position: absolute;
      top: 34rpx;
      right: 20rpx;

      .left {
        flex: 1;
        padding: 30rpx;

        .fourTags {
          width: 99rpx;
          height: 36rpx;
          background: #F4F7FF;
          border-radius: 18rpx;
          font-family: Source Han Sans CN;
          font-weight: 500;
          font-size: 20rpx;
          color: #80A6FD;
          line-height: 33rpx;
          margin-right: 7rpx;
          display: flex;
          align-items: center;
          justify-content: center;
        }

        .dis {
          background: #FFFFFF;
          border-radius: 20rpx;
          font-family: Source Han Sans CN;
          font-weight: 400;
          font-size: 22rpx;
          color: #666666;
          line-height: 33rpx;
          margin-top: 28rpx;
        }
      }

      .right {
        display: flex;
        align-items: center;
        justify-content: center;
        flex-direction: column;
        width: 264rpx;
        padding: 20rpx;

        .avtar {
          width: 132rpx;
          height: 132rpx;
          // background: #A8C6FF;
          box-shadow: 0rpx 3rpx 8rpx 0rpx rgba(0, 0, 0, 0.1);
          border-radius: 50%;
          border: 6px solid #FFFFFF;
        }

        .badge {
          display: flex;
          align-items: center;
          justify-content: center;
          width: 151rpx;
          height: 47rpx;
          // background: linear-gradient(0deg, #7EA4FD 0%, #9DBBFD 100%);
          background: #FFFFFF;
          border-radius: 23rpx;
          border: 1px solid #F2F2F2;
          margin-top: 22rpx;

          .badgeTT {
            width: 100rpx;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            font-family: Source Han Sans CN;
            font-weight: 500;
            font-size: 22rpx;
            color: #ED2C25;
            line-height: 33rpx;
            margin-left: 5rpx;
          }
        }
      }
    }

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

  .main {
    display: flex;
    flex-direction: column;
    justify-content: center;
    background: #F6F6F6;
    padding: 15rpx 20rpx;

    .main-title {
      font-family: Source Han Sans CN;
      font-weight: bold;
      font-size: 30rpx;
      color: #333333;
    }

    .m-car {
      background: #FFFFFF;
      border-radius: 14rpx;

      .carStatus {
        font-family: Source Han Sans CN;
        font-weight: 400;
        font-size: 26rpx;
        color: #666666;
      }

      .green {
        font-family: Source Han Sans CN;
        font-weight: 500;
        font-size: 36rpx;
        color: #3E9746;
        background: #F3FEF3;
      }

      .blue {
        font-family: Source Han Sans CN;
        font-weight: 500;
        font-size: 36rpx;
        color: #7EA4FD;
        background: #F3F5FC;
        border-radius: 14rpx 0rpx 0rpx 14rpx;
      }

      .orange {
        font-family: Source Han Sans CN;
        font-weight: 500;
        font-size: 36rpx;
        color: #ED7912;
        background: #FBF6EF;
      }

      .red {
        font-family: Source Han Sans CN;
        font-weight: 500;
        font-size: 36rpx;
        color: #F15F5F;
        background: #FBF1F1;
        border-radius: 0rpx 14rpx 14rpx 0rpx;
      }
    }

    .m-day {
      background: #FFFFFF;
      border-radius: 14rpx;
      margin-top: 20rpx;
      padding: 20rpx;

      .dayDis {
        // width: 100%;
        background: #F7F7FA;
        border-radius: 14rpx;
        margin-top: 20rpx;
        padding: 20rpx;

        .schedulett {
          font-family: Source Han Sans CN;
          font-weight: 500;
          font-size: 30rpx;
          color: #7EA4FD;
        }

        .scheduletime {
          font-family: Source Han Sans CN;
          font-weight: 400;
          font-size: 26rpx;
          color: #666666;
          margin-top: 16rpx;
        }
      }
    }

    .m-data {
      background: #FFFFFF;
      border-radius: 14rpx;
      margin-top: 20rpx;

      .sales {
        position: relative;
        border-radius: 14rpx;
        padding: 20rpx;
        text-align: left;

        .sales-num {
          font-family: Source Han Sans CN;
          font-weight: 400;
          font-size: 32rpx;
          color: #333333;
        }

        .sales-name {
          font-family: Source Han Sans CN;
          font-weight: 400;
          font-size: 26rpx;
          color: #666666;
        }
      }

      :deep(.wd-grid-item__content.data-v-0c130832) {
        padding: 0;
      }
    }
  }
}

.logo {
  margin-left: auto;
  margin-right: auto;
  margin-top: 200rpx;
  margin-bottom: 50rpx;
  width: 200rpx;
  height: 200rpx;
}

.text-area {
  display: flex;
  justify-content: center;
}

.title {
  font-size: 36rpx;
  color: #8f8f94;
}
</style>