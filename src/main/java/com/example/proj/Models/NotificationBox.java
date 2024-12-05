package com.example.proj.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NotificationBox {
    private ObservableList<Notification> notifications;

    public NotificationBox() {
        this.notifications = FXCollections.observableArrayList();
    }

    public ObservableList<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(ObservableList<Notification> notifications) {
        this.notifications = notifications;
    }


    /**
     * Retrieves and generates notifications for a member based on the books they have borrowed.
     * <p>
     * This method queries the database for the logs of the books borrowed by the member
     * (identified by their member ID). It calculates the due date for each book as 15 days after the
     * borrowing date and generates a notification for each book. If the due date is tomorrow or later,
     * the notification will be marked as not overdue, otherwise it will be marked as overdue.
     *
     * @param memberId the ID of the member whose notifications are to be retrieved.
     */
    public void getNotificationsForMember(String memberId) {

        LocalDate today = LocalDate.now();

        String query = "SELECT bookId, creationDate FROM logs WHERE id = ? ORDER BY creationDate DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, memberId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String bookId = resultSet.getString("bookId");
                LocalDate creationDate = resultSet.getDate("creationDate").toLocalDate();
                LocalDate dueDate = creationDate.plusDays(15);

                LocalDate tomorrow = today.plusDays(1);
                if (!dueDate.isBefore(tomorrow)) {
                    notifications.add(new Notification(bookId, dueDate, false));
                } else {
                    notifications.add(new Notification(bookId, dueDate, true));
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Finds a specific notification based on the book ID.
     * <p>
     * This method searches through the list of notifications and returns the notification
     * associated with the given book ID. If no such notification exists, it returns `null`.
     *
     * @param bookId the ID of the book for which the notification is to be found.
     * @return the notification associated with the given book ID, or `null` if no such notification exists.
     */
    public Notification findNotification (String bookId) {
        for (Notification i : notifications) {
            if (i.getBookId().equals(bookId)) {
                return i;
            }
        }
        return null;
    }
}
