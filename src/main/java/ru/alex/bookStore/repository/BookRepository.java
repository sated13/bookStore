package ru.alex.bookStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alex.bookStore.entities.Book;

import java.util.List;
import java.util.Set;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findBooksByBookTitle(String bookTitle);

    /*List<Book> findBooksByAuthorsContains(String author);

    List<Book> findBooksByBookTitleContains(String stringInBookTitle);

    List<Book> findBooksByAuthorsAndBookTitleAnd(Set<String> authors, String bookTitle);*/
}
