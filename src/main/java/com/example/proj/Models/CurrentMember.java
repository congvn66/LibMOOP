package com.example.proj.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;

public class CurrentMember {
    private static Member member;
    private static ObservableList<BookItem> listOfMemBook;
    private static ObservableList<BookItem> initialListOfMemBook;
    private static ObservableList<BookItem> listOfLibBook;
    private static ObservableList<BookItem> initialListOfLibBook;

    public CurrentMember(Member member1) {
        member = member1;
        listOfLibBook = FXCollections.observableArrayList();
        listOfMemBook = FXCollections.observableArrayList();
        for (Log i: member.getLogger().getMemListOfLog()) {
            listOfMemBook.add(member.getCatalog().getBookId().get(i.getBookId()));
        }
        for (String i : member.getCatalog().getBookId().keySet()) {
            listOfLibBook.add(member.getCatalog().getBookId().get(i));
        }
        initialListOfLibBook = listOfLibBook;
        initialListOfMemBook = listOfMemBook;
    }

    public static ObservableList<BookItem> getListOfLibBook() {
        return listOfLibBook;
    }

    public static ObservableList<BookItem> getListOfMemBook() {
        return listOfMemBook;
    }
    public static void updateListOfMemBook(BookItem bookItem) {
        for (BookItem i : listOfMemBook) {
            if (i.getId() == member.getId()) {
                if (listOfMemBook != initialListOfMemBook) {
                    listOfMemBook.set(listOfMemBook.indexOf(i), bookItem);
                    initialListOfMemBook.set(initialListOfMemBook.indexOf(i), bookItem);
                    break;
                } else {
                    initialListOfMemBook.set(initialListOfMemBook.indexOf(i), bookItem);
                    break;
                }
            }
        }
    }

    public static void updateListOfLibBook(BookItem bookItem) {
        for (BookItem i : listOfLibBook) {
            if (i.getId().equals(bookItem.getId())) {
                if (listOfLibBook != initialListOfLibBook) {
                    listOfLibBook.set(listOfLibBook.indexOf(i), bookItem);
                }
                initialListOfLibBook.set(initialListOfLibBook.indexOf(i), bookItem);
                return;
            }
        }
    }

    public static void deleteListOfMemBook(BookItem bookItem) {
        initialListOfMemBook.remove(bookItem);
        if (listOfMemBook != initialListOfMemBook) {
            listOfMemBook.remove(bookItem);
        }
    }

    public static void addListOfMemBook(BookItem bookItem) {
        initialListOfMemBook.add(bookItem);
        if (listOfMemBook != initialListOfMemBook) {
            listOfMemBook.add(bookItem);
        }
    }

    public static void setListOfMemBook(ObservableList<BookItem> listOfMemBook) {
        CurrentMember.listOfMemBook = listOfMemBook;
    }

    public static void setListOfLibBook(ObservableList<BookItem> listOfLibBook) {
        CurrentMember.listOfLibBook = listOfLibBook;
    }

    public static void returnListOfMemBook() {
        listOfMemBook = initialListOfMemBook;
    }

    public static void returnListOfLibBook() {
        listOfLibBook = initialListOfLibBook;
    }

    public static Member getMember() {
        return member;
    }
}
