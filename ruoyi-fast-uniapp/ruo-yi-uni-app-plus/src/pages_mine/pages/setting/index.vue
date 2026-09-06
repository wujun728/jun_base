<template>
  <view class="setting-container" :style="{height: `${windowHeight}px`}">
    <view class="menu-list">
      <view class="list-cell list-cell-arrow" @click="handleToPwd">
        <view class="menu-item-box">
          <view class="iconfont icon-password menu-icon"></view>
          <view>修改密码</view>
        </view>
      </view>
      <view class="list-cell list-cell-arrow" @click="handleToUpgrade">
        <view class="menu-item-box">
          <view class="iconfont icon-refresh menu-icon"></view>
          <view>检查更新</view>
        </view>
      </view>
      <view class="list-cell list-cell-arrow" @click="handleCleanTmp">
        <view class="menu-item-box">
          <view class="iconfont icon-clean menu-icon"></view>
          <view>清理缓存</view>
        </view>
      </view>
    </view>
    <view class="cu-list menu">
      <view class="cu-item item-box">
        <view class="content text-center" @click="handleLogout">
          <text class="text-black">退出登录</text>
        </view>
      </view>
    </view>
  </view>
  <view>
  	  <uni-popup ref="popup" type="dialog">
  	    <uni-popup-dialog type="info" cancelText="关闭" confirmText="退出"
  	                      title="通知" content="确定注销并退出系统吗" 
  	    				  @confirm="dialogConfirm"
  	                      @close="dialogClose">
  	    </uni-popup-dialog>
  	  </uni-popup>
  </view>
</template>

<script setup>
  import { ref } from "vue";
  import store from '@/store'
  
  const windowHeight=ref(uni.getSystemInfoSync().windowHeight) ;
  const popup = ref(null);
  
  function handleToPwd() {
	  uni.navigateTo({
	  	url: '/pages/mine/pwd/index'
	  });
  };
  
  function handleToUpgrade() {
	  uni.showToast({
	    title: '模块建设中~',
	    mask: false,
	  	icon:"none",
	    duration: 1000
	  });
  };
  
  function handleCleanTmp() {
    uni.showToast({
      title: '模块建设中~',
      mask: false,
    	icon:"none",
      duration: 1000
    });
  };
  function handleLogout() {
  	  popup.value.open();
  };
  function dialogConfirm() {
  	//console.log('----------------点击确认------------')
  	store.dispatch('LogOut').then(() => {
  			uni.reLaunch({
  				url: '/pages/login'
  			});
  	})
  };
  function dialogClose() {
  	//console.log('点击关闭')
  };
</script>

<style lang="scss" scoped>
  .page {
    background-color: #f8f8f8;
  }

  .item-box {
    background-color: #FFFFFF;
    margin: 30rpx;
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    padding: 10rpx;
    border-radius: 8rpx;
    color: #303133;
    font-size: 32rpx;
  }
</style>
