package com.ruoyi.netty.websocket.nettyServer;

import java.util.Map;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class NettyWebSocketEndpointHandler {

   private Map<String, String> pathParam;

   private Map<String, String> urlParam;

   public static void sendMsg(ChannelHandlerContext context, String msg) {
      TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(msg);
      context.channel().writeAndFlush(textWebSocketFrame);
   }

   public abstract void onMessage(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame);

   public abstract void onOpen(ChannelHandlerContext channelHandlerContext, FullHttpMessage fullHttpMessage);

   public abstract void onClose(ChannelHandlerContext channelHandlerContext);

   public abstract void onError(ChannelHandlerContext channelHandlerContext, Throwable throwable);

   public String getPathParam(String key) {
      return pathParam.get(key);
   }

   public void closeChannel(ChannelHandlerContext channelHandlerContext) {
      channelHandlerContext.close().addListener(ChannelFutureListener.CLOSE);
   }
}
