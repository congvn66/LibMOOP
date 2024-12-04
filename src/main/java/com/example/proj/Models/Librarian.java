package com.example.proj.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class Librarian extends Account{

    private Map<String, Member> memberMap;
    private LinkedHashMap<Date, ObservableList<Log>> totalLogs;
    private HashMap<String, LinkedHashMap<Date, ObservableList<Log>>> memberLogs;
    private LinkedHashMap<Date, Integer> memberRegister;
    private Map<String, ObservableList<Log>> logList;

    public Librarian() {
        super("admin", AccountStatus.ACTIVE, "admin");
        this.memberLogs = new HashMap<>();
        this.totalLogs = new LinkedHashMap<>();
        this.memberRegister = new LinkedHashMap<>();
        this.logList = new HashMap<>();
    }

    public Librarian(String id, AccountStatus status, String password) {
        super(id, status, password);
        this.memberLogs = new HashMap<>();
        this.totalLogs = new LinkedHashMap<>();
        this.memberRegister = new LinkedHashMap<>();
        this.logList = new HashMap<>();
    }

    public LinkedHashMap<Date, ObservableList<Log>> getTotalLogs() {
        if (totalLogs.isEmpty()) {
            loadLogFromDatabase();
        }
        return totalLogs;
    }

    public HashMap<String, LinkedHashMap<Date, ObservableList<Log>>> getMemberLogs() {
        if (memberLogs.isEmpty()) {
            loadLogFromDatabase();
        }
        return memberLogs;
    }

    public LinkedHashMap<Date, Integer> getMemberRegister() {
        if (memberRegister.isEmpty()) {
            loadMembersFromDatabase();
        }
        return memberRegister;
    }

    public Map<String, ObservableList<Log>> getLogList() {
        if (logList.isEmpty()) {
            loadLogFromDatabase();
        }
        return logList;
    }

    public Map<String,Member> getMemberMap() {
        if(this.memberMap == null) {
            this.memberMap = new HashMap<>();
            this.loadMembersFromDatabase();
        }
        return this.memberMap;
    }

    private void putMemberInMap(Member member) {
        this.memberMap.put(member.getId(), member);
    }

    /**
     * Adds a member registration to the `memberRegister` map, which tracks the number
     * of members registered on specific dates. If the given registration date is not
     * already in the map, adds it with an initial count of 1. If the date already exists,
     * increments the count for that date.
     *
     * @param member the `Member` object whose registration date will be added to the map.
     */
    public void addMemberRegisterToMap(Member member) {
        // if not contain.
        if (!memberRegister.containsKey(member.getCreateDate())) {
            this.memberRegister.put(member.getCreateDate(), 1);
            return;
        }
        // apply the changes.
        this.memberRegister.replace(member.getCreateDate(), this.memberRegister.get(member.getCreateDate()) + 1);
    }

    /**
     * Loads members from the database and adds them to the internal maps for member tracking and registration count.
     * This method retrieves member details from the 'members' table in the 'shibalib' database and processes each
     * member's information. It creates a `Member` object and adds it to the `memberRegister` map and the member
     * data map using appropriate methods.
     * <p>
     * The method assumes the following columns in the 'members' table:
     * - id (String)
     * - accountStatus (String)
     * - password (String)
     * - numberOfBooks (int)
     * - point (int)
     * - createDate (Date)
     *
     */
    private void loadMembersFromDatabase() {
        // SQL query.
        String query = "SELECT id, accountStatus, password, numberOfBooks, point, createDate FROM members";

        // database connection, execution, and saving result.
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            // get data.
            while (resultSet.next()) {
                String id = resultSet.getString("id").trim();
                String accountStatusString = resultSet.getString("accountStatus").trim();
                String password = resultSet.getString("password").trim();
                int totalBooksCheckedOut = resultSet.getInt("numberOfBooks");
                int point = resultSet.getInt("point");
                Date date = resultSet.getDate("createDate");

                // fetch data into map.
                Member member = new Member(
                        id,
                        AccountStatus.valueOf(accountStatusString.toUpperCase()),
                        password,
                        totalBooksCheckedOut,
                        point,
                        date
                );
                this.putMemberInMap(member);
                this.addMemberRegisterToMap(member);
            }
        } catch (SQLException e) {
            System.out.println("Error loading members from database: " + e.getMessage());
        }
    }


    /**
     * Loads logs from the database and adds them to the internal map for log tracking.
     * This method retrieves log data from the 'logs' table in the 'shibalib' database and processes each
     * log entry. It creates a `Log` object and adds it to the internal map using the `addLogToMap` method.
     * <p>
     * The method assumes the following columns in the 'logs' table:
     * - id (String)
     * - creationDate (Date)
     * - bookId (String)
     *
     */
    public void loadLogFromDatabase() {
        // SQL query.
        String query = "SELECT id, creationDate, bookId FROM logs ";

        // database connection, execution and saving result.
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            // get data.
            while (resultSet.next()) {
                String id = resultSet.getString("id").trim();
                Date creationDate = resultSet.getDate("creationDate");
                String bookId = resultSet.getString("bookId");

                // fetching stuff.
                Log log = new Log(id, creationDate, bookId);
                addLogToMap(log);
            }
        } catch (SQLException e) {
            System.out.println("Error loading logs from database: " + e.getMessage());
        }
    }

    /**
     * Adds a log entry to various internal maps that track logs by date and member.
     * This method updates the following maps:
     * - `totalLogs`: A map that tracks logs by their creation date.
     * - `memberLogs`: A map that tracks logs for each member by their ID and creation date.
     * - `logList`: A map that tracks all logs for each member by their ID.
     * <p>
     * The method uses `computeIfAbsent` to ensure that a new list is created if one does not exist for a given key.
     * Then, it adds the provided log to the appropriate list based on its creation date or member ID.
     *
     * @param log The log entry to be added to the maps.
     */
    public void addLogToMap(Log log) {
        totalLogs.computeIfAbsent(log.getCreationDate(), k -> FXCollections.observableList(new ArrayList<Log>())).add(log);
        memberLogs.computeIfAbsent(log.getId(), k -> new LinkedHashMap<>());
        memberLogs.get(log.getId()).computeIfAbsent(log.getCreationDate(), k -> FXCollections.observableList(new ArrayList<>())).add(log);
        logList.computeIfAbsent(log.getId(), k -> FXCollections.observableList(new ArrayList<Log>())).add(log);
    }

    /**
     * Adds a new book item to the catalog by writing it to the database and updating the catalog.
     *
     * @param bookItem the book item to be added.
     */
    public void addBookItem(BookItem bookItem) {
        int generatedId = this.getCatalog().writeBookItemToDatabaseAndReturnId(bookItem);

        if (generatedId != -1) {
            bookItem.setId(String.valueOf(generatedId));
            this.getCatalog().addBookItem(bookItem, false);
            System.out.println("Book item added to catalog with ID: " + generatedId);
        } else {
            System.out.println("Failed to add book item to catalog due to database error.");
        }
    }

    /**
     * Blocks a member by updating their account status to "BLACKLISTED" in the database.
     *
     * @param id the ID of the member to be blocked.
     */
    public void blockMemberDatabase(String id) {
        String updateQuery = "UPDATE members SET accountStatus = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setString(1, AccountStatus.BLACKLISTED.name());
            preparedStatement.setString(2, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                if (this.getMemberMap().containsKey(id)) {
                    this.getMemberMap().get(id).setStatus(AccountStatus.BLACKLISTED);
                }
                System.out.println("Member " + id + " has been blocked.");
            } else {
                System.out.println("Member with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error blocking member: " + e.getMessage());
        }
    }

    /**
     * Deletes a member's account from the database.
     *
     * @param id the ID of the member to be deleted.
     */
    public void deleteMemberAccount(String id) {
        String updateQuery = "DELETE FROM members WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setString(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                if (this.getMemberMap().containsKey(id)) {
                    this.memberMap.remove(id);
                }
            }
        } catch (SQLException e) {
            System.out.println("Delete member error");
        }
    }

    /**
     * Changes the status of a member in the database.
     *
     * @param id the ID of the member.
     * @param status the new account status to be set.
     */
    public void changeMemberStatus(String id, AccountStatus status) {
        String updateQuery = "UPDATE members SET accountStatus = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setString(1, status.name());
            preparedStatement.setString(2, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                if (this.getMemberMap().containsKey(id)) {
                    this.getMemberMap().get(id).setStatus(status);
                }
            }
        } catch (SQLException e) {
            System.out.println("Update member error");
        }
    }

    /**
     * Reduces the points of a member in the database.
     *
     * @param id the ID of the member.
     * @param point the number of points to be reduced.
     */
    public void reducePointMemberDatabase(String id, int point) {
        String updateQuery = "UPDATE members SET point = point - ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            // Set the parameter for the query
            preparedStatement.setString(2, id);
            preparedStatement.setInt(1, point);
            // Execute the update
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                if (this.getMemberMap().containsKey(id)) {
                    int newPoints = this.getMemberMap().get(id).getPoint() - point;
                    this.getMemberMap().get(id).setPoint(newPoints);
                }
                System.out.println("Member " + id + " has lost " + point + " reputation.");
            } else {
                System.out.println("Member with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error reducing member points: " + e.getMessage());
        }
    }

    /**
     * Updates the password of a member in the database.
     *
     * @param id the ID of the member.
     * @param password the new password.
     */
    public void updatePassWord(String id, String password) {
        String updateQuery = "UPDATE members SET password = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            // Set the parameter for the query
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, id);
            // Execute the update
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                if (this.getMemberMap().containsKey(id)) {
                    this.getMemberMap().get(id).setPassword(password);
                }
            }
        } catch (SQLException e) {
        }
    }

    /**
     * Updates the points of a member in the database.
     * If the new points value is 0, the member's status will be changed to "BLACKLISTED".
     *
     * @param id the ID of the member.
     * @param newPoint the new points value.
     */
    public void updatePoint(String id, int newPoint) {
        String updateQuery = "UPDATE members SET point = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
             ) {

            // Set the parameter for the query
            preparedStatement.setInt(1, newPoint);
            preparedStatement.setString(2, id);
            // Execute the update
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                if (this.getMemberMap().containsKey(id)) {
                    if (newPoint == 0) {
                        this.changeMemberStatus(id, AccountStatus.BLACKLISTED);
                    }
                    this.getMemberMap().get(id).setPoint(newPoint);
                }
            }
        } catch (SQLException e) {
        }
    }

    /**
     * Adds a new member to the database.
     *
     * @param member the member to be added.
     */
    public void addNewMemberDatabase(Member member) {
        String insertQuery = "INSERT INTO members (id, accountStatus, password, numberOfBooks, point, createDate) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            // Set the parameters for the query
            preparedStatement.setString(1, member.getId().trim());
            preparedStatement.setString(2, member.getStatus().name());
            preparedStatement.setString(3, member.getPassword().trim());
            preparedStatement.setInt(4, member.getTotalBooksCheckedOut());
            preparedStatement.setInt(5, member.getPoint());
            preparedStatement.setDate(6, member.getCreateDate());

            // Execute the insert
            preparedStatement.executeUpdate();
            System.out.println("Member added: " + member);

        } catch (SQLException e) {
            System.out.println("Error adding new member: " + e.getMessage());
        }
    }

    /**
     * Increases the total number of books checked out for a member in the database by 1.
     *
     * @param id the ID of the member whose checked-out books count will be increased.
     */
    public void increaseBookForMemberDatabase(String id) {
        String updateQuery = "UPDATE members SET numberOfBooks = numberOfBooks + 1 WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {


            preparedStatement.setString(1, id);


            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                Member member = this.getMemberMap().get(id);
                if (member != null) {
                    member.setTotalBooksCheckedOut(member.getTotalBooksCheckedOut() + 1);
                }
                System.out.println("Total books checked out for member " + id + " has been increased.");
            } else {
                System.out.println("Member with ID " + id + " not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error increasing book count for member: " + e.getMessage());
        }
    }

    /**
     * Decreases the total number of books checked out for a member in the database by 1.
     *
     * @param id the ID of the member whose checked-out books count will be decreased.
     */
    public void decreaseBookForMemberDatabase(String id) {
        String updateQuery = "UPDATE members SET numberOfBooks = numberOfBooks - 1 WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {


            preparedStatement.setString(1, id);


            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                Member member = this.getMemberMap().get(id);
                if (member != null) {
                    member.setTotalBooksCheckedOut(member.getTotalBooksCheckedOut() - 1);
                }
                System.out.println("Total books checked out for member " + id + " has been decreased.");
            } else {
                System.out.println("Member with ID " + id + " not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error increasing book count for member: " + e.getMessage());
        }
    }

    /**
     * Removes a book from the catalog by its ID and prints the total book count before and after the removal.
     *
     * @param id the ID of the book to be removed from the catalog.
     */
    public void removeBook(String id) {
        System.out.println(this.getCatalog().getTotalBooks().get());
        this.getCatalog().removeBookById(id, true, false);
        System.out.println(this.getCatalog().getTotalBooks().get());
    }

    public void printAllMember() {
        System.out.println(this.memberMap.size());
        for(String s : this.memberMap.keySet()) {
            this.memberMap.get(s).printInfo();
        }
    }
}
