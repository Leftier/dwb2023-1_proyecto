package com.product.api.service;

import com.product.api.dto.ApiResponse;
import com.product.api.entity.Product;

import java.util.List;

public interface SvcProduct {

    ApiResponse<List<Product>> getProducts(Integer category_id);

    ApiResponse<Product> getProduct(String gtin);

    ApiResponse<String> createProduct(Product in);

    ApiResponse<String> updateProduct(Product in, Integer id);

    ApiResponse<String> updateProductStock(String gtin, Integer stock);

    ApiResponse<String> deleteProduct(Integer id);

    ApiResponse<String> updateProductCategory(String gtin, Integer category_id);
}
