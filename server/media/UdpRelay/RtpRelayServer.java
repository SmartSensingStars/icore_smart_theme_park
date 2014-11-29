package com.larcloud.server.media.UdpRelay;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
/**
 * Created with IntelliJ IDEA.
 * User: ycd
 * Date: 14-7-1
 */
@Service("RtpRelayServer")
public class RtpRelayServer {
    //@@@{'accessKey':'xxx','serialNo':'c01a626039469375','baseUrl':'user/login','data':{'username':'test','password':'test'}}
    private Logger logger = Logger.getLogger(RtpRelayServer.class.getName()+" [RtpRelayServer]");
    private NioEventLoopGroup workerGroup;
    private Integer port=6015;
    private Channel serverChannel;
    private RtpRelayHandler rtpRelayHandler;


    public RtpRelayServer() {}

    @PostConstruct
    public void start() throws Exception {
        Bootstrap b = new Bootstrap();
        rtpRelayHandler= new RtpRelayHandler();
        b.group(workerGroup).channel(NioDatagramChannel.class).handler(rtpRelayHandler);
        //b.bind(port).sync().channel().closeFuture().await();
        serverChannel = b.bind(port).await().channel();
        logger.info("RtpRelayServer started at port: " + port + serverChannel.toString());
    }

    @PreDestroy
    public void stop() {
        serverChannel.close();
        logger.warn("RtpRelayServer stopped.");
    }

    public NioEventLoopGroup getWorkerGroup() {
        return workerGroup;
    }

    public void setWorkerGroup(NioEventLoopGroup workerGroup) {
        this.workerGroup = workerGroup;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }


}