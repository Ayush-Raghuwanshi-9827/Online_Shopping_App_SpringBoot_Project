package com.ecomerse.Online_Shopping_App.repository;

import com.ecomerse.Online_Shopping_App.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    public List<Product> findByIsActiveTrue();

    public List<Product> findByCategory(String category);
}
