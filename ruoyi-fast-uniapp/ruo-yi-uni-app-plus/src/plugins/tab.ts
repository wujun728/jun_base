let _data:any = {}

function setData(data:any){
  _data = data
}

function getData<T>(){
  return _data as T
}


export default {
  getData,
  /**
   * 关闭所有页面，打开到应用内的某个页面
   * @param url 页面路径
   * @returns 
   */
  reLaunch(url: string,config:any={}): Promise<unknown> {
    setData(config.data)
    return new Promise((resolve, reject) => {
      uni.reLaunch({
        url: url,
        success: resolve,
        fail: reject
      });
    });
  },

  /**
   * 跳转到 tabBar 页面，并关闭其他所有非 tabBar 页面
   * @param url 页面路径
   * @returns 
   */
  switchTab(url: string,config:any={}): Promise<unknown> {
    setData(config.data)
    return new Promise((resolve, reject) => {
      uni.switchTab({
        url: url,
        success: resolve,
        fail: reject
      });
    });
  }
  ,
  /**
   * 关闭当前页面，跳转到应用内的某个页面
   * @param url 页面路径
   * @returns 
   */
  redirectTo(url: string,config:any={}): Promise<unknown> {
    setData(config.data)
    return new Promise((resolve, reject) => {
      uni.redirectTo({
        url: url,
        success: resolve,
        fail: reject
      });
    });
  },
  /**
   * 保留当前页面，跳转到应用内的某个页面
   * @param url 页面路径
   * @returns 
   */
  navigateTo(url: string,config:any={}): Promise<unknown> {
    setData(config.data)
    return new Promise((resolve, reject) => {
      uni.navigateTo({
        url: url,
        success: resolve,
        fail: reject
      });
    });
  },

  /**
   * 关闭当前页面，返回上一页面或多级页面
   * @returns 
   */
  navigateBack(config:any={}): Promise<unknown> {
    setData(config.data)
    return new Promise((resolve, reject) => {
      uni.navigateBack({
        success: resolve,
        fail: reject
      });
    });
  },
}
