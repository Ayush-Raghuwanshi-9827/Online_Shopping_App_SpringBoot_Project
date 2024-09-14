package com.ecomerse.Online_Shopping_App.service;

import com.ecomerse.Online_Shopping_App.model.Cart;

import java.util.List;

public interface CartService {

    public Cart saveCart(Integer productId, Integer userID);

    public List<Cart> getCartByUser(Integer userID);

    public Integer getCountCart(Integer userId);

    public void updateQuantity(String sy, Integer cid);
}
