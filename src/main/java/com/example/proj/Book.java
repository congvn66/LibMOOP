package com.example.proj;

public abstract class Book {
    private String ISBN;
    private String title;
    private String subject;
    private String publisher;
    private String language;
    private String numberOfPage;
    private Author author;

    // Constructor
    public Book(String ISBN, String title, String subject, String publisher,
                String language, String numberOfPage, String authorName, String authorDescription) {
        this.ISBN = ISBN;
        this.title = title;
        this.subject = subject;
        this.publisher = publisher;
        this.language = language;
        this.numberOfPage = numberOfPage;
        this.author = new Author(authorName, authorDescription);
    }

    // Getter & Setter
    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getNumberOfPage() {
        return numberOfPage;
    }

    public void setNumberOfPage(String numberOfPage) {
        this.numberOfPage = numberOfPage;
    }

    public Author getAuthor() {
        return this.author;
    }

    // Phương thức abstract, các lớp con cần phải triển khai phương thức này
    public abstract void displayInfo();
}
