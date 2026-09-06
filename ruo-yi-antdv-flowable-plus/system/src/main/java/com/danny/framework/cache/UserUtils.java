package com.danny.framework.cache;

import com.danny.common.constant.Constants;
import com.danny.common.core.domain.entity.SysMenu;
import com.danny.common.core.domain.entity.SysRole;
import com.danny.common.core.domain.entity.SysUser;
import com.danny.common.core.domain.model.LoginUser;
import com.danny.common.core.redis.RedisCache;
import com.danny.common.utils.SecurityUtils;
import com.danny.common.utils.spring.SpringUtils;
import com.danny.system.service.ISysUserService;
import com.danny.system.service.SysMenuService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

/**
 * 用户工具类
 *
 * @author ruoyi
 */
@Component
public class UserUtils implements CacheUtil {

    @Override
    public String getCacheName() {
        return CACHE_NAME;
    }

    @Override
    public String getRemark() {
        return "用户信息";
    }

    @Override
    public String getCacheId() {
        return "userUtils";
    }

    @Override
    public String getCacheValue(String key) {
        return REDIS_CACHE.getCacheMapValue(CACHE_NAME, key).toString();
    }

    @Override
    public Collection<String> getCacheKeys() {
        return REDIS_CACHE.getCacheMapKeys(CACHE_NAME);
    }

    @Override
    public void clearCacheByKeys(String... keys) {
        REDIS_CACHE.deleteCacheMapValue(CACHE_NAME, keys);
    }

    private static final String CACHE_NAME = "userCache";

    private static final RedisCache REDIS_CACHE;

    private static final ISysUserService sysUserService;

    private static final SysMenuService sysMenuService;

    static {
        REDIS_CACHE = SpringUtils.getBean(RedisCache.class);
        sysUserService = SpringUtils.getBean(ISysUserService.class);
        sysMenuService = SpringUtils.getBean(SysMenuService.class);
    }

    @Override
    public void clearCache() {
        clearUserCache();
    }

    /**
     * 清空用户
     */
    public static void clearUserCache() {
        Collection<String> keys = REDIS_CACHE.keys(CACHE_NAME);
        REDIS_CACHE.deleteObject(keys);
    }

    /**
     * 根据userId获取用户对象
     *
     * @param userId
     * @return
     */
    public static SysUser getSysUser(String userId) {
        SysUser sysUser = REDIS_CACHE.getCacheMapValue(CACHE_NAME, Constants.SYS_USER_KEY + userId);
        if (sysUser == null) {
            sysUser = sysUserService.get(userId);
            if (null != sysUser) {
                REDIS_CACHE.setCacheMapValue(CACHE_NAME, Constants.SYS_USER_KEY + userId, sysUser);
                REDIS_CACHE.setCacheMapValue(CACHE_NAME, Constants.SYS_USER_UN_KEY + sysUser.getUserName(), sysUser);
            }
        }
        return sysUser;
    }

    /**
     * 根据userName获取用户对象
     *
     * @param userName
     * @return
     */
    public static SysUser getSysUserByUserName(String userName) {
        SysUser sysUser = REDIS_CACHE.getCacheMapValue(CACHE_NAME, Constants.SYS_USER_UN_KEY + userName);
        if (sysUser == null) {
            sysUser = sysUserService.selectUserByUserName(userName);
            REDIS_CACHE.setCacheMapValue(CACHE_NAME, Constants.SYS_USER_KEY + sysUser.getId(), sysUser);
            REDIS_CACHE.setCacheMapValue(CACHE_NAME, Constants.SYS_USER_UN_KEY + userName, sysUser);
        }
        return sysUser;
    }

    /**
     * 清除指定用户缓存
     *
     * @param sysUser
     */
    public static void clearCache(SysUser sysUser) {
        REDIS_CACHE.deleteCacheMapValue(CACHE_NAME, Constants.SYS_USER_KEY + sysUser.getId(), Constants.SYS_USER_UN_KEY + sysUser.getUserName());
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    public static SysUser getUser() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        return getSysUserByUserName(loginUser.getUsername());
    }

    /**
     * 获取当前用户角色列表
     *
     * @return
     */
    public static List<SysRole> getSysRoleList() {
        SysUser sysUser = getUser();
        if (sysUser.isAdmin()) {
            return RoleUtils.getAllSysRole();
        } else {
            return sysUser.getSysRoles();
        }
    }

    /**
     * 获取当前用户授权菜单
     *
     * @return
     */
    public static List<SysMenu> getSysMenuList() {
        SysUser sysUser = getUser();
        return sysMenuService.selectMenuList(sysUser.getId());
    }

    public static boolean hasRole(String roleCode) {
        List<SysRole> roleList = getSysRoleList();
        boolean roleFlag = false;
        if (!CollectionUtils.isEmpty(roleList)) {
            for (int i = 0; i < roleList.size(); i++) {
                if (roleList.get(i).getRoleKey().equals(roleCode)) {
                    roleFlag = true;
                    break;
                }
            }
        }
        return roleFlag;
    }

    public static boolean hasRole(String roleCode, List<SysRole> roleList) {
        boolean roleFlag = false;
        if (!CollectionUtils.isEmpty(roleList)) {
            for (int i = 0; i < roleList.size(); i++) {
                if (roleList.get(i).getRoleKey().equals(roleCode)) {
                    roleFlag = true;
                    break;
                }
            }
        }
        return roleFlag;
    }
}
