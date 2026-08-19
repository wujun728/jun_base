var serverUrl = "http://127.0.0.1:8888"; 

var sysName = "API开发后台";
var ownerName = "wujun";
var projectName = "lowcode-api-web";

var authorizationStorageName = projectName.replaceAll('-','_')+"_authorization";
var Authorization=localStorage.getItem(authorizationStorageName);

var lockStorageName = projectName.replaceAll('-','_')+'_lock_flag';
var lockFlag = localStorage.getItem(lockStorageName);

var loginRememberStorageName = projectName.replaceAll('-','_')+"_login_remember";
var loginRemeber=localStorage.getItem(loginRememberStorageName);
var pass_salt="n3im7uzqcqlenz464zyydpiqiqrvfwvo";

function getUrlParam(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg); //获取url中"?"符后的字符串并正则匹配
	var context = "";
	if (r != null){
		context = decodeURI(r[2]);
	}else{
		reg = null;
		r = null;
	}
	return context == null || context == "" || context == "undefined" ? "" : context;
}

layui.use(['layer','element'], function() {
	var $ = layui.jquery,
    layer = layui.layer,
    element = layui.element;

	$.ajaxSetup({
	    complete: function (request, status) {
	        if (typeof (request) != 'undefined') {
	        	if(request.statusText=="error"){
	        		layer.msg("请求异常", {icon:2});
	        		return;
	        	}
	            var code = request.responseJSON?request.responseJSON.code:"";
	            if(code && code!=200){
	            	if(code == 402 || code == 401){
	            		layer.msg(request.responseJSON.message, {icon:2,time:1000},function(){
	            			location.href="/"+projectName+"/login.html?redirect="+encodeURI(window.location.href)
	            		});
	            	}else{
	            		layer.msg(request.responseJSON.message, {icon:2});
	            	}
	            }
	        }
	    },
	    error: function (jqXHR, textStatus, errorThrown) {
	    	var errorText = '';
	    	if(jqXHR.responseJSON){
	    		errorText = jqXHR.responseJSON.error;
	    	}
	        switch (jqXHR.status) {
				case (502):
					errorText = '系统异常';
					break;
	            case (500):
	                errorText = '系统异常';
	                break;
	            case (400):
	            	errorText = '请求参数错误';
	                break;
	            case (401):
	                break;
	            case (403):
	                errorText = '您没有访问权限';
	                break;
	            case (404):
	                errorText = '请检查您的访问地址是否存在';
	                break;
	            case (405):
					errorText = '请检查您的请求方式是否错误';
	                break;
	            default:
	            	break;
	        }
	        if (jqXHR.status > 200){
	        	layer.msg(errorText,{icon:2});
	        }
	    }
	});
});


function copyText($, text,func){
	if(!text || !$.trim(text)){
		return;
	}
	var $temp = $("<textarea>");
	$("body").append($temp);
	$temp.val(text).select();
	document.execCommand("copy");
	$temp.remove();
	if(func){
		func();
	}
}

function toUnderscoreToCamelCase(str){
	if (str == ''){
		return '';
	  }
	  var a = str.split('_');
	  for (var i = 1; i < a.length; i++) {    //如果从第一个单词开始首字母大写就改成 i=0；
		a[i] = a[i][0].toUpperCase()+a[i].slice(1);  //首字母大写
	  }
	  return a.join('');
}

function toLowerCaseFirstOne(str){
	if (str == ''){
		return '';
	  }
	var strnew = str.substring(0,1).toLowerCase()+str.substring(1);
	return strnew;
}

function toUpperCaseFirstOne(str){
	if (str == ''){
		return '';
	  }
	var strnew = str.substring(0,1).toUpperCase()+str.substring(1);
	return strnew;
}