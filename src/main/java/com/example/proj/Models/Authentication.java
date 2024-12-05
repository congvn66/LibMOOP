package com.example.proj.Models;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Authentication {
    private String id;
    private String passWord;
    private Map<String, Librarian> librarianMap;

    /**
     * Constructs an {@code Authentication} object and initializes the librarian map.
     * This constructor loads the librarians from the database to populate the map.
     */
    public Authentication() {
        librarianMap = new HashMap<>();
        this.loadLibrariansFromDatabase();
    }

    /**
     * Constructs an {@code Authentication} object with the specified ID and password.
     * This constructor initializes the librarian map and loads librarians from the database.
     *
     * @param id the ID used for authentication
     * @param passWord the password associated with the provided ID
     */
    public Authentication(String id, String passWord) {
        this.id = id;
        this.passWord = passWord;
        librarianMap = new HashMap<>();
        this.loadLibrariansFromDatabase();
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    /**
     * Adds a librarian to the librarian map with the librarian's ID as the key.
     *
     * @param librarian the {@code Librarian} object to be added to the map
     */
    private void putLibrarianInMap (Librarian librarian) {
        this.librarianMap.put(librarian.getId(), librarian);
    }

    /**
     * Loads librarian data from the database and adds each librarian to the librarian map.
     * This method retrieves librarian information such as ID, account status, and password from the database
     * and uses this information to create a {@code Librarian} object, which is then added to the librarian map.
     */
    private void loadLibrariansFromDatabase() {
        // sql query.
        String sql = "SELECT id, accountStatus, password FROM librarians";

        // initialize connection with the database, create Statement object to execute query,
        // ResultSet to store the result of query's execution.
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            // traverse the result.
            while (resultSet.next()) {
                // get value for librarian.
                String id = resultSet.getString("id").trim();
                String accountStatus = resultSet.getString("accountStatus").trim();
                String password = resultSet.getString("password").trim();

                // create and put librarian in the map
                Librarian librarian = new Librarian(id, AccountStatus.valueOf(accountStatus.toUpperCase()), password);
                this.putLibrarianInMap(librarian);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the login credentials for a member are valid based on the given librarian.
     * This method verifies whether the provided ID and password match the credentials
     * of a member stored in the librarian's member map.
     *
     * @param librarian the {@code Librarian} object that contains the member map
     * @return the {@code Member} object if the login is successful, or {@code null} if the credentials are invalid
     */
    public Member checkLoginMember(Librarian librarian) {
        // verify id.
        Member member = librarian.getMemberMap().get(this.id);

        // verify password.
        if (member == null || !member.getPassword().equals(this.passWord)) {
            return null;
        }
        return member;
    }

    /**
     * Verifies if the login credentials for a librarian are valid.
     * This method checks if the provided librarian ID and password match any librarian's credentials
     * stored in the librarian map.
     *
     * @return the {@code Librarian} object if the login is successful, or {@code null} if the credentials are invalid
     */
    public Librarian checkLoginasLibrarian() {
        if (librarianMap.containsKey(this.id) && librarianMap.get(this.id.trim()).getPassword().equals(this.passWord)) {
            return librarianMap.get(this.id);
        }
        return null;
    }

    /**
     * Checks if a member with the provided ID already exists in the librarian's member map.
     * This method verifies whether the given member ID is already registered by checking if it exists in the member map.
     *
     * @return {@code true} if the member ID is not registered (i.e., available for registration),
     *         {@code false} if the member ID is already registered.
     */
    public boolean checkRegisterMember() {
        Librarian librarian = new Librarian();
        Member member = librarian.getMemberMap().get(this.id);
        if (member != null || this.librarianMap.containsKey(this.id)) {
            return false;
        }
        return true;
    }

    /**
     * Checks if a librarian with the provided ID exists in the librarian map.
     * This method verifies whether the given librarian ID is already registered by checking if it exists in the librarian map.
     *
     * @return {@code true} if the librarian ID exists, {@code false} if it does not exist.
     */
    public boolean checkLibrarianID() {
        if (librarianMap.containsKey(this.id)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if a member with the provided ID exists in the member map of a librarian.
     * This method verifies whether the given member ID is already registered by checking if it exists in the librarian's member map.
     *
     * @return {@code true} if the member ID exists, {@code false} if it does not exist.
     */
    public boolean checkMemberID() {
        Librarian librarian = new Librarian();
        Member member = librarian.getMemberMap().get(this.id);
        if (member == null) {
            return false;
        }
        return true;
    }

    /**
     * Checks if a member has checked out a specific number of books.
     * This method verifies whether the member has checked out the specified number of books.
     * This method use for "forget password" situation. If you get the right amount of books, you can reset the password.
     *
     * @param num the number of books to check
     * @return {@code true} if the member has checked out exactly the specified number of books,
     *         {@code false} otherwise.
     */
    public boolean checkMemberBookNum(int num) {
        Librarian librarian = new Librarian();
        Member member = librarian.getMemberMap().get(this.id);
        if (member.getTotalBooksCheckedOut() == num) {
            return true;
        }
        return false;
    }
}
