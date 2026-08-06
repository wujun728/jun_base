package com.ruoyi.netty.websocket.endpoints;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

/**
 * 一个简单的 TCP 长连接服务示例：\n
 * 协议约定：\n
 * 1. 客户端连接后首先发送认证报文：AUTH:clientId (以换行结尾)；认证成功后可正常收发。\n
 * 2. 心跳：客户端定期发送 PING，服务端回复 PONG。\n
 * 3. 其它普通文本消息，服务端原样回显(ECHO)。\n
 * 4. 服务器可通过静态方法 sendToClient / broadcast 发送消息。\n
 * 行分隔符使用标准的换行符(\n 或 \r\n)，通过 DelimiterBasedFrameDecoder 解析。\n
 */
@Component
public class TcpLongConnectionServer {

    private static final Logger log = LoggerFactory.getLogger(TcpLongConnectionServer.class);

    /** 是否启用 */
    @Value("${netty.tcp.enable:true}")
    private boolean enable;

    /** 监听端口 */
    @Value("${netty.tcp.port:18888}")
    private int port;

    /** boss 线程数 */
    @Value("${netty.tcp.bossThreads:1}")
    private int bossThreads;

    /** worker 线程数 */
    @Value("${netty.tcp.workerThreads:4}")
    private int workerThreads;

    /** 读空闲(秒)。超过则主动关闭 */
    @Value("${netty.tcp.readerIdleSeconds:120}")
    private int readerIdleSeconds;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel serverChannel;

    /** clientId -> Channel */
    private static final Map<String, Channel> CLIENTS = new ConcurrentHashMap<>();

    /** ChannelId -> clientId */
    private static final Map<String, String> CHANNEL_BINDINGS = new ConcurrentHashMap<>();

    @PostConstruct
    public void start() throws InterruptedException {
        if (!enable) {
            log.info("TCP LongConnection disabled (netty.tcp.enable=false)");
            return;
        }
        bossGroup = new NioEventLoopGroup(bossThreads);
        workerGroup = new NioEventLoopGroup(workerThreads);
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        // 基于换行分割帧，防止拆包/粘包（最大 8K 一行）
                        p.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                        p.addLast(new StringDecoder(CharsetUtil.UTF_8));
                        p.addLast(new StringEncoder(CharsetUtil.UTF_8));
                        p.addLast(new IdleStateHandler(readerIdleSeconds, 0, 0));
                        p.addLast(new TcpLongConnectionHandler());
                    }
                });
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(port)).sync();
        serverChannel = future.channel();
        log.info("TCP LongConnection Server started at port {}", port);
    }

    @PreDestroy
    public void stop() {
        try {
            if (serverChannel != null) {
                serverChannel.close().syncUninterruptibly();
            }
        } finally {
            if (bossGroup != null) {
                bossGroup.shutdownGracefully();
            }
            if (workerGroup != null) {
                workerGroup.shutdownGracefully();
            }
            log.info("TCP LongConnection Server stopped");
        }
    }

    /** 向指定 client 发送 */
    public static boolean sendToClient(String clientId, String msg) {
        Channel ch = CLIENTS.get(clientId);
        if (ch == null || !ch.isActive()) {
            return false;
        }
        ch.writeAndFlush(msg + "\n");
        return true;
    }

    /** 广播 */
    public static int broadcast(String msg) {
        int count = 0;
        for (Channel ch : CLIENTS.values()) {
            if (ch.isActive()) {
                ch.writeAndFlush(msg + "\n");
                count++;
            }
        }
        return count;
    }

    /** 获取当前在线数量 */
    public static int onlineSize() {
        return CLIENTS.size();
    }

    /** Netty 处理器 */
    private static class TcpLongConnectionHandler extends SimpleChannelInboundHandler<String> {

        @Override
        public void channelInactive(ChannelHandlerContext ctx) {
            unbind(ctx);
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) {
            String trim = msg.trim();
            if (trim.isEmpty()) {
                return;
            }
            // 认证
            if (trim.startsWith("AUTH:")) {
                String clientId = trim.substring(5).trim();
                if (clientId.isEmpty()) {
                    ctx.writeAndFlush("ERR:EMPTY_CLIENT_ID\n");
                    return;
                }
                bind(clientId, ctx);
                ctx.writeAndFlush("AUTH_OK\n");
                return;
            }
            // 心跳
            if ("PING".equalsIgnoreCase(trim)) {
                ctx.writeAndFlush("PONG\n");
                return;
            }
            // 未认证直接断开
            if (!CHANNEL_BINDINGS.containsKey(ctx.channel().id().asShortText())) {
                ctx.writeAndFlush("ERR:UNAUTH\n").addListener(ChannelFutureListener.CLOSE);
                return;
            }
            // 普通消息：回显
            ctx.writeAndFlush("ECHO:" + trim + "\n");
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent idle) {
                if (idle.state() == IdleState.READER_IDLE) {
                    log.debug("Close idle connection: {}", ctx.channel().remoteAddress());
                    ctx.writeAndFlush("ERR:IDLE_TIMEOUT\n").addListener(ChannelFutureListener.CLOSE);
                }
            } else {
                super.userEventTriggered(ctx, evt);
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            log.debug("TCP connection error: {}", cause.getMessage());
            ctx.close();
        }

        private void bind(String clientId, ChannelHandlerContext ctx) {
            // 如果已存在旧连接，关闭旧连接
            Channel old = CLIENTS.put(clientId, ctx.channel());
            if (old != null && old != ctx.channel()) {
                old.writeAndFlush("ERR:KICKED\n").addListener(ChannelFutureListener.CLOSE);
            }
            CHANNEL_BINDINGS.put(ctx.channel().id().asShortText(), clientId);
            log.info("Client authenticated, clientId={}, remote={}", clientId, ctx.channel().remoteAddress());
        }

        private void unbind(ChannelHandlerContext ctx) {
            String channelId = ctx.channel().id().asShortText();
            String clientId = CHANNEL_BINDINGS.remove(channelId);
            if (clientId != null) {
                CLIENTS.remove(clientId, ctx.channel());
                log.info("Client disconnected, clientId={}, remote={}", clientId, ctx.channel().remoteAddress());
            }
        }
    }
}
