package ru.alex.bookStore.utils.bookCategory;

import ru.alex.bookStore.entities.BookCategory;

import java.util.List;
import java.util.Set;

public interface CategoryService {

    boolean save(String category);

    boolean delete(String category);

    int delete(Set<String> categories);

    long countCategories();

    BookCategory findByCategory(String category);

    Set<BookCategory> findByCategories(Set<String> categories);

    List<BookCategory> getAllCategories();

    List<String> getAllStringCategories();

    /*Set<Book> getBooksByCategory(String category);*/

    boolean changeCategoryDetails(BookCategory category, String newCategoryName/*, Set<Book> books*/);
}
