package ru.alex.bookStore.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import ru.alex.bookStore.entities.Book;
import ru.alex.bookStore.entities.UserRole;
import ru.alex.bookStore.utils.book.BookService;
import ru.alex.bookStore.utils.bookCategory.BookCategoryService;
import ru.alex.bookStore.utils.roles.RoleService;
import ru.alex.bookStore.utils.ui.YesNoDialog;
import ru.alex.bookStore.utils.users.UserService;

import java.util.HashSet;
import java.util.Set;

@SpringUI(path = "/adminPanel")
public class AdminUI extends BaseUI {

    @Autowired
    BookCategoryService bookCategoryService;
    @Autowired
    BookService bookService;
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;

    Button logoutButtonBase = new Button("Logout", this::logoutButtonClicked);
    HorizontalSplitPanel createAndShowAllPanel = new HorizontalSplitPanel();
    VerticalLayout globalPanel = new VerticalLayout();
    VerticalLayout leftPanel = new VerticalLayout();

    @Override
    public void init(VaadinRequest vaadinRequest) {
        float horizontalPanelSize = 0;

        Window window = new Window();

        MenuBar menuBar = new MenuBar();
        HorizontalLayout horizontalPanelForButtons = new HorizontalLayout();

        horizontalPanelForButtons.setStyleName(ValoTheme.LAYOUT_WELL);

        createAndShowAllPanel.setWidth(100f, Unit.PERCENTAGE);
        createAndShowAllPanel.setHeight(100f, Unit.PERCENTAGE);
        createAndShowAllPanel.setSplitPosition(30f, Unit.PERCENTAGE);

        globalPanel.addComponent(horizontalPanelForButtons);
        globalPanel.setComponentAlignment(horizontalPanelForButtons, Alignment.TOP_RIGHT);

        globalPanel.addComponent(menuBar);
        globalPanel.setComponentAlignment(menuBar, Alignment.TOP_CENTER);

        globalPanel.addComponent(createAndShowAllPanel);

        logoutButtonBase.setStyleName(ValoTheme.BUTTON_LINK);
        horizontalPanelForButtons.addComponent(logoutButtonBase);
        horizontalPanelSize += logoutButtonBase.getWidth();

        horizontalPanelForButtons.setWidth(horizontalPanelSize, Unit.PIXELS);

        menuBar.setStyleName(ValoTheme.MENUBAR_BORDERLESS);
        menuBar.setWidth(100f, Unit.PERCENTAGE);

        MenuBar.MenuItem usersMenuItem = menuBar.addItem("Users",
                (selectedMenuItem) -> createLeftPanelForUserMenu());
        MenuBar.MenuItem rolesOfUsersMenuItem = menuBar.addItem("Roles of users",
                (selectedMenuItem) -> createLeftPanelForUsersRolesMenu());
        MenuBar.MenuItem booksMenuItem = menuBar.addItem("Books",
                (selectedMenuItem) -> createLeftPanelForBooksMenu());
        MenuBar.MenuItem categoriesOfBooksMenuItem = menuBar.addItem("Categories of books",
                (selectedMenuItem) -> createLeftPanelForBooksCategoriesMenu());

        createAndShowAllPanel.removeAllComponents();
        createAndShowAllPanel.addComponent(leftPanel);

        window.setContent(globalPanel);
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

    private void createLeftPanelForUserMenu() {
        ListSelect<String> listWithUsers = new ListSelect<>();
        listWithUsers.setWidth(100f, Unit.PERCENTAGE);
        listWithUsers.setHeight(100f, Unit.PERCENTAGE);

        listWithUsers.addSelectionListener(event -> {
            Set<String> selected = event.getNewSelection();
            Notification.show(selected.size() + " users selected.", Notification.Type.TRAY_NOTIFICATION);
        });
        listWithUsers.setItems(userService.getAllUsernames());

        VerticalLayout panelWithButtons = new VerticalLayout();
        panelWithButtons.setWidth(100f, Unit.PERCENTAGE);

        Button newUserButton = new Button("Create user", this::createUserButtonClick);
        Button deleteUserButton = new Button("Delete user", this::deleteUserButtonClick);

        panelWithButtons.addComponent(newUserButton);
        panelWithButtons.addComponent(deleteUserButton);

        panelWithButtons.setComponentAlignment(newUserButton, Alignment.TOP_LEFT);
        panelWithButtons.setComponentAlignment(deleteUserButton, Alignment.TOP_LEFT);

        leftPanel.removeAllComponents();
        leftPanel.addComponent(listWithUsers);
        leftPanel.addComponent(panelWithButtons);
        leftPanel.setComponentAlignment(panelWithButtons, Alignment.TOP_CENTER);
    }

    private void createLeftPanelForUsersRolesMenu() {
        ListSelect<String> listWithRolesOfUsers = new ListSelect<>();
        listWithRolesOfUsers.setWidth(100f, Unit.PERCENTAGE);
        listWithRolesOfUsers.setHeight(100f, Unit.PERCENTAGE);

        listWithRolesOfUsers.addSelectionListener(event -> {
            Set<String> selected = event.getNewSelection();
            Notification.show(selected.size() + " roles of users selected.", Notification.Type.TRAY_NOTIFICATION);
        });
        listWithRolesOfUsers.setItems(roleService.getAllStringRoles());

        VerticalLayout panelWithButtons = new VerticalLayout();
        panelWithButtons.setWidth(100f, Unit.PERCENTAGE);

        Button newUserRoleButton = new Button("Create user role", this::createUserRoleButtonClick);
        Button deleteUserRoleButton = new Button("Delete user role", this::deleteUserRoleButtonClick);

        panelWithButtons.addComponent(newUserRoleButton);
        panelWithButtons.addComponent(deleteUserRoleButton);

        panelWithButtons.setComponentAlignment(newUserRoleButton, Alignment.TOP_LEFT);
        panelWithButtons.setComponentAlignment(deleteUserRoleButton, Alignment.TOP_LEFT);

        leftPanel.removeAllComponents();
        leftPanel.addComponent(listWithRolesOfUsers);
        leftPanel.addComponent(panelWithButtons);
        leftPanel.setComponentAlignment(panelWithButtons, Alignment.TOP_CENTER);
    }

    private void createLeftPanelForBooksMenu() {
        ListSelect<Book> listWithBooks = new ListSelect<>();
        listWithBooks.setWidth(100f, Unit.PERCENTAGE);
        listWithBooks.setHeight(100f, Unit.PERCENTAGE);

        listWithBooks.addSelectionListener(event -> {
            Set<Book> selected = event.getNewSelection();
            Notification.show(selected.size() + " books selected.", Notification.Type.TRAY_NOTIFICATION);
        });
        listWithBooks.setItems(bookService.getAllBooks());

            /*VerticalLayout panelWithButtons = new VerticalLayout();
            panelWithButtons.setWidth(100f, Unit.PERCENTAGE);*/

        leftPanel.removeAllComponents();
        leftPanel.addComponent(listWithBooks);
            /*leftPanel.addComponent(panelWithButtons);
        leftPanel.setComponentAlignment(panelWithButtons, Alignment.TOP_CENTER);*/
    }

    private void createLeftPanelForBooksCategoriesMenu() {
        ListSelect<String> listWithCategoriesOfBooks = new ListSelect<>();
        listWithCategoriesOfBooks.setWidth(100f, Unit.PERCENTAGE);
        listWithCategoriesOfBooks.setHeight(100f, Unit.PERCENTAGE);

        listWithCategoriesOfBooks.addSelectionListener(event -> {
            Set<String> selected = event.getNewSelection();
            Notification.show(selected.size() + " categories of books selected.", Notification.Type.TRAY_NOTIFICATION);
        });
        listWithCategoriesOfBooks.setItems(bookCategoryService.getAllStringCategories());

        VerticalLayout panelWithButtons = new VerticalLayout();
        panelWithButtons.setWidth(100f, Unit.PERCENTAGE);

        Button newBookCategoryButton = new Button("Create book category", this::createBookCategoryButtonClick);
        Button deleteBookCategoryButton = new Button("Delete book category", this::deleteBookCategoryButtonClick);

        panelWithButtons.addComponent(newBookCategoryButton);
        panelWithButtons.addComponent(deleteBookCategoryButton);

        panelWithButtons.setComponentAlignment(newBookCategoryButton, Alignment.TOP_LEFT);
        panelWithButtons.setComponentAlignment(deleteBookCategoryButton, Alignment.TOP_LEFT);

        leftPanel.removeAllComponents();
        leftPanel.addComponent(listWithCategoriesOfBooks);
        leftPanel.addComponent(panelWithButtons);
        leftPanel.setComponentAlignment(panelWithButtons, Alignment.TOP_CENTER);
    }

    private void createUserButtonClick(Button.ClickEvent e) {
        Window window = new Window();
        window.setCaption("Create user");

        FormLayout createUserLayout = new FormLayout();
        HorizontalSplitPanel createUserSplitLayout = new HorizontalSplitPanel();
        createUserSplitLayout.setHeight(100f, Unit.PERCENTAGE);
        createUserSplitLayout.setWidth(100f, Unit.PERCENTAGE);

        VerticalLayout leftPanel = new VerticalLayout();
        leftPanel.setCaption("User credentials");
        leftPanel.setHeight(100f, Unit.PERCENTAGE);
        leftPanel.setWidth(100f, Unit.PERCENTAGE);

        Button createNewUserButton = new Button("Create");
        createNewUserButton.addClickListener((event) -> {
            HorizontalSplitPanel parentSplitPanel = (HorizontalSplitPanel) getParent();
            VerticalLayout rightPanel = (VerticalLayout) createUserSplitLayout.getSecondComponent();
            ListSelect<String> listWithRolesOfUsers = (ListSelect<String>) rightPanel.getComponent(1);
            Set<String> selectedItems = listWithRolesOfUsers.getSelectedItems();

            boolean resultOfOperation = false;
            if (null != passwordField.getValue() &&
                    passwordField.getValue().equals(confirmPasswordField.getValue())) {
                resultOfOperation = userService.save(usernameField.getValue(),
                        passwordField.getValue(), roleService.findByRoles(selectedItems));
            }

            Notification.show("User \"" + usernameField.getValue() + "\" with roles \"" +
                            StringUtils.collectionToDelimitedString(selectedItems, ", ") +
                            ((resultOfOperation) ? "\" created." : " didn't created."),
                    Notification.Type.TRAY_NOTIFICATION);
        });

        leftPanel.addComponent(new Label("User credentials"));

        usernameField.setValue("");
        passwordField.setValue("");
        confirmPasswordField.setValue("");

        leftPanel.addComponent(usernameField);
        leftPanel.addComponent(passwordField);
        leftPanel.addComponent(confirmPasswordField);
        leftPanel.addComponent(createNewUserButton);
        leftPanel.setHeight(100f, Unit.PERCENTAGE);

        VerticalLayout rightPanel = new VerticalLayout();
        rightPanel.setHeight(100f, Unit.PERCENTAGE);
        rightPanel.setWidth(100f, Unit.PERCENTAGE);

        ListSelect<String> listWithRolesOfUsers = new ListSelect<>();
        listWithRolesOfUsers.setWidth(100f, Unit.PERCENTAGE);
        listWithRolesOfUsers.setHeight(100f, Unit.PERCENTAGE);

        listWithRolesOfUsers.setItems(roleService.getAllStringRoles());

        rightPanel.addComponent(new Label("Choose roles"));
        rightPanel.addComponent(listWithRolesOfUsers);
        rightPanel.setHeight(100f, Unit.PERCENTAGE);

        createUserSplitLayout.addComponent(leftPanel);
        createUserSplitLayout.addComponent(rightPanel);

        createUserLayout.addComponent(createUserSplitLayout);

        window.setContent(createUserLayout);
        window.center();
        window.setModal(true);
        window.addCloseListener(e1 -> createLeftPanelForUserMenu());
        addWindow(window);
    }

    private void createUserRoleButtonClick(Button.ClickEvent e) {
        Window window = new Window();
        window.setCaption("Create user role");

        FormLayout createRoleLayout = new FormLayout();

        TextField roleField = new TextField("New role");
        roleField.setWidth(100f, Unit.PERCENTAGE);

        Button createNewUserRoleButton = new Button("Create");
        createNewUserRoleButton.addClickListener((event) -> {

            boolean resultOfOperation;

                resultOfOperation = roleService.save(roleField.getValue());

            Notification.show("Role \"" + roleField.getValue() +
                            ((resultOfOperation) ? "\" created." : " didn't created."),
                    Notification.Type.TRAY_NOTIFICATION);
        });
        createRoleLayout.addComponent(roleField);
        createRoleLayout.addComponent(createNewUserRoleButton);
        createRoleLayout.setComponentAlignment(createNewUserRoleButton, Alignment.TOP_LEFT);

        window.setContent(createRoleLayout);
        window.center();
        window.setModal(true);
        window.addCloseListener(e1 -> createLeftPanelForUsersRolesMenu());
        addWindow(window);
    }

    private void createBookCategoryButtonClick(Button.ClickEvent e) {
        Window window = new Window();
        window.setCaption("Create book category");

        FormLayout createCategoryLayout = new FormLayout();

        TextField categoryField = new TextField("New category");
        categoryField.setWidth(100f, Unit.PERCENTAGE);

        Button createNewUserRoleButton = new Button("Create");
        createNewUserRoleButton.addClickListener((event) -> {

            boolean resultOfOperation;

            resultOfOperation = bookCategoryService.save(categoryField.getValue());

            Notification.show("Category \"" + categoryField.getValue() +
                            ((resultOfOperation) ? "\" created." : " didn't created."),
                    Notification.Type.TRAY_NOTIFICATION);
        });
        createCategoryLayout.addComponent(categoryField);
        createCategoryLayout.addComponent(createNewUserRoleButton);
        createCategoryLayout.setComponentAlignment(createNewUserRoleButton, Alignment.TOP_LEFT);

        window.setContent(createCategoryLayout);
        window.center();
        window.setModal(true);
        window.addCloseListener(e1 -> createLeftPanelForBooksCategoriesMenu());
        addWindow(window);
    }

    private void deleteUserButtonClick(Button.ClickEvent e) {
        VerticalLayout panel = (VerticalLayout) createAndShowAllPanel.getFirstComponent();
        ListSelect<String> listWithUsers = (ListSelect<String>) panel.getComponent(0);
        Set<String> selectedItems = listWithUsers.getSelectedItems();

        Window confirmDialogWindow = new YesNoDialog("Confirmation",
                "Do you really want to delete users: " + StringUtils.collectionToDelimitedString(selectedItems, ", "),
                resultIsYes -> {
                    if (resultIsYes) {
                        int countOfDeletedUsers = userService.delete(selectedItems);
                        createLeftPanelForUserMenu();
                        Notification.show(countOfDeletedUsers + " users deleted.",
                                Notification.Type.TRAY_NOTIFICATION);
                    }
                });

        confirmDialogWindow.center();
        addWindow(confirmDialogWindow);
    }

    private void deleteUserRoleButtonClick(Button.ClickEvent e) {
        VerticalLayout panel = (VerticalLayout) createAndShowAllPanel.getFirstComponent();
        ListSelect<String> listWithUsers = (ListSelect<String>) panel.getComponent(0);
        Set<String> selectedItems = listWithUsers.getSelectedItems();

        Window confirmDialogWindow = new YesNoDialog("Confirmation",
                "Do you really want to delete roles: " + StringUtils.collectionToDelimitedString(selectedItems, ", "),
                resultIsYes -> {
                    if (resultIsYes) {
                        int countOfDeletedUsers = roleService.delete(selectedItems);
                        createLeftPanelForUsersRolesMenu();
                        Notification.show(countOfDeletedUsers + " roles deleted",
                                Notification.Type.TRAY_NOTIFICATION);
                    }
                });

        confirmDialogWindow.center();
        addWindow(confirmDialogWindow);
    }

    private void deleteBookCategoryButtonClick(Button.ClickEvent e) {
        VerticalLayout panel = (VerticalLayout) createAndShowAllPanel.getFirstComponent();
        ListSelect<String> listWithUsers = (ListSelect<String>) panel.getComponent(0);
        Set<String> selectedItems = listWithUsers.getSelectedItems();

        Window confirmDialogWindow = new YesNoDialog("Confirmation",
                "Do you really want to delete categories of books: " + StringUtils.collectionToDelimitedString(selectedItems, ", "),
                resultIsYes -> {
                    if (resultIsYes) {
                        int countOfDeletedUsers = bookCategoryService.delete(selectedItems);
                        createLeftPanelForBooksCategoriesMenu();
                        Notification.show(countOfDeletedUsers + " categories deleted.",
                                Notification.Type.TRAY_NOTIFICATION);
                    }
                });

        confirmDialogWindow.center();
        addWindow(confirmDialogWindow);
    }
}
