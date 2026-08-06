package com.ruoyi.auth.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.auth.common.domain.OauthUser;
import com.ruoyi.auth.common.mapper.OauthUserMapper;
import com.ruoyi.auth.common.service.IOauthUserService;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.SysUserMapper;

/**
 * 第三方认证Service业务层处理
 * 
 * @author Dftre
 * @date 2024-01-18
 */
@Service
public class OauthUserServiceImpl implements IOauthUserService {
    @Autowired
    private OauthUserMapper oauthUserMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 查询第三方认证
     * 
     * @param id 第三方认证主键
     * @return 第三方认证
     */
    @Override
    public OauthUser selectOauthUserById(Long id) {
        return oauthUserMapper.selectOauthUserById(id);
    }

    @Override
    public OauthUser selectOauthUserByUUID(String uuid) {
        return oauthUserMapper.selectOauthUserByUUID(uuid);
    }

    @Override
    public OauthUser selectOauthUserByUserId(Long userId) {
        return oauthUserMapper.selectOauthUserByUserId(userId);
    }

    /**
     * 查询第三方认证列表
     * 
     * @param oauthUser 第三方认证
     * @return 第三方认证
     */
    @Override
    public List<OauthUser> selectOauthUserList(OauthUser oauthUser) {
        return oauthUserMapper.selectOauthUserList(oauthUser);
    }

    /**
     * 新增第三方认证
     * 
     * @param oauthUser 第三方认证
     * @return 结果
     */
    @Override
    public int insertOauthUser(OauthUser oauthUser) {
        return oauthUserMapper.insertOauthUser(oauthUser);
    }

    /**
     * 修改第三方认证
     * 
     * @param oauthUser 第三方认证
     * @return 结果
     */
    @Override
    public int updateOauthUser(OauthUser oauthUser) {
        return oauthUserMapper.updateOauthUser(oauthUser);
    }

    /**
     * 批量删除第三方认证
     * 
     * @param ids 需要删除的第三方认证主键
     * @return 结果
     */
    @Override
    public int deleteOauthUserByIds(Long[] ids) {
        return oauthUserMapper.deleteOauthUserByIds(ids);
    }

    /**
     * 删除第三方认证信息
     * 
     * @param id 第三方认证主键
     * @return 结果
     */
    @Override
    public int deleteOauthUserById(Long id) {
        return oauthUserMapper.deleteOauthUserById(id);
    }

    public SysUser selectSysUserByUUID(String uuid) {
        OauthUser oauthUser = oauthUserMapper.selectOauthUserByUUID(uuid);
        if (StringUtils.isNotNull(oauthUser)) {
            return sysUserMapper.selectUserById(oauthUser.getUserId());
        } else {
            return null;
        }
    }

    /**
     * 校验source平台是否绑定
     *
     * @param userId 用户编号
     * @param source 绑定平台
     * @return 结果
     */
    public boolean checkAuthUser(Long userId, String source) {
        return oauthUserMapper.checkAuthUser(userId, source) > 0;
    };

    /**
     * 校验用户名称是否唯一
     * 
     * @param userName 用户名称
     * @return 结果
     */
    public boolean checkUserNameUnique(String userName) {
        return oauthUserMapper.checkUserNameUnique(userName) > 0;
    };

    /**
     * 校验手机号码是否唯一
     *
     * @param phonenumber 手机号码
     * @return 结果
     */
    public boolean checkPhoneUnique(String phonenumber) {
        return oauthUserMapper.checkPhoneUnique(phonenumber) > 0;
    };

    /**
     * 校验email是否唯一
     *
     * @param email 用户邮箱
     * @return 结果
     */
    public boolean checkEmailUnique(String email) {
        return oauthUserMapper.checkEmailUnique(email) > 0;
    };
}
