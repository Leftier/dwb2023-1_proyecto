package com.product.api.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.product.api.entity.Product;

import java.util.List;

@Repository
public interface RepoProduct extends JpaRepository<Product, Integer> {

    @Query(value = "SELECT * FROM product WHERE category_id = :category_id AND status = 1", nativeQuery = true)
    List<Product> findByCategoryId(@Param("category_id") Integer category_id);

    @Query(value = "SELECT * FROM product WHERE gtin = :gtin", nativeQuery = true)
    Product findByGtin(@Param("gtin") String gtin);
    @Query(value = "SELECT * FROM product WHERE gtin = :gtin AND status = :status", nativeQuery = true)
    Product findByGtinAndStatus(@Param("gtin") String gtin, @Param("status") Integer status);

    @Modifying
    @Transactional
    @Query(value = "UPDATE product "
            + "SET gtin = :gtin, "
            + "product = :product, "
            + "description = :description, "
            + "price = :price, "
            + "stock = :stock, "
            + "status = 1, "
            + "category_id = :category_id "
            + "WHERE product_id = :product_id", nativeQuery = true)
    void updateProduct(
            @Param("product_id") Integer product_id,
            @Param("gtin") String gtin,
            @Param("product") String product,
            @Param("description") String description,
            @Param("price") Double price,
            @Param("stock") Integer stock,
            @Param("category_id") Integer category_id
    );

    @Modifying
    @Transactional
    @Query(value = "UPDATE product SET status = 0 WHERE product_id = :product_id AND status = 1", nativeQuery = true)
    Integer deleteProduct(@Param("product_id") Integer product_id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE product SET stock = :stock WHERE gtin = :gtin AND status = 1", nativeQuery = true)
    void updateProductStock(@Param("gtin") String gtin, @Param("stock") Integer stock);

    @Modifying
    @Transactional
    @Query(value = "UPDATE product SET category_id = :category_id WHERE gtin = :gtin AND status = 1", nativeQuery = true)
    Integer updateProductCategory(String gtin, Integer category_id);
}
