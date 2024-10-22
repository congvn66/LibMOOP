package com.example.proj.Models;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    private Member member;
    //private Catalog catalog;
    //private BookItem book;
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
        //catalog = new Catalog();
        member = new Member("cong", AccountStatus.ACTIVE, "congdz", 0, 100);
        //book = new BookItem("B001", "Effective Java", "Joshua Bloch", 2008, BookStatus.AVAILABLE, false);

    }

    @Test
    public void testConstructor() {
        assertEquals("cong", member.getId());
        assertEquals(AccountStatus.ACTIVE, member.getStatus());
        assertEquals("congdz", member.getPassword());
        assertEquals(0, member.getTotalBooksCheckedOut());
        assertEquals(100, member.getPoint());
        assertNotNull(member.getCreateDate());
    }

    @Test
    public void testSettersAndGetters() {
        member.setTotalBooksCheckedOut(4);
        assertEquals(4, member.getTotalBooksCheckedOut());

        member.setPoint(15);
        assertEquals(15, member.getPoint());

        Date newDate = Date.valueOf(LocalDate.of(2022, 1, 1));
        member.setCreateDate(newDate);
        assertEquals(newDate, member.getCreateDate());
    }

    @Test
    public void testLendBookSuccess() throws ParseException {
        BookItem book = this.member.getCatalog().findBookById("1001");

        Date creationDate = Date.valueOf(LocalDate.now());

        member.basicActions("1001", creationDate, "LEND");

        // post-check: Book should now be loaned, totalBooksCheckedOut should increment
        assertEquals(BookStatus.LOANED, book.getStatus());
        assertEquals(1, member.getTotalBooksCheckedOut()); // Initially 3, now 4
    }

    @Test
    public void testLendBookFailure_MaxBooksReached() throws ParseException {
        BookItem book = this.member.getCatalog().findBookById("1003");

        Date creationDate = Date.valueOf(LocalDate.now());

        // Set member to have 5 books (max allowed)
        member.setTotalBooksCheckedOut(5);

        // Try to lend a book when max books are already checked out
        member.basicActions("1003", creationDate, "LEND");

        // Book should still be available and not loaned
        assertEquals(BookStatus.AVAILABLE, book.getStatus());
        assertEquals(5, member.getTotalBooksCheckedOut()); // Should remain at 5
    }

    @Test
    public void testReturnBook() throws ParseException {
        BookItem book = this.member.getCatalog().findBookById("1012");

        Date creationDate = Date.valueOf(LocalDate.now());

        // lend the book to change status
        member.basicActions("1012", creationDate, "LEND");
        assertEquals(BookStatus.LOANED, book.getStatus());

        // return the book
        member.basicActions("1012", creationDate, "RETURN");

        // check if the book status is still loaned (assuming status doesn't reset on return)
        assertEquals(BookStatus.AVAILABLE, book.getStatus());
    }

    @Test
    public void testRenewBook() throws ParseException {
        BookItem book = this.member.getCatalog().findBookById("1012");

        Date creationDate = Date.valueOf(LocalDate.now());

        // lend the book
        member.basicActions("1012", creationDate, "LEND");

        // renew the book
        member.basicActions("1012", creationDate, "RENEW");

        // check if book remains loaned after renewal
        assertEquals(BookStatus.LOANED, book.getStatus());
    }

}