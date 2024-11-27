package com.example.proj.Models;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.json.JSONArray;
import org.json.JSONObject;

public class GoogleBookApi {



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

                // Parse JSON Response
                JSONObject jsonResponse = new JSONObject(response.toString());
                if (jsonResponse.has("items")) {
                    JSONArray items = jsonResponse.getJSONArray("items");
                    JSONObject bookInfo = items.getJSONObject(0).getJSONObject("volumeInfo");

                    // Lấy các thông tin từ bookInfo
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

                    // Tạo đối tượng BookItem
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
    }
}

