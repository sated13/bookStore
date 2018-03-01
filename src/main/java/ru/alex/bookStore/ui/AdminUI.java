package ru.alex.bookStore.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.alex.bookStore.entities.Book;
import ru.alex.bookStore.entities.BookCategory;
import ru.alex.bookStore.entities.User;
import ru.alex.bookStore.entities.UserRole;
import ru.alex.bookStore.repository.BookCategoryRepository;
import ru.alex.bookStore.repository.BookRepository;
import ru.alex.bookStore.repository.UserRepository;
import ru.alex.bookStore.repository.UserRoleRepository;

import java.util.Set;

@SpringUI(path = "/adminPanel")
public class AdminUI extends BaseUI {

    @Autowired
    BookCategoryRepository bookCategoryRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserRoleRepository userRoleRepository;

    Button logoutButtonBase = new Button("Logout", this::logoutButtonClicked);
    HorizontalSplitPanel createAndShowAllPanel = new HorizontalSplitPanel();

    @Override
    public void init(VaadinRequest vaadinRequest) {
        float horizontalPanelSize = 0;

        Window window = new Window();

        VerticalLayout components = new VerticalLayout();
        MenuBar menuBar = new MenuBar();
        HorizontalLayout horizontalPanelForButtons = new HorizontalLayout();

        horizontalPanelForButtons.setStyleName(ValoTheme.LAYOUT_WELL);

        createAndShowAllPanel.setWidth(100f, Unit.PERCENTAGE);
        createAndShowAllPanel.setHeight(100f, Unit.PERCENTAGE);
        createAndShowAllPanel.setSplitPosition(30f, Unit.PERCENTAGE);

        components.addComponent(horizontalPanelForButtons);
        components.setComponentAlignment(horizontalPanelForButtons, Alignment.TOP_RIGHT);

        components.addComponent(menuBar);
        components.setComponentAlignment(menuBar, Alignment.TOP_CENTER);

        components.addComponent(createAndShowAllPanel);

        logoutButtonBase.setStyleName(ValoTheme.BUTTON_LINK);
        horizontalPanelForButtons.addComponent(logoutButtonBase);
        horizontalPanelSize += logoutButtonBase.getWidth();

        horizontalPanelForButtons.setWidth(horizontalPanelSize, Unit.PIXELS);

        menuBar.setStyleName(ValoTheme.MENUBAR_BORDERLESS);
        menuBar.setWidth(100f, Unit.PERCENTAGE);

        MenuBar.MenuItem usersMenuItem = menuBar.addItem("Users", (selectedMenuItem) -> {
            VerticalLayout panel = new VerticalLayout();

            ListSelect<User> listWithUsers = new ListSelect<>();
            listWithUsers.setWidth(100f, Unit.PERCENTAGE);
            listWithUsers.setHeight(100f, Unit.PERCENTAGE);

            listWithUsers.addSelectionListener(event -> {
                Set<User> selected = event.getNewSelection();
                Notification.show(selected.size() + " users selected.", Notification.Type.TRAY_NOTIFICATION);
            });
            listWithUsers.setItems(userRepository.findAll());

            HorizontalLayout panelWithButtons = new HorizontalLayout();
            panelWithButtons.setWidth(100f, Unit.PERCENTAGE);

            panel.addComponent(listWithUsers);
            panel.addComponent(panelWithButtons);

            createAndShowAllPanel.removeAllComponents();
            createAndShowAllPanel.addComponent(panel);
        });
        MenuBar.MenuItem rolesOfUsersMenuItem = menuBar.addItem("Roles of users", (selectedMenuItem) -> {
            VerticalLayout panel = new VerticalLayout();

            ListSelect<UserRole> listWithRolesOfUsers = new ListSelect<>();
            listWithRolesOfUsers.setWidth(100f, Unit.PERCENTAGE);
            listWithRolesOfUsers.setHeight(100f, Unit.PERCENTAGE);

            listWithRolesOfUsers.addSelectionListener(event -> {
                Set<UserRole> selected = event.getNewSelection();
                Notification.show(selected.size() + " roles of users selected.", Notification.Type.TRAY_NOTIFICATION);
            });
            listWithRolesOfUsers.setItems(userRoleRepository.findAll());

            HorizontalLayout panelWithButtons = new HorizontalLayout();
            panelWithButtons.setWidth(100f, Unit.PERCENTAGE);

            panel.addComponent(listWithRolesOfUsers);
            panel.addComponent(panelWithButtons);

            createAndShowAllPanel.removeAllComponents();
            createAndShowAllPanel.addComponent(panel);
        });
        MenuBar.MenuItem booksMenuItem = menuBar.addItem("Books", (selectedMenuItem) -> {
            VerticalLayout panel = new VerticalLayout();

            ListSelect<Book> listWithBooks = new ListSelect<>();
            listWithBooks.setWidth(100f, Unit.PERCENTAGE);
            listWithBooks.setHeight(100f, Unit.PERCENTAGE);

            listWithBooks.addSelectionListener(event -> {
                Set<Book> selected = event.getNewSelection();
                Notification.show(selected.size() + " books selected.", Notification.Type.TRAY_NOTIFICATION);
            });
            listWithBooks.setItems(bookRepository.findAll());

            HorizontalLayout panelWithButtons = new HorizontalLayout();
            panelWithButtons.setWidth(100f, Unit.PERCENTAGE);

            panel.addComponent(listWithBooks);
            panel.addComponent(panelWithButtons);

            createAndShowAllPanel.removeAllComponents();
            createAndShowAllPanel.addComponent(panel);
        });
        MenuBar.MenuItem categoriesOfBooksMenuItem = menuBar.addItem("Categories of books", (selectedMenuItem) -> {
            VerticalLayout panel = new VerticalLayout();

            ListSelect<BookCategory> listWithCategoriesOfBooks = new ListSelect<>();
            listWithCategoriesOfBooks.setWidth(100f, Unit.PERCENTAGE);
            listWithCategoriesOfBooks.setHeight(100f, Unit.PERCENTAGE);

            listWithCategoriesOfBooks.addSelectionListener(event -> {
                Set<BookCategory> selected = event.getNewSelection();
                Notification.show(selected.size() + " categories of books selected.", Notification.Type.TRAY_NOTIFICATION);
            });
            listWithCategoriesOfBooks.setItems(bookCategoryRepository.findAll());

            HorizontalLayout panelWithButtons = new HorizontalLayout();
            panelWithButtons.setWidth(100f, Unit.PERCENTAGE);

            panel.addComponent(listWithCategoriesOfBooks);
            panel.addComponent(panelWithButtons);

            createAndShowAllPanel.removeAllComponents();
            createAndShowAllPanel.addComponent(panel);
        });

        window.setContent(components);
        window.setSizeFull();
        window.setResizable(false);
        window.setClosable(false);
        addWindow(window);
    }

    private void logoutButtonClicked(Button.ClickEvent e) {
        SecurityContextHolder.clearContext();
        //redirect to login page
        getPage().setLocation("/main?logout");
    }

    private class UserMenuItemCommand implements MenuBar.Command {

        @Override
        public void menuSelected(MenuBar.MenuItem selectedItem) {

        }
    }
}
