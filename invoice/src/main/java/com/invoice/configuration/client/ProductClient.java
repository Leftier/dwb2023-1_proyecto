package com.invoice.configuration.client;

import com.invoice.api.dto.DtoProduct;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.invoice.api.dto.ApiResponse;



public interface ProductClient {

    /*
     * Requerimiento 3
     * Actualizar método getProduct para obtener la información necesaria de un producto
     */
    @GetMapping("product/{gtin}")
    ResponseEntity<DtoProduct> getProduct(@PathVariable("gtin") String gtin);

    @PutMapping("product/{gtin}/stock/{stock}")
    ResponseEntity<ApiResponse> updateProductStock(@PathVariable("gtin") String gtin, @PathVariable("stock") Integer stock);

}
