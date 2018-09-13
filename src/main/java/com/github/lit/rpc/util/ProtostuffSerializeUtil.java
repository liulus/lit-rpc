package com.github.lit.rpc.util;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liulu
 * @version : v1.0
 * date : 2018-09-13 16:57
 */
public class ProtostuffSerializeUtil {

    private static final Map<Class<?>, Schema<?>> SCHEMA_MAP = new ConcurrentHashMap<>(64);


    public static <T> Schema<T> getSchema(Class<T> clazz) {
        //noinspection unchecked
        return (Schema<T>) SCHEMA_MAP.computeIfAbsent(clazz, RuntimeSchema::createFrom);
    }


    public static byte[] serialize(Object obj) {
        if (obj == null) {
            return new byte[0];
        }
        Schema schema = getSchema(obj.getClass());
        return ProtostuffIOUtil.toByteArray(obj, schema, LinkedBuffer.allocate());
    }

    public static <T> T deserialize(Class<T> cls, byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        Schema<T> schema = getSchema(cls);
        T msg = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(data, msg, schema);
        return msg;
    }

}
