package ru.alex.bookStore.entities;

import javax.imageio.ImageIO;
import javax.persistence.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Entity
@Table(name = "covers")
public class Cover {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cover_id", unique = true, nullable = false)
    private Long coverId;

    @Column(nullable = false)
    private String file_name = "";

    @Transient
    private byte[] pictureOfBookCover = new byte[0];

    private boolean isPresented = false;

    public Cover() {
    }

    public Cover(byte[] pictureOfBookCover) {
        setPictureOfBookCover(pictureOfBookCover);
    }

    public void setFilename(Long bookId) {
        this.file_name = "book_" + bookId.toString() + ".png";
    }

    public byte[] getPictureOfBookCover() {
        if (pictureOfBookCover.length == 0) {
            try {
                String location = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
                BufferedImage cover = ImageIO.read(new File(location + "books_covers" + File.separator + file_name));
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(cover, "png", byteArrayOutputStream);
                pictureOfBookCover = byteArrayOutputStream.toByteArray();
            } catch (IOException e) {
                //ToDo: add logging
                e.printStackTrace();
            }
        }
        return pictureOfBookCover;
    }

    public void setPictureOfBookCover(byte[] pictureOfBookCover) {
        this.pictureOfBookCover = pictureOfBookCover;
        if (pictureOfBookCover.length != 0) {
            try {
                String location = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pictureOfBookCover);
                BufferedImage cover = ImageIO.read(byteArrayInputStream);
                ImageIO.write(cover, "png", new File(location + "books_covers" + File.separator + file_name));
            } catch (IOException e) {
                //ToDo: add logging
                e.printStackTrace();
            }
        }
    }

    public boolean isPresented() {
        return isPresented;
    }

    public void setPresented(boolean presented) {
        isPresented = presented;
    }

    public String getFilename() {
        return file_name;
    }
}
