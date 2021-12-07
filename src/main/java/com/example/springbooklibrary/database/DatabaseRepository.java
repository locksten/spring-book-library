package com.example.springbooklibrary.database;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class DatabaseRepository<Id, T> {

    private final ILibraryDatabase database;

    public ConcurrentHashMap<Id, T> map;

    public DatabaseRepository(ILibraryDatabase libraryDatabase, ConcurrentHashMap<Id, T> map) {
        this.database = libraryDatabase;
        this.map = map;
    }

    public Collection<T> getAll() {
        return this.map.values();
    }

    public T getById(Id id) {
        T value = this.map.get(id);
        if (value == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return value;
    }

    public boolean exists(Id id) {
        return this.map.containsKey(id);
    }

    public T add(Id id, T entity) {
        T originalValue = this.map.putIfAbsent(id, entity);
        if (originalValue != null) throw new ResponseStatusException(HttpStatus.CONFLICT);
        try {
            database.save();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return entity;
    }

    public void remove(Id id) {
        this.map.remove(id);
        try {
            database.save();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
