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
import ginyi.system.domain.SysRole;
import ginyi.system.domain.model.dto.RoleDto;
import ginyi.system.domain.model.vo.BaseVo;
import ginyi.system.domain.model.vo.RoleVo;
import ginyi.system.mapper.SysMenuMapper;
import ginyi.system.mapper.SysRoleMapper;
import ginyi.system.service.ISysRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SysRoleServiceImpl implements ISysRoleService {

    @Resource
    private SysRoleMapper roleMapper;
    @Resource
    private SysMenuMapper menuMapper;
    @Resource
    private RedisCache redisCache;

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        List<SysRole> perms = roleMapper.selectRolePermissionByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms) {
            if (StringUtils.isNotNull(perm)) {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 角色列表
     *
     * @param roleDto
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public BaseVo<RoleVo> list(RoleDto roleDto, Long page, Long pageSize) {
        IPage<RoleVo> list = roleMapper.list(roleDto, new MyPage(page, pageSize).getPage());
        BaseVo<RoleVo> baseVo = new BaseVo<>();
        baseVo.setList(list.getRecords());
        baseVo.setCount((int) list.getTotal());
        return baseVo;
    }

    /**
     * 根据角色id获取角色
     *
     * @param roleId
     * @return
     */
    @Override
    public RoleVo getRoleByRoleId(Long roleId) {
        // 检查缓存中是否标记着空id
        if (redisCache.hasKey(CacheConstants.ROLE_NOT_EXIST_KEY + roleId)) {
            throw new CommonException(StateCode.ERROR_NOT_EXIST, roleId + CommonMessageConstants.ROLE_NOT_EXIST);
        }
        // 检查缓存中是否存在
        RoleVo role = redisCache.getCacheObject(CacheConstants.ROLE_DETAILS_BY_ROLEID_KEY + roleId, RoleVo.class);
        if (StringUtils.isNotNull(role)) {
            return role;
        }
        role = roleMapper.selectRoleByRoleId(roleId);
        if (StringUtils.isNull(role)) {
            redisCache.setCacheObject(CacheConstants.ROLE_NOT_EXIST_KEY + roleId, null);
            throw new CommonException(StateCode.ERROR_NOT_EXIST, roleId + CommonMessageConstants.ROLE_NOT_EXIST);
        }
        redisCache.setCacheObject(CacheConstants.ROLE_DETAILS_BY_ROLEID_KEY + roleId, role);
        return role;
    }

    /**
     * 新增角色
     *
     * @param roleDto
     */
    @Override
    @Transactional
    public void addRole(RoleDto roleDto) {
        // 角色名称是否被使用
        if (redisCache.hasKey(CacheConstants.ROLE_NAME_USED_KEY + roleDto.getRoleName())) {
            throw new CommonException(StateCode.ERROR_EXIST, CommonMessageConstants.ROLE_NAME_USED);
        }
        // 角色权限字符是否被使用
        if (redisCache.hasKey(CacheConstants.ROLE_CODE_USED_KEY + roleDto.getRoleName())) {
            throw new CommonException(StateCode.ERROR_EXIST, CommonMessageConstants.ROLE_PERMISSION_CODE_USED);
        }
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRole::getRoleName, roleDto.getRoleName());
        SysRole role = roleMapper.selectOne(queryWrapper);
        if (StringUtils.isNotNull(role)) {
            redisCache.setCacheObject(CacheConstants.ROLE_NAME_USED_KEY + roleDto.getRoleName(), null);
            throw new CommonException(StateCode.ERROR_EXIST, CommonMessageConstants.ROLE_NAME_USED);
        }
        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRole::getRoleKey, roleDto.getRoleKey());
        role = roleMapper.selectOne(queryWrapper);
        if (StringUtils.isNotNull(role)) {
            redisCache.setCacheObject(CacheConstants.ROLE_NAME_USED_KEY + roleDto.getRoleKey(), null);
            throw new CommonException(StateCode.ERROR_EXIST, CommonMessageConstants.ROLE_PERMISSION_CODE_USED);
        }
        roleMapper.insertRole(roleDto);
        roleMapper.insertRoleMenu(roleDto);
        redisCache.removeCacheObject(CacheConstants.ROLE_KEY_PREFIX);
    }

    /**
     * 更新角色
     *
     * @param roleDto
     */
    @Override
    @Transactional
    public void updateRole(RoleDto roleDto) {
        // 检查缓存中是否存在空id
        if (redisCache.hasKey(CacheConstants.ROLE_NOT_EXIST_KEY + roleDto.getRoleId())) {
            throw new CommonException(StateCode.ERROR_NOT_EXIST, roleDto.getRoleId() + CommonMessageConstants.ROLE_NOT_EXIST);
        }
        // 检查缓存中标记着角色权限字符已被使用
        if (redisCache.hasKey(CacheConstants.ROLE_CODE_USED_KEY + roleDto.getRoleKey())) {
            throw new CommonException(StateCode.ERROR_EXIST, CommonMessageConstants.ROLE_PERMISSION_CODE_USED);
        }
        // 检查缓存中标记着角色名称已被使用
        if (redisCache.hasKey(CacheConstants.ROLE_NAME_USED_KEY + roleDto.getRoleName())) {
            throw new CommonException(StateCode.ERROR_EXIST, CommonMessageConstants.ROLE_NAME_USED);
        }
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRole::getRoleId, roleDto.getRoleId());
        SysRole role = roleMapper.selectOne(queryWrapper);
        // 是否存在该角色
        if (StringUtils.isNull(role)) {
            redisCache.setCacheObject(CacheConstants.ROLE_NOT_EXIST_KEY + roleDto.getRoleId(), null);
            throw new CommonException(StateCode.ERROR_NOT_EXIST, roleDto.getRoleId() + CommonMessageConstants.ROLE_NOT_EXIST);
        }
        // 角色名称是否被使用
        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRole::getRoleName, roleDto.getRoleName());
        role = roleMapper.selectOne(queryWrapper);
        if (StringUtils.isNotNull(role) && !roleDto.getRoleId().equals(role.getRoleId())) {
            redisCache.setCacheObject(CacheConstants.ROLE_NAME_USED_KEY + roleDto.getRoleName(), null);
            throw new CommonException(StateCode.ERROR_EXIST, CommonMessageConstants.ROLE_NAME_USED);
        }
        // 角色权限字符是否被使用
        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRole::getRoleKey, roleDto.getRoleKey());
        role = roleMapper.selectOne(queryWrapper);
        if (StringUtils.isNotNull(role) && !roleDto.getRoleId().equals(role.getRoleId())) {
            redisCache.setCacheObject(CacheConstants.ROLE_CODE_USED_KEY + roleDto.getRoleKey(), null);
            throw new CommonException(StateCode.ERROR_EXIST, CommonMessageConstants.ROLE_PERMISSION_CODE_USED);
        }
        // 菜单id是否存在
        if (roleDto.getPermissions().size() > 0) {
            List<SysMenu> menuList = redisCache.getCacheList(CacheConstants.MENU_LIST_KEY, SysMenu.class);
            if (StringUtils.isNull(menuList) || menuList.size() == 0) {
                LambdaQueryWrapper<SysMenu> menuQueryWrapper = new LambdaQueryWrapper<>();
                menuList = menuMapper.selectList(menuQueryWrapper);
                redisCache.setCacheList(CacheConstants.MENU_LIST_KEY, menuList);
            }
            for (Long menuId : roleDto.getPermissions()) {
                boolean exist = menuList.stream().anyMatch(menu -> menuId.equals(menu.getMenuId()));
                if (!exist) {
                    // 菜单id不存在
                    throw new CommonException(StateCode.ERROR_NOT_EXIST, menuId + CommonMessageConstants.ROLE_MENU_NOT_EXIST);
                }
            }
        }
        roleMapper.updateRole(roleDto);
        if (roleDto.getPermissions().size() > 0) {
            roleMapper.updateRoleMenu(roleDto);
        }
        redisCache.removeCacheObject(CacheConstants.ROLE_KEY_PREFIX);
    }

    /**
     * 删除角色
     *
     * @param roleId
     */
    @Override
    public void removeByRoleId(Long roleId) {
        // 检查缓存中是否存在空id
        if (redisCache.hasKey(CacheConstants.ROLE_NOT_EXIST_KEY + roleId)) {
            throw new CommonException(StateCode.ERROR_NOT_EXIST, roleId + CommonMessageConstants.ROLE_NOT_EXIST);
        }
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRole::getRoleId, roleId);
        SysRole role = roleMapper.selectOne(queryWrapper);
        if (StringUtils.isNull(role)) {
            redisCache.setCacheObject(CacheConstants.ROLE_NOT_EXIST_KEY + roleId, null);
            throw new CommonException(StateCode.ERROR_NOT_EXIST, roleId + CommonMessageConstants.ROLE_NOT_EXIST);
        }
        roleMapper.deleteById(roleId);
        redisCache.removeCacheObject(CacheConstants.ROLE_KEY_PREFIX);
    }

    /**
     * 批量删除角色
     *
     * @param ids
     */
    @Override
    public void removeByRoleIds(Set<Long> ids) {
        if (ids.size() > 0) {
            List<SysRole> roleList;
            roleList = redisCache.getCacheList(CacheConstants.ROLE_LIST_KEY, SysRole.class);
            if (StringUtils.isNull(roleList) || roleList.size() == 0) {
                LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
                roleList = roleMapper.selectList(queryWrapper);
                redisCache.setCacheList(CacheConstants.ROLE_LIST_KEY, roleList);
            }
            for (Long roleId : ids) {
                // 检查缓存中是否存在空id
                if (redisCache.hasKey(CacheConstants.ROLE_NOT_EXIST_KEY + roleId)) {
                    throw new CommonException(StateCode.ERROR_NOT_EXIST, roleId + CommonMessageConstants.ROLE_NOT_EXIST);
                }
                boolean exist = roleList.stream().anyMatch(role -> roleId.equals(role.getRoleId()));
                if (!exist) {
                    redisCache.setCacheObject(CacheConstants.ROLE_NOT_EXIST_KEY + roleId, null);
                    throw new CommonException(StateCode.ERROR_NOT_EXIST, roleId + CommonMessageConstants.ROLE_NOT_EXIST);
                }
            }
            roleMapper.deleteBatchIds(ids);
            redisCache.removeCacheObject(CacheConstants.ROLE_KEY_PREFIX);
        } else {
            throw new CommonException(StateCode.ERROR_REQUEST_PARAMS, CommonMessageConstants.SYS_REQUEST_ILLEGAL);
        }
    }

    /**
     * 更新状态
     * @param roleDto
     */
    @Override
    public void updateStatus(RoleDto roleDto) {
        if (StringUtils.isNull(roleDto.getRoleId())) {
            throw new CommonException(StateCode.ERROR_PARAMS, CommonMessageConstants.ROLE_ID_NOT_FOUND);
        }
        // 状态参数是否合法
        if (!("0".equals(roleDto.getStatus()) || "1".equals(roleDto.getStatus()))) {
            throw new CommonException(StateCode.ERROR_PARAMS, CommonMessageConstants.ROLE_STATUS_ILLEGAL);
        }
        // 检查缓存中是否标记着空id
        if (redisCache.hasKey(CacheConstants.ROLE_NOT_EXIST_KEY + roleDto.getRoleId())) {
            throw new CommonException(StateCode.ERROR_NOT_EXIST, roleDto.getRoleId() + CommonMessageConstants.ROLE_NOT_EXIST);
        }
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRole::getRoleId, roleDto.getRoleId());
        SysRole menu = roleMapper.selectOne(queryWrapper);
        if (StringUtils.isNull(menu)) {
            redisCache.setCacheObject(CacheConstants.ROLE_NOT_EXIST_KEY + roleDto.getRoleId(), null);
            throw new CommonException(StateCode.ERROR_NOT_EXIST, roleDto.getRoleId() + CommonMessageConstants.ROLE_NOT_EXIST);
        }
        roleMapper.updateRoleStatus(roleDto);
    }
}
