package com.ruoyi.netty.websocket.endpoints;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.ruoyi.netty.websocket.annotations.NettyWebSocketEndpoint;
import com.ruoyi.netty.websocket.nettyServer.NettyWebSocketEndpointHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

@Component
@NettyWebSocketEndpoint(path = "/test/{seqNumber}")
public class TestNettyWebSocket extends NettyWebSocketEndpointHandler {

   private static final Map<String, ChannelHandlerContext> map = new ConcurrentHashMap<>();

   @Override
   public void onMessage(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) {
      System.out.println(textWebSocketFrame.text());
   }

   @Override
   public void onOpen(ChannelHandlerContext channelHandlerContext, FullHttpMessage fullHttpMessage) {
      map.put(getPathParam("seqNumber"), channelHandlerContext);
   }

   @Override
   public void onClose(ChannelHandlerContext channelHandlerContext) {
      map.remove(getPathParam("seqNumber"));
   }

   @Override
   public void onError(ChannelHandlerContext channelHandlerContext, Throwable throwable) {

   }

   public static void send(String seqNumber, String msg) {
      sendMsg(map.get(seqNumber), msg);
   }
}
