package com.product.api.service;

import com.product.api.dto.ApiResponse;
import com.product.api.entity.ProductImage;

import java.util.List;

public interface SvcProductImage {

    ApiResponse<List<ProductImage>> getProductImages(Integer product_id);

    ApiResponse<String> createProductImage(ProductImage in);

    ApiResponse<String> deleteProductImage(Integer id);

}
