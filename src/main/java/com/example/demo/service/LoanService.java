package com.example.demo.service;

import com.example.demo.model.Loan;
import com.example.demo.model.Book;
import com.example.demo.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanService {
    private final LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    public Loan save(Loan loan) {
        return loanRepository.save(loan);
    }

    public boolean isBookAvailable(Book book) {
        return loanRepository.findAll().stream()
                .noneMatch(l -> l.getBook().getId().equals(book.getId()) && l.getReturnDate() == null);
    }

    public void markAsReturned(Long loanId) {
        loanRepository.findById(loanId).ifPresent(loan -> {
            if (loan.getReturnDate() != null) {
                java.time.LocalDate today = java.time.LocalDate.now();
                java.time.LocalDate estimatedReturn = null;
                try {
                    estimatedReturn = java.time.LocalDate.parse(loan.getReturnDate());
                } catch (Exception ignored) {}
                if (estimatedReturn != null && today.isBefore(estimatedReturn)) {
                    // Nu permite returnarea înainte de data estimată
                    return;
                }
            }
            loan.setReturnDate(java.time.LocalDate.now().toString());
            loanRepository.save(loan);
        });
    }
} 