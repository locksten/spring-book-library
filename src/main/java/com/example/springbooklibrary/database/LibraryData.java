package com.example.springbooklibrary.database;

import com.example.springbooklibrary.book.Book;
import com.example.springbooklibrary.checkout.Checkout;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LibraryData {
    ConcurrentHashMap<UUID, Book> books = new ConcurrentHashMap<>();
    ConcurrentHashMap<UUID, Checkout> checkouts = new ConcurrentHashMap<>();

    public ConcurrentHashMap<UUID, Book> getBooks() {
        return books;
    }

    public ConcurrentHashMap<UUID, Checkout> getCheckouts() {
        return checkouts;
    }

}
