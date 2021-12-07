package com.example.springbooklibrary.checkout;

import java.time.LocalDateTime;
import java.util.UUID;

public class Checkout {
    private final UUID id = UUID.randomUUID();
    private UUID bookId;
    private String name;
    private LocalDateTime start;
    private LocalDateTime end;

    public Checkout(UUID bookId, String name, LocalDateTime start, LocalDateTime end) {
        this.bookId = bookId;
        this.name = name;
        this.start = start;
        this.end = end;
    }

    public Checkout() {}

    public String getName() {
        return name;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public UUID getId() {
        return id;
    }

    public UUID getBookId() {
        return bookId;
    }
}
