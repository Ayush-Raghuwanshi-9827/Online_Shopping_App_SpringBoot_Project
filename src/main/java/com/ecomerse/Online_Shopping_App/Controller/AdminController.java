package com.ecomerse.Online_Shopping_App.Controller;

import com.ecomerse.Online_Shopping_App.model.Category;
import com.ecomerse.Online_Shopping_App.model.Product;
import com.ecomerse.Online_Shopping_App.model.UserDetails;
import com.ecomerse.Online_Shopping_App.service.CartService;
import com.ecomerse.Online_Shopping_App.service.CategoryService;
import com.ecomerse.Online_Shopping_App.service.ProductService;
import com.ecomerse.Online_Shopping_App.service.UserDetailsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CartService cartService;

    @GetMapping("/")
    public String index(){
        return "admin/index";
    }

    @GetMapping("/loadAddProduct")
    public String loadAddProduct(Model model){
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("categories", categories);
        return "admin/add_product";
    }

    @GetMapping("/category")
    public String category(Model model){
        model.addAttribute("categorys", categoryService.getAllCategory());
        return "admin/category";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        String imageName = file != null ? file.getOriginalFilename() : "Default.jpg";
        Boolean existCategory = categoryService.existCategory(category.getName());
        category.setImageName(imageName);
        if (existCategory){
            session.setAttribute("errorMsg", "Category name already exists");
        }else {
            Category saveCategory = categoryService.saveCategory(category);
            if (ObjectUtils.isEmpty(saveCategory)){
                session.setAttribute("errorMsg", "Not saved! Internal Server Error");
            }else {
               File saveFile = new ClassPathResource("static/img").getFile();
               Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+"Category",File.separator+file.getOriginalFilename());
               Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
               session.setAttribute("succMsg", "Saved Category Successfully");
            }
        }
        return "redirect:/admin/category";
    }

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable int id, HttpSession session){

        Boolean deleteCategory = categoryService.deletCategory(id);
        if (deleteCategory){
            session.setAttribute("succMsg", "Category Deleted Successfully");
        }else {
            session.setAttribute("errorMsg","Something Wrong on Server!");
        }
        return "redirect:/admin/category";
    }

    @GetMapping("/loadEditCategory/{id}")
    public String loadEditCategory(@PathVariable int id, Model model){
        model.addAttribute("category", categoryService.getCategoryById(id));
        return "admin/edit_category";
    }

    @PostMapping("/updateCategory")
    public String updateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        Category category1 = categoryService.getCategoryById(category.getId());
        String imageName = file.isEmpty() ? category1.getImageName() : file.getOriginalFilename();

        if (!ObjectUtils.isEmpty(category1)) {
            category1.setName(category.getName());
            category1.setIsActive(category.getIsActive());
            category1.setImageName(imageName);
        }
        Category updateCategory = categoryService.saveCategory(category1);
        if (!ObjectUtils.isEmpty(updateCategory)) {
            if (!file.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+"Category",File.separator+file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            session.setAttribute("succMsg", "Category updated successfully");
        } else {
            session.setAttribute("errorMsg", "Something went wrong on the server!");
        }
        return "redirect:/admin/loadEditCategory/" + category.getId();
    }


    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product, HttpSession session, @RequestParam("file") MultipartFile image) throws IOException {
        String imageName = image.isEmpty() ? "Default.jpg" : image.getOriginalFilename();
        product.setImage(imageName);
        product.setDiscount(0);
        product.setDiscountPrice(product.getPrice());
        Product saveProduct =  productService.saveProduct(product);
        if (!ObjectUtils.isEmpty(saveProduct)){
            File saveFile = new ClassPathResource("static/img").getFile();
            Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+"product",File.separator+image.getOriginalFilename());
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            session.setAttribute("succMsg", "Product Saved Successfully");
        }else {
            session.setAttribute("erroeMsg", "Something Wrong On server!");
        }
        return "redirect:/admin/loadAddProduct";
    }

    @GetMapping("/products")
    public String loadViewProduct(Model model){
        List<Product> productList = productService.getAllProduct();
        model.addAttribute("products", productList);
        return "admin/products";
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable int id, HttpSession session){
        Boolean deleteProduct = productService.deleteById(id);
        if (deleteProduct){
            session.setAttribute("succMsg", "Product Deleted Successfully");
        }else {
            session.setAttribute("errorMsg","Something Wrong on Server!");
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/editProduct/{id}")
    public String loadEditProduct(@PathVariable int id, Model model){
     model.addAttribute("product", productService.getById(id));
     model.addAttribute("categories", categoryService.getAllCategory());
        return "admin/edit_product";
    }

    @PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image, HttpSession session) throws IOException {

        if ((product.getDiscount() < 0) || (product.getDiscount() > 100)) {
            session.setAttribute("errorMsg", "Invalid Discount");
            return "redirect:/admin/editProduct/" + product.getId();
        } else {
            Product updateProduct = productService.updateProduct(product, image);

            if (!ObjectUtils.isEmpty(updateProduct)){
               session.setAttribute("succMsg", "Product updated Successfully");
            }
            else
                session.setAttribute("errorMsg", "Something Wrong on server!");
        }

        return "redirect:/admin/editProduct/"+product.getId();
    }

    @GetMapping("/addAdmin")
    public String addAdmin(){
        return "/admin/addAdmin";
    }

    @PostMapping("/addAdmin")
    public String changeRoleToAdmin(@RequestParam String email, HttpSession session) {
        UserDetails user = userDetailsService.getUserByEmail(email);
        if (ObjectUtils.isEmpty(user)) {
            session.setAttribute("errorMsg", "Invalid Email, Please enter the valid registered Email");
        } else {
            user.setRole("ROLE_ADMIN");
            userDetailsService.updateUser(user);
            session.setAttribute("succMsg", "New Admin created Successfully");
        }
        return "redirect:/admin/addAdmin";
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
}
