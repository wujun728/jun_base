package com.ruoyi.netty.websocket.nettyServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ruoyi.netty.websocket.nettyServer.handler.WebSocketHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

@Component
public class NettyWebSocketServer {

   private static final Logger log = LoggerFactory.getLogger(NettyWebSocketServer.class);
   private ServerBootstrap serverBootstrap;
   private Channel serverChannel;

   @Value("${netty.websocket.maxMessageSize}")
   private Long messageSize;

   @Value("${netty.websocket.bossThreads}")
   private Long bossThreads;

   @Value("${netty.websocket.workerThreads}")
   private Long workerThreads;

   @Value("${netty.websocket.port}")
   private Long port;

   @Value("${netty.websocket.enable}")
   private Boolean enable;

   public ServerBootstrap start() throws InterruptedException {
      if (!enable) {
         return null;
      }
      ServerBootstrap serverBootstrap = new ServerBootstrap();
      NioEventLoopGroup boss = new NioEventLoopGroup(4);
      NioEventLoopGroup worker = new NioEventLoopGroup(workerThreads.intValue());
      serverBootstrap.group(boss, worker);
      serverBootstrap.channel(NioServerSocketChannel.class);
      serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
      serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
         @Override
         protected void initChannel(NioSocketChannel channel) throws Exception {
            ChannelPipeline pipeline = channel.pipeline();
            pipeline.addLast(new HttpServerCodec());
            pipeline.addLast(new HttpObjectAggregator(messageSize.intValue()));
            pipeline.addLast(new WebSocketHandler());
            pipeline.addLast(new WebSocketServerProtocolHandler("/", true));
         }
      });
      ChannelFuture future = serverBootstrap.bind(port.intValue()).sync();
      serverChannel = future.channel();
      log.info("netty for websocket start success, running in port: {}", this.port);
      this.serverBootstrap = serverBootstrap;
      return this.serverBootstrap;
   }

   public void shutdown() {
      if (serverChannel != null) {
         serverChannel.close().syncUninterruptibly();
      }
      if (serverBootstrap != null) {
         EventLoopGroup bossGroup = serverBootstrap.config().group();
         EventLoopGroup workerGroup = serverBootstrap.config().childGroup();
         if (bossGroup != null) {
            bossGroup.shutdownGracefully().syncUninterruptibly();
         }
         if (workerGroup != null) {
            workerGroup.shutdownGracefully().syncUninterruptibly();
         }
      }
      log.info("netty for websocket shudown success");
   }
}
