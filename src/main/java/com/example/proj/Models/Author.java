package com.example.proj.Models;

public class Author {
    private String name;
    private String description;

    public Author(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Author (String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
