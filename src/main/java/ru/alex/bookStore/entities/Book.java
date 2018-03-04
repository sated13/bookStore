package ru.alex.bookStore.entities;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long bookId;

    @Column(nullable = false)
    private String bookTitle = "";

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> authors;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<BookCategory> categories = new ArrayList<>();
    private Integer numberOfPages = 0;
    private Short year = 0;
    private String publishingHouse = "";
    private BigDecimal price;
    private Integer numberOfCopies = 0;
    private Byte[] pictureOfBookCover;

    public Book() {
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public Set<String> getAuthors() {
        return Collections.unmodifiableSet(authors);
    }

    public List<BookCategory> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public Short getYear() {
        return year;
    }

    public String getPublishingHouse() {
        return publishingHouse;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getNumberOfCopies() {
        return numberOfCopies;
    }

    public Byte[] getPictureOfBookCover() {
        return pictureOfBookCover;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void addAuthor(String author) {
        this.authors.add(author);
    }

    public void addCategory(String category) {
        BookCategory bookCategory = new BookCategory();
        bookCategory.setCategory(category);
        this.categories.add(bookCategory);
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public void setYear(Short year) {
        this.year = year;
    }

    public void setPublishingHouse(String publishingHouse) {
        this.publishingHouse = publishingHouse;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setNumberOfCopies(Integer numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }

    public void setPictureOfBookCover(Byte[] pictureOfBookCover) {
        this.pictureOfBookCover = pictureOfBookCover;
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
}
