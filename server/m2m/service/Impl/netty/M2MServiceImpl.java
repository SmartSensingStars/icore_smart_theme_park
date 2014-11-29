package com.larcloud.server.m2m.service.Impl.netty;

import com.larcloud.server.m2m.message.M2MMessage;
import com.larcloud.server.m2m.service.IM2MService;
//import com.larcloud.work.nwwork.M2MObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * A UDP server
 *
 *
 *
 */
@Service("M2MServiceImpl")
public class M2MServiceImpl implements IM2MService {
    //@@@{'accessKey':'xxx','serialNo':'c01a626039469375','baseUrl':'user/login','data':{'username':'test','password':'test'}}
    private Logger logger = Logger.getLogger(M2MServiceImpl.class.getName());

    @Autowired
    private NioEventLoopGroup workerGroup;

    private String serviceHost="127.0.0.1";
    private Integer servicePort=8080;
    private Integer port=6011;
    private Channel serverChannel;
    private UdpServerHandler udpServerHandler;

    public M2MServiceImpl() {}


    @PostConstruct
    public void start() throws Exception {
        Bootstrap b = new Bootstrap();
        udpServerHandler = new UdpServerHandler(serviceHost,servicePort);
        b.group(workerGroup).channel(NioDatagramChannel.class).handler(udpServerHandler);
        //b.bind(port).sync().channel().closeFuture().await();
        serverChannel = b.bind(port).await().channel();
        logger.info("M2M netty server started at port: " + port + serverChannel.toString());
    }

    @PreDestroy
    public void stop() {
        serverChannel.close();
        logger.warn("M2M netty server stopped.");
    }

    public NioEventLoopGroup getWorkerGroup() {
        return workerGroup;
    }

    public void setWorkerGroup(NioEventLoopGroup workerGroup) {
        this.workerGroup = workerGroup;
    }

    public String getServiceHost() {
        return serviceHost;
    }

    public void setServiceHost(String serviceHost) {
        this.serviceHost = serviceHost;
    }

    public Integer getServicePort() {
        return servicePort;
    }

    public void setServicePort(Integer servicePort) {
        this.servicePort = servicePort;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }


    @Override
    public void receive(M2MMessage m2mMessage) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void sendThrough(M2MMessage m2MMessage) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void send(M2MMessage m2mMessage) throws Exception {
        udpServerHandler.send(m2mMessage);
    }

    @Override
    public void sendAckOk(M2MMessage m2mMessage) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void sendAckError(M2MMessage m2mMessage, String errorString) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

//    @Override
//    public void dispatchGlodioM2M(M2MObject mo) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }

    @Override
    public void login(M2MMessage m2mMessage) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void logout(M2MMessage m2mMessage) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}