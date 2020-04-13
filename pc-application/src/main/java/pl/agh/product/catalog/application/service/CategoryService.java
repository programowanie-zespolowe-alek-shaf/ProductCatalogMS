package pl.agh.product.catalog.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.agh.product.catalog.common.exception.BadRequestException;
import pl.agh.product.catalog.common.response.ListResponse;
import pl.agh.product.catalog.mysql.entity.Category;
import pl.agh.product.catalog.mysql.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category add(String name) throws BadRequestException {
        if (categoryRepository.existsByNameIgnoreCase(name)) {
            throw new BadRequestException("category already exists");
        }
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    public ListResponse findCategories() {
        List<Category> categories = categoryRepository.findAll();
        return new ListResponse(categories, categories.size());
    }

    public Category find(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category delete(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (!category.isPresent()) {
            return null;
        }
        categoryRepository.delete(category.get());
        return category.get();
    }
}
