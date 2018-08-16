package ru.alex.bookStore.utils.shoppingBasket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.alex.bookStore.entities.Book;
import ru.alex.bookStore.entities.User;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class ShoppingBasketService {

    private Map<User, Set<BasketItem>> basketItems = new ConcurrentHashMap<>();

    public ShoppingBasketService() {
    }

    public Set<BasketItem> getBasketForUser(@NotNull User user) {
        return basketItems.getOrDefault(user, null);
    }

    public boolean addItemForUser(@NotNull User user, @NotNull Book book) {
        try {
            if (!basketItems.containsKey(user)) {
                basketItems.put(user, new HashSet<>());
            }
            Iterator<BasketItem> itemIterator = basketItems.get(user).iterator();
            BasketItem basketItem;
            boolean addedBook = false;
            while (itemIterator.hasNext()) {
                basketItem = itemIterator.next();
                if (basketItem.getBook().equals(book)) {
                    basketItem.addBook();
                    addedBook = true;
                    break;
                }
            }
            if (!addedBook) {
                basketItem = new BasketItem(book, 1);
                basketItems.get(user).add(basketItem);
            }
            return true;
        } catch (Exception e) {
            log.error("Error during adding item {} to user {} basket: {}", book, user, e);
            return false;
        }
    }

    public double getTotalCostForUser(@NotNull User user) {
        double totalCost = 0.0;

        if (basketItems.containsKey(user)) {
            for (BasketItem item : basketItems.get(user)) {
                totalCost += item.getTotalCost();
            }
        }

        return totalCost;
    }

    public int getTotalCountForUser(@NotNull User user) {
        int totalCount = 0;

        if (basketItems.containsKey(user)) {
            for (BasketItem item : basketItems.get(user)) {
                totalCount += item.getBooksCount();
            }
        }

        return totalCount;
    }

    public int getBookCount(@NotNull User user, @NotNull Book book) {
        int bookCount = 0;

        if (basketItems.containsKey(user)) {
            for (BasketItem basketItem : basketItems.get(user)) {
                if (basketItem.getBook().equals(book)) {
                    bookCount = basketItem.getBooksCount();
                    break;
                }
            }
        }

        return bookCount;
    }

    public boolean deleteBookFromUser(@NotNull User user, Book book) {
        boolean result = false;

        if (basketItems.containsKey(user)) {
            Iterator<BasketItem> itemIterator = basketItems.get(user).iterator();
            BasketItem basketItem;
            while (itemIterator.hasNext()) {
                basketItem = itemIterator.next();
                if (basketItem.getBook().equals(book)) {
                    basketItem.delete1Book();
                    if (basketItem.getBooksCount() == 0) itemIterator.remove();
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    public class BasketItem {
        private Book book;
        private int booksCount;

        public BasketItem(Book book, int booksCount) {
            this.book = book;
            this.booksCount = booksCount == 0 ? 1 : booksCount;
        }

        public Book getBook() {
            return book;
        }

        public double getTotalCost() {
            return book.getPrice() * booksCount;
        }

        public int getBooksCount() {
            return booksCount;
        }

        public void delete1Book() {
            booksCount = booksCount > 0 ? booksCount - 1 : 0;
        }

        public void addBook() {
            booksCount++;
        }
    }
}
