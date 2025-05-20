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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @ModelAttribute("loan")
    public Loan getLoan() {
        return new Loan();
    }

    @GetMapping("/loans")
    public String listLoans(Model model) {
        try {
            List<Loan> loans = loanService.findAll();
            List<Book> availableBooks = bookService.findAll().stream()
                    .filter(book -> loanService.isBookAvailable(book))
                    .collect(Collectors.toList());

            model.addAttribute("loans", loans);
            model.addAttribute("availableBooks", availableBooks);
            model.addAttribute("readers", readerService.findAll());
            return "loans";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "A apărut o eroare la încărcarea împrumuturilor: " + e.getMessage());
            return "loans";
        }
    }

    @PostMapping("/loans")
    public String addLoan(@ModelAttribute("loan") Loan loan, RedirectAttributes redirectAttributes) {
        try {
            if (loan.getBook() == null || loan.getBook().getId() == null) {
                redirectAttributes.addFlashAttribute("error", "Vă rugăm să selectați o carte");
                return "redirect:/loans";
            }

            if (loan.getEstimatedReturnDate() == null) {
                redirectAttributes.addFlashAttribute("error", "Vă rugăm să specificați data estimată de returnare");
                return "redirect:/loans";
            }

            if (loan.getEstimatedReturnDate().isBefore(LocalDate.now())) {
                redirectAttributes.addFlashAttribute("error", "Data estimată de returnare trebuie să fie în viitor");
                return "redirect:/loans";
            }

            loanService.save(loan);
            redirectAttributes.addFlashAttribute("success", "Împrumutul a fost creat cu succes");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "A apărut o eroare la crearea împrumutului: " + e.getMessage());
        }
        return "redirect:/loans";
    }

    @PostMapping("/loans/return/{id}")
    public String returnLoan(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            loanService.markAsReturned(id);
            redirectAttributes.addFlashAttribute("success", "Cartea a fost returnată cu succes");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "A apărut o eroare la returnarea cărții: " + e.getMessage());
        }
        return "redirect:/loans";
    }
} 