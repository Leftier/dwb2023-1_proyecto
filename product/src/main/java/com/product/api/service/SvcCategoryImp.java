package com.product.api.service;

import com.product.api.dto.ApiResponse;
import com.product.api.entity.Category;
import com.product.api.repository.RepoCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.product.api.dto.ApiResponse.*;

@Service
public class SvcCategoryImp implements SvcCategory {

    private final RepoCategory repo;

    @Autowired
    public SvcCategoryImp(RepoCategory _repo) {
        this.repo = _repo;
    }

    @Override
    public ApiResponse<List<Category>> getCategories() {
        List<Category> categories = repo.findByStatus(1);
        return statusOk(categories);
    }

    @Override
    public ApiResponse<Category> getCategory(Integer category_id) {
        Category category = repo.findByCategoryId(category_id);
        if (category == null)
            throw statusBadRequest("category does not exist");

        return statusOk(category);
    }

    @Override
    public ApiResponse<String> createCategory(Category category) {
        Category categorySaved = repo.findByCategory(category.getCategory());
        if (categorySaved == null) {
            repo.createCategory(category.getCategory());
            return statusCreated("category created");
        }

        if (categorySaved.getStatus() == 0) {
            repo.activateCategory(categorySaved.getCategory_id());
            return statusOk("category has been activated");
        }

        throw statusBadRequest("category already exists");
    }

    @Override
    public ApiResponse<String> updateCategory(Integer category_id, Category category) {
        Category categorySaved = repo.findByCategoryId(category_id);
        if (categorySaved == null)
            throw statusBadRequest("category does not exist");

        if (categorySaved.getStatus() == 0)
            throw statusBadRequest("category is not active");

        categorySaved = repo.findByCategory(category.getCategory());
        if (categorySaved != null)
            throw statusBadRequest("category already exists");

        repo.updateCategory(category_id, category.getCategory());
        return statusOk("category updated");
    }

    @Override
    public ApiResponse<String> deleteCategory(Integer category_id) {
        Category categorySaved = repo.findByCategoryId(category_id);
        if (categorySaved == null)
            throw statusNotFound("category does not exist");

        repo.deleteById(category_id);
        return statusOk("category removed");
    }

}