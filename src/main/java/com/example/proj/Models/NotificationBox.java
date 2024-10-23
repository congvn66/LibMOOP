package com.example.proj.Models;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NotificationBox {
    private List<Notification> notifications;

    public NotificationBox() {
        this.notifications = new ArrayList<>();
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }



    public void getNotificationsForMember(String memberId) {
        String jdbcURL = "jdbc:mysql://localhost:3306/shibalib";
        String username = "root";
        String password = "";
        LocalDate today = LocalDate.now();

        String query = "SELECT bookId, creationDate FROM logs WHERE id = ? ORDER BY creationDate DESC";

        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, memberId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String bookId = resultSet.getString("bookId");
                LocalDate creationDate = resultSet.getDate("creationDate").toLocalDate();
                LocalDate dueDate = creationDate.plusDays(15);

                LocalDate tomorrow = today.plusDays(1);
                if (!dueDate.isBefore(tomorrow)) {
                    notifications.add(new Notification(bookId, dueDate.toString(), false));
                } else {
                    notifications.add(new Notification(bookId, dueDate.toString(), true));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
