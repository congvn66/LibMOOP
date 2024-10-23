package com.example.proj.Controller;

import com.example.proj.Models.AccountStatus;
import com.example.proj.Models.BookItem;
import com.example.proj.Models.Librarian;
import com.example.proj.Models.Member;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CurrentLibrarian {
    private static Librarian librarian;
    private static ObservableList <Member> memberObservableList;
    private static ObservableList <Member> initialMemberList;

    private static ObservableList <BookItem> bookObservableList;

    private static ObservableList <BookItem> initialBookList;

    public CurrentLibrarian(String name, AccountStatus status, String passWord) {
        librarian = new Librarian(name, status, passWord);
        memberObservableList = FXCollections.observableArrayList();
        bookObservableList = FXCollections.observableArrayList();
        for (String i : librarian.getMemberMap().keySet()) {
            memberObservableList.add(librarian.getMemberMap().get(i));
        }
        for (String i : librarian.getCatalog().getBookId().keySet()) {
            bookObservableList.add(librarian.getCatalog().getBookId().get(i));
        }
        initialMemberList = memberObservableList;
        initialBookList = bookObservableList;
    }

    public static ObservableList<Member> getMemberObservableList() {
        return memberObservableList;
    }

    public static ObservableList<BookItem> getBookObservableList() {
        return bookObservableList;
    }
    public static void updateMemberObservableList(Member member) {
        for (Member i : memberObservableList) {
            if (i.getId() == member.getId()) {
                memberObservableList.set(memberObservableList.indexOf(i), member);
                initialMemberList.set(initialMemberList.indexOf(i), member);
                break;
            }
        }
    }

    public static void updateBookObservableList(BookItem bookItem) {
        for (BookItem i : bookObservableList) {
            if (i.getId() == bookItem.getId()) {
                bookObservableList.set(bookObservableList.indexOf(i), bookItem);
                initialBookList.set(initialBookList.indexOf(i), bookItem);
                break;
            }
        }
    }

    public static void deleteMemberObservableList(Member member) {
                initialMemberList.remove(member);
                memberObservableList = initialMemberList;
    }

    public static void deleteBookObservableList(BookItem bookItem) {
        initialBookList.remove(bookItem);
        bookObservableList = initialBookList;
    }
    public static void setMemberObservableList(ObservableList<Member> memberObservableList) {
        CurrentLibrarian.memberObservableList = memberObservableList;
    }

    public static void setBookObservableList(ObservableList<BookItem> bookObservableList) {
        CurrentLibrarian.bookObservableList = bookObservableList;
    }

    public static void returnMemberObservableList() {
        memberObservableList = initialMemberList;
    }

    public static void returnBookObservableList() {
        bookObservableList = initialBookList;
    }

    public static Librarian getLibrarian() {
        return librarian;
    }
}
