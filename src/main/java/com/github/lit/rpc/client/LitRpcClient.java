package com.github.lit.rpc.client;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liulu
 * @version : v1.0
 * date : 2018-09-13 20:41
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LitRpcClient {

    String host() default "127.0.0.1";

    int port() default 4796;

    String[] servicePackages() default {};

    Class[] serviceInterfaces() default {};
}
