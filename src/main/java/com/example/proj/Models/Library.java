package com.example.proj.Models;

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
