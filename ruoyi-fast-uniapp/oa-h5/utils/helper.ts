import type { PropType } from "@/utils/typeHelper";

// 手机号码正则验证
export const phoneReg = /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/;

// 座机号码正则验证
export const landlineReg = /^([0-9]{3,4}-)?[0-9]{7,8}$/;

// 手机座机同时校验
export const phonelineReg = /^(((\d{3,4}-)?[0-9]{7,8})|(1(3|4|5|6|7|8|9)\d{9}))$/;

// 输入整数：
export const number = /(^-?[1-9]([0-9]*)$|^-?[0-9]$)/;

// 邮箱正则验证
export const mailboxReg = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;

// 验证税号 15或者17或者18或者20位字母、数字组成
export const identReg = /^[A-Z0-9]{15}$|^[A-Z0-9]{17}$|^[A-Z0-9]{18}$|^[A-Z0-9]{20}$/;

// 验证图片格式
export const checkImageType = ["jpeg", "gif", "bmp", "png", "jpg"];

// 验证上传文件格式
export const uploadTypes = [...checkImageType, "doc", "docx", "xls", "xlsx", "xlsm", "ppt", "pptx", "txt", "pdf"];

// 上传图片大小限制
export const defaultImageSize = 2;
//
export const fileSizeOne = 15;

// 根据星期几的数字获取大写星期
export const dayCycleArray = ["周天", "周一", "周二", "周三", "周四", "周五", "周六"];

/**
 * 根据文件名称截取返回文件扩展名
 */
export const getFileExtension = (filename: string): string => {
  if (filename) {
    return filename.slice(filename.lastIndexOf(".") + 1).toLowerCase();
  }
};

/**
 * 判断图片类型
 */
export const isTypeImage = (name: string): boolean => {
  const fileTypeName = getFileExtension(name);
  if (fileTypeName) {
    return checkImageType.includes(fileTypeName);
  }
};

/**
 * 根据颜色和透明度生成bag
 */

export const getColor = (thisColor: string, thisOpacity: string | number): string => {
  let theColor = thisColor.toLowerCase();
  const r = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/;
  if (theColor && r.test(theColor)) {
    if (theColor.length === 4) {
      let sColorNew = "#";
      for (let i = 1; i < 4; i += 1) {
        sColorNew += theColor.slice(i, i + 1).concat(theColor.slice(i, i + 1));
      }
      theColor = sColorNew;
    }
    const sColorChange = [];
    for (let i = 1; i < 7; i += 2) {
      sColorChange.push(parseInt("0x" + theColor.slice(i, i + 2)));
    }
    return "rgba(" + sColorChange.join(",") + "," + thisOpacity + ")";
  }
};

/**
 * 根据文件类型返回图标
 */
export const isFileTypeIcon = (name: string): string => {
  let icon = "";
  const fileType = getFileExtension(name);
  if (fileType === "ppt" || fileType === "pptx") {
    icon = "ppt.png";
  } else if (fileType === "doc" || fileType === "docx" || fileType === "mp4") {
    icon = "word.png";
  } else if (fileType === "xls" || fileType === "xlsx" || fileType === "xlsm") {
    icon = "excel.png";
  } else if (fileType === "pdf") {
    icon = "pdf.png";
  } else if (isTypeImage(fileType)) {
    icon = "image.png";
  } else if (fileType === "txt") {
    icon = "txt.png";
  }
  return icon;
};

/**
 * H5文件下载
 */
export const fileLinkDownLoad = (url: string, name: string): void => {
  const urlPath = url;
  const link = document.createElement("a");
  link.style.display = "none";
  link.href = urlPath;
  link.download = name;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
};

// 获取不同型号手机顶部刘海高度
export const getPhoneInfo = () => {
  const phoneInfo = uni.getSystemInfoSync();
  const statusBarObj = { statusBarHeight: 20 };
  statusBarObj.statusBarHeight = phoneInfo.statusBarHeight;
  return statusBarObj;
};

/**
 * 函数防抖 (只执行最后一次点击)
 * @param fn
 * @param delay
 * @returns {Function}
 * @constructor
 */
export function debounce<T extends (...args: any[]) => void>(func: T, delay = 800): (...args: Parameters<T>) => void {
  let timer: ReturnType<typeof setTimeout>;
  return function debounced(...args: Parameters<T>): void {
    if (timer) clearTimeout(timer);
    timer = setTimeout(() => {
      func(...args);
    }, delay);
  };
}

/**
 * 自动补零
 */
export const getZeroNumber = (num: number = 0): string | number => {
  return num <= 9 ? "0" + num : num;
};
/**
 * 页面跳转
 * 默认跳转首页
 */
export const clickNavigateTo = (url: string = "/pages/index/index"): void => {
  uni.navigateTo({
    url: url,
    animationType: "slide-in-right"
  });
};
/**
 * 页面跳转
 * 默认跳转首页
 */
export const clickSwitchTab = (url: string = "/pages/workbench/index"): void => {
  uni.switchTab({
    url: url,
  });
};

/**
 * 页面跳转
 * 默认跳转首页
 */
export const delayedNavigateTo = (url: string = "/pages/index/index"): void => {
  setTimeout(() => {
    uni.navigateTo({
      url: url,
      animationType: "slide-in-right"
    });
  }, 1000);
};

/**
 * 延时清空跳转
 * 页面跳转
 * 默认跳转首页
 */
export const delayedReLaunch = (url: string = "/pages/index/index"): void => {
  setTimeout(() => {
    uni.reLaunch({
      url: url
    });
  }, 500);
};

/**
 * 延时清空跳转
 * 页面跳转
 * 默认跳转首页
 */
export const clicKReLaunch = (url: string = "/pages/index/index"): void => {
  uni.reLaunch({
    url: url
  });
};

/**
 * 显示消息提示框
 * title'提示'
 */
export const showModal = (content: string = "", title: string = "提示") => {
  return new Promise((reslove, reject) => {
    uni.showModal({
      title: title,
      content: content + "?",
      success: (res) => {
        if (res.confirm) {
          reslove(true);
        } else if (res.cancel) {
          reject();
        }
      }
    });
  });
};

/**
 * 判断两个日期相差多少小时
 */
export const divTime = (time1: string, time2: string, type: string): number => {
  const time3: number = Date.parse(String(new Date(time1)));
  const time4: number = Date.parse(String(new Date(time2)));
  if (time3 > time4) return 0;
  const data1 = Math.abs(parseInt(String((time3 - time4) / (1000 * 60 * 60 * 24))));
  const data2 = Number(Math.abs(parseFloat(String((time3 - time4) / 1000 / 3600))).toFixed(2));
  return type === "day" ? data1 : data2;
};

/**
 * 数字金额转换成大写中文
 */
export const intToChinese = (_n: number): string => {
  let n = _n.toString();
  if (!/^(0|[1-9]\d*)(\.\d+)?$/.test(n)) {
    return "未输入"; // 判断数据是否大于0
  }
  let unit = "仟佰拾亿仟佰拾万仟佰拾元角分";
  let str = "";
  n += "00";
  const indexpoint = n.indexOf("."); // 如果是小数，截取小数点前面的位数
  if (indexpoint >= 0) {
    n = n.substring(0, indexpoint) + n.substr(indexpoint + 1, 2); // 若为小数，截取需要使用的unit单位
  }
  unit = unit.substr(unit.length - n.length); // 若为整数，截取需要使用的unit单位
  for (let i = 0; i < n.length; i++) {
    str += "零壹贰叁肆伍陆柒捌玖".charAt(Number(n.charAt(i))) + unit.charAt(i); // 遍历转化为大写的数字
  }
  return str
    .replace(/零(仟|佰|拾|角)/g, "零")
    .replace(/(零)+/g, "零")
    .replace(/零(万|亿|元)/g, "$1")
    .replace(/(亿)万|壹(拾)/g, "$1$2")
    .replace(/^元零?|零分/g, "")
    .replace(/元$/g, "元整"); // 替换掉数字里面的零字符，得到结果
};

/**
 * 回到顶部
 */
export const backToTop = () => {
  uni.pageScrollTo({
    scrollTop: 0,
    duration: 100,
  });
};

/**
 *根据id(默认值id,可自定义筛选filter字段)获取数组中对应index
 */
export const getFindIndex = <T extends PropType>(arr: Array<T>, id: number | string | boolean = "", filter: keyof T = "id"): number => {
  const index = arr.findIndex((item) => {
    return item[filter] === id;
  });
  return index;
};

export const download = (url: string, name: string) => {
  uni.showLoading({ title: "正在下载" });

  const dtask = plus.downloader.createDownload(url, { filename: "file://storage/emulated/0/tuoluojiang/" + name }, (d,
    status) => {
    if (status === 200) {
      uni.hideLoading();
      uni.showToast({
        icon: "none",
        mask: true,
        title: "已保存到文件夹：/tuoluojiang/" + name,
        duration: 3000,
      });
    } else {
      uni.hideLoading();
      plus.downloader.clear(); // 清除下载任务
      uni.showToast({
        icon: "none",
        mask: true,
        title: "下载失败，请稍后重试",
      });
    }
  });
  dtask.start();
};

// app与h5页面的下载
export const uploadDownload = (url: string, name: string): void => {
  // #ifdef APP-PLUS
  download(url, name);
  // #endif
  // #ifdef H5
  fileLinkDownLoad(url, name);
  // #endif
};

// app与h5页面的预览功能
export const lookPreview = (url: string, name: string, imgUrls = <any>[]) => {
  if (isTypeImage(url)) {
    uni.previewImage({
      current: url,
      urls: imgUrls
    });
  } else {
    // H5  文件下载
    // #ifdef H5
    fileLinkDownLoad(url, name);
    // #endif
    // #ifdef APP-PLUS
    uni.downloadFile({
      url: url,
      success: (res) => {
        const filePath = res.tempFilePath;
        uni.openDocument({
          filePath: filePath,
          showMenu: true,
          success: () => {
            console.log("打开文档成功");
          }
        });
      }
    });
    // #endif
  }
};

/**
 * 将十六进制的颜色转成rgba()
 */
export const hexToRGBA = (hex: string, opacity: number): string => {
  const validHexRegex = /^#([0-9A-Fa-f]{6})$/;
  if (!validHexRegex.test(hex)) {
    throw new Error("Invalid hex color format");
  }

  const hexToDec = (hex: string): number => parseInt(hex, 16);
  const r = hexToDec(hex.slice(1, 3));
  const g = hexToDec(hex.slice(3, 5));
  const b = hexToDec(hex.slice(5, 7));

  if (isNaN(opacity) || opacity < 0 || opacity > 1) {
    throw new Error("Invalid opacity value");
  }

  return `rgba(${r}, ${g}, ${b}, ${opacity})`;
};

/**
 * 实现判断对象中是否含有某个ID/filter 并返回 true 并跳出循环
 */
export const isIdInObjectArray = (objArray: any[], idToFind: number, filter = "id"): boolean => {
  let found = false;
  for (let i = 0; i < objArray.length; i++) {
    const item = objArray[i];
    if (item[filter] === idToFind) {
      found = true;
      break;
    }
  }
  return found;
};

/**
 * 返回数组长度为0的哪一项
 */
export const filterByChildren = <T extends any[]>(array: T, filter: string = "children"): any => {
  return array.filter(item => item[filter]?.length);
};

/**
 * 过滤数组中长度为0的哪一项
 */
export const updateArrayResults = <T extends any[]>(array: T, filter: string = "children") => {
  const filteredArray = filterByChildren<T>(array, filter);
  array.splice(0, array.length, ...filteredArray);
  return array;
};

/**
 * 获取数组中id值返回id的数组
 */
export const getIdsFromArray = <T extends { id: number }>(array: T[]): number[] => {
  return array.map(item => item.id);
};

/**
 * 计算两个经纬度之间的距离
 * @param {Object} location1 第一个坐标点的经纬度信息
 * @param {Object} location2 第二个坐标点的经纬度信息
 * @returns {Number} 两个坐标点之间的距离，单位为米
 */

interface loc {
  latitude: number;
  longitude: number;
}
export const getDistance = (location1: loc, location2: loc): number => {
  const EARTH_RADIUS = 6378137.0; // 地球半径，单位为米
  const radLat1 = (location1.latitude * Math.PI) / 180.0;
  const radLat2 = (location2.latitude * Math.PI) / 180.0;
  const a = radLat1 - radLat2;
  const b = (location1.longitude - location2.longitude) * Math.PI / 180.0;
  const s = 2
    * Math.asin(
      Math.sqrt(
        Math.pow(Math.sin(a / 2), 2)
        + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)
      )
    );
  return s * EARTH_RADIUS;
};

export const formatTime = (seconds: number) => {
  const hours = Math.floor(seconds / 3600);
  const minutes = Math.floor((seconds % 3600) / 60);
  let result = "";
  if (hours > 0) {
    result += `${hours}小时`;
  }
  if (minutes > 0) {
    result += `${minutes}分钟`;
  }
  return result || "0分钟";
};

/**
 * 第一次打开应用便将设备信息获取并当作常量保存下来
 */
const systemInfo = uni.getSystemInfoSync();
export const getSystemInfo = () => systemInfo;

/**
 * 是否携带参数
 * @returns 当前页面路径
 */
export const getCurrentPath = (hasParams: boolean = true) => {
  const pages = getCurrentPages();
  const { fullPath, path } = pages[pages.length - 1].$vm.$page;
  return hasParams ? fullPath : path;
};
