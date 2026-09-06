package ginyi.framework.security.service;

import ginyi.common.exception.CommonException;
import ginyi.common.constant.CommonMessageConstants;
import ginyi.common.result.StateCode;
import ginyi.common.utils.StringUtils;
import ginyi.common.utils.enums.UserStatus;
import ginyi.system.domain.LoginUser;
import ginyi.system.domain.SysUser;
import ginyi.system.service.ISysPasswordService;
import ginyi.system.service.ISysPermissionService;
import ginyi.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Resource
    private ISysUserService userService;
    @Resource
    private ISysPasswordService passwordService;
    @Resource
    private ISysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userService.selectUserByUserName(username);
        if (StringUtils.isNull(user)) {
            // 不存在
            throw new CommonException(StateCode.ERROR_UNAUTHENTICATION, CommonMessageConstants.USER_NOT_EXIST);
        } else if (UserStatus.DELETED.getCode().equals(user.getDeleted())) {
            // 被删除
            throw new CommonException(StateCode.ERROR_UNAUTHENTICATION, CommonMessageConstants.USER_IS_DELETED);
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            // 被停用
            throw new CommonException(StateCode.ERROR_UNAUTHENTICATION, CommonMessageConstants.USER_IS_FORBIDDEN);
        }

        passwordService.validate(user);
        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user) {
        return new LoginUser(user.getUserId(), user.getDeptId(), user, permissionService.getMenuPermission(user));
    }
}
