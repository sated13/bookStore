package ru.alex.bookStore.utils.bookCategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.alex.bookStore.entities.BookCategory;
import ru.alex.bookStore.repository.BookCategoryRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class BookCategoryServiceImpl implements BookCategoryService{

    @Autowired
    BookCategoryRepository bookCategoryRepository;

    BookCategoryServiceImpl() {}

    public boolean save(String category) {
        BookCategory userRole = new BookCategory();
        userRole.setCategory(category);
        try {
            bookCategoryRepository.save(userRole);
            return true;
        }
        catch (Exception e) {
            //ToDo: add logging
            return false;
        }
    }

    public boolean delete(String category) {
        BookCategory userRole = findByCategory(category);
        try {
            bookCategoryRepository.delete(userRole);
            return true;
        }
        catch (Exception e) {
            //ToDo: add logging
            return false;
        }
    }

    public boolean delete(BookCategory category) {
        try {
            bookCategoryRepository.delete(category);
            return true;
        }
        catch (Exception e) {
            //ToDo: add logging
            return false;
        }
    }

    public int delete(Set<String> categories) {
        int result = 0;

        for (String category: categories) {
            result += (delete(category)) ? 1 : 0;
        }

        return result;
    }

    public BookCategory findByCategory(String category) {
        BookCategory userRole = null;
        try {
            userRole = bookCategoryRepository.findBookCategoryByCategory(category);
        }
        catch (Exception e) {
            //ToDo: add logging
        }
        return userRole;
    }

    public List<BookCategory> getAllCategories() {
        return Collections.unmodifiableList(bookCategoryRepository.findAll(new Sort(Sort.Direction.ASC, "category")));
    }

    public List<String> getAllStringCategories() {
        List<BookCategory> allCategories = getAllCategories();
        List<String> allStringCategories = new ArrayList<>();
        for(BookCategory category: allCategories) {
            allStringCategories.add(category.getCategory());
        }
        return allStringCategories;
    }
}
