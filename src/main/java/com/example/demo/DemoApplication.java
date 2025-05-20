package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.demo.model.Author;
import com.example.demo.model.Category;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner initData(AuthorRepository authorRepository, CategoryRepository categoryRepository) {
		return args -> {
			if (authorRepository.count() == 0) {
				authorRepository.save(new Author(null, "J.K. Rowling", null));
				authorRepository.save(new Author(null, "George Orwell", null));
				authorRepository.save(new Author(null, "J.R.R. Tolkien", null));
			}
			if (categoryRepository.count() == 0) {
				categoryRepository.save(new Category(null, "Ficțiune", null));
				categoryRepository.save(new Category(null, "Fantasy", null));
				categoryRepository.save(new Category(null, "Clasic", null));
			}
		};
	}

}
