package com.example.springbooklibrary.book;

import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
public class BookController {
    private final BookRepository bookRepository;

    BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/books")
    public Collection<Book> all() {
        return bookRepository.getAll();
    }

    @GetMapping("/books/{id}")
    Book one(@PathVariable UUID id) {
        return bookRepository.getById(id);
    }

    @PostMapping("/books")
    Book newBook(@RequestBody Book newBook) {
        return bookRepository.add(newBook);
    }

    @DeleteMapping("/books/{id}")
    void deleteOne(@PathVariable UUID id) {
        bookRepository.remove(id);
    }


}
