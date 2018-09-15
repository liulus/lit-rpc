package com.github.lit.rpc.service;

import com.github.lit.rpc.HelloReq;
import com.github.lit.rpc.HelloRes;

/**
 * @author liulu
 * @version : v1.0
 * date : 2018-09-13 20:28
 */
public interface HelloWorldService {

    String sayHello(String name);


    HelloRes req (HelloReq helloReq);
}
