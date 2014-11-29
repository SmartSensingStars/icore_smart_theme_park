package com.larcloud.server.media.RtspServer.message;

/**
 * Created with IntelliJ IDEA.
 * User: EggYan
 * Date: 14-7-2
 * Time: 下午9:03
 * To change this template use File | Settings | File Templates.
 */
public class RtspMessage {
    public static final String TYPE = "type";
    //实时视频
    public static final int TYPE_RTSP_DESCRIPTOR = 1;
    public static final int TYPE_RTSP_DESCRIPTOR_RESPONSE = 2;
    public static final int TYPE_RTSP_SETUP = 3;
    public static final int TYPE_RTSP_SETUP_RESPONSE = 4;
    public static final int TYPE_RTSP_PLAY = 5;
    public static final int TYPE_RTSP_PLAY_RESPONSE = 6;
    public static final int TYPE_RTSP_TEARDOWN = 7;
    public static final int TYPE_RTSP_TEARDOWN_RESPONSE = 8;
    public static final int TYPE_RTSP_DISCONNECT = 9;
    public static final int TYPE_RTSP_DISCONNECT_RESPONSE = 10;
}
