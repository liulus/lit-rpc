package com.github.lit.rpc;

import com.github.lit.rpc.client.LitRpcClient;
import com.github.lit.rpc.service.HelloWorldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author liulu
 * @version : v1.0
 * date : 2018-09-10 20:37
 */
@Slf4j
public class ClientApplication {


    public static void main(String[] args) {


        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ClientConfig.class);

        HelloWorldService bean = applicationContext.getBean(HelloWorldService.class);
//        String cat = bean.sayHello("cat");

        HelloReq req = new HelloReq();
        req.setA("E");
        req.setB(123.94D);
        req.setC(23);

        HelloRes res = bean.req(req);


        System.out.println(res);

    }

    @LitRpcClient(port = 4001, servicePackages = "com.github.lit.rpc.service")
    static class ClientConfig {

    }


}
