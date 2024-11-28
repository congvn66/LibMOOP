package com.example.proj;

import com.example.proj.Models.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class SystemTestDrive {
    public static void main(String[] args) throws ParseException {
        Librarian librarian = new Librarian();
        int tmp = librarian.getCatalog().getTotalBooks().intValue();
        System.out.println(tmp);
//        CmdLine cmdLine = new CmdLine();
//        cmdLine.run();
    }
}
