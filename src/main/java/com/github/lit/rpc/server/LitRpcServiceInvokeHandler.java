package com.github.lit.rpc.server;

import com.github.lit.rpc.protocol.LitRequest;
import com.github.lit.rpc.protocol.LitResponse;
import com.github.lit.rpc.util.ProtostuffSerializeUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * @author liulu
 * @version : v1.0
 * date : 2018-09-13 19:31
 */
@Slf4j
public class LitRpcServiceInvokeHandler extends ChannelInboundHandlerAdapter {

    private static final AttributeKey<LitRequest> REQUEST = AttributeKey.valueOf("request");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof LitRequest) {
            ctx.channel().attr(REQUEST).set((LitRequest) msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        LitRequest request = ctx.channel().attr(REQUEST).get();
        LitResponse response = invoke(request);
        ctx.channel().writeAndFlush(response);
    }

    @SneakyThrows
    private LitResponse invoke(LitRequest request) {

        Class<?> cls = ClassUtils.forName(request.getClassName(), null);
        Object obj = LitRpcContext.get(cls);
        Method method = ReflectionUtils.findMethod(cls, request.getMethodName(), request.getParamTypes());
        if (method == null) {
            log.warn("未能找到 类 {} 的方法 {}", request.getClassName(), request.getMethodName());
            return null;
        }
        Object retValue = ReflectionUtils.invokeMethod(method, obj, request.getParamValues().toArray());

        LitResponse response = new LitResponse();
        response.setReturnType(method.getReturnType().getName());
        response.setReturnValue(ProtostuffSerializeUtil.serialize(retValue));
        return response;
    }




}
