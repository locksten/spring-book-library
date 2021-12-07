package com.example.springbooklibrary.database;

import com.example.springbooklibrary.book.Book;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LibraryData {
    ConcurrentHashMap<UUID, Book> books;

    public ConcurrentHashMap<UUID, Book> getBooks() {
        return books;
    }

}
