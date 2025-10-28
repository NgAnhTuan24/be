package com.example.be.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetailDTO {
    private Long id;
    private CatSummaryDTO cat;
    private Double price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CatSummaryDTO getCat() {
        return cat;
    }

    public void setCat(CatSummaryDTO cat) {
        this.cat = cat;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
