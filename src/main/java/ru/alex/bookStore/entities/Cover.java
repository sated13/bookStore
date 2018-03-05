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
    private Byte[] pictureOfBookCover;

    public Cover() { }

    public Byte[] getPictureOfBookCover() {
        return pictureOfBookCover;
    }

    public void setPictureOfBookCover(Byte[] pictureOfBookCover) {
        this.pictureOfBookCover = pictureOfBookCover;
    }
}
