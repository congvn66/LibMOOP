package com.example.proj.Models;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class GoogleBookApi {
    private static final Map<String, BookItem> cache = new HashMap<>();

    /**
     * Fetches book details by a given author using the Google Books API.
     * <p>
     * This method sends a GET request to the Google Books API, retrieves book details
     * related to the specified author, and maps them into a list of {@link BookItem} objects.
     *
     * @param author the name of the author whose books are to be retrieved.
     *               Spaces in the author's name will be replaced with "+" for URL encoding.
     * @return a {@link List} of {@link BookItem} containing details of books by the specified author.
     *         Returns an empty list if no books are found or an exception occurs.
     */
    public static List<BookItem> getBookDetailsByAuthor(String author) {
        long startTime = System.currentTimeMillis();
        List<BookItem> bookItems = new ArrayList<>();
        try {
            // create url api.
            String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=inauthor:" + author.replace(" ", "+");
            URL url = new URL(apiUrl);

            // send get request.
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            // return data value is JSON format.
            connection.setRequestProperty("Accept", "application/json");

            // read return value from API.
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // convert JSON string to JSON object.
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray items = jsonResponse.optJSONArray("items");
            if (items != null) {
                // traverse
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    JSONObject volumeInfo = item.getJSONObject("volumeInfo");

                    // get data.
                    String isbn = getISBN(volumeInfo);
                    String bookTitle = volumeInfo.optString("title", "N/A");
                    String authors = getAuthors(volumeInfo);
                    String publisher = volumeInfo.optString("publisher", "N/A");
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

                    // create book item with default value.
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
        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;

        System.out.println("Time elapsed " + duration + " ms");
        return bookItems;
    }

    /**
     * Fetches book details by a given title using the Google Books API.
     * <p>
     * This method sends a GET request to the Google Books API to search for books
     * matching the specified title. The API response is parsed into a list of
     * {@link BookItem} objects containing detailed information about each book.
     *
     * @param title the title of the book(s) to search for. Spaces in the title are
     *              replaced with "+" for URL encoding.
     * @return a {@link List} of {@link BookItem} containing details of books matching the title.
     *         Returns an empty list if no books are found or if an error occurs during the process.
     */
    public static List<BookItem> getBookDetailsByTitle(String title) {
        List<BookItem> bookItems = new ArrayList<>();
        try {
            // create url api.
            String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=" + title.replace(" ", "+");
            URL url = new URL(apiUrl);

            // send get request.
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            // return data value is JSON format.
            connection.setRequestProperty("Accept", "application/json");

            // read return value from API.
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // convert JSON string to JSON object.
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray items = jsonResponse.optJSONArray("items");
            if (items != null) {
                // traverse
                for (int i = 0; i < items.length(); i++) {
                    // get data.
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

                    // create book item with default value.
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


    /**
     * Fetches book details from Google Books API using the provided ISBN.
     * If the book information for the given ISBN is already cached, it retrieves the data from the cache.
     * Otherwise, it makes an HTTP GET request to the API to fetch the details and stores the result in the cache.
     *
     * @param isbn the ISBN of the book to fetch details for.
     * @return a {@link BookItem} object containing the book details if found, otherwise returns {@code null}.
     */
    public static BookItem getBookDetailsByISBN(String isbn) {
        // caching for less time.
        if (cache.containsKey(isbn)) {
            return cache.get(isbn);
        }

        // api url.
        String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;

        try {

            // send get request.
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // check response code, if 200, OK.
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                // read from JSON data
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // data extraction.
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

                    // add stuff.
                    BookItem book = new BookItem(isbn, title, subject, publisher, language, numberOfPages,
                            authorName, authorDescription, id, isReferenceOnly, price, format, status,
                            dateOfPurchase, publicationDate, number, location, imgName);

                    // cache save.
                    if (book != null) {
                        cache.put(isbn, book);
                    }

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



    /**
     * Extracts the primary subject of the book from the JSON object.
     * If no subject is found, returns a default message.
     *
     * @param bookInfo the JSON object containing book details.
     * @return the subject of the book or a default message if unavailable.
     */
    private static String getSubject(JSONObject bookInfo) {
        if (bookInfo.has("categories")) {
            JSONArray categories = bookInfo.getJSONArray("categories");
            return categories.length() > 0 ? categories.getString(0) : "No subject available";
        }
        return "No subject available";
    }


    /**
     * Extracts the ISBN of the book from the JSON object.
     * Prioritizes ISBN-13; if unavailable, falls back to ISBN-10.
     * If no ISBN is found, returns a default message.
     *
     * @param bookInfo the JSON object containing book details.
     * @return the ISBN (either ISBN-13 or ISBN-10) or a default message if unavailable.
     */
    private static String getISBN(JSONObject bookInfo) {
        if (bookInfo.has("industryIdentifiers")) {
            JSONArray identifiers = bookInfo.getJSONArray("industryIdentifiers");
            for (int i = 0; i < identifiers.length(); i++) {
                JSONObject identifier = identifiers.getJSONObject(i);
                if (identifier.getString("type").equals("ISBN_13")) {
                    return identifier.getString("identifier");
                }
            }
            // if no ISBN-13, then ISBN-10
            for (int i = 0; i < identifiers.length(); i++) {
                JSONObject identifier = identifiers.getJSONObject(i);
                if (identifier.getString("type").equals("ISBN_10")) {
                    return identifier.getString("identifier");
                }
            }
        }
        return "No ISBN available"; 
    }


    /**
     * Extracts the authors of the book from the JSON object and formats them as a comma-separated string.
     * If no authors are found, returns a default message.
     *
     * @param bookInfo the JSON object containing book details.
     * @return a comma-separated list of authors or a default message if unavailable.
     */
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

    /**
     * Parses the publication date of the book from the JSON object.
     * If the date is available, formats it as a {@link Date} object.
     * If parsing fails or no date is found, returns {@code null}.
     *
     * @param bookInfo the JSON object containing book details.
     * @return the publication date as a {@link Date} object or {@code null} if unavailable.
     */
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

    /**
     * Extracts the URL of the book's thumbnail image from the JSON object.
     * If the "imageLinks" field is present, retrieves the "thumbnail" URL.
     * If the "thumbnail" URL is not available or the "imageLinks" field is absent,
     * returns a default placeholder image ("image.png").
     *
     * @param bookInfo the JSON object containing book details.
     * @return the URL of the thumbnail image, or "image.png" as the default placeholder.
     */
    private static String getImageUrl(JSONObject bookInfo) {
        if (bookInfo.has("imageLinks")) {
            JSONObject imageLinks = bookInfo.getJSONObject("imageLinks");
            return imageLinks.optString("thumbnail", "image.png");
        }
        return "image.png";
    }

    public static void main(String[] args) {
//        String isbn = "0747532699";
//        BookItem book = getBookDetailsByISBN(isbn);
//        if (book != null) {
//            book.displayInfo();
//        }

        System.out.println("-------------------------------------------");

        List<BookItem> books = getBookDetailsByAuthor("j k rowling");
        System.out.println(books.size());
        for (BookItem book1 : books) {
            book1.displayInfo();
        }

        System.out.println("-------------------------------------------");

    }
}

