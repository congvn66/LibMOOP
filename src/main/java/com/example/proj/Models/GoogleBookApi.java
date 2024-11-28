package com.example.proj.Models;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONObject;

public class GoogleBookApi {
    public static List<BookItem> getBookDetailsByAuthor(String author) {
        List<BookItem> bookItems = new ArrayList<>();
        try {
            String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=inauthor:" + author.replace(" ", "+");
            URL url = new URL(apiUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray items = jsonResponse.optJSONArray("items");
            if (items != null) {
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    JSONObject volumeInfo = item.getJSONObject("volumeInfo");

                    String isbn = getISBN(volumeInfo);
                    String bookTitle = volumeInfo.optString("title", "N/A");
                    String authors = getAuthors(volumeInfo);
                    String publisher = volumeInfo.optString("publisher", "N/A");
                    String publishedDate = volumeInfo.optString("publishedDate", "N/A");
                    String subject = getSubject(volumeInfo);
                    String language = volumeInfo.optString("language", "No language available");
                    String numberOfPages = volumeInfo.optString("pageCount", "No number of pages available");
                    String authorDescription = "Amazing writer";
                    String id = null;
                    boolean isReferenceOnly = false;
                    double price = 0.0;
                    BookFormat format = BookFormat.PAPERBACK;
                    BookStatus status = BookStatus.AVAILABLE;
                    Date dateOfPurchase = new Date();
                    Date publicationDate = parsePublicationDate(volumeInfo);
                    int number = 1;
                    String location = "Unknown";
                    String imgName = getImageUrl(volumeInfo);

                    bookItems.add(new BookItem(
                            isbn,
                            bookTitle,
                            subject,
                            publisher,
                            language,
                            numberOfPages,
                            authors,
                            authorDescription,
                            id,
                            isReferenceOnly,
                            price,
                            format,
                            status,
                            dateOfPurchase,
                            publicationDate,
                            number,
                            location,
                            imgName
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookItems;
    }


    public static List<BookItem> getBookDetailsByTitle(String title) {
        List<BookItem> bookItems = new ArrayList<>();
        try {
            String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=" + title.replace(" ", "+");
            URL url = new URL(apiUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray items = jsonResponse.optJSONArray("items");
            if (items != null) {
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    JSONObject volumeInfo = item.getJSONObject("volumeInfo");

                    String isbn = getISBN(volumeInfo);
                    String bookTitle = volumeInfo.optString("title", "N/A");
                    String authors = getAuthors(volumeInfo);
                    String publisher = volumeInfo.optString("publisher", "N/A");
                    String publishedDate = volumeInfo.optString("publishedDate", "N/A");
                    String subject = getSubject(volumeInfo);
                    String language = volumeInfo.optString("language", "No language available");
                    String numberOfPages = volumeInfo.optString("pageCount", "No number of pages available");
                    String authorDescription = "Amazing writer";
                    String id = null;
                    boolean isReferenceOnly = false;
                    double price = 0.0;
                    BookFormat format = BookFormat.PAPERBACK;
                    BookStatus status = BookStatus.AVAILABLE;
                    Date dateOfPurchase = new Date();
                    Date publicationDate = parsePublicationDate(volumeInfo);
                    int number = 1;
                    String location = "Unknown";
                    String imgName = getImageUrl(volumeInfo);


                    bookItems.add(new BookItem(
                            isbn,
                            bookTitle,
                            subject,
                            publisher,
                            language,
                            numberOfPages,
                            authors,
                            authorDescription,
                            id,
                            isReferenceOnly,
                            price,
                            format,
                            status,
                            dateOfPurchase,
                            publicationDate,
                            number,
                            location,
                            imgName
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookItems;
    }


    public static BookItem getBookDetailsByISBN(String isbn) {
        String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;

        try {

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                if (jsonResponse.has("items")) {
                    JSONArray items = jsonResponse.getJSONArray("items");
                    JSONObject bookInfo = items.getJSONObject(0).getJSONObject("volumeInfo");

                    String title = bookInfo.optString("title", "No title available");
                    String publisher = bookInfo.optString("publisher", "No publisher available");
                    String subject = getSubject(bookInfo);
                    String language = bookInfo.optString("language", "No language available");
                    String numberOfPages = bookInfo.optString("pageCount", "No number of pages available");
                    String authorName = getAuthors(bookInfo);
                    String authorDescription = "Amazing writer";
                    String id = null;
                    boolean isReferenceOnly = false;
                    double price = 0.0;
                    BookFormat format = BookFormat.PAPERBACK;
                    BookStatus status = BookStatus.AVAILABLE;
                    Date dateOfPurchase = new Date();
                    Date publicationDate = parsePublicationDate(bookInfo);
                    int number = 1;
                    String location = "Unknown";
                    String imgName = getImageUrl(bookInfo);

                    BookItem book = new BookItem(isbn, title, subject, publisher, language, numberOfPages,
                            authorName, authorDescription, id, isReferenceOnly, price, format, status,
                            dateOfPurchase, publicationDate, number, location, imgName);

                    return book;
                } else {
                    System.out.println("No book found for the provided ISBN.");
                }
            } else {
                System.out.println("Error: Unable to fetch data. HTTP Response Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getSubject(JSONObject bookInfo) {
        if (bookInfo.has("categories")) {
            JSONArray categories = bookInfo.getJSONArray("categories");
            return categories.length() > 0 ? categories.getString(0) : "No subject available";
        }
        return "No subject available";
    }

    private static String getISBN(JSONObject bookInfo) {
        if (bookInfo.has("industryIdentifiers")) {
            JSONArray identifiers = bookInfo.getJSONArray("industryIdentifiers");
            for (int i = 0; i < identifiers.length(); i++) {
                JSONObject identifier = identifiers.getJSONObject(i);
                if (identifier.getString("type").equals("ISBN_13")) {
                    return identifier.getString("identifier");
                }
            }
            // Nếu không có ISBN-13, kiểm tra ISBN-10
            for (int i = 0; i < identifiers.length(); i++) {
                JSONObject identifier = identifiers.getJSONObject(i);
                if (identifier.getString("type").equals("ISBN_10")) {
                    return identifier.getString("identifier");
                }
            }
        }
        return "No ISBN available"; 
    }


    private static String getAuthors(JSONObject bookInfo) {
        if (bookInfo.has("authors")) {
            JSONArray authors = bookInfo.getJSONArray("authors");
            StringBuilder authorList = new StringBuilder();
            for (int i = 0; i < authors.length(); i++) {
                authorList.append(authors.getString(i));
                if (i < authors.length() - 1) {
                    authorList.append(", ");
                }
            }
            return authorList.toString();
        }
        return "No author available";
    }

    private static Date parsePublicationDate(JSONObject bookInfo) {
        String dateString = bookInfo.optString("publishedDate", "No publication date available");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        try {
            if (!dateString.equals("No publication date available")) {
                return dateFormat.parse(dateString);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getImageUrl(JSONObject bookInfo) {
        if (bookInfo.has("imageLinks")) {
            JSONObject imageLinks = bookInfo.getJSONObject("imageLinks");
            return imageLinks.optString("thumbnail", "default.png");
        }
        return "default.png";
    }



    public static void main(String[] args) {
        String isbn = "0747532699";
        BookItem book = getBookDetailsByISBN(isbn);
        if (book != null) {
            book.displayInfo();
        }

        System.out.println("-------------------------------------------");

        List<BookItem> books = getBookDetailsByTitle("Harry Potter");
        for (BookItem book1 : books) {
            book1.displayInfo();
        }

        System.out.println("-------------------------------------------");

        List<BookItem> books2 = getBookDetailsByAuthor("J. K. Rowling");
        for (BookItem book2 : books2) {
            book2.displayInfo();
        }
    }
}

