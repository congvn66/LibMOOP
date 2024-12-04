package com.example.proj.Models;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.scene.image.Image;
import java.io.ByteArrayOutputStream;


import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Singleton service class responsible for generating QR codes for book items.
 * <p>
 * The class provides a method to generate a QR code based on book information, including
 * title, author, ISBN, price, language, and number of pages.
 * </p>
 */
public class QRCodeService {
    private static QRCodeService qrCodeService;

    private QRCodeService() {

    }

    /**
     * Returns the singleton instance of the {@link QRCodeService} class.
     * <p>
     * This method ensures that only one instance of the service is created (Singleton pattern).
     * </p>
     *
     * @return the singleton instance of {@link QRCodeService}.
     */
    public static synchronized QRCodeService getInstance() {
        if (qrCodeService == null) {
            qrCodeService = new QRCodeService();
        }
        return qrCodeService;
    }


    /**
     * Generates a QR code for a given book item.
     * <p>
     * This method creates a QR code containing details about the book such as title,
     * author, ISBN, price, language, and number of pages, then converts it to an image.
     * </p>
     *
     * @param book the {@link BookItem} for which the QR code is to be generated.
     * @param width the desired width of the generated QR code image.
     * @param height the desired height of the generated QR code image.
     * @return an {@link Image} representing the generated QR code.
     * @throws IOException if an I/O error occurs while generating the QR code image.
     * @throws WriterException if an error occurs during QR code generation.
     */
    public Image generateQRCode(BookItem book, int width, int height) throws IOException, WriterException {
        String qrContent = String.format("Title: %s\nAuthor: %s (%s)\nISBN: %s\nPrice: %s $\nLanguage: %s\nNumber of pages: %s\n",
                book.getTitle(),
                book.getAuthor().getName(),
                book.getAuthor().getDescription(),
                book.getISBN(),
                book.getPrice(),
                book.getLanguage(),
                book.getNumberOfPage()
                );

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        byte[] qrCodeBytes = outputStream.toByteArray();

        return new Image(new ByteArrayInputStream(qrCodeBytes));
    }

}