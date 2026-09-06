<script setup lang="ts">
import { onLaunch, onShow, onHide } from '@dcloudio/uni-app';
import { usePhoneStore } from '@/store';
onLaunch(() => {
  console.log('App Launch');
  checkIsIPhoneX()
});
onShow(() => {
  console.log('App Show');
});
onHide(() => {
  console.log('App Hide');
});
const PhoneStore = usePhoneStore();
function checkIsIPhoneX() {
  // 胶囊按钮位置信息
  const menuButtonInfo = uni.getMenuButtonBoundingClientRect();
  // 导航栏高度 = 状态栏高度 + 44
  uni.getSystemInfo({
    success: (res) => {
      console.log(res);
      PhoneStore.setIsIphoneX({
        // navBarHeight: res.statusBarHeight + 44,
        menuRight: res.screenWidth - menuButtonInfo.right,
        menuTop: menuButtonInfo.top,
        menuHeight: menuButtonInfo.height
      });
      // 根据安全高度判断 ；  判断设备是否为刘海屏
      if ((res.model.includes("iPhone 11") || res.model.includes("iPhone 12") || res.model.includes("iPhone 13") || res.model.includes("iPhone14,5") || res.model.includes("iPhone14")) || (res.model.includes("iPhone") && (res.model.includes("Pro") || res.model.includes("mini") || res.model.includes("X"))) && (res.statusBarHeight! > 20)) {
        PhoneStore.setIsIphoneX({
          isIphoneX: true
        });
        console.log(PhoneStore.isIphoneX.isIphoneX);

      } else {
        PhoneStore.setIsIphoneX({
          isIphoneX: false
        });
        console.log(PhoneStore.isIphoneX.isIphoneX);

      }
    }
  })
}
</script>
<style lang="scss">
/* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
@import "uview-plus/index.scss";
</style>