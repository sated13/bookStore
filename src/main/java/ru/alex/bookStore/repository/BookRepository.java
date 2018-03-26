package ru.alex.bookStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.alex.bookStore.entities.Book;
import ru.alex.bookStore.entities.BookCategory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findBooksByBookTitle(String bookTitle);

    Set<Book> findBooksByCategoriesContains(BookCategory category);

    Integer countBooksByCategoriesContains(BookCategory category);

    Set<Book> findTop10ByOrderByAddingDayDesc();

    Set<Book> findTop10ByOrderByCountOfSoldItemsDesc();
}
