package com.example.demo.repository;

import com.example.demo.model.BookDetails;
import org.springframework.data.jpa.repository.JpaRepository;
 
public interface BookDetailsRepository extends JpaRepository<BookDetails, Long> {} 