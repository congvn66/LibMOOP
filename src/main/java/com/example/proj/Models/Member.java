package com.example.proj.Models;

import javafx.beans.property.SimpleIntegerProperty;

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
    private SimpleIntegerProperty point;

    public int getPoint() {
        return point.get();
    }

    public SimpleIntegerProperty getPointProperty() {
        return point;
    }

    public void setPoint(int point) {
        this.point.set(point);
    }

    public Member() {
        super("none", AccountStatus.NONE, "1234");
        this.totalBooksCheckedOut = 0;
        this.getCatalog().loadCatalogFromDatabase();
        this.point.set(0);
        this.createDate = java.sql.Date.valueOf(LocalDate.now());
    }

    public NotificationBox getNotificationBox() {
        if (this.notificationBox == null) {
            this.notificationBox = new NotificationBox();
            this.notificationBox.getNotificationsForMember(this.getId());
        }
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
        this.point = new SimpleIntegerProperty(point);
        this.logger = new MemberLogger(id);
    }

    public Member(String id, AccountStatus status, String password, int totalBooksCheckOut, int point) {
        super(id, status, password);
        this.totalBooksCheckedOut = totalBooksCheckOut;
        //this.getCatalog().loadCatalogFromDatabase();
        this.point = new SimpleIntegerProperty(point);
        this.createDate = java.sql.Date.valueOf(LocalDate.now());
        //this.logger = new MemberLogger(id);
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
    public void replaceNotificationBox(Notification a) {
        for (Notification i : this.notificationBox.getNotifications()) {
            if (a.getBookId().equals(i.getBookId())) {
                this.notificationBox.getNotifications().set(this.notificationBox.getNotifications().indexOf(i), a);
                return;
            }
        }
    }

    public void addNotificationBox(Notification a) {
        this.getNotificationBox().getNotifications().add(a);
    }

    public void deleteNotificationBox(Notification a) {
        for (Notification i : this.notificationBox.getNotifications()) {
            if (a.getBookId().equals(i.getBookId())) {
                this.notificationBox.getNotifications().remove(i);
                return;
            }
        }
    }

    public String basicActions(String id, Date creationDate, String type) throws ParseException {
        //this.logger.generateMemberLog("src/main/resources/database/members.txt", this.getId());
        if (this.getCatalog().findBookById(id) == null) {
            return "Book not found.";
        }
        BookItem book = this.getCatalog().findBookById(id);
        String returnString = "";
        switch (type) {
            case "LEND":
                if (this.getStatus() == AccountStatus.BLACKLISTED) {
                    returnString = "You have been banned!";
                    //System.out.println(returnString);
                    break;
                }
                if (book.getStatus() == BookStatus.AVAILABLE && !book.getIsReferenceOnly()) {
                    if (this.getTotalBooksCheckedOut() < 5) {
                        returnString = this.logger.updateLog(this, book.getId(), creationDate, "LEND");
                        Librarian adminLend = new Librarian();
                        adminLend.increaseBookForMemberDatabase(this.getId());
                        this.updateBook(book.getId(), 13, String.valueOf(BookStatus.LOANED));
                        Notification addNotification = new Notification(book.getId(), LocalDate.now().plusDays(15), false);
                        addNotificationBox(addNotification);
                    } else {
                        returnString = "You have reached maximum number of books!";
                        System.out.println(returnString);
                    }
                } else {
                    returnString = "You can't borrow that book now.";
                    System.out.println(returnString);
                }
                break;
            case "RETURN":
                returnString = this.logger.updateLog(this, book.getId(), creationDate, "RETURN");
                break;
            case "RENEW":
                returnString = this.logger.updateLog(this, book.getId(), creationDate, "RENEW");
                break;
            case "LOST":
                returnString = this.logger.updateLog(this, book.getId(), creationDate, "LOST");
            default:
                break;
        }
        return returnString;
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
