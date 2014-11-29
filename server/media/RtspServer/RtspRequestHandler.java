package com.larcloud.server.media.RtspServer;

/**
 * Created with IntelliJ IDEA.
 * User: ycd
 * Date: 14-7-1
 */

import com.larcloud.component.ITerminalComponent;
import com.larcloud.server.m2m.message.M2MDataJsonMessage;
import com.larcloud.server.m2m.message.M2MMessage;
import com.larcloud.server.m2m.service.IM2MService;
import com.larcloud.server.media.RtspServer.message.RtspMessage;
import com.larcloud.server.media.RtspServer.message.SessionDescriptor;
import com.larcloud.util.ParamMap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.rtsp.RtspHeaders;
import io.netty.handler.codec.rtsp.RtspMethods;
import io.netty.handler.codec.rtsp.RtspResponseStatuses;
import io.netty.handler.codec.rtsp.RtspVersions;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ChannelHandler.Sharable
public class RtspRequestHandler extends ChannelInboundHandlerAdapter {
    private Logger logger = Logger.getLogger(RtspRequestHandler.class.getName() + " [RtspRequestHandler]");

    private static ConcurrentHashMap<ChannelHandlerContext, RtspSession> rtspSessions = new ConcurrentHashMap<ChannelHandlerContext, RtspSession>();
    private static ConcurrentHashMap<String, OnRtspMessageListener> onRtspMessageListeners = new ConcurrentHashMap<String, OnRtspMessageListener>();
    @Resource(name = "M2MServiceImpl")
    IM2MService m2mServiceImpl;

    @Autowired
    ITerminalComponent terminalCom;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        logger.debug("msg 接收到客户端请求");
        if (msg instanceof HttpRequest) {
            HttpRequest rtspMessage = (HttpRequest) msg;
            logger.debug(rtspMessage.toString());
            if (rtspSessions.get(ctx) != null) {
                rtspSessions.get(ctx).processRtspRequest(rtspMessage);
            }
        } else {
            logger.debug("RTSP ACK");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.debug("exceptionCaught:" + cause.toString());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        RtspSession rtspSession = new RtspSession(ctx,this);
        rtspSession.setM2mService(m2mServiceImpl);
        rtspSession.setTerminalCom(terminalCom);
        rtspSessions.put(ctx, rtspSession);

        logger.debug("channelActive" + ctx.pipeline().channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("channelInactive");
        if (rtspSessions.get(ctx) != null) {
            rtspSessions.get(ctx).processDisconnect();
            rtspSessions.remove(ctx);
        }
        super.channelInactive(ctx);
    }

    public static void OnRtspMessageArrived(M2MDataJsonMessage m2MDataJsonMessage) {
        ParamMap params = m2MDataJsonMessage.getData();
        if((params.getString("serialNo")!=null)&&(!params.getString("serialNo").equals(""))){
            OnRtspMessageListener rtspMessageListener = onRtspMessageListeners.get(params.getString("serialNo"));
            if(rtspMessageListener != null){
            switch (params.getInteger(RtspMessage.TYPE)){
                case RtspMessage.TYPE_RTSP_DESCRIPTOR_RESPONSE:
                    SessionDescriptor sessionDescriptor=new SessionDescriptor();
                    sessionDescriptor.setProfile(params.getString("profile"));
                    sessionDescriptor.setSps(params.getString("sps"));
                    sessionDescriptor.setPps(params.getString("pps"));
                    rtspMessageListener.OnDescribeArrived(sessionDescriptor,params.getString("cseq"));
                    break;
                case RtspMessage.TYPE_RTSP_SETUP_RESPONSE:
                    rtspMessageListener.OnSetupArrived(params.getInteger("ssrc"),params.getString("cseq"));
                    break;
                case RtspMessage.TYPE_RTSP_PLAY_RESPONSE:
                    rtspMessageListener.OnPlayArrived(params.getString("cseq"));
                    break;
                case RtspMessage.TYPE_RTSP_TEARDOWN_RESPONSE:
                    rtspMessageListener.OnTeardownArrived(params.getString("cseq"));
                    break;
                case RtspMessage.TYPE_RTSP_DISCONNECT_RESPONSE:
                    break;
            }
            }
        }
    }

    public void addRtspMessageListener(String serialNo, OnRtspMessageListener onRtspMessageListener) {
        onRtspMessageListeners.put(serialNo,onRtspMessageListener);
    }
}
