package com.ecomerse.Online_Shopping_App.Service_Imple;

import com.ecomerse.Online_Shopping_App.model.Category;
import com.ecomerse.Online_Shopping_App.repository.CategoryRepository;
import com.ecomerse.Online_Shopping_App.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class CategoryServiceImple implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Boolean existCategory(String name) {
        return categoryRepository.existsByName(name);
    }

    @Override
    public Boolean deletCategory(int id) {
         Category category = categoryRepository.findById(id).orElse(null);
         if (!ObjectUtils.isEmpty(category)){
             categoryRepository.delete(category);
             return true;
         }
         return false;
    }

    @Override
    public Category getCategoryById(int id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public List<Category> getAllActiveCategory() {
        List<Category> activeCategories = categoryRepository.findByIsActiveTrue();
        return activeCategories;
    }
}
