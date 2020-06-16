package pl.agh.product.catalog.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.agh.product.catalog.application.dto.CategoryRequestDTO;
import pl.agh.product.catalog.application.service.CategoryService;
import pl.agh.product.catalog.common.exception.CustomException;
import pl.agh.product.catalog.common.response.ListResponse;
import pl.agh.product.catalog.mysql.entity.Category;

import javax.validation.Valid;
import java.net.URI;

import static pl.agh.product.catalog.common.util.ResponseFormat.APPLICATION_JSON;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = CategoryController.PREFIX)
public class CategoryController {

    static final String PREFIX = "/categories";

    private final CategoryService categoryService;

    @Secured("ROLE_ADMIN")
    @PostMapping(consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
    public ResponseEntity<?> addCategory(@RequestBody @Valid CategoryRequestDTO categoryRequestDTO) throws CustomException {
        Category createdCategory = categoryService.add(categoryRequestDTO.getName());
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

    @GetMapping(produces = APPLICATION_JSON)
    public ResponseEntity<?> findCategories(@RequestParam int limit, @RequestParam int offset) {
        ListResponse categories = categoryService.findCategories(limit, offset);
        return ResponseEntity.ok(categories);
    }

    @GetMapping(value = "{id}", produces = APPLICATION_JSON)
    public ResponseEntity<Category> getCategory(@PathVariable("id") Long id) {
        Category category = categoryService.find(id);
        if (category == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(category);
        }
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping(value = "{id}", produces = APPLICATION_JSON)
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        Category deletedCategory = categoryService.delete(id);
        if (deletedCategory == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
