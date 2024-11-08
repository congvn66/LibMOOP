package com.example.proj.Models;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Member extends Account{
    private int totalBooksCheckedOut;
    private MemberLogger logger;
    private java.sql.Date createDate;
    private NotificationBox notificationBox;
    private int point;

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public Member() {
        super("none", AccountStatus.NONE, "1234");
        this.totalBooksCheckedOut = 0;
        this.getCatalog().loadCatalogFromDatabase();
        this.point = 0;
        this.createDate = java.sql.Date.valueOf(LocalDate.now());
    }

    public NotificationBox getNotificationBox() {
        if (this.notificationBox == null) {
            this.notificationBox = new NotificationBox();

        }
        this.notificationBox.getNotificationsForMember(this.getId());
        return this.notificationBox;
    }

    public void showNotifications() {
        List<Notification> tmp = this.getNotificationBox().getNotifications();
        for (Notification m : tmp) {
            System.out.println(m.toString());
        }
    }

    public Member(String id, AccountStatus status, String password, int totalBooksCheckedOut, int point, java.sql.Date createDate) {
        super(id, status, password);
        this.totalBooksCheckedOut = totalBooksCheckedOut;
        this.createDate = createDate;
        this.point = point;
        this.logger = new MemberLogger(id);
    }

    public Member(String id, AccountStatus status, String password, int totalBooksCheckOut, int point) {
        super(id, status, password);
        this.totalBooksCheckedOut = totalBooksCheckOut;
        //this.getCatalog().loadCatalogFromDatabase();
        this.point = point;
        this.createDate = java.sql.Date.valueOf(LocalDate.now());
    }

    public int getTotalBooksCheckedOut() {
        return totalBooksCheckedOut;
    }

    public void setTotalBooksCheckedOut(int totalBooksCheckedOut) {
        this.totalBooksCheckedOut = totalBooksCheckedOut;
    }

    public java.sql.Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(java.sql.Date createDate) {
        this.createDate = createDate;
    }

    public MemberLogger getLogger() {
        return logger;
    }

    public void basicActions(String id, Date creationDate, String type) throws ParseException {
        //this.logger.generateMemberLog("src/main/resources/database/members.txt", this.getId());
        if (this.getCatalog().findBookById(id) == null) {
            System.out.println("Book not found.");
            return ;
        }
        BookItem book = this.getCatalog().findBookById(id);
        switch (type) {
            case "LEND":
                if (this.getStatus() == AccountStatus.BLACKLISTED) {
                    System.out.println("You have been banned!");
                    break;
                }
                if (book.getStatus() == BookStatus.AVAILABLE && !book.getIsReferenceOnly()) {
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
