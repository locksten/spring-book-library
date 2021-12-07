package com.example.springbooklibrary.checkout;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
public class CheckoutController {

    private final CheckoutService checkoutService;

    CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    final static class CheckoutRequest {
        @NotNull
        private UUID bookId;

        @NotNull
        private String name;

        @NotNull
        private LocalDateTime start;

        @NotNull
        private LocalDateTime end;

        public UUID getBookId() {
            return bookId;
        }

        public void setBookId(UUID bookId) {
            this.bookId = bookId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public LocalDateTime getStart() {
            return start;
        }

        public void setStart(LocalDateTime start) {
            this.start = start;
        }

        public LocalDateTime getEnd() {
            return end;
        }

        public void setEnd(LocalDateTime end) {
            this.end = end;
        }
    }

    @PostMapping("/checkouts")
    Checkout checkOut(@Valid @RequestBody CheckoutRequest request) {
        return this.checkoutService.checkOutBook(new Checkout(request.bookId, request.name, request.start, request.end));
    }

}