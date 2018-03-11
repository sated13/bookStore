package ru.alex.bookStore.entities;

import javax.persistence.*;

@Entity
@Table(name = "covers")
public class Cover {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long bookId;

    @Column(nullable = false)
    private byte[] pictureOfBookCover;

    public Cover() { }

    public Cover(byte[] pictureOfBookCover) {
        this.pictureOfBookCover = pictureOfBookCover;
    }

    public byte[] getPictureOfBookCover() {
        return pictureOfBookCover;
    }

    public void setPictureOfBookCover(byte[] pictureOfBookCover) {
        this.pictureOfBookCover = pictureOfBookCover;
    }
}
