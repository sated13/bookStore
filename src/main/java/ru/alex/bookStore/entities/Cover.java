package ru.alex.bookStore.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

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
    @Getter
    private String fileName = "";

    @Transient
    @Getter
    @Setter
    private byte[] pictureOfBookCover = new byte[0];

    @Getter
    @Setter
    private boolean isPresented = false;

    public void setFilename(Long bookId) {
        this.fileName = "book_" + bookId.toString() + ".jpg";
    }

}
