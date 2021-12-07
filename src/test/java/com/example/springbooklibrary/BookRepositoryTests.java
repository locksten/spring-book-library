package com.example.springbooklibrary;

import com.example.springbooklibrary.book.Book;
import com.example.springbooklibrary.book.BookRepository;
import com.example.springbooklibrary.database.ILibraryDatabase;
import com.example.springbooklibrary.database.LibraryData;
import com.example.springbooklibrary.database.LibraryDatabase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

public class BookRepositoryTests {

    private final BookRepository bookRepository;

    private final Book bookA = new Book(
            "TitleAB",
            "AuthorA",
            "CategoryA",
            "LanguageA",
            LocalDate.of(2000, 1, 1),
            "isbnA");

    private final Book bookB = new Book(
            "TitleB",
            "AuthorB",
            "CategoryB",
            "LanguageB",
            LocalDate.of(2000, 1, 1),
            "isbnB");

    private final Book bookC = new Book(
            "TitleC",
            "AuthorC",
            "CategoryC",
            "LanguageC",
            LocalDate.of(2000, 1, 1),
            "isbnC");

    public BookRepositoryTests() {
        ILibraryDatabase libraryDatabase = Mockito.mock(LibraryDatabase.class);
        when(libraryDatabase.getData()).thenReturn(new LibraryData());
        bookRepository = Mockito.spy(new BookRepository(libraryDatabase));
        when(bookRepository.getAll()).thenReturn(List.of(bookA, bookB, bookC));
    }

    @Test
    public void bookTitleFilterTest() {
        BookRepository.Filters filters = new BookRepository.Filters();
        filters.setTitle("B");
        assertThat(bookRepository.getAll(filters, book -> false)).matches(
                books -> books.size() == 2 && books.contains(bookA) && books.contains(bookB));
    }

    @Test
    public void bookAuthorFilterTest() {
        BookRepository.Filters filters = new BookRepository.Filters();
        filters.setAuthor("AuthorA");
        assertThat(bookRepository.getAll(filters, book -> false)).matches(
                books -> books.size() == 1 && books.contains(bookA));
    }

    @Test
    public void bookCategoryFilterTest() {
        BookRepository.Filters filters = new BookRepository.Filters();
        filters.setCategory("CategoryC");
        assertThat(bookRepository.getAll(filters, book -> false)).matches(
                books -> books.size() == 1 && books.contains(bookC));
    }

    @Test
    public void bookLanguageFilterTest() {
        BookRepository.Filters filters = new BookRepository.Filters();
        filters.setLanguage("LanguageA");
        assertThat(bookRepository.getAll(filters, book -> false)).matches(
                books -> books.size() == 1 && books.contains(bookA));
    }

    @Test
    public void bookIsbnFilterTest() {
        BookRepository.Filters filters = new BookRepository.Filters();
        filters.setIsbn("isbnB");
        assertThat(bookRepository.getAll(filters, book -> false)).matches(
                books -> books.size() == 1 && books.contains(bookB));
    }

    @Test
    public void bookIsCheckedOutFilterTest() {
        BookRepository.Filters filters = new BookRepository.Filters();

        filters.setIsCheckedOut(true);
        assertThat(bookRepository.getAll(filters, book -> !book.equals(bookB))).matches(
                books -> books.size() == 2 && books.contains(bookA) && books.contains(bookC));

        filters.setIsCheckedOut(false);
        assertThat(bookRepository.getAll(filters, book -> book.equals(bookA))).matches(
                books -> books.size() == 2 && books.contains(bookB) && books.contains(bookC));
    }

}

