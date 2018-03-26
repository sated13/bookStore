package ru.alex.bookStore.utils.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.alex.bookStore.entities.Book;
import ru.alex.bookStore.entities.BookCategory;
import ru.alex.bookStore.entities.Cover;
import ru.alex.bookStore.repository.BookRepository;
import ru.alex.bookStore.utils.bookCategory.CategoryService;
import ru.alex.bookStore.utils.cover.CoverService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
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
        Cover cover = setParameters(newBook, bookParameters);

        try {
            newBook.setPictureOfBookCover(cover);
            newBook.setAddingDay(LocalDate.now());
            coverService.save(cover);
            bookRepository.save(newBook);
        } catch (Exception e) {
            //ToDo: add logging
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
            //ToDo: add logging
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
            //ToDo: add logging
        }

        return resultMap;
    }

    @Override
    public Integer countOfBooksWithCategory(BookCategory category) {
        try {
            return bookRepository.countBooksByCategoriesContains(category);
        } catch (Exception e) {
            //ToDo: add logging
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
            //ToDo: add logging
        }

        return resultMap;
    }

    @Override
    public long countBooks() {
        try {
            return bookRepository.count();
        } catch (Exception e) {
            //ToDo: add logging
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
            Cover newCover = setParameters(book, bookParameters);

            if (newCover.isPresented()) {
                book.setPictureOfBookCover(newCover);
                coverService.save(newCover);
            }
            bookRepository.save(book);
            return true;
        } catch (Exception e) {
            //ToDo: add logging
            return false;
        }
    }

    @Override
    public Set<Book> findBooksByCategoriesContains(BookCategory category) {
        try {
            return bookRepository.findBooksByCategoriesContains(category);
        } catch (Exception e) {
            //ToDo: add logging
            return null;
        }
    }

    @Override
    public Set<Book> findTop10BooksOrderedByAddingDay() {
        try {
            return bookRepository.findTop10ByOrderByAddingDayDesc();
        } catch (Exception e) {
            //ToDo: add logging
            return null;
        }
    }

    @Override
    public Set<Book> findTop10BooksOrderedByCountOfSoldItems() {
        try {
            return bookRepository.findTop10ByOrderByCountOfSoldItemsDesc();
        } catch (Exception e) {
            //ToDo: add logging
            return null;
        }
    }

    private Cover setParameters(Book book, Map<String, Object> bookParameters) {
        Cover cover = new Cover();

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
                    book.setPrice((BigDecimal) bookParameters.get(key));
                    break;
                }
                case "numberOfCopies": {
                    book.setNumberOfCopies((Integer) bookParameters.get(key));
                    break;
                }
                case "pictureOfBookCover": {
                    cover = new Cover((byte[]) bookParameters.get(key));
                    cover.setPresented(true);
                    break;
                }
            }
        }

        return cover;
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
                //ToDo: add logging
            }
        }

        return countOfChangedBooks;
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
                //ToDo: add logging
                e.printStackTrace(System.out);
            }
        }

        return countOfChangedBooks;
    }
}
