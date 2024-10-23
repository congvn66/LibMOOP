package com.example.proj.Models;

public class Notification {
    private static Catalog catalog;
    private String bookId;
    private String dueDate;
    private boolean late;

    public Notification(String bookId, String dueDate, boolean late) {
        this.bookId = bookId;
        this.dueDate = dueDate;
        this.late = late;
    }

    public Catalog getCatalog() {
        if (catalog == null) {
            catalog = new Catalog();
            catalog.loadCatalogFromDatabase();
        }
        return catalog;
    }

    public String getBookId() {
        return bookId;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String toString() {
        String title = this.getCatalog().findBookById(this.bookId).getTitle();
        if (this.late) {
            return "you have to return book: " + title + " right now";
        }
        return "you have to return book: " + title + ", at: " + dueDate;
    }
}
