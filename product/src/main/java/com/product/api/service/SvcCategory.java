package com.product.api.service;

import com.product.api.dto.ApiResponse;
import com.product.api.entity.Category;

import java.util.List;

public interface SvcCategory {

    ApiResponse<List<Category>> getCategories();

    ApiResponse<Category> getCategory(Integer category_id);

    ApiResponse<String> createCategory(Category category);

    ApiResponse<String> updateCategory(Integer category_id, Category category);

    ApiResponse<String> deleteCategory(Integer category_id);

}