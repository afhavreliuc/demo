package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Category cat1 = new Category();
        cat1.setId(1L);
        cat1.setName("Ficțiune");
        Category cat2 = new Category();
        cat2.setId(2L);
        cat2.setName("Fantasy");
        when(categoryRepository.findAll()).thenReturn(List.of(cat1, cat2));
        List<Category> result = categoryService.findAll();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Ficțiune");
        assertThat(result.get(1).getName()).isEqualTo("Fantasy");
    }
} 