package com.ecomerse.Online_Shopping_App.service;

import com.ecomerse.Online_Shopping_App.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    public Product saveProduct(Product product);

    public List<Product> getAllProduct();

    public Boolean deleteById(Integer id);

    public Product getById(Integer id);

    public Product updateProduct(Product product, MultipartFile image) throws IOException;

    public List<Product> getAllIsActiveProduct(String category);
}
