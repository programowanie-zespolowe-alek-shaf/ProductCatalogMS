package pl.agh.product.catalog.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.agh.product.catalog.application.service.CategoryService;
import pl.agh.product.catalog.common.exception.CustomException;
import pl.agh.product.catalog.common.response.ListResponse;
import pl.agh.product.catalog.mysql.entity.Category;

import java.net.URI;

import static pl.agh.product.catalog.common.util.ResponseFormat.APPLICATION_JSON;

@RestController
@RequestMapping(value = CategoryController.PREFIX)
public class CategoryController {

    final static String PREFIX = "/categories";

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.POST, produces = {APPLICATION_JSON})
    public ResponseEntity addCategory(@RequestParam String name) throws CustomException {
        Category createdCategory = categoryService.add(name);
        if (createdCategory == null) {
            return ResponseEntity.notFound().build();
        } else {
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdCategory.getId())
                    .toUri();

            return ResponseEntity.created(uri)
                    .body(createdCategory);
        }
    }

    @RequestMapping(method = RequestMethod.GET, produces = {APPLICATION_JSON})
    public ResponseEntity findCategories() {
        ListResponse categories = categoryService.findCategories();
        return ResponseEntity.ok(categories);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = {APPLICATION_JSON})
    public ResponseEntity<Category> getCategory(@PathVariable("id") Long id) {
        Category category = categoryService.find(id);
        if (category == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(category);
        }
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = {APPLICATION_JSON})
    public ResponseEntity deleteCategory(@PathVariable Long id) {
        Category deletedCategory = categoryService.delete(id);
        if (deletedCategory == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
