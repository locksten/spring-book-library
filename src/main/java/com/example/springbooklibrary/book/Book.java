package com.example.springbooklibrary.book;

import java.util.UUID;

public class Book {
    private UUID id;
    private String title;

    public Book(UUID id, String title) {
        this.id = id;
        this.title = title;
    }

    public Book() {}

    public String getTitle() {
        return title;
    }

    public UUID getId() {
        return id;
    }
}
