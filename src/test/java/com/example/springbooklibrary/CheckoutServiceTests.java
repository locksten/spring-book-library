package com.example.springbooklibrary;

import com.example.springbooklibrary.book.Book;
import com.example.springbooklibrary.book.BookRepository;
import com.example.springbooklibrary.checkout.Checkout;
import com.example.springbooklibrary.checkout.CheckoutRepository;
import com.example.springbooklibrary.checkout.CheckoutService;
import com.example.springbooklibrary.exception.BookNotFoundException;
import com.example.springbooklibrary.exception.CheckoutDurationTooLongException;
import com.example.springbooklibrary.exception.CheckoutEndBeforeStartException;
import com.example.springbooklibrary.exception.TooManyCheckoutsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;

import java.time.*;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CheckoutServiceTests {

    private final BookRepository bookRepository = Mockito.mock(BookRepository.class);

    private final CheckoutRepository checkoutRepository = Mockito.mock(CheckoutRepository.class);

    private final CheckoutService checkoutService = new CheckoutService(bookRepository, checkoutRepository);

    private final Book book = new Book(
            "TitleA",
            "AuthorA",
            "CategoryA",
            "LanguageA",
            LocalDate.of(2000, 1, 1),
            "isbnA");

    private final Checkout checkout = new Checkout(
            book.getId(),
            "alice",
            LocalDateTime.of(2000, 1, 1, 10, 0),
            LocalDateTime.of(2000, 1, 1, 10, 0));

    public void mockAddCheckout() {
        when(checkoutRepository.add(any(Checkout.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
    }

    public void mockBookExists() {
        when(bookRepository.exists(book.getId())).thenReturn(true);
    }

    @Test
    public void checkOutMissingBookTest() {
        mockAddCheckout();

        Assertions.assertThrows(BookNotFoundException.class, () -> checkoutService.checkOutBook(checkout));

        mockBookExists();
        assertThat(checkoutService.checkOutBook(checkout)).isEqualTo(checkout);
    }

    @Test
    public void checkOutEndBeforeStartTest() {
        mockAddCheckout();
        mockBookExists();

        Checkout checkout = new Checkout(
                book.getId(),
                "alice",
                LocalDateTime.of(2000, 1, 1, 10, 1),
                LocalDateTime.of(2000, 1, 1, 10, 0));

        Assertions.assertThrows(CheckoutEndBeforeStartException.class, () -> checkoutService.checkOutBook(checkout));
    }

    @Test
    public void checkOutDurationTooLongTest() {
        mockAddCheckout();
        mockBookExists();

        Checkout checkout = new Checkout(
                book.getId(),
                "alice",
                LocalDateTime.of(2000, 1, 1, 10, 0),
                LocalDateTime.of(2000, 1, 1, 10, 0));

        Assertions.assertDoesNotThrow(() -> checkoutService.checkOutBook(checkout));

        checkout.setEnd(checkout.getStart().plus(checkoutService.getMaxCheckoutDuration()));
        Assertions.assertDoesNotThrow(() -> checkoutService.checkOutBook(checkout));

        checkout.setEnd(checkout.getStart().plus(checkoutService.getMaxCheckoutDuration().plusSeconds(1)));
        Assertions.assertThrows(CheckoutDurationTooLongException.class, () -> checkoutService.checkOutBook(checkout));
    }

    @Test
    public void TooManyCheckoutsTest() {
        mockAddCheckout();
        mockBookExists();
        CheckoutService checkoutService = Mockito.spy(this.checkoutService);
        when(checkoutService.getMaxCheckoutsPerPerson()).thenReturn(3);
        when(checkoutService.getMaxCheckoutDuration()).thenReturn(Duration.ofDays(300));
        when(checkoutRepository.getAll()).thenReturn(
                Arrays.asList(
                        new Checkout(
                                book.getId(),
                                "alice",
                                LocalDateTime.of(2000, 5, 1, 10, 0),
                                LocalDateTime.of(2000, 7, 1, 10, 0)),
                        new Checkout(
                                book.getId(),
                                "alice",
                                LocalDateTime.of(2000, 6, 1, 10, 0),
                                LocalDateTime.of(2000, 8, 1, 10, 0)),
                        new Checkout(
                                book.getId(),
                                "alice",
                                LocalDateTime.of(2000, 4, 1, 10, 0),
                                LocalDateTime.of(2000, 9, 1, 10, 0))
                ));

        Checkout checkoutA = new Checkout(
                book.getId(),
                "alice",
                LocalDateTime.of(2000, 10, 1, 10, 0),
                LocalDateTime.of(2000, 11, 1, 10, 0));
        Assertions.assertDoesNotThrow(() -> checkoutService.checkOutBook(checkoutA));

        Checkout checkoutB = new Checkout(
                book.getId(),
                "alice",
                LocalDateTime.of(2000, 6, 1, 10, 0),
                LocalDateTime.of(2000, 7, 1, 10, 0));
        Assertions.assertThrows(TooManyCheckoutsException.class, () -> checkoutService.checkOutBook(checkoutB));

        Checkout checkoutC = new Checkout(
                book.getId(),
                "bob",
                LocalDateTime.of(2000, 6, 1, 10, 0),
                LocalDateTime.of(2000, 7, 1, 10, 0));
        Assertions.assertDoesNotThrow(() -> checkoutService.checkOutBook(checkoutC));
    }

    @Test
    public void isBookCurrentlyCheckedOutTest() {
        CheckoutService checkoutService = Mockito.spy(this.checkoutService);
        when(checkoutService.getClock()).thenReturn(Clock.fixed(Instant.EPOCH.plusSeconds(100), ZoneId.systemDefault()));

        when(checkoutRepository.getAll()).thenReturn(List.of());
        assertThat(checkoutService.isBookCurrentlyCheckedOut().test(book)).isFalse();

        when(checkoutRepository.getAll()).thenReturn(
                List.of(new Checkout(
                        (new Book()).getId(),
                        "alice",
                        LocalDateTime.ofInstant(Instant.EPOCH.plusSeconds(50), ZoneId.systemDefault()),
                        LocalDateTime.ofInstant(Instant.EPOCH.plusSeconds(150), ZoneId.systemDefault()))
                ));
        assertThat(checkoutService.isBookCurrentlyCheckedOut().test(book)).isFalse();

        when(checkoutRepository.getAll()).thenReturn(
                List.of(new Checkout(
                        book.getId(),
                        "alice",
                        LocalDateTime.ofInstant(Instant.EPOCH.plusSeconds(200), ZoneId.systemDefault()),
                        LocalDateTime.ofInstant(Instant.EPOCH.plusSeconds(300), ZoneId.systemDefault()))
                ));
        assertThat(checkoutService.isBookCurrentlyCheckedOut().test(book)).isFalse();

        when(checkoutRepository.getAll()).thenReturn(
                List.of(new Checkout(
                        book.getId(),
                        "alice",
                        LocalDateTime.ofInstant(Instant.EPOCH.plusSeconds(50), ZoneId.systemDefault()),
                        LocalDateTime.ofInstant(Instant.EPOCH.plusSeconds(150), ZoneId.systemDefault()))
                ));
        assertThat(checkoutService.isBookCurrentlyCheckedOut().test(book)).isTrue();

    }
}

