package com.example.proj;

import java.util.Date;
import java.util.List;

public class SystemTestDrive {
    public static void main(String[] args) {
        Library library = new Library("City Library");

        String path = "C:/Users/Cong/Desktop/libmoop/src/main/resources/database/real_books.txt";

        library.LoadFromFile(path);

        library.displayLibraryInfo();

        System.out.println("---------------------------------------------------------");

        List<BookItem> foundBooksByTitle = library.findBooksByTitle("hAndMaid");
        for (BookItem book : foundBooksByTitle) {
            book.displayInfo();
        }

    }
}
