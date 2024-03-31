package com.example.supplier.category;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/categories", produces = "application/json")
@AllArgsConstructor
public class CategoryControllerImpl implements CategoryController{
    private final CategoryService categoryService;


    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryDto categoryDto) {
        Category category = mapToEntity(categoryDto);
        Category createdCategory = categoryService.createCategory(category);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapToDto(createdCategory));
    }


    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        List<Category> allCategories = categoryService.getAllCategories();
        if (allCategories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(allCategories.stream()
                .map(this::mapToDto)
                .toList());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        return ResponseEntity
                .ok()
                .body(mapToDto(categoryService.getCategoryById(id)));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id,
                                            @RequestBody CategoryDto categoryDto) {
        Category category = mapToEntity(categoryDto);
        Category updatedCategory = categoryService.updateCategory(id, category);
        return ResponseEntity
                .ok()
                .body(mapToDto(updatedCategory));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    private Category mapToEntity(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        return category;
    }

    private CategoryDto mapToDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }
}