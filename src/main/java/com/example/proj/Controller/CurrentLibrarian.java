package com.example.proj.Controller;

import com.example.proj.Models.AccountStatus;
import com.example.proj.Models.Librarian;
import com.example.proj.Models.Member;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CurrentLibrarian {
    private static Librarian librarian;
    private static ObservableList <Member> memberObservableList;
    public CurrentLibrarian(String name, AccountStatus status, String passWord) {
        librarian = new Librarian(name, status, passWord);
        memberObservableList = FXCollections.observableArrayList();
        for (String i : librarian.getMemberMap().keySet()) {
            memberObservableList.add(librarian.getMemberMap().get(i));
        }
    }

    public static ObservableList<Member> getMemberObservableList() {
        return memberObservableList;
    }

    public static Librarian getLibrarian() {
        return librarian;
    }
}
