package com.invoice.api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.invoice.api.entity.Cart;
import com.invoice.api.repository.RepoCart;
import com.invoice.configuration.client.ProductClient;
import com.invoice.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.invoice.api.dto.ApiResponse;
import com.invoice.api.entity.Invoice;
import com.invoice.api.entity.Item;
import com.invoice.api.repository.RepoInvoice;
import com.invoice.api.repository.RepoItem;

@Service
public class SvcInvoiceImp implements SvcInvoice {

    @Autowired
    RepoInvoice repo;

    @Autowired
    RepoItem repoItem;

    @Autowired
    RepoCart repoCart;

    @Autowired
    ProductClient productCl;

    @Override
    public List<Invoice> getInvoices(String rfc) {
        return repo.findByRfcAndStatus(rfc, 1);
    }

    @Override
    public List<Item> getInvoiceItems(Integer invoice_id) {
        return repoItem.getInvoiceItems(invoice_id);
    }

    @Override
    public ApiResponse generateInvoice(String rfc) {
        /*
         * Requerimiento 5
         * Implementar el método para generar una factura
         */

        //[ El método recibe un RFC, con este se debe consultar el carrito de compras del cliente, si no tiene
        //[ productos en el carrito se debe enviar un NOT FOUND con el mensaje ”cart has no items”
        List<Cart> carts = repoCart.findByRfcAndStatus(rfc, 1);
        if (carts.isEmpty())
            throw new ApiException(HttpStatus.NOT_FOUND, "cart has no items");

        //[ Por cada producto en el carrito, se debe generar un artículo de la factura, donde:
        //[ – unit price = precio del producto
        //[ – total = precio del producto por la cantidad de productos comprados
        //[ – taxes = 16% del total
        //[ – subtotal = total menos impuestos
        List<Item> items = new ArrayList<>();
        for (Cart cart : carts) {
            Item item = new Item();
            item.setGtin(cart.getGtin());
            item.setQuantity(cart.getQuantity());
            Double unit_price = productCl.getProduct(cart.getGtin()).getBody().getPrice();
            item.setUnit_price(unit_price);

            double total = cart.getQuantity() * unit_price;
            item.setTotal(total);

            double taxes = total * .16;
            item.setTaxes(taxes);

            double subtotal = total - item.getTaxes();
            item.setSubtotal(subtotal);
            item.setStatus(1);
            items.add(item);
        }

        //[ Para la factura:
        //[ – total = suma de los totales de todos los productos
        //[ – taxes = suma de los impuestos de todos los productos
        //[ – subtotal = suma de los subtotales de todos los productos
        //[ – created at = fecha y hora en la que se generó la factura
        Invoice invoice = new Invoice();
        double total = 0;
        double taxes = 0;
        double subtotal = 0;

        for (Item item : items) {
            total += item.getTotal();
            taxes += item.getTaxes();
            subtotal += item.getSubtotal();
        }

        invoice.setTotal(total);
        invoice.setTaxes(taxes);
        invoice.setSubtotal(subtotal);
        invoice.setCreated_at(LocalDateTime.now());

        invoice.setRfc(rfc);
        invoice.setStatus(1);

        //[ Por cada artículo comprado, se debe actualizar el stock del producto
        int id = repo.save(invoice).getInvoice_id();
        for (Item item : items) {
            productCl.updateProductStock(item.getGtin(), item.getQuantity());
            item.setId_invoice(id);
        }

        //[ Vaciar el carrito de compras del cliente
        repoCart.clearCart(rfc);

        //[ Toda la información de la factura y sus artículos deben quedar registrados en la base de datos
        repoItem.saveAll(items);
        return new ApiResponse("invoice generated");
    }

}
