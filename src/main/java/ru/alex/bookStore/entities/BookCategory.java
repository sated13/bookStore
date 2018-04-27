package ru.alex.bookStore.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "book_categories")
@NoArgsConstructor
public class BookCategory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_category_id", unique = true, nullable = false)
    private Long bookCategoryId;

    @Column(nullable = false)
    @Getter @Setter private String category = "";

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
