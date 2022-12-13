package com.invoice.api.service;

import java.util.List;

import com.invoice.configuration.client.ProductClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.invoice.api.dto.ApiResponse;
import com.invoice.api.entity.Cart;
import com.invoice.api.repository.RepoCart;
import com.invoice.configuration.client.CustomerClient;
import com.invoice.exception.ApiException;

@Service
public class SvcCartImp implements SvcCart {

    @Autowired
    RepoCart repo;

    @Autowired
    CustomerClient customerCl;

    @Autowired
    ProductClient productCl;

    @Override
    public List<Cart> getCart(String rfc) {
        return repo.findByRfcAndStatus(rfc, 1);
    }

    @Override
    public ApiResponse addToCart(Cart cart) {
        if (!validateCustomer(cart.getRfc()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "customer does not exist");

        /*
         * Requerimiento 3
         * Validar que el GTIN exista. Si existe, asignar el stock del producto a la variable product_stock
         */
        if (!validateProduct(cart.getGtin()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "product does not exist");

        Integer product_stock = productCl.getProduct(cart.getGtin()).getBody().getStock(); //[ Stock could be null
        if (cart.getQuantity() > product_stock) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "invalid quantity");
        }

        /*
         * Requerimiento 4
         * Validar si el producto ya había sido agregado al carrito para solo actualizar su cantidad
         */
        List<Cart> carts = repo.findByRfcAndStatus(cart.getRfc(), 1);
        for (Cart item : carts) {
            if (!item.getGtin().equals(cart.getGtin())) continue;

            if (item.getQuantity() + cart.getQuantity() > product_stock)
                throw new ApiException(HttpStatus.BAD_REQUEST, "invalid quantity");

            repo.updateCartQty(item.getCart_id(), item.getQuantity() + cart.getQuantity());
            return new ApiResponse("quantity updated");
        }

        cart.setStatus(1);
        repo.save(cart);
        return new ApiResponse("item added");
    }

    @Override
    public ApiResponse removeFromCart(Integer cart_id) {
        if (repo.removeFromCart(cart_id) <= 0)
            throw new ApiException(HttpStatus.BAD_REQUEST, "item cannot be removed");

        return new ApiResponse("item removed");
    }

    @Override
    public ApiResponse clearCart(String rfc) {
        if (repo.clearCart(rfc) <= 0)
            throw new ApiException(HttpStatus.BAD_REQUEST, "cart cannot be removed");

        return new ApiResponse("cart removed");
    }

    private boolean validateCustomer(String rfc) {
        try {
            return customerCl.getCustomer(rfc).getStatusCode() == HttpStatus.OK;
        } catch (Exception exception) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "unable to retrieve customer information");
        }
    }

    private boolean validateProduct(String gtin) {
        try {
            return productCl.getProduct(gtin).getStatusCode() == HttpStatus.OK;
        } catch (Exception exception) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "unable to retrieve product information");
        }
    }
}
