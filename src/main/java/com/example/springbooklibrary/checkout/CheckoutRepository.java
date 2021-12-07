package com.example.springbooklibrary.checkout;

import com.example.springbooklibrary.database.DatabaseRepository;
import com.example.springbooklibrary.database.ILibraryDatabase;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class CheckoutRepository extends DatabaseRepository<UUID, Checkout> {

    public CheckoutRepository(ILibraryDatabase libraryDatabase) {
        super(libraryDatabase, libraryDatabase.getData().getCheckouts());
    }

    public Checkout add(Checkout checkout) {
        return super.add(checkout.getId(), checkout);
    }

}