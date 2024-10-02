package com.example.proj;

import com.example.proj.Catalog;

import java.util.Date;
import java.util.List;

public class Library {
    private String name;
    private Catalog catalog;

    // Constructor
    public Library(String name) {
        this.name = name;
        this.catalog = new Catalog();
    }









    public void displayLibraryInfo() {
        System.out.println("Library Name: " + name);
        catalog.displayCatalogInfo();
    }
}
