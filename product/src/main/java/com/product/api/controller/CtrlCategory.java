package com.product.api.controller;

import com.product.api.dto.ApiResponse;
import com.product.api.entity.Category;
import com.product.api.service.SvcCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.product.api.dto.ApiResponse.statusBadRequest;

@RestController
@RequestMapping("/category")
public class CtrlCategory {

    private final SvcCategory svc;

    @Autowired
    public CtrlCategory(SvcCategory _svc) {
        this.svc = _svc;
    }

    @GetMapping
    public ApiResponse<List<Category>> getCategories() {
        return svc.getCategories();
    }

    @GetMapping("/{category_id}")
    public ApiResponse<Category> getCategory(@PathVariable int category_id) {
        return svc.getCategory(category_id);
    }

    @PostMapping
    public ApiResponse<String> createCategory(@Valid @RequestBody Category category, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw statusBadRequest(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        return svc.createCategory(category);
    }

    @PutMapping("/{category_id}")
    public ApiResponse<String> updateCategory(@PathVariable int category_id, @Valid @RequestBody Category category, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw statusBadRequest(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        return svc.updateCategory(category_id, category);
    }

    @DeleteMapping("/{category_id}")
    public ApiResponse<String> deleteCategory(@PathVariable int category_id) {
        return svc.deleteCategory(category_id);
    }

}
