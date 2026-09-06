// import { systemVersion } from '@/api/public'
// import { Res } from '@/utils/typeHelper'
// import message from '@/utils/message'
// 版本更新
export const checkVersion = () => {
  // const appid = plus.runtime.appid;
  // plus.runtime.getProperty(appid, function (widgetInfo) {
  //   systemVersion({
  //     appId: appid,
  //     packVersion: widgetInfo.version,
  //     resPackVersion: widgetInfo.version,
  //     jsVersion: widgetInfo.name
  //   }).then((res: Res) => {
  //     if (res.data.upgrade && res.data.path) {
  //       if (res.data.upgrade == "patch") { // 资源包更新
  //         console.log("data.data.path", res.data.path);
  //         uni.downloadFile({ // 下载资源包
  //           url: res.data.path,
  //           success: (downloadResult) => {
  //             console.log("downloadResult", downloadResult);
  //             if (downloadResult.statusCode === 200) {
  //               plus.runtime.install(downloadResult.tempFilePath, { // 安装资源包
  //                 force: false
  //               }, function () {
  //                 console.log("install success...");
  //                 plus.runtime.restart(); // 重启APP}, function (e) {
  //                 console.error("install fail...");
  //               });
  //             }
  //           }
  //         });
  //       } else if (res.data.upgrade == "full") { // 整包更新
  //         uni.showModal({
  //           title: "更新提示",
  //           content: res.data.msg,
  //           success: (res) => {
  //             if (res.confirm) {
  //               const appurl = "";
  //               if (plus.os.name == "iOS") {
  //                 plus.runtime.launchApplication({
  //                   action: `itms-apps://itunes.apple.com/cn/app/id${appid}`
  //                 }, function (e) {
  //                   alert("Open system default browser failed: " + e.message);
  //                 });
  //               } else if (plus.os.name == "Android") {
  //                 plus.runtime.openURL(appurl); // 跳转应用发布平台
  //               } else {
  //                 message.error(error.message);
  //               }
  //             }
  //           }
  //         });
  //       }
  //     }
  //   }).catch((error: Res) => {
  //     message.error(error.message);
  //   });
  // });
};
