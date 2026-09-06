<script lang="ts" setup>
import { useUserInfoStore, useUserStore } from '@/store';
import { forward } from '@/utils/router';
import { logout } from '@/service/api/user'
onLoad(() => {
  uni.setNavigationBarColor({
    frontColor: '#000000',
    backgroundColor: '#8daffd',
  })
});
// const PhoneStore = usePhoneStore()
const userInfoStore = useUserInfoStore();
const userStore = useUserStore();
const deptNames = ref(userInfoStore.users.users?.deptNames.join(','))
function goTo(val: string) {
  forward(val);
}
async function logoutFn() {
  const res = await logout({})
  // 退出登录清除本地数据
  uni.removeStorageSync('token');
  uni.removeStorageSync('username');
  uni.removeStorageSync('password');
  uni.removeStorageSync('loginTime');

  userStore.setUserInfo({
    token: ''
  });
  userInfoStore.setUserInfo2({
    users: {}
  });
  uni.reLaunch({
    url: '/pages/login/login'
  })
  console.log("🚀 ~ logout ~ res:", res)
}
</script>

<template>
  <div class="content">
    <!-- <view :class="PhoneStore.isIphoneX.isIphoneX ? 'c-t-isLH c-t' : 'c-t-noLH c-t'"> -->
    <view class="PhoneStore.isIphoneX.isIphoneX ? 'c-t-isLH c-t' : 'c-t-noLH c-t'">
      <!-- <div :class="PhoneStore.isIphoneX.isIphoneX ? 'title-isLH' : 'title-noLH'">我的</div> -->
      <view class="c-t-son">
        <div style="display: flex;align-items: center;justify-content: center;width: 100%;">
          <div class="right">
            <div class="avtar">
              <img :src=userInfoStore.users.users?.avatar alt="" style="width: 100%;height: 100%;border-radius: 50%;">
              <div v-if="userInfoStore.users.users?.badgeImageUrl" class="badge">
                <img :src=userInfoStore.users.users?.badgeImageUrl alt="" style="width: 100%;height: 100%;">
              </div>
            </div>
          </div>
          <div class="left">
            <div style="font-family: Source Han Sans CN;font-weight: 500;font-size: 40rpx;color: #333333;">
              {{ userInfoStore.users.users?.name }}
            </div>
            <div style="margin-top: 20rpx;">
              <div
                style="font-family: Source Han Sans CN;font-weight: 400;font-size: 24rpx;color: #666666;line-height: 33rpx;">
                {{ deptNames.replace(/,/g, "/") }}/{{ userInfoStore.users.users?.postName }}
              </div>
            </div>
          </div>
        </div>
        <div>
          <wd-cell title="个人信息" is-link @click="goTo('personalInfo')" />
          <!-- <wd-cell title="我的徽章" is-link /> -->
          <wd-cell title="我的徽章" is-link @click="goTo('badge')" />
          <wd-cell title="修改密码" is-link @click="goTo('changePassword')" />
        </div>
        <view class="footer">
          <wd-button type="primary" block @click="logoutFn">退出登录</wd-button>
        </view>
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
    <tabbar :selected="3" />
  </div>
</template>

<style scoped lang="scss">
.content {
  // display: flex;
  // flex-direction: column;
  // justify-content: center;
  // align-items: center;
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

  .c-t {
    position: absolute;
    background: linear-gradient(0deg, rgba(246, 246, 246, 1) 0%, rgba(126, 164, 253, 0.88) 88%);

    .title-isLH {
      position: absolute;
      top: 108rpx;
      left: 344rpx;
      font-family: PingFang SC;
      font-weight: 600;
      font-size: 32rpx;
      color: #000000;
    }

    .title-noLH {
      position: absolute;
      top: 58rpx;
      left: 344rpx;
      font-family: PingFang SC;
      font-weight: 600;
      font-size: 32rpx;
      color: #000000;
    }

    .c-t-son {
      // position: absolute;
      // top: 60rpx;
      display: flex;
      // width: 100%;
      flex-direction: column;
      justify-content: center;
      padding: 20rpx;

      :deep(.wd-cell.data-v-a65b3963) {
        height: 88rpx;
        background: #FFFFFF;
        border-radius: 14rpx;
        margin-top: 20rpx;
      }

      .left {
        flex: 1;
        padding: 30rpx;
      }

      .right {
        display: flex;
        align-items: center;
        justify-content: center;

        .avtar {
          position: relative;
          width: 104rpx;
          height: 104rpx;
          // background: #A8C6FF;
          box-shadow: 0rpx 3rpx 8rpx 0rpx rgba(0, 0, 0, 0.1);
          border-radius: 50%;
          border: 4px solid #FFFFFF;
        }

        .badge {
          display: flex;
          align-items: center;
          justify-content: center;
          width: 36rpx;
          height: 36rpx;
          background: #FFFFFF;
          box-shadow: 0rpx 3rpx 8rpx 0rpx rgba(0, 0, 0, 0.1);
          border-radius: 50%;
          position: absolute;
          right: 0;
          bottom: -6rpx;
        }
      }

      :deep(.wd-button.is-medium.is-round.data-v-aa3a6253) {
        width: 608rpx;
        height: 88rpx;
        background: #7EA4FD;
        border-radius: 14rpx;
        font-family: Source Han Sans CN;
        font-weight: 500;
        font-size: 30rpx;
        color: #FFFFFF;
      }

      .footer {
        margin-top: 40rpx;
      }
    }
  }

  .c-t-isLH {
    width: 100%;
    height: 448rpx;
  }

  .c-t-noLH {
    width: 100%;
    height: 402rpx;
  }
}
</style>