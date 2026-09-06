package com.geek.system.mapper;

import java.util.List;

import com.geek.system.domain.SysUserPost;
import com.mybatisflex.core.BaseMapper;

/**
 * 用户与岗位关联表 数据层
 * 
 * @author geek
 */
public interface SysUserPostMapper extends BaseMapper<SysUserPost> {

    /**
     * 通过用户ID删除用户和岗位关联
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteUserPostByUserId(Long userId);

    /**
     * 批量新增用户岗位信息
     * 
     * @param userPostList 用户岗位列表
     * @return 结果
     */
    public int batchUserPost(List<SysUserPost> userPostList);
}
