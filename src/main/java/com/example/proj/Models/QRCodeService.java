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

    public Image generateQRCode(Book book, int width, int height) throws IOException, WriterException {
        String qrContent = String.format("Book: %s\nAuthor: %s\nISBN: %s\nPrice: %s",
                book.getTitle(),
                book.getAuthor(),
                book.getISBN(),
                book.getPrice());

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        byte[] qrCodeBytes = outputStream.toByteArray();

        return new Image(new ByteArrayInputStream(qrCodeBytes));
    }

    public void saveQRCodeToFile(Book book, String filePath) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        String qrContent = String.format("Book: %s\nAuthor: %s\nISBN: %s\nPrice: %s",
                book.getTitle(),
                book.getAuthor(),
                book.getISBN(),
                book.getPrice());

        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300);
        java.nio.file.Path path = java.nio.file.Paths.get(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
}