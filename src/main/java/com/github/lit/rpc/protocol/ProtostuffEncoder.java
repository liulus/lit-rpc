package com.github.lit.rpc.protocol;

import com.github.lit.rpc.util.ProtostuffSerializeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;

/**
 * @author liulu
 * @version : v1.0
 * date : 2018-09-13 11:48
 */
public class ProtostuffEncoder extends MessageToByteEncoder {

    private Schema schema;

    public ProtostuffEncoder(Class<?> schemaClass) {
        schema = ProtostuffSerializeUtil.getSchema(schemaClass);
    }


    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) {
        //noinspection unchecked
        byte[] bytes = ProtostuffIOUtil.toByteArray(msg, schema, LinkedBuffer.allocate());
        out.writeBytes(bytes);
    }
}
