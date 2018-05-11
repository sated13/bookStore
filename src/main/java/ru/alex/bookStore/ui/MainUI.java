package ru.alex.bookStore.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.alex.bookStore.entities.Book;
import ru.alex.bookStore.entities.BookCategory;
import org.springframework.security.core.userdetails.User;
import ru.alex.bookStore.repository.CategoryRepository;
import ru.alex.bookStore.utils.book.BookService;
import ru.alex.bookStore.utils.shoppingBasket.ShoppingBasketService;
import ru.alex.bookStore.utils.ui.UiUtils;

import java.util.List;
import java.util.Set;

@SpringUI(path = "/main")
@Theme("fixed-valo")
@Slf4j
public class MainUI extends BaseUI {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    BookService bookService;
    @Autowired
    ShoppingBasketService basketService;
    @Autowired
    UiUtils uiUtils;

    private Button loginButtonBase = new Button("Login", this::loginButtonBaseClick);
    Button registerButtonBase = new Button("Register", this::registerButtonBaseClick);
    private Button logoutButtonBase = new Button("Logout", this::logoutButtonClicked);
    Button adminPanelButton = new Button("Admin Panel", this::adminPanelButtonClick);
    Button shoppingBasketButton = new Button("Items: 0, Total Cost: 0", VaadinIcons.CART);

    VerticalLayout layoutWithBooks = new VerticalLayout();

    private final String anonymousUser = "anonymousUser";
    private Boolean isAnonymousUser = false;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String stringUsername;
        if (principal instanceof String) {
            isAnonymousUser = anonymousUser.equals(principal);
            stringUsername = (String) principal;
        } else {
            stringUsername = ((User) principal).getUsername();
        }

        //float horizontalPanelSize = 0;

        Window window = new Window();

        VerticalLayout components = new VerticalLayout();
        components.setSpacing(false);

        HorizontalLayout horizontalPanelForButtons = new HorizontalLayout();
        horizontalPanelForButtons.setSpacing(false);
        horizontalPanelForButtons.setDefaultComponentAlignment(Alignment.TOP_RIGHT);

        //horizontalPanelForButtons.setStyleName(ValoTheme.LAYOUT_WELL);

        loginButtonBase.setStyleName(ValoTheme.BUTTON_LINK);
        registerButtonBase.setStyleName(ValoTheme.BUTTON_LINK);
        logoutButtonBase.setStyleName(ValoTheme.BUTTON_LINK);
        adminPanelButton.setStyleName(ValoTheme.BUTTON_LINK);

        shoppingBasketButton.addClickListener(this::shoppingBasketButtonClick);
        shoppingBasketButton.setStyleName(ValoTheme.BUTTON_LINK);
        setShoppingBasketButtonCaption(getCurrentLoggedInUser());

        if (!isAnonymousUser) {
            if (doesUserHaveAdminRole(stringUsername)) {
                horizontalPanelForButtons.addComponent(adminPanelButton);
                //horizontalPanelSize += adminPanelButton.getWidth();
            }

            horizontalPanelForButtons.addComponent(shoppingBasketButton);
            //horizontalPanelSize += shoppingBasketButton.getWidth();

            horizontalPanelForButtons.addComponent(logoutButtonBase);
            //horizontalPanelSize += logoutButtonBase.getWidth();
        } else {
            horizontalPanelForButtons.addComponent(loginButtonBase);
            //horizontalPanelSize += loginButtonBase.getWidth();
            horizontalPanelForButtons.addComponent(registerButtonBase);
            //horizontalPanelSize += registerButtonBase.getWidth();
        }

        //horizontalPanelForButtons.setWidth(horizontalPanelSize, Unit.PIXELS);

        HorizontalLayout menuLayout = new HorizontalLayout();
        menuLayout.setWidth(100f, Unit.PERCENTAGE);
        menuLayout.addStyleName("for-second-top-level");

        MenuBar menuBar = new MenuBar();
        menuBar.setStyleName(ValoTheme.MENUBAR_BORDERLESS);

        Button newButton = new Button("New", this::newButtonClick);
        Button bestsellersButton = new Button("Bestsellers", this::bestsellersButtonClick);
        Button aboutUsButton = new Button("About us", this::aboutUsButtonClick);

        newButton.setStyleName(ValoTheme.BUTTON_LINK);
        bestsellersButton.setStyleName(ValoTheme.BUTTON_LINK);
        aboutUsButton.setStyleName(ValoTheme.BUTTON_LINK);

        MenuBar.MenuItem categoriesMenuItem = menuBar.addItem("Categories", null, null);

        List<BookCategory> allBookCategories = categoryRepository.findAll();

        for (BookCategory bookCategory : allBookCategories) {
            categoriesMenuItem.addItem(bookCategory.getCategory(), null, (MenuBar.Command) selectedItem -> categoryClick(bookCategory));
        }

        menuLayout.addComponent(newButton);
        menuLayout.addComponent(bestsellersButton);
        menuLayout.addComponent(menuBar);
        menuLayout.addComponent(aboutUsButton);

        menuLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        layoutWithBooks.setSizeFull();

        HorizontalLayout horizontalLayoutWithHorizontalPanelForButtons = new HorizontalLayout();
        horizontalLayoutWithHorizontalPanelForButtons.setWidth(100f, Unit.PERCENTAGE);
        horizontalLayoutWithHorizontalPanelForButtons.addComponent(horizontalPanelForButtons);
        horizontalLayoutWithHorizontalPanelForButtons.setComponentAlignment(horizontalPanelForButtons, Alignment.TOP_RIGHT);
        horizontalLayoutWithHorizontalPanelForButtons.addStyleName("for-first-top-level");

        /*components.addComponent(horizontalPanelForButtons);*/
        components.addComponents(horizontalLayoutWithHorizontalPanelForButtons, menuLayout, layoutWithBooks);

        //components.setComponentAlignment(horizontalPanelForButtons, Alignment.TOP_RIGHT);
        components.setComponentAlignment(menuLayout, Alignment.TOP_CENTER);
        components.setMargin(false);

        window.setContent(components);
        window.setSizeFull();
        window.setResizable(false);
        window.setClosable(false);
        addWindow(window);
    }

    private void newButtonClick(Button.ClickEvent event) {
        Set<Book> top10NewBooks = bookService.findTop10BooksOrderedByAddingDay();
        layoutWithBooks.removeAllComponents();

        /*for (Book book : top10NewBooks) {
            layoutWithBooks.addComponent(uiUtils.createLayoutForBookDetails(book, uiUtils.addButton(isAnonymousUser, book, shoppingBasketButton)));
        }*/
        showFoundBooks(top10NewBooks);
        layoutWithBooks.markAsDirty();
    }

    private void bestsellersButtonClick(Button.ClickEvent event) {
        Set<Book> bestsellers = bookService.findTop10BooksOrderedByCountOfSoldItems();
        layoutWithBooks.removeAllComponents();

        /*for (Book book : bestsellers) {
            layoutWithBooks.addComponent(uiUtils.createLayoutForBookDetails(book, uiUtils.addButton(isAnonymousUser, book, shoppingBasketButton)));
        }*/
        showFoundBooks(bestsellers);
        layoutWithBooks.markAsDirty();
    }

    private void categoryClick(BookCategory category) {
        Set<Book> booksWithSelectedCategory = bookService.findBooksByCategoriesContains(category);
        layoutWithBooks.removeAllComponents();

        layoutWithBooks.addComponent(new Label("Books with category: " + booksWithSelectedCategory.size()));

        /*for (Book book : booksWithSelectedCategory) {
            layoutWithBooks.addComponent(uiUtils.createLayoutForBookDetails(book, uiUtils.addButton(isAnonymousUser, book, shoppingBasketButton)));
        }*/
        showFoundBooks(booksWithSelectedCategory);
        layoutWithBooks.markAsDirty();
    }

    private void showFoundBooks(Set<Book> foundBooks) {
        for (Book book : foundBooks) {
            layoutWithBooks.addComponent(uiUtils.createLayoutForBookDetails(book, addButton(book)));
        }
    }

    private AbstractOrderedLayout addButton(Book book) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(false);
        layout.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        Button addBookToBasket = new Button("Add to basket", VaadinIcons.PLUS);
        addBookToBasket.addClickListener(event -> {
            if (!isAnonymousUser) {
                ru.alex.bookStore.entities.User user = getCurrentLoggedInUser();
                boolean result = basketService.addItemForUser(user, book);

                if (result) {
                    String msg = "Book \"" + book.toString() + "\" added to shopping basket";
                    Notification.show(msg,
                            Notification.Type.TRAY_NOTIFICATION);
                    log.info(msg);
                }

                setShoppingBasketButtonCaption(user);
            }
        });
        layout.addComponent(addBookToBasket);

        return layout;
    }

    private void setShoppingBasketButtonCaption(ru.alex.bookStore.entities.User user) {
        if (null != shoppingBasketButton && null != user)
            shoppingBasketButton.setCaption("Items: " + basketService.getTotalCountForUser(user) +
                    ", Total Cost: " + basketService.getTotalCostForUser(user));
    }

    private ru.alex.bookStore.entities.User getCurrentLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String stringUsername = (principal instanceof String) ? (String) principal : ((User) principal).getUsername();

        return userService.findByUsername(stringUsername);
    }

    private void aboutUsButtonClick(Button.ClickEvent event) {
        layoutWithBooks.removeAllComponents();

        layoutWithBooks.addComponent(new Label("E-mail: belov.aleksandr@bk.ru"));
    }

    private void logoutButtonClicked(Button.ClickEvent e) {
        SecurityContextHolder.clearContext();
        //redirect to login page
        getPage().setLocation("/main?logout");
    }

    private void loginButtonBaseClick(Button.ClickEvent e) {
        createForm("authorization", localVaadinRequest);
    }

    private void registerButtonBaseClick(Button.ClickEvent e) {
        createForm("register", localVaadinRequest);
    }

    private void adminPanelButtonClick(Button.ClickEvent e) {
        getPage().setLocation("/adminPanel");
    }

    private void shoppingBasketButtonClick(Button.ClickEvent e) {
        getPage().setLocation("/shoppingBasket");
    }

    private boolean doesUserHaveAdminRole(String username) {
        return userService.isAdmin(username);
    }
}
