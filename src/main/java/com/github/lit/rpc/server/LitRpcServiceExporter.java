package com.github.lit.rpc.server;

import com.github.lit.rpc.protocol.LitRequest;
import com.github.lit.rpc.protocol.LitResponse;
import com.github.lit.rpc.protocol.ProtostuffDecoder;
import com.github.lit.rpc.protocol.ProtostuffEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author liulu
 * @version : v1.0
 * date : 2018-09-13 18:39
 */
@Slf4j
public class LitRpcServiceExporter {


    public void startServer(int port) {
        // 监听访问
        log.info("开始启动 netty 服务");
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        NioEventLoopGroup bossLoopGroup = new NioEventLoopGroup(1);
        int cpuNum = Runtime.getRuntime().availableProcessors();
        NioEventLoopGroup workLoopGroup = new NioEventLoopGroup(cpuNum * 2);

        serverBootstrap.group(bossLoopGroup, workLoopGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);

        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ProtostuffDecoder(LitRequest.class));
                ch.pipeline().addLast(new LitRpcServiceInvokeHandler());
                ch.pipeline().addLast(new ProtostuffEncoder(LitResponse.class));
            }
        });

        // --设置netty监听的ip和端口
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        serverBootstrap.bind(port);

        log.info("netty 服务在端口: {} 启动完成", port);
    }


}
