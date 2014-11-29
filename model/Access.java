package com.larcloud.model;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 13-6-23
 * Time: 下午5:50
 * To change this template use File | Settings | File Templates.
 */
public class Access extends BaseModel {
    public static final String ACCESS_ID = "accessId";
    public static final String ENDED_AT = "endedAt";
    public static final String ACCESS_KEY = "accessKey";
    public static final String LAST_ACCESSED_AT = "lastAccessedAt";

    private Integer accessId;
    private Date endedAt;
    private User user;
    private String accessKey;
    private Date lastAccessedAt;

    public Integer getAccessId() {
        return accessId;
    }

    public void setAccessId(Integer accessId) {
        this.accessId = accessId;
    }

    public Date getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(Date endedAt) {
        this.endedAt = endedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public Date getLastAccessedAt() {
        return lastAccessedAt;
    }

    public void setLastAccessedAt(Date lastAccessedAt) {
        this.lastAccessedAt = lastAccessedAt;
    }
}
