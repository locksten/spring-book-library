package com.example.springbooklibrary.book;

import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
public class BookController {
    private final BookRepository bookRepository;
    private final BookService bookService;

    BookController(BookRepository bookRepository, BookService bookService) {
        this.bookRepository = bookRepository;
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public Collection<Book> all(BookRepository.Filters filters) {
        return bookService.getBooks(filters);
    }

    @GetMapping("/books/{id}")
    Book one(@PathVariable UUID id) {
        return bookRepository.getById(id);
    }

    @DeleteMapping("/books/{id}")
    void deleteOne(@PathVariable UUID id) {
        bookRepository.remove(id);
    }

    @PostMapping("/books")
    Book newBook(@RequestBody Book newBook) {
        return bookRepository.add(newBook);
    }
}
