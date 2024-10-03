package com.example.proj;

public class Member extends Account{

    private int totalBooksCheckedOut;

    public Member() {
        super("none", AccountStatus.NONE, "1234");
        this.totalBooksCheckedOut = 0;
        this.getCatalog().ImportFromFile();
    }

    public Member(String id, AccountStatus status, String password, int totalBooksCheckOut) {
        super(id, status, password);
        this.totalBooksCheckedOut = totalBooksCheckOut;
        this.getCatalog().ImportFromFile();
    }

    public int getTotalBooksCheckedOut() {
        return totalBooksCheckedOut;
    }

    public void setTotalBooksCheckedOut(int totalBooksCheckedOut) {
        this.totalBooksCheckedOut = totalBooksCheckedOut;
    }

    public void printInfo() {
        System.out.println("Id: " + this.getId());
        System.out.println("Status: " + this.getStatus());
        System.out.println("Password: " + this.getPassword());
        System.out.println("Total books checked out: " + this.getTotalBooksCheckedOut());
        System.out.println("\n");
    }
}
