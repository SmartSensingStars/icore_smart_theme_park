package com.larcloud.server.media.RtspServer;

import com.larcloud.component.ITerminalComponent;
import com.larcloud.server.m2m.message.M2MDataJsonMessage;
import com.larcloud.server.m2m.message.M2MMessage;
import com.larcloud.server.m2m.service.IM2MService;
import com.larcloud.server.media.RtspServer.message.RtspMessage;
import com.larcloud.server.media.RtspServer.message.SessionDescriptor;
import com.larcloud.server.media.UdpRelay.RtpRelayHandler;
import com.larcloud.util.ParamMap;
import com.larcloud.util.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.rtsp.RtspHeaders;
import io.netty.handler.codec.rtsp.RtspMethods;
import io.netty.handler.codec.rtsp.RtspResponseStatuses;
import io.netty.handler.codec.rtsp.RtspVersions;
import io.netty.util.CharsetUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.log4j.Logger;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: ycd
 * Date: 14-7-1
 */
public class RtspSession {
    private Logger logger = Logger.getLogger(RtspSession.class.getName()+" [RtspSession]");
    public static final String RTSP_SERVER = "RTSP Server";
    private String serialNo;
    private ChannelHandlerContext ctx;
    private RtspRequestHandler rtspRequestHandler;
    private SessionDescriptor sessionDescriptor;
    private String destinationIp;
    private long timestamp;
    private boolean isRtpOverTcp=false;

    private IM2MService m2mService;
    private ITerminalComponent terminalCom;

    private int txNo=0;

    public IM2MService getM2mService() {
        return m2mService;
    }

    public void setM2mService(IM2MService m2mService) {
        this.m2mService = m2mService;
    }

    public ITerminalComponent getTerminalCom() {
        return terminalCom;
    }

    public void setTerminalCom(ITerminalComponent terminalCom) {
        this.terminalCom = terminalCom;
    }

    RtspSession(ChannelHandlerContext ctx,RtspRequestHandler rtspRequestHandler){
        this.ctx=ctx;
        this.timestamp = System.currentTimeMillis();
        this.rtspRequestHandler = rtspRequestHandler;
    }

    public void processRtspRequest(HttpRequest request) {
        try {
            if (request.getMethod().equals(RtspMethods.OPTIONS)) {
                processOptions(request);
            } else if (request.getMethod().equals(RtspMethods.DESCRIBE)) {
                processDescribe(request);
            } else if (request.getMethod().equals(RtspMethods.SETUP)) {
                processSetup(request);
            } else if (request.getMethod().equals(RtspMethods.PLAY)) {
                processPlay(request);
            } else if (request.getMethod().equals(RtspMethods.PAUSE)) {
                processPause(request);
            } else if (request.getMethod().equals(RtspMethods.GET_PARAMETER)) {
                processGetParameter(request);
            } else if (request.getMethod().equals(RtspMethods.TEARDOWN)) {
                processTeardown(request);
            } else {
                HttpResponse response = new DefaultHttpResponse(RtspVersions.RTSP_1_0, RtspResponseStatuses.METHOD_NOT_ALLOWED);
                response.headers().set(HttpHeaders.Names.SERVER, RTSP_SERVER );
                response.headers().set(RtspHeaders.Names.CSEQ, request.headers().get(RtspHeaders.Names.CSEQ));
                response.headers().set(RtspHeaders.Names.ALLOW, "SETUP, PLAY");
                ctx.channel().writeAndFlush(response);
            }
        } catch (Exception e) {
            logger.error("Unexpected error during processing,Caused by ", e);
            HttpResponse response = new DefaultHttpResponse(RtspVersions.RTSP_1_0, RtspResponseStatuses.INTERNAL_SERVER_ERROR);
            response.headers().add(HttpHeaders.Names.SERVER, RTSP_SERVER );
            response.headers().add(RtspHeaders.Names.CSEQ, request.headers().get(RtspHeaders.Names.CSEQ));
            ctx.channel().writeAndFlush(response);
        }
    }
    private void processOptions(HttpRequest request){
       try{String OPTIONS = RtspMethods.DESCRIBE.name() + "," + RtspMethods.SETUP.name() + ","
                + RtspMethods.TEARDOWN.name() + "," + RtspMethods.PLAY.name();
        FullHttpResponse response  = new DefaultFullHttpResponse(RtspVersions.RTSP_1_0, RtspResponseStatuses.OK);
        response.headers().add(RtspHeaders.Names.CSEQ, request.headers().get(RtspHeaders.Names.CSEQ));
        response.headers().add(RtspHeaders.Names.PUBLIC, OPTIONS);
        logger.debug("返回响应: \n" + response.toString());
        ctx.channel().writeAndFlush(response);
       }catch (Exception e) {
            logger.error("Unexpected error during processing,Caused by ", e);
            HttpResponse response = new DefaultHttpResponse(RtspVersions.RTSP_1_0, RtspResponseStatuses.INTERNAL_SERVER_ERROR);
            response.headers().add(HttpHeaders.Names.SERVER, RTSP_SERVER );
            if(request.headers()!=null)
            response.headers().add(RtspHeaders.Names.CSEQ, request.headers().get(RtspHeaders.Names.CSEQ));
            ctx.channel().writeAndFlush(response);
        }
    }
    private void processDescribe(HttpRequest request){
        if(parserUri(request.getUri())){

            M2MMessage m2m = new M2MMessage();
            m2m.setCommandId(M2MMessage.COMMAND_DATA_JSON_DOWN);
            m2m.setSerialNo(this.serialNo);

            /*
            Integer txNo= terminalCom.increaseTxNo(this.serialNo);
           */
            m2m.setTxNo(txNo++);

            M2MDataJsonMessage m2mDataJson = new M2MDataJsonMessage();
            m2mDataJson.setTypeId(M2MDataJsonMessage.TYPE_S2T);
            ParamMap params= new ParamMap();
            params.put("cseq",request.headers().get(RtspHeaders.Names.CSEQ));
            params.put("type", RtspMessage.TYPE_RTSP_DESCRIPTOR);
            m2mDataJson.setData(params);
            m2m.setDataObj(m2mDataJson);

        if(m2mService!=null && this.serialNo!=null && !this.serialNo.isEmpty()){
            try {
                m2mService.send(m2m);
            } catch (Exception e) {
                logger.error(StringUtil.getFullStackTrace(e));
            }
        }
        }else {
            logger.error("serial is offline.");
            FullHttpResponse response = new DefaultFullHttpResponse(RtspVersions.RTSP_1_0, RtspResponseStatuses.INTERNAL_SERVER_ERROR);
            response.headers().set(HttpHeaders.Names.SERVER, RTSP_SERVER);
            response.headers().set(RtspHeaders.Names.CSEQ, request.headers().get(RtspHeaders.Names.CSEQ));
            ctx.channel().writeAndFlush(response);
        }
    }
    private void sendDescribe(String cseq){
        try{
        String requestContent = getSessionDescriptor();
        if(requestContent!=null){
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(RtspVersions.RTSP_1_0, RtspResponseStatuses.OK,Unpooled.copiedBuffer(requestContent, CharsetUtil.UTF_8));
        response.headers().set(RtspHeaders.Names.SERVER, RTSP_SERVER);
        response.headers().set(HttpHeaders.Names.DATE, getGmtDate());
        response.headers().set(RtspHeaders.Names.CSEQ, cseq);
        response.headers().set(RtspHeaders.Names.CONTENT_TYPE, "application/sdp");
        response.headers().set(RtspHeaders.Names.CONTENT_BASE,InetAddress.getLocalHost().getHostAddress()+":"+6018+"/");
        response.headers().set(RtspHeaders.Names.CONTENT_LENGTH, String.valueOf(requestContent.length()));
        logger.debug("返回响应: \n" + response.toString()+"\n"+response.content().toString(CharsetUtil.UTF_8));
        ctx.channel().writeAndFlush(response);
        } else{
            FullHttpResponse response = new DefaultFullHttpResponse(RtspVersions.RTSP_1_0, RtspResponseStatuses.INTERNAL_SERVER_ERROR);
            response.headers().set(HttpHeaders.Names.SERVER, RTSP_SERVER);
            response.headers().set(RtspHeaders.Names.CSEQ, cseq);
            ctx.channel().writeAndFlush(response);
        }
        }catch (UnknownHostException e){
            logger.error(e);
        }
    }
    private String getSessionDescriptor(){
        if(sessionDescriptor == null ||
                sessionDescriptor.getProfile()==null|| sessionDescriptor.getProfile().equals("")
                || sessionDescriptor.getSps()==null|| sessionDescriptor.getSps().equals("")
                || sessionDescriptor.getPps()==null|| sessionDescriptor.getPps().equals(""))
        return  null;
        try{
        StringBuilder sessionDescriptorString = new StringBuilder();
        sessionDescriptorString.append("v=0\n");
        // The RFC 4566 (5.2) suggest to use an NTP timestamp here but we will simply use a UNIX timestamp
        // TODO: Add IPV6 support
        sessionDescriptorString.append("o=- "+timestamp+" "+timestamp+" IN IP4 "+"0.0.0.0"+"\n");
        sessionDescriptorString.append("s=Unnamed\n");
        sessionDescriptorString.append("c=IN IP4 "+"0.0.0.0"+"\n");
        // t=0 0 means the session is permanent (we don't know when it will stop)
        sessionDescriptorString.append("t=0 0\n");
        sessionDescriptorString.append("a=recvonly\n");
        if(sessionDescriptor!=null)
        sessionDescriptorString.append("m=video 0 RTP/AVP 96\r\n" +
                "b=RR:0\r\n" +
                "a=rtpmap:96 H264/90000\r\n" +
                "a=fmtp:96 packetization-mode=1;profile-level-id="+sessionDescriptor.getProfile()
                +";sprop-parameter-sets="+sessionDescriptor.getSps()+","+sessionDescriptor.getPps()+";\r\n");

        sessionDescriptorString.append("a=control:trackID="+0+"\n");
        return sessionDescriptorString.toString();
        }catch (Exception e){//UnknownHostException

        }
        return null;

    }
    private void processSetup(HttpRequest request){
        FullHttpResponse response = null;
        //获取Cseq
        String cseq = request.headers().get(RtspHeaders.Names.CSEQ);
        if (null == cseq || "".equals(cseq)) {
            logger.error("Setup cesq is null.");
            response = new DefaultFullHttpResponse(RtspVersions.RTSP_1_0, RtspResponseStatuses.INTERNAL_SERVER_ERROR);
            response.headers().set(HttpHeaders.Names.SERVER, RTSP_SERVER);
            response.headers().set(RtspHeaders.Names.CSEQ, request.headers().get(RtspHeaders.Names.CSEQ));
            ctx.channel().writeAndFlush(response);
        }
        String strTransport = request.headers().get(RtspHeaders.Names.TRANSPORT);
        if (null == strTransport || strTransport.equals("")) {
            logger.error("Setup strTransport is null.");
            response = new DefaultFullHttpResponse(RtspVersions.RTSP_1_0, RtspResponseStatuses.INTERNAL_SERVER_ERROR);
            response.headers().set(HttpHeaders.Names.SERVER, RTSP_SERVER );
            response.headers().set(RtspHeaders.Names.CSEQ, request.headers().get(RtspHeaders.Names.CSEQ));
            ctx.channel().writeAndFlush(response);
        }
        Pattern p=Pattern.compile("\\S*interleaved\\S*",Pattern.CASE_INSENSITIVE);
        Matcher m=p.matcher(request.headers().get(RtspHeaders.Names.TRANSPORT));
        if(m.find())
            isRtpOverTcp=true;
        else isRtpOverTcp=false;
        if (isRtpOverTcp){
            M2MMessage m2m = new M2MMessage();
            m2m.setCommandId(M2MMessage.COMMAND_DATA_JSON_DOWN);
            m2m.setSerialNo(this.serialNo);
            Integer txNo= terminalCom.increaseTxNo(this.serialNo);
            m2m.setTxNo(txNo);
            M2MDataJsonMessage m2mDataJson = new M2MDataJsonMessage();
            m2mDataJson.setTypeId(M2MDataJsonMessage.TYPE_S2T);
            ParamMap params= new ParamMap();
            params.put("cseq",request.headers().get(RtspHeaders.Names.CSEQ));
            params.put("type", RtspMessage.TYPE_RTSP_SETUP);
            m2mDataJson.setData(params);
            m2m.setDataObj(m2mDataJson);
            if(m2mService!=null && this.serialNo!=null && !this.serialNo.isEmpty()){
                try {
                    m2mService.send(m2m);
                } catch (Exception e) {
                    logger.error(StringUtil.getFullStackTrace(e));
                }
            }
        }
    }
    private void sendSetup(int ssrc,String cseq){
        RtpRelayHandler.relayPairTcp.put(ssrc,this);
        FullHttpResponse response = null;
        response = new DefaultFullHttpResponse(RtspVersions.RTSP_1_0, RtspResponseStatuses.OK);
        String transport = "RTP/AVP/TCP;unicast;interleaved=0-1;" +
                "ssrc="+Integer.toHexString(1940706664)+";";
        response.headers().add(RtspHeaders.Names.SERVER, RTSP_SERVER);
        response.headers().set(HttpHeaders.Names.DATE, getGmtDate());
        response.headers().set(RtspHeaders.Names.CSEQ, cseq);
        response.headers().set(RtspHeaders.Names.SESSION, "1185d20035702ca;timeout=-1");
        response.headers().set(RtspHeaders.Names.TRANSPORT,transport);
        response.headers().set(RtspHeaders.Names.CACHE_CONTROL,"no-cache");

        logger.debug("返回响应: \n" + response.toString());
        ctx.channel().writeAndFlush(response);
    }
    private void processPlay(HttpRequest request){
        FullHttpResponse response = null;
        String cseq = request.headers().get(RtspHeaders.Names.CSEQ);
        if (null == cseq || "".equals(cseq)) {
            logger.error("Play cesq is null.");
            response = new DefaultFullHttpResponse(RtspVersions.RTSP_1_0, RtspResponseStatuses.INTERNAL_SERVER_ERROR);
            response.headers().set(HttpHeaders.Names.SERVER, RTSP_SERVER);
            response.headers().set(RtspHeaders.Names.CSEQ, request.headers().get(RtspHeaders.Names.CSEQ));
            ctx.channel().writeAndFlush(response);
        }
        String sessionId = request.headers().get(RtspHeaders.Names.SESSION);
        if (sessionId == null) {
            logger.error("Play session  is null.");
            response = new DefaultFullHttpResponse(RtspVersions.RTSP_1_0, RtspResponseStatuses.INTERNAL_SERVER_ERROR);
            response.headers().set(HttpHeaders.Names.SERVER, RTSP_SERVER);
            response.headers().set(RtspHeaders.Names.CSEQ, request.headers().get(RtspHeaders.Names.CSEQ));
            ctx.channel().writeAndFlush(response);
        }
        if (isRtpOverTcp){
            M2MMessage m2m = new M2MMessage();
            m2m.setCommandId(M2MMessage.COMMAND_DATA_JSON_DOWN);
            m2m.setSerialNo(this.serialNo);
            Integer txNo= terminalCom.increaseTxNo(this.serialNo);
            m2m.setTxNo(txNo);
            M2MDataJsonMessage m2mDataJson = new M2MDataJsonMessage();
            m2mDataJson.setTypeId(M2MDataJsonMessage.TYPE_S2T);
            ParamMap params= new ParamMap();
            params.put("cseq",request.headers().get(RtspHeaders.Names.CSEQ));
            params.put("type", RtspMessage.TYPE_RTSP_PLAY);
            m2mDataJson.setData(params);
            m2m.setDataObj(m2mDataJson);
            if(m2mService!=null && this.serialNo!=null && !this.serialNo.isEmpty()){
                try {
                    m2mService.send(m2m);
                } catch (Exception e) {
                    logger.error(StringUtil.getFullStackTrace(e));
                }
            }
        }
        sendPlay("url=trackID=0",request.headers().get(RtspHeaders.Names.CSEQ));
    }
    private void sendPlay(String rtpinfo,String cseq){
        FullHttpResponse response = null;
        response = new DefaultFullHttpResponse(RtspVersions.RTSP_1_0, RtspResponseStatuses.OK);
        response.headers().add(RtspHeaders.Names.SERVER, RTSP_SERVER);
        response.headers().set(HttpHeaders.Names.DATE, getGmtDate());
        response.headers().set(RtspHeaders.Names.CSEQ, cseq);
        response.headers().set(RtspHeaders.Names.RTP_INFO,rtpinfo);
        response.headers().set(RtspHeaders.Names.SESSION, "1185d20035702ca;timeout=-1");
        response.headers().set(RtspHeaders.Names.CONTENT_LENGTH, 0);
        logger.debug("返回响应: \n" + response.toString());
        ctx.channel().writeAndFlush(response);
    }
    private HttpResponse processPause(HttpRequest request){
        HttpResponse response = null;
        return response;
    }
    private HttpResponse processGetParameter(HttpRequest request){
        HttpResponse response = null;
        return response;
    }
    private void processTeardown(HttpRequest request){
        FullHttpResponse response = null;
        String cseq = request.headers().get(RtspHeaders.Names.CSEQ);
        if (null == cseq || "".equals(cseq)) {
            logger.error("Teardown cesq is null.");
            response = new DefaultFullHttpResponse(RtspVersions.RTSP_1_0, RtspResponseStatuses.INTERNAL_SERVER_ERROR);
            response.headers().set(HttpHeaders.Names.SERVER, RTSP_SERVER);
            response.headers().set(RtspHeaders.Names.CSEQ, request.headers().get(RtspHeaders.Names.CSEQ));
            ctx.channel().writeAndFlush(response);
        }
        M2MMessage m2m = new M2MMessage();
        m2m.setCommandId(M2MMessage.COMMAND_DATA_JSON_DOWN);
        m2m.setSerialNo(this.serialNo);
        Integer txNo= terminalCom.increaseTxNo(this.serialNo);
        m2m.setTxNo(txNo);
        M2MDataJsonMessage m2mDataJson = new M2MDataJsonMessage();
        m2mDataJson.setTypeId(M2MDataJsonMessage.TYPE_S2T);
        ParamMap params= new ParamMap();
        params.put("cseq",request.headers().get(RtspHeaders.Names.CSEQ));
        params.put("type", RtspMessage.TYPE_RTSP_TEARDOWN);
        m2mDataJson.setData(params);
        m2m.setDataObj(m2mDataJson);
        if(m2mService!=null && this.serialNo!=null && !this.serialNo.isEmpty()){
            try {
                m2mService.send(m2m);
            } catch (Exception e) {
                logger.error(StringUtil.getFullStackTrace(e));
            }
        }
        sendTeardown(request.headers().get(RtspHeaders.Names.CSEQ));
    }
    private void sendTeardown(String cseq){
        FullHttpResponse response = null;
        response = new DefaultFullHttpResponse(RtspVersions.RTSP_1_0, RtspResponseStatuses.OK);
        response.headers().add(RtspHeaders.Names.SERVER, RTSP_SERVER);
        response.headers().set(HttpHeaders.Names.DATE, getGmtDate());
        response.headers().set(RtspHeaders.Names.CSEQ, cseq);
        response.headers().set(RtspHeaders.Names.SESSION, "1185d20035702ca");
        response.headers().set(RtspHeaders.Names.CONTENT_LENGTH, 0);
        logger.debug("返回响应: \n" + response.toString());
        ctx.channel().writeAndFlush(response);
    }
    public void sendRtp(ByteBuf data){
        int dataLength=data.readableBytes();
        byte []  header=new byte[4];
        header[0] = 0x24;
        header[1] = 0x00;
        header[2] = (byte)((dataLength & 0x0000FF00)>> 8);
        header[3] = (byte)(dataLength & 0x000000FF);
        ByteBuf headerBuf=Unpooled.directBuffer(dataLength+4);
        headerBuf.writeBytes(header);
        headerBuf.writeBytes(data);
        ctx.channel().writeAndFlush(headerBuf);
    }
    public void processDisconnect(){
        M2MMessage m2m = new M2MMessage();
        m2m.setCommandId(M2MMessage.COMMAND_DATA_JSON_DOWN);
        m2m.setSerialNo(this.serialNo);
        Integer txNo= terminalCom.increaseTxNo(this.serialNo);
        m2m.setTxNo(txNo);
        M2MDataJsonMessage m2mDataJson = new M2MDataJsonMessage();
        m2mDataJson.setTypeId(M2MDataJsonMessage.TYPE_S2T);
        ParamMap params= new ParamMap();
        params.put("type", RtspMessage.TYPE_RTSP_DISCONNECT);
        params.put("info","channelInactive");
        m2mDataJson.setData(params);
        m2m.setDataObj(m2mDataJson);
        if(m2mService!=null && this.serialNo!=null && !this.serialNo.isEmpty()){
            try {
                m2mService.send(m2m);
            } catch (Exception e) {
                logger.error(StringUtil.getFullStackTrace(e));
            }
        }
    }
    private  String getGmtDate() {
        DateFormat df = new SimpleDateFormat("d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("GMT")); // modify Time Zone.
        return (df.format(new Date()));
    }
    private Boolean parserUri(String uri){
        List<NameValuePair> params = URLEncodedUtils.parse(URI.create(uri), "UTF-8");
        if (params.size()>0) {
            for (Iterator<NameValuePair> it = params.iterator();it.hasNext();) {
                NameValuePair param = it.next();// FLASH ON/OFF
                if (param.getName().equals("serialNo")) {
                    this.serialNo=param.getValue();
                    this.rtspRequestHandler.addRtspMessageListener(this.serialNo,new OnRtspMessageListener() {
                        public void OnDescribeArrived(SessionDescriptor sessionDescriptor,String cseq) {
                            RtspSession.this.sessionDescriptor=sessionDescriptor;
                            sendDescribe(cseq);
                        }
                        public void OnSetupArrived(int ssrc, String cseq) {
                            sendSetup(ssrc,cseq);
                        }
                        public void OnPlayArrived( String cseq) {
                            //sendPlay(rtpInfo,cseq);
                        }
                        public void OnTeardownArrived(String cseq) {
                            //sendTeardown(cseq);
                        }
                    });
                    logger.debug("serialNo parsed:"+param.getValue());
                    return  true;
            }
        }
        }
       return false;
    }
}
