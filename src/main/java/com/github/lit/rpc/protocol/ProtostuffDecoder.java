package com.github.lit.rpc.protocol;

import com.github.lit.rpc.util.ProtostuffSerializeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;

import java.util.List;

/**
 * @author liulu
 * @version : v1.0
 * date : 2018-09-13 11:48
 */
public class ProtostuffDecoder extends MessageToMessageDecoder<ByteBuf> {

    private Schema schema;

    public ProtostuffDecoder(Class<?> schemaClass) {
        schema = ProtostuffSerializeUtil.getSchema(schemaClass);
    }


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        final byte[] array;
        final int offset;
        final int length = in.readableBytes();
        if (in.hasArray()) {
            array = in.array();
            offset = in.arrayOffset() + in.readerIndex();
        } else {
            array = new byte[length];
            in.getBytes(in.readerIndex(), array, 0, length);
            offset = 0;
        }
        Object msg = schema.newMessage();
        //noinspection unchecked
        ProtostuffIOUtil.mergeFrom(array, offset, length, msg, schema);

        out.add(msg);
    }
}
