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
    private static ObservableList <Member> initialMemberList;

    public CurrentLibrarian(String name, AccountStatus status, String passWord) {
        librarian = new Librarian(name, status, passWord);
        memberObservableList = FXCollections.observableArrayList();
        for (String i : librarian.getMemberMap().keySet()) {
            memberObservableList.add(librarian.getMemberMap().get(i));
        }
        initialMemberList = memberObservableList;
    }

    public static ObservableList<Member> getMemberObservableList() {
        return memberObservableList;
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

    public static void deleteMemberObservableList(Member member) {
                memberObservableList.remove(member);
                initialMemberList.remove(member);
    }

    public static void setMemberObservableList(ObservableList<Member> memberObservableList) {
        CurrentLibrarian.memberObservableList = memberObservableList;
    }

    public static void returnMemberObservableList() {
        memberObservableList = initialMemberList;
    }

    public static Librarian getLibrarian() {
        return librarian;
    }
}
