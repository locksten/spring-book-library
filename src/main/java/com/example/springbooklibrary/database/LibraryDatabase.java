package com.example.springbooklibrary.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class LibraryDatabase implements ILibraryDatabase {
    private final static Path DATABASE_PATH = Paths.get("SpringBookLibrary.json");

    private final LibraryData libraryData;

    public LibraryDatabase() throws IOException {
        this.libraryData = readFromJsonDatabaseFile(LibraryData.class);
    }

    public void save() throws IOException {
        writeToJsonDatabaseFile(libraryData);
    }

    public static <T> T readFromJsonDatabaseFile(Class<T> c) throws IOException {
        try {
            ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
            return mapper.readValue(DATABASE_PATH.toFile(), c);
        } catch (Exception e) {
            throw new IOException("Failed to read database.", e);
        }
    }

    public static void writeToJsonDatabaseFile(Object data) throws IOException {
        try {
            ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
            mapper.setDateFormat(new StdDateFormat());
            mapper.writerWithDefaultPrettyPrinter().writeValue(DATABASE_PATH.toFile(), data);
        } catch (Exception e) {
            throw new IOException("Failed to write database.", e);
        }
    }

    public LibraryData getData() {
        return libraryData;
    }

}
