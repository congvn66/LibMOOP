package com.example.proj.Models;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CatalogTest {
    private Librarian librarian;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
    }

    @AfterAll
    static void teardownDatabase() throws SQLException {
        DatabaseConnection.closeConnection();
    }

    @BeforeEach
    public void setUp() {
        librarian = new Librarian();
        //librarian.getCatalog().clear();
    }

    @Test
    public void testConstructor() {

        // Check that the creation date is set to the current date
        assertNotNull(librarian.getCatalog().getCreationDate());

        // Check that the total number of books is initialized to 0
        assertEquals(2616, librarian.getCatalog().getTotalBooks().intValue());

        // Check that all maps are initialized and empty
        assertNotNull(librarian.getCatalog().getBookTitles());
        assertTrue(librarian.getCatalog().getBookTitles().isEmpty());

        assertNotNull(librarian.getCatalog().getBookAuthors());
        assertTrue(librarian.getCatalog().getBookAuthors().isEmpty());

        assertNotNull(librarian.getCatalog().getBookSubjects());
        assertTrue(librarian.getCatalog().getBookSubjects().isEmpty());

        assertNotNull(librarian.getCatalog().getBookPublicationDates());
        assertTrue(librarian.getCatalog().getBookPublicationDates().isEmpty());

        assertNotNull(librarian.getCatalog().getBookId());
        assertTrue(librarian.getCatalog().getBookId().isEmpty());

        assertNotNull(librarian.getCatalog().getBookStatus());
        assertTrue(librarian.getCatalog().getBookStatus().isEmpty());
    }

    @Test
    public void testLoadCatalogFromDatabase() {
        // verify that the catalog is populated with data
        List<BookItem> booksByTitle = librarian.getCatalog().findBooksByTitle("Updated Title");
        for (BookItem i : booksByTitle) {
            i.displayInfo();
        }
        assertNotNull(booksByTitle);
        assertFalse(booksByTitle.isEmpty());

        // check the first book item's details
        BookItem bookItem = booksByTitle.get(0);
        assertEquals("Updated Title", bookItem.getTitle());
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
                null, // ID
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
        librarian.addBookItem(bookItem);

        // Check if the title was added correctly

        List<BookItem> booksByTitle = librarian.getCatalog().getBookTitles().get("EFFECTIVE JAVA");
        System.out.println(booksByTitle.size());
        assertNotNull(booksByTitle);
        assertEquals(1, booksByTitle.size());
        assertEquals("Effective Java", booksByTitle.get(0).getTitle());

        // Check if the author was added correctly
        List<BookItem> booksByAuthor = librarian.getCatalog().getBookAuthors().get("JOSHUA BLOCH");
        assertNotNull(booksByAuthor);
        assertEquals(1, booksByAuthor.size());
        assertEquals("Joshua Bloch", booksByAuthor.get(0).getAuthor().getName());

        // Check if the subject was added correctly
        List<BookItem> booksBySubject = librarian.getCatalog().getBookSubjects().get("PROGRAMMING");
        assertNotNull(booksBySubject);
        assertEquals(1, booksBySubject.size());
        assertEquals("Programming", booksBySubject.getFirst().getSubject());

        // Check if the publication date was added correctly
        List<BookItem> booksByDate = librarian.getCatalog().getBookPublicationDates().get(Date.valueOf(LocalDate.of(2008, 5, 8)));
        assertNotNull(booksByDate);
        assertEquals(1, booksByDate.size());
        assertEquals("Effective Java", booksByDate.getFirst().getTitle());


        // Check if totalBooks was incremented
        assertEquals(librarian.getCatalog().getTotalBooks(), librarian.getCatalog().getTotalBooks());

        librarian.getCatalog().removeBookById(booksByTitle.getFirst().getId(), true, false);
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
        librarian.getCatalog().writeBookItemToDatabaseAndReturnId(bookItem);

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
        librarian.getCatalog().loadCatalogFromDatabase();
        librarian.getCatalog().editBookInDataBase("1", 2, "Updated Title");

        // Assert
        assertEquals("Updated Title", librarian.getCatalog().findBookById("1").getTitle());
    }

    @Test
    public void testEditBookInDataBase_UpdateISBN() {
        // Act
        //librarian.getCatalog().loadCatalogFromDatabase();
        librarian.getCatalog().editBookInDataBase("1", 1, "987654321");

        // Assert
        assertEquals("987654321", librarian.getCatalog().findBookById("1").getISBN());
    }

    @Test
    public void testEditBookInDataBase_InvalidField() {
        // Act
        librarian.getCatalog().loadCatalogFromDatabase();
        librarian.getCatalog().editBookInDataBase("1", 99, "Some Value"); // Invalid field

        // Assert: Expect no change
        assertEquals("Updated Title", librarian.getCatalog().findBookById("1").getTitle());
    }

    @Test
    public void testEditBookInDataBase_BookNotFound() {
        // Act
        librarian.getCatalog().loadCatalogFromDatabase();
        librarian.getCatalog().editBookInDataBase("5161951", 2, "Updated Title"); // Non-existing ID

        // Assert: Should not throw exception and should not change anything
        assertNull(librarian.getCatalog().findBookById("5161951"));
    }

//    @Test
//    public void testRemoveBookById_Success() {
//        librarian.getCatalog().loadCatalogFromDatabase();
//        String bookId = "100"; // Replace with a valid ID that exists in your database
//        assertTrue(librarian.getCatalog().removeBookById(bookId, true, false), "Book should be removed successfully");
//
//        // Verify that the book is no longer present in the catalog
//        assertNull(librarian.getCatalog().findBookById(bookId), "Book should be null after removal");
//
//
//        librarian.getCatalog().loadCatalogFromDatabase();
//        // Verify that the book is no longer present in the catalog
//        assertNull(librarian.getCatalog().findBookById(bookId), "Book should be null after removal");
//    }
}
