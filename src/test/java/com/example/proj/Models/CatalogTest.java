package com.example.proj.Models;

import org.junit.jupiter.api.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class CatalogTest {
    private Librarian librarian;
    private static Connection connection;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        connection = DatabaseConnection.getConnection();
    }

    @AfterAll
    static void teardownDatabase() throws SQLException {
        DatabaseConnection.closeConnection(CatalogTest.connection);
    }

    @BeforeEach
    public void setUp() {
        librarian = new Librarian();
        //librarian.getCatalog().clear();
    }


    @Order(0)
    @Test
    public void testLoadCatalogFromDatabase() {
        // verify that the catalog is populated with data
        List<BookItem> booksByTitle = librarian.getCatalog().findBooksByTitle("Updated Title");
//        for (BookItem i : booksByTitle) {
//            i.displayInfo();
//        }
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

    @Order(1)
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

    @Order(2)
    @Test
    public void testEditBookInDataBase_UpdateTitle() {
        // Act
        librarian.getCatalog().loadCatalogFromDatabase();
        librarian.getCatalog().editBookInDataBase("1", 2, "Updated Title");

        // Assert
        assertEquals("Updated Title", librarian.getCatalog().findBookById("1").getTitle());
    }

    @Order(3)
    @Test
    public void testEditBookInDataBase_UpdateISBN() {
        // Act
        //librarian.getCatalog().loadCatalogFromDatabase();
        librarian.getCatalog().editBookInDataBase("1", 1, "987654321");

        // Assert
        assertEquals("987654321", librarian.getCatalog().findBookById("1").getISBN());
    }

    @Order(4)
    @Test
    public void testEditBookInDataBase_InvalidField() {
        // Act
        librarian.getCatalog().loadCatalogFromDatabase();
        librarian.getCatalog().editBookInDataBase("1", 99, "Some Value"); // Invalid field

        // Assert: Expect no change
        assertEquals("Updated Title", librarian.getCatalog().findBookById("1").getTitle());
    }

    @Order(5)
    @Test
    public void testEditBookInDataBase_BookNotFound() {
        // Act
        librarian.getCatalog().loadCatalogFromDatabase();
        librarian.getCatalog().editBookInDataBase("5161951", 2, "Updated Title"); // Non-existing ID

        // Assert: Should not throw exception and should not change anything
        assertNull(librarian.getCatalog().findBookById("5161951"));
    }


}
