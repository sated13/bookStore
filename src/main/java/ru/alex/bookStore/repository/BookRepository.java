package ru.alex.bookStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alex.bookStore.entities.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    Book findBookByBookTitle(String bookTitle);
}
