package com.example.springbooklibrary.checkout;
import com.example.springbooklibrary.book.Book;
import com.example.springbooklibrary.book.BookRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Predicate;

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

    static boolean isBookCheckedOut(Checkout checkout, LocalDateTime time, UUID bookId) {
        return checkout.getBookId().equals(bookId) && isCheckedOut(checkout, time);
    }

    public boolean isBookCheckedOut(UUID bookId) {
        LocalDateTime now = LocalDateTime.now();
        for (Checkout checkout : checkoutRepository.getAll()) {
            if (isBookCheckedOut(checkout, now, bookId)) {
                return true;
            }
        }
        return false;
    }

    public Predicate<Book> isBookCheckedOut() {
        return book -> isBookCheckedOut(book.getId());
    }

}