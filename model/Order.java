package com.larcloud.model;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-5-26
 * Time: 上午11:22
 * To change this template use File | Settings | File Templates.
 */
public class Order extends BaseModel {
    public static final String ORDER_ID = "orderId";
    public static final String TAG_NO = "tagNo";
    public static final String STATUS = "status";
    public static final String AMOUNT = "amount";
    public static final String PROGRAM_ITEM_ID_LIST = "programItemIdList";
    public static final Integer STATUS_NEW=1;
    public static final Integer STATUS_OK=8;
    public static final Integer STATUS_CANCELLED=4;

    private Integer orderId;
    private String tagNo;
    private Double amount;
    private Integer status;
    private String statusName;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getTagNo() {
        return tagNo;
    }

    public void setTagNo(String tagNo) {
        this.tagNo = tagNo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
        if (status==STATUS_OK){
            statusName="完成";
        }
        if (status==STATUS_NEW){
            statusName="新增";
        }
        if (status==STATUS_CANCELLED){
            statusName="取消";
        }
    }
}
