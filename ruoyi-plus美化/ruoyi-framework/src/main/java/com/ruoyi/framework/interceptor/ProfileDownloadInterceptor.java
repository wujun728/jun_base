package com.ruoyi.framework.interceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUtils;

/**
 * @author byH
 * @date Created 2026/1/20 10:51
 */
@Component
public class ProfileDownloadInterceptor implements HandlerInterceptor
{
    private static final String RESOURCE_PREFIX = Constants.RESOURCE_PREFIX + "/";
    private static final String NOSNIFF_HEADER = "X-Content-Type-Options";
    private static final String NOSNIFF_VALUE = "nosniff";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        String requestUri = request.getRequestURI();
        if (!StringUtils.contains(requestUri, RESOURCE_PREFIX))
        {
            return true;
        }
        String fileName = StringUtils.substringAfterLast(requestUri, "/");
        if (StringUtils.isEmpty(fileName))
        {
            fileName = "download";
        }
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(NOSNIFF_HEADER, NOSNIFF_VALUE);
        FileUtils.setAttachmentResponseHeader(response, fileName);
        return true;
    }
}