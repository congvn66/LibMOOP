package com.example.proj;

import com.sun.source.tree.LambdaExpressionTree;

import java.util.Date;

public class BookLending extends MemberActions{
    private Date dueDate;
    private Date returnDate;
    private BookItem bookItem;
    private String type;

    public BookItem getBookItem() {
        return this.bookItem;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public void renewBook() {
        return;
    }

    public void returnBook() {
        return;
    }

    public BookLending(Date creationDate, Date dueDate, Date returnDate, BookItem bookItem, String type) {
        super(creationDate);
        this.returnDate = returnDate;
        this.dueDate = dueDate;
        this.bookItem = bookItem;
        this.type = type;
    }
}
