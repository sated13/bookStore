package ru.alex.bookStore.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.persistence.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Entity
@Table(name = "covers")
@NoArgsConstructor
@Slf4j
public class Cover {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cover_id", unique = true, nullable = false)
    private Long coverId;

    @Column(nullable = false)
    @Getter private String fileName = "";

    @Transient
    private byte[] pictureOfBookCover = new byte[0];

    @Getter @Setter private boolean isPresented = false;

    public Cover(byte[] pictureOfBookCover) {
        setPictureOfBookCover(pictureOfBookCover);
    }

    public void setFilename(Long bookId) {
        this.fileName = "book_" + bookId.toString() + ".jpg";
    }

    public byte[] getPictureOfBookCover() {
        if (pictureOfBookCover.length == 0) {
            try {
                String location = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
                BufferedImage cover = ImageIO.read(new File(location + "books_covers" + File.separator + fileName));
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(cover, "jpg", byteArrayOutputStream);
                pictureOfBookCover = byteArrayOutputStream.toByteArray();
            } catch (IOException e) {
                log.error("Error during getting book cover", e);
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
                ImageIO.write(cover, "jpg", new File(location + "books_covers" + File.separator + fileName));
            } catch (IOException e) {
                log.error("Error during saving book cover", e);
            }
        }
    }
}
