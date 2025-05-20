package com.example.demo.service;

import com.example.demo.model.Book;
import com.example.demo.model.Loan;
import com.example.demo.repository.LoanRepository;
import com.example.demo.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class LoanServiceTest {
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private LoanService loanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveLoanSuccess() {
        Book book = new Book();
        book.setId(1L);
        Loan loan = new Loan();
        loan.setBook(book);
        loan.setEstimatedReturnDate(LocalDate.now().plusDays(5));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(loanRepository.findAll()).thenReturn(java.util.Collections.emptyList());
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);
        Loan saved = loanService.save(loan);
        assertThat(saved).isNotNull();
        assertThat(saved.getBook().getId()).isEqualTo(1L);
    }

    @Test
    void testSaveLoanBookUnavailable() {
        Book book = new Book();
        book.setId(1L);
        Loan loan = new Loan();
        loan.setBook(book);
        loan.setEstimatedReturnDate(LocalDate.now().plusDays(5));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Loan activeLoan = new Loan();
        activeLoan.setBook(book);
        activeLoan.setReturnDate(null);
        when(loanRepository.findAll()).thenReturn(java.util.List.of(activeLoan));
        assertThrows(IllegalArgumentException.class, () -> loanService.save(loan));
    }

    @Test
    void testSaveLoanWithPastDate() {
        Book book = new Book();
        book.setId(1L);
        Loan loan = new Loan();
        loan.setBook(book);
        loan.setEstimatedReturnDate(LocalDate.now().minusDays(1));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(loanRepository.findAll()).thenReturn(java.util.Collections.emptyList());
        assertThrows(IllegalArgumentException.class, () -> loanService.save(loan));
    }
} 