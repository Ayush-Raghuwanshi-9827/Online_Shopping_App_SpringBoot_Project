package com.ecomerse.Online_Shopping_App.Controller;

import com.ecomerse.Online_Shopping_App.model.Category;
import com.ecomerse.Online_Shopping_App.model.Product;
import com.ecomerse.Online_Shopping_App.repository.ProductRepository;
import com.ecomerse.Online_Shopping_App.service.CategoryService;
import com.ecomerse.Online_Shopping_App.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    public String index(){
        return "index";
    }


    @GetMapping("/register")
    public String register(){
        return "register";
    }


    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/products")
    public  String product(Model model, @RequestParam(value = "category", defaultValue = "") String category){

        System.out.println("Category" + category);
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

    @GetMapping("/product")
    public  String view_product(){
        return "view_product";
    }

}
