package com.example.demo.service;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    public BookServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveAndFindBook() {
        Book book = new Book();
        book.setTitle("Service Test Book");
        when(bookRepository.save(book)).thenReturn(book);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book saved = bookService.save(book);
        assertThat(saved.getTitle()).isEqualTo("Service Test Book");
        Optional<Book> found = bookService.findById(1L);
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Service Test Book");
    }
} 