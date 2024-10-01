package com.example.proj;

import java.util.Date;
import java.util.List;

public class SystemTestDrive {
    public static void main(String[] args) {
        // Tạo một thư viện
        Library library = new Library("City Library");

        // Tạo một số BookItem với các thông tin đầy đủ
        BookItem book1 = new BookItem(
                "978-3-16-148410-0", // ISBN
                "The Great Gatsby", // title
                "Fiction", // subject
                "Scribner", // publisher
                "English", // language
                "180", // numberOfPage
                "F. Scott Fitzgerald", // authorName
                "An American novelist and short story writer.", // authorDescription
                "1", // barcode
                false, // isReferenceOnly
                10.99, // price
                BookFormat.HARDCOVER, // format (giả sử có enum BookFormat)
                BookStatus.AVAILABLE, // status (giả sử có enum BookStatus)
                new Date(), // dateOfPurchase
                new Date(), // publicationDate
                1, // number (số bản sao)
                "A1" // location (vị trí)
        );

        BookItem book2 = new BookItem(
                "978-0-7432-7356-5",
                "To Kill a Mockingbird",
                "Fiction",
                "J.B. Lippincott & Co.",
                "English",
                "281",
                "Harper Lee",
                "An American novelist, widely known for her novel.",
                "2",
                false,
                7.99,
                BookFormat.PAPERBACK,
                BookStatus.AVAILABLE,
                new Date(),
                new Date(),
                1,
                "B1"
        );

        BookItem book3 = new BookItem(
                "978-0-452-28423-4",
                "1984",
                "Dystopian",
                "Secker & Warburg",
                "English",
                "328",
                "George Orwell",
                "An English novelist and journalist.",
                "3",
                false,
                8.99,
                BookFormat.EBOOK,
                BookStatus.AVAILABLE,
                new Date(),
                new Date(),
                1,
                "C1"
        );

        BookItem book4 = new BookItem(
                "978-0-06-112008-4",
                "The Catcher in the Rye",
                "Fiction",
                "Little, Brown and Company",
                "English",
                "214",
                "J.D. Salinger",
                "An American writer known for his novel.",
                "4",
                false,
                6.99,
                BookFormat.HARDCOVER,
                BookStatus.AVAILABLE,
                new Date(),
                new Date(),
                1,
                "D1"
        );

        // Thêm BookItem vào thư viện
        library.addBookItem(book1);
        library.addBookItem(book2);
        library.addBookItem(book3);
        library.addBookItem(book4);

        // Hiển thị thông tin thư viện
        library.displayLibraryInfo();

        // Tìm kiếm theo tiêu đề
        //System.out.println("\nSearching for books with title '1984':");
        List<BookItem> foundBooksByTitle = library.findBooksByTitle("rYe");
        for (BookItem book : foundBooksByTitle) {
            book.displayInfo();
        }

    }
}
