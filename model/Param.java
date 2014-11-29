package com.larcloud.model;

/**
 * Created with IntelliJ IDEA.
 * User: lynn
 * Date: 14-8-7
 * Time: 下午1:34
 * To change this template use File | Settings | File Templates.
 */
public class Param extends BaseModel {
    public static final String PARAM_ID = "paramId";
    public static final String NAME = "name";
    public static final String PRICE = "price";

    private Integer paramId;
    private String name;
    private Integer price;


    public Integer getParamId() {
        return paramId;
    }

    public void setParamId(Integer paramId) {
        this.paramId = paramId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
