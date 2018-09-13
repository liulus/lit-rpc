package com.github.lit.rpc.server;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liulu
 * @version : v1.0
 * date : 2018-09-13 17:58
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(LitRpcServiceRegister.class)
public @interface LitRpcServer {

    int port() default 4796;

    String[] servicePackages() default {};

    Class[] serviceInterfaces() default {};

}
