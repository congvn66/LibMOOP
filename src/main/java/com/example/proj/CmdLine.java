package com.example.proj;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        while (true) {
            int option = 0;
            boolean validInput = false;


            while (!validInput) {
                System.out.println("===== Library Management System =====");
                System.out.println("1. login as member");
                System.out.println("2. login as librarian");
                System.out.println("3. Exit");
                System.out.print("Please select an option: ");

                if (scanner.hasNextInt()) {
                    option = scanner.nextInt();
                    scanner.nextLine();
                    if (option >= 1 && option <= 3) {
                        validInput = true;
                    } else {
                        System.out.println("Invalid option. Please enter a number between 1 and 3.");
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
                    System.out.println("Goodbye!");
                    return;
            }
            if (this.currentMember != null) {
                break;
            }
            if (this.currentLibrarian != null) {
                this.LibrarianMenu();
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
        int option = 0;
        boolean exit = false;  // Biến để kiểm soát việc thoát menu

        while (!exit) {  // Vòng lặp chính để giữ menu Librarian
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
                        System.out.println("wtf did u type? :(");
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
                        System.out.println("tf did u type?");
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
                        System.out.println("tf did u type?");
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
                    break;
                case 5:
                    // Thêm logic khóa thành viên theo id ở đây
                    System.out.println("Blocking a member...");
                    break;
                case 6:
                    // Thêm logic xóa sách theo id ở đây
                    System.out.println("Removing a book...");
                    break;
                case 7:
                    // Thêm logic cập nhật sách theo id ở đây
                    System.out.println("Updating a book...");
                    break;
                case 8:
                    System.out.println("Goodbye!");
                    exit = true;  // Thoát menu
                    break;
            }
        }
    }


    private boolean loginAsLibrarian() {
        System.out.println("id: ");
        String id = scanner.nextLine();
        System.out.println("password: ");
        String password = scanner.nextLine();


        if (this.librarianMap.get(id) == null || !this.librarianMap.get(id).getPassword().equals(password)) {
            System.out.println("You are not a librarian!");
            return false;
        }
        this.currentLibrarian = this.librarianMap.get(id);
        System.out.println("Logged in successfully");
        return true;
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
