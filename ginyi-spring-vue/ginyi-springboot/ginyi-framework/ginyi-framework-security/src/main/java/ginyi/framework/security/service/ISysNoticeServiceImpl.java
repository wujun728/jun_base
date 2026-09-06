package ginyi.framework.security.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import ginyi.common.constant.CacheConstants;
import ginyi.common.constant.CommonMessageConstants;
import ginyi.common.exception.CommonException;
import ginyi.common.mysql.MyPage;
import ginyi.common.redis.cache.RedisCache;
import ginyi.common.result.StateCode;
import ginyi.common.utils.StringUtils;
import ginyi.system.domain.LoginUser;
import ginyi.system.domain.SysNotice;
import ginyi.system.domain.SysUser;
import ginyi.system.domain.model.dto.NoticeDto;
import ginyi.system.domain.model.vo.BaseVo;
import ginyi.system.domain.model.vo.NoticeVo;
import ginyi.system.mapper.SysNoticeMapper;
import ginyi.system.mapper.SysUserMapper;
import ginyi.system.service.ISysNoticeService;
import ginyi.system.service.ITokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ISysNoticeServiceImpl implements ISysNoticeService {

    @Resource
    private SysNoticeMapper noticeMapper;
    @Resource
    private SysUserMapper userMapper;
    @Resource
    private ITokenService tokenService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private RedisCache redisCache;

    /**
     * 获取通知公告列表
     */
    @Override
    public BaseVo<SysNotice> list(NoticeDto noticeDto, Long page, Long pageSize) {
        IPage<SysNotice> list = noticeMapper.list(noticeDto, new MyPage(page, pageSize).getPage());
        for (SysNotice notice : list.getRecords()) {
            // 如果 userReadIds 是 null，则把它设置为[]
            if (StringUtils.isNull(notice.getUserReadIds())) {
                notice.setUserReadIds(new ArrayList<>());
            }
        }

        BaseVo<SysNotice> baseVo = new BaseVo<>();
        baseVo.setList(list.getRecords());
        baseVo.setCount((int) list.getTotal());
        return baseVo;
    }

    /**
     * 获取用户通知公告列表
     */
    @Override
    public BaseVo<NoticeVo> getUserNoticeList(Long page, Long pageSize) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUserId();
        IPage<SysNotice> list = noticeMapper.getUserNoticeList(userId, new MyPage(page, pageSize).getPage());

        ArrayList<NoticeVo> noticeList = new ArrayList<>();
        for (SysNotice notice : list.getRecords()) {
            List<Long> userReadIds = notice.getUserReadIds();
            NoticeVo vo = new NoticeVo();
            BeanUtils.copyProperties(notice, vo);
            // 判断是否已读
            if (StringUtils.isNotNull(userReadIds)) {
                vo.setHaveRead(userReadIds.contains(userId));
            }
            noticeList.add(vo);
        }

        BaseVo<NoticeVo> baseVo = new BaseVo<>();
        baseVo.setList(noticeList);
        baseVo.setCount((int) list.getTotal());
        return baseVo;
    }


    /**
     * 发布通知公告
     */
    @Override
    public void add(NoticeDto noticeDto) {
        List<SysUser> userList = userMapper.selectList(null);
        if (noticeDto.getUserIds().size() > 0) {
            for (Long userId : noticeDto.getUserIds()) {
                boolean isExist = false;
                for (SysUser user : userList) {
                    // 判断 userId 是否合法
                    if (userId.equals(user.getUserId())) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    throw new CommonException(StateCode.ERROR_NOT_EXIST, userId + CommonMessageConstants.USER_NOT_EXIST);
                }
            }
        } else {
            throw new CommonException(StateCode.ERROR_PARAMS, CommonMessageConstants.USER_IDS_ILLEGAL);
        }

        noticeMapper.addNotice(noticeDto);
    }

    /**
     * 更新通知公告
     *
     * @param noticeDto
     */
    @Override
    public void updateNotice(NoticeDto noticeDto) {
        // 检查缓存中是否有标记空id
        if (redisCache.hasKey(CacheConstants.NOTICE_NOT_EXIST_KEY + noticeDto.getNoticeId())) {
            throw new CommonException(StateCode.ERROR_NOT_EXIST, noticeDto.getNoticeId() + CommonMessageConstants.NOTICE_NOT_EXIST);
        }
        SysNotice notice = noticeMapper.selectOne(noticeDto.getNoticeId());
        if (StringUtils.isNull(notice)) {
            redisCache.setCacheObject(CacheConstants.NOTICE_NOT_EXIST_KEY + noticeDto.getNoticeId(), noticeDto.getNoticeId());
            throw new CommonException(StateCode.ERROR_NOT_EXIST, noticeDto.getNoticeId() + CommonMessageConstants.NOTICE_NOT_EXIST);
        }

        // 如果有人确认收到该通知公告，则不允许修改
        if (StringUtils.isNotNull(notice.getUserReadIds())) {
            throw new CommonException(StateCode.ERROR_PARAMS_SERVICE, CommonMessageConstants.NOTICE_NOT_ALLOW);
        }
        noticeMapper.updateNotice(noticeDto);

    }

    /**
     * 确认收到通知公告
     *
     * @param noticeId
     */
    @Override
    public void haveRead(Long noticeId) {
        // 检查缓存中是否有标记空id
        if (redisCache.hasKey(CacheConstants.NOTICE_NOT_EXIST_KEY + noticeId)) {
            throw new CommonException(StateCode.ERROR_NOT_EXIST, noticeId + CommonMessageConstants.NOTICE_NOT_EXIST);
        }

        LambdaQueryWrapper<SysNotice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysNotice::getNoticeId, noticeId);
        SysNotice notice = noticeMapper.selectOne(noticeId);
        if (StringUtils.isNull(notice)) {
            redisCache.setCacheObject(CacheConstants.NOTICE_NOT_EXIST_KEY + noticeId, noticeId);
            throw new CommonException(StateCode.ERROR_NOT_EXIST, noticeId + CommonMessageConstants.NOTICE_NOT_EXIST);
        }

        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUserId();
        List<Long> userReadIds = notice.getUserReadIds();
        // 如果为 null 则初始化为 []
        if (StringUtils.isNull(userReadIds)) {
            userReadIds = new ArrayList<>();
        }
        // 判断该通知公告的用户列表中是否存在该用户
        if (!notice.getUserIds().contains(userId)) {
            throw new CommonException(StateCode.ERROR_NOT_EXIST, CommonMessageConstants.NOTICE_CURRENT_USER_NOT_EXIST);
        }
        // 判断是否已经确认收到该通知公告了
        if (userReadIds.contains(userId)) {
            throw new CommonException(StateCode.ERROR_NOT_EXIST, CommonMessageConstants.NOTICE_HAVE_READ);
        }
        userReadIds.add(userId);
        notice.setUserReadIds(userReadIds);
        noticeMapper.haveRead(notice);
    }

    /**
     * 删除通知公告
     *
     * @param noticeId
     */
    @Override
    public void remove(Long noticeId) {
        // 检查缓存中是否有标记空id
        if (redisCache.hasKey(CacheConstants.NOTICE_NOT_EXIST_KEY + noticeId)) {
            throw new CommonException(StateCode.ERROR_NOT_EXIST, noticeId + CommonMessageConstants.NOTICE_NOT_EXIST);
        }
        SysNotice notice = noticeMapper.selectOne(noticeId);
        if (StringUtils.isNull(notice)) {
            redisCache.setCacheObject(CacheConstants.NOTICE_NOT_EXIST_KEY + noticeId, noticeId);
            throw new CommonException(StateCode.ERROR_NOT_EXIST, noticeId + CommonMessageConstants.NOTICE_NOT_EXIST);
        }
        // 检查数据库中是否存在该通知公告
        LambdaQueryWrapper<SysNotice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysNotice::getNoticeId, noticeId);
        noticeMapper.delete(queryWrapper);
    }


}
