package com.example.proj.Models;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CatalogTest {
    private Catalog catalog;
    private static Connection connection;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shibalib", "root", "");
    }

    @AfterAll
    static void teardownDatabase() throws SQLException {
        connection.close();
    }

    @BeforeEach
    public void setUp() {
        Librarian l = new Librarian();
    }

    @Test
    public void testConstructor() {

        // Check that the creation date is set to the current date
        assertNotNull(catalog.getCreationDate());

        // Check that the total number of books is initialized to 0
        assertEquals(0, catalog.getTotalBooks());

        // Check that all maps are initialized and empty
        assertNotNull(catalog.getBookTitles());
        assertTrue(catalog.getBookTitles().isEmpty());

        assertNotNull(catalog.getBookAuthors());
        assertTrue(catalog.getBookAuthors().isEmpty());

        assertNotNull(catalog.getBookSubjects());
        assertTrue(catalog.getBookSubjects().isEmpty());

        assertNotNull(catalog.getBookPublicationDates());
        assertTrue(catalog.getBookPublicationDates().isEmpty());

        assertNotNull(catalog.getBookId());
        assertTrue(catalog.getBookId().isEmpty());

        assertNotNull(catalog.getBookStatus());
        assertTrue(catalog.getBookStatus().isEmpty());
    }

    @Test
    public void testLoadCatalogFromDatabase() {
        // load catalog from database
        catalog.loadCatalogFromDatabase();

        // verify that the catalog is populated with data
        List<BookItem> booksByTitle = catalog.getBookTitles().get("project management");
        assertNotNull(booksByTitle);
        assertFalse(booksByTitle.isEmpty());

        // check the first book item's details
        BookItem bookItem = booksByTitle.get(0);
        assertEquals("Project Management", bookItem.getTitle());
        assertEquals("Gary R. Heerkens", bookItem.getAuthor().getName());
        assertEquals("Business", bookItem.getSubject());
        assertEquals(BookStatus.RESERVED, bookItem.getStatus());
        assertEquals(BookFormat.JOURNAL, bookItem.getFormat());

    }

    @Test
    public void testAddBookItem() {
        // Create a mock BookItem object
        Author author = new Author("Joshua Bloch", "Well-known Java author");
        BookItem bookItem = new BookItem(
                "9780134685991", // ISBN
                "Effective Java", // Title
                "Programming", // Subject
                "Addison-Wesley", // Publisher
                "English", // Language
                "416", // Number of Pages
                author.getName(), // Author name
                author.getDescription(), // Author description
                "2615", // ID
                false, // Is reference only
                45.0, // Price
                BookFormat.PAPERBACK, // Format
                BookStatus.AVAILABLE, // Status
                Date.valueOf(LocalDate.of(2017, 12, 27)), // Date of purchase
                Date.valueOf(LocalDate.of(2008, 5, 8)), // Publication date
                1, // Number
                "Shelf A", // Location
                "deafault.png"
        );

        // Add book item to the catalog
        catalog.addBookItem(bookItem, true);

        // Check if the title was added correctly
        List<BookItem> booksByTitle = catalog.getBookTitles().get("effective java");
        assertNotNull(booksByTitle);
        assertEquals(1, booksByTitle.size());
        assertEquals("Effective Java", booksByTitle.get(0).getTitle());

        // Check if the author was added correctly
        List<BookItem> booksByAuthor = catalog.getBookAuthors().get("joshua bloch");
        assertNotNull(booksByAuthor);
        assertEquals(1, booksByAuthor.size());
        assertEquals("Joshua Bloch", booksByAuthor.get(0).getAuthor().getName());

        // Check if the subject was added correctly
        List<BookItem> booksBySubject = catalog.getBookSubjects().get("programming");
        assertNotNull(booksBySubject);
        assertEquals(1, booksBySubject.size());
        assertEquals("Programming", booksBySubject.get(0).getSubject());

        // Check if the publication date was added correctly
        List<BookItem> booksByDate = catalog.getBookPublicationDates().get(Date.valueOf(LocalDate.of(2008, 5, 8)));
        assertNotNull(booksByDate);
        assertEquals(1, booksByDate.size());
        assertEquals("Effective Java", booksByDate.get(0).getTitle());

        // Check if the status was added correctly
        List<BookItem> booksByStatus = catalog.getBookStatus().get(BookStatus.AVAILABLE);
        assertNotNull(booksByStatus);
        assertEquals(1, booksByStatus.size());
        assertEquals(BookStatus.AVAILABLE, booksByStatus.get(0).getStatus());

        // Check if the book was added to the ID map correctly
        BookItem bookById = catalog.getBookId().get("2615");
        assertNotNull(bookById);
        assertEquals("2615", bookById.getId());

        // Check if totalBooks was incremented
        assertEquals(1, catalog.getTotalBooks());
    }

    @Test
    public void testWriteBookItemToDatabase() {
        // Create a mock BookItem object
        Author author = new Author("Joshua Bloch", "Well-known Java author");
        BookItem bookItem = new BookItem(
                "9780134685991", // ISBN
                "Effective Java", // Title
                "Programming", // Subject
                "Addison-Wesley", // Publisher
                "English", // Language
                "416", // Number of Pages
                author.getName(), // Author name
                author.getDescription(), // Author description
                "2616", // ID
                false, // Is reference only
                45.0, // Price
                BookFormat.PAPERBACK, // Format
                BookStatus.AVAILABLE, // Status
                Date.valueOf(LocalDate.of(2017, 12, 27)), // Date of purchase
                Date.valueOf(LocalDate.of(2008, 5, 8)), // Publication date
                1, // Rack number
                "Shelf A", // Rack location
                "default.png"
        );

        // Insert book item into the database
        catalog.writeBookItemToDatabaseAndReturnId(bookItem);

        // Check the database to see if the book was inserted correctly
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shibalib", "root", "");
             Statement statement = connection.createStatement()) {

            String query = "SELECT * FROM bookitem WHERE id = '2616'";
            ResultSet resultSet = statement.executeQuery(query);

            assertTrue(resultSet.next());
            assertEquals("9780134685991", resultSet.getString("ISBN"));
            assertEquals("Effective Java", resultSet.getString("title"));
            assertEquals("Programming", resultSet.getString("subject"));
            assertEquals("Addison-Wesley", resultSet.getString("publisher"));
            assertEquals("English", resultSet.getString("language"));
            assertEquals("416", resultSet.getString("numberOfPage"));
            assertEquals("Joshua Bloch", resultSet.getString("authorName"));
            assertEquals("Well-known Java author", resultSet.getString("authorDescription"));
            assertEquals("2616", resultSet.getString("id"));
            assertEquals("false", resultSet.getString("isReferenceOnly"));
            assertEquals(45.0, resultSet.getDouble("price"));
            assertEquals("PAPERBACK", resultSet.getString("format"));
            assertEquals("AVAILABLE", resultSet.getString("status"));
            assertEquals("2017-12-27", resultSet.getDate("dateOfPurchase").toString());
            assertEquals("2008-05-08", resultSet.getDate("publicationDate").toString());
            assertEquals(1, resultSet.getInt("number"));
            assertEquals("Shelf A", resultSet.getString("location"));

        } catch (Exception e) {
            e.printStackTrace();
            fail("Error during test execution: " + e.getMessage());
        }
    }

    @Test
    public void testEditBookInDataBase_UpdateTitle() {
        // Act
        catalog.loadCatalogFromDatabase();
        catalog.editBookInDataBase("1", 2, "Updated Title");

        // Assert
        assertEquals("Updated Title", catalog.findBookById("1").getTitle());
    }

    @Test
    public void testEditBookInDataBase_UpdateISBN() {
        // Act
        catalog.loadCatalogFromDatabase();
        catalog.editBookInDataBase("1", 1, "987654321");

        // Assert
        assertEquals("987654321", catalog.findBookById("1").getISBN());
    }

    @Test
    public void testEditBookInDataBase_InvalidField() {
        // Act
        catalog.loadCatalogFromDatabase();
        catalog.editBookInDataBase("1", 99, "Some Value"); // Invalid field

        // Assert: Expect no change
        assertEquals("Updated Title", catalog.findBookById("1").getTitle());
    }

    @Test
    public void testEditBookInDataBase_BookNotFound() {
        // Act
        catalog.loadCatalogFromDatabase();
        catalog.editBookInDataBase("5161951", 2, "Updated Title"); // Non-existing ID

        // Assert: Should not throw exception and should not change anything
        assertNull(catalog.findBookById("5161951"));
    }

    @Test
    public void testRemoveBookById_Success() {
        catalog.loadCatalogFromDatabase();
        String bookId = "100"; // Replace with a valid ID that exists in your database
        assertTrue(catalog.removeBookById(bookId, true, false), "Book should be removed successfully");

        // Verify that the book is no longer present in the catalog
        assertNull(catalog.findBookById(bookId), "Book should be null after removal");


        catalog.loadCatalogFromDatabase();
        // Verify that the book is no longer present in the catalog
        assertNull(catalog.findBookById(bookId), "Book should be null after removal");
    }
}
