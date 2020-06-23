package manager.model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class FileInfo {
    private String filename;
    private long size;
    private LocalDateTime lastModified;
    private String type;
    private ImageView icon;

    public FileInfo(Path path) {
        try {
            Pair<String, String> fileInfo = getFileInfo(path);
            this.filename = fileInfo.getKey();
            this.size = !Files.isDirectory(path) ? Files.size(path) : 0;
            this.type = fileInfo.getValue();
            Icon image = FileSystemView.getFileSystemView().getSystemIcon(path.toFile());
            BufferedImage bufferedImage = new BufferedImage(image.getIconWidth(), image.getIconHeight(), BufferedImage.TYPE_INT_ARGB
            );
            image.paintIcon(null, bufferedImage.getGraphics(), 0, 0);
            Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
            this.icon = new ImageView(fxImage);
            this.lastModified = LocalDateTime.ofInstant(Files.getLastModifiedTime(path).toInstant(), ZoneOffset.ofHours(3));
        } catch (IOException e) {
            throw new RuntimeException("Unable to create file info from path");
        }
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }


    public ImageView getIcon() {
        return icon;
    }

    public void setIcon(ImageView icon) {
        this.icon = icon;
    }

    private Pair<String, String> getFileInfo(Path path) {
        String fileName = path.getFileName().toString();
        if (Files.isDirectory(path)) {
            return new Pair<>(fileName, "[DIR]");
        } else {
            int dot = fileName.lastIndexOf('.');
            if (dot == 0 && fileName.length() > 1) {
                return new Pair<>(fileName, "[EMPTY]");
            } else {
                String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
                return new Pair<>(tokens[0], tokens.length > 1 ? tokens[1] : "[EMPTY]");
            }
        }
    }

}