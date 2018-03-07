package ru.alex.bookStore.entities;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
@Transactional
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", unique = true, nullable = false)
    private Long bookId;

    @Column(nullable = false)
    private String bookTitle = "";

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> authors;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "books_categories",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "book_category_id", referencedColumnName= "book_category_id"))
    private Set<BookCategory> categories = new HashSet<>();
    private Integer numberOfPages = 0;
    private Short year = 0;
    private String publishingHouse = "";
    private BigDecimal price;
    private Integer numberOfCopies = 0;
    @OneToOne(fetch = FetchType.LAZY)
    private Cover pictureOfBookCover;

    public Book() {
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public Set<String> getAuthors() {
        return Collections.unmodifiableSet(authors);
    }

    public Set<BookCategory> getCategories() {
        return Collections.unmodifiableSet(categories);
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
        return pictureOfBookCover.getPictureOfBookCover();
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setAuthors(Set<String> authors) {
        this.authors = authors;
    }

    public void addAuthor(String author) {
        this.authors.add(author);
    }

    public void addCategory(String category) {
        BookCategory bookCategory = new BookCategory();
        bookCategory.setCategory(category);
        this.categories.add(bookCategory);
    }

    public void setCategories(Set<BookCategory> categories) {
        this.categories = categories;
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
        this.pictureOfBookCover.setPictureOfBookCover(pictureOfBookCover);
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
