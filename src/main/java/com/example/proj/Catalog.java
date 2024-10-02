package com.example.proj;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private Date parseDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust format as necessary
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Handle error appropriately
        }
    }
    public void ImportFromFile(String path) {
        try (BufferedReader br  = new BufferedReader(new FileReader(path))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] tmp = line.split(",");
                if (tmp.length != 17) {
                    continue;
                }
                String ISBN = tmp[0].trim();
                String title = tmp[1].trim();
                String subject = tmp[2].trim();
                String publisher = tmp[3].trim();
                String language = tmp[4].trim();
                String numberOfPage = tmp[5].trim();
                String authorName = tmp[6].trim();
                String authorDescription = tmp[7].trim();
                String id = tmp[8].trim();
                Boolean isRefOnly = Boolean.parseBoolean(tmp[9].trim());
                double price = Double.parseDouble(tmp[10].trim());
                String bookFormat = tmp[11].trim();
                String bookStatus = tmp[12].trim();
                Date dateOfPurchase = parseDate(tmp[13].trim());
                Date publicationDate = parseDate(tmp[14].trim());
                int number = Integer.parseInt(tmp[15].trim());
                String location = tmp[16].trim();

                BookItem addin = new BookItem(ISBN, title, subject, publisher, language, numberOfPage, authorName,authorDescription,
                        id, isRefOnly, price, BookFormat.valueOf(bookFormat.toUpperCase()), BookStatus.valueOf(bookStatus.toUpperCase()),
                        dateOfPurchase, publicationDate, number, location);

                this.addBookItem(addin);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot open file!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void displayCatalogInfo() {
        System.out.println("Catalog Creation Date: " + creationDate);
        System.out.println("Total Books in Catalog: " + totalBooks);
        for (String i : this.bookId.keySet()) {
            this.bookId.get(i).displayInfo();
        }
    }
}
