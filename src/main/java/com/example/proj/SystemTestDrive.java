package com.example.proj;

import java.util.Date;
import java.util.List;

public class SystemTestDrive {
    public static void main(String[] args) {
        Librarian newAccount = new Librarian("congdz", AccountStatus.ACTIVE, "congdz");

        List<BookItem> list  = newAccount.findBooksByAuthor("hawking");

        for (BookItem i : list) {
            i.displayInfo();
        }
    }
}
