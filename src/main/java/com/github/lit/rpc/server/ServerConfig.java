package com.github.lit.rpc.server;

import lombok.Data;

/**
 * @author liulu
 * @version : v1.0
 * date : 2018-09-13 17:47
 */
@Data
public class ServerConfig {

    /**
     * 服务端默认端口号
     */
    private Integer port = 4796;

    private String[] basePackages;


}
