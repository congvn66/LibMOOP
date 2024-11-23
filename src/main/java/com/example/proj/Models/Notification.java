package com.example.proj.Models;

import java.time.LocalDate;
import java.time.Period;

public class Notification {
    private static Catalog catalog;
    private String bookId;
    private LocalDate dueDate;
    private boolean late;

    public Notification(String bookId, LocalDate dueDate, boolean late) {
        this.bookId = bookId;
        this.dueDate = dueDate;
        this.late = late;
    }

    public Catalog getCatalog() {
        if (catalog == null) {
            catalog = CurrentMember.getMember().getCatalog();
        }
        return catalog;
    }

    public String getBookId() {
        return bookId;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String toString() {
        String title = this.getCatalog().findBookById(this.bookId).getTitle();
        if (this.late) {
            return "You have to return book: " + title + " right now";
        }
        return "Book: " + title + " is due on " + dueDate.toString() + "\n(" + Period.between(LocalDate.now(), dueDate).getDays() + " days left)";
    }
}
