package com.aidex.system.service;

import com.aidex.common.core.service.BaseService;
import com.aidex.system.domain.SysPost;

import java.util.List;

/**
 * 岗位信息 服务层
 * 
 * @author ruoyi
 */
public interface SysPostService extends BaseService<SysPost>
{
    /**
     * 根据用户ID获取岗位选择框列表
     * 
     * @param userId 用户ID
     * @return 选中岗位ID列表
     */
    public List<String> selectPostListByUserId(String userId);

    /**
     * 校验岗位名称
     * @param post 岗位信息
     * @return 结果
     */
    public void checkPostNameUnique(SysPost post);

    /**
     * 校验岗位编码
     * @param post 岗位信息
     * @return 结果
     */
    public void checkPostCodeUnique(SysPost post);

    /**
     * 通过岗位ID查询岗位使用数量
     * @param postId 岗位ID
     * @return 结果
     */
    public int countUserPostById(String postId);


    /**
     * 批量删除岗位信息
     * 
     * @param postIds 需要删除的岗位ID
     * @return 结果
     * @throws Exception 异常
     */
    public int deletePostByIds(String[] postIds);

    /**
     * 获取最大编号
     * @param sysPost
     * @return
     */
    public int findMaxSort(SysPost sysPost);

}
