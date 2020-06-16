package pl.agh.product.catalog.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.agh.product.catalog.common.exception.BadRequestException;
import pl.agh.product.catalog.common.response.ListResponse;
import pl.agh.product.catalog.common.util.ListUtil;
import pl.agh.product.catalog.mysql.entity.Category;
import pl.agh.product.catalog.mysql.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category add(String name) throws BadRequestException {
        if (categoryRepository.existsByNameIgnoreCase(name)) {
            throw new BadRequestException("category already exists");
        }
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    public ListResponse findCategories(int limit, int offset) {
        List<Category> categories = categoryRepository.findAll();
        int count = categories.size();
        categories = ListUtil.clampedSublist(categories, limit, offset);
        return new ListResponse(categories, count);
    }

    public Category find(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category delete(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            return null;
        }
        categoryRepository.delete(category.get());
        return category.get();
    }
}
