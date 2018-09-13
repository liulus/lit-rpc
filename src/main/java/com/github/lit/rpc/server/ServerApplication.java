package com.github.lit.rpc.server;

/**
 * @author liulu
 * @version : v1.0
 * date : 2018-09-13 17:46
 */
public class ServerApplication {


    public static void run(String basePackage) {
        ServerConfig config = new ServerConfig();
        config.setBasePackages(new String[]{basePackage});
        run(config);
    }


    public static void run(ServerConfig config) {


    }


}
