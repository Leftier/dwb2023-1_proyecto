package com.product.api.service;

import com.product.api.dto.ApiResponse;
import com.product.api.entity.ProductImage;
import com.product.api.repository.RepoProductImage;
import com.product.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import static com.product.api.dto.ApiResponse.statusBadRequest;
import static com.product.api.dto.ApiResponse.statusOk;

@Service
@PropertySource("classpath:configuration/path.config")
public class SvcProductImageImp implements SvcProductImage {

    private final RepoProductImage repo;

    @Autowired
    public SvcProductImageImp(RepoProductImage _repo) {
        this.repo = _repo;
    }

    @Value("${product.images.path}")
    private String path;

    @Override
    public ApiResponse<List<ProductImage>> getProductImages(Integer id_product) {
        return statusOk(repo.findByProductId(id_product));
    }

    @Override
    public ApiResponse<String> createProductImage(ProductImage productImage) {
        try {
            File folder = new File(path + "/" + productImage.getProduct_id());
            if (!folder.exists())
                folder.mkdirs();

            String file = path + productImage.getProduct_id() + "/img_" + new Date().getTime() + ".bmp";

            byte[] data = Base64.getMimeDecoder().decode(productImage.getImage().substring(productImage.getImage().indexOf(",") + 1));
            try (OutputStream stream = new FileOutputStream(file)) {
                stream.write(data);
            }

            productImage.setStatus(1);
            productImage.setImage(productImage.getProduct_id() + "/img_" + new Date().getTime() + ".bmp");

            repo.save(productImage);
            return statusOk("product image created");

        } catch (Exception e) {
            throw statusBadRequest("product image can not be created" + ". " + e.getLocalizedMessage());
        }
    }

    @Override
    public ApiResponse<String> deleteProductImage(Integer id) {
        if (repo.deleteProductImage(id) <= 0)
            throw statusBadRequest("product image cannot be deleted");

        return statusOk("product image removed");
    }

}
