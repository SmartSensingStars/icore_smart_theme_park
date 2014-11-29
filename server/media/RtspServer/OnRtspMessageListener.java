package com.larcloud.server.media.RtspServer;

import com.larcloud.server.m2m.message.M2MMessage;
import com.larcloud.server.media.RtspServer.message.SessionDescriptor;

/**
 * Created with IntelliJ IDEA.
 * User: ycd
 * Date: 14-7-1
 */
public interface OnRtspMessageListener {
    void OnDescribeArrived(SessionDescriptor sessionDescriptor, String cseq);
    void OnSetupArrived(int ssrc, String cseq);
    void OnPlayArrived(String cseq);
    void OnTeardownArrived(String cseq);
}
