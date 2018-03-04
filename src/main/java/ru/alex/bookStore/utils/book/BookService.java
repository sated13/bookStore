package ru.alex.bookStore.utils.book;

import ru.alex.bookStore.entities.Book;

import java.util.List;
import java.util.Set;

public interface BookService {

    //boolean save(Book book);

    //boolean delete(Book book);

    //int delete(Set<String> books);

    List<Book> findBooksByBookTitle(String bookTitle);

    List<Book> getAllBooks();

    List<String> getAllStringBooks();

}
