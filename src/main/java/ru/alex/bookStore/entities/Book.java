package ru.alex.bookStore.entities;

import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "books")
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", unique = true, nullable = false)
    private Long bookId;

    @Column(nullable = false)
    private String bookTitle = "";

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> authors;

    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "books_categories",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "book_category_id", referencedColumnName = "book_category_id"))
    private Set<BookCategory> categories = new HashSet<>();
    private Integer numberOfPages = 0;
    private Short year = 0;
    private String publishingHouse = "";
    private BigDecimal price = new BigDecimal(0);
    private Integer numberOfCopies = 0;
    private Integer countOfSoldItems = 0;
    private LocalDate addingDay;

    @OneToOne(fetch = FetchType.EAGER)
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
        return categories;
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

    public Cover getPictureOfBookCover() {
        return pictureOfBookCover;
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

    public void addCategory(BookCategory category) {
        categories.add(category);
    }

    public void deleteCategory(BookCategory category) {
        categories.remove(category);
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

    public Integer getCountOfSoldItems() {
        return countOfSoldItems;
    }

    public void setCountOfSoldItems(Integer countOfSoldItems) {
        this.countOfSoldItems = countOfSoldItems;
    }

    public LocalDate getAddingDay() {
        return addingDay;
    }

    public void setAddingDay(LocalDate addingDay) {
        this.addingDay = addingDay;
    }

    public void setPictureOfBookCover(Cover pictureOfBookCover) {
        this.pictureOfBookCover = pictureOfBookCover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
