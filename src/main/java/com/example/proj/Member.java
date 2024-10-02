package com.example.proj;

public class Member extends Account{

    private int totalBooksCheckedOut;

    public Member(String id, AccountStatus status, String password) {
        super(id, status, password);
        this.getCatalog().ImportFromFile();
    }
}
