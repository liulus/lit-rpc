package com.github.lit.rpc.service.impl;

import com.github.lit.rpc.HelloReq;
import com.github.lit.rpc.HelloRes;
import com.github.lit.rpc.service.HelloWorldService;
import org.springframework.stereotype.Service;

/**
 * @author liulu
 * @version : v1.0
 * date : 2018-09-13 20:29
 */
@Service
public class HelloWorldServiceImpl implements HelloWorldService {

    @Override
    public String sayHello(String name) {
        System.out.println("hello: " + name);
        return "hello: " + name;
    }

    @Override
    public HelloRes req(HelloReq helloReq) {

        System.out.println(helloReq);

        HelloRes res = new HelloRes();
        res.setA("A");
        res.setB(12.4D);
        res.setC(12);

        return res;
    }
}
