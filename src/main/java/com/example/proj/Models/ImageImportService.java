package com.example.proj.Models;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Stream;

public class ImageImportService {
    public File importImage(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Image");
        try {
            fileChooser.setInitialDirectory(new File(getClass().getResource("/asset/book/").toURI()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(stage);
            return selectedFile;
    }
    public boolean movableImageToFolder(File selectedFile) {
        Path targetFolderPath = null;
        try {
            targetFolderPath = Path.of(getClass().getResource("/asset/book/").toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        Path targetPath = targetFolderPath.resolve(selectedFile.getName());
        if (Files.exists(targetPath) && !targetPath.equals(selectedFile.toPath())) {
                return false;
        }
        return true;
    }
    public void moveImageToFolder(File selectedFile) {
        try {
            Path targetPath = Path.of(getClass().getResource("/asset/book/").toURI());
            try {
                Files.move(selectedFile.toPath(), targetPath.resolve(selectedFile.getName()), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (URISyntaxException e) {
            System.out.println("can not move Image");
        }
    }
}
