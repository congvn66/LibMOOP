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

    /**
     * Retrieves the catalog of books associated with the current member.
     *
     * This method checks if the catalog is already initialized. If not, it initializes
     * the catalog by fetching it from the current member using `CurrentMember.getMember().getCatalog()`.
     *
     * @return the catalog associated with the current member.
     */
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

    /**
     * Returns a string representation of the notification regarding a borrowed book.
     *
     * The string will display the title of the book along with the due date. If the book is overdue,
     * the message will indicate that the book must be returned immediately. If the book is not overdue,
     * the message will include the due date and the number of days remaining until the book is due.
     *
     * @return a string indicating the status of the borrowed book and its due date.
     */
    public String toString() {
        String title = this.getCatalog().findBookById(this.bookId).getTitle();
        if (this.late) {
            return "You have to return book: " + title + " right now";
        }
        return "Book: " + title + " is due on " + dueDate.toString() + "\n(" + Period.between(LocalDate.now(), dueDate).getDays() + " days left)";
    }
}
