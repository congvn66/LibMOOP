package com.example.proj.Models;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;


import org.json.JSONObject;
import org.json.JSONArray;

public class OpenLibraryApi {

    /**
     * Tìm kiếm sách theo tác giả từ Open Library API và trả về danh sách các sách được tìm thấy.
     *
     * @param author Tên tác giả cần tìm kiếm.
     * @return Danh sách các {@link BookItem} chứa thông tin sách tìm được.
     */
    public static List<BookItem> searchBooksByAuthor(String author) {
        long startTime = System.currentTimeMillis();
        List<BookItem> bookItems = new ArrayList<>();

        try {
            // Xây dựng URL truy vấn đến Open Library API
            String queryUrl = "https://openlibrary.org/search.json?author=" + author.replace(" ", "+") + "&sort=new"; // URL encode the title
            HttpURLConnection connection = (HttpURLConnection) new URL(queryUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(5000); // Thời gian kết nối tối đa là 5 giây
            connection.setReadTimeout(5000); // Thời gian đọc dữ liệu tối đa là 5 giây

            // Đọc phản hồi từ API
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line); // Kết hợp các dòng trả về thành một chuỗi duy nhất
            }
            reader.close(); // Đóng BufferedReader


            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray docs = jsonResponse.getJSONArray("docs");



            for (int i = 0; i < Math.min(10, docs.length()); i++) {
                JSONObject doc = docs.getJSONObject(i);

                // Trích xuất các thông tin cần thiết từ JSON response
                String isbn = doc.optJSONArray("isbn") != null ? doc.getJSONArray("isbn").optString(0, "No ISBN") : "No ISBN";
                String language = doc.optJSONArray("language") != null ? doc.getJSONArray("language").optString(0, "No language") : "No language";
                String titleText = doc.optString("title", "No title");
                String authorName = doc.optJSONArray("author_name") != null ? doc.getJSONArray("author_name").optString(0, "Unknown author") : "Unknown author";
                String numberOfPages = doc.optString("number_of_pages_median", "No number of pages");
                String publisher = doc.optJSONArray("publisher") != null ? doc.getJSONArray("publisher").optString(0, "No publisher") : "No publisher";
                String subject = doc.optJSONArray("subject") != null ? doc.getJSONArray("subject").optString(0, "No subject") : "No subject";
                Date dateOfPurchase = new Date();
                Date publicationDate = null;
                int number = 1;
                String location = "Unknown";
                String imgName = getImageUrl(doc);

                // Tạo một đối tượng BookItem và thêm vào danh sách kết quả
                BookItem bookItem = new BookItem(isbn, titleText, subject, publisher, language, numberOfPages, authorName,
                        "no description", null, false, 1000, BookFormat.EBOOK, BookStatus.AVAILABLE, dateOfPurchase,
                        publicationDate, number, location, imgName);
                bookItems.add(bookItem);
            }
        } catch (Exception e) {
            // Xử lý ngoại lệ nếu có lỗi xảy ra trong quá trình kết nối hoặc phân tích JSON
            System.out.println(e.getMessage());
        }

        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;

        System.out.println("Time elapsed " + duration + " ms");

        return bookItems;
    }

    /**
     * Tìm kiếm sách theo tiêu đề từ Open Library API và trả về danh sách các sách được tìm thấy.
     *
     * @param title Tiêu đề của sách cần tìm kiếm.
     * @return Danh sách các {@link BookItem} chứa thông tin sách tìm được.
     */
    public static List<BookItem> searchBooksByTitle(String title) {
        long startTime = System.currentTimeMillis(); // Ghi lại thời gian bắt đầu tìm kiếm
        List<BookItem> bookItems = new ArrayList<>(); // Danh sách chứa kết quả sách

        try {
            // Xây dựng URL truy vấn đến Open Library API
            String queryUrl = "https://openlibrary.org/search.json?title=" + title.replace(" ", "+");

            // Thiết lập kết nối HTTP đến Open Library API
            HttpURLConnection connection = (HttpURLConnection) new URL(queryUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(5000); // Thời gian kết nối tối đa là 5 giây
            connection.setReadTimeout(5000);    // Thời gian đọc dữ liệu tối đa là 5 giây

            // Đọc phản hồi từ API
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line); // Kết hợp các dòng trả về thành một chuỗi duy nhất
            }
            reader.close(); // Đóng BufferedReader

            // Phân tích cú pháp phản hồi JSON
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray docs = jsonResponse.getJSONArray("docs");

            // Lặp qua các sách được trả về, lấy tối đa 10 cuốn sách
            for (int i = 0; i < Math.min(10, docs.length()); i++) {
                JSONObject doc = docs.getJSONObject(i);

                // Trích xuất các thông tin cần thiết từ JSON response
                String isbn = doc.optJSONArray("isbn") != null ? doc.getJSONArray("isbn").optString(0, "No ISBN") : "No ISBN";
                String language = doc.optJSONArray("language") != null ? doc.getJSONArray("language").optString(0, "No language") : "No language";
                String titleText = doc.optString("title", "No title");
                String authorName = doc.optJSONArray("author_name") != null ? doc.getJSONArray("author_name").optString(0, "Unknown author") : "Unknown author";
                String numberOfPages = doc.optString("number_of_pages_median", "No number of pages");
                String publisher = doc.optJSONArray("publisher") != null ? doc.getJSONArray("publisher").optString(0, "No publisher") : "No publisher";
                String subject = doc.optJSONArray("subject") != null ? doc.getJSONArray("subject").optString(0, "No subject") : "No subject";
                Date dateOfPurchase = new Date(); // Lấy ngày hiện tại làm ngày mua
                Date publicationDate = null;  // Không có thông tin về ngày xuất bản
                int number = 1;               // Giả định rằng mỗi sách có một bản sao
                String location = "Unknown";  // Không có thông tin về vị trí
                String imgName = getImageUrl(doc); // Lấy đường dẫn ảnh từ hàm getImageUrl()

                // Tạo một đối tượng BookItem và thêm vào danh sách kết quả
                BookItem bookItem = new BookItem(isbn, titleText, subject, publisher, language, numberOfPages, authorName,
                        "no description", null, false, 1000, BookFormat.EBOOK, BookStatus.AVAILABLE, dateOfPurchase,
                        publicationDate, number, location, imgName);
                bookItems.add(bookItem);
            }
        } catch (Exception e) {
            // Xử lý ngoại lệ nếu có lỗi xảy ra trong quá trình kết nối hoặc phân tích JSON
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis(); // Ghi lại thời gian kết thúc tìm kiếm

        long duration = endTime - startTime; // Tính toán thời gian thực hiện tìm kiếm

        // In ra thời gian thực hiện
        System.out.println("Time elapsed " + duration + " ms");

        return bookItems; // Trả về danh sách các sách tìm được
    }

    /**
     * Lấy thông tin chi tiết của sách dựa trên ISBN từ OpenLibrary API.
     *
     * @param isbn ISBN của cuốn sách cần tìm thông tin.
     * @return một đối tượng {@link BookItem} chứa thông tin chi tiết của cuốn sách nếu tìm thấy, ngược lại trả về {@code null}.
     */
    public static BookItem searchBookByIsbn(String isbn) {
        String apiUrl = "https://openlibrary.org/api/books?bibkeys=ISBN:" + isbn + "&format=json&jscmd=data";

        try {
            // Gửi yêu cầu GET
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Đọc phản hồi
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Phân tích phản hồi JSON
                JSONObject jsonResponse = new JSONObject(response.toString());
                String key = "ISBN:" + isbn;
                if (jsonResponse.has(key)) {
                    JSONObject bookInfo = jsonResponse.getJSONObject(key);

                    // Trích xuất thông tin sách
                    String title = bookInfo.optString("title", "Unknown");
                    String authors = getAuthorsName(bookInfo);
                    String publisher = getPublishers(bookInfo);
                    String language = "Unknown";
                    String numberOfPages = bookInfo.optString("number_of_pages", "Unknown");
                    String subject = getSubject(bookInfo);
                    String authorDescription = getAuthorsDescription(bookInfo);
                    String id = null;
                    boolean isReferenceOnly = false;
                    double price = 0.0;
                    BookFormat format = BookFormat.PAPERBACK;
                    BookStatus status = BookStatus.AVAILABLE;
                    Date dateOfPurchase = new Date();
                    Date publicationDate = null; // OpenLibrary không cung cấp định dạng ngày xuất bản chính xác
                    int number = 1;
                    String location = "Unknown";
                    String imgName = getImageUrl(bookInfo);

                    // Tạo đối tượng BookItem
                    return new BookItem(isbn, title, subject, publisher, language, numberOfPages, authors,
                            authorDescription, id, isReferenceOnly, price, format, status, dateOfPurchase,
                            publicationDate, number, location, imgName);
                } else {
                    System.out.println("No books found.");
                }
            } else {
                System.out.println("Error: Can't get data, HTTP code: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    /**
     * Trích xuất thông tin tác giả từ phản hồi của OpenLibrary API.
     *
     * @param bookInfo đối tượng JSON chứa thông tin chi tiết của sách.
     * @return danh sách tác giả cách nhau bằng dấu phẩy hoặc thông điệp mặc định nếu không có thông tin.
     */
    private static String getAuthorsName(JSONObject bookInfo) {
        if (bookInfo.has("authors")) {
            StringBuilder authors = new StringBuilder();
            for (Object authorObj : bookInfo.getJSONArray("authors")) {
                JSONObject author = (JSONObject) authorObj;
                String name = author.optString("name", "Unknown author");
                authors.append(name).append(", ");
            }
            if (authors.length() > 0) {
                authors.setLength(authors.length() - 2);
                return authors.toString();
            }
            return "No author available";
        }
        return "No author available";
    }


    /**
     * Extracts authors' description from the OpenLibrary API response.
     *
     * @param bookInfo the JSON object containing book details.
     * @return a comma-separated list of authors or a default message if unavailable.
     */
    private static String getAuthorsDescription(JSONObject bookInfo) {
        if (bookInfo.has("authors")) {
            StringBuilder authors = new StringBuilder();
            for (Object authorObj : bookInfo.getJSONArray("authors")) {
                JSONObject author = (JSONObject) authorObj;
                String url = author.optString("url", "No description available");
                authors.append(url).append(", ");
            }
            if (authors.length() > 0) {
                authors.setLength(authors.length() - 2);
                return authors.toString();
            }
            return "No description available";
        }
        return "No description available";
    }



    /**
     * Extracts publishers from the OpenLibrary API response.
     *
     * @param bookInfo the JSON object containing book details.
     * @return a comma-separated list of publishers or a default message if unavailable.
     */
    private static String getPublishers(JSONObject bookInfo) {
        if (bookInfo.has("publishers")) {
            StringBuilder publishers = new StringBuilder();
            for (Object publisherObj : bookInfo.getJSONArray("publishers")) {
                JSONObject publisher = (JSONObject) publisherObj;
                publishers.append(publisher.optString("name", "Unknown publisher")).append(", ");
            }
            return publishers.length() > 0 ? publishers.substring(0, publishers.length() - 2) : "No publisher available";
        }
        return "No publisher available";
    }

    /**
     * Extracts subject from the OpenLibrary API response.
     *
     * @param bookInfo the JSON object containing book details.
     * @return subject name, if no subject, return "No subject available"
     */
    private static String getSubject(JSONObject bookInfo) {
        if (bookInfo.has("subjects")) {
            JSONArray subjectsArray = bookInfo.getJSONArray("subjects");
            if (subjectsArray.length() > 0) {
                JSONObject firstSubject = subjectsArray.getJSONObject(0);
                String subjectName = firstSubject.optString("name", "No subject available");
                return subjectName;
            }
        }
        return "No subject available";
    }




    /**
     * Extracts the image URL of the book's cover from the OpenLibrary API response.
     *
     * @param bookInfo the JSON object containing book details.
     * @return the URL of the book cover image or "image.png" if unavailable.
     */
    private static String getImageUrl(JSONObject bookInfo) {
        if (bookInfo.has("cover")) {
            JSONObject cover = bookInfo.getJSONObject("cover");
            return cover.optString("medium", "image.png");
        }
        return "image.png";
    }


    public static void main(String[] args) {
        String author = "j k rowling";
        List<BookItem> bookItems = searchBooksByAuthor(author);
        System.out.println(bookItems.size());
        for (BookItem i : bookItems) {
            i.displayInfo();
        }


    }
}
