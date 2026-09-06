package ginyi.framework.security.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import ginyi.common.constant.CacheConstants;
import ginyi.common.redis.cache.RedisCache;
import ginyi.common.utils.StringUtils;
import ginyi.common.utils.text.Convert;
import ginyi.system.domain.SysConfig;
import ginyi.system.mapper.SysConfigMapper;
import ginyi.system.service.ISysConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysConfigServiceImpl implements ISysConfigService {

    @Resource
    private RedisCache redisCache;
    @Resource
    private SysConfigMapper configMapper;

    /**
     * 获取验证码开关
     *
     * @return true开启，false关闭
     */
    @Override
    public boolean selectCaptchaEnabled() {
        String captchaEnabled = selectConfigByKey("sys.account.captchaEnabled");
        if (StringUtils.isEmpty(captchaEnabled)) {
            return true;
        }
        return Convert.toBool(captchaEnabled);
    }

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数key
     * @return 参数键值
     */
    @Override
    public String selectConfigByKey(String configKey) {
        String configValue = Convert.toStr(redisCache.getCacheObject(getCacheKey(configKey), String.class));
        if (StringUtils.isNotEmpty(configValue)) {
            return configValue;
        }
        LambdaQueryWrapper<SysConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysConfig::getConfigKey, configKey);
        SysConfig sysConfig = configMapper.selectOne(queryWrapper);
        if (StringUtils.isNotNull(sysConfig)) {
            redisCache.setCacheObject(getCacheKey(configKey), sysConfig.getConfigValue());
            return sysConfig.getConfigValue();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey) {
        return CacheConstants.SYS_CONFIG_KEY + configKey;
    }
}
