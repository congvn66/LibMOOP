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

public class QRCodeService {
    private static QRCodeService qrCodeService;
    private QRCodeService() {

    }
    public static synchronized QRCodeService getInstance() {
        if (qrCodeService == null) {
            qrCodeService = new QRCodeService();
        }
        return qrCodeService;
    }

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