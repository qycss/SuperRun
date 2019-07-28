package com.kechuang.www.kc.HelpView;

/**
 * Created by lyq on 2018/9/20.
 */

public class Order {
    private String Id;
    private Integer orderType;
    private String orderTitle;
    private String orderDetail;
    private String pubId;
    private String accId;

    public Order(String id,Integer type,String title,String detail,String pubid,String accid){
        this.Id = id;
        this.orderType=type;
        this.orderTitle = title;
        this.orderDetail = detail;
        this.pubId = pubid;
        this.accId = accid;
    }


    public String getOrderId() {
        return Id;
    }

    public void setOrderId(String orderId) {
        Id = orderId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        orderType = orderType;
    }

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        orderTitle = orderTitle;
    }

    public String getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(String orderDetail) {
        orderDetail = orderDetail;
    }

    public String getPubId() {
        return pubId;
    }

    public void setPubId(String pubId) {
        this.pubId = pubId;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }





}
