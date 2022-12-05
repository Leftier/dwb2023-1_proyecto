package com.product.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.product.api.dto.ApiResponse;
import com.product.api.entity.Product;
import com.product.api.repository.RepoCategory;
import com.product.api.repository.RepoProduct;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import static com.product.api.dto.ApiResponse.*;

@Service
public class SvcProductImp implements SvcProduct {

    private final RepoProduct repo;
    private final RepoCategory repoCategory;

    @Autowired
    public SvcProductImp(RepoProduct repo, RepoCategory repoCategory) {
        this.repo = repo;
        this.repoCategory = repoCategory;
    }

    @Override
    public ApiResponse<List<Product>> getProducts(Integer category_id) {
        List<Product> products = repo.findByCategoryId(category_id);

        return statusOk(products);
    }

    @Override
    public ApiResponse<Product> getProduct(String gtin) {
        Product product = repo.findByGtinAndStatus(gtin, 1);
        if (product == null)
            throw statusNotFound("product does not exist");

        product.setCategory(repoCategory.findByCategoryId(product.getCategory_id()));
        return statusOk(product);
    }

    @Override
    public ApiResponse<String> createProduct(Product in) {
        Product productSaved = repo.findByGtinAndStatus(in.getGtin(), 0);
        if (productSaved != null && productSaved.getStatus() == 0) {
            // Con update validamos nombre, GTIN y categoria única
            updateProduct(in, productSaved.getProduct_id());
            return statusOk("product activated");
        }

        try {
            in.setStatus(1);
            repo.save(in);
        } catch (DataIntegrityViolationException e) {
            handleException(e);
        }

        return statusOk("product created");
    }

    @Override
    public ApiResponse<String> updateProduct(Product in, Integer id) {
        Product productSaved = repo.findByGtin(in.getGtin());
        if (productSaved == null)
            throw statusNotFound("product does not exist");

        try {
            repo.updateProduct(id, in.getGtin(), in.getProduct(), in.getDescription(), in.getPrice(), in.getStock(), in.getCategory_id());
        } catch (DataIntegrityViolationException e) {
            handleException(e);
        }

        return statusOk("product updated");
    }

    private void handleException(DataIntegrityViolationException e) {
        if (e.getLocalizedMessage().contains("gtin"))
            throw statusBadRequest("product gtin already exist");

        if (e.getLocalizedMessage().contains("product"))
            throw statusBadRequest("product name already exist");

        if (e.contains(SQLIntegrityConstraintViolationException.class))
            throw statusNotFound("category not found");

        throw statusBadRequest("product cannot be created/updated");
    }

    @Override
    public ApiResponse<String> deleteProduct(Integer id) {
        if (repo.deleteProduct(id) <= 0)
            throw statusBadRequest("product cannot be deleted");

        return statusOk("product removed");
    }

    @Override
    public ApiResponse<String> updateProductStock(String gtin, Integer stock) {
        Product product = (Product) getProduct(gtin).getBody();
        if (product == null)
            throw statusNotFound("product does not exist");

        if (stock > product.getStock())
            throw statusBadRequest("stock to update is invalid");

        repo.updateProductStock(gtin, product.getStock() - stock);
        return statusOk("product stock updated");
    }

    @Override
    public ApiResponse<String> updateProductCategory(String gtin, Integer category_id) {
        try {
            int updated = repo.updateProductCategory(gtin, category_id);
            if (updated <= 0)
                throw statusBadRequest("product category cannot be updated");

        } catch (DataIntegrityViolationException e) {
            handleException(e);
        }

        return statusOk("product category updated");
    }
}
