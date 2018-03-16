package ru.alex.bookStore.utils.book;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.bookStore.entities.Book;
import ru.alex.bookStore.entities.BookCategory;
import ru.alex.bookStore.entities.Cover;
import ru.alex.bookStore.repository.BookRepository;
import ru.alex.bookStore.utils.cover.CoverService;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    CoverService coverService;

    @Override
    public Book save(Map<String, Object> bookParameters) {
        Book newBook = new Book();
        Cover cover = setParameters(newBook, bookParameters);

        try {
            newBook.setPictureOfBookCover(cover);
            coverService.save(cover);
            bookRepository.save(newBook);
        } catch (Exception e) {
            //ToDo: add logging
            return null;
        }

        return newBook;
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
            Cover cover = book.getPictureOfBookCover();
            Cover newCover = setParameters(book, bookParameters);

            if (newCover.isPresented()) {
                book.setPictureOfBookCover(cover);
                coverService.save(cover);
            }

            return true;
        } catch (Exception e) {
            //ToDo: add logging
            return false;
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
}
