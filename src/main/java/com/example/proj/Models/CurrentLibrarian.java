package com.example.proj.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CurrentLibrarian {
    private static Librarian librarian;
    private static ObservableList <Member> memberObservableList;
    private static ObservableList <Member> initialMemberList;

    private static ObservableList <BookItem> bookObservableList;

    private static ObservableList <BookItem> initialBookList;

    public CurrentLibrarian(Librarian librarian) {
        this.librarian = librarian;
    }

    public static ObservableList<Member> getMemberObservableList() {
        if (memberObservableList == null) {
            memberObservableList = FXCollections.observableArrayList();
            for (String i : librarian.getMemberMap().keySet()) {
                memberObservableList.add(librarian.getMemberMap().get(i));
            }
            initialMemberList = memberObservableList;
            return memberObservableList;
        }
            return memberObservableList;
    }

    public static ObservableList<BookItem> getBookObservableList() {
        if (bookObservableList == null) {
            bookObservableList = FXCollections.observableArrayList();
            for (String i : librarian.getCatalog().getBookId().keySet()) {
                bookObservableList.add(librarian.getCatalog().getBookId().get(i));
            }
            initialBookList = bookObservableList;
            return bookObservableList;
        }
        return bookObservableList;
    }
    public static void updateMemberObservableList(Member member) {
        for (Member i : memberObservableList) {
            if (i.getId() == member.getId()) {
                if (memberObservableList != initialMemberList) {
                    memberObservableList.set(memberObservableList.indexOf(i), member);
                    initialMemberList.set(initialMemberList.indexOf(i), member);
                    break;
                } else {
                    initialMemberList.set(initialMemberList.indexOf(i), member);
                    break;
                }
            }
        }
    }

    public static void updateBookObservableList(BookItem bookItem) {
        for (BookItem i : bookObservableList) {
            if (i.getId().equals(bookItem.getId())) {
                if (bookObservableList != initialBookList) {
                    bookObservableList.set(bookObservableList.indexOf(i), bookItem);
                    initialBookList.set(initialBookList.indexOf(i), bookItem);
                    return;
                } else {
                    initialBookList.set(initialBookList.indexOf(i), bookItem);
                    return;
                }
            }
        }
    }

    public static void deleteMemberObservableList(Member member) {
        if (initialMemberList != memberObservableList) {
            initialMemberList.remove(member);
            memberObservableList.remove(member);
        } else {
            initialMemberList.remove(member);
        }
    }

    public static void deleteBookObservableList(BookItem bookItem) {
        if (initialBookList != bookObservableList) {
            initialBookList.remove(bookItem);
            bookObservableList.remove(bookItem);
        } else {
            initialBookList.remove(bookItem);
        }
    }

    public static void addBookObservableList(BookItem bookItem) {
        if (initialBookList != bookObservableList) {
            initialBookList.add(bookItem);
            bookObservableList.add(bookItem);
        } else {
            initialBookList.add(bookItem);
        }
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
