package ru.alex.bookStore.utils.book;

import ru.alex.bookStore.entities.Book;
import ru.alex.bookStore.entities.BookCategory;
import ru.alex.bookStore.entities.Cover;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BookService {

    Book save(Map<String, Object> bookParameters);

    boolean delete(Book book);

    int delete(Set<Book> books);

    List<Book> findBooksByBookTitle(String bookTitle);

    List<Book> getAllBooks();

    List<String> getAllStringBooks();

    Set<BookCategory> getBookCategories(Book book);

    Cover getBookCover(Book book);

    boolean changeBookDetails(Book book, Map<String, Object> bookParameters);
}
