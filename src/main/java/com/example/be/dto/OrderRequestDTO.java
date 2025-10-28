package com.example.be.dto;

import java.util.List;

public class OrderRequestDTO {
    private Long userId;
    private List<Long> catIds;
    private Double shippingFee;
    private String shippingMethod;
    private String paymentMethod;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getCatIds() {
        return catIds;
    }

    public void setCatIds(List<Long> catIds) {
        this.catIds = catIds;
    }

    public Double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(Double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
