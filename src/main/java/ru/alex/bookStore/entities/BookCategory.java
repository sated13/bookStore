package ru.alex.bookStore.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "book_categories")
public class BookCategory implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookCategoryId", unique = true, nullable = false)
    private Long bookCategoryId;
    private String category = "";

    public BookCategory() { }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
