package com.example.proj.Models;


import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.*;


import java.sql.*;
import java.util.Map;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

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


    @Order(0)
    @Test
    void testBlockMemberDatabase() throws SQLException {
        // arrange
        Map<String, Member> members = librarian.getMemberMap();

        // Act
        librarian.blockMemberDatabase("test");

        // Assert
        Member member = librarian.getMemberMap().get("test");
        assertEquals(AccountStatus.BLACKLISTED, member.getStatus());

        // Verify change in the database
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT accountStatus FROM members WHERE id='test'");
        assertTrue(rs.next());
        assertEquals("BLACKLISTED", rs.getString("accountStatus"));
        librarian.changeMemberStatus("test", AccountStatus.ACTIVE);
    }

    @Order(1)
    @Test
    void testAddNewMemberDatabase() throws SQLException {
        // Arrange
        Member newMember = new Member("thienan1", AccountStatus.ACTIVE, "j97", 0, 100, new Date(System.currentTimeMillis()));

        // Act
        librarian.addNewMemberDatabase(newMember);


        // Verify insertion into the database
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM members WHERE id='thienan1'");
        assertTrue(rs.next());
        assertEquals("ACTIVE", rs.getString("accountStatus"));
        assertEquals("j97", rs.getString("password"));

        librarian.deleteMemberAccount("thienan1");
    }

    @Order(2)
    @Test
    void testReducePointMemberDatabase() throws SQLException {
        // Arrange
        Map<String, Member> members = librarian.getMemberMap();

        // Act
        librarian.reducePointMemberDatabase("test", 1);

        // Assert
        assertEquals(99, librarian.getMemberMap().get("test").getPoint());

        // Verify the database update
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT point FROM members WHERE id='test'");
        assertTrue(rs.next());
        assertEquals(99, rs.getInt("point"));

        librarian.reducePointMemberDatabase("test", -1);
    }

    @Order(3)
    @Test
    void testIncreaseBookForMemberDatabase() throws SQLException {
        // Arrange
        Map<String, Member> members = librarian.getMemberMap();

        // Act
        librarian.increaseBookForMemberDatabase("test");

        // Assert
        assertEquals(1, librarian.getMemberMap().get("test").getTotalBooksCheckedOut());

        // Verify the database update
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT numberOfBooks FROM members WHERE id='test'");
        assertTrue(rs.next());
        assertEquals(1, rs.getInt("numberOfBooks"));

    }

    @Order(4)
    @Test
    void testDecreaseBookForMemberDatabase() throws SQLException {
        // Arrange
        Map<String, Member> members = librarian.getMemberMap();

        // Act
        librarian.decreaseBookForMemberDatabase("test");

        // Assert
        assertEquals(0, librarian.getMemberMap().get("test").getTotalBooksCheckedOut());

        // Verify the database update
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT numberOfBooks FROM members WHERE id='test'");
        assertTrue(rs.next());
        assertEquals(0, rs.getInt("numberOfBooks"));

        //librarian.increaseBookForMemberDatabase("test");


    }
}
