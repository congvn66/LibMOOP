package com.example.proj;

import com.example.proj.Catalog;

import java.util.Date;
import java.util.List;

public class Library {
    private String name;
    private Catalog catalog;

    // Constructor
    public Library(String name) {
        this.name = name;
        this.catalog = new Catalog();
    }

    public void LoadFromFile() {
        this.catalog.ImportFromFile();
    }

    public void addBookItem(BookItem bookItem) {
        this.catalog.addBookItem(bookItem);
        this.catalog.writeBookItemToFile(bookItem);
    }

    public List<BookItem> findBooksByTitle(String title) {
        return catalog.findBooksByTitle(title);
    }

    public List<BookItem> findBooksByAuthor(String author) {
        return catalog.findBooksByAuthor(author);
    }

    public List<BookItem> findBooksBySubject(String subject) {
        return catalog.findBooksBySubject(subject);
    }

    public List<BookItem> findBooksByPublicationDate(Date publicationDate) {
        return catalog.findBooksByPublicationDate(publicationDate);
    }

    public void removeBook(String id) {
        this.catalog.removeBookById(id);
    }

    public void displayLibraryInfo() {
        System.out.println("Library Name: " + name);
        catalog.displayCatalogInfo();
    }
}
