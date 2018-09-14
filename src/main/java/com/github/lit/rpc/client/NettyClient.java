package com.github.lit.rpc.client;

import com.github.lit.rpc.protocol.LitRequest;
import com.github.lit.rpc.protocol.LitResponse;
import com.github.lit.rpc.protocol.ProtostuffDecoder;
import com.github.lit.rpc.protocol.ProtostuffEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author liulu
 * @version : v1.0
 * date : 2018-09-14 19:35
 */
public class NettyClient extends SimpleChannelInboundHandler<LitResponse> {

    private final Object obj = new Object();

    private String host;

    private int port;

    private LitResponse response;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LitResponse msg) throws Exception {
        this.response = msg;
        synchronized (obj) {
            obj.notifyAll();
        }
    }


    public LitResponse send(LitRequest request) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ProtostuffDecoder(LitResponse.class));
                            ch.pipeline().addLast(NettyClient.this);
                            ch.pipeline().addLast(new ProtostuffEncoder(LitRequest.class));
                        }
                    });
            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().writeAndFlush(request).sync();

            synchronized (obj) {
                obj.wait(); // 未收到响应，使线程等待
            }

            if (response != null) {
                future.channel().closeFuture();
            }
            return response;
        } catch (Exception ignored) {
            //
            return null;
        } finally {

            workerGroup.shutdownGracefully();
        }

    }


}
