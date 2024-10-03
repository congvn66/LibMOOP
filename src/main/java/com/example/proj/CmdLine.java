package com.example.proj;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CmdLine {
    private Scanner scanner = new Scanner(System.in);
    private String filePath;
    private Map<String, Librarian> librarianMap;

    public CmdLine() {
        File file = new File("src/main/resources/database/librarians.txt");
        if (file.exists()) {
            String absolute = file.getAbsolutePath();
            this.filePath = absolute;
        }
        this.librarianMap = new HashMap<>();
        this.LoadLibrarianFromFile();
    }

    private void putLibrarianInMap (Librarian librarian) {
        this.librarianMap.put(librarian.getId(), librarian);
    }

    private void LoadLibrarianFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(this.filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tmp = line.split(";");
                if (tmp.length != 3) {
                    continue;
                }
                String id = tmp[0].trim();
                String accountStatus = tmp[1].trim();
                String password = tmp[2].trim();
                //int totalBooksCheckedOut = Integer.parseInt(tmp[3].trim());

                Librarian librarian = new Librarian(id, AccountStatus.valueOf(accountStatus.toUpperCase()), password);

                this.putLibrarianInMap(librarian);


            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        this.loginAsLibrarian();
    }

    private boolean loginAsLibrarian() {
        System.out.println("id: ");
        String id = scanner.nextLine();
        System.out.println("password: ");
        String password = scanner.nextLine();

        Librarian current = new Librarian(id, AccountStatus.ACTIVE, password);

        if (this.librarianMap.get(current.getId()) == null) {
            System.out.println("You are not a librarian!");
            return false;
        }
        System.out.println("Logged in successfully");
        return true;
    }
}
