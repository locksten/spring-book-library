package com.example.springbooklibrary.book;

import java.time.LocalDate;
import java.util.UUID;

public class Book {
    private final UUID id = UUID.randomUUID();
    private String title;
    private String author;
    private String category;
    private String language;
    private LocalDate publicationDate;
    private String isbn;

    public Book(String title, String author, String category, String language, LocalDate publicationDate, String isbn) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.language = language;
        this.publicationDate = publicationDate;
        this.isbn = isbn;
    }

    public Book() {}

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public String getLanguage() {
        return language;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public String getIsbn() {
        return isbn;
    }

}
