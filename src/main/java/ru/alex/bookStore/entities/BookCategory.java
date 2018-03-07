package ru.alex.bookStore.entities;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "book_categories")
@Transactional
public class BookCategory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_category_id", unique = true, nullable = false)
    private Long bookCategoryId;

    @Column(nullable = false)
    private String category = "";

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "categories")
    private Set<Book> books;

    public BookCategory() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return category;
    }
}
