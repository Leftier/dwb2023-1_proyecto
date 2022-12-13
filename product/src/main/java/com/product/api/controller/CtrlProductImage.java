package com.product.api.controller;

import com.product.api.dto.ApiResponse;
import com.product.api.entity.ProductImage;
import com.product.api.service.SvcProductImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.product.api.dto.ApiResponse.statusBadRequest;

@RestController
@RequestMapping("/product-image")
public class CtrlProductImage {

	private final  SvcProductImage svc;

    @Autowired
    public CtrlProductImage(SvcProductImage svc) {
        this.svc = svc;
    }

    @GetMapping("/{product_id}")
    public ApiResponse<List<ProductImage>> getProductImages(@PathVariable("product_id") Integer product_id) {
        return svc.getProductImages(product_id);
    }

    @PostMapping
    public ApiResponse<String> uploadProductImage(@Valid @RequestBody ProductImage in, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw statusBadRequest(bindingResult.getAllErrors().get(0).getDefaultMessage());

        return svc.createProductImage(in);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteProductImage(@PathVariable("id") Integer id) {
        return svc.deleteProductImage(id);
    }

}
