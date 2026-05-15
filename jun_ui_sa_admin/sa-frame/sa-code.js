// RuoYi 动态菜单 + 用户信息加载
// 替代原有静态菜单配置

// ================================= 基本信息 =================================
sa_admin.title = "若依管理系统";
sa_admin.logo = 'sa-frame/admin-logo.png';
sa_admin.icon = 'sa-frame/admin-logo.png';

// ================================= Token 检查 =================================
(function() {
    var token = ruoyi.getToken();
    if (!token) {
        top.location.href = 'login.html';
        return;
    }

    // 加载用户信息
    ruoyi.get('/getInfo', function(res) {
        // 存储权限
        var permissions = res.permissions || [];
        var roles = (res.roles || []);
        ruoyi.setPermissions(permissions);
        ruoyi.setRoles(roles);
        sa.setAuth(permissions);

        // 存储用户信息
        var user = res.user || {};
        localStorage.setItem('ruoyi_user', JSON.stringify(user));

        // 设置用户头像和昵称
        sa_admin.user = {
            username: user.nickName || user.userName || 'Admin',
            avatar: user.avatar ? ('/prod-api' + user.avatar) : 'sa-frame/admin-logo.png'
        };

        // 加载菜单
        ruoyi.get('/getRouters', function(menuRes) {
            var routers = menuRes.data || [];
            var menuList = ruoyi.convertRoutersToMenuList(routers);
            sa_admin.setMenuList(menuList);
            sa_admin.init();
        }, function() {
            // 菜单加载失败也要初始化
            sa_admin.setMenuList([]);
            sa_admin.init();
        });

    }, function() {
        // 获取用户信息失败，跳转登录
        ruoyi.removeToken();
        top.location.href = 'login.html';
    });
})();

// ================================= 下拉菜单配置 =================================
sa_admin.dropList = [
    {
        name: '个人中心',
        click: function() {
            sa_admin.showTab({
                id: 'user-profile',
                name: '个人中心',
                url: 'sa-view/system/user/profile/index.html'
            });
        }
    },
    {
        name: '退出登录',
        click: function() {
            sa.confirm('确定注销并退出系统吗？', function() {
                ruoyi.post('/logout', function() {
                    ruoyi.removeToken();
                    ruoyi.setPermissions([]);
                    ruoyi.setRoles([]);
                    sa.clearAuth();
                    localStorage.removeItem('ruoyi_user');
                    top.location.href = 'login.html';
                }, function() {
                    // 即使请求失败也清除本地数据
                    ruoyi.removeToken();
                    top.location.href = 'login.html';
                });
            });
        }
    }
];
