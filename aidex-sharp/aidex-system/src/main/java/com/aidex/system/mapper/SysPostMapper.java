package com.aidex.system.mapper;

import com.aidex.common.core.mapper.BaseMapper;
import com.aidex.system.domain.SysPost;

import java.util.List;

/**
 * 岗位信息 数据层
 * 
 * @author ruoyi
 */
public interface SysPostMapper extends BaseMapper<SysPost>
{

    /**
     * 根据用户ID获取岗位选择框列表
     * 
     * @param userId 用户ID
     * @return 选中岗位ID列表
     */
    public List<String> selectPostListByUserId(String userId);

    /**
     * 查询用户所属岗位组
     * 
     * @param userName 用户名
     * @return 结果
     */
    public List<SysPost> selectPostsByUserName(String userName);

    /**
     * 批量删除岗位信息
     * 
     * @param postIds 需要删除的岗位ID
     * @return 结果
     */
    public int deletePostByIds(String[] postIds);

    /**
     * 唯一性校验判断
     * 
     * @param sysPost 岗位名称
     * @return 结果
     */
    @Override
    public List<SysPost> findListWithUnique(SysPost sysPost);


    /**
     * 查询最大编号
     * @param
     * @return int
     */
    public Integer findMaxSort(SysPost sysPost);

}
