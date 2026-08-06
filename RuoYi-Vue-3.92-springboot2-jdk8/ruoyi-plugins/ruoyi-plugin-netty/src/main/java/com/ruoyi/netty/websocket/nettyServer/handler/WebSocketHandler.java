package com.ruoyi.netty.websocket.nettyServer.handler;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.netty.websocket.annotations.NettyWebSocketEndpoint;
import com.ruoyi.netty.websocket.nettyServer.NettyWebSocketEndpointHandler;
import com.ruoyi.netty.websocket.utils.CommonUtil;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import jakarta.annotation.PostConstruct;

@Component
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

   @Autowired
   private List<NettyWebSocketEndpointHandler> handlers;

   private static final Map<String, PathMatchModel> uriHandlerMapper = new ConcurrentHashMap<>();

   public static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

   public static final Map<ChannelId, NettyWebSocketEndpointHandler> channelHandlerMap = new ConcurrentHashMap<>();

   @PostConstruct
   private void init() throws URISyntaxException, NoSuchMethodException, SecurityException {
      for (NettyWebSocketEndpointHandler handler : handlers) {
         Class<?> handlerClass = handler.getClass();
         NettyWebSocketEndpoint annotation = handlerClass.getAnnotation(NettyWebSocketEndpoint.class);
         if (annotation == null || StringUtils.isEmpty(annotation.path())) {
            throw new RuntimeException("未配置路径的 netty websocket endpoint ");
         }
         PathMatchModel pathMachModel = parseHandler(annotation.path(), handlerClass);
         uriHandlerMapper.put(pathMachModel.path, pathMachModel);
      }
   }

   @Override
   protected void channelRead0(ChannelHandlerContext context, TextWebSocketFrame webSocketFrame) throws Exception {
      NettyWebSocketEndpointHandler handler = channelHandlerMap.get(context.channel().id());
      handler.onMessage(context, webSocketFrame);
   }

   @Override
   public void channelActive(ChannelHandlerContext ctx) throws Exception {
      channelGroup.add(ctx.channel());
   }

   @Override
   public void channelInactive(ChannelHandlerContext ctx) throws Exception {
      NettyWebSocketEndpointHandler handler = channelHandlerMap.get(ctx.channel().id());
      if (handler != null) {
         handler.onClose(ctx);
      }
      channelHandlerMap.remove(ctx.channel().id());
      channelGroup.remove(ctx.channel());
   }

   @Override
   public void channelRegistered(ChannelHandlerContext ctx) throws Exception {

   }

   @Override
   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      channelHandlerMap.get(ctx.channel().id()).onError(ctx, cause);
   }

   @Override
   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      if (msg instanceof FullHttpRequest) {
         FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
         if (channelHandlerMap.get(ctx.channel().id()) != null) {
            super.channelRead(ctx, fullHttpRequest);
            return;
         }
         URI uri = new URI(fullHttpRequest.uri());
         PathMatchModel mathPathMachModel = mathPathMachModel(uri.getPath());
         if (mathPathMachModel == null) {
            ctx.channel()
                  .writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND));
            ctx.close().addListener(ChannelFutureListener.CLOSE);
            return;
         }
         Constructor<?> constructor = mathPathMachModel.handlerConstructor;
         NettyWebSocketEndpointHandler newInstance = (NettyWebSocketEndpointHandler) constructor.newInstance();
         if (!(mathPathMachModel.pathParams == null || mathPathMachModel.pathParams.isEmpty())) {
            newInstance.setPathParam(
                  CommonUtil.parsePathParam(uri.getPath(), mathPathMachModel.pathParams, mathPathMachModel.path));
            super.channelRead(ctx, msg);
         }
         newInstance.setUrlParam(CommonUtil.parseQueryParameters(uri.getQuery()));
         channelHandlerMap.put(ctx.channel().id(), newInstance);
         newInstance.onOpen(ctx, fullHttpRequest);
      } else if (msg instanceof TextWebSocketFrame) {
         super.channelRead(ctx, msg);
      }

   }

   private static PathMatchModel parseHandler(String path, Class<?> handlerClass)
         throws NoSuchMethodException, SecurityException {
      List<String> paramName = new ArrayList<>();
      String[] split = path.split("/");
      for (int index = 1; index < split.length; index++) {
         String item = split[index];
         if (item.startsWith("{") && item.endsWith("}")) {
            paramName.add(item.substring(1, item.length() - 1).trim());
            split[index] = "?";
         }
      }
      StringBuilder finalPath = new StringBuilder("");
      for (int index = 1; index < split.length; index++) {
         finalPath.append("/").append(split[index]);
      }
      return new PathMatchModel(paramName, finalPath.toString(), handlerClass.getDeclaredConstructor());
   }

   private static PathMatchModel mathPathMachModel(String uri) {
      Map<Integer, PathMatchModel> map = new HashMap<>();
      for (String key : uriHandlerMapper.keySet()) {
         int mathUri = CommonUtil.mathUri(uri, key);
         if (mathUri > 0) {
            map.put(mathUri, uriHandlerMapper.get(key));
         }
      }
      if (map.keySet() == null || map.keySet().isEmpty()) {
         return null;
      }
      Integer max = CommonUtil.getMax(map.keySet());
      return map.get(max);
   }

   private static final class PathMatchModel {
      private final List<String> pathParams;

      private final String path;

      private final Constructor<?> handlerConstructor;

      public PathMatchModel(List<String> pathParams, String path, Constructor<?> handlerConstructor) {
         this.pathParams = pathParams;
         this.path = path;
         this.handlerConstructor = handlerConstructor;
      }

   }
}
