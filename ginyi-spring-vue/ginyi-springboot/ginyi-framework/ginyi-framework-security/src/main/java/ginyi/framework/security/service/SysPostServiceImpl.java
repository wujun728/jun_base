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
import ginyi.system.domain.SysMenu;
import ginyi.system.domain.SysPost;
import ginyi.system.domain.model.dto.PostDto;
import ginyi.system.domain.model.vo.BaseVo;
import ginyi.system.domain.model.vo.PostVo;
import ginyi.system.mapper.SysPostMapper;
import ginyi.system.service.ISysPostService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Service
public class SysPostServiceImpl implements ISysPostService {

    @Resource
    private SysPostMapper postMapper;
    @Resource
    private RedisCache redisCache;

    /**
     * 查询岗位列表
     *
     * @param postDto
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public BaseVo<PostVo> list(PostDto postDto, Long page, Long pageSize) {
        IPage<PostVo> list = postMapper.list(postDto, new MyPage(page, pageSize).getPage());
        BaseVo<PostVo> baseVo = new BaseVo<>();
        baseVo.setList(list.getRecords());
        baseVo.setCount((int) list.getTotal());
        return baseVo;
    }

    /**
     * 获取岗位详情
     *
     * @param postId
     * @return
     */
    @Override
    public PostVo getPostByPostId(Long postId) {
        // 检查缓存中是否存在空id
        if (redisCache.hasKey(CacheConstants.POST_NOT_EXIST_KEY + postId)) {
            throw new CommonException(StateCode.ERROR_NOT_EXIST, postId + CommonMessageConstants.POST_NOT_EXIST);
        }
        PostVo postVo = new PostVo();
        // 检查缓存中是否存在
        SysPost post = redisCache.getCacheObject(CacheConstants.POST_DETAILS_BY_POSTID_KEY + postId, SysPost.class);
        if (StringUtils.isNotNull(post)) {
            BeanUtils.copyProperties(post, postVo);
            return postVo;
        }
        LambdaQueryWrapper<SysPost> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysPost::getPostId, postId);
        post = postMapper.selectOne(queryWrapper);
        if (StringUtils.isNull(post)) {
            redisCache.setCacheObject(CacheConstants.POST_NOT_EXIST_KEY + postId, null);
            throw new CommonException(StateCode.ERROR_NOT_EXIST, postId + CommonMessageConstants.POST_NOT_EXIST);
        }
        redisCache.setCacheObject(CacheConstants.POST_DETAILS_BY_POSTID_KEY + postId, post);
        BeanUtils.copyProperties(post, postVo);
        return postVo;
    }

    /**
     * 添加岗位
     *
     * @param postDto
     */
    @Override
    public void addPost(PostDto postDto) {
        if (redisCache.hasKey(CacheConstants.ROLE_NAME_USED_KEY + postDto.getPostName())) {
            throw new CommonException(StateCode.ERROR_EXIST, CommonMessageConstants.POST_NAME_USED);
        }
        if (redisCache.hasKey(CacheConstants.ROLE_CODE_USED_KEY + postDto.getPostCode())) {
            throw new CommonException(StateCode.ERROR_EXIST, CommonMessageConstants.POST_CODE_USED);
        }
        // 检查名称是否被使用
        LambdaQueryWrapper<SysPost> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysPost::getPostName, postDto.getPostName());
        SysPost post = postMapper.selectOne(queryWrapper);
        if (StringUtils.isNotNull(post)) {
            redisCache.setCacheObject(CacheConstants.ROLE_NAME_USED_KEY + postDto.getPostName(), null);
            throw new CommonException(StateCode.ERROR_EXIST, CommonMessageConstants.POST_NAME_USED);
        }
        // 检查编码是否被使用
        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysPost::getPostCode, postDto.getPostCode());
        post = postMapper.selectOne(queryWrapper);
        if (StringUtils.isNotNull(post)) {
            redisCache.setCacheObject(CacheConstants.ROLE_CODE_USED_KEY + postDto.getPostCode(), null);
            throw new CommonException(StateCode.ERROR_EXIST, CommonMessageConstants.POST_CODE_USED);
        }
        postMapper.insertPost(postDto);
        redisCache.removeCacheObject(CacheConstants.POST_KEY_PREFIX);
        redisCache.removeCacheObject(CacheConstants.ROLE_KEY_PREFIX);
    }

    /**
     * 更新岗位
     *
     * @param postDto
     */
    @Override
    public void updatePost(PostDto postDto) {
        // 检查缓存中是否存在空id
        if (redisCache.hasKey(CacheConstants.POST_NOT_EXIST_KEY + postDto.getPostId())) {
            throw new CommonException(StateCode.ERROR_NOT_EXIST, postDto.getPostId() + CommonMessageConstants.POST_NOT_EXIST);
        }
        LambdaQueryWrapper<SysPost> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysPost::getPostId, postDto.getPostId());
        SysPost post = postMapper.selectOne(queryWrapper);
        if (StringUtils.isNull(post)) {
            redisCache.setCacheObject(CacheConstants.POST_NOT_EXIST_KEY + postDto.getPostId(), null);
            throw new CommonException(StateCode.ERROR_NOT_EXIST, postDto.getPostId() + CommonMessageConstants.POST_NOT_EXIST);
        }
        // 检查名称是否被使用
        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysPost::getPostName, postDto.getPostName());
        post = postMapper.selectOne(queryWrapper);
        if (StringUtils.isNotNull(post) && !post.getPostId().equals(postDto.getPostId())) {
            throw new CommonException(StateCode.ERROR_EXIST, CommonMessageConstants.POST_NAME_USED);
        }
        // 检查编码是否被使用
        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysPost::getPostCode, postDto.getPostCode());
        post = postMapper.selectOne(queryWrapper);
        if (StringUtils.isNotNull(post) && !post.getPostId().equals(postDto.getPostId())) {
            throw new CommonException(StateCode.ERROR_EXIST, CommonMessageConstants.POST_CODE_USED);
        }
        postMapper.updatePost(postDto);
        redisCache.removeCacheObject(CacheConstants.POST_KEY_PREFIX);
    }

    /**
     * 删除岗位
     *
     * @param postId
     */
    @Override
    public void removePostById(Long postId) {
        // 查看缓存中是否存在空id
        if (redisCache.hasKey(CacheConstants.POST_NOT_EXIST_KEY + postId)) {
            throw new CommonException(StateCode.ERROR_NOT_EXIST, postId + CommonMessageConstants.POST_NOT_EXIST);
        }
        LambdaQueryWrapper<SysPost> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysPost::getPostId, postId);
        SysPost post = postMapper.selectOne(queryWrapper);
        if (StringUtils.isNull(post)) {
            redisCache.setCacheObject(CacheConstants.POST_NOT_EXIST_KEY + postId, null);
            throw new CommonException(StateCode.ERROR_NOT_EXIST, postId + CommonMessageConstants.POST_NOT_EXIST);
        }
        postMapper.deleteById(postId);
        redisCache.removeCacheObject(CacheConstants.POST_KEY_PREFIX);
    }

    /**
     * 批量删除岗位
     *
     * @param ids
     */
    @Override
    public void removeDeptByIds(Set<Long> ids) {
        if (ids.size() > 0) {
            List<SysPost> postList;
            postList = redisCache.getCacheList(CacheConstants.POST_LIST_KEY, SysPost.class);
            if (StringUtils.isNull(postList) || postList.size() == 0) {
                LambdaQueryWrapper<SysPost> queryWrapper = new LambdaQueryWrapper<>();
                postList = postMapper.selectList(queryWrapper);
                redisCache.setCacheList(CacheConstants.POST_LIST_KEY, postList);
            }
            for (Long postId : ids) {
                // 检查缓存中是否有标记着空id
                if (redisCache.hasKey(CacheConstants.POST_NOT_EXIST_KEY + postId)) {
                    throw new CommonException(StateCode.ERROR_NOT_EXIST, postId + CommonMessageConstants.POST_NOT_EXIST);
                }
                boolean exist = postList.stream().anyMatch(post -> postId.equals(post.getPostId()));
                if (!exist) {
                    redisCache.setCacheObject(CacheConstants.POST_NOT_EXIST_KEY + postId, null);
                    throw new CommonException(StateCode.ERROR_NOT_EXIST, postId + CommonMessageConstants.POST_NOT_EXIST);
                }
            }
            postMapper.deleteBatchIds(ids);
            redisCache.removeCacheObject(CacheConstants.POST_KEY_PREFIX);
        } else {
            throw new CommonException(StateCode.ERROR_REQUEST_PARAMS, CommonMessageConstants.SYS_REQUEST_ILLEGAL);
        }
    }

    /**
     * 更新状态
     * @param postDto
     */
    @Override
    public void updateStatus(PostDto postDto) {
        if (StringUtils.isNull(postDto.getPostId())) {
            throw new CommonException(StateCode.ERROR_PARAMS, CommonMessageConstants.POST_ID_NOT_FOUND);
        }
        // 状态参数是否合法
        if (!("0".equals(postDto.getStatus()) || "1".equals(postDto.getStatus()))) {
            throw new CommonException(StateCode.ERROR_PARAMS, CommonMessageConstants.POST_STATUS_ILLEGAL);
        }
        // 检查缓存中是否标记着空id
        if (redisCache.hasKey(CacheConstants.USER_NOT_EXIST_KEY + postDto.getPostId())) {
            throw new CommonException(StateCode.ERROR_NOT_EXIST, postDto.getPostId() + CommonMessageConstants.POST_NOT_EXIST);
        }
        LambdaQueryWrapper<SysPost> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysPost::getPostId, postDto.getPostId());
        SysPost post = postMapper.selectOne(queryWrapper);
        if (StringUtils.isNull(post)) {
            redisCache.setCacheObject(CacheConstants.POST_NOT_EXIST_KEY + postDto.getPostId(), null);
            throw new CommonException(StateCode.ERROR_NOT_EXIST, postDto.getPostId() + CommonMessageConstants.POST_NOT_EXIST);
        }
        postMapper.updatePostStatus(postDto);
    }
}
