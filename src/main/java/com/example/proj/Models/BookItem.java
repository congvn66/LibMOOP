package com.example.proj.Models;

import java.util.Date;
import java.util.Objects;

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
                    BookFormat format, BookStatus status, Date dateOfPurchase, Date publicationDate,
                    int number, String location) {
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

    public boolean getIsReferenceOnly() {
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

    public Rack getRack() {
        return this.rack;
    }

    public void setRack(int number, String locate) {
        this.rack.setNumber(number);
        this.rack.setLocationIdentifier(locate);
    }



    @Override
    public void displayInfo() {
        System.out.println("ISBN: " + this.getISBN());
        System.out.println("Title: " + this.getTitle());
        System.out.println("Subject: " + this.getSubject());
        System.out.println("Publisher: " + this.getPublisher());
        System.out.println("Language: " + this.getLanguage());
        System.out.println("Number of pages: " + this.getNumberOfPage());
        System.out.println("Author name: " + this.getAuthor().getName());
        System.out.println("Author description: " + this.getAuthor().getDescription());
        System.out.println("Id: " + id);
        System.out.println("Reference Only: " + isReferenceOnly);
        System.out.println("Price: " + price);
        System.out.println("Format: " + format);
        System.out.println("Status: " + status);
        System.out.println("Date of Purchase: " + dateOfPurchase);
        System.out.println("Publication Date: " + publicationDate);
        System.out.println("\n");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        BookItem bookItem = (BookItem) obj;
        return this.id.equals(bookItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Ensure to override hashCode whenever equals is overridden
    }
}
