package com.geek.system.service.impl;

import java.util.List;

import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

import com.geek.common.constant.CacheConstants;
import com.geek.common.constant.UserConstants;
import com.geek.common.core.text.Convert;
import com.geek.common.exception.ServiceException;
import com.geek.common.utils.CacheUtils;
import com.geek.common.utils.StringUtils;
import com.geek.common.utils.poi.ExcelUtil;
import com.geek.system.domain.SysConfig;
import com.geek.system.mapper.SysConfigMapper;
import com.geek.system.service.ISysConfigService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 参数配置 服务层实现
 * 
 * @author geek
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {

    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init() {
        loadingConfigCache();
    }

    /**
     * 根据键名查询参数配置信息
     * 
     * @param configKey 参数key
     * @return 参数键值
     */
    @Override
    public String selectConfigByKey(String configKey) {
        String configValue = Convert.toStr(getCache().get(configKey, String.class));
        if (StringUtils.isNotEmpty(configValue)) {
            return configValue;
        }
        SysConfig retConfig = this.queryChain()
                .eq(SysConfig::getConfigKey, configKey)
                .one();
        if (StringUtils.isNotNull(retConfig)) {
            CacheUtils.put(CacheConstants.SYS_CONFIG_KEY, configKey, retConfig.getConfigValue());
            return retConfig.getConfigValue();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 获取验证码开关
     * 
     * @return true开启，false关闭
     */
    @Override
    public boolean selectCaptchaEnabled() {
        String captchaEnabled = selectConfigByKey("sys.account.captchaEnabled");
        return Convert.toBool(captchaEnabled, true);
    }

    private QueryChain<SysConfig> selectConfigList(SysConfig config) {
        return this.queryChain()
                .like(SysConfig::getConfigKey, config.getConfigKey())
                .eq(SysConfig::getConfigType, config.getConfigType())
                .like(SysConfig::getConfigName, config.getConfigName())
                .between(SysConfig::getCreateTime,
                        config.getParams().get("beginTime"),
                        config.getParams().get("endTime"));
    }

    @Override
    public Page<SysConfig> page(SysConfig config, int pageNum, int pageSize) {
        return this.selectConfigList(config).page(Page.of(pageNum, pageSize));
    }

    @Override
    public void export(SysConfig config, HttpServletResponse response) {
        List<SysConfig> list = this.selectConfigList(config).list();
        ExcelUtil<SysConfig> util = new ExcelUtil<>(SysConfig.class);
        util.exportExcel(response, list, "参数数据");
    }

    /**
     * 新增参数配置
     * 
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public boolean insertConfig(SysConfig config) {
        boolean result = this.save(config);
        if (result) {
            CacheUtils.put(CacheConstants.SYS_CONFIG_KEY, config.getConfigKey(), config.getConfigValue());
        }
        return result;
    }

    /**
     * 修改参数配置
     * 
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public boolean updateConfig(SysConfig config) {
        SysConfig temp = this.getById(config.getConfigId());
        if (!StringUtils.equals(temp.getConfigKey(), config.getConfigKey())) {
            CacheUtils.removeIfPresent(CacheConstants.SYS_CONFIG_KEY, temp.getConfigKey());
        }

        boolean result = this.updateById(config);
        if (result) {
            CacheUtils.put(CacheConstants.SYS_CONFIG_KEY, config.getConfigKey(), config.getConfigValue());
        }
        return result;
    }

    /**
     * 批量删除参数信息
     * 
     * @param configIds 需要删除的参数ID
     */
    @Override
    public void deleteConfigByIds(Long[] configIds) {
        for (Long configId : configIds) {
            SysConfig config = getById(configId);
            if (StringUtils.equals(UserConstants.YES, config.getConfigType())) {
                throw new ServiceException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
            }
            this.removeById(configId);
            getCache().evict(config.getConfigKey());
        }
    }

    /**
     * 加载参数缓存数据
     */
    @Override
    public void loadingConfigCache() {
        List<SysConfig> configsList = this.list();
        for (SysConfig config : configsList) {
            getCache().put(config.getConfigKey(), config.getConfigValue());
        }
    }

    /**
     * 清空参数缓存数据
     */
    @Override
    public void clearConfigCache() {
        CacheUtils.getCache(CacheConstants.SYS_CONFIG_KEY).clear();
    }

    /**
     * 重置参数缓存数据
     */
    @Override
    public void resetConfigCache() {
        clearConfigCache();
        loadingConfigCache();
    }

    /**
     * 校验参数键名是否唯一
     * 
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public boolean checkConfigKeyUnique(SysConfig config) {
        return !this.queryChain()
                .eq(SysConfig::getConfigKey, config.getConfigKey())
                .ne(SysConfig::getConfigId, config.getConfigId())
                .exists();
    }

    /**
     * 获取config缓存
     *
     * @return
     */
    private Cache getCache() {
        return CacheUtils.getCache(CacheConstants.SYS_CONFIG_KEY);
    }
}
