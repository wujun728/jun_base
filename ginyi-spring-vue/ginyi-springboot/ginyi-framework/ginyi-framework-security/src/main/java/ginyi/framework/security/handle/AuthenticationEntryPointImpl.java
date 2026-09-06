package ginyi.framework.security.handle;

import com.alibaba.fastjson2.JSON;
import ginyi.common.constant.CommonMessageConstants;
import ginyi.common.result.CommonResult;
import ginyi.common.result.StateCode;
import ginyi.common.utils.ServletUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 认证失败处理类 返回未授权
 *
 * @author ruoyi
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException {
        ServletUtils.renderString(response, JSON.toJSONString(CommonResult.error(StateCode.ERROR_AUTHENTICATION_VALID, CommonMessageConstants.SYS_AUTHENTICATION_VALID)));
    }
}
