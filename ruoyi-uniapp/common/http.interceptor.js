/**
 * Copyright (c) 2013-Now http://aidex.vip All rights reserved.
 */
// 此处第二个参数vm，就是我们在页面使用的this，你可以通过vm获取vuex等操作
const install = (Vue, vm) => {
	// 通用请求头设定
	const ajaxHeader = 'x-ajax';
	const sessionIdHeader = 'Authorization';
	const rememberMeHeader = 'x-remember';
	
	// 请求参数默认配置
	Vue.prototype.$u.http.setConfig({
		baseUrl: vm.vuex_config.baseUrl,
		originalData: true, 
		// 默认头部，http2约定header名称统一小写 aidex
		header: {
			'content-type': 'application/x-www-form-urlencoded',
			'x-requested-with': 'XMLHttpRequest'
		}
	});
	
	// 请求拦截，配置Token等参数
	Vue.prototype.$u.http.interceptor.request = (req) => {
		if (!req.header){
			req.header = [];
		}
		req.header["source"] = "app";
		
		// 默认指定返回 JSON 数据
		if (!req.header[ajaxHeader]){
			req.header[ajaxHeader] = 'json';
		}
		
		// 设定传递 Token 认证参数 aidex
		if (!req.header[sessionIdHeader] && vm.vuex_token){
			req.header[sessionIdHeader] = vm.vuex_token;
		}
		
		// 为节省流量，记住我数据不是每次都发送的，当会话失效后，尝试重试登录 aidex
		if (!req.header[rememberMeHeader] && vm.vuex_remember && req.remember){
			req.header[rememberMeHeader] = vm.vuex_remember;
			req.remember = false;
		}
	    console.log('request', req);
		return req;
	}
	
	// 响应拦截，判断状态码是否通过
	Vue.prototype.$u.http.interceptor.response = async (res, req) => {
	    console.log('response', res);
		let data = res.data;
		if (!(data)){
			vm.$u.toast('未连接到服务器')
			return false;
		}
		
		if (typeof data === 'object' && !(data instanceof Array)){
			if (data.token){
				vm.$u.vuex('vuex_token', data.token);
				if (data.user){
					vm.$u.vuex('vuex_user', data.user);
				}
				vm.$u.vuex('vuex_isAgent', data.isAgent);
			}
			if (data.result === 'login'){
				vm.$u.vuex('vuex_user', {});
				if (req.tryagain == undefined || req.tryagain){
					req.tryagain = false; req.remember = true;
					await vm.$u.http.request(req).then(res => {
						data = res;
					});
				}
				if (data.result === 'login'){
					if (!req.data.loginCheck){
						vm.$u.toast(data.msg);
					}
					req.tryagain = true;
				}
			}
		}
		
		if (res.header && res.header[rememberMeHeader]){
			let remember = res.header[rememberMeHeader];
			if (remember && remember != 'deleteMe'){
				vm.$u.vuex('vuex_remember', remember);
			}else{
				vm.$u.vuex('vuex_remember', '');
			}
		}
		
		return data;
	}
	
	// 封装 get text 请求
	vm.$u.getText = (url, data = {}, header = {}) => {
		return vm.$u.http.request({
			dataType: 'text',
			method: 'GET',
			url,
			header,
			data
		})
	}
	
	// 封装 post json 请求
	vm.$u.postJson = (url, data = {}, header = {}) => {
		header['content-type'] = 'application/json';
		return vm.$u.http.request({
			url,
			method: 'POST',
			header,
			data
		})
	}
	
}

export default {
	install
}