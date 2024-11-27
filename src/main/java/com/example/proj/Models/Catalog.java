package com.example.proj.Models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class Catalog {
    private String filePath;
    private Date creationDate;
    private SimpleIntegerProperty totalBooks;
    private Map<String, List<BookItem>> bookTitles;
    private Map<String, List<BookItem>> bookAuthors;
    private Map<String, List<BookItem>> bookSubjects;
    private Map<Date, List<BookItem>> bookPublicationDates;
    private Map<String, BookItem> bookId;
    private Map<BookStatus, ObservableList<BookItem>> bookStatus;

    private static Catalog instance;

    // Constructor
    private Catalog() {
        File file = new File("src/main/resources/database/real_books.txt");
        if (file.exists()) {
            String absolute = file.getAbsolutePath();
            this.filePath = absolute;
        }
        this.creationDate = new Date(); // Ngày tạo là ngày hiện tại
        this.totalBooks = new SimpleIntegerProperty(0);
        this.bookTitles = new HashMap<>();
        this.bookAuthors = new HashMap<>();
        this.bookSubjects = new HashMap<>();
        this.bookPublicationDates = new HashMap<>();
        this.bookId = new HashMap<>();
        this.bookStatus = new HashMap<>();
    }

    public static Catalog getInstance() {
        if (instance == null) {
            instance = new Catalog(); // Nếu chưa có instance, tạo mới
            instance.loadCatalogFromDatabase();
        }
        return instance;
    }

    public void setTotalBooks(int totalBooks) {
        this.totalBooks.set(totalBooks);
    }

    public Map<String, List<BookItem>> getBookTitles() {
        return bookTitles;
    }

    public void setBookTitles(Map<String, List<BookItem>> bookTitles) {
        this.bookTitles = bookTitles;
    }

    public Map<String, List<BookItem>> getBookAuthors() {
        return bookAuthors;
    }

    public void setBookAuthors(Map<String, List<BookItem>> bookAuthors) {
        this.bookAuthors = bookAuthors;
    }

    public void setBookSubjects(Map<String, List<BookItem>> bookSubjects) {
        this.bookSubjects = bookSubjects;
    }

    public Map<Date, List<BookItem>> getBookPublicationDates() {
        return bookPublicationDates;
    }

    public void setBookPublicationDates(Map<Date, List<BookItem>> bookPublicationDates) {
        this.bookPublicationDates = bookPublicationDates;
    }

    public void setBookId(Map<String, BookItem> bookId) {
        this.bookId = bookId;
    }

    public void setBookStatus(Map<BookStatus, ObservableList<BookItem>> bookStatus) {
        this.bookStatus = bookStatus;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Map<String, List<BookItem>> getBookSubjects() {
        return bookSubjects;
    }

    public Map<String, BookItem> getBookId() {
        return bookId;
    }

    public Map<BookStatus, ObservableList<BookItem>> getBookStatus() {
        return bookStatus;
    }

    public void loadCatalogFromDatabase() {
        long startTime = System.currentTimeMillis();
        String jdbcURL = "jdbc:mysql://localhost:3306/shibalib";
        String username = "root";
        String password = "";

        String query = "SELECT ISBN, title, subject, publisher, language, numberOfPage, authorName, authorDescription, id, isReferenceOnly, price, format, status, dateOfPurchase, publicationDate, number, location, imgName FROM bookitem";

        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String ISBN = resultSet.getString("ISBN");
                String title = resultSet.getString("title");
                String subject = resultSet.getString("subject");
                String publisher = resultSet.getString("publisher");
                String language = resultSet.getString("language");
                String numberOfPage = resultSet.getString("numberOfPage");
                String authorName = resultSet.getString("authorName");
                String authorDescription = resultSet.getString("authorDescription");
                String id = resultSet.getString("id");
                boolean isReferenceOnly = resultSet.getBoolean("isReferenceOnly");
                double price = resultSet.getDouble("price");
                BookFormat format = BookFormat.valueOf(resultSet.getString("format"));
                BookStatus status = BookStatus.valueOf(resultSet.getString("status"));
                Date dateOfPurchase = new Date(resultSet.getDate("dateOfPurchase").getTime());
                Date publicationDate = new Date(resultSet.getDate("publicationDate").getTime());
                int number = resultSet.getInt("number");
                String location = resultSet.getString("location");
                String imageName = resultSet.getString("imgName");

                BookItem bookItem = new BookItem(ISBN, title, subject, publisher, language, numberOfPage,
                        authorName, authorDescription, id, isReferenceOnly, price, format, status,
                        dateOfPurchase, publicationDate, number, location, imageName);

                this.addBookItem(bookItem, false);
            }

            //System.out.println("Catalog loaded successfully from the database!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("load catalog from db elapsed time: " + (endTime - startTime) + " ms");
    }

    public void addBookItem(BookItem bookItem, boolean isEdit) {
        // Update titles
        bookTitles.computeIfAbsent(bookItem.getTitle().toUpperCase(), k -> new ArrayList<BookItem>() {
        }).add(bookItem);

        // Update authors
        String author = bookItem.getAuthor().getName().toUpperCase();
        bookAuthors.computeIfAbsent(author, k -> new ArrayList<>()).add(bookItem);

        // Update subjects
        String subject = bookItem.getSubject().toUpperCase();
        bookSubjects.computeIfAbsent(subject, k -> new ArrayList<>()).add(bookItem);

        // Update publication dates
        Date publicationDate = bookItem.getPublicationDate();
        bookPublicationDates.computeIfAbsent(publicationDate, k -> new ArrayList<>()).add(bookItem);

        // Update status
        BookStatus status = bookItem.getStatus();
        bookStatus.computeIfAbsent(status, k -> FXCollections.observableArrayList()).add(bookItem);

        // Update ID
        if (!isEdit) {
            String id = bookItem.getId();
            bookId.put(id, bookItem);

            // Increment totalBooks
            totalBooks.set(totalBooks.get() + 1);
        }
    }

    public int writeBookItemToDatabaseAndReturnId(BookItem bookItem) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Database connection details
        String jdbcURL = "jdbc:mysql://localhost:3306/shibalib";
        String dbUsername = "root";
        String dbPassword = "";

        String sql = "INSERT INTO bookitem (ISBN, title, subject, publisher, language, numberOfPage, authorName, authorDescription, id, isReferenceOnly, price, format, status, dateOfPurchase, publicationDate, number, location) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int generatedId = -1;

        try (Connection connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set parameters
            statement.setString(1, bookItem.getISBN());
            statement.setString(2, bookItem.getTitle());
            statement.setString(3, bookItem.getSubject());
            statement.setString(4, bookItem.getPublisher());
            statement.setString(5, bookItem.getLanguage());
            statement.setString(6, bookItem.getNumberOfPage());
            statement.setString(7, bookItem.getAuthor().getName());
            statement.setString(8, bookItem.getAuthor().getDescription());
            statement.setString(9, null);
            statement.setString(10, bookItem.getIsReferenceOnly() ? "true" : "false");
            statement.setDouble(11, bookItem.getPrice());
            statement.setString(12, bookItem.getFormat().name());
            statement.setString(13, bookItem.getStatus().name());
            statement.setString(14, dateFormat.format(bookItem.getDateOfPurchase()));
            statement.setString(15, dateFormat.format(bookItem.getPublicationDate()));
            statement.setInt(16, bookItem.getRack().getNumber());
            statement.setString(17, bookItem.getRack().getLocationIdentifier());

            // Execute the insert and retrieve the generated keys
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getInt(1); // Retrieve the generated ID
                        System.out.println("A new book item was inserted successfully with ID: " + generatedId);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error inserting the book item into the database: " + e.getMessage());
        }

        return generatedId; // Return the generated ID
    }


    public void writeBookItemToFile(BookItem bookItem) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String filePath = this.filePath;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            File file = new File(filePath);
            if (file.length() > 0) {
                writer.newLine();
            }
            writer.write(bookItem.getISBN() + ";" +
                    bookItem.getTitle() + ";" +
                    bookItem.getSubject() + ";" +
                    bookItem.getPublisher() + ";" +
                    bookItem.getLanguage() + ";" +
                    bookItem.getNumberOfPage() + ";" +
                    bookItem.getAuthor().getName() + ";" +
                    bookItem.getAuthor().getDescription() + ";" +
                    bookItem.getId() + ";" +
                    bookItem.getIsReferenceOnly() + ";" +
                    bookItem.getPrice() + ";" +
                    bookItem.getFormat() + ";" +
                    bookItem.getStatus() + ";" +
                    dateFormat.format(bookItem.getDateOfPurchase()) + ";" +
                    dateFormat.format(bookItem.getPublicationDate()) + ";" +
                    bookItem.getRack().getNumber() + ";" +
                    bookItem.getRack().getLocationIdentifier());

            //writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public BookItem findBookById(String id) {
        return bookId.get(id);
    }

    public List<BookItem> findBooksByTitle(String title) {
        List<BookItem> matchingBooks = new ArrayList<>();
        for (String innerTitle  : bookTitles.keySet()) {
            if (innerTitle.toUpperCase().contains(title.toUpperCase())) {
                matchingBooks.addAll(bookTitles.get(innerTitle));
            }
        }
        return matchingBooks;
    }

    public List<BookItem> findBooksByAuthor(String author) {
        List<BookItem> matchingBooks = new ArrayList<>();
        for (String innerAuthor  : bookAuthors.keySet()) {
            if (innerAuthor.toUpperCase().contains(author.toUpperCase())) {
                matchingBooks.addAll(bookAuthors.get(innerAuthor));
            }
        }
        return matchingBooks;
    }

    public List<BookItem> findBooksBySubject(String subject) {
        return bookSubjects.getOrDefault(subject.toUpperCase(), new ArrayList<>());
    }

    public List<BookItem> findBooksByPublicationDate(Date publicationDate) {
        return bookPublicationDates.getOrDefault(publicationDate, new ArrayList<>());
    }

    public boolean updateCatalog() {
        // Logic để cập nhật catalog
        return true; // Trả về true nếu thành công
    }

    public SimpleIntegerProperty getTotalBooks() {
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

    public void editBookInDataBase(String bookId, int fieldToEdit, String newValue) {
        if (this.findBookById(bookId) == null) {
            return;
        }
        long startTime = System.currentTimeMillis();
        String query = "";

        switch (fieldToEdit) {
            case 1: // ISBN
                query = "UPDATE bookitem SET ISBN = ? WHERE id = ?";
                break;
            case 2: // title
                query = "UPDATE bookitem SET title = ? WHERE id = ?";
                break;
            case 3: // subject
                query = "UPDATE bookitem SET subject = ? WHERE id = ?";
                break;
            case 4: // publisher
                query = "UPDATE bookitem SET publisher = ? WHERE id = ?";
                break;
            case 5: // language
                query = "UPDATE bookitem SET language = ? WHERE id = ?";
                break;
            case 6: // numberOfPage
                query = "UPDATE bookitem SET numberOfPage = ? WHERE id = ?";
                break;
            case 7: // authorName
                query = "UPDATE bookitem SET authorName = ? WHERE id = ?";
                break;
            case 8: // authorDescription
                query = "UPDATE bookitem SET authorDescription = ? WHERE id = ?";
                break;
            case 9: // id
                System.out.println("ID cannot be changed.");
                return;
            case 10: // isReferenceOnly
                query = "UPDATE bookitem SET isReferenceOnly = ? WHERE id = ?";
                break;
            case 11: // price
                query = "UPDATE bookitem SET price = ? WHERE id = ?";
                break;
            case 12: // format
                query = "UPDATE bookitem SET format = ? WHERE id = ?";
                break;
            case 13: // status
                query = "UPDATE bookitem SET status = ? WHERE id = ?";
                break;
            case 14: // dateOfPurchase
                query = "UPDATE bookitem SET dateOfPurchase = ? WHERE id = ?";
                break;
            case 15: // publicationDate
                query = "UPDATE bookitem SET publicationDate = ? WHERE id = ?";
                break;
            case 16: // number
                query = "UPDATE bookitem SET number = ? WHERE id = ?";
                break;
            case 17: // location
                query = "UPDATE bookitem SET location = ? WHERE id = ?";
                break;
            case 18: // image name
                query = "UPDATE bookitem SET imgName = ? WHERE id = ?";
                break;
            default:
                System.out.println("Invalid field.");
                return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shibalib", "root", "");
             PreparedStatement statement = connection.prepareStatement(query)) {


            statement.setString(1, newValue);
            statement.setString(2, bookId);


            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                BookItem oldBookItem = this.bookId.get(bookId);
                if (oldBookItem != null) {
                    // Update the specific fields in the relevant maps
                    switch (fieldToEdit) {
                        case 1: // ISBN
                            this.removeBookById(oldBookItem.getId(), false, true);
                            oldBookItem.setISBN(newValue);
                            this.addBookItem(oldBookItem, true);
                            break;

                        case 2: // title
                            this.removeBookById(oldBookItem.getId(), false, true);
                            oldBookItem.setTitle(newValue);
                            this.addBookItem(oldBookItem, true);
                            break;

                        case 3: // subject
                            this.removeBookById(oldBookItem.getId(), false, true);
                            oldBookItem.setSubject(newValue);
                            this.addBookItem(oldBookItem, true);
                            break;

                        case 4: // publisher
                            this.removeBookById(oldBookItem.getId(), false, true);
                            oldBookItem.setPublisher(newValue);
                            this.addBookItem(oldBookItem, true);
                            break;

                        case 5: // language
                            this.removeBookById(oldBookItem.getId(), false, true);
                            oldBookItem.setLanguage(newValue);
                            this.addBookItem(oldBookItem, true);
                            break;

                        case 6: // numberOfPage
                            this.removeBookById(oldBookItem.getId(), false, true);
                            oldBookItem.setNumberOfPage(newValue);
                            this.addBookItem(oldBookItem, true);
                            break;

                        case 7: // authorName
                            this.removeBookById(oldBookItem.getId(), false, true);
                            oldBookItem.getAuthor().setName(newValue);
                            this.addBookItem(oldBookItem, true);
                            break;

                        case 8: // authorDescription
                            this.removeBookById(oldBookItem.getId(), false, true);
                            oldBookItem.getAuthor().setDescription(newValue);
                            this.addBookItem(oldBookItem, true);
                            break;

                        case 9: // id
                            System.out.println("ID cannot be changed.");
                            return;

                        case 10: // isReferenceOnly
                            this.removeBookById(oldBookItem.getId(), false, true);
                            oldBookItem.setReferenceOnly(Boolean.parseBoolean(newValue));
                            this.addBookItem(oldBookItem, true);
                            break;

                        case 11: // price
                            this.removeBookById(oldBookItem.getId(), false, true);
                            oldBookItem.setPrice(Double.parseDouble(newValue));
                            this.addBookItem(oldBookItem, true);
                            break;

                        case 12: // format
                            this.removeBookById(oldBookItem.getId(), false, true);
                            oldBookItem.setFormat(BookFormat.valueOf(newValue));
                            this.addBookItem(oldBookItem, true);
                            break;

                        case 13: // status
                            // Remove the book by ID and add it back with updated status
                            this.removeBookById(oldBookItem.getId(), false, true);

                            BookStatus newStatus = BookStatus.valueOf(newValue.toUpperCase()); // Assuming newValue corresponds to a valid enum value
                            oldBookItem.setStatus(newStatus);

                            this.addBookItem(oldBookItem, true);
                            break;

                        case 14: // dateOfPurchase
                            // Remove the book by ID and add it back with updated dateOfPurchase
                            this.removeBookById(oldBookItem.getId(), false, true);

                            try {
                                oldBookItem.setDateOfPurchase(java.sql.Date.valueOf(newValue)); // Assuming newValue is in the format "yyyy-mm-dd"
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid date format for dateOfPurchase. Please use 'yyyy-mm-dd'.");
                                return;
                            } // Assuming newValue is in valid format

                            this.addBookItem(oldBookItem, true);
                            break;

                        case 15: // publicationDate
                            // Remove the book by ID and add it back with updated publicationDate
                            this.removeBookById(oldBookItem.getId(), false, true);

                            try {
                                oldBookItem.setPublicationDate(java.sql.Date.valueOf(newValue)); // Assuming newValue is in the format "yyyy-mm-dd"
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid date format for publicationDate. Please use 'yyyy-mm-dd'.");
                                return;
                            } // Assuming newValue is in valid format

                            this.addBookItem(oldBookItem, true);
                            break;

                        case 16: // number
                            // Remove the book by ID and add it back with updated number
                            this.removeBookById(oldBookItem.getId(), false, true);

                            oldBookItem.setRack(Integer.parseInt(newValue), oldBookItem.getRack().getLocationIdentifier()); // Assuming newValue is a valid integer string

                            this.addBookItem(oldBookItem, true);
                            break;

                        case 17: // location
                            // Remove the book by ID and add it back with updated location
                            this.removeBookById(oldBookItem.getId(), false, true);

                            oldBookItem.setRack(oldBookItem.getRack().getNumber(), newValue);

                            this.addBookItem(oldBookItem, true);
                            break;
                        default:
                            System.out.println("Invalid field.");
                            return;
                    }

                }
//                this.totalBooks = 0;
//                this.bookSubjects = new HashMap<>();
//                this.bookAuthors = new HashMap<>();
//                this.bookTitles = new HashMap<>();
//                this.bookId = new HashMap<>();
//                this.bookPublicationDates = new HashMap<>();
//                this.loadCatalogFromDatabase();
                System.out.println("Book " + bookId + " has been updated.");

            } else {
                System.out.println("No book found with ID " + bookId + ".");
            }

        } catch (SQLException e) {
            System.out.println("Error updating the book in the database: " + e.getMessage());
        }
        long endTime = System.currentTimeMillis();
        System.out.println("update elapsed time: " + (endTime - startTime) + " ms");
    }


    public boolean removeBookById(String id, boolean inDatabase, boolean isEdit) {
        long startTime = System.currentTimeMillis();

        if (!bookId.containsKey(id)) {
            System.out.println("Book not found!");
            return false;
        }

        BookItem bookToRemove = this.bookId.get(id);

        // Create threads
        Thread titleThread = new Thread(() -> {
            String title = bookToRemove.getTitle().toUpperCase();
            List<BookItem> rmFromTitle = this.bookTitles.get(title);
            if (rmFromTitle != null) {
                synchronized (rmFromTitle) {
                    rmFromTitle.remove(bookToRemove);
                    if (rmFromTitle.isEmpty()) {
                        bookTitles.remove(title);
                    }
                }
            }
        });



        Thread authorThread = new Thread(() -> {
            String author = bookToRemove.getAuthor().getName().toUpperCase();
            List<BookItem> rmFromAuthor = bookAuthors.get(author);
            if (rmFromAuthor != null) {
                synchronized (rmFromAuthor) {
                    rmFromAuthor.remove(bookToRemove);
                    if (rmFromAuthor.isEmpty()) {
                        bookAuthors.remove(author);
                    }
                }
            }
        });

        Thread subjectThread = new Thread(() -> {
            String subject = bookToRemove.getSubject().toUpperCase();
            List<BookItem> rmFromSubject = bookSubjects.get(subject);
            if (rmFromSubject != null) {
                synchronized (rmFromSubject) {
                    //rmFromSubject.removeIf(book -> book.getId().equals(bookToRemove.getId()));
                    rmFromSubject.remove(bookToRemove);
                    if (rmFromSubject.isEmpty()) {
                        bookSubjects.remove(subject);
                    }
                }
            }
        });

        Thread dateThread = new Thread(() -> {
            Date date = bookToRemove.getPublicationDate();
            List<BookItem> rmFromDate = bookPublicationDates.get(date);
            if (rmFromDate != null) {
                synchronized (rmFromDate) {
                    rmFromDate.remove(bookToRemove);
                    if (rmFromDate.isEmpty()) {
                        bookPublicationDates.remove(date);
                    }
                }
            }
        });

        Thread statusThread = new Thread(() -> {
            BookStatus status = bookToRemove.getStatus();
            if (bookStatus.get(status) != null) {
                synchronized (bookStatus.get(status)) {
                    bookStatus.get(status).remove(bookToRemove);
                    if (bookStatus.get(status).isEmpty()) {
                        bookStatus.remove(status);
                    }
                }
            }
        });

        // start
        titleThread.start();
        authorThread.start();
        subjectThread.start();
        dateThread.start();
        statusThread.start();

        // wait for all threads to finish
        try {
            titleThread.join();
            authorThread.join();
            subjectThread.join();
            dateThread.join();
            statusThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        if (!isEdit) {
            this.bookId.remove(id);
            totalBooks.set(totalBooks.get() - 1);
        }
        if (inDatabase == true) {
            this.removeBookFromDatabase(id);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("remove book elapsed time: " + (endTime - startTime) + " ms");
        return true;
    }

    private void removeBookFromDatabase(String id) {
        String query = "DELETE FROM bookitem WHERE id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shibalib", "root", "");
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, id);

            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("A book was successfully deleted!");
            } else {
                System.out.println("No book found with the given ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error deleting the book from the database: " + e.getMessage());
        }
    }


    private void removeBookFromFile(String id) {
        File inputFile = new File(this.filePath);
        File tempFile = new File("temp_book_items.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                String[] fields = currentLine.split(";");
                String idt = fields[8];

                if (idt.equals(id)) {
                    continue;
                }

                writer.write(currentLine);
                writer.newLine();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!inputFile.delete()) {
            System.out.println("Could not delete the original file");
            return;
        }
        if (!tempFile.renameTo(inputFile)) {
            System.out.println("Could not rename the temp file to the original file");
            return;
        }
    }

    public void displayCatalogInfo() {
        System.out.println("Catalog Creation Date: " + creationDate);
        System.out.println("Total Books in Catalog: " + totalBooks);
    }
}
