package com.example.proj.Models;

import java.util.Date;
import java.util.List;

public abstract class Account {
    private String id;
    private String password;
    private static Catalog catalog;
    private AccountStatus status;

    public Account(String id, AccountStatus status, String password) {
        this.id = id;
        this.status = status;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public Catalog getCatalog() {
        return Catalog.getInstance();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    /**
     * Updates a specific field of a book identified by its ID.
     *
     * @param id the unique identifier of the book to be updated
     * @param field the field of the book to update, represented as an integer
     *              (e.g., 1 for title, 2 for author, etc.)
     * @param newValue the new value to set for the specified field
     */
    public void updateBook(String id, int field, String newValue) {
        this.getCatalog().editBookInDataBase(id, field, newValue);
    }

    /**
     * Finds and retrieves a list of book items that match the given title.
     *
     * @param title the title of the books to search for; it can be a partial or full title
     * @return a list of {@code BookItem} objects that match the given title, or an empty list if no books are found
     */
    public List<BookItem> findBooksByTitle(String title) {
        return this.getCatalog().findBooksByTitle(title);
    }

    /**
     * Finds and retrieves a list of book items written by the specified author.
     *
     * @param author the name of the author to search for; it can be a partial or full name
     * @return a list of {@code BookItem} objects written by the specified author, or an empty list if no books are found
     */
    public List<BookItem> findBooksByAuthor(String author) {
        return this.getCatalog().findBooksByAuthor(author);
    }

    /**
     * Finds and retrieves a list of book items that match the specified subject.
     *
     * @param subject the subject of the books to search for; have to be full name of the subject
     * @return a list of {@code BookItem} objects that match the specified subject, or an empty list if no books are found
     */
    public List<BookItem> findBooksBySubject(String subject) {
        return this.getCatalog().findBooksBySubject(subject);
    }

    /**
     * Finds and retrieves a list of book items published on the specified date.
     *
     * @param publicationDate the publication date of the books to search for
     * @return a list of {@code BookItem} objects published on the specified date, or an empty list if no books are found
     */
    public List<BookItem> findBooksByPublicationDate(Date publicationDate) {
        return this.getCatalog().findBooksByPublicationDate(publicationDate);
    }
}
