package com.example.proj.Models;

import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.util.Date;

public class LibraryLogger {
    private String filePath;


    public void updateLog(Member member, String bookId, Date date, String type) throws ParseException {
        String sqlInsert = "INSERT INTO logs (id, creationDate, bookId) VALUES (?, ?, ?)";
        String sqlDelete = "DELETE FROM logs WHERE id = ? AND creationDate = ? AND bookId = ?";
        String sqlSelect = "SELECT creationDate FROM logs WHERE id = ? AND bookId = ?";
        boolean found = false;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shibalib", "root", "");
             PreparedStatement insertStatement = connection.prepareStatement(sqlInsert);
             PreparedStatement deleteStatement = connection.prepareStatement(sqlDelete);
             PreparedStatement selectStatement = connection.prepareStatement(sqlSelect)) {

            switch (type.toUpperCase()) {
                case "LEND":
                    insertStatement.setString(1, member.getId());  // Sử dụng ID của member
                    insertStatement.setDate(2, new java.sql.Date(date.getTime()));
                    insertStatement.setString(3, bookId);  // Thêm bookId
                    insertStatement.executeUpdate();
                    break;

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
                        member.updateBook(bookId, 13, "AVAILABLE");

                        if (daysBetween > 15) {
                            System.out.println("Warning: The return date exceeds the allowable 15 days limit!");
                            int p = member.getPoint() - 1;
                            member.setPoint(p);
                            admin.reducePointMemberDatabase(member.getId());

                            if (member.getPoint() == 0) {
                                member.setStatus(AccountStatus.BLACKLISTED);
                                admin.blockMemberDatabase(member.getId());
                            }
                        }
                        // Delete the log entry for the returned book
                        deleteStatement.setString(1, member.getId());
                        deleteStatement.setDate(2, new java.sql.Date(borrowedDate.getTime()));
                        deleteStatement.setString(3, bookId);
                        deleteStatement.executeUpdate();
                    }
                    if (!found) {
                        System.out.println("You haven't borrowed this book.");
                    }
                    break;

                case "RENEW":
                    if (member.getStatus() == AccountStatus.BLACKLISTED) {
                        System.out.println("You've been blocked for returning books late too many times");
                        break;
                    }

                    // Prepare to select the existing log entry
                    selectStatement.setString(1, member.getId());
                    selectStatement.setString(2, bookId);
                    resultSet = selectStatement.executeQuery();
                    if (resultSet.next()) {
                        found = true;
                        Date borrowedDate = resultSet.getDate("creationDate");
                        long daysBetween = (date.getTime() - borrowedDate.getTime()) / (1000 * 60 * 60 * 24);

                        // Create a query to update the existing log entry's date
                        String sqlUpdate = "UPDATE logs SET creationDate = ? WHERE id = ? AND bookId = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(sqlUpdate)) {
                            updateStatement.setDate(1, new java.sql.Date(date.getTime()));
                            updateStatement.setString(2, member.getId());
                            updateStatement.setString(3, bookId);
                            updateStatement.executeUpdate();
                        }

                        if (daysBetween > 15) {
                            System.out.println("Warning: The renewal date exceeds the allowable 15 days limit.");
                            Librarian admin = new Librarian();
                            int p = member.getPoint() - 1;
                            member.setPoint(p);
                            admin.reducePointMemberDatabase(member.getId());

                            if (member.getPoint() == 0) {
                                member.setStatus(AccountStatus.BLACKLISTED);
                                admin.blockMemberDatabase(member.getId());
                            }
                        }
                    }
                    if (!found) {
                        System.out.println("You haven't borrowed this book.");
                    }
                    break;


                default:
                    System.out.println("Invalid type. Use 'LEND', 'RETURN', or 'RENEW'.");
                    return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public void generateMemberLog(String membersFilePath, String memberId) {
        BufferedReader reader = null;
        boolean memberExists = false;
        try {
            reader = new BufferedReader(new FileReader(membersFilePath));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] memberDetails = line.split(";");

                if (memberDetails[0].equals(memberId)) {
                    memberExists = true;
                    break;
                }
            }

            if (memberExists) {
                String memberLogFilePath = "src/main/resources/database/" + memberId + ".txt";

                File memberLogFile = new File(memberLogFilePath);
                if (!memberLogFile.exists()) {
                    memberLogFile.createNewFile();
                    System.out.println("Log file created for member: " + memberId);
                } else {
                    System.out.println("Log file loaded for member: " + memberId);
                }
            } else {
                System.out.println("Member " + memberId + " does not exist in the member list.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
