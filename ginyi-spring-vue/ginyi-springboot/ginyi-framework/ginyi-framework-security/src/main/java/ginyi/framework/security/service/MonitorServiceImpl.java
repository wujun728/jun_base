package ginyi.framework.security.service;

import ginyi.common.constant.CacheConstants;
import ginyi.common.constant.CommonMessageConstants;
import ginyi.common.exception.CommonException;
import ginyi.common.mysql.MyPage;
import ginyi.common.redis.cache.RedisCache;
import ginyi.common.result.StateCode;
import ginyi.system.domain.LoginUser;
import ginyi.system.domain.model.vo.BaseVo;
import ginyi.system.domain.model.vo.SessionUserVo;
import ginyi.system.service.ISysOnlineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class MonitorServiceImpl implements ISysOnlineService {

    @Resource
    private RedisCache redisCache;

    @Override
    public BaseVo<SessionUserVo> getOnlineUserList(Long page, Long pageSize) {
        MyPage IPage = new MyPage(page, pageSize);
        long skipCount = IPage.getPageSize() * (IPage.getPageNum() - 1);

        BaseVo<SessionUserVo> baseVo = new BaseVo<>();
        List<SessionUserVo> list = new LinkedList<>();
        Set<String> keys = redisCache.getKeys(CacheConstants.LOGIN_TOKEN_KEY + "*");
        LoginUser loginUser;
        for (String key : keys) {
            SessionUserVo sessionUserVo = new SessionUserVo();
            loginUser = redisCache.getCacheObject(key, LoginUser.class);
            BeanUtils.copyProperties(loginUser, sessionUserVo);
            list.add(sessionUserVo);
        }

        long toIndex = Math.min(skipCount + IPage.getPageSize(), list.size());

        if(skipCount > list.size()){
            list = new LinkedList<>();
        }else {
            list = list.subList((int) skipCount, (int) toIndex);
        }

        baseVo.setList(list);
        baseVo.setCount(keys.size());
        return baseVo;
    }

    @Override
    public void removeUser(String sessionId) {
        if(!redisCache.hasKey(CacheConstants.LOGIN_TOKEN_KEY + sessionId)){
            throw  new CommonException(StateCode.ERROR_NOT_EXIST, sessionId + CommonMessageConstants.USER_NOT_EXIST);
        }
        redisCache.removeCacheObject(CacheConstants.LOGIN_TOKEN_KEY + sessionId);
    }

    @Override
    public void removeUser(Set<String> ids) {
        for (String sessionId : ids) {
            if(!redisCache.hasKey(CacheConstants.LOGIN_TOKEN_KEY + sessionId)){
                throw  new CommonException(StateCode.ERROR_NOT_EXIST, sessionId + CommonMessageConstants.USER_NOT_EXIST);
            }
        }
        // 全部没问题再执行
        for (String sessionId : ids) {
            redisCache.removeCacheObject(CacheConstants.LOGIN_TOKEN_KEY + sessionId);
        }
    }
}
