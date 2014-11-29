package com.larcloud.server.media.RtspServer.message;

/**
 * Created with IntelliJ IDEA.
 * User: ycd
 * Date: 14-7-1
 */
public class SessionDescriptor {

    private String profile;
    private String sps;
    private String pps;


    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getSps() {
        return sps;
    }

    public void setSps(String sps) {
        this.sps = sps;
    }

    public String getPps() {
        return pps;
    }

    public void setPps(String pps) {
        this.pps = pps;
    }
}


