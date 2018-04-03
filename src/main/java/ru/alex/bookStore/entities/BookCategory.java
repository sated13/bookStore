package ru.alex.bookStore.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "book_categories")
public class BookCategory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_category_id", unique = true, nullable = false)
    private Long bookCategoryId;

    @Column(nullable = false)
    private String category = "";

    public BookCategory() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return category;
    }

    @Override
    public int hashCode() {
        return category.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) return false;
        if (this == obj) return true;
        if (!(obj instanceof BookCategory)) return false;

        BookCategory bookCategory = (BookCategory) obj;
        return bookCategoryId.equals(bookCategory.bookCategoryId);
    }
}
