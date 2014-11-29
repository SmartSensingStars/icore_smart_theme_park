package com.larcloud.async;

/**
* Created with IntelliJ IDEA.
* User: spectrext
* Date: 14-7-5
* Time: 下午6:27
* To change this template use File | Settings | File Templates.
*/
public class AsyncRequestItem {
    private Integer objectId;
    private Integer status;
    private Object data;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
