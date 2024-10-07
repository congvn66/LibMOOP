package com.example.proj;

import java.util.Date;
import java.util.List;

public abstract class Account {
    private String id;
    private String password;
    private Catalog catalog;
    private AccountStatus status;


    public Account(String id, AccountStatus status, String password) {
        this.id = id;
        this.status = status;
        this.password = password;
        this.catalog = new Catalog();

    }

    public void updateBook(String id, int field, String newValue) {
        this.getCatalog().editBookInDataBase(id, field, newValue);
    }

    public String getId() {
        return id;
    }

    public Catalog getCatalog() {
        return this.catalog;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public boolean resetPassword() {
        this.password = "1234";
        return true;
    }

    public List<BookItem> findBooksByTitle(String title) {
        return this.getCatalog().findBooksByTitle(title);
    }

    public List<BookItem> findBooksByAuthor(String author) {
        return this.getCatalog().findBooksByAuthor(author);
    }

    public List<BookItem> findBooksBySubject(String subject) {
        return this.getCatalog().findBooksBySubject(subject);
    }

    public List<BookItem> findBooksByPublicationDate(Date publicationDate) {
        return this.getCatalog().findBooksByPublicationDate(publicationDate);
    }
}
