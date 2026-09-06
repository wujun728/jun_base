package ginyi.framework.security.interceptor;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.servlet.ServletUtil;
import ginyi.common.constant.CacheConstants;
import ginyi.common.constant.CommonMessageConstants;
import ginyi.common.exception.CommonException;
import ginyi.common.redis.cache.RedisCache;
import ginyi.common.result.StateCode;
import ginyi.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class PreviewEnvInterceptor implements HandlerInterceptor {

    @Resource
    private RedisCache redisCache;
    @Value("${ginyi.project-author}")
    private String author;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 不允许增删改数据
//        boolean flag;
//        List<String> list = Arrays.asList("add", "update", "edit", "delete", "remove", "register");
//        for (String url : list) {
//            flag = request.getRequestURI().contains(url);
//            if (flag) {
//                throw new CommonException(StateCode.ERROR_LIMITED, CommonMessageConstants.SYS_PREVIEW_ENV);
//            }
//        }

        // 记录ip请求，防止被恶意请求接口
        String clientIP = ServletUtil.getClientIP(request);
        String userAgent = ServletUtil.getHeader(request, "User-Agent", "utf-8");
        String key = CacheConstants.API_REQUEST + SecureUtil.md5(clientIP + userAgent) + ":" + clientIP + ":" + request.getRequestURI();
        Integer count = redisCache.getCacheObject(key, Integer.class);
        long expire = redisCache.getExpire(key);

        if (StringUtils.isNull(count)) {
            redisCache.setCacheObject(key, 1, 300, TimeUnit.SECONDS);
        } else {
            // 请求大于100次
            if (count >= 100) {
                redisCache.setCacheObject(key, count + 1, 300, TimeUnit.SECONDS);
                throw new CommonException(StateCode.ERROR_LIMITED, CommonMessageConstants.SYS_BED_REQUEST);
            } else {
                redisCache.setCacheObject(key, count + 1, (int) expire, TimeUnit.SECONDS);
            }
        }
        return true;
    }
}
