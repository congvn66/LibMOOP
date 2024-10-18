package com.example.proj.Controller;

public class CurrentAccount {
    private static String name;

    public CurrentAccount(String name) {
        this.name = name;
    }

    public static String getName() {
        return name;
    }

}
