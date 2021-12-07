package com.example.springbooklibrary.book;

import com.example.springbooklibrary.database.DatabaseRepository;
import com.example.springbooklibrary.database.ILibraryDatabase;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class BookRepository extends DatabaseRepository<UUID, Book> {

    public BookRepository(ILibraryDatabase libraryDatabase) {
        super(libraryDatabase, libraryDatabase.getData().getBooks());
    }

    public Book add(Book book) {
        return super.add(book.getId(), book);
    }

}
