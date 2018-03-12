package ru.alex.bookStore.utils.bookCategory;

import ru.alex.bookStore.entities.Book;
import ru.alex.bookStore.entities.BookCategory;

import java.util.List;
import java.util.Set;

public interface BookCategoryService {

    boolean save(String category);

    boolean delete(String category);

    int delete(Set<String> users);

    BookCategory findByCategory(String category);

    Set<BookCategory> findByCategories(Set<String> categories);

    List<BookCategory> getAllCategories();

    List<String> getAllStringCategories();

    Set<Book> getBooksByCategory(String category);
}
