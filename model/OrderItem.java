package com.larcloud.model;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-5-26
 * Time: 上午11:22
 * To change this template use File | Settings | File Templates.
 */
public class OrderItem extends BaseModel {
    public static final String ORDER_ITEM_ID = "orderItemId";
    public static final String PRODUCT_ID = "productId";
    public static final String ORDER_ITEM_ID_LIST = "orderItemIdList";

    private Integer orderItemId;
    private Integer productId;
    private ProgramItem product;
    private Integer orderId;
    private Order order;
    private Double quantity;
    private Double unitPrice;
    private List<Integer> orderItemList;


    public OrderItem() {
    }


    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public ProgramItem getProduct() {
        return product;
    }

    public void setProduct(ProgramItem product) {
        this.product = product;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }


    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public List<Integer> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<Integer> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
