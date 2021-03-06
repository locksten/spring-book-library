package com.example.springbooklibrary.checkout;
import com.example.springbooklibrary.book.Book;
import com.example.springbooklibrary.book.BookRepository;
import com.example.springbooklibrary.exception.BookNotFoundException;
import com.example.springbooklibrary.exception.CheckoutDurationTooLongException;
import com.example.springbooklibrary.exception.CheckoutEndBeforeStartException;
import com.example.springbooklibrary.exception.TooManyCheckoutsException;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Predicate;

@Service
public class CheckoutService {
    private final int maxCheckoutsPerPerson = 3;
    private final Duration maxCheckoutDuration = Duration.ofDays(2 * 30);

    private final BookRepository bookRepository;
    private final CheckoutRepository checkoutRepository;

    public CheckoutService(BookRepository bookRepository, CheckoutRepository checkoutRepository) {
        this.bookRepository = bookRepository;
        this.checkoutRepository = checkoutRepository;
    }

    public Checkout checkOutBook(Checkout checkout) {
        if (!bookRepository.exists(checkout.getBookId())) throw new BookNotFoundException();
        if (checkout.getEnd().isBefore(checkout.getStart())) throw new CheckoutEndBeforeStartException();
        if (checkout.getStart().plus(getMaxCheckoutDuration()).isBefore(checkout.getEnd()))
            throw new CheckoutDurationTooLongException(getMaxCheckoutDuration());
        if (isUsersNumberOfCheckoutsDuringCheckoutGreaterThan(checkout, getMaxCheckoutsPerPerson()))
            throw new TooManyCheckoutsException(getMaxCheckoutsPerPerson());

        return checkoutRepository.add(checkout);
    }

    private boolean isUsersNumberOfCheckoutsDuringCheckoutGreaterThan(Checkout checkout, int threshold) {
        int counter = 0;
        for (Checkout c : checkoutRepository.getAll()) {
            if (c.getName().equals(checkout.getName()) && isCheckoutsOverlapping(checkout, c)) {
                counter++;
                if (counter >= threshold) return true;
            }
        }
        return false;
    }

    private static boolean isCheckoutBefore(Checkout checkout, LocalDateTime time) {
        return checkout.getStart().isBefore(time) && checkout.getEnd().isBefore(time);
    }

    private static boolean isCheckoutAfter(Checkout checkout, LocalDateTime time) {
        return checkout.getStart().isAfter(time) && checkout.getEnd().isAfter(time);
    }

    private static boolean isCheckoutsOverlapping(Checkout a, Checkout b) {
        return !(isCheckoutBefore(a, b.getStart()) || isCheckoutAfter(a, b.getEnd()));
    }

    private static boolean isCheckedOut(Checkout checkout, LocalDateTime time) {
        return !checkout.getStart().isAfter(time) && !checkout.getEnd().isBefore(time);
    }

    private static boolean isBookCheckedOut(Checkout checkout, LocalDateTime time, UUID bookId) {
        return checkout.getBookId().equals(bookId) && isCheckedOut(checkout, time);
    }

    private boolean isBookCurrentlyCheckedOut(UUID bookId) {
        LocalDateTime now = LocalDateTime.now(getClock());
        for (Checkout checkout : checkoutRepository.getAll()) {
            if (isBookCheckedOut(checkout, now, bookId)) {
                return true;
            }
        }
        return false;
    }

    public Predicate<Book> isBookCurrentlyCheckedOut() {
        return book -> isBookCurrentlyCheckedOut(book.getId());
    }

    public int getMaxCheckoutsPerPerson() {
        return maxCheckoutsPerPerson;
    }

    public Duration getMaxCheckoutDuration() {
        return maxCheckoutDuration;
    }

    public Clock getClock() {
        return Clock.systemDefaultZone();
    }

}