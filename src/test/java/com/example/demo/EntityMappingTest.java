package com.example.demo;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EntityMappingTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void testBookSave() {
        Book book = new Book();
        book.setTitle("Test Book");
        bookRepository.save(book);
        assert bookRepository.findAll().size() > 0;
    }
}
