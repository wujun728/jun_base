package com.aidex.system.service.impl;

import com.aidex.common.core.service.BaseServiceImpl;
import com.aidex.common.exception.BizException;
import com.aidex.common.exception.CustomException;
import com.aidex.common.utils.NumberUtils;
import com.aidex.system.common.SysErrorCode;
import com.aidex.system.domain.SysPost;
import com.aidex.system.mapper.SysPostMapper;
import com.aidex.system.mapper.SysUserPostMapper;
import com.aidex.system.service.SysPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 岗位信息 服务层处理
 *
 * @author ruoyi
 */
@Transactional(readOnly = true)
@Service
public class SysPostServiceImpl extends BaseServiceImpl<SysPostMapper, SysPost> implements SysPostService {

    @Autowired
    private SysUserPostMapper userPostMapper;


    /**
     * 根据用户ID获取岗位选择框列表
     *
     * @param userId 用户ID
     * @return 选中岗位ID列表
     */
    @Override
    public List<String> selectPostListByUserId(String userId) {
        return mapper.selectPostListByUserId(userId);
    }

    /**
     * 校验岗位名称是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public void checkPostNameUnique(SysPost post) {
        SysPost sysPostUnique = new SysPost();
        sysPostUnique.setNotEqualId(post.getId());
        sysPostUnique.setPostName(post.getPostName());
        if (!CollectionUtils.isEmpty(mapper.findListWithUnique(sysPostUnique))) {
            throw new BizException(SysErrorCode.B_SYSPOST_PostNameAlreadyExist);
        }
    }

    /**
     * 校验岗位编码是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public void checkPostCodeUnique(SysPost post) {
        SysPost sysPostUnique = new SysPost();
        sysPostUnique.setNotEqualId(post.getId());
        sysPostUnique.setPostCode(post.getPostCode());
        if (!CollectionUtils.isEmpty(mapper.findListWithUnique(sysPostUnique))) {
            throw new BizException(SysErrorCode.B_SYSPOST_PostCodeAlreadyExist);
        }
    }

    /**
     * 通过岗位ID查询岗位使用数量
     *
     * @param postId 岗位ID
     * @return 结果
     */
    @Override
    public int countUserPostById(String postId) {
        return userPostMapper.countUserPostById(postId);
    }


    /**
     * 批量删除岗位信息
     *
     * @param postIds 需要删除的岗位ID
     * @return 结果
     * @throws Exception 异常
     */
    @Override
    @Transactional(readOnly = false)
    public int deletePostByIds(String[] postIds) {
        for (String postId : postIds) {
            SysPost post = get(postId);
            if (countUserPostById(postId) > 0) {
                throw new CustomException(String.format("%1$s已分配,不能删除", post.getPostName()));
            }
        }
        return mapper.deletePostByIds(postIds);
    }

    /**
     * 保存岗位信息
     * @param sysPost
     * @return
     */
    @Override
    public boolean save(SysPost sysPost) {
        checkPostNameUnique(sysPost);
        checkPostCodeUnique(sysPost);
        return super.save(sysPost);
    }

    /**
     * 获取最大编号
     * @param sysPost
     * @return
     */
    @Override
    public int findMaxSort(SysPost sysPost){
        return NumberUtils.nextOrder(mapper.findMaxSort(sysPost));
    }

}
