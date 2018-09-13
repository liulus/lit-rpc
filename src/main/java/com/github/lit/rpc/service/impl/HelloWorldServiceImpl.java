package com.github.lit.rpc.service.impl;

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
}
