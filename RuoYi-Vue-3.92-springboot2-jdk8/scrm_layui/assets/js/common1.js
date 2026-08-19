/** EasyWeb iframe v3.1.8 date:2020-05-04 License By http://xiaonuo.vip */
layui.config({  // common.js是配置layui扩展模块的目录，每个页面都需要引入
    version: '318',   // 更新组件缓存，设为true不缓存，也可以设一个固定值
    base: getProjectUrl() + 'assets/module/',
    pageTabs: false,  // 默认关闭多标签
    // 请求完成后预处理
    ajaxSuccessBefore: function (res, url, obj) {
        //关闭加载层
        layui.layer.closeAll('loading');
        if(obj.param.dataType === "html") {
            return true;
        } else {
            return handleNetworkError(res);
        }
    }
}).extend({
    steps: 'steps/steps',
    notice: 'notice/notice',
    cascader: 'cascader/cascader',
    dropdown: 'dropdown/dropdown',
    fileChoose: 'fileChoose/fileChoose',
    Split: 'Split/Split',
    Cropper: 'Cropper/Cropper',
    tagsInput: 'tagsInput/tagsInput',
    citypicker: 'city-picker/city-picker',
    introJs: 'introJs/introJs',
    zTree: 'zTree/zTree',
    iconPicker: 'iconPicker/iconPicker',
    xnUtil: 'xnUtil/xnUtil'
}).use(['layer', 'admin', 'table', 'xnUtil', 'notice'], function () {
    var $ = layui.jquery;
    var admin = layui.admin;
    var xnUtil = layui.xnUtil;
    var table = layui.table;
    //没有默认主题时，设置默认主题
    var defaultTheme = admin.getTempData('defaultTheme', true);
    if(defaultTheme === undefined) {
        admin.changeTheme('theme-snowy');
    }
    //表格重载时ajaxSuccessBefore无法捕获ajax结果，使用此处判断
    $.ajaxSetup({
        timeout : 10000, //超时时间设置，单位毫秒，默认10秒
        complete: function (XMLHttpRequest, textStatus) {
            //关闭加载层
            layui.layer.closeAll('loading');
            if(XMLHttpRequest.responseJSON !== null && XMLHttpRequest.responseJSON !== undefined) {
                if(!XMLHttpRequest.responseJSON.success) {
                    // 登录已过期，请重新登录
                    if(XMLHttpRequest.responseJSON.code === 1011008) {
                        window.location.href = "/";
                    }
                }
            }
        }
    });

    // 页面载入就检查按钮权限
    xnUtil.renderPerm();
});

/** 获取当前项目的根路径，通过获取layui.js全路径截取assets之前的地址 */
function getProjectUrl() {
    var layuiDir = layui.cache.dir;
    if (!layuiDir) {
        var js = document.scripts, last = js.length - 1, src;
        for (var i = last; i > 0; i--) {
            if (js[i].readyState === 'interactive') {
                src = js[i].src;
                break;
            }
        }
        var jsPath = src || js[last].src;
        layuiDir = jsPath.substring(0, jsPath.lastIndexOf('/') + 1);
    }
    var projectUrl = layuiDir.substring(0, layuiDir.indexOf('assets'));
    return projectUrl;
}

function supportPreview(suffix) {
    var result = [];
    result.push('pdf');
    result.push('doc');
    result.push('docx');
    result.push('xls');
    result.push('xlsx');
    result.push('ppt');
    result.push('pptx');
    result.push('jpg');
    result.push('png');
    result.push('jpeg');
    result.push('tif');
    result.push('bmp');
    result.push('gif');
    result.push('txt');
    return result.indexOf(suffix) !== -1;
}

function isDoc(suffix) {
    var result = [];
    result.push('doc');
    result.push('docx');
    result.push('xls');
    result.push('xlsx');
    result.push('ppt');
    result.push('pptx');
    return result.indexOf(suffix) !== -1;
}

// 网络错误处理
function handleNetworkError(res) {
    if(res.code !== 0) {
        if(res.success !== null && res.success !== undefined) {
            if(!res.success) {
                // 登录已过期，请重新登录
                if(res.code === 1011008 || res.code === 1011004) {
                    window.location.href = "/";
                } else {
                    if(res.message) {
                        layui.notice.msg(res.message, {icon: 2});
                    } else {
                        layui.notice.msg("服务器出现异常，请联系管理员", {icon: 2});
                    }
                    return false;
                }
            }
        } else {
            if(res.code === 500) {
                if(res.msg === "error") {
                    layui.notice.msg("服务器出现异常，请联系管理员", {icon: 2});
                    return false;
                }
            }

            if(res.code === 404) {
                if(res.msg === "error") {
                    layui.notice.msg("资源路径不存在，请检查请求地址", {icon: 2});
                    return false;
                }
            }
        }
    } else {
        //网络错误
        if(res.msg === "timeout") {
            layui.notice.msg("请求超时，请检查网络状态", {icon: 2});
            return false;
        }
        if(res.msg === "error") {
            layui.notice.msg("网络错误，请检查网络连接", {icon: 2});
            return false;
        }
    }
    return true;
}


