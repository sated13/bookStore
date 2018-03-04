package ru.alex.bookStore.utils.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.alex.bookStore.entities.Book;
import ru.alex.bookStore.repository.BookRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
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
        List<Book> allBooks = getAllBooks();
        List<String> allStringBooks = new ArrayList<>();
        for(Book book: allBooks) {
            allStringBooks.add(book.toString());
        }
        return allStringBooks;
    }
}
