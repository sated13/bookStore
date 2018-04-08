package ru.alex.bookStore.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import ru.alex.bookStore.entities.Book;
import ru.alex.bookStore.utils.shoppingBasket.ShoppingBasketService;
import ru.alex.bookStore.utils.ui.UiUtils;

import java.util.Iterator;
import java.util.Set;

@SpringUI(path = "/shoppingBasket")
@Theme("fixed-valo")
public class ShoppingBasketUI extends BaseUI {

    @Autowired
    private ShoppingBasketService basketService;
    @Autowired
    private UiUtils uiUtils;

    private Button logoutButton = new Button("Logout", this::logoutButtonClicked);
    private Button shoppingBasketButton = new Button("Items: 0, Total Cost: 0", VaadinIcons.CART);
    private Button mainPageButton = new Button("Main Page", this::mainPageButtonClick);

    private int contentWindowScrollLeftOffset = 0;
    private Window contentWindow = new Window();

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        VerticalLayout globalLayout = new VerticalLayout();
        globalLayout.setSpacing(false);

        VerticalLayout layoutWithBooks = new VerticalLayout();

        HorizontalLayout horizontalPanelForButtons = new HorizontalLayout();
        //horizontalPanelForButtons.setStyleName(ValoTheme.LAYOUT_WELL);

        logoutButton.setStyleName(ValoTheme.BUTTON_LINK);
        shoppingBasketButton.setStyleName(ValoTheme.BUTTON_LINK);
        mainPageButton.setStyleName(ValoTheme.BUTTON_LINK);

        horizontalPanelForButtons.addComponents(mainPageButton, shoppingBasketButton, logoutButton);
        horizontalPanelForButtons.setSpacing(false);
        horizontalPanelForButtons.setDefaultComponentAlignment(Alignment.TOP_RIGHT);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String stringUsername = (principal instanceof String) ? (String) principal : ((User) principal).getUsername();

        ru.alex.bookStore.entities.User user = userService.findByUsername(stringUsername);

        Set<ShoppingBasketService.BasketItem> booksInBasket = basketService.getBasketForUser(user);

        if (null != booksInBasket) {
            for (ShoppingBasketService.BasketItem basketItem : booksInBasket) {
                layoutWithBooks.addComponent(
                        uiUtils.createLayoutForBookDetails(
                                basketItem.getBook(), deleteButton(basketItem.getBook())));
            }
        }

        setShoppingBasketButtonCaption(user);

        layoutWithBooks.setSizeFull();
        layoutWithBooks.markAsDirty();

        Button payBooksButton = new Button("Pay books");

        HorizontalLayout layout = new HorizontalLayout();
        layout.addComponent(horizontalPanelForButtons);
        layout.setComponentAlignment(horizontalPanelForButtons, Alignment.TOP_RIGHT);
        layout.setWidth(100f, Unit.PERCENTAGE);

        globalLayout.addComponent(layout);
        /*globalLayout.addComponent(horizontalPanelForButtons);
        globalLayout.setComponentAlignment(horizontalPanelForButtons, Alignment.TOP_RIGHT);*/
        globalLayout.addComponents(layoutWithBooks, payBooksButton);
        globalLayout.setComponentAlignment(payBooksButton, Alignment.TOP_LEFT);

        contentWindow.setContent(globalLayout);
        contentWindow.setSizeFull();
        contentWindow.setResizable(false);
        contentWindow.setClosable(false);
        contentWindow.setScrollLeft(contentWindowScrollLeftOffset);
        addWindow(contentWindow);
    }

    private AbstractOrderedLayout deleteButton(Book book) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        Button deleteBookFromBasketButton = new Button("Delete from basket", VaadinIcons.MINUS);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String stringUsername = (principal instanceof String) ? (String) principal : ((User) principal).getUsername();

        ru.alex.bookStore.entities.User user = userService.findByUsername(stringUsername);

        Label itemsCountLabel = new Label("Added: " + basketService.getBookCount(user, book));

        deleteBookFromBasketButton.addClickListener(event -> {
            boolean result = basketService.deleteBookFromUser(user, book);

            if (result)
                Notification.show("Book \"" + book.toString() + "\" deleted from shopping basket",
                        Notification.Type.TRAY_NOTIFICATION);

            if (null != shoppingBasketButton)
                shoppingBasketButton.setCaption("Items: " + basketService.getTotalCountForUser(user) +
                        ", Total Cost: " + basketService.getTotalCostForUser(user));

            if (basketService.getBookCount(user, book) != 0) {
                itemsCountLabel.setValue("Added: " + basketService.getBookCount(user, book));
            } else {
                Iterator<Window> iterator = getPage().getUI().getWindows().iterator();
                if (iterator.hasNext()) {
                    contentWindowScrollLeftOffset = iterator.next().getScrollLeft();
                }
                getPage().reload();
            }
        });

        layout.addComponents(itemsCountLabel, deleteBookFromBasketButton);

        return layout;
    }

    private void setShoppingBasketButtonCaption(ru.alex.bookStore.entities.User user) {
        shoppingBasketButton.setCaption("Items: " + basketService.getTotalCountForUser(user) + ", Total Cost: " + basketService.getTotalCostForUser(user));
    }

    private void logoutButtonClicked(Button.ClickEvent e) {
        SecurityContextHolder.clearContext();
        //redirect to login page
        getPage().setLocation("/main?logout");
    }

    private void mainPageButtonClick(Button.ClickEvent e) {
        getPage().setLocation("/main");
    }
}
