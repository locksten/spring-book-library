package com.example.springbooklibrary.book;

import com.example.springbooklibrary.checkout.CheckoutService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final CheckoutService checkoutService;

    public BookService(BookRepository bookRepository, CheckoutService checkoutService){
        this.bookRepository = bookRepository;
        this.checkoutService = checkoutService;
    }

    public Collection<Book> getBooks(BookRepository.Filters filters) {
        return bookRepository.getAll(filters, this.checkoutService.isBookCurrentlyCheckedOut());
    }

}