package com.example.proj.Models;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
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
    private String imgName;
    private Rack rack;

    public BookItem(String ISBN, String title, String subject, String publisher, String language,
                    String numberOfPage, String authorName, String authorDescription,
                    String id, boolean isReferenceOnly, double price,
                    BookFormat format, BookStatus status, Date dateOfPurchase, Date publicationDate,
                    int number, String location, String imgName) {
        super(ISBN, title, subject, publisher, language, numberOfPage, authorName, authorDescription);
        this.id = id;
        this.isReferenceOnly = isReferenceOnly;
        this.price = price;
        this.format = format;
        this.status = status;
        this.dateOfPurchase = dateOfPurchase;
        this.publicationDate = publicationDate;
        this.imgName = imgName;
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

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
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

    public Rack getRack() {
        return this.rack;
    }

    public void setRack(int number, String locate) {
        this.rack.setNumber(number);
        this.rack.setLocationIdentifier(locate);
    }

    /**
     * Checks if the provided string is a valid URL.
     * This method attempts to create a {@link URL} object from the given string and checks if it is a valid URL format.
     * If the string is a valid URL, the method returns {@code true}. Otherwise, it returns {@code false}.
     *
     * @return {@code true} if the string represents a valid URL, {@code false} if it does not.
     */
    public boolean checkURL() {
        try {
            new URL(imgName).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException m) {
            return false;
        }
    }

    /**
     * Generates the file path for an image based on the provided image name.
     * This method constructs the full file path by appending the image name to a predefined base path.
     * If the image name is {@code null} or empty, a default image file name ("image.png") is used.
     *
     * @param imgName the name of the image file
     * @return the full file path for the image, including the base path and the image name
     */
    public String generateImagePathFromImageName(String imgName) {
        String basePath = "/asset/book/";
        if (imgName == null || imgName.trim().isEmpty()) {
            imgName = "image.png";
        }
        return basePath + imgName;
    }

    /**
     * Displays detailed information about the book.
     * This method prints various attributes of the book, including its ISBN, title, subject, publisher, language,
     * number of pages, author details, price, format, status, date of purchase, publication date, and image name (or URL).
     * It also prints additional attributes like whether the book is for reference only and its unique ID.
     */
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
        System.out.println("Image name (or url): " + imgName);
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
        return Objects.hash(id);
    }
}
