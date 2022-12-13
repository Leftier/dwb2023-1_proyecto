package com.product.api.controller;

import javax.validation.Valid;

import com.product.api.dto.DtoProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.product.api.dto.ApiResponse;
import com.product.api.entity.Product;
import com.product.api.service.SvcProduct;

import java.util.List;

import static com.product.api.dto.ApiResponse.statusBadRequest;

@RestController
@RequestMapping("/product")
public class CtrlProduct {

    private final SvcProduct svc;

    @Autowired
    public CtrlProduct(SvcProduct svc) {
        this.svc = svc;
    }

    @GetMapping("/category/{category_id}")
    public ApiResponse<List<Product>> getProducts(@PathVariable int category_id) {
        return svc.getProducts(category_id);
    }

    @GetMapping("/{gtin}")
    public ApiResponse<Product> getProduct(@PathVariable String gtin) {
        return svc.getProduct(gtin);
    }

    @PostMapping
    public ApiResponse<String> createProduct(@Valid @RequestBody Product in, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw statusBadRequest(bindingResult.getAllErrors().get(0).getDefaultMessage());

        return svc.createProduct(in);
    }

    @PutMapping("/{id}")
    public ApiResponse<String> updateProduct(@PathVariable("id") Integer id, @Valid @RequestBody Product in, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw statusBadRequest(bindingResult.getAllErrors().get(0).getDefaultMessage());

        return svc.updateProduct(in, id);
    }

    @PutMapping("/{gtin}/stock/{stock}")
    public ApiResponse<String> updateProductStock(@PathVariable("gtin") String gtin, @PathVariable("stock") int stock) {
        return svc.updateProductStock(gtin, stock);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteProduct(@PathVariable("id") Integer id) {
        return svc.deleteProduct(id);
    }

    @PutMapping("/{gtin}/category")
    public ApiResponse<String> updateProductCategory(@PathVariable("gtin") String gtin, @Valid @RequestBody DtoProductCategory productCategory, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw statusBadRequest(bindingResult.getAllErrors().get(0).getDefaultMessage());

        return svc.updateProductCategory(gtin, productCategory.getCategory_id());
    }
}
