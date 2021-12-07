package com.example.springbooklibrary.checkout;
import com.example.springbooklibrary.book.BookRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CheckoutService {
    private final BookRepository bookRepository;
    private final CheckoutRepository checkoutRepository;

    CheckoutService(BookRepository bookRepository, CheckoutRepository checkoutRepository) {
        this.bookRepository = bookRepository;
        this.checkoutRepository = checkoutRepository;
    }

    public Checkout checkOutBook(Checkout checkout) {
        if (!bookRepository.exists(checkout.getBookId())) throw new RuntimeException("book not found");
        if (checkout.getEnd().isBefore(checkout.getStart())) throw new RuntimeException("end before start");
        if (!checkout.getStart().plusMonths(2).isAfter(checkout.getEnd()))
            throw new RuntimeException("too long");
        if (isUsersNumberOfActiveCheckoutsGreaterThan(checkout.getName(), LocalDateTime.now(), 3))
            throw new RuntimeException("too many");

        return checkoutRepository.add(checkout);
    }

    boolean isUsersNumberOfActiveCheckoutsGreaterThan(String name, LocalDateTime now, int threshold) {
        int counter = 0;
        for (Checkout checkout : checkoutRepository.getAll()) {
            if (isCheckedOutBy(checkout, now, name)) {
                counter++;
                if (counter > threshold) return true;
            }
        }
        return false;
    }

    static boolean isCheckedOut(Checkout checkout, LocalDateTime time) {
        return !checkout.getStart().isAfter(time) && !checkout.getEnd().isBefore(time);
    }

    static boolean isCheckedOutBy(Checkout checkout, LocalDateTime time, String name) {
        return checkout.getName().equals(name) && isCheckedOut(checkout, time);
    }

}