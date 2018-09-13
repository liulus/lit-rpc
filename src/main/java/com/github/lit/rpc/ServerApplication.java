package com.github.lit.rpc;

import com.github.lit.rpc.server.LitRpcServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author liulu
 * @version : v1.0
 * date : 2018-09-10 20:15
 */
@Slf4j
public class ServerApplication {



    public static void main(String[] args) throws Exception {
        new AnnotationConfigApplicationContext(ServerConfig.class);
    }

    @LitRpcServer(port = 4001, servicePackages = "com.github.lit.rpc.service")
    @ComponentScan(basePackages = "com.github.lit.rpc")
    static class ServerConfig {

    }



}
