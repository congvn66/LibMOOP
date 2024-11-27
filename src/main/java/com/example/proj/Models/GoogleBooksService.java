package com.example.proj.Models;

import com.example.proj.Models.BookItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GoogleBooksService {

    private static final String API_URL = "https://www.googleapis.com/books/v1/volumes";
    private static final String IMAGE_FOLDER = "src/main/resources/asset/book/";

    public List<BookItem> searchBooks(String query) {
        List<BookItem> bookItems = new ArrayList<>();
        try {
            // Tạo URL truy vấn
            String url = API_URL + "?q=" + query.replace(" ", "+");

            // Gửi request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Xử lý JSON từ response
            if (response.statusCode() == HttpURLConnection.HTTP_OK) {
                JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
                JsonArray items = jsonResponse.getAsJsonArray("items");

                if (items != null) {
                    for (int i = 0; i < items.size(); i++) {
                        JsonObject item = items.get(i).getAsJsonObject();
                        JsonObject volumeInfo = item.getAsJsonObject("volumeInfo");

                        // Lấy ISBN từ mảng industryIdentifiers
                        String isbn = null;
                        if (volumeInfo.has("industryIdentifiers")) {
                            JsonArray industryIdentifiers = volumeInfo.getAsJsonArray("industryIdentifiers");
                            for (int j = 0; j < industryIdentifiers.size(); j++) {
                                JsonObject identifierObj = industryIdentifiers.get(j).getAsJsonObject();
                                if (identifierObj.has("type") && "ISBN_13".equals(identifierObj.get("type").getAsString())) {
                                    isbn = identifierObj.get("identifier").getAsString();
                                    break;
                                }
                            }
                        }


                        String imageUrl = null;
                        if (volumeInfo.has("imageLinks")) {
                            JsonObject imageLinks = volumeInfo.getAsJsonObject("imageLinks");
                            imageUrl = imageLinks.has("thumbnail") ? imageLinks.get("thumbnail").getAsString() : null;
                        }

                        String imageName = "default.png";


                        if (imageUrl != null) {
                            imageName = generateImageNameFromUrl(isbn);

                            // need to be disabled!
                            saveImageFromUrl(imageUrl, imageName);
                        }


                        BookItem bookItem = new BookItem(
                                isbn,
                                volumeInfo.has("title") ? volumeInfo.get("title").getAsString() : null,
                                volumeInfo.has("categories") ? volumeInfo.get("categories").getAsJsonArray().get(0).getAsString() : null,
                                volumeInfo.has("publisher") ? volumeInfo.get("publisher").getAsString() : null,
                                volumeInfo.has("language") ? volumeInfo.get("language").getAsString() : null,
                                volumeInfo.has("pageCount") ? String.valueOf(volumeInfo.get("pageCount").getAsInt()) : null,
                                volumeInfo.has("authors") ? volumeInfo.getAsJsonArray("authors").get(0).getAsString() : null,
                                "super handsome",
                                null,
                                false,
                                1000.0,
                                BookFormat.EBOOK,
                                BookStatus.AVAILABLE,
                                null,
                                null,
                                0,
                                null,
                                imageName
                        );

                        bookItems.add(bookItem);
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return bookItems;
    }

    private String generateImageNameFromUrl(String isbn) {
        return BookItem.generateImageNameFromChosenImage(isbn + ".png");
    }

    private void saveImageFromUrl(String imageUrl, String imageName) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (InputStream in = connection.getInputStream()) {
            Path imagePath = Path.of(IMAGE_FOLDER + imageName);
            Files.createDirectories(imagePath.getParent());
            Files.copy(in, imagePath);
        }
    }

    public static void main(String[] args) {
        GoogleBooksService service = new GoogleBooksService();
        List<BookItem> books = service.searchBooks("Clean Code");
        for (BookItem book : books) {
            book.displayInfo();
        }
    }
}
