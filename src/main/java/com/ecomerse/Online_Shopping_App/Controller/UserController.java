package com.ecomerse.Online_Shopping_App.Controller;

import com.ecomerse.Online_Shopping_App.Service_Imple.CategoryServiceImple;
import com.ecomerse.Online_Shopping_App.model.Cart;
import com.ecomerse.Online_Shopping_App.model.Category;
import com.ecomerse.Online_Shopping_App.model.UserDetails;
import com.ecomerse.Online_Shopping_App.service.CartService;
import com.ecomerse.Online_Shopping_App.service.UserDetailsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private CategoryServiceImple categoryService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CartService cartService;

    @GetMapping("/")
    public String home(){
        return "User/home";
    }

    @ModelAttribute
    public void getUserDetails(Principal p, Model model){
        if (p!=null){
            var email = p.getName();
            var userDtl = userDetailsService.getUserByEmail(email);
            model.addAttribute("user", userDtl);
        }
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("categories", categories);
    }

    @GetMapping("/addCart")
    public String addCart(@RequestParam Integer pid, @RequestParam Integer uid, HttpSession session){
        Cart saveCart = cartService.saveCart(pid, uid);
        if (ObjectUtils.isEmpty(saveCart)){
            session.setAttribute("errorMsg", "Product add to cart failed");
        }else {
            session.setAttribute("succMsg", "Product added to card successfully");
        }
        System.out.println("Im here!");
        return "redirect:/product/" + pid;
    }

    @GetMapping("/cart")
    public String UserCart(Principal p, Model model){
        UserDetails userDetails = getLoggedInUserDetails(p);
        List<Cart> carts = cartService.getCartByUser(userDetails.getId());
        model.addAttribute("carts" , carts);
        if (!carts.isEmpty()){
            Double totalOrderPrice = carts.get(carts.size() -1).getTotalOrderAmount();
            model.addAttribute("totalPrice", totalOrderPrice);
        }
        return "User/Cart";
    }

    private UserDetails getLoggedInUserDetails(Principal p) {
    return userDetailsService.getUserByEmail(p.getName());
    }


    @GetMapping("/cartQuantityUpdate")
    public String updateCartQuantity(@RequestParam("sy") String sy, @RequestParam("cid") Integer cid){
        cartService.updateQuantity(sy, cid);
        return "redirect:/user/cart";
    }
}
