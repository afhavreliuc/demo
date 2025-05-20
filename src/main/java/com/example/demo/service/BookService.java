package com.example.demo.service;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        log.info("[BookService] findAll called");
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Long id) {
        log.info("[BookService] findById called with id={}", id);
        return bookRepository.findById(id);
    }

    public Book save(Book book) {
        log.info("[BookService] save called for book with title='{}'", book.getTitle());
        return bookRepository.save(book);
    }

    public void deleteById(Long id) {
        log.info("[BookService] deleteById called with id={}", id);
        bookRepository.deleteById(id);
    }

    public Page<Book> findAll(Pageable pageable) {
        log.info("[BookService] findAll (paged) called: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        return bookRepository.findAll(pageable);
    }
} 