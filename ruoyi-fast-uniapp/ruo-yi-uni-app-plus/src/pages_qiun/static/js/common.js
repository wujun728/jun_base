let isReadyLogin = 1
let loginFlag = 1
export default {
	//提示窗
	tipMsg: function (title, icon, time, mask,callback) {
	    title = title == undefined ? "系统繁忙" : title;
	    icon = icon == undefined ? "none" : icon;
	    time = time == undefined ? 1300 : time;
	    mask = mask == undefined ? true : mask;
	    uni.showToast({
	        title: title,
	        icon: icon,
	        mask: mask,
	        duration: time,
			success() {
				if(callback){
					setTimeout(()=>{
						callback()
					},time);
				}	
			}
	    })
	},
	getTelephoneInfo(){
		return new Promise((resolve, reject) => {
			var data = uni.getStorageSync("telephoneInfo");
			if(!data){
				// 获取右上角胶囊的位置信息
				//#ifndef H5
				let btn = wx.getMenuButtonBoundingClientRect();
				uni.getSystemInfo({
					success: e => {
						let info = {
							screenHeight:e.screenHeight,
							statusBarHeight:e.statusBarHeight,
							windowWidth:e.windowWidth,
							top:btn.top
						}
						uni.setStorageSync("telephoneInfo",info);
						resolve(info);
					},
					fail: (err) => {
						reject(err);
					}
				})
				//#endif
			}else{
				resolve(data);
			}
		})
	},
	// 获取当前年月日
	getNowDate(){
		let date = new Date;
		let now = date.getFullYear() + "-" + (date.getMonth()+1) + "-" + date.getDate();
		return now;
	},
	// 获取上个月的年月日
	getPreMonth(date) {
		var arr = date.split('-');
		var year = arr[0]; //获取当前日期的年份
		var month = arr[1]; //获取当前日期的月份
		var day = arr[2]; //获取当前日期的日
		var days = new Date(year, month, 0);
		days = days.getDate(); //获取当前日期中月的天数
		var year2 = year;
		var month2 = parseInt(month) - 1;
		if (month2 == 0) {
			year2 = parseInt(year2) - 1;
			month2 = 12;
		}
		var day2 = day;
		var days2 = new Date(year2, month2, 0);
		days2 = days2.getDate();
		if (day2 > days2) {
			day2 = days2;
		}
		if (month2 < 10) {
			month2 = '0' + month2;
		}
		var t2 = year2 + '-' + month2 + '-' + "01";
		return t2;
	},
	//检测小程序更新
	checkUpdateVersion(){
		//新版本更新
		if (uni.canIUse('getUpdateManager')) {
			//判断当前微信版本是否支持版本更新
			const updateManager = uni.getUpdateManager();
			updateManager.onCheckForUpdate(function (res) {
				if (res.hasUpdate) {
					// 请求完新版本信息的回调
					updateManager.onUpdateReady(function () {
						uni.showModal({
							title: '更新提示',
							content: '已更新版本，是否重启小程序？',
							showCancel:false,
							cancelColor:'#eeeeee',
							confirmColor:'#40A2ED',
							success: function (res) {
								if (res.confirm) {
									// 新的版本已经下载好，调用 applyUpdate 应用新版本并重启
									updateManager.applyUpdate();
								}
							},
						});
					});
					// 新的版本下载失败
					updateManager.onUpdateFailed(function () {
						uni.showModal({
							title: '更新失败',
							content: '请检查网络设置，若仍更新失败，重新搜索打开',
							success(res) {
								if (res.confirm) {
									// 新的版本已经下载好，调用 applyUpdate 应用新版本并重启
									updateManager.applyUpdate();
								}
							}
						});
					});
				}
			});
		} else {
			uni.showModal({
				// 如果希望用户在最新版本的客户端上体验您的小程序，可以这样子提示
				title: '提示',
				content: '当前微信版本过低，无法使用该功能，请升级到最新微信版本后重试。',
			});
		}
	},
	/**
	 * @param {string} url 目标页面的路由
	 * @param {Object} param 传递给目标页面的参数
	 * @description 处理目标页面的参数，转成json字符串传递给param字段，在目标页面通过JSON.parse(options.param)接收
	 */
	navigateTo(url, param = {},flag) {
		if(isReadyLogin<=0 && !flag){
			this.loginTip();
		}else{
			let part = '';
			for(var item in param){
				part += '&' + item + '=' + param[item];
			}
			url = url + part.replace('&','?');
			uni.navigateTo({
				url: url,
				fail:err=> {
					this.tipMsg('页面正在火速开发中，敬请期待！');
				},
			})
		}
	},
	navigateBack(url, param = {}) {
	    if (loginFlag <= 0) {
	        this.tipMsg("很抱歉,你没有权限！");
	    } else {
	        let part = '';
	        for (var item in param) {
	            part += '&' + item + '=' + param[item];
	        }
	        url = "/pages" + url + part.replace('&', '?');
	        uni.navigateBack({
	            url: url,
	            fail: err => {
	                this.tipMsg('暂未开放该功能！');
	            },
	        })
	    }
	},
}