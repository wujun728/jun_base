package ginyi.framework.security.handle;

import com.alibaba.fastjson2.JSON;
import ginyi.common.result.CommonResult;
import ginyi.common.result.StateCode;
import ginyi.common.utils.Constants;
import ginyi.common.utils.ServletUtils;
import ginyi.common.utils.StringUtils;
import ginyi.framework.security.manager.AsyncManager;
import ginyi.framework.security.manager.factory.AsyncFactory;
import ginyi.system.domain.LoginUser;
import ginyi.system.service.ITokenService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义退出处理类 返回成功
 *
 * @author ruoyi
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    @Resource
    private ITokenService tokenService;

    /**
     * 退出处理
     *
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        LoginUser loginUser = tokenService.getLoginUser(request);
        // 判断token是否存在
        if (StringUtils.isNull(loginUser)) {
            ServletUtils.renderString(response, JSON.toJSONString(CommonResult.error(StateCode.ERROR_TIMEOUT_TOKEN)));
        }

        String userName = loginUser.getUsername();
        // 删除用户缓存记录
        tokenService.delLoginUser(loginUser.getToken());
        // 记录用户退出日志
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGOUT, "退出成功"));
        ServletUtils.renderString(response, JSON.toJSONString(CommonResult.success()));
    }
}
