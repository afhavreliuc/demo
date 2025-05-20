package com.example.demo.controller;

import com.example.demo.model.Loan;
import com.example.demo.model.Book;
import com.example.demo.model.Reader;
import com.example.demo.service.BookService;
import com.example.demo.service.LoanService;
import com.example.demo.service.ReaderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LoanWebController {
    private final LoanService loanService;
    private final BookService bookService;
    private final ReaderService readerService;

    public LoanWebController(LoanService loanService, BookService bookService, ReaderService readerService) {
        this.loanService = loanService;
        this.bookService = bookService;
        this.readerService = readerService;
    }

    @GetMapping("/loans")
    public String listLoans(Model model) {
        List<Loan> loans = loanService.findAll();
        List<Book> availableBooks = bookService.findAll().stream()
                .filter(book -> loanService.isBookAvailable(book))
                .collect(Collectors.toList());
        model.addAttribute("loans", loans);
        model.addAttribute("availableBooks", availableBooks);
        model.addAttribute("readers", readerService.findAll());
        model.addAttribute("loan", new Loan());
        return "loans";
    }

    @PostMapping("/loans")
    public String addLoan(@ModelAttribute Loan loan) {
        if (loanService.isBookAvailable(loan.getBook())) {
            loan.setLoanDate(LocalDate.now().toString());
            loanService.save(loan);
        }
        return "redirect:/loans";
    }

    @PostMapping("/loans/return/{id}")
    public String returnLoan(@PathVariable Long id) {
        loanService.markAsReturned(id);
        return "redirect:/loans";
    }
} 