package com.larcloud.server.media.RtspServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.rtsp.RtspRequestDecoder;
import io.netty.handler.codec.rtsp.RtspResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.ResourceLeakDetector;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class RtspServer {
    private Logger logger = Logger.getLogger(RtspServer.class.getName()+" [RtspServer]");

    private static final EventLoopGroup bossGroup = new NioEventLoopGroup(2);
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup(2);
    private Integer port=6018;
    private Channel serverChannel;

    @Autowired
    private InMultiplexerHandler inMultiplexerHandler;
    @Autowired
    private RtspRequestHandler rtspRequestHandler;

	@PostConstruct
	public void start() throws Exception {
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(final SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(inMultiplexerHandler);
                    pipeline.addLast("decoder", new RtspRequestDecoder());
                    pipeline.addLast("encoder", new RtspResponseEncoder());
                    pipeline.addLast("handler", rtspRequestHandler);
                }
            });
        //b.option(ChannelOption.SO_KEEPALIVE,true);
		serverChannel = b.bind(port).await().channel();//.sync().channel().closeFuture().sync()
        logger.info("Starting RtspServer at port: " + port);
	}

	@PreDestroy
	public void stop() {
		serverChannel.close();
        logger.warn("RtspServer stopped.");
	}
}
