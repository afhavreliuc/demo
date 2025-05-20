package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.demo.model.Author;
import com.example.demo.model.Category;
import com.example.demo.model.Book;
import com.example.demo.model.Reader;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.ReaderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import java.util.Set;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner initData(AuthorRepository authorRepository, CategoryRepository categoryRepository, BookRepository bookRepository, ReaderRepository readerRepository, JdbcTemplate jdbcTemplate) {
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
			if (readerRepository.count() == 0) {
				Reader reader = new Reader();
				reader.setName("Cititor Demo");
				readerRepository.save(reader);
			}
			if (bookRepository.count() == 0) {
				Author author = authorRepository.findAll().stream().findFirst().orElse(null);
				Category category = categoryRepository.findAll().stream().findFirst().orElse(null);
				if (author != null && category != null) {
					Book book = new Book();
					book.setTitle("Carte demo");
					book.setAuthors(Set.of(author));
					book.setCategory(category);
					bookRepository.save(book);
				}
			}

			// Adaugă user demo pentru Spring Security JDBC
			try {
				jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS users (username VARCHAR(50) PRIMARY KEY, password VARCHAR(100), enabled BOOLEAN)");
				jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS authorities (username VARCHAR(50), authority VARCHAR(50))");
				jdbcTemplate.execute("CREATE UNIQUE INDEX IF NOT EXISTS ix_auth_username ON authorities (username, authority)");
				String hash = new BCryptPasswordEncoder().encode("password");
				jdbcTemplate.update("DELETE FROM authorities WHERE username = 'demo'");
				jdbcTemplate.update("DELETE FROM users WHERE username = 'demo'");
				jdbcTemplate.update("INSERT INTO users (username, password, enabled) VALUES (?, ?, ?)", "demo", hash, true);
				jdbcTemplate.update("INSERT INTO authorities (username, authority) VALUES (?, ?)", "demo", "ROLE_USER");
			} catch (Exception ex) {
				System.out.println("Eroare la crearea userului demo: " + ex.getMessage());
			}
		};
	}

}
