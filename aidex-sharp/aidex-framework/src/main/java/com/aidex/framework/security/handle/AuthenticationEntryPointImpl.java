package com.aidex.framework.security.handle;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson2.JSON;
import com.aidex.common.constant.HttpStatus;
import com.aidex.common.core.domain.AjaxResult;
import com.aidex.common.utils.ServletUtils;
import com.aidex.common.utils.StringUtils;

/**
 * 认证失败处理类 返回未授权
 * 
 * @author ruoyi
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable
{
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException
    {
        int code = HttpStatus.UNAUTHORIZED;
        String msg = StringUtils.format("请求访问：{}，认证失败，无法访问系统资源", request.getRequestURI());
        String source = request.getHeader("source");
        if(StringUtils.isNotEmpty(source) && "app".equals(source)){
            AjaxResult.error(code, msg);
            Map<String,String> result = new HashMap<String,String>(16);
            result.put("result","login");
            ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(msg, result)));
        }else{
            ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(code, msg)));
        }
    }
}
