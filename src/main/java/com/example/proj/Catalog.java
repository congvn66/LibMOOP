package com.example.proj;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Catalog {
    private String filePath;
    private Date creationDate;
    private int totalBooks;
    private Map<String, List<BookItem>> bookTitles;
    private Map<String, List<BookItem>> bookAuthors;
    private Map<String, List<BookItem>> bookSubjects;
    private Map<Date, List<BookItem>> bookPublicationDates;
    private Map<String, BookItem> bookId;
    // Constructor
    public Catalog() {
        File file = new File("src/main/resources/database/real_books.txt");
        if (file.exists()) {
            String absolute = file.getAbsolutePath();
            this.filePath = absolute;
        }
        this.creationDate = new Date(); // Ngày tạo là ngày hiện tại
        this.totalBooks = 0;
        this.bookTitles = new HashMap<>();
        this.bookAuthors = new HashMap<>();
        this.bookSubjects = new HashMap<>();
        this.bookPublicationDates = new HashMap<>();
        this.bookId = new HashMap<>();
    }


    public void addBookItem(BookItem bookItem) {

        bookTitles.computeIfAbsent(bookItem.getTitle().toLowerCase(), k -> new ArrayList<>()).add(bookItem);


        String author = bookItem.getAuthor().getName().toLowerCase();
        bookAuthors.computeIfAbsent(author, k -> new ArrayList<>()).add(bookItem);


        String subject = bookItem.getSubject().toLowerCase();
        bookSubjects.computeIfAbsent(subject, k -> new ArrayList<>()).add(bookItem);


        Date publicationDate = bookItem.getPublicationDate();
        bookPublicationDates.computeIfAbsent(publicationDate, k -> new ArrayList<>()).add(bookItem);

        String id = bookItem.getId();
        bookId.put(id, bookItem);

        totalBooks++;
    }

    public void writeBookItemToFile(BookItem bookItem) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String filePath = this.filePath;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            File file = new File(filePath);
            if (file.length() > 0) {
                writer.newLine();
            }
            writer.write(bookItem.getISBN() + ";" +
                    bookItem.getTitle() + ";" +
                    bookItem.getSubject() + ";" +
                    bookItem.getPublisher() + ";" +
                    bookItem.getLanguage() + ";" +
                    bookItem.getNumberOfPage() + ";" +
                    bookItem.getAuthor().getName() + ";" +
                    bookItem.getAuthor().getDescription() + ";" +
                    bookItem.getId() + ";" +
                    bookItem.isReferenceOnly() + ";" +
                    bookItem.getPrice() + ";" +
                    bookItem.getFormat() + ";" +
                    bookItem.getStatus() + ";" +
                    dateFormat.format(bookItem.getDateOfPurchase()) + ";" +
                    dateFormat.format(bookItem.getPublicationDate()) + ";" +
                    bookItem.getRack().getNumber() + ";" +
                    bookItem.getRack().getLocationIdentifier());

            //writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public BookItem findBookById(String id) {
        return bookId.get(id);
    }

    public List<BookItem> findBooksByTitle(String title) {
        List<BookItem> matchingBooks = new ArrayList<>();
        for (String innerTitle  : bookTitles.keySet()) {
            if (innerTitle.toLowerCase().contains(title.toLowerCase())) {
                matchingBooks.addAll(bookTitles.get(innerTitle));
            }
        }
        return matchingBooks;
    }


    public List<BookItem> findBooksByAuthor(String author) {
        List<BookItem> matchingBooks = new ArrayList<>();
        for (String innerAuthor  : bookAuthors.keySet()) {
            if (innerAuthor.toLowerCase().contains(author.toLowerCase())) {
                matchingBooks.addAll(bookAuthors.get(innerAuthor));
            }
        }
        return matchingBooks;
    }


    public List<BookItem> findBooksBySubject(String subject) {
        return bookSubjects.getOrDefault(subject.toLowerCase(), new ArrayList<>());
    }


    public List<BookItem> findBooksByPublicationDate(Date publicationDate) {
        return bookPublicationDates.getOrDefault(publicationDate, new ArrayList<>());
    }


    public boolean updateCatalog() {
        // Logic để cập nhật catalog
        return true; // Trả về true nếu thành công
    }


    public int getTotalBooks() {
        return totalBooks;
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



    public void editBook(String bookId, int fieldToEdit, String newValue) {
        List<String> lines = new ArrayList<>();
        boolean bookFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                if (fields[8].equals(bookId)) {
                    bookFound = true;
                    switch (fieldToEdit) {
                        case 1: fields[0] = newValue; break;  // ISBN
                        case 2: fields[1] = newValue; break;  // title
                        case 3: fields[2] = newValue; break;  // subject
                        case 4: fields[3] = newValue; break;  // publisher
                        case 5: fields[4] = newValue; break;  // language
                        case 6: fields[5] = newValue; break;  // numberOfPage
                        case 7: fields[6] = newValue; break;  // authorName
                        case 8: fields[7] = newValue; break;  // authorDescription
                        case 9: fields[8] = newValue; break;  // id
                        case 10: fields[9] = newValue; break; // isReferenceOnly
                        case 11: fields[10] = newValue; break; // price
                        case 12: fields[11] = newValue; break; // format
                        case 13: fields[12] = newValue; break; // status
                        case 14: fields[13] = newValue; break; // dateOfPurchase
                        case 15: fields[14] = newValue; break; // publicationDate
                        case 16: fields[15] = newValue; break; // number
                        case 17: fields[16] = newValue; break; // location
                        default:
                            System.out.println("Invalid field.");
                            return;
                    }
                }

                lines.add(String.join(";", fields));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ghi lại file với thông tin đã chỉnh sửa
        if (bookFound) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
                System.out.println("Book " + bookId + " has been updated.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.totalBooks = 0;
            // load again.
            this.bookSubjects = new HashMap<>();
            this.bookAuthors = new HashMap<>();
            this.bookTitles = new HashMap<>();
            this.bookId = new HashMap<>();
            this.bookPublicationDates = new HashMap<>();
            this.ImportFromFile();
        } else {
            System.out.println("Book with ID " + bookId + " not found.");
        }
    }

    public void ImportFromFile() {
        try (BufferedReader br  = new BufferedReader(new FileReader(this.filePath))) {
            String line;
            //br.readLine();
            while ((line = br.readLine()) != null) {
                String[] tmp = line.split(";");
                if (tmp.length != 17) {
                    continue;
                }
                String ISBN = tmp[0].trim();
                String title = tmp[1].trim();
                String subject = tmp[2].trim();
                String publisher = tmp[3].trim();
                String language = tmp[4].trim();
                String numberOfPage = tmp[5].trim();
                String authorName = tmp[6].trim();
                String authorDescription = tmp[7].trim();
                String id = tmp[8].trim();
                Boolean isRefOnly = Boolean.parseBoolean(tmp[9].trim());
                double price = Double.parseDouble(tmp[10].trim());
                String bookFormat = tmp[11].trim();
                String bookStatus = tmp[12].trim();
                Date dateOfPurchase = parseDate(tmp[13].trim());
                Date publicationDate = parseDate(tmp[14].trim());
                int number = Integer.parseInt(tmp[15].trim());
                String location = tmp[16].trim();

                BookItem addin = new BookItem(ISBN, title, subject, publisher, language, numberOfPage, authorName,authorDescription,
                        id, isRefOnly, price, BookFormat.valueOf(bookFormat.toUpperCase()), BookStatus.valueOf(bookStatus.toUpperCase()),
                        dateOfPurchase, publicationDate, number, location);

                this.addBookItem(addin);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot open file!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public boolean removeBookById(String id) {
        if (!bookId.containsKey(id)) {
            System.out.println("Book not found!");
            return false;
        }

        BookItem bookToRemove = this.bookId.get(id);

        String title = bookToRemove.getTitle();
        List<BookItem> rmFromTitle = this.bookTitles.get(title);
        if (rmFromTitle != null) {
            rmFromTitle.remove(bookToRemove);
            if (rmFromTitle.isEmpty()) {
                bookTitles.remove(title);
            }
        }

        String author = bookToRemove.getAuthor().getName();
        List<BookItem> rmFromAuthor = bookAuthors.get(author);
        if (rmFromAuthor != null) {
            rmFromAuthor.remove(bookToRemove);
            if (rmFromAuthor.isEmpty()) {
                bookAuthors.remove(author);
            }
        }

        String subject = bookToRemove.getSubject();
        List<BookItem> rmFromSubject = bookAuthors.get(author);
        if (rmFromSubject != null) {
            rmFromSubject.remove(bookToRemove);
            if (rmFromSubject.isEmpty()) {
                bookAuthors.remove(author);
            }
        }

        Date date = bookToRemove.getPublicationDate();
        List<BookItem> rmFromDate = bookAuthors.get(author);
        if (rmFromDate != null) {
            rmFromDate.remove(bookToRemove);
            if (rmFromDate.isEmpty()) {
                bookAuthors.remove(author);
            }
        }

        this.removeBookFromFile(id);

        totalBooks--;
        return true;
    }

    private void removeBookFromFile(String id) {
        File inputFile = new File(this.filePath);
        File tempFile = new File("temp_book_items.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                String[] fields = currentLine.split(";");
                String idt = fields[8];

                if (idt.equals(id)) {
                    continue;
                }

                writer.write(currentLine);
                writer.newLine();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!inputFile.delete()) {
            System.out.println("Could not delete the original file");
            return;
        }
        if (!tempFile.renameTo(inputFile)) {
            System.out.println("Could not rename the temp file to the original file");
            return;
        }
    }

    public void displayCatalogInfo() {
        System.out.println("Catalog Creation Date: " + creationDate);
        System.out.println("Total Books in Catalog: " + totalBooks);
    }
}
