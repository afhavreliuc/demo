package com.example.demo.service;

import com.example.demo.model.Loan;
import com.example.demo.model.Book;
import com.example.demo.repository.LoanRepository;
import com.example.demo.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
    }

    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    @Transactional
    public Loan save(Loan loan) {
        if (loan.getBook() == null || loan.getBook().getId() == null) {
            throw new IllegalArgumentException("Cartea este obligatorie");
        }

        Optional<Book> bookOpt = bookRepository.findById(loan.getBook().getId());
        if (bookOpt.isEmpty()) {
            throw new IllegalArgumentException("Cartea nu a fost găsită");
        }

        if (!isBookAvailable(bookOpt.get())) {
            throw new IllegalArgumentException("Cartea nu este disponibilă");
        }

        if (loan.getEstimatedReturnDate() == null) {
            throw new IllegalArgumentException("Data estimată de returnare este obligatorie");
        }

        if (loan.getEstimatedReturnDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data estimată de returnare trebuie să fie în viitor");
        }

        loan.setBook(bookOpt.get());
        loan.setLoanDate(LocalDate.now());
        return loanRepository.save(loan);
    }

    public boolean isBookAvailable(Book book) {
        if (book == null || book.getId() == null) {
            return false;
        }
        return loanRepository.findAll().stream()
                .noneMatch(l -> l.getBook().getId().equals(book.getId()) && l.getReturnDate() == null);
    }

    @Transactional
    public void markAsReturned(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Împrumutul nu a fost găsit"));

        if (loan.getReturnDate() != null) {
            throw new IllegalArgumentException("Cartea a fost deja returnată");
        }

        LocalDate today = LocalDate.now();
        if (loan.getEstimatedReturnDate() != null && today.isBefore(loan.getEstimatedReturnDate())) {
            throw new IllegalArgumentException("Nu poți returna cartea înainte de data estimată");
        }

        loan.setReturnDate(today);
        loanRepository.save(loan);
    }
} 