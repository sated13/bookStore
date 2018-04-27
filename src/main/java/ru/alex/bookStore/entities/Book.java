package ru.alex.bookStore.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "books")
@NoArgsConstructor
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", unique = true, nullable = false)
    private Long bookId;

    @Column(nullable = false)
    @Getter @Setter private String bookTitle = "";

    @ElementCollection(fetch = FetchType.EAGER)
    @Getter @Setter private Set<String> authors;

    @Column(length = 10000)
    @Getter @Setter private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "books_categories",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "book_category_id", referencedColumnName = "book_category_id"))
    @Getter @Setter private Set<BookCategory> categories = new HashSet<>();
    @Getter @Setter private Integer numberOfPages = 0;
    @Getter @Setter private Short year = 0;
    @Getter @Setter private String publishingHouse = "";
    @Getter @Setter private double price = 0.0;
    @Getter @Setter private Integer numberOfCopies = 0;
    @Getter @Setter private Integer countOfSoldItems = 0;
    @Getter @Setter private LocalDate addingDay;

    @OneToOne(fetch = FetchType.EAGER)
    @Getter @Setter private Cover pictureOfBookCover;

    public Long getID() {
        return bookId;
    }

    public void addAuthor(String author) {
        this.authors.add(author);
    }

    public void addCategory(BookCategory category) {
        categories.add(category);
    }

    public void deleteCategory(BookCategory category) {
        categories.remove(category);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result
                .append(bookTitle)
                .append(" ")
                .append(StringUtils.collectionToCommaDelimitedString(authors));
        return result.toString();
    }

    @Override
    public int hashCode() {
        return (toString()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) return false;
        if (this == obj) return true;
        if (!(obj instanceof Book)) return false;

        Book book = (Book) obj;
        return bookId.equals(book.bookId);
    }
}
