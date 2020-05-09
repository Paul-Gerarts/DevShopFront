package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.StatusNotification;

public interface CategoryService {

    StatusNotification delete(Long id);

    StatusNotification setNewCategories(Long categoryToDelete, Long categoryToSet);

    StatusNotification updateCategory(String categoryToUpdate, Long categoryToSet);
}
