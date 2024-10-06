package com.example.proj;

import java.util.Date;

public class BookReservation extends MemberActions{
    private BookStatus bookStatus;
    private BookItem bookItem;

    public BookStatus getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(BookStatus bookStatus) {
        this.bookStatus = bookStatus;
    }

    public BookReservation(Date creationDate, BookStatus bookStatus, BookItem bookItem) {
        super(creationDate);
        this.bookStatus = bookStatus;
        this.bookItem = bookItem;
    }
}
