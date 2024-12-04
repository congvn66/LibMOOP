package com.example.proj.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.text.ParseException;
import java.util.Date;

public class MemberLogger {
    private String id;
    private ObservableList<Log> memListOfLog;

    public MemberLogger(String id) {
        this.id = id;
        memListOfLog = FXCollections.observableArrayList();
        loadMemListOfBook();
    }

    /**
     * Loads a list of logs for the current member from the database.
     * It retrieves records from the `logs` table where the member ID matches the current member's ID.
     * Each log contains information about the member's borrowed books, including the book ID and the date of borrowing.
     * The retrieved logs are added to the `memListOfLog` list.
     */
    public void loadMemListOfBook() {
        String query = "SELECT id, creationDate, bookId FROM logs WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             preparedStatement.setString(1, id);
             ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                id = resultSet.getString("id").trim();
                java.sql.Date creationDate = resultSet.getDate("creationDate");
                String bookId = resultSet.getString("bookId").trim();
                Log log = new Log(id, creationDate, bookId);

                this.memListOfLog.add(log);
            }
        } catch (SQLException e) {
            System.out.println("Error loading members from database: " + e.getMessage());
        }
    }

    public ObservableList<Log> getMemListOfLog() {
        return memListOfLog;
    }

    /**
     * Updates the logs for a member based on the type of action (LEND, RETURN, RENEW).
     * It inserts, deletes, or updates entries in the `logs` table based on the action type and updates the member's status and points accordingly.
     *
     * @param member the member who is performing the action.
     * @param bookId the ID of the book for the action.
     * @param date the date the action is being performed (borrow, return, or renew).
     * @param type the type of action (LEND, RETURN, or RENEW).
     * @return a message indicating the result of the action.
     * @throws ParseException if there is an error parsing the date.
     */
    public String updateLog(Member member, String bookId, Date date, String type) throws ParseException {
        String sqlInsert = "INSERT INTO logs (id, creationDate, bookId) VALUES (?, ?, ?)";
        String sqlDelete = "DELETE FROM logs WHERE id = ? AND creationDate = ? AND bookId = ?";
        String sqlSelect = "SELECT creationDate FROM logs WHERE id = ? AND bookId = ?";
        boolean found = false;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(sqlInsert);
             PreparedStatement deleteStatement = connection.prepareStatement(sqlDelete);
             PreparedStatement selectStatement = connection.prepareStatement(sqlSelect)) {

            switch (type.toUpperCase()) {
                case "LEND":
                    insertStatement.setString(1, id);  // Sử dụng ID của member
                    insertStatement.setDate(2, new java.sql.Date(date.getTime()));
                    insertStatement.setString(3, bookId);  // Thêm bookId
                    insertStatement.executeUpdate();
                    member.setTotalBooksCheckedOut(member.getTotalBooksCheckedOut() + 1);
                    memListOfLog.add(new Log(id, new java.sql.Date(date.getTime()), bookId));
                    return "You have successfully borrowed this book";

                case "RETURN":
                    selectStatement.setString(1, member.getId()); // Sử dụng ID của member
                    selectStatement.setString(2, bookId); // Thêm bookId vào điều kiện tìm kiếm
                    ResultSet resultSet = selectStatement.executeQuery();
                    if (resultSet.next()) {
                        found = true;
                        Date borrowedDate = resultSet.getDate("creationDate");
                        long daysBetween = (date.getTime() - borrowedDate.getTime()) / (1000 * 60 * 60 * 24);

                        // Update member data
                        Librarian admin = new Librarian();
                        admin.decreaseBookForMemberDatabase(member.getId());
                        member.setTotalBooksCheckedOut(member.getTotalBooksCheckedOut() - 1);
                        member.updateBook(bookId, 13, String.valueOf(BookStatus.AVAILABLE));

                        // Delete the log entry for the returned book
                        deleteStatement.setString(1, member.getId());
                        deleteStatement.setDate(2, new java.sql.Date(borrowedDate.getTime()));
                        deleteStatement.setString(3, bookId);
                        deleteStatement.executeUpdate();
                        Notification deleteNotification = new Notification(bookId, new java.sql.Date(date.getTime()).toLocalDate(), false);
                        member.deleteNotificationBox(deleteNotification);
                        memListOfLog.remove(new Log(id, new java.sql.Date(borrowedDate.getTime()), bookId));
                        member.setTotalBooksCheckedOut(member.getTotalBooksCheckedOut() - 1);

                        if (daysBetween > 15) {
                            int p = member.getPoint() - ((int)daysBetween - 15);
                            member.setPoint(p);
                            admin.reducePointMemberDatabase(member.getId(), ((int)daysBetween - 15));

                            if (member.getPoint() == 0) {
                                member.setStatus(AccountStatus.BLACKLISTED);
                                admin.blockMemberDatabase(member.getId());
                            }
                            return "Warning: The return date exceeds the allowable 15 days limit!";
                        }
                        return "You have successfully return this book";
                    }
                    if (!found) {
                        return "You haven't borrowed this book.";
                    }
                    break;

                case "RENEW":
                    if (member.getStatus() == AccountStatus.BLACKLISTED) {
                        return "You've been blocked being in blacklist";
                    }

                    // Prepare to select the existing log entry
                    selectStatement.setString(1, member.getId());
                    selectStatement.setString(2, bookId);
                    resultSet = selectStatement.executeQuery();
                    if (resultSet.next()) {
                        found = true;
                        Date borrowedDate = resultSet.getDate("creationDate");
                        long daysBetween = (date.getTime() - borrowedDate.getTime()) / (1000 * 60 * 60 * 24);

                        String sqlUpdate = "UPDATE logs SET creationDate = ? WHERE id = ? AND bookId = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(sqlUpdate)) {
                            updateStatement.setDate(1, new java.sql.Date(date.getTime()));
                            updateStatement.setString(2, member.getId());
                            updateStatement.setString(3, bookId);
                            updateStatement.executeUpdate();
                            memListOfLog.set(memListOfLog.indexOf(new Log(id, new java.sql.Date(borrowedDate.getTime()), bookId)),
                                    new Log(id, new java.sql.Date(date.getTime()), bookId));
                        }

                        Notification newNotification = new Notification(bookId, new java.sql.Date(date.getTime()).toLocalDate().plusDays(15), false);
                        member.replaceNotificationBox(newNotification);

                        if (daysBetween > 15) {
                            Librarian admin = new Librarian();
                            int p = member.getPoint() - ((int)daysBetween - 15);
                            member.setPoint(p);
                            admin.reducePointMemberDatabase(member.getId(), ((int)daysBetween - 15));

                            if (member.getPoint() == 0) {
                                member.setStatus(AccountStatus.BLACKLISTED);
                                admin.blockMemberDatabase(member.getId());
                            }
                            return "Warning: The renewal date exceeds the allowable 15 days limit.";
                        }
                        return "You have successfully renew this book";
                    }
                    if (!found) {
                        return "You haven't borrowed this book.";
                    }
                    break;


                default:
                    return "Invalid type. Use 'LEND', 'RETURN', or 'RENEW'.";
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

}
