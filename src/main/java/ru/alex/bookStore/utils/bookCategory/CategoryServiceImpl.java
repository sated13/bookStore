package ru.alex.bookStore.utils.bookCategory;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.bookStore.entities.BookCategory;
import ru.alex.bookStore.repository.CategoryRepository;

import java.util.*;

@Service
@Transactional
@NoArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public boolean save(String category) {
        try {
            if (null == findByCategory(category)) {
                BookCategory userRole = new BookCategory();
                userRole.setCategory(category);
                categoryRepository.save(userRole);
                return true;
            } else return false;
        } catch (Exception e) {
            log.info("Error during saving category {}: {}", category, e.getMessage());
            log.debug("Error during saving category {}: {}", category, e);
            return false;
        }
    }

    public boolean delete(String category) {
        BookCategory bookCategory = findByCategory(category);
        return deleteCategory(bookCategory);
    }

    public boolean delete(BookCategory category) {
        return deleteCategory(category);
    }

    private boolean deleteCategory(BookCategory category) {
        try {
            categoryRepository.delete(category);
            return true;
        } catch (Exception e) {
            log.info("Error during deleting category {}: {}", category, e.getMessage());
            log.debug("Error during deleting category {}: {}", category, e);
            return false;
        }
    }

    public int delete(Set<String> categories) {
        int result = 0;

        for (String category : categories) {
            result += (delete(category)) ? 1 : 0;
        }

        return result;
    }

    @Override
    public long countCategories() {
        try {
            return categoryRepository.count();
        } catch (Exception e) {
            log.info("Error during counting categories: {}", e.getMessage());
            log.debug("Error during counting categories: {}", e);
            return 0;
        }
    }

    public BookCategory findByCategory(String category) {
        BookCategory bookCategory = null;
        try {
            bookCategory = categoryRepository.findBookCategoryByCategory(category);
        } catch (Exception e) {
            log.info("Error during finding category {}: {}", category, e.getMessage());
            log.debug("Error during finding category {}: {}", category, e);
        }
        return bookCategory;
    }

    @Override
    public Set<BookCategory> findByCategories(Set<String> categories) {
        Set<BookCategory> foundedCategories = new HashSet<>();
        BookCategory tempCategory;
        for (String category : categories) {
            tempCategory = findByCategory(category);
            if (null != tempCategory) foundedCategories.add(tempCategory);
        }
        return foundedCategories;
    }

    public List<BookCategory> getAllCategories() {
        return Collections.unmodifiableList(categoryRepository.findAll(new Sort(Sort.Direction.ASC, "category")));
    }

    public List<String> getAllStringCategories() {
        List<BookCategory> allCategories = getAllCategories();
        List<String> allStringCategories = new ArrayList<>();
        for (BookCategory category : allCategories) {
            allStringCategories.add(category.getCategory());
        }
        return allStringCategories;
    }

    @Override
    public boolean changeCategoryDetails(BookCategory category, String newCategoryName) {
        try {
            if (category != null) {
                category.setCategory(newCategoryName);
                categoryRepository.save(category);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.info("Error during changing category {} name to {}: {}", category, newCategoryName, e.getMessage());
            log.debug("Error during changing category {} name to {}: {}", category, newCategoryName, e);
            return false;
        }
    }
}
