package ru.alex.bookStore.utils.book;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.alex.bookStore.entities.Book;
import ru.alex.bookStore.entities.BookCategory;
import ru.alex.bookStore.entities.Cover;
import ru.alex.bookStore.repository.BookRepository;
import ru.alex.bookStore.utils.bookCategory.CategoryService;
import ru.alex.bookStore.utils.cover.CoverService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@NoArgsConstructor
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    CoverService coverService;
    @Autowired
    CategoryService categoryService;

    @Override
    public boolean save(Map<String, Object> bookParameters) {
        Book newBook = new Book();
        setParameters(newBook, bookParameters);

        log.debug("save Book method, bookParameters: {}", bookParameters);
        try {
            newBook.setAddingDay(LocalDate.now());
            bookRepository.save(newBook);
        } catch (Exception e) {
            log.debug("Error during book saving: {}", e.getMessage());
            log.error("Error during book saving with parameters: {}", bookParameters, e);
            return false;
        }

        return true;
    }

    @Override
    public boolean delete(Book book) {
        try {
            bookRepository.delete(book);
            return true;
        } catch (Exception e) {
            log.debug("Error during book deleting: {}", e.getMessage());
            log.error("Error during book deleting", e);
            return false;
        }
    }

    @Override
    public int delete(Set<Book> books) {
        int result = 0;

        for (Book book : books) {
            result += (delete(book)) ? 1 : 0;
        }

        return result;
    }

    @Override
    public Map<BookCategory, Set<Book>> getBooksByCategories() {
        Map<BookCategory, Set<Book>> resultMap = new HashMap<>();

        try {
            List<BookCategory> categories = categoryService.getAllCategories();
            Set<Book> setOfBooksForCategory;

            for (BookCategory category : categories) {
                setOfBooksForCategory = findBooksByCategoriesContains(category);
                resultMap.put(category, setOfBooksForCategory);
            }
        } catch (Exception e) {
            log.debug("Error during getting books by categories: {}", e.getMessage());
            log.error("Error during getting books by categories", e);
        }

        return resultMap;
    }

    @Override
    public Integer countOfBooksWithCategory(BookCategory category) {
        try {
            return bookRepository.countBooksByCategoriesContains(category);
        } catch (Exception e) {
            log.debug("Error during counting books with category {}: {}", category, e.getMessage());
            log.error("Error during counting books with category {}", category, e);
            return 0;
        }
    }

    @Override
    public Map<BookCategory, Integer> getCountOfBooksByCategories() {
        Map<BookCategory, Integer> resultMap = new HashMap<>();

        try {
            List<BookCategory> categories = categoryService.getAllCategories();

            for (BookCategory category : categories) {
                resultMap.put(category, bookRepository.countBooksByCategoriesContains(category));
            }
        } catch (Exception e) {
            log.debug("Error during counting books by categories: {}", e.getMessage());
            log.error("Error during counting books by categories", e);
        }

        return resultMap;
    }

    @Override
    public long countBooks() {
        try {
            return bookRepository.count();
        } catch (Exception e) {
            log.debug("Error during counting books: {}", e.getMessage());
            log.error("Error during counting books", e);
            return 0;
        }
    }

    @Override
    public List<Book> findBooksByBookTitle(String bookTitle) {
        return Collections.unmodifiableList(bookRepository.findBooksByBookTitle(bookTitle));
    }

    @Override
    public List<Book> getAllBooks() {
        return Collections.unmodifiableList(bookRepository.findAll(new Sort(Sort.Direction.ASC, "bookTitle")));
    }

    @Override
    public List<String> getAllStringBooks() {
        return getAllBooks().stream().map(Book::toString).collect(Collectors.toList());
    }

    @Override
    public Set<BookCategory> getBookCategories(Book book) {
        return book.getCategories();
    }

    @Override
    public Cover getBookCover(Book book) {
        return book.getPictureOfBookCover();
    }

    @Override
    public boolean changeBookDetails(Book book, Map<String, Object> bookParameters) {
        try {
            Cover newCover = coverService.createEmptyCover();
            setParameters(book, bookParameters);

            if (bookParameters.containsKey("pictureOfBookCover")) {
                newCover.setFilename(book.getID());
                newCover.setPictureOfBookCover((byte[]) bookParameters.get("pictureOfBookCover"));
                newCover.setPresented(true);

                book.setPictureOfBookCover(newCover);
                coverService.save(newCover);
            }

            bookRepository.save(book);
            return true;
        } catch (Exception e) {
            log.debug("Error during changing book details: {}\n{}\n{}", book, bookParameters, e.getMessage());
            log.error("Error during changing book details: {}\n{}", book, bookParameters, e);
            return false;
        }
    }

    @Override
    public Set<Book> findBooksByCategoriesContains(BookCategory category) {
        try {
            return bookRepository.findBooksByCategoriesContains(category);
        } catch (Exception e) {
            log.debug("Error during finding book by category {}: {}", category, e.getMessage());
            log.error("Error during finding book by category {}", category, e);
            return null;
        }
    }

    @Override
    public Set<Book> findTop10BooksOrderedByAddingDay() {
        try {
            return bookRepository.findTop10ByOrderByAddingDayDesc();
        } catch (Exception e) {
            log.debug("Error during finding top 10 books: {}", e.getMessage());
            log.error("Error during finding top 10 books", e);
            return null;
        }
    }

    @Override
    public Set<Book> findTop10BooksOrderedByCountOfSoldItems() {
        try {
            return bookRepository.findTop10ByOrderByCountOfSoldItemsDesc();
        } catch (Exception e) {
            log.debug("Error during finding top 10 books ordered by count of sold items: {}", e.getMessage());
            log.error("Error during finding top 10 books ordered by count of sold items", e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private void setParameters(Book book, Map<String, Object> bookParameters) {

        for (String key : bookParameters.keySet()) {
            switch (key) {
                case "bookTitle": {
                    book.setBookTitle((String) bookParameters.get(key));
                    break;
                }
                case "authors": {
                    book.setAuthors((Set<String>) bookParameters.get(key));
                    break;
                }
                case "description": {
                    book.setDescription((String) bookParameters.get(key));
                    break;
                }
                case "categories": {
                    book.setCategories((Set<BookCategory>) bookParameters.get(key));
                    break;
                }
                case "numberOfPages": {
                    book.setNumberOfPages((Integer) bookParameters.get(key));
                    break;
                }
                case "year": {
                    book.setYear((Short) bookParameters.get(key));
                    break;
                }
                case "publishingHouse": {
                    book.setPublishingHouse((String) bookParameters.get(key));
                    break;
                }
                case "price": {
                    book.setPrice((double) bookParameters.get(key));
                    break;
                }
                case "numberOfCopies": {
                    book.setNumberOfCopies((Integer) bookParameters.get(key));
                    break;
                }
            }
        }
    }

    @Override
    public int addCategoryToBooks(BookCategory category, Set<Book> books) {
        int countOfChangedBooks = 0;

        for (Book book : books) {
            try {
                book.addCategory(category);
                bookRepository.save(book);
                countOfChangedBooks++;
            } catch (Exception e) {
                log.debug("Error during adding category {} to book {}: {}", category, book, e.getMessage());
                log.error("Error during adding category {} to book {}", category, book, e);
            }
        }

        return countOfChangedBooks;
    }

    @Override
    public boolean addCoverToBook(Book book, byte[] pictureBytes) {
        try {
            Cover cover = coverService.createEmptyCover();
            //coverService.save(cover);
            coverService.setBookId(cover, book.getID());

            cover.setPictureOfBookCover(pictureBytes);
            cover.setPresented(true);

            coverService.save(cover);

            book.setPictureOfBookCover(cover);
            bookRepository.save(book);
            return true;
        } catch (Exception e) {
            //ToDo exception
            return false;
        }
    }

    @Override
    public int setCategoryOnBooks(BookCategory category, Set<Book> books) {
        int countOfChangedBooks = 0;
        Set<Book> booksWithCategory = findBooksByCategoriesContains(category);

        for (Book book : booksWithCategory) {
            try {
                if (!books.contains(book)) {
                    book.deleteCategory(category);
                    bookRepository.save(book);
                    countOfChangedBooks++;
                }
            } catch (Exception e) {
                log.debug("Error during setting category {} for book {}: {}", category, book, e.getMessage());
                log.error("Error during setting category {} for book {}", category, book, e);
            }
        }

        return countOfChangedBooks;
    }
}
