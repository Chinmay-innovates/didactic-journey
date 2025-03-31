package com.salon.service.impl;

import com.salon.dto.SalonDTO;
import com.salon.model.Category;
import com.salon.repository.CategoryRepository;
import com.salon.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category saveCategory(Category category, SalonDTO salonDTO) {
        Category newCategory = new Category();
        newCategory.setName(category.getName());
        newCategory.setImage(category.getImage());
        newCategory.setSalonId(salonDTO.getId());
        newCategory.setImage(category.getImage());

        return categoryRepository.save(newCategory);
    }

    @Override
    public Set<Category> getAllCategoriesBySalonId(Long salonId) {
        return categoryRepository.findBySalonId(salonId);
    }

    @Override
    public Category getCategoryById(Long categoryId) throws Exception {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            throw new Exception("Category doesn't exist with Id " + categoryId);
        }
        return category;
    }

    @Override
    public void deleteCategoryById(Long categoryId, Long salonId) throws Exception {
        Category category = getCategoryById(categoryId);
        if(!category.getSalonId().equals(salonId)) {
            throw new Exception("You don't have permit to delete this category");
        }
        categoryRepository.deleteById(categoryId);
    }
}
