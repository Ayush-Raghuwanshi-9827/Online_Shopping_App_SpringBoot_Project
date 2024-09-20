package com.ecomerse.Online_Shopping_App.Service_Imple;

import com.ecomerse.Online_Shopping_App.model.Product;
import com.ecomerse.Online_Shopping_App.repository.ProductRepository;
import com.ecomerse.Online_Shopping_App.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public Product saveProduct(Product product) {

        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public Boolean deleteById(Integer id) {
        Product product = productRepository.findById(id).orElse(null);
        if (!ObjectUtils.isEmpty(product)){
            productRepository.delete(product);
            return true;
        }
        return false;
    }

    @Override
    public Product getById(Integer id) {
       return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product updateProduct(Product product, MultipartFile image) throws IOException {
        Product product1 = getById(product.getId());
        String imageName = image.isEmpty() ? product1.getImage() : image.getOriginalFilename();

        product1.setTitle(product.getTitle());
        product1.setDescription(product.getDescription());
        product1.setPrice(product.getPrice());
        product1.setStock(product.getStock());
        product1.setCategory(product.getCategory());
        product1.setImage(imageName);
        product1.setIsActive(product.getIsActive());
        product1.setDiscount(product.getDiscount());

        Double discount = product.getPrice() * (product1.getDiscount() / 100.0);
        Double discountPrice = product.getPrice() - discount;
        product1.setDiscountPrice(discountPrice);

        Product updateProduct = productRepository.save(product1);

        if (!ObjectUtils.isEmpty(updateProduct)) {
            if (!image.isEmpty()) {
                // Define the path for saving images
                String uploadDir = "src/main/resources/static/img/product/"; // Path to save files
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs(); // Create the directory if it doesn't exist
                }

                // Save the file
                Path path = Paths.get(dir.getAbsolutePath() + File.separator + image.getOriginalFilename());
                Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            return product1; // Return the updated product
        }
        return null;
    }


    @Override
    public List<Product> getAllIsActiveProduct(String category) {
       List<Product> productList = null;
        if (ObjectUtils.isEmpty(category))
            productList = productRepository.findByIsActiveTrue();
       else
            productList = productRepository.findByCategory(category);

        return productList;
    }

    @Override
    public List<Product> getSearchProduct(String ch) {
        return productRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch,ch);
    }
}
