package com.example.proj.Controller;

import com.example.proj.Models.BookItem;
import com.example.proj.Models.Catalog;
import com.example.proj.Models.Librarian;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DisplayImageExample {

    private static List<BookItem> books = new ArrayList<>();

    public static void main(String[] args) {

        Librarian l = new Librarian();


        JFrame frame = new JFrame("Display Book Image Example");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        String bookId = JOptionPane.showInputDialog(frame, "Enter the book's id to search:");


        BookItem foundBook = l.getCatalog().findBookById(bookId);
        System.out.println(foundBook.generateImageNameFromChosenImage(foundBook.getImgName()));

        if (foundBook != null) {

            String imagePath = foundBook.generateImagePathFromImageName(foundBook.getImgName());
            ImageIcon imageIcon = new ImageIcon(imagePath);


            if (imageIcon.getIconWidth() == -1) {
                JOptionPane.showMessageDialog(frame, "Unable to load image for the book: " + foundBook.getTitle());
                return;
            }


            JLabel label = new JLabel(imageIcon);


            frame.add(label);
            frame.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(frame, "Book not found.");
        }
    }



}
