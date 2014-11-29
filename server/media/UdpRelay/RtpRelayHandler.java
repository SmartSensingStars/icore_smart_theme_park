package com.larcloud.server.media.UdpRelay;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.larcloud.server.media.RtspServer.RtspSession;
import com.larcloud.service.exception.BaseException;
import com.larcloud.util.StringUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;

import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: ycd
 * Date: 14-7-1
 */
public class RtpRelayHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private Logger logger = Logger.getLogger(RtpRelayHandler.class.getName()+" [RtpRelayServer]");
    //public static ConcurrentHashMap<String, InetSocketAddress> relayPair = new ConcurrentHashMap<String, InetSocketAddress>();
    //public static ConcurrentHashMap<String, RtspSession> relayPairTcp = new ConcurrentHashMap<String, RtspSession>();
    public static ConcurrentHashMap<Integer, RtspSession> relayPairTcp = new ConcurrentHashMap<Integer, RtspSession>();
    private ChannelHandlerContext context;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("rtp relay channel is active");
        this.context=ctx;
        super.channelActive(ctx);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet)  throws Exception {
        ByteBuf rtpPacketBuf = packet.content();
        byte[] rtpPacketBytes=new byte[12];
        rtpPacketBuf.getBytes(0,rtpPacketBytes,0,12);
        RtspSession rtspSession= relayPairTcp.get(toInt(rtpPacketBytes[8],rtpPacketBytes[9],
                rtpPacketBytes[10],rtpPacketBytes[11]));
        if(rtspSession!=null){
            rtspSession.sendRtp(packet.content());
        }
        else {
            throw new BaseException(BaseException.ENTITY_DOES_NOT_EXIST_ERROR);
        }


    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error(StringUtil.getFullStackTrace(cause));
    }
    private int toInt(byte b1, byte b2, byte b3, byte b4) {

        int i1 = b1 < 0 ? (256 + b1) : b1;
        int i2 = b2 < 0 ? (256 + b2) : b2;
        int i3 = b3 < 0 ? (256 + b3) : b3;
        int i4 = b4 < 0 ? (256 + b4) : b4;
        return (i1 << 24) + (i2 << 16) + (i3 << 8) + i4;
    }

}