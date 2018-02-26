package ru.alex.bookStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alex.bookStore.entities.BookCategory;

public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {

    BookCategory findBookCategoryByCategory(String category);
}
