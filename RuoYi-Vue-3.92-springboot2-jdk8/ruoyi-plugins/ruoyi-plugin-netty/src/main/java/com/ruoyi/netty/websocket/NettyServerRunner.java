package com.ruoyi.netty.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.ruoyi.netty.websocket.nettyServer.NettyWebSocketServer;

import jakarta.annotation.PreDestroy;

@Component
public class NettyServerRunner implements ApplicationRunner {

   @Autowired
   private NettyWebSocketServer server;

   @Override
   public void run(ApplicationArguments args) throws Exception {
      server.start();
   }

   @PreDestroy
   public void destroy() {
      server.shutdown();
   }

}
