package com.example.proj;

import java.util.Date;
import java.util.List;

public class SystemTestDrive {
    public static void main(String[] args) {

        Library library = new Library("City Library");

        library.LoadFromFile();

        library.displayLibraryInfo();


    }
}
