import { TOKENNAME, BASEAPI } from "@/config/app";
import store from "../store";
import { uploadTypes, isTypeImage } from "@/utils/helper";

/**
 * 附件上传
 * h5上传文件与图片
 * app仅上传图片
 */
export const uploadFlie = (url: string = "common/upload", formData: object = {}, size: number = 2): Promise<any> => {
  const fileSize = size * 1024 * 1024;
  return new Promise((reslove, reject) => {
    // #ifdef H5
    uni.chooseFile({
      count: 1, // 默认100
      extension: uploadTypes,
      success: (res: any) => {
        const tempFilePaths = res.tempFilePaths[0];
        const tempFiles = res.tempFiles[0];
        // if (isTypeImage(tempFiles.name)) {
        //   // 图片压缩
        //   uni.getImageInfo({
        //     src: tempFilePaths,
        //     success: function (image) {
        //       const compressImg = compressImages(tempFilePaths, image)
        //       if (tempFiles.size > fileSize) {
        //         reject('图片或文件大小不能超过' + size + 'MB')
        //       } else {
        //         uploadFiles(compressImg, url)
        //       }
        //     }
        //   })

        // } else {
        if (tempFiles.size > fileSize) {
          reject("图片或文件大小不能超过" + size + "MB");
        } else {
          uploadFiles(tempFilePaths, url);
        }
        // }
      },
      fail: () => {
        reject("图片或文件选择失败");
      }
    });
    //  #endif

    // #ifndef H5
    uni.chooseImage({
      count: 1,
      sourceType: ["album"], // 从相册选择
      success: (chooseImageRes: any) => {
        const tempFilePaths = chooseImageRes.tempFilePaths[0];
        const tempFiles = chooseImageRes.tempFiles[0];
        // 图片压缩
        uni.getImageInfo({
          src: tempFilePaths,
          success: function (image) {
            // const compressImg = compressImages(tempFilePaths, image)

            if (tempFiles.size > fileSize) {
              reject("图片或文件大小不能超过" + size + "MB");
            } else {
              uploadFiles(tempFilePaths, url);
            }
          }
        });
      },
      fail: () => {
        reject("图片选择失败");
      }
    });
    // #endif

    // 文件上传
    const uploadFiles = (tempFilePaths: any, url: string) => {
      uni.showLoading({
        title: "上传中"
      });

      uni.uploadFile({
        url: uni.getStorageSync("chooseApiUrl") + "" + BASEAPI + "" + url,
        header: {
          [TOKENNAME]: "Bearer " + store.state.app.token
        },
        filePath: tempFilePaths,
        name: "file",
        formData: formData,
        success: (uploadFileRes) => {
          uni.hideLoading();
          const res = JSON.parse(uploadFileRes.data);
          reslove(res);
        },
        fail: () => {
          uni.hideLoading();
          reject("上传失败，请稍后再试！");
        },
        complete: () => {
          uni.hideLoading();
        }
      });
    };
  });
};

/**
 * 图片上传
 * @param url 上传的URL，默认为'common/upload'
 * @param formData 表单数据对象
 * @param size 图片大小限制（单位：MB），默认为2MB
 * @param sourceType 图片选择来源类型，默认为['album']
 */
export const uploadImage = (url: string = "common/upload", formData: object = {}, size: number = 2, sourceType: string[] = ["camera", "album"]): Promise<any> => {
  const fileSize = size * 1024 * 1024;
  return new Promise((reslove, reject) => {
    uni.chooseImage({
      count: 1,
      sourceType: sourceType,
      success: (chooseImageRes: any) => {
        const tempFilePaths = chooseImageRes.tempFilePaths[0];
        const tempFiles = chooseImageRes.tempFiles[0];
        // #ifndef H5
        if (tempFiles.size > fileSize) {
          reject("图片或文件大小不能超过" + size + "MB");
        } else {
          uploadFiles(tempFilePaths, url);
        }
        // uni.compressImage({
        //   src: tempFilePaths,
        //   quality: 50, // 压缩质量，范围0～100，数值越小，质量越低，压缩率越高（仅对jpg有效）
        //   success: res => {
        //     console.log('app压缩图片', res)
        //     uni.readFile({
        //       filePath: res.tempFilePath, // 图片文件路径
        //       encoding: 'base64', // 文件内容的编码格式，这里使用base64编码
        //       success: function (res) {
        //         // 读取成功，res.data为文件的base64编码字符串
        //         var base64Image = res.data;
        //         uploadFiles(base64Image, url)
        //       }
        //     });
        //   }
        // })

        // #endif
        // #ifdef H5
        // 图片压缩
        console.log(tempFilePaths.path, tempFiles, "888888888888888888");
        uni.getImageInfo({
          src: tempFilePaths,
          success: function (image) {
            const compressImg = compressImages(tempFilePaths, image);
            if (tempFiles.size > fileSize) {
              reject("图片或文件大小不能超过" + size + "MB");
            } else {
              uploadFiles(tempFilePaths, url);
            }
          }
        });
        // #endif
      },
      fail: () => {
        reject("图片选择失败");
      }
    });

    // 文件上传
    const uploadFiles = (tempFilePaths: any, url: string) => {
      uni.showLoading({
        title: "上传中"
      });
      uni.uploadFile({
        url: uni.getStorageSync("chooseApiUrl") + "" + BASEAPI + "" + url,
        header: {
          [TOKENNAME]: "Bearer " + store.state.app.token
        },
        filePath: tempFilePaths,
        name: "file",
        formData: formData,
        success: (uploadFileRes: { data: string }) => {
          uni.hideLoading();
          const res = JSON.parse(uploadFileRes.data);
          reslove(res);
        },
        fail: () => {
          uni.hideLoading();
          reject("上传失败，请稍后再试！");
        },
        complete: () => {
          uni.hideLoading();
        }
      });
    };
  });
};

// 压缩文件
const compressImages = (tempFilePaths, image) => {
  let canvasWidth = image.width; // 图片原始长宽
  let canvasHeight = image.height;
  const base = canvasWidth / canvasHeight;
  // 设置画布最大宽度
  if (canvasWidth > 800) {
    canvasWidth = 800;
    canvasHeight = Math.floor(canvasWidth / base);
  }
  const img = new Image();
  img.src = tempFilePaths; // 要压缩的图片
  const canvas = document.createElement("canvas");
  const ctx = canvas.getContext("2d");
  canvas.width = canvasWidth;
  canvas.height = canvasHeight;
  ctx.clearRect(0, 0, canvasWidth, canvasHeight);
  ctx.drawImage(img, 0, 0, canvasWidth, canvasHeight);
  // 指定格式 PNG
  return canvas.toDataURL("image/png");
};

/**
 * 转换文件大小
 */
export const formatBytes = (bytes: number, decimals: number = 2): string => {
  if (bytes === 0) return "0 Bytes";
  const k = 1024;
  const dm = decimals < 0 ? 0 : decimals;
  const sizes = ["Bytes", "KB", "MB", "GB"];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + " " + sizes[i];
};
