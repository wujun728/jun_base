package com.danny.framework.cache;

import com.danny.common.constant.Constants;
import com.danny.common.core.domain.entity.SysRole;
import com.danny.common.core.redis.RedisCache;
import com.danny.common.utils.spring.SpringUtils;
import com.danny.system.service.ISysRoleService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典工具类
 *
 * @author ruoyi
 */
@Component
public class RoleUtils implements CacheUtil {

    @Override
    public String getCacheName() {
        return CACHE_NAME;
    }

    @Override
    public String getRemark() {
        return "角色信息";
    }

    @Override
    public String getCacheId() {
        return "roleUtils";
    }

    @Override
    public String getCacheValue(String key) {
        return REDIS_CACHE.getCacheMapValue(CACHE_NAME, key).toString();
    }

    @Override
    public Collection<String> getCacheKeys() {
        return REDIS_CACHE.keys(CACHE_NAME);
    }

    @Override
    public void clearCache() {
        clearRoleCache();
    }

    @Override
    public void clearCacheByKeys(String... keys) {
        REDIS_CACHE.deleteCacheMapValue(CACHE_NAME, keys);
    }

    private static final String CACHE_NAME = "roleCache";

    private static final RedisCache REDIS_CACHE;

    private static final ISysRoleService sysRoleService;

    static {
        REDIS_CACHE = SpringUtils.getBean(RedisCache.class);
        sysRoleService = SpringUtils.getBean(ISysRoleService.class);
    }


    /**
     * 清空部门
     */
    public static void clearRoleCache() {
        Collection<String> keys = REDIS_CACHE.keys(CACHE_NAME);
        REDIS_CACHE.deleteObject(keys);
    }

    /**
     * 根据roleId获取角色对象
     * @param roleId
     * @return
     */
    public static SysRole getSysRole(String roleId) {
        SysRole sysRole = REDIS_CACHE.getCacheMapValue(CACHE_NAME, Constants.SYS_ROLE_KEY + roleId);
        if(sysRole == null){
            sysRole = sysRoleService.get(roleId);
            REDIS_CACHE.setCacheMapValue(CACHE_NAME, Constants.SYS_ROLE_KEY + roleId, sysRole);
        }
        return sysRole;
    }


    /**
     * 清除指定角色缓存
     * @param sysRole
     */
    public static void clearCache(SysRole sysRole){
        REDIS_CACHE.deleteCacheMapValue(CACHE_NAME, Constants.SYS_ROLE_KEY + sysRole.getId());
    }

    /**
     * 获取角色列表
     * @return
     */
    public static List<SysRole> getAllSysRole(){
        List<SysRole> sysRoleList = REDIS_CACHE.getCacheObjList(CACHE_NAME);
        if(CollectionUtils.isEmpty(sysRoleList)){
            sysRoleList = sysRoleService.selectRoleAll();
            Map<String, SysRole> roleMap = sysRoleList.stream().collect(Collectors.toMap(SysRole -> Constants.SYS_ROLE_KEY + SysRole.getId(), a -> a,(k1, k2)->k1));
            REDIS_CACHE.setCacheMap(CACHE_NAME, roleMap);
        }
        return sysRoleList;
    }
}
