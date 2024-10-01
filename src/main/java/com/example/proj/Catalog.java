package com.example.proj;

import java.util.*;

public class Catalog {
    private Date creationDate;
    private int totalBooks;
    private Map<String, List<BookItem>> bookTitles;
    private Map<String, List<BookItem>> bookAuthors;
    private Map<String, List<BookItem>> bookSubjects;
    private Map<Date, List<BookItem>> bookPublicationDates;
    private Map<String, BookItem> bookId;
    // Constructor
    public Catalog() {
        this.creationDate = new Date(); // Ngày tạo là ngày hiện tại
        this.totalBooks = 0;
        this.bookTitles = new HashMap<>();
        this.bookAuthors = new HashMap<>();
        this.bookSubjects = new HashMap<>();
        this.bookPublicationDates = new HashMap<>();
        this.bookId = new HashMap<>();
    }


    public void addBookItem(BookItem bookItem) {

        bookTitles.computeIfAbsent(bookItem.getTitle().toLowerCase(), k -> new ArrayList<>()).add(bookItem);


        String author = bookItem.getAuthor().getName().toLowerCase();   // Giả định rằng BookItem có phương thức getAuthor()
        bookAuthors.computeIfAbsent(author, k -> new ArrayList<>()).add(bookItem);


        String subject = bookItem.getSubject().toLowerCase(); // Giả định rằng BookItem có phương thức getSubject()
        bookSubjects.computeIfAbsent(subject, k -> new ArrayList<>()).add(bookItem);


        Date publicationDate = bookItem.getPublicationDate(); // Giả định rằng BookItem có phương thức getPublicationDate()
        bookPublicationDates.computeIfAbsent(publicationDate, k -> new ArrayList<>()).add(bookItem);

        String id = bookItem.getId();
        bookId.put(id, bookItem);

        totalBooks++;
    }

    public BookItem findBookById(String id) {
        return bookId.get(id);
    }

    public List<BookItem> findBooksByTitle(String title) {
        List<BookItem> matchingBooks = new ArrayList<>();
        for (String innerTitle  : bookTitles.keySet()) {
            if (innerTitle.toLowerCase().contains(title.toLowerCase())) {
                matchingBooks.addAll(bookTitles.get(innerTitle));
            }
        }
        return matchingBooks;
    }


    public List<BookItem> findBooksByAuthor(String author) {
        return bookAuthors.getOrDefault(author, new ArrayList<>());
    }


    public List<BookItem> findBooksBySubject(String subject) {
        return bookSubjects.getOrDefault(subject, new ArrayList<>());
    }


    public List<BookItem> findBooksByPublicationDate(Date publicationDate) {
        return bookPublicationDates.getOrDefault(publicationDate, new ArrayList<>());
    }


    public boolean updateCatalog() {
        // Logic để cập nhật catalog
        return true; // Trả về true nếu thành công
    }


    public int getTotalBooks() {
        return totalBooks;
    }


    public void displayCatalogInfo() {
        System.out.println("Catalog Creation Date: " + creationDate);
        System.out.println("Total Books in Catalog: " + totalBooks);
    }
}
