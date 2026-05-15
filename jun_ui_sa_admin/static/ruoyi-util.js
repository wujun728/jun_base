/**
 * RuoYi 核心桥接库
 * 替代 ruoyi-ui 中分散在 Vuex/Axios/utils 中的功能
 * 用于 jun_ui_sa_admin (纯HTML/JS iframe框架) 对接 RuoYi 后端
 */
var ruoyi = {};

// ========================= Token 管理 =========================
(function() {
    var TokenKey = 'Admin-Token';

    ruoyi.getToken = function() {
        var pairs = document.cookie.split(';');
        for (var i = 0; i < pairs.length; i++) {
            var pair = pairs[i].trim();
            if (pair.indexOf(TokenKey + '=') === 0) {
                return decodeURIComponent(pair.substring(TokenKey.length + 1));
            }
        }
        return '';
    };

    ruoyi.setToken = function(token) {
        document.cookie = TokenKey + '=' + encodeURIComponent(token) + '; path=/';
    };

    ruoyi.removeToken = function() {
        document.cookie = TokenKey + '=; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT';
    };
})();

// ========================= HTTP 请求封装 =========================
(function() {
    var baseURL = '/prod-api';
    var isReloginShow = false;

    /**
     * 核心请求方法
     * @param {Object} options - { url, method, data, params, headers, responseType, success, error }
     */
    ruoyi.request = function(options) {
        var url = options.url || '';
        var method = (options.method || 'get').toUpperCase();
        var headers = options.headers || {};
        var responseType = options.responseType || '';

        // 拼接baseURL
        if (url.indexOf('http') !== 0) {
            url = baseURL + url;
        }

        // 添加token
        var token = ruoyi.getToken();
        if (token && headers.isToken !== false) {
            headers['Authorization'] = 'Bearer ' + token;
        }
        delete headers.isToken;

        // 默认Content-Type
        if (!headers['Content-Type']) {
            headers['Content-Type'] = 'application/json;charset=utf-8';
        }

        var ajaxOpts = {
            url: url,
            type: method,
            headers: headers,
            dataType: responseType === 'blob' ? undefined : 'json',
            xhrFields: responseType === 'blob' ? { responseType: 'blob' } : {},
            success: function(res, textStatus, xhr) {
                if (responseType === 'blob') {
                    if (options.success) options.success(xhr.response || res);
                    return;
                }
                // 处理响应码
                var code = res.code || 200;
                var msg = res.msg || '';
                if (code === 200) {
                    if (options.success) options.success(res);
                } else if (code === 401) {
                    if (!isReloginShow) {
                        isReloginShow = true;
                        ELEMENT.MessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录', '系统提示', {
                            confirmButtonText: '重新登录',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(function() {
                            isReloginShow = false;
                            ruoyi.removeToken();
                            top.location.href = '/login.html';
                        }).catch(function() {
                            isReloginShow = false;
                        });
                    }
                    if (options.error) options.error(msg || '无效的会话，或者会话已过期，请重新登录。');
                } else if (code === 500) {
                    ELEMENT.Message({ message: msg, type: 'error' });
                    if (options.error) options.error(msg);
                } else if (code === 601) {
                    ELEMENT.Message({ message: msg, type: 'warning' });
                    if (options.error) options.error(msg);
                } else {
                    ELEMENT.Notification.error({ title: msg });
                    if (options.error) options.error(msg);
                }
            },
            error: function(xhr, textStatus, errorThrown) {
                var message = '后端接口连接异常';
                if (textStatus === 'timeout') {
                    message = '系统接口请求超时';
                } else if (xhr.status) {
                    message = '系统接口' + xhr.status + '异常';
                }
                ELEMENT.Message({ message: message, type: 'error', duration: 5000 });
                if (options.error) options.error(message);
            }
        };

        // 处理参数
        if (method === 'GET' || method === 'DELETE') {
            if (options.params) {
                var qs = ruoyi.tansParams(options.params);
                if (qs) {
                    ajaxOpts.url += (ajaxOpts.url.indexOf('?') > -1 ? '&' : '?') + qs.slice(0, -1);
                }
            }
        } else {
            if (options.data) {
                if (headers['Content-Type'] && headers['Content-Type'].indexOf('application/json') > -1) {
                    ajaxOpts.data = JSON.stringify(options.data);
                } else {
                    ajaxOpts.data = options.data;
                }
            }
        }

        $.ajax(ajaxOpts);
    };

    /**
     * GET 请求
     */
    ruoyi.get = function(url, params, success, error) {
        if (typeof params === 'function') {
            error = success;
            success = params;
            params = {};
        }
        ruoyi.request({ url: url, method: 'get', params: params, success: success, error: error });
    };

    /**
     * POST 请求
     */
    ruoyi.post = function(url, data, success, error) {
        if (typeof data === 'function') {
            error = success;
            success = data;
            data = {};
        }
        ruoyi.request({ url: url, method: 'post', data: data, success: success, error: error });
    };

    /**
     * PUT 请求
     */
    ruoyi.put = function(url, data, success, error) {
        if (typeof data === 'function') {
            error = success;
            success = data;
            data = {};
        }
        ruoyi.request({ url: url, method: 'put', data: data, success: success, error: error });
    };

    /**
     * DELETE 请求
     */
    ruoyi.del = function(url, params, success, error) {
        if (typeof params === 'function') {
            error = success;
            success = params;
            params = {};
        }
        // DELETE with body support
        if (params && !$.isEmptyObject(params)) {
            ruoyi.request({ url: url, method: 'delete', data: params, success: success, error: error });
        } else {
            ruoyi.request({ url: url, method: 'delete', success: success, error: error });
        }
    };
})();

// ========================= 文件下载 =========================
(function() {
    ruoyi.download = function(url, params, filename) {
        var loadingInstance = ELEMENT.Loading.service({
            text: '正在下载数据，请稍候',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
        });
        ruoyi.request({
            url: url,
            method: 'post',
            data: params,
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            responseType: 'blob',
            success: function(data) {
                if (data.type && data.type !== 'application/json') {
                    var blob = new Blob([data]);
                    var downloadUrl = window.URL.createObjectURL(blob);
                    var a = document.createElement('a');
                    a.href = downloadUrl;
                    a.download = filename || 'download';
                    document.body.appendChild(a);
                    a.click();
                    document.body.removeChild(a);
                    window.URL.revokeObjectURL(downloadUrl);
                } else {
                    ELEMENT.Message.error('下载文件出现错误，请联系管理员！');
                }
                loadingInstance.close();
            },
            error: function() {
                ELEMENT.Message.error('下载文件出现错误，请联系管理员！');
                loadingInstance.close();
            }
        });
    };

    ruoyi.downloadGet = function(url, filename) {
        var token = ruoyi.getToken();
        var xhr = new XMLHttpRequest();
        var fullUrl = url.indexOf('http') === 0 ? url : '/prod-api' + url;
        xhr.open('GET', fullUrl, true);
        xhr.responseType = 'blob';
        xhr.setRequestHeader('Authorization', 'Bearer ' + token);
        var loadingInstance = ELEMENT.Loading.service({
            text: '正在下载数据，请稍候',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
        });
        xhr.onload = function() {
            if (xhr.status === 200) {
                var blob = new Blob([xhr.response]);
                var downloadFilename = filename;
                var disposition = xhr.getResponseHeader('content-disposition');
                if (!downloadFilename && disposition) {
                    var match = disposition.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/);
                    if (match) downloadFilename = decodeURIComponent(match[1].replace(/['"]/g, ''));
                }
                var downloadUrl = window.URL.createObjectURL(blob);
                var a = document.createElement('a');
                a.href = downloadUrl;
                a.download = downloadFilename || 'download';
                document.body.appendChild(a);
                a.click();
                document.body.removeChild(a);
                window.URL.revokeObjectURL(downloadUrl);
            } else {
                ELEMENT.Message.error('下载文件出现错误');
            }
            loadingInstance.close();
        };
        xhr.onerror = function() {
            ELEMENT.Message.error('下载文件出现错误');
            loadingInstance.close();
        };
        xhr.send();
    };
})();

// ========================= 工具函数 =========================
(function() {
    /**
     * 日期格式化
     */
    ruoyi.parseTime = function(time, pattern) {
        if (!time) return null;
        var format = pattern || '{y}-{m}-{d} {h}:{i}:{s}';
        var date;
        if (typeof time === 'object') {
            date = time;
        } else {
            if ((typeof time === 'string') && (/^[0-9]+$/.test(time))) {
                time = parseInt(time);
            } else if (typeof time === 'string') {
                time = time.replace(new RegExp(/-/gm), '/').replace('T', ' ').replace(new RegExp(/\.[\d]{3}/gm), '');
            }
            if ((typeof time === 'number') && (time.toString().length === 10)) {
                time = time * 1000;
            }
            date = new Date(time);
        }
        var formatObj = {
            y: date.getFullYear(),
            m: date.getMonth() + 1,
            d: date.getDate(),
            h: date.getHours(),
            i: date.getMinutes(),
            s: date.getSeconds(),
            a: date.getDay()
        };
        return format.replace(/{(y|m|d|h|i|s|a)+}/g, function(result, key) {
            var value = formatObj[key];
            if (key === 'a') return ['日', '一', '二', '三', '四', '五', '六'][value];
            if (result.length > 0 && value < 10) value = '0' + value;
            return value || 0;
        });
    };

    /**
     * 构造树型结构数据
     */
    ruoyi.handleTree = function(data, id, parentId, children) {
        var config = {
            id: id || 'id',
            parentId: parentId || 'parentId',
            childrenList: children || 'children'
        };
        var childrenListMap = {};
        var nodeIds = {};
        var tree = [];
        for (var i = 0; i < data.length; i++) {
            var d = data[i];
            var pid = d[config.parentId];
            if (!childrenListMap[pid]) childrenListMap[pid] = [];
            nodeIds[d[config.id]] = d;
            childrenListMap[pid].push(d);
        }
        for (var i = 0; i < data.length; i++) {
            var d = data[i];
            var pid = d[config.parentId];
            if (!nodeIds[pid]) tree.push(d);
        }
        function adapt(o) {
            if (childrenListMap[o[config.id]] !== undefined) {
                o[config.childrenList] = childrenListMap[o[config.id]];
            }
            if (o[config.childrenList]) {
                for (var i = 0; i < o[config.childrenList].length; i++) {
                    adapt(o[config.childrenList][i]);
                }
            }
        }
        for (var i = 0; i < tree.length; i++) adapt(tree[i]);
        return tree;
    };

    /**
     * 添加日期范围参数
     */
    ruoyi.addDateRange = function(params, dateRange, propName) {
        var search = JSON.parse(JSON.stringify(params));
        search.params = (typeof search.params === 'object' && search.params !== null && !Array.isArray(search.params)) ? search.params : {};
        dateRange = Array.isArray(dateRange) ? dateRange : [];
        if (typeof propName === 'undefined') {
            search.params['beginTime'] = dateRange[0];
            search.params['endTime'] = dateRange[1];
        } else {
            search.params['begin' + propName] = dateRange[0];
            search.params['end' + propName] = dateRange[1];
        }
        return search;
    };

    /**
     * 回显数据字典
     */
    ruoyi.selectDictLabel = function(datas, value) {
        if (value === undefined) return '';
        var actions = [];
        Object.keys(datas).some(function(key) {
            if (datas[key].value == ('' + value)) {
                actions.push(datas[key].label);
                return true;
            }
        });
        if (actions.length === 0) actions.push(value);
        return actions.join('');
    };

    /**
     * 回显数据字典（字符串、数组）
     */
    ruoyi.selectDictLabels = function(datas, value, separator) {
        if (value === undefined || value.length === 0) return '';
        if (Array.isArray(value)) value = value.join(',');
        var actions = [];
        var currentSeparator = separator === undefined ? ',' : separator;
        var temp = value.split(currentSeparator);
        Object.keys(temp).some(function(val) {
            var match = false;
            Object.keys(datas).some(function(key) {
                if (datas[key].value == ('' + temp[val])) {
                    actions.push(datas[key].label + currentSeparator);
                    match = true;
                }
            });
            if (!match) actions.push(temp[val] + currentSeparator);
        });
        return actions.join('').substring(0, actions.join('').length - 1);
    };

    /**
     * 参数处理
     */
    ruoyi.tansParams = function(params) {
        var result = '';
        for (var propName in params) {
            if (!params.hasOwnProperty(propName)) continue;
            var value = params[propName];
            var part = encodeURIComponent(propName) + '=';
            if (value !== null && value !== '' && typeof value !== 'undefined') {
                if (typeof value === 'object') {
                    for (var key in value) {
                        if (!value.hasOwnProperty(key)) continue;
                        if (value[key] !== null && value[key] !== '' && typeof value[key] !== 'undefined') {
                            var subPart = encodeURIComponent(propName + '[' + key + ']') + '=';
                            result += subPart + encodeURIComponent(value[key]) + '&';
                        }
                    }
                } else {
                    result += part + encodeURIComponent(value) + '&';
                }
            }
        }
        return result;
    };

    /**
     * 表单重置 - 供 Vue 实例中调用
     */
    ruoyi.resetForm = function(vm, refName) {
        if (vm.$refs[refName]) {
            vm.$refs[refName].resetFields();
        }
    };

    /**
     * 获取URL参数
     */
    ruoyi.getQueryParam = function(name) {
        var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return decodeURIComponent(r[2]);
        return null;
    };
})();

// ========================= 字典缓存 =========================
(function() {
    var dictCachePrefix = 'ruoyi_dict_';

    /**
     * 获取字典数据
     */
    ruoyi.getDicts = function(dictType, callback) {
        // 先尝试缓存
        var cached = localStorage.getItem(dictCachePrefix + dictType);
        if (cached) {
            try {
                var data = JSON.parse(cached);
                if (data && data.length > 0) {
                    if (callback) callback(data);
                    return;
                }
            } catch(e) {}
        }
        // 从API获取
        ruoyi.get('/system/dict/data/type/' + dictType, function(res) {
            var data = res.data || [];
            // 转换为 {value, label} 格式
            var list = [];
            for (var i = 0; i < data.length; i++) {
                list.push({
                    value: data[i].dictValue,
                    label: data[i].dictLabel,
                    listClass: data[i].listClass || '',
                    cssClass: data[i].cssClass || ''
                });
            }
            localStorage.setItem(dictCachePrefix + dictType, JSON.stringify(list));
            if (callback) callback(list);
        });
    };

    /**
     * 批量加载字典到 Vue 实例的 data 上
     * @param {Vue} vm - Vue 实例
     * @param {Array} dictTypes - 字典类型数组
     */
    ruoyi.loadDicts = function(vm, dictTypes) {
        if (!dictTypes || !dictTypes.length) return;
        for (var i = 0; i < dictTypes.length; i++) {
            (function(dictType) {
                // 将 sys_normal_disable 转为 sysNormalDisableOptions 或直接用 dict.xxx
                var key = dictType.replace(/_([a-z])/g, function(m, c) { return c.toUpperCase(); }) + 'Options';
                if (!vm[key]) vm.$set(vm, key, []);
                ruoyi.getDicts(dictType, function(data) {
                    vm[key] = data;
                });
            })(dictTypes[i]);
        }
    };

    /**
     * 清除所有字典缓存
     */
    ruoyi.clearDictCache = function() {
        var keys = [];
        for (var i = 0; i < localStorage.length; i++) {
            var key = localStorage.key(i);
            if (key.indexOf(dictCachePrefix) === 0) keys.push(key);
        }
        for (var i = 0; i < keys.length; i++) {
            localStorage.removeItem(keys[i]);
        }
    };
})();

// ========================= 权限指令 =========================
(function() {
    /**
     * 检查是否有权限
     */
    ruoyi.hasPermi = function(permi) {
        var permissions = JSON.parse(localStorage.getItem('ruoyi_permissions') || '[]');
        if (permissions.indexOf('*:*:*') > -1) return true;
        if (typeof permi === 'string') {
            return permissions.indexOf(permi) > -1;
        }
        if (Array.isArray(permi)) {
            for (var i = 0; i < permi.length; i++) {
                if (permissions.indexOf(permi[i]) > -1) return true;
            }
        }
        return false;
    };

    /**
     * 检查是否有角色
     */
    ruoyi.hasRole = function(role) {
        var roles = JSON.parse(localStorage.getItem('ruoyi_roles') || '[]');
        if (roles.indexOf('admin') > -1) return true;
        if (typeof role === 'string') {
            return roles.indexOf(role) > -1;
        }
        if (Array.isArray(role)) {
            for (var i = 0; i < role.length; i++) {
                if (roles.indexOf(role[i]) > -1) return true;
            }
        }
        return false;
    };

    /**
     * 存储权限和角色
     */
    ruoyi.setPermissions = function(permissions) {
        localStorage.setItem('ruoyi_permissions', JSON.stringify(permissions || []));
    };
    ruoyi.setRoles = function(roles) {
        localStorage.setItem('ruoyi_roles', JSON.stringify(roles || []));
    };

    // Vue 自定义指令 v-hasPermi
    if (window.Vue) {
        Vue.directive('hasPermi', {
            inserted: function(el, binding) {
                var value = binding.value;
                if (value && value.length > 0) {
                    if (!ruoyi.hasPermi(value)) {
                        el.parentNode && el.parentNode.removeChild(el);
                    }
                }
            }
        });
        Vue.directive('hasRole', {
            inserted: function(el, binding) {
                var value = binding.value;
                if (value && value.length > 0) {
                    if (!ruoyi.hasRole(value)) {
                        el.parentNode && el.parentNode.removeChild(el);
                    }
                }
            }
        });
    }
})();

// ========================= 菜单转换 =========================
(function() {
    /**
     * 将 /getRouters 返回的菜单数据转换为 sa-admin 的菜单格式
     * RuoYi 格式: { name, path, component, meta: { title, icon }, children, hidden }
     * sa-admin 格式: { id, name, icon, url, childList }
     */
    ruoyi.convertRoutersToMenuList = function(routers, parentPath) {
        var menuList = [];
        parentPath = parentPath || '';

        for (var i = 0; i < routers.length; i++) {
            var route = routers[i];
            if (route.hidden) continue;

            var menu = {};
            menu.id = 'menu-' + (parentPath ? parentPath + '-' : '') + route.path;
            menu.name = (route.meta && route.meta.title) ? route.meta.title : route.name || route.path;
            menu.icon = (route.meta && route.meta.icon) ? ruoyi.convertIcon(route.meta.icon) : '';

            var fullPath = parentPath;
            if (route.path.indexOf('/') === 0) {
                fullPath = route.path;
            } else {
                fullPath = parentPath ? (parentPath + '/' + route.path) : route.path;
            }

            // 有子菜单
            if (route.children && route.children.length > 0) {
                // Layout 或 ParentView 级别
                if (route.component === 'Layout' || route.component === 'ParentView' || !route.component) {
                    // 只有一个可见子菜单，且父级无 alwaysShow
                    var visibleChildren = [];
                    for (var j = 0; j < route.children.length; j++) {
                        if (!route.children[j].hidden) visibleChildren.push(route.children[j]);
                    }
                    if (visibleChildren.length === 1 && !route.alwaysShow) {
                        var child = visibleChildren[0];
                        var childPath = fullPath + '/' + child.path;
                        menu.name = (child.meta && child.meta.title) ? child.meta.title : child.name || child.path;
                        menu.icon = (child.meta && child.meta.icon) ? ruoyi.convertIcon(child.meta.icon) : menu.icon;
                        menu.url = ruoyi.resolveComponentUrl(child.component, childPath, child.meta);
                        menu.id = 'menu-' + childPath;
                    } else {
                        menu.childList = ruoyi.convertRoutersToMenuList(route.children, fullPath);
                    }
                } else {
                    // 有component也有children
                    menu.url = ruoyi.resolveComponentUrl(route.component, fullPath, route.meta);
                    menu.childList = ruoyi.convertRoutersToMenuList(route.children, fullPath);
                }
            } else {
                // 叶子节点
                if (route.component && route.component !== 'Layout' && route.component !== 'ParentView') {
                    menu.url = ruoyi.resolveComponentUrl(route.component, fullPath, route.meta);
                }
            }

            // InnerLink 处理
            if (route.component === 'InnerLink' && route.meta && route.meta.link) {
                menu.url = route.meta.link;
                menu.isBlank = true;
            }

            menuList.push(menu);
        }
        return menuList;
    };

    /**
     * 将 Vue 组件路径映射到 sa-view 下的 HTML 页面
     * 例如: system/user/index → sa-view/system/user/index.html
     */
    ruoyi.resolveComponentUrl = function(component, fullPath, meta) {
        if (!component || component === 'Layout' || component === 'ParentView' || component === 'InnerLink') {
            return '';
        }
        // 将 Vue 组件路径转换为 HTML 路径
        var htmlPath = 'sa-view/' + component.replace(/^\//, '') + '.html';
        return htmlPath;
    };

    /**
     * 将 RuoYi SVG 图标名转换为 Element UI 图标类名
     */
    ruoyi.convertIcon = function(icon) {
        if (!icon) return '';
        // RuoYi 使用自定义 svg 图标, 这里映射常用的
        var iconMap = {
            'system': 'el-icon-setting',
            'monitor': 'el-icon-monitor',
            'tool': 'el-icon-s-tools',
            'guide': 'el-icon-guide',
            'user': 'el-icon-user-solid',
            'peoples': 'el-icon-user',
            'tree-table': 'el-icon-s-grid',
            'tree': 'el-icon-s-operation',
            'dict': 'el-icon-notebook-2',
            'edit': 'el-icon-edit',
            'post': 'el-icon-s-custom',
            'message': 'el-icon-chat-dot-round',
            'log': 'el-icon-document',
            'online': 'el-icon-view',
            'job': 'el-icon-time',
            'druid': 'el-icon-data-analysis',
            'server': 'el-icon-s-platform',
            'redis': 'el-icon-s-data',
            'redis-list': 'el-icon-s-order',
            'code': 'el-icon-s-opportunity',
            'swagger': 'el-icon-document-checked',
            'form': 'el-icon-document-add',
            'build': 'el-icon-s-order',
            'list': 'el-icon-tickets',
            'checkbox': 'el-icon-finished',
            'component': 'el-icon-menu',
            'documentation': 'el-icon-document',
            'chart': 'el-icon-data-line',
            'clipboard': 'el-icon-document-copy',
            'example': 'el-icon-s-promotion',
            'excel': 'el-icon-document',
            'eye': 'el-icon-view',
            'eye-open': 'el-icon-view',
            'international': 'el-icon-place',
            'language': 'el-icon-place',
            'lock': 'el-icon-lock',
            'logininfor': 'el-icon-s-custom',
            'nested': 'el-icon-s-unfold',
            'number': 'el-icon-s-data',
            'password': 'el-icon-key',
            'pdf': 'el-icon-document',
            'phone': 'el-icon-phone',
            'question': 'el-icon-question',
            'rate': 'el-icon-star-off',
            'row': 'el-icon-s-unfold',
            'search': 'el-icon-search',
            'shopping': 'el-icon-shopping-cart-full',
            'size': 'el-icon-s-data',
            'skill': 'el-icon-magic-stick',
            'star': 'el-icon-star-on',
            'tab': 'el-icon-date',
            'table': 'el-icon-s-grid',
            'theme': 'el-icon-brush',
            'time': 'el-icon-time',
            'validCode': 'el-icon-key',
            'wechat': 'el-icon-chat-round',
            'zip': 'el-icon-document-remove',
            'bug': 'el-icon-warning',
            'date': 'el-icon-date',
            'color': 'el-icon-brush',
            'cascader': 'el-icon-s-unfold',
            'input': 'el-icon-edit',
            'textarea': 'el-icon-document',
            'select': 'el-icon-s-operation',
            'radio': 'el-icon-finished',
            'upload': 'el-icon-upload',
            'download': 'el-icon-download',
            'slider': 'el-icon-s-data',
            'switch': 'el-icon-open',
            'button': 'el-icon-s-flag',
            'icon': 'el-icon-picture',
            'link': 'el-icon-link',
            'email': 'el-icon-message',
            'money': 'el-icon-coin',
            'peoples': 'el-icon-user',
            'education': 'el-icon-s-marketing',
            '404': 'el-icon-warning-outline',
            'cache': 'el-icon-s-data'
        };
        if (iconMap[icon]) return iconMap[icon];
        // 如果已经是 el-icon 格式
        if (icon.indexOf('el-icon') === 0) return icon;
        return 'el-icon-menu';
    };
})();

// ========================= Vue 原型方法挂载 =========================
(function() {
    if (window.Vue) {
        Vue.prototype.$ruoyi = ruoyi;
        Vue.prototype.parseTime = ruoyi.parseTime;
        Vue.prototype.handleTree = ruoyi.handleTree;
        Vue.prototype.selectDictLabel = ruoyi.selectDictLabel;
        Vue.prototype.selectDictLabels = ruoyi.selectDictLabels;
        Vue.prototype.addDateRange = ruoyi.addDateRange;

        // $modal 方法
        Vue.prototype.$modal = {
            msg: function(content) { ELEMENT.Message.info(content); },
            msgError: function(content) { ELEMENT.Message.error(content); },
            msgSuccess: function(content) { ELEMENT.Message.success(content); },
            msgWarning: function(content) { ELEMENT.Message.warning(content); },
            alert: function(content) { ELEMENT.MessageBox.alert(content, '系统提示'); },
            alertError: function(content) { ELEMENT.MessageBox.alert(content, '系统提示', { type: 'error' }); },
            alertSuccess: function(content) { ELEMENT.MessageBox.alert(content, '系统提示', { type: 'success' }); },
            alertWarning: function(content) { ELEMENT.MessageBox.alert(content, '系统提示', { type: 'warning' }); },
            confirm: function(content) {
                return ELEMENT.MessageBox.confirm(content, '系统提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                });
            },
            prompt: function(content) {
                return ELEMENT.MessageBox.prompt(content, '系统提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消'
                });
            },
            loading: function(content) {
                return ELEMENT.Loading.service({
                    lock: true,
                    text: content || '加载中...',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
            },
            closeLoading: function() {}
        };

        // resetForm
        Vue.prototype.resetForm = function(refName) {
            if (this.$refs[refName]) {
                this.$refs[refName].resetFields();
            }
        };

        // Element size
        Vue.prototype.$ELEMENT = { size: 'small', zIndex: 3000 };
    }
})();

// 对外暴露
window.ruoyi = ruoyi;
