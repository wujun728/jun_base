package ginyi.framework.websocket;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
@ServerEndpoint(value = "/websocket/{userId}")  // 接口路径 ws://主机ip:端口/webSocket;
public class WebSocket {

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 用户ID
     */
    private String userId;

    /**
     * concurrent包的线程安全 Set，用来存放每个客户端对应的MyWebSocket对象。
     * 虽然 @Component 默认是单例模式的，但 springboot 还是会为每个 websocket 连接初始化一个 bean，所以可以用一个静态 set 保存起来
     * 注：底下WebSocket是当前类名
     */
    private static CopyOnWriteArraySet<WebSocket> webSockets = new CopyOnWriteArraySet<>();
    /**
     * 用来存在线连接用户信息
     */
    private static ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<>();

    /**
     * 链接成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") String userId) {
        try {
            this.session = session;
            this.userId = userId;
            webSockets.add(this);
            sessionPool.put(userId, session);
            log.info("【websocket消息】新的连接，新建连接的用户是：{}, 当前总连接数为：{}", userId, webSockets.size());
        } catch (Exception e) {
        }
    }

    /**
     * 链接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        try {
            webSockets.remove(this);
            sessionPool.remove(this.userId);
            log.info("【websocket消息】连接断开，断开连接的用户是：{}，当前总连接数为：{}", userId, webSockets.size());
        } catch (Exception e) {
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     */
    @OnMessage
    public void onMessage(String message, @PathParam(value = "userId") String userId) {
        log.info("【websocket消息】收到客户端消息，发送者是：{}，消息是：{}", userId, message);
    }

    /**
     * 发送错误时的处理
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {

        log.error("用户错误，原因:{}", error.getMessage());
        error.printStackTrace();
    }


    /**
     * 此为广播消息
     *
     * @param message
     */
    public void sendAllMessage(String message) {
        log.info("【websocket消息】广播消息:" + message);
        for (WebSocket webSocket : webSockets) {
            try {
                if (webSocket.session.isOpen()) {
                    webSocket.session.getAsyncRemote().sendText(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 此为单点消息 (单人)
     *
     * @param userId
     * @param message
     */
    public void sendOneMessage(String userId, String message) {
        Session session = sessionPool.get(userId);
        if (session != null && session.isOpen()) {
            try {
                log.info("【websocket消息】 单点消息:" + message);
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 此为单点消息 (多人)
     *
     * @param userIds
     * @param message
     */
    public void sendMoreMessage(String[] userIds, String message) {
        for (String userId : userIds) {
            Session session = sessionPool.get(userId);
            if (session != null && session.isOpen()) {
                try {
                    log.info("【websocket消息】 单点消息:" + message);
                    session.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}

