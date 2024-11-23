package com.example.proj.Models;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Authentication {
    private String id;
    private String passWord;
    private Map<String, Librarian> librarianMap;

    public Authentication() {
        librarianMap = new HashMap<>();
        this.loadLibrariansFromDatabase();
    }

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

    private void putLibrarianInMap (Librarian librarian) {
        this.librarianMap.put(librarian.getId(), librarian);
    }

    private void loadLibrariansFromDatabase() {
        String sql = "SELECT id, accountStatus, password FROM librarians";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shibalib", "root", "");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                String id = resultSet.getString("id").trim();
                String accountStatus = resultSet.getString("accountStatus").trim();
                String password = resultSet.getString("password").trim();

                Librarian librarian = new Librarian(id, AccountStatus.valueOf(accountStatus.toUpperCase()), password);

                this.putLibrarianInMap(librarian);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Member checkLoginMember(Librarian librarian) {
        Member member = librarian.getMemberMap().get(this.id);
        if (member == null || !member.getPassword().equals(this.passWord)) {
            return null;
        }
        return member;
    }

    public Librarian checkLoginasLibrarian() {
        if (librarianMap.containsKey(this.id) && librarianMap.get(this.id.trim()).getPassword().equals(this.passWord)) {
        return librarianMap.get(this.id);
        }
        return null;
    }

    public boolean checkRegisterMember() {
        Librarian librarian = new Librarian();
        Member member = librarian.getMemberMap().get(this.id);
        if (member != null ) {
            return false;
        }
        return true;
    }

    public boolean checkLibrarianID() {
        if (librarianMap.containsKey(this.id)) {
            return true;
        }
        return false;
    }

    public boolean checkMemberID() {
        Librarian librarian = new Librarian();
        Member member = librarian.getMemberMap().get(this.id);
        if (member == null) {
            return false;
        }
        return true;
    }

    public boolean checkMemberBookNum(int num) {
        Librarian librarian = new Librarian();
        Member member = librarian.getMemberMap().get(this.id);
        if(member.getTotalBooksCheckedOut() == num) {
            return true;
        }
        return false;
    }
}
