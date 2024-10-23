package com.example.proj.Models;

import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class Catalog {
    private String filePath;
    private Date creationDate;
    private int totalBooks;
    private Map<String, List<BookItem>> bookTitles;
    private Map<String, List<BookItem>> bookAuthors;
    private Map<String, List<BookItem>> bookSubjects;
    private Map<Date, List<BookItem>> bookPublicationDates;
    private Map<String, BookItem> bookId;
    private Map<BookStatus, List<BookItem>> bookStatus;

    // Constructor
    public Catalog() {
        File file = new File("src/main/resources/database/real_books.txt");
        if (file.exists()) {
            String absolute = file.getAbsolutePath();
            this.filePath = absolute;
        }
        this.creationDate = new Date(); // Ngày tạo là ngày hiện tại
        this.totalBooks = 0;
        this.bookTitles = new HashMap<>();
        this.bookAuthors = new HashMap<>();
        this.bookSubjects = new HashMap<>();
        this.bookPublicationDates = new HashMap<>();
        this.bookId = new HashMap<>();
        this.bookStatus = new HashMap<>();
    }

    public void setTotalBooks(int totalBooks) {
        this.totalBooks = totalBooks;
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

    public void setBookStatus(Map<BookStatus, List<BookItem>> bookStatus) {
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

    public Map<BookStatus, List<BookItem>> getBookStatus() {
        return bookStatus;
    }

    public void loadCatalogFromDatabase() {
        long startTime = System.currentTimeMillis();
        String jdbcURL = "jdbc:mysql://localhost:3306/shibalib";
        String username = "root";
        String password = "";

        String query = "SELECT ISBN, title, subject, publisher, language, numberOfPage, authorName, authorDescription, id, isReferenceOnly, price, format, status, dateOfPurchase, publicationDate, number, location FROM bookitem";

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
                Date dateOfPurchase = resultSet.getDate("dateOfPurchase");
                Date publicationDate = resultSet.getDate("publicationDate");
                int number = resultSet.getInt("number");
                String location = resultSet.getString("location");

                BookItem bookItem = new BookItem(ISBN, title, subject, publisher, language, numberOfPage,
                        authorName, authorDescription, id, isReferenceOnly, price, format, status,
                        dateOfPurchase, publicationDate, number, location);

                this.addBookItem(bookItem);
            }

            //System.out.println("Catalog loaded successfully from the database!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("load catalog from db elapsed time: " + (endTime - startTime) + " ms");
    }

    public void addBookItem(BookItem bookItem) {
        // Update titles
        bookTitles.computeIfAbsent(bookItem.getTitle().toLowerCase(), k -> new ArrayList<>()).add(bookItem);

        // Update authors
        String author = bookItem.getAuthor().getName().toLowerCase();
        bookAuthors.computeIfAbsent(author, k -> new ArrayList<>()).add(bookItem);

        // Update subjects
        String subject = bookItem.getSubject().toLowerCase();
        bookSubjects.computeIfAbsent(subject, k -> new ArrayList<>()).add(bookItem);

        // Update publication dates
        Date publicationDate = bookItem.getPublicationDate();
        bookPublicationDates.computeIfAbsent(publicationDate, k -> new ArrayList<>()).add(bookItem);

        // Update status
        BookStatus status = bookItem.getStatus();
        bookStatus.computeIfAbsent(status, k -> new ArrayList<>()).add(bookItem);

        // Update ID
        String id = bookItem.getId();
        bookId.put(id, bookItem);

        // Increment totalBooks
        totalBooks++;
    }

    public void writeBookItemToDatabase(BookItem bookItem) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Database connection details
        String jdbcURL = "jdbc:mysql://localhost:3306/shibalib";
        String dbUsername = "root";
        String dbPassword = "";

        String sql = "INSERT INTO bookitem (ISBN, title, subject, publisher, language, numberOfPage, authorName, authorDescription, id, isReferenceOnly, price, format, status, dateOfPurchase, publicationDate, number, location) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, bookItem.getISBN());
            statement.setString(2, bookItem.getTitle());
            statement.setString(3, bookItem.getSubject());
            statement.setString(4, bookItem.getPublisher());
            statement.setString(5, bookItem.getLanguage());
            statement.setString(6, bookItem.getNumberOfPage());
            statement.setString(7, bookItem.getAuthor().getName());
            statement.setString(8, bookItem.getAuthor().getDescription());
            statement.setString(9, bookItem.getId());
            statement.setString(10, bookItem.getIsReferenceOnly() ? "true" : "false");
            statement.setDouble(11, bookItem.getPrice());
            statement.setString(12, bookItem.getFormat().name());
            statement.setString(13, bookItem.getStatus().name());
            statement.setString(14, dateFormat.format(bookItem.getDateOfPurchase()));
            statement.setString(15, dateFormat.format(bookItem.getPublicationDate()));
            statement.setInt(16, bookItem.getRack().getNumber());
            statement.setString(17, bookItem.getRack().getLocationIdentifier());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new book item was inserted successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Error inserting the book item into the database: " + e.getMessage());
        }
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
        return bookSubjects.getOrDefault(title.toLowerCase(), new ArrayList<>());
    }

    public List<BookItem> findBooksByAuthor(String author) {
        return bookSubjects.getOrDefault(author.toLowerCase(), new ArrayList<>());
    }

    public List<BookItem> findBooksBySubject(String subject) {
        return bookSubjects.getOrDefault(subject.toLowerCase(), new ArrayList<>());
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
                BookItem updatedBookItem = this.bookId.get(bookId);
                BookItem oldBook = this.bookId.get(bookId);
                if (updatedBookItem != null) {
                    // Update the specific fields in the relevant maps
                    switch (fieldToEdit) {
                        case 1: // ISBN
                            updatedBookItem.setISBN(newValue);
                            this.removeBookById(oldBook.getId(), false);
                            this.addBookItem(updatedBookItem);
                            break;

                        case 2: // title
                            String oldTitle = updatedBookItem.getTitle();
                            updatedBookItem.setTitle(newValue);

                            // Remove the book by ID and add it back with updated title
                            this.removeBookById(oldBook.getId(), false);
                            this.addBookItem(updatedBookItem);
                            break;

                        case 3: // subject
                            String oldSubject = updatedBookItem.getSubject();
                            updatedBookItem.setSubject(newValue);

                            // Remove the book by ID and add it back with updated subject
                            this.removeBookById(oldBook.getId(), false);
                            this.addBookItem(updatedBookItem);
                            break;

                        case 4: // publisher
                            updatedBookItem.setPublisher(newValue);

                            // Remove the book by ID and add it back with updated publisher
                            this.removeBookById(oldBook.getId(), false);
                            this.addBookItem(updatedBookItem);
                            break;

                        case 5: // language
                            updatedBookItem.setLanguage(newValue);

                            // Remove the book by ID and add it back with updated language
                            this.removeBookById(oldBook.getId(), false);
                            this.addBookItem(updatedBookItem);
                            break;

                        case 6: // numberOfPage
                            updatedBookItem.setNumberOfPage(newValue); // Assuming newValue is a valid integer string

                            // Remove the book by ID and add it back with updated numberOfPage
                            this.removeBookById(oldBook.getId(), false);
                            this.addBookItem(updatedBookItem);
                            break;

                        case 7: // authorName
                            String oldAuthorName = updatedBookItem.getAuthor().getName();
                            updatedBookItem.getAuthor().setName(newValue); // Assuming Author has a method to set name

                            // Remove the book by ID and add it back with updated authorName
                            this.removeBookById(oldBook.getId(), false);
                            this.addBookItem(updatedBookItem);
                            break;

                        case 8: // authorDescription
                            updatedBookItem.getAuthor().setDescription(newValue); // Assuming Author has a method to set description

                            // Remove the book by ID and add it back with updated authorDescription
                            this.removeBookById(oldBook.getId(), false);
                            this.addBookItem(updatedBookItem);
                            break;

                        case 9: // id
                            System.out.println("ID cannot be changed.");
                            return;

                        case 10: // isReferenceOnly
                            updatedBookItem.setReferenceOnly(Boolean.parseBoolean(newValue)); // Assuming newValue is a valid boolean string

                            // Remove the book by ID and add it back with updated isReferenceOnly
                            this.removeBookById(oldBook.getId(), false);
                            this.addBookItem(updatedBookItem);
                            break;

                        case 11: // price
                            updatedBookItem.setPrice(Double.parseDouble(newValue)); // Assuming newValue is a valid double string

                            // Remove the book by ID and add it back with updated price
                            this.removeBookById(oldBook.getId(), false);
                            this.addBookItem(updatedBookItem);
                            break;

                        case 12: // format
                            updatedBookItem.setFormat(BookFormat.valueOf(newValue));

                            // Remove the book by ID and add it back with updated format
                            this.removeBookById(oldBook.getId(), false);
                            this.addBookItem(updatedBookItem);
                            break;

                        case 13: // status
                            BookStatus newStatus = BookStatus.valueOf(newValue.toUpperCase()); // Assuming newValue corresponds to a valid enum value
                            updatedBookItem.setStatus(newStatus);

                            // Remove the book by ID and add it back with updated status
                            this.removeBookById(oldBook.getId(), false);
                            this.addBookItem(updatedBookItem);
                            break;

                        case 14: // dateOfPurchase
                            try {
                                updatedBookItem.setDateOfPurchase(java.sql.Date.valueOf(newValue)); // Assuming newValue is in the format "yyyy-mm-dd"
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid date format for dateOfPurchase. Please use 'yyyy-mm-dd'.");
                                return;
                            } // Assuming newValue is in valid format

                            // Remove the book by ID and add it back with updated dateOfPurchase
                            this.removeBookById(oldBook.getId(), false);
                            this.addBookItem(updatedBookItem);
                            break;

                        case 15: // publicationDate
                            try {
                                updatedBookItem.setPublicationDate(java.sql.Date.valueOf(newValue)); // Assuming newValue is in the format "yyyy-mm-dd"
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid date format for publicationDate. Please use 'yyyy-mm-dd'.");
                                return;
                            } // Assuming newValue is in valid format

                            // Remove the book by ID and add it back with updated publicationDate
                            this.removeBookById(oldBook.getId(), false);
                            this.addBookItem(updatedBookItem);
                            break;

                        case 16: // number
                            updatedBookItem.setRack(Integer.parseInt(newValue), updatedBookItem.getRack().getLocationIdentifier()); // Assuming newValue is a valid integer string

                            // Remove the book by ID and add it back with updated number
                            this.removeBookById(oldBook.getId(), false);
                            this.addBookItem(updatedBookItem);
                            break;

                        case 17: // location
                            updatedBookItem.setRack(updatedBookItem.getRack().getNumber(), newValue);

                            // Remove the book by ID and add it back with updated location
                            this.removeBookById(oldBook.getId(), false);
                            this.addBookItem(updatedBookItem);
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

    public void editBook(String bookId, int fieldToEdit, String newValue) {
        List<String> lines = new ArrayList<>();
        boolean bookFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                if (fields[8].equals(bookId)) {
                    bookFound = true;
                    switch (fieldToEdit) {
                        case 1: fields[0] = newValue; break;  // ISBN
                        case 2: fields[1] = newValue; break;  // title
                        case 3: fields[2] = newValue; break;  // subject
                        case 4: fields[3] = newValue; break;  // publisher
                        case 5: fields[4] = newValue; break;  // language
                        case 6: fields[5] = newValue; break;  // numberOfPage
                        case 7: fields[6] = newValue; break;  // authorName
                        case 8: fields[7] = newValue; break;  // authorDescription
                        case 9: fields[8] = newValue; break;  // id
                        case 10: fields[9] = newValue; break; // isReferenceOnly
                        case 11: fields[10] = newValue; break; // price
                        case 12: fields[11] = newValue; break; // format
                        case 13: fields[12] = newValue; break; // status
                        case 14: fields[13] = newValue; break; // dateOfPurchase
                        case 15: fields[14] = newValue; break; // publicationDate
                        case 16: fields[15] = newValue; break; // number
                        case 17: fields[16] = newValue; break; // location
                        default:
                            System.out.println("Invalid field.");
                            return;
                    }
                }

                lines.add(String.join(";", fields));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ghi lại file với thông tin đã chỉnh sửa
        if (bookFound) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
                System.out.println("Book " + bookId + " has been updated.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.totalBooks = 0;
            // load again.
            this.bookSubjects = new HashMap<>();
            this.bookAuthors = new HashMap<>();
            this.bookTitles = new HashMap<>();
            this.bookId = new HashMap<>();
            this.bookPublicationDates = new HashMap<>();
            this.ImportFromFile();
        } else {
            System.out.println("Book with ID " + bookId + " not found.");
        }
    }

    public void ImportFromFile() {
        try (BufferedReader br  = new BufferedReader(new FileReader(this.filePath))) {
            String line;
            //br.readLine();
            while ((line = br.readLine()) != null) {
                String[] tmp = line.split(";");
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

    public boolean removeBookById(String id, boolean inDatabase) {
        long startTime = System.currentTimeMillis();

        if (!bookId.containsKey(id)) {
            System.out.println("Book not found!");
            return false;
        }

        BookItem bookToRemove = this.bookId.get(id);

        // Create threads
        Thread titleThread = new Thread(() -> {
            String title = bookToRemove.getTitle().toLowerCase();
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
            String author = bookToRemove.getAuthor().getName().toLowerCase();
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
            String subject = bookToRemove.getSubject().toLowerCase();
            List<BookItem> rmFromSubject = bookSubjects.get(subject);
            if (rmFromSubject != null) {
                synchronized (rmFromSubject) {
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

        // start
        titleThread.start();
        authorThread.start();
        subjectThread.start();
        dateThread.start();

        // wait for all threads to finish
        try {
            titleThread.join();
            authorThread.join();
            subjectThread.join();
            dateThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

        this.bookId.remove(id);

        if (inDatabase == true) {
            this.removeBookFromDatabase(id);
        }
        totalBooks--;

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
