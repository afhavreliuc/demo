package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class BookDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String isbn;
    private int numberOfPages;

    @OneToOne
    @JoinColumn(name = "book_id")
    private Book book;

    public BookDetails() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public int getNumberOfPages() { return numberOfPages; }
    public void setNumberOfPages(int numberOfPages) { this.numberOfPages = numberOfPages; }
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
} 