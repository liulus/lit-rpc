package com.github.lit.rpc.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liulu
 * @version : v1.0
 * date : 2018-09-13 18:05
 */
public class LitRpcContext {

    private static final Map<Class<?>, Object> OBJECT_MAP = new ConcurrentHashMap<>();


    public static Object get(Class<?> key) {
        return OBJECT_MAP.get(key);
    }

    public static void put(Class<?> key, Object value) {
        OBJECT_MAP.put(key, value);
    }


}
