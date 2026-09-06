<script setup lang="ts">
import { onLaunch, onShow, onHide } from "@dcloudio/uni-app";
import { useStore } from "vuex";
import { reactive } from "vue";
import WebSocketClass from "@/utils/webSocket";
import { VUE_APP_WS_URL } from "@/config/app";
import { autoLoad } from "@/utils/autoload";
import { clickNavigateTo } from "@/utils/helper";
import { checkVersion } from "@/utils/version";

const store = useStore();
const globalData = reactive({
  socketObj: null
});
onLaunch((options) => {
  // #ifdef WEB
  if (options.path === "pages/module/questionnaire") {
    if (options.query.unique) {
      uni.setStorageSync("QUESTIONNAIRE_CURD_UNIQUE", options.query.unique);
    } else {
      uni.removeStorageSync("QUESTIONNAIRE_CURD_UNIQUE");
    }
  }
  // #endif
  store.commit("init");
  // #ifdef APP-PLUS
  checkVersion();
  // #endif
  if (store.state.app.isLogin) {
    // #ifdef APP-PLUS
    plus.navigator.closeSplashscreen();
    addEventListener();
    // #endif
    // 只加载一次
    autoLoad();
  } else {
    // #ifdef APP-PLUS
    uni.reLaunch({
      url: "/pages/users/login/index",
      success: () => {
        // 跳转完页面后再关闭启动页
        plus.navigator.closeSplashscreen();
      }
    });
    // #endif
  }
});
onShow(() => {
  if (store.state.app.isLogin) {
    // 如果已经有sockt实例
    if (globalData.socketObj) {
      // 如果sockt实例未连接
      if (!globalData.socketObj.isConnect) {
        globalData.socketObj.initSocket();
      }
    } else {
      let token = store.state.app.token;
      let wsUrl = `${VUE_APP_WS_URL}/ws?type=ent&token=${token}`;
      globalData.socketObj = new WebSocketClass(wsUrl, 10);
      globalData.socketObj.initSocket();
    }
  }
  // #ifdef APP-PLUS
  setTimeout(() => {
    plus.navigator.closeSplashscreen();
  }, 2000);
  // #endif
});
onHide(() => { });

// 消息推送逻辑
const addEventListener = (): void => {
  /* 5+  push 消息推送 ps:使用:H5+的方式监听，实现推送 */
  // let info = plus.push.getClientInfo();
  plus.push.addEventListener(
    "click",
    function (msg: any) {
      clickNavigateTo(JSON.parse(JSON.parse(msg.payload)).url);
    },
    false
  );

  plus.push.addEventListener(
    "receive",
    function (msg: any) {
      if (msg.aps) {
        try {
          let options: PlusPushMessageOptions = {
            cover: false,
            sound: "system",
            title: msg.payload.title
          };
          plus.push.createMessage(msg.payload.body, "LocalMSG", options);
        } catch (e) {
          console.log(e);
        }
      } else if (msg.payload === "LocalMSG") {

      } else if (msg.payload.type === "url") {
        clickNavigateTo(msg.payload.url);
      } else {
        try {
          let options: PlusPushMessageOptions = {
            cover: false,
            sound: "system",
            title: msg.payload.title
          };
          plus.push.createMessage(msg.payload.body, "LocalMSG", options);
        } catch (e) {
          console.log(e);
        }
      }
    },
    false
  );
};
</script>

<style lang="scss">
@import url("@/static/css/base.css");
@import url("@/static/iconfont/iconfont.css");
@import "@/static/css/style.scss";
/*每个页面公共css */
</style>
