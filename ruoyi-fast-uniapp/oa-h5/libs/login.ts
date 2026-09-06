import { debounce } from "@/utils/helper";
import store from "@/store";
const serverConfigInfoData = uni.getStorageSync("serverConfigInfo") || [];
// function prePage() {
//   const pages = getCurrentPages();
//   const prePage = pages[pages.length - 1];
//   return prePage?.route;
// }

export const toLogin = debounce(_toLogin, 800);

function _toLogin() {
  store.commit("logout");
  if (serverConfigInfoData.length) {
    uni.setStorageSync("serverConfigInfo", serverConfigInfoData);
  }
  // // #ifdef APP-PLUS
  // path = '/' + path
  // if (!serverConfigInfoData.length) {
  // // serverConfigInfoData.unshift(defaultEnterpriseList);
  // uni.setStorageSync('serverConfigInfo', JSON.stringify(serverConfigInfoData))
  // uni.reLaunch({
  //  url: "/pages/users/login/config",
  // })
  // return
  // }
  // // #endif

  // #ifdef H5

  // const path = location.pathname + location.search;
  // console.log(serverConfigInfoData)
  // if (serverConfigInfoData.length == 0) {
  // // serverConfigInfoData.unshift(defaultEnterpriseList);
  // uni.setStorageSync('serverConfigInfo', JSON.stringify(serverConfigInfoData))
  // uni.reLaunch({
  // url: "/pages/users/login/config",
  // })
  // return
  // }
  // #endif
  const pages = getCurrentPages();
  const currentPath = pages[pages.length - 1].$vm.$page.fullPath;
  store.commit("setLoginBackUrl", currentPath);

  uni.redirectTo({
    url: "/pages/users/login/index"
  });
}
