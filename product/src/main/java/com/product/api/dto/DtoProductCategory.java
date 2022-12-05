package com.product.api.dto;

public class DtoProductCategory {

    private Integer category_id;

    public DtoProductCategory() {
    }

    public DtoProductCategory(Integer _category_id) {
        this.category_id = _category_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

}
