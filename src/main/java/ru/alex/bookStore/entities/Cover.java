package ru.alex.bookStore.entities;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Entity
@Table(name = "covers")
//@Transactional
public class Cover {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cover_id", unique = true, nullable = false)
    private Long coverId;

    @Column(nullable = false)
    @Lob
    private byte[] pictureOfBookCover = new byte[0];

    private boolean isPresented = false;

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

    public boolean isPresented() {
        return isPresented;
    }

    public void setPresented(boolean presented) {
        isPresented = presented;
    }
}
