package com.ecomerse.Online_Shopping_App.repository;

import com.ecomerse.Online_Shopping_App.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    public Cart findByProductIdAndUserId(Integer pid, Integer uid);

    public  Integer countByUserId(Integer userId);

    public List<Cart> findByUserId(Integer userId);
}
