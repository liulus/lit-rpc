package com.github.lit.rpc;

import com.github.lit.rpc.protocol.LitRequest;
import com.github.lit.rpc.protocol.LitResponse;
import com.github.lit.rpc.protocol.ProtostuffDecoder;
import com.github.lit.rpc.protocol.ProtostuffEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liulu
 * @version : v1.0
 * date : 2018-09-10 20:37
 */
@Slf4j
public class ClientApplication {


    public static void main(String[] args) throws InterruptedException {

        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ProtostuffDecoder(LitResponse.class));
                ch.pipeline().addLast(new NettyClientHandler());
                ch.pipeline().addLast(new ProtostuffEncoder(LitRequest.class));
            }
        });

        ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(4001)).sync();

        channelFuture.channel().closeFuture().sync();

    }

    static class NettyClientHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            log.info("channelActive");

            LitRequest request = new LitRequest();

            request.setClassName("com.github.lit.rpc.service.HelloWorldService");
            request.setMethodName("sayHello");

            List<Object> args = new ArrayList<>();
            args.add("cat");

            request.setParamTypes(new Class[]{String.class});
            request.setParamValues(args);

            ctx.channel().writeAndFlush(request);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            log.info("channelRead {}", msg);
            if (msg instanceof LitResponse) {
                log.info("sdsd {}", msg);
            }
        }
    }



}
