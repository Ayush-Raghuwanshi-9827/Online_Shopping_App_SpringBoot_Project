package com.ecomerse.Online_Shopping_App.service;

import com.ecomerse.Online_Shopping_App.model.Category;

import java.util.List;

public interface CategoryService {

public Category saveCategory(Category category);

public List<Category> getAllCategory();

public  Boolean existCategory(String name);

public Boolean deletCategory(int id);

public Category getCategoryById(int id);

public List<Category> getAllActiveCategory();
}
