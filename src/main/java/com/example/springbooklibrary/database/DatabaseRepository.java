package com.example.springbooklibrary.database;

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
        return this.map.get(id);
    }

    public boolean exists(Id id) {
        return this.map.get(id) != null;
    }

    public T add(Id id, T entity) {
        this.map.put(id, entity);
        database.save();
        return entity;
    }

    public void remove(Id id) {
        this.map.remove(id);
        database.save();
    }
}
