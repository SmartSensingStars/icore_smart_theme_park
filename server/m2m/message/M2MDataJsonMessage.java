package com.larcloud.server.m2m.message;

import com.larcloud.util.ParamMap;




/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-4-2
 * Time: 下午5:27
 * To change this template use File | Settings | File Templates.
 */
public class M2MDataJsonMessage {
    public static final String TYPE_ID ="typeId";
    public static final Integer TYPE_T2T = 1;
    public static final Integer TYPE_S2T = 2;
    public static final Integer TYPE_ACK_ERROR = 4;
    public static final Integer TYPE_ACK_OK = 8;


    private Integer typeId;

    private ParamMap data;

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public ParamMap getData() {
        return data;
    }

    public void setData(ParamMap data) {
        this.data = data;
    }
}
