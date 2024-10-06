package com.example.proj;

import java.util.Date;
import java.util.Map;

public class Member extends Account{
    private int totalBooksCheckedOut;
    private LibraryLogger logger;
    private Map<String, String> listOfBook;

    public Member() {
        super("none", AccountStatus.NONE, "1234");
        this.totalBooksCheckedOut = 0;
        this.getCatalog().ImportFromFile();
    }

    public Member(String id, AccountStatus status, String password, int totalBooksCheckOut) {
        super(id, status, password);
        this.totalBooksCheckedOut = totalBooksCheckOut;
        this.getCatalog().ImportFromFile();
        this.logger = new LibraryLogger();
        this.logger.generateMemberLog("src/main/resources/database/members.txt", this.getId());
    }

    public int getTotalBooksCheckedOut() {
        return totalBooksCheckedOut;
    }

    public void setTotalBooksCheckedOut(int totalBooksCheckedOut) {
        this.totalBooksCheckedOut = totalBooksCheckedOut;
    }

    public void lendBook(String id, Date creationDate) {
        if (this.getCatalog().findBookById(id) == null) {
            System.out.println("Book not found.");
            return;
        }
        BookItem book = this.getCatalog().findBookById(id);
        if (book.getStatus() == BookStatus.AVAILABLE && !book.isReferenceOnly()) {
            if (this.getTotalBooksCheckedOut() < 5) {
                // create lending transaction.


                Librarian admin = new Librarian();
                admin.updateBook(id, 12, "LOANED");

            }
        }
    }

    public void printInfo() {
        System.out.println("Id: " + this.getId());
        System.out.println("Status: " + this.getStatus());
        System.out.println("Password: " + this.getPassword());
        System.out.println("Total books checked out: " + this.getTotalBooksCheckedOut());
        System.out.println("\n");
    }
}
