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
    private Date creationDate;
    private SimpleIntegerProperty totalBooks;
    private Map<String, List<BookItem>> bookTitles;
    private Map<String, List<BookItem>> bookAuthors;
    private Map<String, List<BookItem>> bookSubjects;
    private Map<Date, List<BookItem>> bookPublicationDates;
    private Map<String, BookItem> bookId;
    private Map<String, ObservableList<BookItem>> bookStatus;
    private static Catalog instance = null;

    /**
     * Private constructor for initializing a Catalog object.
     * This constructor initializes the catalog with the current date and sets up various maps
     * to store books by different attributes such as title, author, subject, publication date,
     * book ID, and status. Additionally, it initializes the total number of books to zero.
     */
    private Catalog() {
        // current date.
        this.creationDate = new Date();

        // java fx stuff for detecting the changes.
        this.totalBooks = new SimpleIntegerProperty(0);

        // books' map
        this.bookTitles = new LinkedHashMap<>();
        this.bookAuthors = new LinkedHashMap<>();
        this.bookSubjects = new LinkedHashMap<>();
        this.bookPublicationDates = new HashMap<>();
        this.bookId = new LinkedHashMap<>();
        this.bookStatus = new LinkedHashMap<>();
    }

    /**
     * Returns the singleton instance of the Catalog class.
     * If the instance does not exist, it is created and initialized by loading
     * the catalog data from the database.
     *
     * @return The singleton instance of the Catalog class.
     */
    public static Catalog getInstance() {
        if (instance == null) {
            instance = new Catalog();
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

    public void setBookStatus(Map<String, ObservableList<BookItem>> bookStatus) {
        this.bookStatus = bookStatus;
    }

    public SimpleIntegerProperty getTotalBooks() {
        return totalBooks;
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

    public Map<String, ObservableList<BookItem>> getBookStatus() {
        return bookStatus;
    }

    /**
     * Loads the catalog data from the database and initializes the catalog with book items.
     * This method executes a SQL query to retrieve all records from the `bookitem` table in the database,
     * then iterates over the result set to create `BookItem` objects and adds them to the catalog.
     * The method tracks the time it takes to load the catalog data.
     */
    public void loadCatalogFromDatabase() {
        long startTime = System.currentTimeMillis();

        // sql query.
        String query = "SELECT * FROM bookitem";

        // connect to database, execute by using prepared statement, then save result in set.
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // traverse
            while (resultSet.next()) {
                // get data.
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

                // create book item and add.
                BookItem bookItem = new BookItem(ISBN, title, subject, publisher, language, numberOfPage,
                        authorName, authorDescription, id, isReferenceOnly, price, format, status,
                        dateOfPurchase, publicationDate, number, location, imageName);
                this.addBookItem(bookItem, false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("load catalog from db elapsed time: " + (endTime - startTime) + " ms");
    }

    /**
     * Adds a {@link BookItem} to the catalog and updates the relevant maps.
     * This method updates various fields like title, author, subject, publication date,
     * status, and ID in the corresponding maps. If the book is being added for the first time
     * (not an edit), it also updates the book ID map and increments the total number of books.
     *
     * @param bookItem the {@link BookItem} to be added to the catalog
     * @param isEdit a boolean flag indicating whether the book item is being edited (true) or newly added (false)
     */
    public void addBookItem(BookItem bookItem, boolean isEdit) {

        // update maps, if existed, add in the list, else create a new list and add the book.
        bookTitles.computeIfAbsent(bookItem.getTitle().toUpperCase(), k -> new ArrayList<BookItem>() {
        }).add(bookItem);

        String author = bookItem.getAuthor().getName().toUpperCase();
        bookAuthors.computeIfAbsent(author, k -> new ArrayList<>()).add(bookItem);

        String subject = bookItem.getSubject().toUpperCase();
        bookSubjects.computeIfAbsent(subject, k -> new ArrayList<>()).add(bookItem);

        Date publicationDate = bookItem.getPublicationDate();
        bookPublicationDates.computeIfAbsent(publicationDate, k -> new ArrayList<>()).add(bookItem);

        BookStatus status = bookItem.getStatus();
        bookStatus.computeIfAbsent(status.toString(), k -> FXCollections.observableArrayList()).add(bookItem);

        // if it is a new addition, add to the ID map
        if (!isEdit) {
            String id = bookItem.getId();
            bookId.put(id, bookItem);

            // Increment totalBooks
            totalBooks.set(totalBooks.get() + 1);
        }
    }

    /**
     * Inserts a new {@link BookItem} into the database and returns the generated ID.
     * This method performs an SQL `INSERT` operation to store the book's details in the `bookitem` table.
     * If the insertion is successful, it returns the generated ID for the new book item.
     * If the insertion fails, it returns -1.
     *
     * @param bookItem the {@link BookItem} to be inserted into the database
     * @return the generated ID for the new book item, or -1 if the insertion fails
     */
    public int writeBookItemToDatabaseAndReturnId(BookItem bookItem) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Database connection details
        String jdbcURL = "jdbc:mysql://localhost:3306/shibalib";
        String dbUsername = "root";
        String dbPassword = "";

        // SQL query
        String sql = "INSERT INTO bookitem (ISBN, title, subject, publisher, language, numberOfPage, authorName, authorDescription, id, isReferenceOnly, price, format, status, dateOfPurchase, publicationDate, number, location, imgName) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int generatedId = -1;

        // database connection and statement's configuration (for return last inserted id)
        try (Connection connection = DatabaseConnection.getConnection();
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
            statement.setString(18, bookItem.getImgName());

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

        return generatedId;
    }

    /**
     * Retrieves a {@link BookItem} from the catalog based on its unique ID.
     * This method looks up the book in the catalog using the provided ID.
     * If a matching book is found, it returns the corresponding {@link BookItem}.
     * If no book is found with the given ID, it returns {@code null}.
     *
     * @param id the unique identifier of the book to be retrieved
     * @return the {@link BookItem} associated with the given ID, or {@code null} if no such book exists
     */
    public BookItem findBookById(String id) {
        return bookId.get(id);
    }

    /**
     * Searches for books in the catalog by their title.
     * This method performs a case-insensitive search and returns a list of books whose titles
     * contain the specified keyword.
     *
     * @param title the title or partial title of the books to search for
     * @return a list of {@link BookItem} objects that match the given title keyword
     */
    public List<BookItem> findBooksByTitle(String title) {
        List<BookItem> matchingBooks = new ArrayList<>();
        for (String innerTitle  : bookTitles.keySet()) {
            if (innerTitle.toUpperCase().contains(title.toUpperCase())) {
                matchingBooks.addAll(bookTitles.get(innerTitle));
            }
        }
        return matchingBooks;
    }

    /**
     * Searches for books in the catalog by their author's name.
     * This method performs a case-insensitive search and returns a list of books whose authors
     * contain the specified keyword.
     *
     * @param author the name or partial name of the author to search for
     * @return a list of {@link BookItem} objects that match the given author keyword
     */
    public List<BookItem> findBooksByAuthor(String author) {
        List<BookItem> matchingBooks = new ArrayList<>();
        for (String innerAuthor  : bookAuthors.keySet()) {
            if (innerAuthor.toUpperCase().contains(author.toUpperCase())) {
                matchingBooks.addAll(bookAuthors.get(innerAuthor));
            }
        }
        return matchingBooks;
    }

    /**
     * Searches for books in the catalog by their subject.
     * This method performs a case-insensitive search and returns a list of books that match the specified subject.
     *
     * @param subject the subject of the books to search for
     * @return a list of {@link BookItem} objects that match the given subject.
     *         If no books match, an empty list is returned.
     */
    public List<BookItem> findBooksBySubject(String subject) {
        return bookSubjects.getOrDefault(subject.toUpperCase(), new ArrayList<>());
    }

    /**
     * Searches for books in the catalog by their status.
     * This method returns an observable list of books with the specified status.
     *
     * @param bookStatus the status of the books to search for (e.g., "AVAILABLE", "LOANED", etc.)
     * @return an {@link ObservableList} (for listener to track changes) of {@link BookItem} objects that match the given status.
     *         If no books match, an empty list is returned.
     */
    public ObservableList<BookItem> findBooksByStatus(String bookStatus) {
        return this.bookStatus.getOrDefault(bookStatus, FXCollections.observableArrayList());
    }

    /**
     * Searches for books in the catalog by their publication date.
     * This method retrieves books with the specified publication date.
     *
     * @param publicationDate the publication date of the books to search for
     * @return a list of {@link BookItem} objects that match the given publication date.
     *         If no books match, an empty list is returned.
     */
    public List<BookItem> findBooksByPublicationDate(Date publicationDate) {
        return bookPublicationDates.getOrDefault(publicationDate, new ArrayList<>());
    }

    /**
     * Edits a specific field of a book in the database and updates the corresponding internal data structure.
     * <p>
     * This method allows you to edit various fields of a book item (identified by its bookId) in the
     * database. It updates the given field (e.g., ISBN, title, author) and then reflects that change
     * in the internal collection of books.
     *
     * @param bookId The unique identifier of the book to be updated.
     * @param fieldToEdit The field of the book to be updated. This is an integer corresponding
     *                    to the field that needs to be changed (e.g., 1 for ISBN, 2 for title).
     * @param newValue The new value that should be assigned to the specified field.
     *
     */
    public void editBookInDataBase(String bookId, int fieldToEdit, String newValue) {
        // Check if the book exists
        if (this.findBookById(bookId) == null) {
            return; // If the book doesn't exist, return without doing anything
        }
        long startTime = System.currentTimeMillis();
        String query = "";

        // Select the SQL query based on the field to edit
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

        // connect database
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            // Set parameters for the query
            statement.setString(1, newValue);
            statement.setString(2, bookId);

            // Execute the update query
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                // Update internal data structures to reflect the changes
                BookItem oldBookItem = this.bookId.get(bookId);
                if (oldBookItem != null) {
                    // Perform updates based on the field edited
                    switch (fieldToEdit) {
                        case 1: // ISBN
                            // remove in map not in the database.
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

                        case 18: // imageName
                            this.removeBookById(oldBookItem.getId(), false, true);

                            oldBookItem.setImgName(newValue);

                            this.addBookItem(oldBookItem, true);
                            break;
                        default:
                            System.out.println("Invalid field.");
                            return;
                    }

                }
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

    /**
     * Removes a book from the system by its ID.
     * The removal is performed from multiple collections (bookId, bookTitles, bookAuthors, bookSubjects, bookPublicationDates, bookStatus),
     * using separate threads for each collection to improve performance.
     *
     * @param id          The ID of the book to be removed.
     * @param inDatabase  A flag indicating whether the book should also be removed from the database (true if it should be removed).
     * @param isEdit      A flag indicating whether the book removal is part of an edit operation (true if it is an edit, false if it's a permanent removal).
     * @return            Returns true if the removal was successful, false if the book was not found.
     */
    public boolean removeBookById(String id, boolean inDatabase, boolean isEdit) {
        long startTime = System.currentTimeMillis();

        // Check if the book exists in the bookId collection
        if (!bookId.containsKey(id)) {
            System.out.println("Book not found!");
            return false;
        }

        BookItem bookToRemove = this.bookId.get(id);

        // Create threads for each collection to remove the book concurrently
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

        // If it's not an edit operation, remove the book from the bookId collection and decrement totalBooks
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

    /**
     * Removes a book from the database by its ID.
     * Executes a DELETE query on the `bookitem` table where the `id` matches the provided book ID.
     * It prints a message indicating whether the book was successfully deleted or not found.
     *
     * @param id The ID of the book to be deleted from the database.
     */
    private void removeBookFromDatabase(String id) {
        String query = "DELETE FROM bookitem WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the book ID to the prepared statement
            statement.setString(1, id);

            int rowsDeleted = statement.executeUpdate();

            // Check if the book was deleted
            if (rowsDeleted > 0) {
                System.out.println("A book was successfully deleted!");
            } else {
                System.out.println("No book found with the given ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error deleting the book from the database: " + e.getMessage());
        }
    }


    /**
     * Displays the information about the catalog, including its creation date and the total number of books.
     */
    public void displayCatalogInfo() {
        System.out.println("Catalog Creation Date: " + creationDate);
        System.out.println("Total Books in Catalog: " + totalBooks);
    }
}
