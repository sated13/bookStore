package ru.alex.bookStore.utils.book;

import ru.alex.bookStore.entities.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BookService {

    boolean save(Map<String, Object> bookParameters);

    boolean delete(Book book);

    int delete(Set<Book> books);

    long countBooks();

    List<Book> findBooksByBookTitle(String bookTitle);

    List<Book> getAllBooks();

    List<String> getAllStringBooks();

    Set<BookCategory> getBookCategories(Book book);

    Cover getBookCover(Book book);

    boolean changeBookDetails(Book book, Map<String, Object> bookParameters);

    Set<Book> findBooksByCategoriesContains(BookCategory category);

    Map<BookCategory, Set<Book>> getBooksByCategories();

    Map<BookCategory, Integer> getCountOfBooksByCategories();

    Integer countOfBooksWithCategory(BookCategory category);

    int addCategoryToBooks(BookCategory category, Set<Book> books);

    boolean addCoverToBook(Book book, byte[] pictureBytes);

    /**
     * Set category only on books from Set
     * @param category  role for users
     * @param books only books from this set should have this category
     */
    int setCategoryOnBooks(BookCategory category, Set<Book> books);

    Set<Book> findTop10BooksOrderedByAddingDay();

    Set<Book> findTop10BooksOrderedByCountOfSoldItems();
}
