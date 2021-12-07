package com.example.springbooklibrary.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class LibraryDatabase implements ILibraryDatabase {

    private final LibraryData libraryData;

    public LibraryDatabase() {
        this.libraryData = readFromJsonDatabaseFile(LibraryData.class);
    }

    public static <T> T readFromJsonDatabaseFile(Class<T> c) {
        try {
            String text = Files.readString(Paths.get("/tmp/spring.json"));
            ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
            return mapper.readValue(text, c);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save() {
        System.out.println("SAVE DB");
    }

    public LibraryData getData() {
        return libraryData;
    }

}
