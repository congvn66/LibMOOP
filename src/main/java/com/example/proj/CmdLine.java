package com.example.proj;

import com.example.proj.Models.*;

import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class CmdLine {
    private Scanner scanner = new Scanner(System.in);
    private String filePath;
    private Map<String, Librarian> librarianMap;
    private Librarian currentLibrarian;
    private Member currentMember;

    public CmdLine() {
        File file = new File("src/main/resources/database/librarians.txt");
        if (file.exists()) {
            String absolute = file.getAbsolutePath();
            this.filePath = absolute;
        }
        this.currentLibrarian = null;
        this.currentMember = null;
        this.librarianMap = new HashMap<>();
        this.loadLibrariansFromDatabase();
    }

    private void putLibrarianInMap (Librarian librarian) {
        this.librarianMap.put(librarian.getId(), librarian);
    }

    private void loadLibrariansFromDatabase() {
        String sql = "SELECT id, accountStatus, password FROM librarians";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shibalib", "root", "");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                String id = resultSet.getString("id").trim();
                String accountStatus = resultSet.getString("accountStatus").trim();
                String password = resultSet.getString("password").trim();

                Librarian librarian = new Librarian(id, AccountStatus.valueOf(accountStatus.toUpperCase()), password);

                this.putLibrarianInMap(librarian);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

                Librarian librarian = new Librarian(id, AccountStatus.valueOf(accountStatus.toUpperCase()), password);

                this.putLibrarianInMap(librarian);


            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() throws ParseException {
        while (true) {
            int option = 0;
            boolean validInput = false;


            while (!validInput) {
                System.out.println("===== Library Management System =====");
                System.out.println("1. login as member");
                System.out.println("2. login as librarian");
                System.out.println("3. register as member");
                System.out.println("4. Exit");
                System.out.print("Please select an option: ");

                if (scanner.hasNextInt()) {
                    option = scanner.nextInt();
                    scanner.nextLine();
                    if (option >= 1 && option <= 4) {
                        validInput = true;
                    } else {
                        System.out.println("Invalid option. Please enter a number between 1 and 4.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next();
                }
            }

            switch (option) {
                case 1:
                    loginAsMember();
                    break;
                case 2:
                    loginAsLibrarian();
                    break;
                case 3:
                    registerAsMember();
                    break;
                case 4:
                    System.out.println("Goodbye!");
                    return;
            }
            if (this.currentMember != null) {
                this.MemberMenu();
                break;
            }
            if (this.currentLibrarian != null) {
                this.LibrarianMenu();
                break;
            }
        }
    }

    private void MemberMenu() throws ParseException {
        int option = 0;
        boolean exit = false;

        while (!exit) {
            boolean validInput = false;

            while (!validInput) {
                System.out.println("===== Library Management System =====");
                System.out.println("1. find book by title.");
                System.out.println("2. find book by author.");
                System.out.println("3. find book by subject.");
                System.out.println("4. lend book.");
                System.out.println("5. return book.");
                System.out.println("6. renew book.");
                System.out.println("7. exit.");
                System.out.print("Please select an option: ");

                if (scanner.hasNextInt()) {
                    option = scanner.nextInt();
                    scanner.nextLine();
                    if (option >= 1 && option <= 7) {
                        validInput = true;
                    } else {
                        System.out.println("Invalid option. Please enter a number between 1 and 7 :( ");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number :( ");
                    scanner.next();
                }
            }

            switch (option) {
                case 1:
                    System.out.println("Title of book: ");
                    String title = this.scanner.nextLine();
                    //Librarian admin1 = new Librarian();
                    List<BookItem> bookItemList1 = this.currentMember.findBooksByTitle(title);
                    if (bookItemList1.isEmpty()) {
                        System.out.println("no books found :(");
                    } else {
                        for (BookItem item : bookItemList1) {
                            item.displayInfo();
                        }
                    }
                    break;
                case 2:
                    System.out.println("Author of book: ");
                    String author = this.scanner.nextLine();
                    //Librarian admin2 = new Librarian();
                    List<BookItem> bookItemList2 = this.currentMember.findBooksByAuthor(author);
                    if (bookItemList2.isEmpty()) {
                        System.out.println("no books found :(");
                    } else {
                        for (BookItem item : bookItemList2) {
                            item.displayInfo();
                        }
                    }
                    break;
                case 3:
                    System.out.println("Subject of book: ");
                    String subject = this.scanner.nextLine();
                    List<BookItem> bookItemList3 = this.currentMember.findBooksBySubject(subject);
                    if (bookItemList3.isEmpty()) {
                        System.out.println("no books found :(");
                    } else {
                        for (BookItem item : bookItemList3) {
                            item.displayInfo();
                        }
                    }
                    break;
                case 4:
                    System.out.println("Id: ");
                    String idL = this.scanner.nextLine();
                    System.out.println("Creation date: ");
                    String d = this.scanner.nextLine();
                    this.currentMember.basicActions(idL, parseDate(d), "LEND");
                    break;
                case 5:
                    System.out.println("Id: ");
                    String idR = this.scanner.nextLine();
                    System.out.println("Creation date: ");
                    String d2 = this.scanner.nextLine();
                    this.currentMember.basicActions(idR, parseDate(d2), "RETURN");
                    break;
                case 6:
                    System.out.println("Id: ");
                    String idRe = this.scanner.nextLine();
                    System.out.println("Creation date: ");
                    String d3 = this.scanner.nextLine();
                    this.currentMember.basicActions(idRe, parseDate(d3), "RENEW");
                    break;
                    //break;
                case 7:
                    System.out.println("Goodbye!");
                    exit = true;
                    break;
            }
        }
    }

    private Date parseDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust format as necessary
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Handle error appropriately
        }
    }

    private void LibrarianMenu() {

        this.currentLibrarian.getCatalog().displayCatalogInfo();
        int option = 0;
        boolean exit = false;

        while (!exit) {
            boolean validInput = false;

            while (!validInput) {
                System.out.println("===== Library Management System =====");
                System.out.println("1. find book by title.");
                System.out.println("2. find book by author.");
                System.out.println("3. find book by subject.");
                System.out.println("4. add book.");
                System.out.println("5. block member by id.");
                System.out.println("6. remove book by id.");
                System.out.println("7. update book by id.");
                System.out.println("8. exit.");
                System.out.print("Please select an option: ");

                if (scanner.hasNextInt()) {
                    option = scanner.nextInt();
                    scanner.nextLine(); // Bỏ qua ký tự newline
                    if (option >= 1 && option <= 8) {
                        validInput = true;
                    } else {
                        System.out.println("Invalid option. Please enter a number between 1 and 8 :( ");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number :( ");
                    scanner.next();  // Bỏ qua giá trị không hợp lệ
                }
            }

            switch (option) {
                case 1:
                    System.out.println("Title of book: ");
                    String title = this.scanner.nextLine();
                    List<BookItem> bookItemList1 = this.currentLibrarian.findBooksByTitle(title);
                    if (bookItemList1.isEmpty()) {
                        System.out.println("no books found :(");
                    } else {
                        for (BookItem item : bookItemList1) {
                            item.displayInfo();
                        }
                    }
                    break;
                case 2:
                    System.out.println("Author of book: ");
                    String author = this.scanner.nextLine();
                    List<BookItem> bookItemList2 = this.currentLibrarian.findBooksByAuthor(author);
                    if (bookItemList2.isEmpty()) {
                        System.out.println("no books found :(");
                    } else {
                        for (BookItem item : bookItemList2) {
                            item.displayInfo();
                        }
                    }
                    break;
                case 3:
                    System.out.println("Subject of book: ");
                    String subject = this.scanner.nextLine();
                    List<BookItem> bookItemList3 = this.currentLibrarian.findBooksBySubject(subject);
                    if (bookItemList3.isEmpty()) {
                        System.out.println("no books found :(");
                    } else {
                        for (BookItem item : bookItemList3) {
                            item.displayInfo();
                        }
                    }
                    break;
                case 4:
                    System.out.println("ISBN: ");
                    String ISBN = this.scanner.nextLine();
                    System.out.println("Title: ");
                    String titleOfBook = this.scanner.nextLine();
                    System.out.println("Subject: ");
                    String subjectdelta = this.scanner.nextLine();
                    System.out.println("Publisher: ");
                    String publisher = this.scanner.nextLine();
                    System.out.println("Language: ");
                    String language = this.scanner.nextLine();
                    System.out.println("Number of pages: ");
                    String noOfPage = this.scanner.nextLine();
                    System.out.println("Author: ");
                    String authorName = this.scanner.nextLine();
                    System.out.println("Author's description: ");
                    String authorDes = this.scanner.nextLine();
                    System.out.println("Id: ");
                    String id = this.scanner.nextLine();
                    if (this.currentLibrarian.getCatalog().findBookById(id.trim()) != null) {
                        System.out.println("existed id :(");
                        break;
                    }
                    System.out.println("Is your book is a reference? (Y for Yes, N for No)");
                    String ref = this.scanner.nextLine();
                    Boolean isReferenceOnly;
                    if (ref.trim().equals("Y")) {
                        isReferenceOnly = true;
                    } else if (ref.trim().equals("N")){
                        isReferenceOnly = false;
                    } else {
                        System.out.println("Wrong call, adding process failed");
                        break;
                    }
                    System.out.println("Price: ");
                    String pr = this.scanner.nextLine();
                    double price = Double.parseDouble(pr.trim());
                    System.out.println("Format: ");

                    String format = this.scanner.nextLine();
                    boolean fmnotPass = true;
                    for (BookFormat value : BookFormat.values()) {
                        if (value.name().equals(format.trim().toUpperCase())) {
                            fmnotPass = false;
                            break;
                        }
                    }
                    if (fmnotPass) {
                        System.out.println("Wrong call, adding process failed");
                        break;
                    }
                    BookFormat bookFormat = BookFormat.valueOf(format.trim().toUpperCase());

                    System.out.println("Status: ");
                    String status = this.scanner.nextLine();
                    boolean stnotPass = true;
                    for (BookStatus value : BookStatus.values()) {
                        if (value.name().equals(status.toUpperCase())) {
                            stnotPass = false;
                            break;
                        }
                    }
                    if (stnotPass) {
                        System.out.println("Wrong call, adding process failed");
                        break;
                    }
                    BookStatus bookStatus = BookStatus.valueOf(status.trim().toUpperCase());
                    System.out.println("Date of purchase (Format yyyy-MM-dd): ");
                    String dOP = this.scanner.nextLine();
                    Date dateOfPurchase = parseDate(dOP.trim());
                    System.out.println("Date of publication (Format yyyy-MM-dd): ");
                    String dOPb = this.scanner.nextLine();
                    Date dateOfPublication = parseDate(dOPb.trim());
                    System.out.println("Number in rack: ");
                    String s = this.scanner.nextLine();
                    int number = Integer.parseInt(s.trim());
                    System.out.println("Rack: ");
                    String location = this.scanner.nextLine();
                    BookItem addin = new BookItem(ISBN, titleOfBook, subjectdelta, publisher, language, noOfPage, authorName,
                            authorDes, id, isReferenceOnly, price, bookFormat, bookStatus, dateOfPurchase, dateOfPublication, number,
                            location);
                    System.out.println("Adding a new book...");
                    this.currentLibrarian.addBookItem(addin);
                    System.out.println("Book added successfully!");
                    this.currentLibrarian.getCatalog().displayCatalogInfo();
                    break;
                case 5:
                    System.out.println("Id: ");
                    String idToBlock = this.scanner.nextLine();
                    System.out.println("Blocking a member...");
                    this.currentLibrarian.blockMemberDatabase(idToBlock);
                    break;
                case 6:
                    System.out.println("Id: ");
                    String idBookToRemove = this.scanner.nextLine();
                    System.out.println("Removing a book...");
                    this.currentLibrarian.removeBook(idBookToRemove);
                    this.currentLibrarian.getCatalog().displayCatalogInfo();
                    break;
                case 7:
                    System.out.println("Id: ");
                    String idBookToUpdate = this.scanner.nextLine();
                    if (this.currentLibrarian.getCatalog().findBookById(idBookToUpdate) == null) {
                        System.out.println("Book not found.");
                        break;
                    }
                    System.out.println("What do you want to update? ");
                    System.out.println("1. ISBN");
                    System.out.println("2. Title");
                    System.out.println("3. Subject");
                    System.out.println("4. Publisher");
                    System.out.println("5. Language");
                    System.out.println("6. Number of pages");
                    System.out.println("7. Author's name");
                    System.out.println("8. Author's description");
                    System.out.println("9. Id");
                    System.out.println("10. Is Reference Only");
                    System.out.println("11. Price");
                    System.out.println("12. Format");
                    System.out.println("13. Status");
                    System.out.println("14. Date of purchase");
                    System.out.println("15. Publication date");
                    System.out.println("16. Number");
                    System.out.println("17. Location");
                    String tmp = this.scanner.nextLine();
                    int field = Integer.parseInt(tmp);
                    switch (field) {
                        case 1:
                            System.out.println("ISBN to change:");
                            String newISBN = this.scanner.nextLine();
                            this.currentLibrarian.updateBook(idBookToUpdate, 1, newISBN);
                            break;
                        case 2:
                            System.out.println("Title to change:");
                            String newTitle = this.scanner.nextLine();
                            this.currentLibrarian.updateBook(idBookToUpdate, 2, newTitle);
                            break;
                        case 3:
                            System.out.println("Subject to change:");
                            String newSubject = this.scanner.nextLine();
                            this.currentLibrarian.updateBook(idBookToUpdate, 3, newSubject);
                            break;
                        case 4:
                            System.out.println("Publisher to change:");
                            String newPublisher = this.scanner.nextLine();
                            this.currentLibrarian.updateBook(idBookToUpdate, 4, newPublisher);
                            break;
                        case 5:
                            System.out.println("Language to change:");
                            String newLanguage = this.scanner.nextLine();
                            this.currentLibrarian.updateBook(idBookToUpdate, 5, newLanguage);
                            break;
                        case 6:
                            System.out.println("Number of Pages to change:");
                            String newNumberOfPages = this.scanner.nextLine();
                            this.currentLibrarian.updateBook(idBookToUpdate, 6, newNumberOfPages);
                            break;
                        case 7:
                            System.out.println("Author Name to change:");
                            String newAuthorName = this.scanner.nextLine();
                            this.currentLibrarian.updateBook(idBookToUpdate, 7, newAuthorName);
                            break;
                        case 8:
                            System.out.println("Author Description to change:");
                            String newAuthorDescription = this.scanner.nextLine();
                            this.currentLibrarian.updateBook(idBookToUpdate, 8, newAuthorDescription);
                            break;
                        case 9:
                            System.out.println("Book ID to change:");
                            String newBookId = this.scanner.nextLine();
                            this.currentLibrarian.updateBook(idBookToUpdate, 9, newBookId);
                            break;
                        case 10:
                            System.out.println("Reference Status (true/false):");
                            boolean newIsReferenceOnly = Boolean.parseBoolean(this.scanner.nextLine());
                            this.currentLibrarian.updateBook(idBookToUpdate, 10, String.valueOf(newIsReferenceOnly));
                            break;
                        case 11:
                            System.out.println("Price to change:");
                            String newPrice = this.scanner.nextLine();
                            this.currentLibrarian.updateBook(idBookToUpdate, 11, newPrice);
                            break;
                        case 12:
                            BookFormat newFormat = null;
                            while (newFormat == null) {
                                System.out.println("Format to change (Valid options: " + Arrays.toString(BookFormat.values()) + "):");
                                try {
                                    String formatInput = this.scanner.nextLine().toUpperCase();
                                    newFormat = BookFormat.valueOf(formatInput);
                                } catch (IllegalArgumentException e) {
                                    System.out.println("Invalid format. Please enter one of the valid options.");
                                }
                            }
                            this.currentLibrarian.updateBook(idBookToUpdate, 12, newFormat.toString());
                            break;
                        case 13:
                            BookStatus newStatus = null;
                            while (newStatus == null) {
                                System.out.println("Status to change (Valid options: " + Arrays.toString(BookStatus.values()) + "):");
                                try {
                                    String statusInput = this.scanner.nextLine().toUpperCase();
                                    newStatus = BookStatus.valueOf(statusInput);
                                } catch (IllegalArgumentException e) {
                                    System.out.println("Invalid status. Please enter one of the valid options.");
                                }
                            }
                            this.currentLibrarian.updateBook(idBookToUpdate, 13, newStatus.toString());
                            break;
                        case 14:
                            System.out.println("Date of Purchase to change (yyyy-MM-dd):");
                            String newDateOfPurchase = this.scanner.nextLine();
                            this.currentLibrarian.updateBook(idBookToUpdate, 14, newDateOfPurchase);
                            break;
                        case 15:
                            System.out.println("Publication Date to change (yyyy-MM-dd):");
                            String newPublicationDate = this.scanner.nextLine();
                            this.currentLibrarian.updateBook(idBookToUpdate, 15, newPublicationDate);
                            break;
                        case 16:
                            System.out.println("Number to change:");
                            String newNumber = this.scanner.nextLine();
                            this.currentLibrarian.updateBook(idBookToUpdate, 16, newNumber);
                            break;
                        case 17:
                            System.out.println("Location to change:");
                            String newLocation = this.scanner.nextLine();
                            this.currentLibrarian.updateBook(idBookToUpdate, 17, newLocation);
                            break;
                        default:
                            System.out.println("Invalid field selected.");
                            break;
                    }

                    System.out.println("Updating a book...");
                    this.currentLibrarian.getCatalog().displayCatalogInfo();
                    break;
                case 8:
                    System.out.println("Goodbye!");
                    exit = true;
                    break;
            }
        }
    }

    private boolean registerAsMember() {
        System.out.println("id: ");
        String id = scanner.nextLine();
        Librarian admin = new Librarian();
        if (admin.getMemberMap().get(id) != null) {
            System.out.println("chosen id!");
            return false;
        }
        System.out.println("password: ");
        String password = this.scanner.nextLine();
        Member member = new Member(id, AccountStatus.ACTIVE, password, 0, 100);
        admin.addNewMemberDatabase(member);
        this.currentMember = member;
        return true;
    }

    private boolean loginAsLibrarian() {
        System.out.println("id: ");
        String id = scanner.nextLine();
        System.out.println("password: ");
        String password = scanner.nextLine();


        String url = "jdbc:mysql://localhost:3306/shibalib";
        String user = "root";
        String pass = "";

        String query = "SELECT accountStatus, password FROM librarians WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String dbPassword = resultSet.getString("password");
                String accountStatuss = resultSet.getString("accountStatus");
                AccountStatus accountStatus = AccountStatus.valueOf(accountStatuss);

                if (dbPassword.equals(password)) {
                    this.currentLibrarian = new Librarian(id, accountStatus, password);
                    System.out.println("Logged in successfully");
                    return true;
                } else {
                    System.out.println("Invalid password!");
                }
            } else {
                System.out.println("You are not a librarian!");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

        return false;
    }

    private boolean loginAsMember() {
        System.out.println("id: ");
        String id = scanner.nextLine();
        System.out.println("password: ");
        String password = scanner.nextLine();

        Librarian librarian = new Librarian();
        Member member = librarian.getMemberMap().get(id);

        if (member == null || !member.getPassword().equals(password)) {
            System.out.println("Wrong id or password");
            return false;
        }

        this.currentMember = member;
        return true;
    }
}
