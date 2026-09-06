package com.geek.system.service.impl;

import static com.geek.common.core.domain.entity.table.SysDeptTableDef.*;
import static com.geek.common.core.domain.entity.table.SysUserTableDef.*;
import static com.geek.system.domain.table.SysPostTableDef.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.geek.common.annotation.DataScope;
import com.geek.common.core.domain.entity.SysDept;
import com.geek.common.core.domain.entity.SysRole;
import com.geek.common.core.domain.entity.SysUser;
import com.geek.common.exception.ServiceException;
import com.geek.common.utils.DateUtils;
import com.geek.common.utils.SecurityUtils;
import com.geek.common.utils.StringUtils;
import com.geek.common.utils.bean.BeanValidators;
import com.geek.common.utils.poi.ExcelUtil;
import com.geek.common.utils.sql.SqlUtil;
import com.geek.system.domain.SysPost;
import com.geek.system.domain.SysUserPost;
import com.geek.system.domain.SysUserRole;
import com.geek.system.mapper.SysRoleMapper;
import com.geek.system.mapper.SysUserMapper;
import com.geek.system.mapper.SysUserPostMapper;
import com.geek.system.mapper.SysUserRoleMapper;
import com.geek.system.service.ISysConfigService;
import com.geek.system.service.ISysUserService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Validator;

/**
 * 用户 业务层处理
 * 
 * @author geek
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    protected Validator validator;

    /**
     * 根据条件分页查询用户列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public QueryChain<SysUser> selectUserList(SysUser user) {
        QueryChain<SysUser> queryChain = this.queryChain()
                .select(SYS_USER.DEFAULT_COLUMNS, SYS_DEPT.DEPT_NAME, SYS_DEPT.LEADER)
                .from(SysUser.class).as("u")
                .leftJoin(SysDept.class).as("d")
                .on(SysUser::getDeptId, SysDept::getDeptId)
                .eq(SysUser::getUserId, user.getUserId())
                .like(SysUser::getUserName, user.getUserName())
                .eq(SysUser::getStatus, user.getStatus())
                .like(SysUser::getPhonenumber, user.getPhonenumber())
                .ge(SysUser::getCreateTime, user.getParams().get("beginTime"))
                .le(SysUser::getCreateTime, user.getParams().get("endTime"));
        if (user.getDeptId() != null) {
            queryChain.and(SYS_DEPT.DEPT_ID.eq(user.getDeptId())
                    .or(SYS_USER.DEPT_ID.in(QueryWrapper.create().from(SYS_DEPT)
                            .select(SYS_DEPT.DEPT_ID)
                            .where(SqlUtil.findInSet(user.getDeptId().toString(), "ancestors")))));
        }
        return queryChain;
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public Page<SysUser> page(SysUser user, int pageNum, int pageSize) {
        Page<SysUser> pg = Page.of(pageNum, pageSize);
        pg.setOptimizeCountQuery(false);
        return selectUserList(user).page(pg);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public void export(SysUser user, HttpServletResponse response) {
        List<SysUser> list = selectUserList(user).list();
        ExcelUtil<SysUser> util = new ExcelUtil<>(SysUser.class);
        util.exportExcel(response, list, "用户数据");
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public Page<SysUser> selectAllocatedList(SysUser user, int pageNum, int pageSize) {
        return this.queryChain().from(SysUser.class).as("u")
                .leftJoin(SysDept.class).as("d")
                .on(SysUser::getDeptId, SysDept::getDeptId)
                .leftJoin(SysUserRole.class)
                .on(SysUser::getUserId, SysUserRole::getUserId)
                .leftJoin(SysRole.class)
                .on(SysRole::getRoleId, SysUserRole::getRoleId)
                .eq(SysRole::getRoleId, user.getRoleId())
                .like(SysUser::getUserName, user.getUserName())
                .like(SysUser::getPhonenumber, user.getPhonenumber())
                .page(Page.of(pageNum, pageSize));
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUnallocatedList(SysUser user) {
        return userMapper.selectUnallocatedList(user);
    }

    /**
     * 通过用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName) {
        return userMapper.selectOneWithRelationsByQuery(
                QueryWrapper.create().from(SysUser.class).eq(SysUser::getUserName, userName));
    }

    /**
     * 通过用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long userId) {
        return userMapper.selectOneWithRelationsById(userId);
    }

    /**
     * 通过手机号查询用户
     * 
     * @param phone 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByPhone(String phone) {
        return userMapper.selectOneWithRelationsByQuery(
                QueryWrapper.create().from(SysUser.class).eq(SysUser::getPhonenumber, phone));
    }

    /**
     * 通过邮箱查询用户
     * 
     * @param email 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByEmail(String email) {
        return userMapper
                .selectOneWithRelationsByQuery(QueryWrapper.create().from(SysUser.class).eq(SysUser::getEmail, email));
    }

    /**
     * 查询用户所属角色组
     * 
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName) {
        List<SysRole> list = roleMapper.baseQueryChain().eq(SysUser::getUserName, userName).list();
        QueryChain.of(SysRole.class)
                .leftJoin(SysUserRole.class)
                .on(SysUserRole::getRoleId, SysRole::getRoleId)
                .leftJoin(SysUser.class)
                .on(SysUser::getUserId, SysUserRole::getUserId)
                .leftJoin(SysDept.class)
                .on(SysUser::getDeptId, SysDept::getDeptId)
                .eq(SysUser::getUserName, userName)
                .list();
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
    }

    /**
     * 查询用户所属岗位组
     * 
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(String userName) {
        List<SysPost> list = QueryChain.of(SysPost.class)
                .select(SYS_POST.POST_ID, SYS_POST.POST_NAME, SYS_POST.POST_CODE)
                .leftJoin(SysUserPost.class)
                .on(SysUserPost::getPostId, SysPost::getPostId)
                .leftJoin(SysUser.class)
                .on(SysUser::getUserId, SysUserPost::getUserId)
                .eq(SysUser::getUserName, userName)
                .list();
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysPost::getPostName).collect(Collectors.joining(","));
    }

    /**
     * 校验用户名称是否唯一
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean checkUserNameUnique(SysUser user) {
        return !this.queryChain()
                .eq(SysUser::getUserName, user.getUserName())
                .ne(SysUser::getUserId, user.getUserId())
                .exists();
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public boolean checkPhoneUnique(SysUser user) {
        return !this.queryChain()
                .eq(SysUser::getPhonenumber, user.getPhonenumber())
                .ne(SysUser::getUserId, user.getUserId())
                .exists();
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public boolean checkEmailUnique(SysUser user) {
        return !this.queryChain()
                .eq(SysUser::getEmail, user.getEmail())
                .ne(SysUser::getUserId, user.getUserId())
                .exists();
    }

    /**
     * 校验用户是否允许操作
     * 
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }

    /**
     * 校验用户是否有数据权限
     * 
     * @param userId 用户id
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public void checkUserDataScope(Long userId) {
        if (!SecurityUtils.isAdmin()) {
            SysUser user = new SysUser();
            user.setUserId(userId);
            List<SysUser> users = selectUserList(user).list();
            if (StringUtils.isEmpty(users)) {
                throw new ServiceException("没有权限访问用户数据！");
            }
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
    public boolean insertUser(SysUser user) {
        // 新增用户信息
        boolean flag = super.save(user);
        // 新增用户岗位关联
        insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return flag;
    }

    /**
     * 注册用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean registerUser(SysUser user) {
        return super.save(user);
    }

    /**
     * 修改保存用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public boolean updateUser(SysUser user) {
        Long userId = user.getUserId();
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPostByUserId(userId);
        // 新增用户与岗位管理
        insertUserPost(user);
        return super.updateById(user);
    }

    /**
     * 用户授权角色
     * 
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    @Override
    @Transactional
    public void insertUserAuth(Long userId, List<Long> roleIds) {
        userRoleMapper.deleteUserRoleByUserId(userId);
        insertUserRole(userId, roleIds);
    }

    /**
     * 修改用户状态
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean updateUserStatus(SysUser user) {
        return super.updateById(user);
    }

    /**
     * 修改用户基本信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean updateUserProfile(SysUser user) {
        return super.updateById(user);
    }

    /**
     * 修改用户头像
     * 
     * @param userName 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(String userName, String avatar) {
        return this.updateChain()
                .eq(SysUser::getUserName, userName)
                .set(SysUser::getAvatar, avatar)
                .update();
    }

    /**
     * 重置用户密码
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean resetPwd(SysUser user) {
        return super.save(user);
    }

    /**
     * 重置用户密码
     * 
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    @Override
    public boolean resetUserPwd(String userName, String password) {
        return this.updateChain()
                .eq(SysUser::getUserName, userName)
                .set(SysUser::getPwdUpdateDate, DateUtils.getNowDate())
                .set(SysUser::getPassword, password)
                .update();
    }

    /**
     * 新增用户角色信息
     * 
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user) {
        this.insertUserRole(user.getUserId(), user.getRoleIds());
    }

    /**
     * 新增用户岗位信息
     * 
     * @param user 用户对象
     */
    public void insertUserPost(SysUser user) {
        List<Long> posts = user.getPostIds();
        if (StringUtils.isNotEmpty(posts)) {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<>(posts.size());
            for (Long postId : posts) {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            userPostMapper.batchUserPost(list);
        }
    }

    /**
     * 新增用户角色信息
     * 
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    public void insertUserRole(Long userId, List<Long> roleIds) {
        if (StringUtils.isNotEmpty(roleIds)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<>(roleIds.size());
            for (Long roleId : roleIds) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            userRoleMapper.batchUserRole(list);
        }
    }

    /**
     * 通过用户ID删除用户
     * 
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserById(Long userId) {
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 删除用户与岗位表
        userPostMapper.deleteUserPostByUserId(userId);
        return userMapper.deleteById(userId);
    }

    /**
     * 批量删除用户信息
     * 
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public boolean deleteUserByIds(List<Long> userIds) {
        for (Long userId : userIds) {
            checkUserAllowed(new SysUser(userId));
            checkUserDataScope(userId);
        }
        // 删除用户与角色关联
        UpdateChain.of(SysUserRole.class)
                .in(SysUserRole::getUserId, userIds)
                .remove();
        // 删除用户与岗位关联
        UpdateChain.of(SysUserPost.class)
                .in(SysUserPost::getUserId, userIds)
                .remove();
        return super.removeByIds(userIds);
    }

    /**
     * 导入用户数据
     * 
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(userList) || userList.size() == 0) {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (SysUser user : userList) {
            try {
                // 验证是否存在这个用户
                SysUser u = selectUserByUserName(user.getUserName());
                if (StringUtils.isNull(u)) {
                    BeanValidators.validateWithException(validator, user);
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    userMapper.insert(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, user);
                    checkUserAllowed(u);
                    checkUserDataScope(u.getUserId());
                    user.setUserId(u.getUserId());
                    user.setUpdateBy(operName);
                    userMapper.update(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}
