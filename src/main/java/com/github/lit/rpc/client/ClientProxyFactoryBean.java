package com.github.lit.rpc.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * @author liulu
 * @version : v1.0
 * date : 2018-09-14 16:26
 */
@Getter
@Setter
public class ClientProxyFactoryBean implements FactoryBean<Object> {

    private Class<?> interfaceClass;

    private String host;

    private int port;

    @Override
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new ClientInvokeProxy(interfaceClass, host, port));
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

}
