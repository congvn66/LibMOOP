package com.example.proj.Models;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


public class ImageImportService {

    /**
     * Opens a file chooser dialog to import an image file.
     * The user can select an image file (PNG, JPG, JPEG) from the file system.
     * The dialog starts from the '/asset/book/' directory.
     *
     * @param stage the JavaFX Stage that owns the file chooser dialog.
     * @return the selected image file, or {@code null} if no file is selected.
     */
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

    /**
     * Checks if the selected image can be moved to the '/asset/book/' folder.
     * If the file already exists in the target folder, it returns false.
     * If the file does not exist or the source file is not the same as the target file, it returns true.
     *
     * @param selectedFile the image file to be moved.
     * @return {@code true} if the image can be moved to the target folder, {@code false} otherwise.
     */
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

    /**
     * Moves the selected image to the '/asset/book/' folder.
     * If the file already exists, it will be replaced.
     *
     * @param selectedFile the image file to be moved.
     */
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
