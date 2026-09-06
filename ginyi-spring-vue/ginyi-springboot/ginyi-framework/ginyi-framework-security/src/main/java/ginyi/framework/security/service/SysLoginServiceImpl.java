package ginyi.framework.security.service;

import ginyi.common.constant.CommonMessageConstants;
import ginyi.common.constant.UserConstants;
import ginyi.common.exception.CommonException;
import ginyi.common.exception.UserPasswordNotMatchException;
import ginyi.common.exception.UserPasswordRetryLimitExceedException;
import ginyi.common.result.StateCode;
import ginyi.common.utils.Constants;
import ginyi.common.utils.DateUtils;
import ginyi.common.utils.ServletUtils;
import ginyi.common.utils.ip.IpUtils;
import ginyi.framework.security.context.AuthenticationContextHolder;
import ginyi.framework.security.manager.AsyncManager;
import ginyi.framework.security.manager.factory.AsyncFactory;
import ginyi.framework.security.utils.SecurityUtils;
import ginyi.system.domain.LoginUser;
import ginyi.system.domain.SysUser;
import ginyi.system.domain.model.dto.LoginDto;
import ginyi.system.domain.model.dto.RegisterDto;
import ginyi.system.domain.model.dto.UserDto;
import ginyi.system.domain.model.vo.LoginVo;
import ginyi.system.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysLoginServiceImpl implements ISysLoginService {

    @Value("${token.prefix}")
    private String tokenPrefix;
    @Value("${token.header}")
    private String tokenHeader;
    @Resource
    private ISysConfigService configService;
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private ISysUserService userService;
    @Resource
    private ITokenService tokenService;
    @Resource
    private IVerifyService verifyService;

    /**
     * 登录验证
     *
     * @param loginDto
     * @return
     */
    @Override
    public LoginVo login(LoginDto loginDto) {

        String username = loginDto.getUsername();
        String password = loginDto.getPassword();
        String code = loginDto.getCode();

        boolean captchaEnabled = configService.selectCaptchaEnabled();
        // 图片验证码开关
        if (captchaEnabled) {
            verifyService.checkImgCode(code);
        }
        // 用户验证
        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);

            // 登录成功
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            recordLoginInfo(loginUser.getUserId());
            String token = tokenService.createToken(loginUser);

            // 生成token
            LoginVo loginVo = new LoginVo();
            loginVo.setToken(tokenPrefix + " " +token);
            loginVo.setTokenHeader(tokenHeader);

            // 记录日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, CommonMessageConstants.LOGIN_SUCCESS));
            return loginVo;
        } catch (Exception e) {
            if (e instanceof AuthenticationException) {
                // 账户被锁定
                if (e.getCause() instanceof UserPasswordRetryLimitExceedException) {
                    UserPasswordRetryLimitExceedException userPasswordRetryLimitExceedException = (UserPasswordRetryLimitExceedException) e.getCause();
                    AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, CommonMessageConstants.USER_IS_LOCKED));
                    throw new UserPasswordRetryLimitExceedException(
                            userPasswordRetryLimitExceedException.getState(),
                            userPasswordRetryLimitExceedException.getData()
                    );
                }
                // 密码不匹配
                if (e.getCause() instanceof UserPasswordNotMatchException) {
                    AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, CommonMessageConstants.USER_PASSWORD_NOT_MATCH));
                    throw new UserPasswordNotMatchException();
                }
                // 其他异常（被禁用、被删除、被停用）
                if(e.getCause() instanceof CommonException){
                    CommonException commonException = (CommonException) e.getCause();
                    AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, String.valueOf(commonException.getData())));
                    throw new CommonException(commonException.getState(), commonException.getData());
                }
            } else {
                // 其他的未知异常
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new CommonException(StateCode.ERROR_SYSTEM);
            }
        } finally {
            AuthenticationContextHolder.clearContext();
        }
        // 其他的未知异常
        return null;
    }


    /**
     * 用户注册
     *
     * @param registerDto
     */
    @Override
    public void register(RegisterDto registerDto) {
        // 图片验证码开关
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            verifyService.checkImgCode(registerDto.getCode());
        }

        SysUser sysUser = new SysUser();
        sysUser.setUserName(registerDto.getUsername());

        // 当前注册的用户是否存在
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(sysUser))) {
            throw new CommonException(StateCode.ERROR_EXIST, CommonMessageConstants.USER_EXIST);
        } else {
            sysUser.setNickName(registerDto.getUsername());
            sysUser.setPassword(SecurityUtils.encryptPassword(registerDto.getPassword()));
            boolean regFlag = userService.registerUser(sysUser);
            if (!regFlag) {
                throw new CommonException(StateCode.ERROR_SYSTEM);
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(registerDto.getUsername(), Constants.REGISTER, CommonMessageConstants.REGISTER_SUCCESS));
            }
        }
    }


    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId) {
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        userDto.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        userDto.setLoginDate(DateUtils.getNowDate());
        userService.updateUser(userDto);
    }
}
