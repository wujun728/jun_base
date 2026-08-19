var request={
	headers:{
		Authorization: Authorization,
		Lang: 'zh_CN'
	},
	urls:{
		//账号密码登录
		accountLogin:{
			method:'POST',
			url: serverUrl + '/v1/dologin'
		},
		//退出登录
		accountLogout:{
			method:'DELETE',
			url: serverUrl + '/v1/logOut'
		},
		dataSourceList:{
			method:'GET',
			url: serverUrl + '/lowcode/dataSources'
		},
		dataSourceAdd:{
			method:'POST',
			url: serverUrl + '/lowcode/dataSource'
		},
		dataSourceEdit:{
			method:'PUT',
			url: serverUrl + '/lowcode/dataSource'
		},
		dataSourceDetail:{
			method:'GET',
			url: serverUrl + '/lowcode/dataSource/{sourceId}',
			param:'sourceId'
		},
		dataSourceDel:{
			method:'DELETE',
			url: serverUrl + '/lowcode/dataSources'
		},
		connectTest:{
			method:'POST',
			url: serverUrl + '/lowcode/connectTest'
		},
		databaseList:{
			method:'GET',
			url: serverUrl + '/lowcode/databases'
		},
		dbTableList:{
			method:'GET',
			url: serverUrl + '/lowcode/dbTables'
		},
		tableEntityInfo:{
			method:'GET',
			url: serverUrl + '/lowcode/tableEntityInfo'
		},
		sqlCRUD:{
			method:'GET',
			url: serverUrl + '/lowcode/sqlCRUD'
		},
		apiInfoAdd:{
			method:'POST',
			url: serverUrl + '/lowcode/apiInfo'
		},
		apiInfoEdit:{
			method:'PUT',
			url: serverUrl + '/lowcode/apiInfo'
		},
		apiInfoList:{
			method:'GET',
			url: serverUrl + '/lowcode/apiInfos'
		},
		apiInfoTrees:{
			method:'GET',
			url: serverUrl + '/lowcode/apiInfoTrees'
		},
		apiInfoDetail:{
			method:'GET',
			url: serverUrl + '/lowcode/apiInfo/{apiId}',
			param:'apiId'
		},
		apiInfoDel:{
			method:'DELETE',
			url: serverUrl + '/lowcode/apiInfo/{apiId}',
			param:'apiId'
		},
		apiGroups:{
			method:'GET',
			url: serverUrl + '/lowcode/apiGroups'
		},
		apiBatchCreate:{
			method:'POST',
			url: serverUrl + '/lowcode/apiBatchCreate'
		}
	}
}

function ajaxRequest($, requestData, pathValue, params,successCallback,errorCallback){
	var headers = request.headers;
	if(requestData.headers){
		for(var i in requestData.headers){
			headers[i] = requestData.headers[i];
		}
		if(params){
			for(var key in params){
				for(var i in requestData.headers){
					if(key == i){
						headers[i] = params[key];
					}
				}
			}
		}
	}
	var url = requestData.url;
	if(requestData.param){
		if(!pathValue){
			layer.msg("参数错误",{icon:0});
			return;
		}
		url = url.replaceAll('{'+requestData.param+'}', pathValue)
	}
	if(request.urls.accountLogin.url == url){
		delete headers.Authorization;
	}
	
	if(requestData.method.toUpperCase() == 'GET'){
		$.ajax({
			url: url,
			type: requestData.method,
			data: params?params:{},
			headers: headers,
			dataType:'json',
			success:function(res){
				if(successCallback){
					successCallback(res)
				}
			},
			error:function(res){
				if(errorCallback){
					errorCallback(res);
				}
			}
		});
	}else{
		$.ajax({
			url: url,
			type: requestData.method,
			data: params?JSON.stringify(params):{},
			headers: headers,
			contentType: 'application/json;charset=UTF-8',
			cache: false,
			async:true,
			dataType:'json',
			success:function(res){
				if(successCallback){
					successCallback(res)
				}
			},
			error:function(res){
				if(errorCallback){
					errorCallback(res);
				}
			}
		});
	}
}

function executeApi($,params,successCallback,errorCallback){
	var url = params.url;
	var type = params.type;
	var data = params.data;
	var headers = params.headers;
	var dataType = params.dataType;
	var contentType = params.contentType;
	var apiParamType = params.apiParamType;
	if(type.toUpperCase() == 'GET'){
		if(apiParamType == 1){
			$.ajax({
				url: url,
				type: type,
				data: data?data:{},
				headers: headers,
				dataType:'json',
				success:function(res){
					if(successCallback){
						successCallback(res)
					}
				},
				error:function(res){
					if(errorCallback){
						errorCallback(res);
					}
				}
			});
		}else{
			$.ajax({
				url: url,
				type: type,
				data: data?JSON.stringify(data):{},
				headers: headers,
				contentType: contentType?(contentType+';charset=UTF-8'):'application/json;charset=UTF-8',
				cache: false,
				async:true,
				dataType:'json',
				success:function(res){
					if(successCallback){
						successCallback(res)
					}
				},
				error:function(res){
					if(errorCallback){
						errorCallback(res);
					}
				}
			});
		}
	}else{
		if(apiParamType ==2){
			$.ajax({
				url: url,
				type: type,
				data: data,
				headers: headers,
				contentType: false,
				processData: false,
				cache: false,
				async:true,
				dataType:'json',
				success:function(res){
					if(successCallback){
						successCallback(res)
					}
				},
				error:function(res){
					if(errorCallback){
						errorCallback(res);
					}
				}
			});
		}else{
			$.ajax({
				url: url,
				type: type,
				data: data?JSON.stringify(data):{},
				headers: headers,
				contentType: contentType?(contentType+';charset=UTF-8'):'application/json;charset=UTF-8',
				cache: false,
				async:true,
				dataType:'json',
				success:function(res){
					if(successCallback){
						successCallback(res)
					}
				},
				error:function(res){
					if(errorCallback){
						errorCallback(res);
					}
				}
			});
		}
	}
}