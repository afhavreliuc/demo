package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.model.Author;
import com.example.demo.model.Category;
import com.example.demo.service.AuthorService;
import com.example.demo.service.BookService;
import com.example.demo.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.HashSet;

@Controller
public class BookWebController {
    private final BookService bookService;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    public BookWebController(BookService bookService, AuthorService authorService, CategoryService categoryService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    @GetMapping("/books")
    public String listBooks(Model model,
                           @RequestParam(required = false) String title,
                           @RequestParam(required = false) Long authorId,
                           @RequestParam(required = false) Long categoryId,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "5") int size,
                           @RequestParam(defaultValue = "title") String sort,
                           @RequestParam(defaultValue = "asc") String dir) {
        Pageable pageable = PageRequest.of(page, size, dir.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending());
        Page<Book> bookPage = bookService.findAll(pageable);
        var books = bookPage.getContent();
        if (title != null && !title.isEmpty()) {
            books = books.stream().filter(b -> b.getTitle() != null && b.getTitle().toLowerCase().contains(title.toLowerCase())).toList();
        }
        if (authorId != null) {
            books = books.stream().filter(b -> b.getAuthors() != null && b.getAuthors().stream().anyMatch(a -> a.getId().equals(authorId))).toList();
        }
        if (categoryId != null) {
            books = books.stream().filter(b -> b.getCategory() != null && b.getCategory().getId().equals(categoryId)).toList();
        }
        model.addAttribute("books", books);
        model.addAttribute("bookPage", bookPage);
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("filterTitle", title);
        model.addAttribute("filterAuthorId", authorId);
        model.addAttribute("filterCategoryId", categoryId);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        return "books";
    }

    @PostMapping("/books")
    public String addBook(@Valid @ModelAttribute("book") Book book, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("books", bookService.findAll());
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("categories", categoryService.findAll());
            return "books";
        }
        if (book.getAuthorIds() != null) {
            var authors = new HashSet<Author>();
            for (Long authorId : book.getAuthorIds()) {
                authorService.findAll().stream()
                    .filter(a -> a.getId().equals(authorId))
                    .findFirst().ifPresent(authors::add);
            }
            book.setAuthors(authors);
        }
        if (book.getCategoryId() != null) {
            categoryService.findAll().stream()
                .filter(c -> c.getId().equals(book.getCategoryId()))
                .findFirst().ifPresent(book::setCategory);
        }
        bookService.save(book);
        return "redirect:/books";
    }

    @PostMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }

    @PostMapping("/books/edit/{id}")
    public String editBook(@PathVariable Long id, @ModelAttribute Book book) {
        var existingOpt = bookService.findById(id);
        if (existingOpt.isPresent()) {
            var existing = existingOpt.get();
            existing.setTitle(book.getTitle());
            // Setează autorii
            if (book.getAuthorIds() != null) {
                var authors = new HashSet<Author>();
                for (Long authorId : book.getAuthorIds()) {
                    authorService.findAll().stream()
                        .filter(a -> a.getId().equals(authorId))
                        .findFirst().ifPresent(authors::add);
                }
                existing.setAuthors(authors);
            }
            // Setează categoria
            if (book.getCategoryId() != null) {
                categoryService.findAll().stream()
                    .filter(c -> c.getId().equals(book.getCategoryId()))
                    .findFirst().ifPresent(existing::setCategory);
            }
            bookService.save(existing);
        }
        return "redirect:/books";
    }
} 