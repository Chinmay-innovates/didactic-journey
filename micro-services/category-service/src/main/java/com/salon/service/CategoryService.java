package com.salon.service;

import com.salon.dto.SalonDTO;
import com.salon.model.Category;

import java.util.Set;

public interface CategoryService {

    Category saveCategory(Category category, SalonDTO salonDTO);

    Set<Category> getAllCategoriesBySalonId(Long salonId);

    Category getCategoryById(Long categoryId) throws Exception;

    void deleteCategoryById(Long categoryId, Long salonId) throws Exception;
}
