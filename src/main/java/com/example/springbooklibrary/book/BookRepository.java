package com.example.springbooklibrary.book;

import com.example.springbooklibrary.database.DatabaseRepository;
import com.example.springbooklibrary.database.ILibraryDatabase;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class BookRepository extends DatabaseRepository<UUID, Book> {

    public BookRepository(ILibraryDatabase libraryDatabase) {
        super(libraryDatabase, libraryDatabase.getData().getBooks());
    }

    final static class Filters {
        private String title;
        private String author;
        private String category;
        private String language;
        private String isbn;
        private Boolean isCheckedOut;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        public Boolean getIsCheckedOut() {
            return isCheckedOut;
        }

        public void setIsCheckedOut(Boolean isCheckedOut) {
            this.isCheckedOut = isCheckedOut;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }

    public static Predicate<Book> matchesTitle(String title) {
        return book -> book.getTitle().contains(title);
    }

    public static Predicate<Book> matchesAuthor(String author) {
        return book -> book.getAuthor().equals(author);
    }

    public static Predicate<Book> matchesCategory(String category) {
        return book -> book.getCategory().equals(category);
    }

    public static Predicate<Book> matchesLanguage(String language) {
        return book -> book.getLanguage().equals(language);
    }

    public static Predicate<Book> matchesIsbn(String isbn) {
        return book -> book.getIsbn().equals(isbn);
    }

    public Collection<Book> getAll(Filters filters, Predicate<Book> isCheckedOut) {
        Collection<Book> books = this.map.values();

        Predicate<Book> predicates = book -> true;
        if (filters.title != null) predicates = predicates.and(matchesTitle(filters.title));
        if (filters.author != null) predicates = predicates.and(matchesAuthor(filters.author));
        if (filters.category != null) predicates = predicates.and(matchesCategory(filters.category));
        if (filters.language != null) predicates = predicates.and(matchesLanguage(filters.language));
        if (filters.isbn != null) predicates = predicates.and(matchesIsbn(filters.isbn));
        if (filters.isCheckedOut != null) predicates = predicates.and(isCheckedOut);

        return books.stream().filter(predicates).collect(Collectors.toList());
    }

    public Book add(Book book) {
        return super.add(book.getId(), book);
    }

}
