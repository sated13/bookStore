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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;

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
