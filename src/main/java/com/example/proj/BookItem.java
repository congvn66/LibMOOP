package com.example.proj;

import java.util.Date;

public class BookItem extends Book {
    private String id;
    private boolean isReferenceOnly;
    private Date borrowed;
    private Date dueDate;
    private double price;
    private BookFormat format;
    private BookStatus status;
    private Date dateOfPurchase;
    private Date publicationDate;

    // CAUTION!!!
    private Rack rack;


    // Constructor
    public BookItem(String ISBN, String title, String subject, String publisher, String language,
                    String numberOfPage, String authorName, String authorDescription,
                    String id, boolean isReferenceOnly, double price,
                    BookFormat format, BookStatus status, Date dateOfPurchase, Date publicationDate, int number, String location) {
        super(ISBN, title, subject, publisher, language, numberOfPage, authorName, authorDescription);
        this.id = id;
        this.isReferenceOnly = isReferenceOnly;
        this.price = price;
        this.format = format;
        this.status = status;
        this.dateOfPurchase = dateOfPurchase;
        this.publicationDate = publicationDate;

        // CAUTION!!!!!
        this.rack = new Rack(number, location);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isReferenceOnly() {
        return isReferenceOnly;
    }

    public void setReferenceOnly(boolean referenceOnly) {
        isReferenceOnly = referenceOnly;
    }

    public Date getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(Date borrowed) {
        this.borrowed = borrowed;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public BookFormat getFormat() {
        return format;
    }

    public void setFormat(BookFormat format) {
        this.format = format;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public Date getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(Date dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public boolean checkout() {
        if (status.equals(BookStatus.AVAILABLE) && !isReferenceOnly) {
            return true;
        }
        return false;
    }


    @Override
    public void displayInfo() {
        System.out.println("Barcode: " + id);
        System.out.println("Reference Only: " + isReferenceOnly);
        System.out.println("Price: " + price);
        System.out.println("Format: " + format);
        System.out.println("Status: " + status);
        System.out.println("Date of Purchase: " + dateOfPurchase);
        System.out.println("Publication Date: " + publicationDate);

    }
}
