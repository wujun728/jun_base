package ginyi.framework.security.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import ginyi.common.constant.CacheConstants;
import ginyi.common.constant.CommonMessageConstants;
import ginyi.common.constant.UserConstants;
import ginyi.common.exception.CommonException;
import ginyi.common.mysql.MyPage;
import ginyi.common.redis.cache.RedisCache;
import ginyi.common.result.StateCode;
import ginyi.common.utils.StringUtils;
import ginyi.system.domain.SysDept;
import ginyi.system.domain.SysPost;
import ginyi.system.domain.SysRole;
import ginyi.system.domain.SysUser;
import ginyi.system.domain.model.dto.DeptDto;
import ginyi.system.domain.model.dto.PostDto;
import ginyi.system.domain.model.dto.RoleDto;
import ginyi.system.domain.model.dto.UserDto;
import ginyi.system.domain.model.vo.BaseVo;
import ginyi.system.domain.model.vo.UserVo;
import ginyi.system.mapper.SysDeptMapper;
import ginyi.system.mapper.SysPostMapper;
import ginyi.system.mapper.SysRoleMapper;
import ginyi.system.mapper.SysUserMapper;
import ginyi.system.service.ISysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl implements ISysUserService {

    @Resource
    private SysUserMapper userMapper;
    @Resource
    private SysPostMapper postMapper;
    @Resource
    private SysRoleMapper roleMapper;
    @Resource
    private SysDeptMapper deptMapper;
    @Resource
    private RedisCache redisCache;

    /**
     * 修改用户基本信息
     *
     * @param userDto 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public void updateUser(UserDto userDto) {
        if (checkLogic(userDto)) {
            userMapper.updateUser(userDto);
            if (StringUtils.isNotNull(userDto.getPostIds()) && userDto.getPostIds().size() > 0) {
                userMapper.updateUserPostIds(userDto);
            }
            if (StringUtils.isNotNull(userDto.getRoleIds()) && userDto.getRoleIds().size() > 0) {
                userMapper.updateUserRoleIds(userDto);
            }
            // 清除缓存
            redisCache.removeCacheObject(CacheConstants.USER_KEY_PREFIX);
        }
    }


    /**
     * 新增用户
     *
     * @param userDto
     */
    @Override
    @Transactional
    public void addUser(UserDto userDto) {
        if (checkLogic(userDto)) {
            userMapper.insertUser(userDto);
            if (StringUtils.isNotNull(userDto.getPostIds()) && userDto.getPostIds().size() > 0) {
                userMapper.insertUserPostIds(userDto);
            }
            if (StringUtils.isNotNull(userDto.getRoleIds()) && userDto.getRoleIds().size() > 0) {
                userMapper.insertUserRoleIds(userDto);
            }
            // 清除缓存
            redisCache.removeCacheObject(CacheConstants.USER_KEY_PREFIX);
            redisCache.removeCacheObject(CacheConstants.POST_KEY_PREFIX);
            redisCache.removeCacheObject(CacheConstants.DEPT_KEY_PREFIX);
        }
    }

    /**
     * 根据用户id查询用户
     *
     * @param userId
     */
    @Override
    public UserVo getUserByUserId(String userId) {
        // 检查缓存中是否记录着空的userId
        if (redisCache.hasKey(CacheConstants.USER_NOT_EXIST_KEY + userId)) {
            throw new CommonException(StateCode.ERROR_NOT_EXIST, CommonMessageConstants.USER_NOT_EXIST);
        }
        UserVo user;
        // 检查缓存中是否有该user
        user = redisCache.getCacheObject(CacheConstants.USER_DETAILS_BY_USERID_KEY + userId, UserVo.class);
        if (StringUtils.isNotNull(user)) {
            return user;
        }
        user = userMapper.selectUserByUserId(userId);
        if (StringUtils.isNull(user)) {
            redisCache.setCacheObject(CacheConstants.USER_NOT_EXIST_KEY + userId, null);
            throw new CommonException(StateCode.ERROR_NOT_EXIST, CommonMessageConstants.USER_NOT_EXIST);
        }
        // 存入缓存
        redisCache.setCacheObject(CacheConstants.USER_DETAILS_BY_USERID_KEY + userId, user);
        return user;
    }


    /**
     * 获取用户列表(不含admin)
     *
     * @param userDto
     * @param page
     * @param pageSize
     */
    @Override
    public BaseVo<UserVo> list(UserDto userDto, Long page, Long pageSize) {
        IPage<UserVo> list = userMapper.list(userDto, new MyPage(page, pageSize).getPage());
        BaseVo<UserVo> baseVo = new BaseVo<>();
        baseVo.setList(list.getRecords());
        baseVo.setCount((int) list.getTotal());
        return baseVo;
    }


    /**
     * 根据用户id删除用户
     *
     * @param userId
     */
    @Override
    public void removeById(Long userId) {
        // 检查缓存中是否标记着空id
        if (redisCache.hasKey(CacheConstants.USER_NOT_EXIST_KEY + userId)) {
            throw new CommonException(StateCode.ERROR_NOT_EXIST, userId + CommonMessageConstants.USER_NOT_EXIST);
        }
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUserId, userId);
        SysUser user = userMapper.selectOne(queryWrapper);
        if (StringUtils.isNull(user)) {
            redisCache.setCacheObject(CacheConstants.USER_NOT_EXIST_KEY + userId, null);
            throw new CommonException(StateCode.ERROR_NOT_EXIST, userId + CommonMessageConstants.USER_NOT_EXIST);
        }
        userMapper.deleteById(userId);
    }

    /**
     * 批量删除用户
     *
     * @param ids
     */
    @Override
    @Transactional
    public void removeUserByIds(Set<Long> ids) {
        if (ids.size() > 0) {
            List<SysUser> userList;
            userList = redisCache.getCacheList(CacheConstants.USER_LIST_KEY, SysUser.class);
            if (StringUtils.isNull(userList) || userList.size() == 0) {
                LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
                userList = userMapper.selectList(queryWrapper);
                redisCache.setCacheList(CacheConstants.USER_LIST_KEY, userList);
            }
            for (Long userId : ids) {
                // 检查缓存中是否标记着空id
                if (redisCache.hasKey(CacheConstants.USER_NOT_EXIST_KEY + userId)) {
                    throw new CommonException(StateCode.ERROR_NOT_EXIST, userId + CommonMessageConstants.USER_NOT_EXIST);
                }
                boolean exist = userList.stream().anyMatch(user -> userId.equals(user.getUserId()));
                if (!exist) {
                    redisCache.setCacheObject(CacheConstants.USER_NOT_EXIST_KEY + userId, null);
                    throw new CommonException(StateCode.ERROR_NOT_EXIST, userId + CommonMessageConstants.USER_NOT_EXIST);
                }
            }
            userMapper.deleteBatchIds(ids);
            redisCache.removeCacheObject(CacheConstants.USER_KEY_PREFIX);
        } else {
            throw new CommonException(StateCode.ERROR_REQUEST_PARAMS, CommonMessageConstants.SYS_REQUEST_ILLEGAL);
        }
    }

    /**
     * 更改用户状态（0正常 1停用）
     *
     * @param userDto
     */
    @Override
    public void updateStatus(UserDto userDto) {
        if (StringUtils.isNull(userDto.getUserId())) {
            throw new CommonException(StateCode.ERROR_PARAMS, CommonMessageConstants.USER_ID_NOT_FOUND);
        }
        // 状态参数是否合法
        if (!("0".equals(userDto.getStatus()) || "1".equals(userDto.getStatus()))) {
            throw new CommonException(StateCode.ERROR_PARAMS, CommonMessageConstants.USER_STATUS_ILLEGAL);
        }
        // 检查缓存中是否标记着空id
        if (redisCache.hasKey(CacheConstants.USER_NOT_EXIST_KEY + userDto.getUserId())) {
            throw new CommonException(StateCode.ERROR_NOT_EXIST, userDto.getUserId() + CommonMessageConstants.USER_NOT_EXIST);
        }
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUserId, userDto.getUserId());
        SysUser user = userMapper.selectOne(queryWrapper);
        if (StringUtils.isNull(user)) {
            redisCache.setCacheObject(CacheConstants.USER_NOT_EXIST_KEY + userDto.getUserId(), null);
            throw new CommonException(StateCode.ERROR_NOT_EXIST, userDto.getUserId() + CommonMessageConstants.USER_NOT_EXIST);
        }
        userMapper.updateUserStatus(userDto);
    }

    /**
     * 根据部门 id 获取用户列表
     *
     * @param deptDto
     * @return
     */
    @Override
    public BaseVo<HashMap<String, Object>> getUserListByDeptIds(DeptDto deptDto) {
        List<Long> deptIds = deptDto.getDeptIds();
        if (deptIds.size() == 0) {
            throw new CommonException(StateCode.ERROR_PARAMS_SERVICE, CommonMessageConstants.SYS_REQUEST_ILLEGAL);
        }
        List<SysDept> deptList = deptMapper.selectList(null);
        // 判断部门id是否合法
        for (Long deptId : deptIds) {
            boolean isExist = false;
            for (SysDept dept : deptList) {
                if (deptId.equals(dept.getDeptId())) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                throw new CommonException(StateCode.ERROR_NOT_EXIST, deptId + CommonMessageConstants.DEPT_NOT_EXIST);
            }
        }
        List<SysUser> userList = userMapper.selectUserByDeptIds(deptIds);

        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();
        for (SysUser user : userList) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("userId", user.getUserId());
            map.put("nickName", user.getNickName());
            arrayList.add(map);
        }

        BaseVo<HashMap<String, Object>> baseVo = new BaseVo<>();
        baseVo.setList(arrayList);
        baseVo.setCount(arrayList.size());
        return baseVo;
    }

    /**
     * 根据岗位id获取用户列表
     * @param postDto
     * @return
     */
    @Override
    public BaseVo<HashMap<String, Object>> getUserListByPostIds(PostDto postDto) {
        List<Long> postIds = postDto.getPostIds();
        if (postIds.size() == 0) {
            throw new CommonException(StateCode.ERROR_PARAMS_SERVICE, CommonMessageConstants.SYS_REQUEST_ILLEGAL);
        }
        List<SysPost> postList = postMapper.selectList(null);
        // 判断岗位id是否合法
        for (Long postId : postIds) {
            boolean isExist = false;
            for (SysPost post : postList) {
                if (postId.equals(post.getPostId())) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                throw new CommonException(StateCode.ERROR_NOT_EXIST, postId + CommonMessageConstants.POST_NOT_EXIST);
            }
        }
        // 查出所有用户
        IPage<UserVo> userList = userMapper.list(new UserDto(), new MyPage(null, null).getPage());

        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();
        for (Long postId : postDto.getPostIds()) {
            for (UserVo user : userList.getRecords()) {
                HashMap<String, Object> map = new HashMap<>();
                if (user.getPostIds().contains(postId)) {
                    map.put("userId", user.getUserId());
                    map.put("nickName", user.getNickName());
                    arrayList.add(map);
                }
            }
        }
        // 去重
        List<HashMap<String, Object>> resultList = arrayList.stream().distinct().collect(Collectors.toList());

        BaseVo<HashMap<String, Object>> baseVo = new BaseVo<>();
        baseVo.setList(resultList);
        baseVo.setCount(resultList.size());
        return baseVo;
    }

    /**
     * 根据角色 id 获取用户列表
     * @param roleDto
     * @return
     */
    @Override
    public BaseVo<HashMap<String, Object>> getUserListByRoleIds(RoleDto roleDto) {
        List<Long> roleIds = roleDto.getRoleIds();
        if (roleIds.size() == 0) {
            throw new CommonException(StateCode.ERROR_PARAMS_SERVICE, CommonMessageConstants.SYS_REQUEST_ILLEGAL);
        }
        List<SysRole> roleList = roleMapper.selectList(null);
        // 判断岗位id是否合法
        for (Long roleId : roleIds) {
            boolean isExist = false;
            for (SysRole role : roleList) {
                if (roleId.equals(role.getRoleId())) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                throw new CommonException(StateCode.ERROR_NOT_EXIST, roleId + CommonMessageConstants.ROLE_NOT_EXIST);
            }
        }
        // 查出所有用户
        IPage<UserVo> userList = userMapper.list(new UserDto(), new MyPage(null, null).getPage());

        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();
        for (Long roleId : roleDto.getRoleIds()) {
            for (UserVo user : userList.getRecords()) {
                HashMap<String, Object> map = new HashMap<>();
                if (user.getRoleIds().contains(roleId)) {
                    map.put("userId", user.getUserId());
                    map.put("nickName", user.getNickName());
                    arrayList.add(map);
                }
            }
        }
        // 去重
        List<HashMap<String, Object>> resultList = arrayList.stream().distinct().collect(Collectors.toList());

        BaseVo<HashMap<String, Object>> baseVo = new BaseVo<>();
        baseVo.setList(resultList);
        baseVo.setCount(resultList.size());
        return baseVo;
    }

    /**
     * 校验逻辑
     *
     * @param userDto
     */
    public boolean checkLogic(UserDto userDto) {
        // 判断部门是否存在
        if (StringUtils.isNotNull(userDto.getDeptId())) {
            // 检查缓存中是否标记着空id
            if (redisCache.hasKey(CacheConstants.DEPT_NOT_EXIST_KEY + userDto.getDeptId())) {
                throw new CommonException(StateCode.ERROR_NOT_EXIST, userDto.getDeptId() + CommonMessageConstants.DEPT_NOT_EXIST);
            }
            SysDept sysDept;
            LambdaQueryWrapper<SysDept> deptQueryWrapper = new LambdaQueryWrapper<>();
            deptQueryWrapper.eq(SysDept::getDeptId, userDto.getDeptId());
            sysDept = deptMapper.selectOne(deptQueryWrapper);
            if (sysDept == null) {
                redisCache.setCacheObject(CacheConstants.DEPT_NOT_EXIST_KEY + userDto.getDeptId(), null);
                throw new CommonException(StateCode.ERROR_NOT_EXIST, userDto.getDeptId() + CommonMessageConstants.DEPT_NOT_EXIST);
            }
        }

        // 用户名是否被占用（插入时）
        if (StringUtils.isNotBlank(userDto.getUserName()) && StringUtils.isNull(userDto.getUserId())) {
            LambdaQueryWrapper<SysUser> userQueryWrapper = new LambdaQueryWrapper<>();
            userQueryWrapper.eq(SysUser::getUserName, userDto.getUserName());
            SysUser user = userMapper.selectOne(userQueryWrapper);
            if (user != null) {
                throw new CommonException(StateCode.ERROR_EXIST, CommonMessageConstants.USER_NAME_USED);
            }
        }

        // 判断插入的岗位id是否存在
        if (StringUtils.isNotNull(userDto.getPostIds()) && userDto.getPostIds().size() > 0) {
            SysPost sysPost;
            for (Long postId : userDto.getPostIds()) {
                if (redisCache.hasKey(CacheConstants.POST_NOT_EXIST_KEY + postId)) {
                    throw new CommonException(StateCode.ERROR_NOT_EXIST, postId + CommonMessageConstants.POST_NOT_EXIST);
                }
                LambdaQueryWrapper<SysPost> postQueryWrapper = new LambdaQueryWrapper<>();
                postQueryWrapper.eq(SysPost::getPostId, postId);
                sysPost = postMapper.selectOne(postQueryWrapper);
                if (sysPost == null) {
                    redisCache.setCacheObject(CacheConstants.POST_NOT_EXIST_KEY + postId, null);
                    throw new CommonException(StateCode.ERROR_NOT_EXIST, postId + CommonMessageConstants.POST_NOT_EXIST);
                }
            }
        }

        // 判断插入的角色id是否存在
        if (StringUtils.isNotNull(userDto.getRoleIds()) && userDto.getRoleIds().size() > 0) {
            SysRole sysRole;
            for (Long roleId : userDto.getRoleIds()) {
                if (redisCache.hasKey(CacheConstants.ROLE_NOT_EXIST_KEY + roleId)) {
                    throw new CommonException(StateCode.ERROR_NOT_EXIST, roleId + CommonMessageConstants.ROLE_NOT_EXIST);
                }
                LambdaQueryWrapper<SysRole> roleQueryWrapper = new LambdaQueryWrapper<>();
                roleQueryWrapper.eq(SysRole::getRoleId, roleId);
                sysRole = roleMapper.selectOne(roleQueryWrapper);
                if (sysRole == null) {
                    redisCache.setCacheObject(CacheConstants.ROLE_NOT_EXIST_KEY + roleId, null);
                    throw new CommonException(StateCode.ERROR_NOT_EXIST, roleId + CommonMessageConstants.ROLE_NOT_EXIST);
                }
            }
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
    public boolean registerUser(SysUser user) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userMapper.insertUser(userDto) > 0;
    }

    @Override
    public SysUser selectUserByUserName(String userName) {
        return userMapper.selectUserByUserName(userName);
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public String checkUserNameUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkUserNameUnique(user.getUserName());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}
