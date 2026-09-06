package com.ruoyi.web.websocket;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.web.service.TokenService;

/**
 * 通知公告WebSocket服务端
 *
 * @author ruoyi
 */
@ServerEndpoint("/ws/notice")
@Component
public class SysNoticeWebSocketServer
{
    private static final Logger log = LoggerFactory.getLogger(SysNoticeWebSocketServer.class);

    private static final ConcurrentHashMap<String, Session> SESSIONS = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session)
    {
        LoginUser loginUser = getLoginUser(session);
        if (loginUser == null)
        {
            closeUnauthorized(session);
            return;
        }
        SpringUtils.getBean(TokenService.class).verifyToken(loginUser);
        session.getUserProperties().put("userId", loginUser.getUserId());
        session.getUserProperties().put("username", loginUser.getUsername());
        SESSIONS.put(session.getId(), session);
    }

    @OnMessage
    public void onMessage(String message, Session session)
    {
        if ("ping".equalsIgnoreCase(message) && session.isOpen())
        {
            session.getAsyncRemote().sendText("pong");
        }
    }

    @OnClose
    public void onClose(Session session)
    {
        SESSIONS.remove(session.getId());
    }

    @OnError
    public void onError(Session session, Throwable error)
    {
        if (session != null)
        {
            SESSIONS.remove(session.getId());
        }
        log.debug("通知公告WebSocket连接异常", error);
    }

    public static void broadcast(String message)
    {
        SESSIONS.forEach((id, session) -> {
            if (session.isOpen())
            {
                session.getAsyncRemote().sendText(message);
            }
            else
            {
                SESSIONS.remove(id);
            }
        });
    }

    private LoginUser getLoginUser(Session session)
    {
        String token = getFirstParameter(session.getRequestParameterMap(), "token");
        if (StringUtils.isEmpty(token))
        {
            return null;
        }
        return SpringUtils.getBean(TokenService.class).getLoginUser(token);
    }

    private String getFirstParameter(Map<String, List<String>> parameters, String name)
    {
        List<String> values = parameters.get(name);
        return values == null || values.isEmpty() ? null : values.get(0);
    }

    private void closeUnauthorized(Session session)
    {
        try
        {
            session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Unauthorized"));
        }
        catch (IOException e)
        {
            log.debug("关闭未授权通知公告WebSocket连接失败", e);
        }
    }
}
