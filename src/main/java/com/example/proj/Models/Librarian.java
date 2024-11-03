package com.example.proj.Models;

import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class Librarian extends Account{

    private Map<String, Member> memberMap;
    private String filePath;

    public Librarian() {
        super("admin", AccountStatus.ACTIVE, "admin");
        //this.getCatalog().loadCatalogFromDatabase();
        File file = new File("src/main/resources/database/members.txt");
        if (file.exists()) {
            String absolute = file.getAbsolutePath();
            this.filePath = absolute;
        }
        //this.memberMap = new HashMap<>();
        //this.loadMembersFromDatabase();
    }

    public Librarian(String id, AccountStatus status, String password) {
        super(id, status, password);
        //this.getCatalog().loadCatalogFromDatabase();
        File file = new File("src/main/resources/database/members.txt");
        if (file.exists()) {
            String absolute = file.getAbsolutePath();
            this.filePath = absolute;
        }
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

    private void loadMembersFromDatabase() {
        String query = "SELECT id, accountStatus, password, numberOfBooks, point, createDate FROM members";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shibalib", "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String id = resultSet.getString("id").trim();
                String accountStatusString = resultSet.getString("accountStatus").trim();
                String password = resultSet.getString("password").trim();
                int totalBooksCheckedOut = resultSet.getInt("numberOfBooks");
                int point = resultSet.getInt("point");
                Date date = resultSet.getDate("createDate");

                Member member = new Member(id, AccountStatus.valueOf(accountStatusString.toUpperCase()), password, totalBooksCheckedOut, point, date);

                this.putMemberInMap(member);
            }
        } catch (SQLException e) {
            System.out.println("Error loading members from database: " + e.getMessage());
        }
    }

    private void loadMemberFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(this.filePath))) {
            //System.out.println("yay");
            String line;
            while ((line = br.readLine()) != null) {
                String[] tmp = line.split(";");
                if (tmp.length != 5) {
                    continue;
                }
                String id = tmp[0].trim();
                String accountStatus = tmp[1].trim();
                String password = tmp[2].trim();
                int totalBooksCheckedOut = Integer.parseInt(tmp[3].trim());
                int point = Integer.parseInt(tmp[4].trim());

                Member member = new Member(id, AccountStatus.valueOf(accountStatus.toUpperCase()), password, totalBooksCheckedOut, point);

                this.putMemberInMap(member);


            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addBookItem(BookItem bookItem) {
        this.getCatalog().addBookItem(bookItem, false);
        this.getCatalog().writeBookItemToDatabase(bookItem);
    }

    public void blockMemberDatabase(String id) {
        String updateQuery = "UPDATE members SET accountStatus = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shibalib", "root", "");
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

    public void deleteMemberAccount(String id) {
        String updateQuery = "DELETE FROM members WHERE id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shibalib", "root", "");
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
    public void changeMemberStatus(String id, AccountStatus status) {
        String updateQuery = "UPDATE members SET accountStatus = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shibalib", "root", "");
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

    public void blockMember(String id) {
        // lines container.
        List<String> lines = new ArrayList<>();
        boolean memberFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                if (fields[0].equals(id)) {
                    fields[1] = "BLACKLISTED";
                    memberFound = true;
                    //change in that time.
                    this.getMemberMap().get(fields[0]).setStatus(AccountStatus.BLACKLISTED);
                }
                //put every line in the list.
                lines.add(String.join(";", fields));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        // write to a new file
        if (memberFound) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
                System.out.println("Member " + id + " has been blocked.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Member with ID " + id + " not found.");
        }
    }

    public void reducePointMemberDatabase(String id) {
        String updateQuery = "UPDATE members SET point = point - 1 WHERE id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shibalib", "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            // Set the parameter for the query
            preparedStatement.setString(1, id);

            // Execute the update
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                if (this.getMemberMap().containsKey(id)) {
                    int newPoints = this.getMemberMap().get(id).getPoint() - 1;
                    this.getMemberMap().get(id).setPoint(newPoints);
                }
                System.out.println("Member " + id + " has lost 1 reputation.");
            } else {
                System.out.println("Member with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error reducing member points: " + e.getMessage());
        }
    }

    public void updatePassWord(String id, String password) {
        String updateQuery = "UPDATE members SET password = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shibalib", "root", "");
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

    public void updatePoint(String id, int newPoint) {
        String updateQuery = "UPDATE members SET point = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shibalib", "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
             ) {

            // Set the parameter for the query
            preparedStatement.setInt(1, newPoint);
            preparedStatement.setString(2, id);
            // Execute the update
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                if (this.getMemberMap().containsKey(id)) {
                    this.getMemberMap().get(id).setPoint(newPoint);
                }
            }
        } catch (SQLException e) {
        }
    }

    public void reducePointMember(String id) {
        // lines container.
        List<String> lines = new ArrayList<>();
        boolean memberFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                if (fields[0].equals(id)) {
                    fields[4] = String.valueOf(Integer.parseInt(fields[4]) - 1);
                    memberFound = true;

                    //change in that time.
                    int point = this.getMemberMap().get(fields[0]).getPoint() - 1;
                    this.getMemberMap().get(fields[0]).setPoint(point);
                }
                //put every line in the list.
                lines.add(String.join(";", fields));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // write to a new file
        if (memberFound) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
                System.out.println("Member " + id + " has lost 1 reputation.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Member with ID " + id + " not found.");
        }
    }

    public void addNewMemberDatabase(Member member) {
        String insertQuery = "INSERT INTO members (id, accountStatus, password, numberOfBooks, point, createDate) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shibalib", "root", "");
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

    public void addNewMember(Member member) {
        String filePath = "src/main/resources/database/members.txt";
        String memberData = member.getId() + ";" +
                member.getStatus() + ";" +
                member.getPassword() + ";" +
                member.getTotalBooksCheckedOut() + ";" +
                member.getPoint() + ";";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(memberData);
            writer.newLine();
            System.out.println("Member added: " + memberData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void increaseBookForMemberDatabase(String id) {
        String updateQuery = "UPDATE members SET numberOfBooks = numberOfBooks + 1 WHERE id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shibalib", "root", "");
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

    public void increaseBookForMember(String id) {
        // lines container.
        List<String> lines = new ArrayList<>();
        boolean memberFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                if (fields[0].equals(id)) {
                    fields[3] = String.valueOf(Integer.parseInt(fields[3]) + 1);
                    memberFound = true;

                    //change in that time.
                    int nob = this.getMemberMap().get(fields[0]).getTotalBooksCheckedOut() + 1;
                    this.getMemberMap().get(fields[0]).setTotalBooksCheckedOut(nob);
                }
                //put every line in the list.
                lines.add(String.join(";", fields));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // write to a new file
        if (memberFound) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
                System.out.println("Member " + id + " has borrowed 1 book.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Member with ID " + id + " not found.");
        }
    }

    public void decreaseBookForMemberDatabase(String id) {
        String updateQuery = "UPDATE members SET numberOfBooks = numberOfBooks - 1 WHERE id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shibalib", "root", "");
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

    public void decreaseBookForMember(String id) {
        // lines container.
        List<String> lines = new ArrayList<>();
        boolean memberFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                if (fields[0].equals(id)) {
                    fields[3] = String.valueOf(Integer.parseInt(fields[3]) - 1);
                    memberFound = true;

                    //change in that time.
                    int nob = this.getMemberMap().get(fields[0]).getTotalBooksCheckedOut() - 1;
                    this.getMemberMap().get(fields[0]).setTotalBooksCheckedOut(nob);
                }
                //put every line in the list.
                lines.add(String.join(";", fields));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // write to a new file
        if (memberFound) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
                System.out.println("Member " + id + " has returned 1 book.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Member with ID " + id + " not found.");
        }
    }

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
