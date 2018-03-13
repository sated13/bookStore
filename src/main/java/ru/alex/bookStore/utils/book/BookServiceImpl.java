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
@Transactional
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    CoverService coverService;

    @Override
    public Book save(Map<String, Object> bookParameters) {
        Book newBook = new Book();
        Cover cover = new Cover();

        for(String key: bookParameters.keySet()) {
            switch (key) {
                case "bookTitle": {
                    newBook.setBookTitle((String)bookParameters.get(key));
                    break;
                }
                case "authors": {
                    newBook.setAuthors((Set<String>) bookParameters.get(key));
                    break;
                }
                case "categories": {
                    newBook.setCategories((Set<BookCategory>) bookParameters.get(key));
                    break;
                }
                case "numberOfPages": {
                    newBook.setNumberOfPages((Integer) bookParameters.get(key));
                    break;
                }
                case "year": {
                    newBook.setYear((Short) bookParameters.get(key));
                    break;
                }
                case "publishingHouse": {
                    newBook.setPublishingHouse((String) bookParameters.get(key));
                    break;
                }
                case "price": {
                    newBook.setPrice((BigDecimal) bookParameters.get(key));
                    break;
                }
                case "numberOfCopies": {
                    newBook.setNumberOfCopies((Integer) bookParameters.get(key));
                    break;
                }
                case "pictureOfBookCover": {
                    cover = new Cover((byte[]) bookParameters.get(key));
                    cover.setPresented(true);
                    break;
                }
            }
        }

        try {
            newBook.setPictureOfBookCover(cover);
            coverService.save(cover);
            bookRepository.save(newBook);
        }
        catch (Exception e) {
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

        for (Book book: books) {
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
        List<String> allStringBooks = getAllBooks().stream().map(Book::toString).collect(Collectors.toList());
        return allStringBooks;
    }

    @Override
    public Set<BookCategory> getBookCategories(Book book) {
        Hibernate.initialize(book.getCategories());
        return book.getCategories();
    }

    @Override
    public Cover getBookCover(Book book) {
        Hibernate.initialize(book.getPictureOfBookCover());
        return book.getPictureOfBookCover();
    }
}
