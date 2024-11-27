package com.example.proj.Models;


import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.sql.*;
import java.util.Map;

public class LibrarianTest {
    private static Librarian librarian;
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
    void setUp() {
        librarian = new Librarian();
    }

    @Test
    void getMemberMap() throws SQLException {
        // test method.
        Map<String, Member> members = librarian.getMemberMap();

        // assert.
        assertEquals(7, members.size());
        assertTrue(members.containsKey("cong"));
        assertTrue(members.containsKey("duc"));
        assertEquals(AccountStatus.ACTIVE, members.get("cong").getStatus());
        assertEquals(AccountStatus.BLACKLISTED, members.get("duc").getStatus());
    }

    @Test
    void testBlockMemberDatabase() throws SQLException {
        // arrange
        Map<String, Member> members = librarian.getMemberMap();

        // Act
        librarian.blockMemberDatabase("jack");

        // Assert
        Member member = librarian.getMemberMap().get("jack");
        assertEquals(AccountStatus.BLACKLISTED, member.getStatus());

        // Verify change in the database
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT accountStatus FROM members WHERE id='jack'");
        assertTrue(rs.next());
        assertEquals("BLACKLISTED", rs.getString("accountStatus"));
    }

    @Test
    void testAddNewMemberDatabase() throws SQLException {
        // Arrange
        Member newMember = new Member("thienan1", AccountStatus.ACTIVE, "j97", 0, 100, new Date(System.currentTimeMillis()));

        // Act
        librarian.addNewMemberDatabase(newMember);

        // Assert
        assertTrue(librarian.getMemberMap().containsKey("thienan1"));

        // Verify insertion into the database
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM members WHERE id='thienan1'");
        assertTrue(rs.next());
        assertEquals("ACTIVE", rs.getString("accountStatus"));
        assertEquals("j97", rs.getString("password"));
    }

    @Test
    void testReducePointMemberDatabase() throws SQLException {
        // Arrange
        Map<String, Member> members = librarian.getMemberMap();

        // Act
        librarian.reducePointMemberDatabase("lebron", 1);

        // Assert
        assertEquals(98, librarian.getMemberMap().get("lebron").getPoint());

        // Verify the database update
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT point FROM members WHERE id='lebron'");
        assertTrue(rs.next());
        assertEquals(98, rs.getInt("point"));
    }

    @Test
    void testIncreaseBookForMemberDatabase() throws SQLException {
        // Arrange
        Map<String, Member> members = librarian.getMemberMap();

        // Act
        librarian.increaseBookForMemberDatabase("test2");

        // Assert
        assertEquals(1, librarian.getMemberMap().get("test2").getTotalBooksCheckedOut());

        // Verify the database update
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT numberOfBooks FROM members WHERE id='test2'");
        assertTrue(rs.next());
        assertEquals(1, rs.getInt("numberOfBooks"));
    }

    @Test
    void testDecreaseBookForMemberDatabase() throws SQLException {
        // Arrange
        Map<String, Member> members = librarian.getMemberMap();

        // Act
        librarian.decreaseBookForMemberDatabase("test2");

        // Assert
        assertEquals(0, librarian.getMemberMap().get("test2").getTotalBooksCheckedOut());

        // Verify the database update
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT numberOfBooks FROM members WHERE id='test2'");
        assertTrue(rs.next());
        assertEquals(0, rs.getInt("numberOfBooks"));
    }
}
