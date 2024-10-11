package com.example.proj;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public class Member extends Account{
    private int totalBooksCheckedOut;
    private LibraryLogger logger;
    private Map<String, String> listOfBook;

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    private int point;

    public Member() {
        super("none", AccountStatus.NONE, "1234");
        this.totalBooksCheckedOut = 0;
        this.getCatalog().ImportFromFile();
        this.point = 0;
    }

    public Member(String id, AccountStatus status, String password, int totalBooksCheckOut, int point) {
        super(id, status, password);
        this.totalBooksCheckedOut = totalBooksCheckOut;
        this.getCatalog().ImportFromFile();

        this.point = point;
    }

    public int getTotalBooksCheckedOut() {
        return totalBooksCheckedOut;
    }

    public void setTotalBooksCheckedOut(int totalBooksCheckedOut) {
        this.totalBooksCheckedOut = totalBooksCheckedOut;
    }

    public void basicActions(String id, Date creationDate, String type) throws ParseException {
        this.logger = new LibraryLogger();
        this.logger.generateMemberLog("src/main/resources/database/members.txt", this.getId());
        if (this.getCatalog().findBookById(id) == null) {
            System.out.println("Book not found.");
            return;
        }
        BookItem book = this.getCatalog().findBookById(id);
        switch (type) {
            case "LEND":
                if (this.getStatus() == AccountStatus.BLACKLISTED) {
                    System.out.println("You have been banned!");
                    break;
                }
                if (book.getStatus() == BookStatus.AVAILABLE && !book.isReferenceOnly()) {
                    if (this.getTotalBooksCheckedOut() < 5) {
                        this.logger.updateLog(this, book.getId(), creationDate, "LEND");
                        Librarian adminLend = new Librarian();
                        adminLend.increaseBookForMemberDatabase(this.getId());
                        this.updateBook(book.getId(), 13, "LOANED");
                    } else {
                        System.out.println("You have reached maximum number of books!");
                    }
                } else {
                    System.out.println("You can't borrow that book now.");
                }
                break;
            case "RETURN":
                this.logger.updateLog(this, book.getId(), creationDate, "RETURN");
                break;
            case "RENEW":
                this.logger.updateLog(this, book.getId(), creationDate, "RENEW");
                break;
            default:
                break;
        }
    }

    public void printInfo() {
        System.out.println("Id: " + this.getId());
        System.out.println("Status: " + this.getStatus());
        System.out.println("Password: " + this.getPassword());
        System.out.println("Total books checked out: " + this.getTotalBooksCheckedOut());
        System.out.println("Points: " + this.getPoint());
        System.out.println("\n");
    }
}
