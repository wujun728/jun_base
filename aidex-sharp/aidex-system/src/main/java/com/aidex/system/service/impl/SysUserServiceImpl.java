package com.aidex.system.service.impl;

import com.aidex.common.annotation.DataScope;
import com.aidex.common.core.domain.BaseEntity;
import com.aidex.common.core.domain.entity.SysRole;
import com.aidex.common.core.domain.entity.SysUser;
import com.aidex.common.core.page.PageDomain;
import com.aidex.common.core.service.BaseServiceImpl;
import com.aidex.common.exception.BizException;
import com.aidex.common.exception.CustomException;
import com.aidex.common.utils.PinYin4JCn;
import com.aidex.common.utils.SecurityUtils;
import com.aidex.common.utils.StringUtils;
import com.aidex.common.utils.spring.SpringUtils;
import com.aidex.framework.cache.ConfigUtils;
import com.aidex.framework.cache.UserUtils;
import com.aidex.system.common.SysErrorCode;
import com.aidex.system.domain.SysPost;
import com.aidex.system.domain.SysUserPost;
import com.aidex.system.domain.SysUserRole;
import com.aidex.system.mapper.*;
import com.aidex.system.service.ISysUserService;
import com.aidex.system.service.SysConfigService;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements ISysUserService
{
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired(required = false)
    private SysRoleMapper roleMapper;

    @Autowired(required = false)
    private SysDeptMapper deptMapper;

    @Autowired(required = false)
    private SysPostMapper postMapper;

    @Autowired(required = false)
    private SysUserRoleMapper userRoleMapper;

    @Autowired(required = false)
    private SysUserPostMapper userPostMapper;

    @Autowired(required = false)
    private SysConfigService configService;

    @Autowired(required = false)
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    @DataScope(deptAlias = "t", userAlias = "t")
    public PageInfo<SysUser> findPage(SysUser sysUser) {
        return super.findPage(sysUser);
    }
    /**
     * 通过用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName)
    {
        return mapper.selectUserByUserName(userName, BaseEntity.DEL_FLAG_NORMAL);
    }


    /**
     * 查询用户所属角色组
     * 
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName)
    {
        List<SysRole> list = roleMapper.selectRolesByUserName(BaseEntity.DEL_FLAG_NORMAL,userName);
        StringBuffer idsStr = new StringBuffer();
        for (SysRole role : list)
        {
            idsStr.append(role.getRoleName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString()))
        {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 查询用户所属岗位组
     * 
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(String userName)
    {
        List<SysPost> list = postMapper.selectPostsByUserName(userName);
        StringBuffer idsStr = new StringBuffer();
        for (SysPost post : list)
        {
            idsStr.append(post.getPostName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString()))
        {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 校验用户名称是否唯一
     * 
     * @param user 用户名称
     * @return 结果
     */
    @Override
    public void checkUserNameUnique(SysUser user)
    {
        SysUser sysUserUnique = new SysUser();
        sysUserUnique.setNotEqualId(user.getId());
        sysUserUnique.setUserName(user.getUserName());
        if (!CollectionUtils.isEmpty(mapper.findListWithUnique(sysUserUnique))) {
            throw new BizException(SysErrorCode.B_SYSUSER_UserNameAlreadyExist);
        }
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public void checkPhoneUnique(SysUser user)
    {
        if(StringUtils.isNotBlank(user.getPhonenumber())){
            SysUser sysUserUnique = new SysUser();
            sysUserUnique.setNotEqualId(user.getId());
            sysUserUnique.setPhonenumber(user.getPhonenumber());
            if (!CollectionUtils.isEmpty(mapper.findListWithUnique(sysUserUnique))) {
                throw new BizException(SysErrorCode.B_SYSUSER_PhoneAlreadyExist);
            }
        }
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public void checkEmailUnique(SysUser user)
    {
        if(StringUtils.isNotBlank(user.getEmail())){
            SysUser sysUserUnique = new SysUser();
            sysUserUnique.setNotEqualId(user.getId());
            sysUserUnique.setEmail(user.getEmail());
            if (!CollectionUtils.isEmpty(mapper.findListWithUnique(sysUserUnique))) {
                throw new BizException(SysErrorCode.B_SYSUSER_EmailAlreadyExist);
            }
        }
    }

    /**
     * 校验用户是否允许操作
     * 
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user)
    {
        if (StringUtils.isNotNull(user.getId()) && user.isAdmin())
        {
            throw new CustomException("不允许操作超级管理员用户");
        }
    }

    /**
     * 新增保存用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public boolean insertUser(SysUser user)
    {
        checkUserNameUnique(user);
        checkPhoneUnique(user);
        checkEmailUnique(user);
        if(StringUtils.isBlank(user.getPassword())){
            String password = ConfigUtils.getConfigValueByKey("sys.user.initPassword","123456");
            user.setPassword(password);
        }
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUserPinyin(PinYin4JCn.getFullAndSimplePinYin(user.getName(),500));

        // 新增用户信息
        boolean result = super.save(user);
        // 新增用户岗位关联
        insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return result;
    }

    /**
     * 修改保存用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public boolean updateUser(SysUser user)
    {
        checkUserNameUnique(user);
        checkPhoneUnique(user);
        checkEmailUnique(user);
        String userId = user.getId();
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPostByUserId(userId);
        // 新增用户与岗位管理
        insertUserPost(user);
        user.setUserPinyin(PinYin4JCn.getFullAndSimplePinYin(user.getName(),500));
        return super.save(user);
    }

    /**
     * 修改用户状态
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional(readOnly = false)
    public boolean updateUserStatus(SysUser user)
    {
        return super.save(user);
    }

    /**
     * 修改用户基本信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional(readOnly = false)
    public boolean updateUserProfile(SysUser user)
    {
        return super.save(user);
    }

    /**
     * 记录登录信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional(readOnly = false)
    public boolean updateUserLoginInfo(SysUser user)
    {
        return mapper.updateUserLoginInfo(user) > 0;
    }

    /**
     * 修改用户头像
     * 
     * @param userName 用户名
     * @param avatar 头像地址
     * @return 结果
     */
    @Override
    @Transactional(readOnly = false)
    public boolean updateUserAvatar(String userName, String avatar)
    {
        return mapper.updateUserAvatar(userName, avatar) > 0 ? true : false;
    }

    /**
     * 重置用户密码
     * 
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    @Override
    @Transactional(readOnly = false)
    public int resetUserPwd(String userName, String password)
    {
        return mapper.resetUserPwd(userName, password);
    }

    /**
     * 新增用户角色信息
     * 
     * @param user 用户对象
     */
    @Override
    @Transactional(readOnly = false)
    public void insertUserRole(SysUser user)
    {
        String[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles))
        {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (String roleId : roles)
            {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.getId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0)
            {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    /**
     * 新增用户岗位信息sy
     * 
     * @param user 用户对象
     */
    @Override
    @Transactional(readOnly = false)
    public void insertUserPost(SysUser user)
    {
        String[] posts = user.getPostIds();
        if (StringUtils.isNotNull(posts))
        {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<SysUserPost>();
            for (String postId : posts)
            {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getId());
                up.setPostId(postId);
                list.add(up);
            }
            if (list.size() > 0)
            {
                userPostMapper.batchUserPost(list);
            }
        }
    }

    /**
     * 通过用户ID删除用户
     * 
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    @Transactional(readOnly = false)
    public boolean deleteUserById(String userId)
    {
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 删除用户与岗位表
        userPostMapper.deleteUserPostByUserId(userId);
        return remove(new SysUser(userId));
    }

    /**
     * 批量删除用户信息
     * 
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    @Transactional(readOnly = false)
    public int deleteUserByIds(String[] userIds)
    {
        for (String userId : userIds)
        {
            checkUserAllowed(new SysUser(userId));
        }
        // 删除用户与角色关联
        userRoleMapper.deleteUserRole(userIds);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPost(userIds);
        return mapper.deleteUserByIds(userIds, BaseEntity.DEL_FLAG_DELETE);
    }

    /**
     * 导入用户数据
     * 
     * @param userList 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    @Transactional(readOnly = false)
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(userList) || userList.size() == 0)
        {
            throw new CustomException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = ConfigUtils.getConfigValueByKey("sys.user.initPassword","123456");
        for (SysUser user : userList)
        {
            try
            {
                // 验证是否存在这个用户
                SysUser u = mapper.selectUserByUserName(user.getUserName(), BaseEntity.DEL_FLAG_NORMAL);
                if (StringUtils.isNull(u))
                {
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    this.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    //不允许更新超级管理员
                    checkUserAllowed(user);
                    user.setUpdateBy(operName);
                    this.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new CustomException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    @Override
    public List<Map<String,Object>> getUserInfoByIds(JSONObject userIdsObj){
        String[] userIds = userIdsObj.getString("userIds").split(";");
        int i = 0;
        //考虑mybatis foreach的限制，所以定义参数格式为list内还是list
        List<List<String>> idsList = new ArrayList<List<String>>();
        List<String> idList = new ArrayList<String>();
        for (String userId : userIds) {
            //当id个数超出限制后，则新new一个list来存放
            if(i % 500 == 0 && i > 0){
                idsList.add(idList);
                idList = new ArrayList<String>();
            }
            idList.add(userId);
            i++;
        }
        idsList.add(idList);
        return mapper.getUserInfoByIds(idsList);
    }

    /**
     * 根据用户ID查询用户的角色信息
     * @param sysUser
     * @return
     */
    @Override
    public PageInfo<SysUser> findRoleUserPage(SysUser sysUser) {
        PageDomain page = sysUser.getPage();
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), page.getOrderByColumn());
        return new PageInfo(mapper.findRoleUserList(sysUser));
    }

    /**
     * 写入用户角色信息
     * @param sysUserRole
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public boolean insertRoleUser(SysUserRole sysUserRole)
    {
        String[] userIds = sysUserRole.getUserId().split(";");
        String roleId = sysUserRole.getRoleId();
        int i = 0;
        //考虑mybatis foreach的限制，所以定义参数格式为list内还是list
        List<List<String>> idsList = new ArrayList<List<String>>();
        List<String> idList = new ArrayList<String>();
        for (String userId : userIds) {
            //当id个数超出限制后，则新new一个list来存放
            if(i % 500 == 0 && i > 0){
                idsList.add(idList);
                idList = new ArrayList<String>();
            }
            idList.add(userId);
            i++;
        }
        idsList.add(idList);
        List<Map<String,Object>>  resultList = sysUserRoleMapper.getRoleUserInfoByIds(idsList,roleId);
        String userUserIds = "";
        if(!CollectionUtils.isEmpty(resultList)){
            for (Map<String,Object> obj:resultList) {
                userUserIds += obj.get("user_id")+";";
            }
        }
        List<SysUserRole> userRoleList = new ArrayList<SysUserRole>();
        for (String userId : userIds) {
            System.out.println(userId+"==="+userUserIds.indexOf(userId));
            if(userUserIds.indexOf(userId) < 0){
                SysUserRole insertSysUserRole = new SysUserRole();
                insertSysUserRole.setRoleId(roleId);
                insertSysUserRole.setUserId(userId);
                userRoleList.add(insertSysUserRole);
            }
        }
        if(!CollectionUtils.isEmpty(userRoleList)){
            sysUserRoleMapper.batchUserRole(userRoleList);
        }
        return true;
    }

    /**
     * 注册用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean registerUser(SysUser user)
    {
        return mapper.insert(user) > 0;
    }

    /**
     * 刷新缓存
     * @return 结果
     */
    @Override
    public void refreshCache() {
        UserUtils.clearUserCache();
        loadingUserCache();
    }

    /**
     * 加载参数缓存数据
     */
    @Override
    public void loadingUserCache()
    {
        List<SysUser> userList = findList(new SysUser());
        for (SysUser sysUser : userList)
        {
            UserUtils.setUserCache(sysUser);
        }
    }

    /**
     * 校验用户是否有数据权限
     *
     * @param userId 用户id
     */
    @Override
    public void checkUserDataScope(String userId)
    {
        if (!SysUser.isAdmin(SecurityUtils.getUserId()))
        {
            SysUser user = new SysUser();
            user.setId(userId);
            List<SysUser> users = SpringUtils.getAopProxy(this).findList(user);
            if (StringUtils.isEmpty(users))
            {
                throw new BizException("没有权限访问用户数据！");
            }
        }
    }
}
