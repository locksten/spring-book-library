package com.example.springbooklibrary.database;

import java.io.IOException;

public interface ILibraryDatabase {
    void save() throws IOException;

    LibraryData getData();
}
