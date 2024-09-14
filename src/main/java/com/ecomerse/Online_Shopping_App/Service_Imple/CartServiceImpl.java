package com.ecomerse.Online_Shopping_App.Service_Imple;

import com.ecomerse.Online_Shopping_App.model.Cart;
import com.ecomerse.Online_Shopping_App.model.Product;
import com.ecomerse.Online_Shopping_App.model.UserDetails;
import com.ecomerse.Online_Shopping_App.repository.CartRepository;
import com.ecomerse.Online_Shopping_App.repository.ProductRepository;
import com.ecomerse.Online_Shopping_App.repository.UserDtlRepository;
import com.ecomerse.Online_Shopping_App.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserDtlRepository userDtlRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Cart saveCart(Integer productId, Integer userID) {

        UserDetails userDetails = userDtlRepository.findById(userID).get();
        Product product = productRepository.findById(productId).get();

        Cart cart = null;

        Cart cartStatus = cartRepository.findByProductIdAndUserId(productId, userID);
        if (ObjectUtils.isEmpty(cartStatus)){

            cart = new Cart();
            cart.setUser(userDetails);
            cart.setProduct(product);
            cart.setQuantity(1);
            cart.setTotalPrice(1*product.getDiscountPrice());

        }else {
            cart = cartStatus;
            cart.setQuantity(cart.getQuantity() +1);
            cart.setTotalPrice(cart.getQuantity()*cart.getProduct().getDiscountPrice());
        }
        Cart savedCart = cartRepository.save(cart);
        return savedCart;
    }

    @Override
    public List<Cart> getCartByUser(Integer userID) {
        List<Cart> carts = cartRepository.findByUserId(userID);
        List<Cart> updateCarts = new ArrayList<>();
        double totalOrderPrice = 0.0;
        for (Cart c : carts){
            double totalPrice  = (c.getProduct().getDiscountPrice()*c.getQuantity());
            c.setTotalPrice(totalPrice);
            totalOrderPrice += totalPrice;
            c.setTotalOrderAmount(totalOrderPrice);
            updateCarts.add(c);
        }
        return updateCarts;
    }

    @Override
    public Integer getCountCart(Integer userId) {
        return cartRepository.countByUserId(userId);
    }

    @Override
    public void updateQuantity(String sy, Integer cid) {
        Cart cartDetails = cartRepository.findById(cid).get();
        Integer updateQuantity;
        if (Objects.equals(sy, "de")) {
            updateQuantity = cartDetails.getQuantity() - 1;
            if (updateQuantity <= 0) {
                cartRepository.delete(cartDetails);
            }else {
                cartDetails.setQuantity(updateQuantity);
                cartRepository.save(cartDetails);
            }
        }else {
            updateQuantity = cartDetails.getQuantity()+1;
            cartDetails.setQuantity(updateQuantity);
            cartRepository.save(cartDetails);
        }
    }


}
