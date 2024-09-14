package com.ecomerse.Online_Shopping_App.Controller;

import com.ecomerse.Online_Shopping_App.Service_Imple.UserDetailsServiceImpl;
import com.ecomerse.Online_Shopping_App.model.Category;
import com.ecomerse.Online_Shopping_App.model.Product;
import com.ecomerse.Online_Shopping_App.model.UserDetails;
import com.ecomerse.Online_Shopping_App.repository.ProductRepository;
import com.ecomerse.Online_Shopping_App.service.CartService;
import com.ecomerse.Online_Shopping_App.service.CategoryService;
import com.ecomerse.Online_Shopping_App.service.ProductService;
import com.ecomerse.Online_Shopping_App.service.UserDetailsService;
import com.ecomerse.Online_Shopping_App.util.CommonUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private CartService cartService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String index(Model model){
        List<Category> activeCategory = categoryService.getAllActiveCategory().stream().limit(6).toList();
        List<Product> activeProduct = productService.getAllIsActiveProduct("").stream().limit(12).toList();

        model.addAttribute("categories", activeCategory);
        model.addAttribute("products", activeProduct);
        return "index";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/singin")
    public String login(){
        return "login";
    }

    @GetMapping("/products")
    public  String product(Model model, @RequestParam(value = "category", defaultValue = "") String category){
        List<Category> activeCategories = categoryService.getAllActiveCategory();
        for (Category c : activeCategories) {
            System.out.println(c.getName());
        }
        List<Product> activeProducts = productService.getAllIsActiveProduct(category);
        model.addAttribute("products", activeProducts);
        model.addAttribute("categories", activeCategories);
        model.addAttribute("paramValue", category);
        return "product";
    }

    @GetMapping("/product/{id}")
    public  String view_product(@PathVariable int id, Model model){
        Product product = productService.getById(id);
        model.addAttribute("details", product);

       return "view_product";
    }

    @PostMapping("/SaveUser")
    public String SaveUserDetails(@ModelAttribute UserDetails userDetails, HttpSession session){
        if(userDetailsService.getUserByEmail(userDetails.getEmail()) == null) {
            UserDetails savedUser = userDetailsService.SaveUser(userDetails);
            if (!ObjectUtils.isEmpty(savedUser)) {
                session.setAttribute("succMsg", "User Details Saved Successfully");
            } else {
                session.setAttribute("errorMsg", "Something Wrong On server!");
            }
        }else {
            session.setAttribute("errorMsg", "This email is already registered");
        }
        return "redirect:/register";
    }

    @ModelAttribute
    public void getUserDetails(Principal p, Model model){
        if (p!=null){
            var email = p.getName();
            var userDtl = userDetailsService.getUserByEmail(email);
            model.addAttribute("user", userDtl);
            Integer cartCount=  cartService.getCountCart(userDtl.getId());
            model.addAttribute("countCart", cartCount);
        }else {
            model.addAttribute("user", null);
        }

        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("categories", categories);
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage(){
        return "forgot_password.html";
    }

    @PostMapping("/forgot-password")
    public String processForgetPasswordPage(@RequestParam String email, HttpSession session, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        var userEmail = userDetailsService.getUserByEmail(email);
        if (ObjectUtils.isEmpty(userEmail)){
            session.setAttribute("errorMsg", "Invalid Email");
        }else {
            String resetToken = UUID.randomUUID().toString();
            userDetailsService.updateUserResetToken(userEmail, resetToken);

            String url = CommonUtil.generateUrl(request)+"/reset-password?token="+resetToken;

            Boolean sendMail = commonUtil.sendEmail(url,email);
            if (sendMail){
                session.setAttribute("succMsg", "Please Check Your Mail.. Password Reset Link sent");
            }else {
                session.setAttribute("errorMsg", "Something wrong on server Mail not sent");
            }
        }
        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam String token, HttpSession session, Model model){
        UserDetails userByToken = userDetailsService.getUserByToken(token);

        if (ObjectUtils.isEmpty(userByToken)){
            model.addAttribute("msg", "Your token is invalid or expired");
            return "message";
        }
        model.addAttribute("token", token);
        return "reset_password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token, HttpSession session, @RequestParam String password, Model model){
        UserDetails userByToken = userDetailsService.getUserByToken(token);

        if (ObjectUtils.isEmpty(userByToken)){
            model.addAttribute("msg", "Your link is invalid or expired");
            return "message";
        }else {
            userByToken.setPassword(passwordEncoder.encode(password));
            userByToken.setResetToken(null);
            userDetailsService.updateUser(userByToken);
            model.addAttribute("msg", "Password Changed Successfully");
        }

        return "message";
    }

    @GetMapping("/search")
    public String SearchProduct(@RequestParam String ch, Model model){

        List<Product> searchProducts = productService.getSearchProduct(ch);
        List<Category> activeCategories = categoryService.getAllActiveCategory();
        model.addAttribute("categories", activeCategories);
        model.addAttribute("products", searchProducts);
        return "product";
    }




}
