package com.github.lit.rpc.client;

import com.github.lit.rpc.protocol.LitRequest;
import com.github.lit.rpc.protocol.LitResponse;
import com.github.lit.rpc.util.ProtostuffSerializeUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author liulu
 * @version : v1.0
 * date : 2018-09-14 16:23
 */
@Slf4j
public class ClientInvokeProxy implements InvocationHandler {

    private Class<?> interfaceClass;

    private String host;

    private int port;

    public ClientInvokeProxy(Class<?> interfaceClass, String host, int port) {
        this.interfaceClass = interfaceClass;
        this.host = host;
        this.port = port;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 构建请求
        LitRequest request = new LitRequest();
        request.setClassName(interfaceClass.getName());
        request.setMethodName(method.getName());
        request.setParamTypes(method.getParameterTypes());
        request.setParamValues(Arrays.asList(args));

        NettyClient client = new NettyClient(host, port);
        LitResponse response = client.send(request);
        return ProtostuffSerializeUtil.deserialize(method.getReturnType(), response.getReturnValue());
    }
}
