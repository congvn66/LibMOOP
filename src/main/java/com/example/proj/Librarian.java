package com.example.proj;

import java.util.Date;
import java.util.List;

public class Librarian extends Account{

    public Librarian(String id, AccountStatus status, String password) {
        super(id, status, password);
        this.getCatalog().ImportFromFile();
    }

    public void LoadFromFile() {
        this.getCatalog().ImportFromFile();
    }

    public void addBookItem(BookItem bookItem) {
        this.getCatalog().addBookItem(bookItem);
        this.getCatalog().writeBookItemToFile(bookItem);
    }


    public void removeBook(String id) {
        this.getCatalog().removeBookById(id);
    }
}
