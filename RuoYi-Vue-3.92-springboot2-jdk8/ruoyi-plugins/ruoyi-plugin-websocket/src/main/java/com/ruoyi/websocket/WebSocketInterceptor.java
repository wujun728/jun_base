package com.ruoyi.websocket;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.TokenService;

/**
 * WebSocket 握手拦截器
 * 用于处理连接时的 authorization 参数
 * 
 * @author ruoyi
 */
@Component
public class WebSocketInterceptor implements HandshakeInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketInterceptor.class);

    @Value("${token.header}")
    private String header;

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // 获取请求参数
        String query = request.getURI().getQuery();
        if (query != null) {
            String[] params = query.split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    attributes.put(keyValue[0], keyValue[1]);
                }
            }
        }

        String token = (String) attributes.get(header);
        // 获取 Sec-WebSocket-Protocol 头部信息（前端通过第二个参数传递的 authorization）
        List<String> protocols = request.getHeaders().get("Sec-WebSocket-Protocol");
        if (token == null && protocols != null && !protocols.isEmpty()) {
            token = protocols.get(0);
            LOGGER.info("WebSocket 连接携带 authorization: {}", token);
        }

        attributes.put(header, token);
        if (!validateToken(token)) {
            LOGGER.warn("WebSocket 连接认证失败: {}", token);
            return false;
        } else {
            LOGGER.info("WebSocket 握手成功，远程地址: {}", request.getRemoteAddress());
            return true;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Exception exception) {
        if (exception != null) {
            LOGGER.error("WebSocket 握手后异常", exception);
        }
    }

    /**
     * 验证token（示例方法，可根据实际需求实现）
     * 
     * @param token 认证token
     * @return 验证结果
     */
    private boolean validateToken(String token) {
        // 这里可以添加实际的token验证逻辑
        // 例如：解析JWT、查询数据库等
        LoginUser loginUser = tokenService.getLoginUser(token);
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNull(SecurityUtils.getAuthentication())) {
            tokenService.verifyToken(loginUser);
            return true;
        } else {
            return false;
        }
    }
}
