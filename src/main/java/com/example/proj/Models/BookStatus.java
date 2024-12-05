package com.example.proj.Models;

/**
 * Enum representing the different statuses a book can have in the library system.
 * This enum defines the possible states of a book, such as whether it is available, reserved,
 * loaned out, or lost.
 */
public enum BookStatus {
    /**
     * The book is available for checkout in the library.
     */
    AVAILABLE,

    /**
     * The book has been reserved by a member and is not available for checkout.
     */
    RESERVED,

    /**
     * The book has been loaned out to a member and is not currently available.
     */
    LOANED,

    /**
     * The book is lost and not available for circulation.
     */
    LOST
}
