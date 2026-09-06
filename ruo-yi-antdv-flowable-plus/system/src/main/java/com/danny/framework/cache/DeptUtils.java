package com.danny.framework.cache;

import com.danny.common.constant.Constants;
import com.danny.common.core.domain.entity.SysDept;
import com.danny.common.core.domain.entity.SysUser;
import com.danny.common.core.domain.model.LoginUser;
import com.danny.common.core.redis.RedisCache;
import com.danny.common.utils.SecurityUtils;
import com.danny.common.utils.spring.SpringUtils;
import com.danny.system.service.SysDeptService;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 字典工具类
 *
 * @author ruoyi
 */
@Component
public class DeptUtils implements CacheUtil {

    @Override
    public String getCacheName() {
        return CACHE_NAME;
    }

    @Override
    public String getRemark() {
        return "部门信息";
    }

    @Override
    public String getCacheId() {
        return "deptUtils";
    }

    @Override
    public Collection<String> getCacheKeys() {
        return REDIS_CACHE.getCacheMapKeys(CACHE_NAME);
    }

    @Override
    public String getCacheValue(String key) {
        return REDIS_CACHE.getCacheMapValue(CACHE_NAME, key).toString();
    }

    @Override
    public void clearCache() {
        clearDeptCache();
    }

    @Override
    public void clearCacheByKeys(String... keys) {
        REDIS_CACHE.deleteCacheMapValue(CACHE_NAME, keys);
    }

    /**
     * 分隔符
     */
    public static final String SEPARATOR = ",";

    private static final String CACHE_NAME = "deptCache";

    private static final RedisCache REDIS_CACHE;

    private static final SysDeptService sysDeptService;

    static {
        REDIS_CACHE = SpringUtils.getBean(RedisCache.class);
        sysDeptService = SpringUtils.getBean(SysDeptService.class);
    }


    /**
     * 清空部门
     */
    public static void clearDeptCache() {
        Collection<String> keys = REDIS_CACHE.keys(CACHE_NAME);
        REDIS_CACHE.deleteObject(keys);
    }

    /**
     * 根据deptId获取部门对象
     *
     * @param deptId
     * @return
     */
    public static SysDept getSysDept(String deptId) {
        SysDept sysDept = REDIS_CACHE.getCacheMapValue(CACHE_NAME, Constants.SYS_DEPT_KEY + deptId);
        if (sysDept == null) {
            sysDept = sysDeptService.get(deptId);
            if (null != sysDept) {
                REDIS_CACHE.setCacheMapValue(CACHE_NAME, Constants.SYS_DEPT_KEY + deptId, sysDept);
                REDIS_CACHE.setCacheMapValue(CACHE_NAME, Constants.SYS_DEPT_DC_KEY + sysDept.getDeptCode(), sysDept);
            }
        }
        return sysDept;
    }

    /**
     * 根据deptCode获取部门对象
     *
     * @param deptCode
     * @return
     */
    public static SysDept getSysDeptByDeptCode(String deptCode) {
        SysDept sysDept = REDIS_CACHE.getCacheMapValue(CACHE_NAME, Constants.SYS_DEPT_DC_KEY + deptCode);
        if (sysDept == null) {
            sysDept = sysDeptService.selectDeptByDeptCode(deptCode);
            REDIS_CACHE.setCacheMapValue(CACHE_NAME, Constants.SYS_DEPT_KEY + sysDept.getId(), sysDept);
            REDIS_CACHE.setCacheMapValue(CACHE_NAME, Constants.SYS_DEPT_DC_KEY + deptCode, sysDept);
        }
        return sysDept;
    }

    /**
     * 清除指定部门缓存
     *
     * @param sysDept
     */
    public static void clearCache(SysDept sysDept) {
        REDIS_CACHE.deleteCacheMapValue(CACHE_NAME, Constants.SYS_DEPT_KEY + sysDept.getId(), Constants.SYS_DEPT_DC_KEY + sysDept.getDeptCode());
    }

    /**
     * 获取当前用户所属部门
     *
     * @return
     */
    public static SysDept getDept() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser sysUser = UserUtils.getSysUserByUserName(loginUser.getUsername());
        return getSysDept(sysUser.getDeptId());
    }
}
