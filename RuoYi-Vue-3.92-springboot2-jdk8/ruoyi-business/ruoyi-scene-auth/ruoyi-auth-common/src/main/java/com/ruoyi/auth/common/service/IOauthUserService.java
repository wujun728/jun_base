package com.ruoyi.auth.common.service;

import java.util.List;

import com.ruoyi.auth.common.domain.OauthUser;
import com.ruoyi.common.core.domain.entity.SysUser;

/**
 * 第三方认证Service接口
 * 
 * @author Dftre
 * @date 2024-01-18
 */
public interface IOauthUserService {
    /**
     * 查询第三方认证
     * 
     * @param id 第三方认证主键
     * @return 第三方认证
     */
    public OauthUser selectOauthUserById(Long id);

    public OauthUser selectOauthUserByUUID(String uuid);

    public OauthUser selectOauthUserByUserId(Long userId);

    public SysUser selectSysUserByUUID(String uuid);

    /**
     * 查询第三方认证列表
     * 
     * @param oauthUser 第三方认证
     * @return 第三方认证集合
     */
    public List<OauthUser> selectOauthUserList(OauthUser oauthUser);

    /**
     * 新增第三方认证
     * 
     * @param oauthUser 第三方认证
     * @return 结果
     */
    public int insertOauthUser(OauthUser oauthUser);

    /**
     * 修改第三方认证
     * 
     * @param oauthUser 第三方认证
     * @return 结果
     */
    public int updateOauthUser(OauthUser oauthUser);

    /**
     * 批量删除第三方认证
     * 
     * @param ids 需要删除的第三方认证主键集合
     * @return 结果
     */
    public int deleteOauthUserByIds(Long[] ids);

    /**
     * 删除第三方认证信息
     * 
     * @param id 第三方认证主键
     * @return 结果
     */
    public int deleteOauthUserById(Long id);

    /**
     * 校验source平台是否绑定
     *
     * @param userId 用户编号
     * @param source 绑定平台
     * @return 结果
     */
    public boolean checkAuthUser(Long userId, String source);

    /**
     * 校验用户名称是否唯一
     * 
     * @param userName 用户名称
     * @return 结果
     */
    public boolean checkUserNameUnique(String userName);

    /**
     * 校验手机号码是否唯一
     *
     * @param phonenumber 手机号码
     * @return 结果
     */
    public boolean checkPhoneUnique(String phonenumber);

    /**
     * 校验email是否唯一
     *
     * @param email 用户邮箱
     * @return 结果
     */
    public boolean checkEmailUnique(String email);

}
