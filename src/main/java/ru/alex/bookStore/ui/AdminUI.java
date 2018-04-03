package ru.alex.bookStore.ui;

import com.vaadin.data.Result;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.data.validator.*;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.datefield.DateResolution;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import ru.alex.bookStore.entities.*;
import ru.alex.bookStore.utils.ui.ComponentValueValidation;
import ru.alex.bookStore.utils.book.BookService;
import ru.alex.bookStore.utils.bookCategory.CategoryService;
import ru.alex.bookStore.utils.roles.RoleService;
import ru.alex.bookStore.utils.ui.ImageUploader;
import ru.alex.bookStore.utils.ui.YesNoDialog;
import ru.alex.bookStore.utils.users.UserService;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@SpringUI(path = "/adminPanel")
public class AdminUI extends BaseUI {

    @Autowired
    CategoryService categoryService;
    @Autowired
    BookService bookService;
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    ConversionService conversionService;

    Button logoutButtonBase = new Button("Logout", this::logoutButtonClicked);
    Button logConfigButtonBase = new Button("Log configuration", this::logConfigurationButtonClicked);
    VerticalLayout globalPanel = new VerticalLayout();
    HorizontalSplitPanel createAndShowAllItemsPanel = new HorizontalSplitPanel();
    VerticalLayout leftPanel = new VerticalLayout();
    VerticalLayout rightPanel = new VerticalLayout();

    private final String usersCaption = "Users";
    private final String rolesCaption = "Roles";
    private final String booksCaption = "Books";
    private final String categoriesCaption = "Categories";

    @Override
    public void init(VaadinRequest vaadinRequest) {
        float horizontalPanelSize = 0;

        Window window = new Window();

        MenuBar menuBar = new MenuBar();
        HorizontalLayout horizontalPanelForButtons = new HorizontalLayout();

        horizontalPanelForButtons.setStyleName(ValoTheme.LAYOUT_WELL);

        createAndShowAllItemsPanel.setWidth(100f, Unit.PERCENTAGE);
        createAndShowAllItemsPanel.setHeight(100f, Unit.PERCENTAGE);
        createAndShowAllItemsPanel.setSplitPosition(30f, Unit.PERCENTAGE);

        globalPanel.addComponent(horizontalPanelForButtons);
        globalPanel.setComponentAlignment(horizontalPanelForButtons, Alignment.TOP_RIGHT);

        globalPanel.addComponent(menuBar);
        globalPanel.setComponentAlignment(menuBar, Alignment.TOP_CENTER);

        logConfigButtonBase.setStyleName(ValoTheme.BUTTON_LINK);
        horizontalPanelForButtons.addComponent(logConfigButtonBase);
        horizontalPanelSize += logConfigButtonBase.getWidth();

        logoutButtonBase.setStyleName(ValoTheme.BUTTON_LINK);
        horizontalPanelForButtons.addComponent(logoutButtonBase);
        horizontalPanelSize += logoutButtonBase.getWidth();

        horizontalPanelForButtons.setWidth(horizontalPanelSize, Unit.PIXELS);

        menuBar.setStyleName(ValoTheme.MENUBAR_BORDERLESS);
        menuBar.setWidth(100f, Unit.PERCENTAGE);

        MenuBar.MenuItem usersMenuItem = menuBar.addItem("Users",
                (selectedMenuItem) -> {
                    createLeftPanelForUsersMenu();
                    createRightPanelForUsersMenu();
                });
        MenuBar.MenuItem rolesMenuItem = menuBar.addItem("Roles",
                (selectedMenuItem) -> {
                    createLeftPanelForRolesMenu();
                    createRightPanelForRolesMenu();
                });
        MenuBar.MenuItem booksMenuItem = menuBar.addItem("Books",
                (selectedMenuItem) -> {
                    createLeftPanelForBooksMenu();
                    createRightPanelForBooksMenu();
                });
        MenuBar.MenuItem categoriesMenuItem = menuBar.addItem("Categories",
                (selectedMenuItem) -> {
                    createLeftPanelForCategoriesMenu();
                    createRightPanelForCategoriesMenu();
                });

        createAndShowAllItemsPanel.removeAllComponents();
        createAndShowAllItemsPanel.addComponent(leftPanel);
        createAndShowAllItemsPanel.addComponent(rightPanel);

        leftPanel.setDefaultComponentAlignment(Alignment.TOP_LEFT);
        rightPanel.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        leftPanel.addLayoutClickListener(event -> {
            if (event.getMouseEventDetails().isDoubleClick()) {
                Component clickedComponent = event.getClickedComponent();
                if (ListSelect.class.equals(event.getClickedComponent().getClass()) && ((ListSelect) clickedComponent).getSelectedItems().size() == 1) {
                    switch (clickedComponent.getCaption()) {
                        case usersCaption: {
                            selectedUserDoubleClick(((ListSelect<String>) clickedComponent).getSelectedItems().iterator().next());
                            break;
                        }
                        case rolesCaption: {
                            selectedRoleDoubleClick(((ListSelect<String>) clickedComponent).getSelectedItems().iterator().next());
                            break;
                        }
                        case booksCaption: {
                            selectedBookDoubleClick(((ListSelect<Book>) clickedComponent).getSelectedItems().iterator().next());
                            break;
                        }
                        case categoriesCaption: {
                            selectedCategoryDoubleClick(((ListSelect<String>) clickedComponent).getSelectedItems().iterator().next());
                            break;
                        }
                    }
                }
            }
        });

        window.setContent(globalPanel);
        window.setSizeFull();
        window.setResizable(false);
        window.setClosable(false);
        addWindow(window);
    }

    private void cleanLeftAndRightPanel() {
        leftPanel.removeAllComponents();
        rightPanel.removeAllComponents();
    }

    private void logoutButtonClicked(Button.ClickEvent e) {
        SecurityContextHolder.clearContext();
        //redirect to login page
        getPage().setLocation("/main?logout");
    }

    private void logConfigurationButtonClicked(Button.ClickEvent e) {
        //redirect to log configuration page
        getPage().setLocation("/logConfigurator");
    }

    private void addCreateAndShowAllItemsPanelOnGlobalPanel() {
        if (globalPanel.getComponentIndex(createAndShowAllItemsPanel) == -1) {
            globalPanel.addComponent(createAndShowAllItemsPanel);
        }
    }

    private void createLeftPanelForUsersMenu() {
        ListSelect<String> listWithUsers = new ListSelect<>(usersCaption);
        listWithUsers.setWidth(100f, Unit.PERCENTAGE);
        listWithUsers.setHeight(100f, Unit.PERCENTAGE);

        listWithUsers.setItems(userService.getAllUserNames());

        VerticalLayout panelWithButtons = new VerticalLayout();
        panelWithButtons.setWidth(100f, Unit.PERCENTAGE);

        Button newUserButton = new Button("Create user", this::createUserButtonClick);
        Button deleteUserButton = new Button("Delete user", this::deleteUserButtonClick);

        panelWithButtons.addComponents(newUserButton, deleteUserButton);

        panelWithButtons.setComponentAlignment(newUserButton, Alignment.TOP_LEFT);
        panelWithButtons.setComponentAlignment(deleteUserButton, Alignment.TOP_LEFT);

        cleanLeftAndRightPanel();
        leftPanel.addComponents(listWithUsers, panelWithButtons);

        addCreateAndShowAllItemsPanelOnGlobalPanel();
    }

    private void createRightPanelForUsersMenu() {
        Label usersCountLabel = new Label("Total count of users: " + userService.countUsers());

        rightPanel.addComponents(usersCountLabel);
    }

    private void selectedUserDoubleClick(String selectedUser) {
        securityService.getAllSessions();
        Window userDetailsWindow = new Window("User details");

        User user = userService.findByUsername(selectedUser);

        FormLayout userDetailsLayout = createLayoutForUserParameters(user, "Save",
                (user1, username, password, roles) -> {

                    Window confirmDialogWindow = new YesNoDialog("Confirmation",
                            "Do you really want to change details for user \"" + user1.getUsername() + "\" with roles \"" +
                                    StringUtils.collectionToDelimitedString(roles, ", ") + "\" ?",
                            resultIsYes -> {
                                if (resultIsYes) {
                                    boolean resultOfOperation = userService.changeUserDetails(user1.getUsername(), usernameField.getValue(),
                                            passwordField.getValue(), roles);

                                    Notification.show("Changes for user \"" + usernameField.getValue() +
                                                    ((resultOfOperation) ? "\" were saved." : " weren't saved."),
                                            Notification.Type.TRAY_NOTIFICATION);
                                }
                            });

                    confirmDialogWindow.center();
                    addWindow(confirmDialogWindow);
                });

        userDetailsWindow.setContent(userDetailsLayout);
        userDetailsWindow.center();
        userDetailsWindow.setModal(true);
        userDetailsWindow.addCloseListener(e1 -> {
            createLeftPanelForUsersMenu();
            createRightPanelForUsersMenu();
        });
        addWindow(userDetailsWindow);
    }

    private void createLeftPanelForRolesMenu() {
        ListSelect<String> listWithRoles = new ListSelect<>(rolesCaption);
        listWithRoles.setWidth(100f, Unit.PERCENTAGE);
        listWithRoles.setHeight(100f, Unit.PERCENTAGE);

        listWithRoles.setItems(roleService.getAllStringRoles());

        VerticalLayout panelWithButtons = new VerticalLayout();
        panelWithButtons.setWidth(100f, Unit.PERCENTAGE);

        Button newUserRoleButton = new Button("Create user role", this::createRoleButtonClick);
        Button deleteUserRoleButton = new Button("Delete user role", this::deleteRoleButtonClick);

        panelWithButtons.addComponents(newUserRoleButton, deleteUserRoleButton);

        panelWithButtons.setComponentAlignment(newUserRoleButton, Alignment.TOP_LEFT);
        panelWithButtons.setComponentAlignment(deleteUserRoleButton, Alignment.TOP_LEFT);

        cleanLeftAndRightPanel();
        leftPanel.addComponents(listWithRoles, panelWithButtons);

        addCreateAndShowAllItemsPanelOnGlobalPanel();
    }

    private void createRightPanelForRolesMenu() {
        Label rolesCountLabel = new Label("Total count of roles: " + roleService.countRoles());

        rightPanel.addComponents(rolesCountLabel);

        Map<UserRole, Integer> setOfUsersByRoles = userService.getCountOfUsersByRoles();

        for (Map.Entry<UserRole, Integer> mapEntry : setOfUsersByRoles.entrySet()) {
            rightPanel.addComponent(new Label("Count of users with \"" + mapEntry.getKey() +
                    "\" role: " + mapEntry.getValue()));
        }
    }

    private void selectedRoleDoubleClick(String selectedRole) {
        Window roleDetailsWindow = new Window("Role details");

        UserRole role = roleService.findByRole(selectedRole);

        FormLayout roleDetailsLayout = createLayoutForRoleParameters(role, "Save",
                (role1, newRoleName, users) -> {

                    Window confirmDialogWindow = new YesNoDialog("Confirmation",
                            "Do you really want to change details for role \"" +
                                    selectedRole + "\" ?",
                            resultIsYes -> {
                                boolean resultOfOperation;

                                if (resultIsYes) {
                                    resultOfOperation = roleService.changeRoleDetails(role1, newRoleName);

                                    int countOfChangedUsers = 0;
                                    if (resultOfOperation) {
                                        countOfChangedUsers = userService.setRoleOnUsers(role1, users);
                                    }

                                    Notification.show("Changes for role \"" + selectedRole +
                                                    ((resultOfOperation) ? "\" were saved. " + "Role deleted from " +
                                                            countOfChangedUsers + " users." : " weren't saved."),
                                            Notification.Type.TRAY_NOTIFICATION);
                                }
                            });

                    confirmDialogWindow.center();
                    addWindow(confirmDialogWindow);
                });

        roleDetailsWindow.setContent(roleDetailsLayout);
        roleDetailsWindow.center();
        roleDetailsWindow.setModal(true);
        roleDetailsWindow.addCloseListener(e1 -> {
            createLeftPanelForRolesMenu();
            createRightPanelForRolesMenu();
        });
        addWindow(roleDetailsWindow);
    }

    private void createLeftPanelForBooksMenu() {
        ListSelect<Book> listWithBooks = new ListSelect<>(booksCaption);
        listWithBooks.setWidth(100f, Unit.PERCENTAGE);
        listWithBooks.setHeight(100f, Unit.PERCENTAGE);

        listWithBooks.setItems(bookService.getAllBooks());

        VerticalLayout panelWithButtons = new VerticalLayout();
        panelWithButtons.setWidth(100f, Unit.PERCENTAGE);

        Button newBookButton = new Button("Create book", this::createBookButtonClick);
        Button deleteBookButton = new Button("Delete book", this::deleteBookButtonClick);

        panelWithButtons.addComponents(newBookButton, deleteBookButton);

        panelWithButtons.setComponentAlignment(newBookButton, Alignment.TOP_LEFT);
        panelWithButtons.setComponentAlignment(deleteBookButton, Alignment.TOP_LEFT);

        cleanLeftAndRightPanel();
        leftPanel.addComponents(listWithBooks, panelWithButtons);

        addCreateAndShowAllItemsPanelOnGlobalPanel();
    }

    private void createRightPanelForBooksMenu() {
        Label booksCountLabel = new Label("Total count of books: " + bookService.countBooks());

        rightPanel.addComponents(booksCountLabel);
    }

    private void selectedBookDoubleClick(@NotNull Book selectedBook) {
        Window bookDetailsWindow = new Window("Book details");

        FormLayout bookDetailsLayout = createLayoutForBookParameters(selectedBook, "Save",
                (book, bookParameters) -> {
                    Window confirmDialogWindow = new YesNoDialog("Confirmation",
                            "Do you really want to change details for book \"" +
                                    book + "\" ?",
                            resultIsYes -> {
                                if (resultIsYes) {
                                    boolean resultOfOperation = bookService.changeBookDetails(book, bookParameters);

                                    Notification.show("Changes for book \"" + book.getBookTitle() + " " +
                                                    StringUtils.collectionToDelimitedString(book.getAuthors(), ", ") +
                                                    ((resultOfOperation) ? "\" were saved." : " weren't saved."),
                                            Notification.Type.TRAY_NOTIFICATION);
                                }
                            });

                    confirmDialogWindow.center();
                    addWindow(confirmDialogWindow);
                });

        bookDetailsWindow.setContent(bookDetailsLayout);
        bookDetailsWindow.center();
        bookDetailsWindow.setModal(true);
        bookDetailsWindow.addCloseListener(e1 -> {
            createLeftPanelForBooksMenu();
            createRightPanelForBooksMenu();
        });
        addWindow(bookDetailsWindow);
    }

    private void createLeftPanelForCategoriesMenu() {
        ListSelect<String> listWithCategories = new ListSelect<>(categoriesCaption);
        listWithCategories.setWidth(100f, Unit.PERCENTAGE);
        listWithCategories.setHeight(100f, Unit.PERCENTAGE);

        listWithCategories.setItems(categoryService.getAllStringCategories());

        VerticalLayout panelWithButtons = new VerticalLayout();
        panelWithButtons.setWidth(100f, Unit.PERCENTAGE);

        Button newBookCategoryButton = new Button("Create book category", this::createCategoryButtonClick);
        Button deleteBookCategoryButton = new Button("Delete book category", this::deleteCategoryButtonClick);

        panelWithButtons.addComponents(newBookCategoryButton, deleteBookCategoryButton);

        panelWithButtons.setComponentAlignment(newBookCategoryButton, Alignment.TOP_LEFT);
        panelWithButtons.setComponentAlignment(deleteBookCategoryButton, Alignment.TOP_LEFT);

        cleanLeftAndRightPanel();
        leftPanel.addComponents(listWithCategories, panelWithButtons);

        addCreateAndShowAllItemsPanelOnGlobalPanel();
    }

    private void createRightPanelForCategoriesMenu() {
        Label categoriesCountLabel = new Label("Total count of categories: " + categoryService.countCategories());

        rightPanel.addComponents(categoriesCountLabel);

        Map<BookCategory, Integer> setOfBooksByCategories = bookService.getCountOfBooksByCategories();

        for (Map.Entry<BookCategory, Integer> mapEntry : setOfBooksByCategories.entrySet()) {
            rightPanel.addComponent(new Label("Count of books with \"" + mapEntry.getKey() +
                    "\" category: " + mapEntry.getValue()));
        }
    }

    private void selectedCategoryDoubleClick(String selectedCategory) {
        Window categoryDetailsWindow = new Window("Category details");

        BookCategory category = categoryService.findByCategory(selectedCategory);

        FormLayout categoryDetailsLayout = createLayoutForCategoryParameters(category, "Save",
                (category1, newCategoryName, books) -> {

                    Window confirmDialogWindow = new YesNoDialog("Confirmation",
                            "Do you really want to change details for category \"" +
                                    selectedCategory + "\" ?",
                            resultIsYes -> {
                                boolean resultOfOperation;

                                if (resultIsYes) {
                                    resultOfOperation = categoryService.changeCategoryDetails(category1, newCategoryName);

                                    int countOfChangedBooks = 0;
                                    if (resultOfOperation) {
                                        countOfChangedBooks = bookService.setCategoryOnBooks(category1, books);
                                    }

                                    Notification.show("Changes for category \"" + selectedCategory +
                                                    ((resultOfOperation) ? "\" were saved. " + "Category deleted from " +
                                                            countOfChangedBooks + " books." : " weren't saved."),
                                            Notification.Type.TRAY_NOTIFICATION);
                                }
                            });

                    confirmDialogWindow.center();
                    addWindow(confirmDialogWindow);
                });

        categoryDetailsWindow.setContent(categoryDetailsLayout);
        categoryDetailsWindow.center();
        categoryDetailsWindow.setModal(true);
        categoryDetailsWindow.addCloseListener(e1 -> {
            createLeftPanelForCategoriesMenu();
            createRightPanelForCategoriesMenu();
        });
        addWindow(categoryDetailsWindow);
    }

    private void createUserButtonClick(Button.ClickEvent e) {
        Window createUserWindow = new Window("Create user");

        FormLayout createUserLayout = createLayoutForUserParameters(null, "Create",
                (user, username, password, roles) -> {
                    boolean resultOfOperation = userService.save(usernameField.getValue(),
                            passwordField.getValue(), roles);

                    Notification.show("User \"" + usernameField.getValue() + "\" with roles \"" +
                                    StringUtils.collectionToDelimitedString(roles, ", ") +
                                    ((resultOfOperation) ? "\" created." : " didn't created."),
                            Notification.Type.TRAY_NOTIFICATION);
                });

        createUserWindow.setContent(createUserLayout);
        createUserWindow.center();
        createUserWindow.setModal(true);
        createUserWindow.addCloseListener(e1 -> {
            createLeftPanelForUsersMenu();
            createRightPanelForUsersMenu();
        });
        addWindow(createUserWindow);
    }

    private void createRoleButtonClick(Button.ClickEvent e) {
        Window window = new Window("Create user role");

        FormLayout createRoleLayout = createLayoutForRoleParameters(null, "Create",
                (role, newRoleName, users) -> {
                    boolean resultOfOperation = roleService.save(newRoleName);

                    int countOfChangedUsers = 0;
                    if (resultOfOperation) {
                        UserRole createdRole = roleService.findByRole(newRoleName);
                        countOfChangedUsers = userService.addRoleToUsers(createdRole, users);
                    }

                    Notification.show("Role \"" + newRoleName +
                                    ((resultOfOperation) ? "\" created." : " didn't created. " + "Role applied to " +
                                            countOfChangedUsers + " users."),
                            Notification.Type.TRAY_NOTIFICATION);
                });

        window.setContent(createRoleLayout);
        window.center();
        window.setModal(true);
        window.addCloseListener(e1 -> {
            createLeftPanelForRolesMenu();
            createRightPanelForRolesMenu();
        });
        addWindow(window);
    }

    private void createBookButtonClick(Button.ClickEvent e) {
        Window window = new Window("Create book");

        FormLayout createBookLayout = createLayoutForBookParameters(null, "Create",
                (book, bookParameters) -> {
                    Boolean resultOfOperation = bookService.save(bookParameters);
                    String bookTitle = (String) bookParameters.get("bookTitle");
                    String authors = StringUtils.collectionToDelimitedString((Set<String>) bookParameters.get("authors"), ", ");

                    Notification.show("Book \"" + bookTitle + " " + authors +
                                    ((resultOfOperation) ? "\" created." : " didn't created."),
                            Notification.Type.TRAY_NOTIFICATION);
                });

        window.setContent(createBookLayout);
        window.setSizeFull();
        window.center();
        window.addCloseListener(e1 -> {
            createLeftPanelForBooksMenu();
            createRightPanelForBooksMenu();
        });
        addWindow(window);
    }

    private void createCategoryButtonClick(Button.ClickEvent e) {
        Window window = new Window("Create book category");

        FormLayout createCategoryLayout = createLayoutForCategoryParameters(null, "Create",
                (category, newCategoryName, books) -> {
                    boolean resultOfOperation = categoryService.save(newCategoryName);

                    int countOfChangedBooks = 0;
                    if (resultOfOperation) {
                        BookCategory createdCategory = categoryService.findByCategory(newCategoryName);
                        countOfChangedBooks = bookService.addCategoryToBooks(createdCategory, books);
                    }

                    Notification.show("Category \"" + newCategoryName +
                                    ((resultOfOperation) ? "\" created." : " didn't created. " + "Category applied to " +
                                            countOfChangedBooks + " books."),
                            Notification.Type.TRAY_NOTIFICATION);
                });

        window.setContent(createCategoryLayout);
        window.center();
        window.setModal(true);
        window.addCloseListener(e1 -> {
            createLeftPanelForCategoriesMenu();
            createRightPanelForCategoriesMenu();
        });
        addWindow(window);
    }

    private void deleteUserButtonClick(Button.ClickEvent e) {
        VerticalLayout panel = (VerticalLayout) createAndShowAllItemsPanel.getFirstComponent();
        ListSelect<String> listWithUsers = (ListSelect<String>) panel.getComponent(0);
        Set<String> selectedItems = listWithUsers.getSelectedItems();

        Window confirmDialogWindow = new YesNoDialog("Confirmation",
                "Do you really want to delete users: " +
                        StringUtils.collectionToDelimitedString(selectedItems, ", "),
                resultIsYes -> {
                    if (resultIsYes) {
                        int countOfDeletedUsers = userService.delete(selectedItems);
                        createLeftPanelForUsersMenu();
                        createRightPanelForUsersMenu();
                        Notification.show(countOfDeletedUsers + " users deleted.",
                                Notification.Type.TRAY_NOTIFICATION);
                    }
                });

        confirmDialogWindow.center();
        addWindow(confirmDialogWindow);
    }

    private void deleteRoleButtonClick(Button.ClickEvent e) {
        VerticalLayout panel = (VerticalLayout) createAndShowAllItemsPanel.getFirstComponent();
        ListSelect<String> listWithUsers = (ListSelect<String>) panel.getComponent(0);
        Set<String> selectedItems = listWithUsers.getSelectedItems();

        Window confirmDialogWindow = new YesNoDialog("Confirmation",
                "Do you really want to delete roles: " +
                        StringUtils.collectionToDelimitedString(selectedItems, ", "),
                resultIsYes -> {
                    if (resultIsYes) {
                        int countOfDeletedUsers = roleService.delete(selectedItems);
                        createLeftPanelForRolesMenu();
                        createRightPanelForRolesMenu();
                        Notification.show(countOfDeletedUsers + " roles deleted",
                                Notification.Type.TRAY_NOTIFICATION);
                    }
                });

        confirmDialogWindow.center();
        addWindow(confirmDialogWindow);
    }

    private void deleteBookButtonClick(Button.ClickEvent e) {
        VerticalLayout panel = (VerticalLayout) createAndShowAllItemsPanel.getFirstComponent();
        ListSelect<Book> listWithBooks = (ListSelect<Book>) panel.getComponent(0);
        Set<Book> selectedItems = listWithBooks.getSelectedItems();

        Window confirmDialogWindow = new YesNoDialog("Confirmation",
                "Do you really want to delete books: " +
                        StringUtils.collectionToDelimitedString(selectedItems, ", "),
                resultIsYes -> {
                    if (resultIsYes) {
                        int countOfDeletedUsers = bookService.delete(selectedItems);
                        createLeftPanelForBooksMenu();
                        createRightPanelForBooksMenu();
                        Notification.show(countOfDeletedUsers + " books deleted",
                                Notification.Type.TRAY_NOTIFICATION);
                    }
                });

        confirmDialogWindow.center();
        addWindow(confirmDialogWindow);
    }

    private void deleteCategoryButtonClick(Button.ClickEvent e) {
        VerticalLayout panel = (VerticalLayout) createAndShowAllItemsPanel.getFirstComponent();
        ListSelect<String> listWithUsers = (ListSelect<String>) panel.getComponent(0);
        Set<String> selectedItems = listWithUsers.getSelectedItems();

        Window confirmDialogWindow = new YesNoDialog("Confirmation",
                "Do you really want to delete categories of books: " +
                        StringUtils.collectionToDelimitedString(selectedItems, ", "),
                resultIsYes -> {
                    if (resultIsYes) {
                        int countOfDeletedUsers = categoryService.delete(selectedItems);
                        createLeftPanelForCategoriesMenu();
                        createRightPanelForCategoriesMenu();
                        Notification.show(countOfDeletedUsers + " categories deleted.",
                                Notification.Type.TRAY_NOTIFICATION);
                    }
                });

        confirmDialogWindow.center();
        addWindow(confirmDialogWindow);
    }

    private FormLayout createLayoutForUserParameters(User user, String buttonCaption, UserButtonClick userButtonClickImpl) {
        FormLayout userDetailsLayout = new FormLayout();

        HorizontalSplitPanel horizontalPanel = new HorizontalSplitPanel();
        VerticalLayout leftPanelUser = new VerticalLayout();
        VerticalLayout rightPanelUser = new VerticalLayout();

        boolean userIsNull = null == user;

        //left panel

        usernameField.setValue((!userIsNull) ? user.getUsername() : "");

        passwordField.setValue("");
        confirmPasswordField.setValue("");

        leftPanelUser.addComponents(usernameField, passwordField, confirmPasswordField);
        leftPanelUser.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        //right panel

        ListSelect<String> listWithRoles = new ListSelect<>();
        listWithRoles.setWidth(100f, Unit.PERCENTAGE);
        listWithRoles.setRequiredIndicatorVisible(true);

        Set<String> roles = (!userIsNull) ? user.getRoles().stream().map(UserRole::getAuthority).collect(Collectors.toSet()) : new HashSet<>();
        ListDataProvider<String> dataProvider = DataProvider.ofCollection(roles);

        listWithRoles.setDataProvider(dataProvider);

        Button actionButton = new Button(buttonCaption);
        actionButton.addClickListener((event) -> {
            boolean resultOfValidation;

            resultOfValidation = ComponentValueValidation.addErrorOnComponent(usernameField,
                    StringUtils.isEmpty(usernameField.getValue()),
                    "Username can't be empty");

            resultOfValidation = resultOfValidation || ComponentValueValidation.addErrorOnComponent(passwordField,
                    StringUtils.isEmpty(passwordField.getValue()),
                    "Password can't be empty");

            resultOfValidation = resultOfValidation || ComponentValueValidation.addErrorOnComponent(listWithRoles,
                    roles.isEmpty(),
                    "Should be presented 1 role at least");

            resultOfValidation = resultOfValidation || ComponentValueValidation.addErrorOnComponent(confirmPasswordField,
                    null != passwordField.getValue() &&
                            !passwordField.getValue().equals(confirmPasswordField.getValue()),
                    "Passwords should be equal");

            if (resultOfValidation) {
                Notification.show("Please check fields filling", Notification.Type.ERROR_MESSAGE);
                return;
            }

            userButtonClickImpl.doAction(user, usernameField.getValue(), passwordField.getValue(), roleService.findByRoles(roles));
        });

        Button addRolesButton = new Button("Add roles");
        Button deleteRolesButton = new Button("Delete roles");

        addRolesButton.addClickListener(event -> {
            Window allRolesWindow = new Window("Choose roles");

            VerticalLayout layout = new VerticalLayout();
            HorizontalLayout horizontalLayout = new HorizontalLayout();

            ListSelect<String> listWithAllRoles = new ListSelect<>();
            listWithAllRoles.setSizeFull();
            listWithAllRoles.setItems(new HashSet<>(roleService.getAllStringRoles()));

            Button addButton = new Button("Add", ev -> {
                if (!listWithAllRoles.getSelectedItems().isEmpty()) {
                    roles.addAll(listWithAllRoles.getSelectedItems());
                    dataProvider.refreshAll();

                    allRolesWindow.close();
                }
            });

            Button cancelButton = new Button("Cancel", ev -> allRolesWindow.close());

            horizontalLayout.addComponents(addButton, cancelButton);

            layout.addComponents(listWithAllRoles, horizontalLayout);
            layout.setDefaultComponentAlignment(Alignment.TOP_LEFT);

            allRolesWindow.setContent(layout);
            allRolesWindow.center();
            allRolesWindow.setModal(true);
            addWindow(allRolesWindow);
        });

        deleteRolesButton.addClickListener(event -> {
            Set<String> setWithSelectedRoles = listWithRoles.getSelectedItems();
            if (!setWithSelectedRoles.isEmpty()) {
                roles.removeAll(setWithSelectedRoles);
                dataProvider.refreshAll();
            }
        });

        rightPanelUser.addComponents(new Label(rolesCaption), listWithRoles, addRolesButton, deleteRolesButton);
        rightPanelUser.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        horizontalPanel.addComponents(leftPanelUser, rightPanelUser);

        userDetailsLayout.addComponents(horizontalPanel, actionButton);

        return userDetailsLayout;
    }

    private FormLayout createLayoutForRoleParameters(UserRole role, String buttonCaption, RoleButtonClick roleButtonClickImpl) {
        FormLayout roleDetailsLayout = new FormLayout();

        HorizontalSplitPanel horizontalPanel = new HorizontalSplitPanel();
        VerticalLayout leftPanelRole = new VerticalLayout();
        VerticalLayout rightPanelRole = new VerticalLayout();

        boolean isRoleNull = null == role;

        //left panel

        TextField roleNameTextField = new TextField("Role name");
        roleNameTextField.setWidth(100f, Unit.PERCENTAGE);
        roleNameTextField.setValue((!isRoleNull) ? role.getAuthority() : "");

        leftPanelRole.addComponent(roleNameTextField);
        leftPanelRole.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        //right panel

        ListSelect<String> listWithUsers = new ListSelect<>();
        listWithUsers.setWidth(100f, Unit.PERCENTAGE);
        Set<String> users = (!isRoleNull) ? userService.findUsersByRolesContains(role).stream().map(User::getUsername).collect(Collectors.toSet())
                : new HashSet<>();

        ListDataProvider<String> dataProvider = DataProvider.ofCollection(users);
        listWithUsers.setDataProvider(dataProvider);

        Button actionButton = new Button(buttonCaption);

        actionButton.addClickListener(event -> {
            boolean resultOfValidation = ComponentValueValidation.addErrorOnComponent(roleNameTextField,
                    StringUtils.isEmpty(roleNameTextField.getValue()),
                    "Name of role can't be empty");

            if (resultOfValidation) {
                Notification.show("Please check fields filling", Notification.Type.ERROR_MESSAGE);
                return;
            }

            roleButtonClickImpl.doAction(role, roleNameTextField.getValue(), userService.findByUserNames(users));
        });

        Button addUsersButton = new Button("Add users");
        Button deleteUsersButton = new Button("Delete users");

        addUsersButton.addClickListener(event -> {
            Window allUsersWindow = new Window("Choose users");

            VerticalLayout layout = new VerticalLayout();
            HorizontalLayout horizontalLayout = new HorizontalLayout();

            ListSelect<String> listWithAllUsers = new ListSelect<>();
            listWithAllUsers.setSizeFull();
            listWithAllUsers.setItems(new HashSet<>(userService.getAllUserNames()));

            Button addButton = new Button("Add", ev -> {
                if (!listWithAllUsers.getSelectedItems().isEmpty()) {
                    users.addAll(listWithAllUsers.getSelectedItems());
                    dataProvider.refreshAll();

                    allUsersWindow.close();
                }
            });

            Button cancelButton = new Button("Cancel", ev -> allUsersWindow.close());

            horizontalLayout.addComponents(addButton, cancelButton);

            layout.addComponents(listWithAllUsers, horizontalLayout);
            layout.setDefaultComponentAlignment(Alignment.TOP_LEFT);

            allUsersWindow.setContent(layout);
            allUsersWindow.center();
            allUsersWindow.setModal(true);
            addWindow(allUsersWindow);
        });

        deleteUsersButton.addClickListener(event -> {
            Set<String> setWithSelectedCategories = listWithUsers.getSelectedItems();
            if (!setWithSelectedCategories.isEmpty()) {
                users.removeAll(setWithSelectedCategories);
                dataProvider.refreshAll();
            }
        });

        rightPanelRole.addComponents(new Label(usersCaption), listWithUsers, addUsersButton, deleteUsersButton);
        rightPanelRole.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        horizontalPanel.addComponents(leftPanelRole, rightPanelRole);

        roleDetailsLayout.addComponents(horizontalPanel, actionButton);

        return roleDetailsLayout;
    }

    private FormLayout createLayoutForBookParameters(Book book, String buttonCaption, BookButtonClick bookButtonClickImpl) {
        FormLayout bookDetailsLayout = new FormLayout();
        HorizontalSplitPanel createBookSplitLayout = new HorizontalSplitPanel();
        createBookSplitLayout.setHeight(100f, Unit.PERCENTAGE);
        createBookSplitLayout.setWidth(100f, Unit.PERCENTAGE);

        VerticalLayout leftPanel = new VerticalLayout();
        leftPanel.setHeight(100f, Unit.PERCENTAGE);
        leftPanel.setWidth(100f, Unit.PERCENTAGE);

        VerticalLayout rightPanelCreateBook = new VerticalLayout();
        rightPanelCreateBook.setHeight(100f, Unit.PERCENTAGE);
        rightPanelCreateBook.setWidth(100f, Unit.PERCENTAGE);

        boolean bookIsNull = null == book;

        //left panel

        leftPanel.addComponent(new Label("Book parameters"));

        TextField bookTitleTextField = new TextField("Title");
        bookTitleTextField.setWidth(100f, Unit.PERCENTAGE);
        bookTitleTextField.setRequiredIndicatorVisible(true);
        if (!bookIsNull) bookTitleTextField.setValue(book.getBookTitle());

        TextField authorsTextField = new TextField("Authors");
        authorsTextField.setWidth(100f, Unit.PERCENTAGE);
        authorsTextField.setRequiredIndicatorVisible(true);
        if (!bookIsNull) authorsTextField.setValue(StringUtils.collectionToDelimitedString(book.getAuthors(), ", "));

        TextArea descriptionTextArea = new TextArea("Description");
        descriptionTextArea.setWidth(100f, Unit.PERCENTAGE);
        descriptionTextArea.setValue((!bookIsNull && !StringUtils.isEmpty(book.getDescription())) ? book.getDescription() : "");

        TextField numberOfPagesTextField = new TextField("Number of pages");
        numberOfPagesTextField.setWidth(100f, Unit.PERCENTAGE);
        if (!bookIsNull) numberOfPagesTextField.setValue(book.getNumberOfPages().toString());

        TextField publishingHouseTextField = new TextField("Publishing house");
        publishingHouseTextField.setWidth(100f, Unit.PERCENTAGE);
        if (!bookIsNull && !StringUtils.isEmpty(book.getPublishingHouse()))
            publishingHouseTextField.setValue(book.getPublishingHouse());

        TextField priceTextField = new TextField("Price");
        priceTextField.setWidth(100f, Unit.PERCENTAGE);

        TextField numberOfCopiesTextField = new TextField("Number of copies");
        numberOfCopiesTextField.setWidth(100f, Unit.PERCENTAGE);

        DateField yearDateField = new DateField("Year") {
            @Override
            protected Result<LocalDate> handleUnparsableDateString(
                    String dateString) {
                try {
                    // try to parse with alternative format
                    LocalDate parsedAtServer = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy"));
                    return Result.ok(parsedAtServer);
                } catch (DateTimeParseException e) {
                    return Result.error("Bad date");
                }
            }
        };

        yearDateField.setResolution(DateResolution.YEAR);
        yearDateField.setTextFieldEnabled(true);
        yearDateField.setDateFormat("yyyy");
        yearDateField.setWidth(100f, Unit.PERCENTAGE);
        if (!bookIsNull)
            yearDateField.setValue((null == book.getYear()) ? LocalDate.now() : LocalDate.of(book.getYear(), 1, 1));

        DateField addingDayDateField = new DateField("Adding day");

        addingDayDateField.setResolution(DateResolution.DAY);
        addingDayDateField.setEnabled(false);
        addingDayDateField.setDateFormat("yyyy-MM-dd");
        addingDayDateField.setWidth(100f, Unit.PERCENTAGE);

        if (!bookIsNull)
            addingDayDateField.setValue((null == book.getAddingDay()) ? LocalDate.now() : book.getAddingDay());

        ImageUploader pictureOfBookCoverImageUploader = new ImageUploader();
        pictureOfBookCoverImageUploader.setWidth(100f, Unit.PERCENTAGE);
        if (!bookIsNull && null != book.getPictureOfBookCover() &&
                book.getPictureOfBookCover().isPresented()) {
            pictureOfBookCoverImageUploader.setOutputStreamForImage(book.getPictureOfBookCover().getPictureOfBookCover());
            pictureOfBookCoverImageUploader.resetProgressbar();
            pictureOfBookCoverImageUploader.showImage();
        }

        ComponentValueValidation.validate(bookTitleTextField,
                new StringLengthValidator("Title can't be empty", 1, 6000), conversionService, String.class);
        ComponentValueValidation.validate(authorsTextField,
                new StringLengthValidator("Authors can't be empty", 1, 6000), conversionService, String.class);
        ComponentValueValidation.validate(priceTextField,
                new BigDecimalRangeValidator("Value should have positive decimal format from 0 to 2000000",
                        BigDecimal.valueOf(0l), BigDecimal.valueOf(2000000l)), conversionService, BigDecimal.class);

        leftPanel.addComponents(bookTitleTextField, authorsTextField, descriptionTextArea,
                numberOfPagesTextField, yearDateField, publishingHouseTextField, priceTextField,
                numberOfCopiesTextField, addingDayDateField, pictureOfBookCoverImageUploader);
        leftPanel.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        //right panel

        ListSelect<String> listWithCategories = new ListSelect<>();
        listWithCategories.setWidth(100f, Unit.PERCENTAGE);
        listWithCategories.setHeight(100f, Unit.PERCENTAGE);
        listWithCategories.setRequiredIndicatorVisible(true);

        Set<String> categories = (!bookIsNull) ? book.getCategories().stream().map(BookCategory::getCategory).collect(Collectors.toSet()) :
                new HashSet<>();

        ListDataProvider<String> dataProvider = DataProvider.ofCollection(categories);
        listWithCategories.setDataProvider(dataProvider);

        Button addCategoriesButton = new Button("Add categories");
        Button deleteCategoriesButton = new Button("Delete categories");

        addCategoriesButton.addClickListener(event -> {
            Window allCategoriesWindow = new Window("Choose categories");

            VerticalLayout layout = new VerticalLayout();
            HorizontalLayout horizontalLayout = new HorizontalLayout();

            ListSelect<String> listWithAllCategories = new ListSelect<>();
            listWithAllCategories.setSizeFull();
            listWithAllCategories.setItems(new HashSet<>(categoryService.getAllStringCategories()));

            Button addButton = new Button("Add", ev -> {
                if (!listWithAllCategories.getSelectedItems().isEmpty()) {
                    categories.addAll(listWithAllCategories.getSelectedItems());
                    dataProvider.refreshAll();
                    allCategoriesWindow.close();
                }
            });

            Button cancelButton = new Button("Cancel", ev -> allCategoriesWindow.close());

            horizontalLayout.addComponents(addButton, cancelButton);

            layout.addComponents(listWithAllCategories, horizontalLayout);
            layout.setDefaultComponentAlignment(Alignment.TOP_LEFT);

            allCategoriesWindow.setContent(layout);
            allCategoriesWindow.center();
            allCategoriesWindow.setModal(true);
            addWindow(allCategoriesWindow);
        });

        deleteCategoriesButton.addClickListener(event -> {
            Set<String> setWithSelectedCategories = listWithCategories.getSelectedItems();
            if (!setWithSelectedCategories.isEmpty()) {
                categories.removeAll(setWithSelectedCategories);
                dataProvider.refreshAll();
            }
        });

        rightPanelCreateBook.addComponents(new Label("Choose book categories"), listWithCategories,
                addCategoriesButton, deleteCategoriesButton);
        rightPanelCreateBook.setHeight(100f, Unit.PERCENTAGE);

        Button actionButton = new Button(buttonCaption);

        actionButton.addClickListener(event -> {
            boolean resultOfValidation;

            resultOfValidation = ComponentValueValidation.addErrorOnComponent(bookTitleTextField,
                    bookTitleTextField.getValue().isEmpty(),
                    "Title can't be empty");

            resultOfValidation = resultOfValidation || ComponentValueValidation.addErrorOnComponent(bookTitleTextField,
                    null != bookTitleTextField.getComponentError(),
                    null != bookTitleTextField.getComponentError() ? bookTitleTextField.getComponentError().getFormattedHtmlMessage() : "");

            resultOfValidation = resultOfValidation || ComponentValueValidation.addErrorOnComponent(authorsTextField,
                    authorsTextField.getValue().isEmpty(),
                    "Authors can't be empty");

            resultOfValidation = resultOfValidation || ComponentValueValidation.addErrorOnComponent(authorsTextField,
                    null != authorsTextField.getComponentError(),
                    null != authorsTextField.getComponentError() ? authorsTextField.getComponentError().getFormattedHtmlMessage() : "");

            resultOfValidation = resultOfValidation || ComponentValueValidation.addErrorOnComponent(listWithCategories,
                    categories.isEmpty(),
                    "Should be presented 1 category at least");

            if (resultOfValidation) {
                Notification.show("Please, check fields filling", Notification.Type.ERROR_MESSAGE);
                return;
            }

            Map<String, Object> bookParameters = new HashMap<>();

            bookParameters.put("bookTitle", bookTitleTextField.getValue());
            bookParameters.put("authors", Arrays.stream(authorsTextField.getValue().split(",")).map(item -> item.trim()).collect(Collectors.toSet()));
            bookParameters.put("description", descriptionTextArea.getValue());
            bookParameters.put("categories", categoryService.findByCategories(categories));
            bookParameters.put("numberOfPages", numberOfPagesTextField.getValue().isEmpty() ? 0 : conversionService.convert(numberOfPagesTextField.getValue(), Integer.class));
            bookParameters.put("year", (null != yearDateField.getValue()) ? (short) yearDateField.getValue().getYear() : (short) 0);
            bookParameters.put("publishingHouse", publishingHouseTextField.getValue());
            bookParameters.put("price", priceTextField.getValue().isEmpty() ? BigDecimal.ZERO : conversionService.convert(priceTextField.getValue(), BigDecimal.class));
            bookParameters.put("numberOfCopies", numberOfCopiesTextField.getValue().isEmpty() ? 0 : conversionService.convert(numberOfCopiesTextField.getValue(), Integer.class));

            if (pictureOfBookCoverImageUploader.getOutputStreamForImage().size() > 0) {
                bookParameters.put("pictureOfBookCover", pictureOfBookCoverImageUploader.getOutputStreamForImage().toByteArray());
            }

            bookButtonClickImpl.doAction(book, bookParameters);
        });

        createBookSplitLayout.addComponents(leftPanel, rightPanelCreateBook);

        bookDetailsLayout.addComponents(createBookSplitLayout, actionButton);

        return bookDetailsLayout;
    }

    private FormLayout createLayoutForCategoryParameters(BookCategory category, String buttonCaption, CategoryButtonClick categoryButtonClickImpl) {
        FormLayout categoryDetailsLayout = new FormLayout();

        HorizontalSplitPanel horizontalPanel = new HorizontalSplitPanel();
        VerticalLayout leftPanelRole = new VerticalLayout();
        VerticalLayout rightPanelRole = new VerticalLayout();

        boolean isCategoryNull = null == category;

        //left panel

        TextField categoryNameTextField = new TextField("Category name");
        categoryNameTextField.setWidth(100f, Unit.PERCENTAGE);
        categoryNameTextField.setValue((!isCategoryNull) ? category.getCategory() : "");

        leftPanelRole.addComponent(categoryNameTextField);
        leftPanelRole.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        //right panel

        ListSelect<Book> listWithBooks = new ListSelect<>();
        listWithBooks.setWidth(100f, Unit.PERCENTAGE);
        Set<Book> books = (!isCategoryNull) ? bookService.findBooksByCategoriesContains(category) : new HashSet<>();

        ListDataProvider<Book> dataProvider = DataProvider.ofCollection(books);
        listWithBooks.setDataProvider(dataProvider);

        Button actionButton = new Button(buttonCaption);

        actionButton.addClickListener(event -> {
            boolean resultOfValidation = ComponentValueValidation.addErrorOnComponent(categoryNameTextField,
                    StringUtils.isEmpty(categoryNameTextField.getValue()),
                    "Name of category can't be empty");

            if (resultOfValidation) {
                Notification.show("Please check fields filling", Notification.Type.ERROR_MESSAGE);
                return;
            }

            categoryButtonClickImpl.doAction(category, categoryNameTextField.getValue(), books);
        });

        Button addBooksButton = new Button("Add books");
        Button deleteBooksButton = new Button("Delete books");

        addBooksButton.addClickListener(event -> {
            Window allBooksWindow = new Window("Choose books");

            VerticalLayout layout = new VerticalLayout();
            HorizontalLayout horizontalLayout = new HorizontalLayout();

            ListSelect<Book> listWithAllBooks = new ListSelect<>();
            listWithAllBooks.setSizeFull();
            listWithAllBooks.setItems(new HashSet<>(bookService.getAllBooks()));

            Button addButton = new Button("Add", ev -> {
                if (!listWithAllBooks.getSelectedItems().isEmpty()) {
                    books.addAll(listWithAllBooks.getSelectedItems());
                    dataProvider.refreshAll();

                    allBooksWindow.close();
                }
            });

            Button cancelButton = new Button("Cancel", ev -> allBooksWindow.close());

            horizontalLayout.addComponents(addButton, cancelButton);

            layout.addComponents(listWithAllBooks, horizontalLayout);
            layout.setDefaultComponentAlignment(Alignment.TOP_LEFT);

            allBooksWindow.setContent(layout);
            allBooksWindow.center();
            allBooksWindow.setModal(true);
            addWindow(allBooksWindow);
        });

        deleteBooksButton.addClickListener(event -> {
            Set<Book> setWithSelectedCategories = listWithBooks.getSelectedItems();
            if (!setWithSelectedCategories.isEmpty()) {
                books.removeAll(setWithSelectedCategories);
                dataProvider.refreshAll();
            }
        });

        rightPanelRole.addComponents(new Label(booksCaption), listWithBooks, addBooksButton, deleteBooksButton);
        rightPanelRole.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        horizontalPanel.addComponents(leftPanelRole, rightPanelRole);

        categoryDetailsLayout.addComponents(horizontalPanel, actionButton);

        return categoryDetailsLayout;
    }

    private interface BookButtonClick {
        void doAction(Book book, Map<String, Object> bookParameters);
    }

    private interface UserButtonClick {
        void doAction(User user, String username, String password, Set<UserRole> roles);
    }

    private interface RoleButtonClick {
        void doAction(UserRole role, String newRoleName, Set<User> users);
    }

    private interface CategoryButtonClick {
        void doAction(BookCategory category, String newCategoryName, Set<Book> books);
    }
}
