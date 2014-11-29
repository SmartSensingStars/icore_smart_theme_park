package com.larcloud.util;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-3-9
 * Time: 下午1:12
 * To change this template use File | Settings | File Templates.
 */
public class ParamItem {
    private String key;
    private Object value;

    public ParamItem(String key,Object value){
        this.key=key;
        this.value=value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public java.lang.Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
