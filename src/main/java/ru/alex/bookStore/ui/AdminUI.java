package ru.alex.bookStore.ui;

import com.vaadin.data.Result;
import com.vaadin.data.validator.*;
import com.vaadin.server.StreamResource;
import com.vaadin.server.UserError;
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
import ru.alex.bookStore.utils.ComponentValueValidation;
import ru.alex.bookStore.utils.book.BookService;
import ru.alex.bookStore.utils.bookCategory.BookCategoryService;
import ru.alex.bookStore.utils.roles.RoleService;
import ru.alex.bookStore.utils.ui.ImageUploader;
import ru.alex.bookStore.utils.ui.YesNoDialog;
import ru.alex.bookStore.utils.users.UserService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

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
    @Autowired
    ConversionService conversionService;

    Button logoutButtonBase = new Button("Logout", this::logoutButtonClicked);
    VerticalLayout globalPanel = new VerticalLayout();
    HorizontalSplitPanel createAndShowAllItemsPanel = new HorizontalSplitPanel();
    VerticalLayout leftPanel = new VerticalLayout();
    VerticalLayout rightPanel = new VerticalLayout();
    //Panel rightTopPanelOnRightPanel = new Panel();
    //FormLayout rightTopPanelOnRightPanelComponents = new FormLayout();

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

        logoutButtonBase.setStyleName(ValoTheme.BUTTON_LINK);
        horizontalPanelForButtons.addComponent(logoutButtonBase);
        horizontalPanelSize += logoutButtonBase.getWidth();

        horizontalPanelForButtons.setWidth(horizontalPanelSize, Unit.PIXELS);

        menuBar.setStyleName(ValoTheme.MENUBAR_BORDERLESS);
        menuBar.setWidth(100f, Unit.PERCENTAGE);

        MenuBar.MenuItem usersMenuItem = menuBar.addItem("Users",
                (selectedMenuItem) -> createLeftPanelForUserMenu());
        MenuBar.MenuItem rolesOfUsersMenuItem = menuBar.addItem("Roles of users",
                (selectedMenuItem) -> createLeftPanelForRolesOfUsersMenu());
        MenuBar.MenuItem booksMenuItem = menuBar.addItem("Books",
                (selectedMenuItem) -> createLeftPanelForBooksMenu());
        MenuBar.MenuItem categoriesOfBooksMenuItem = menuBar.addItem("Categories of books",
                (selectedMenuItem) -> createLeftPanelForCategoriesOfBooksMenu());

        //rightTopPanelOnRightPanelComponents.setHeight("600px");
        //rightTopPanelOnRightPanelComponents.setStyleName(ValoTheme.LAYOUT_WELL);

        createAndShowAllItemsPanel.removeAllComponents();
        createAndShowAllItemsPanel.addComponent(leftPanel);
        createAndShowAllItemsPanel.addComponent(rightPanel);

        window.setContent(globalPanel);
        window.setSizeFull();
        window.setResizable(false);
        window.setClosable(false);
        addWindow(window);
    }

    private void cleanLeftAndRightPanel() {
        leftPanel.removeAllComponents();
        rightPanel.removeAllComponents();

        //rightTopPanelOnRightPanelComponents.removeAllComponents();
        //rightPanel.addComponent(rightTopPanelOnRightPanelComponents);
    }

    private void logoutButtonClicked(Button.ClickEvent e) {
        SecurityContextHolder.clearContext();
        //redirect to login page
        getPage().setLocation("/main?logout");
    }

    private void addCreateAndShowAllItemsPanelOnGlobalPanel() {
        if (globalPanel.getComponentIndex(createAndShowAllItemsPanel) == -1) {
            globalPanel.addComponent(createAndShowAllItemsPanel);
        }
    }

    private void createLeftPanelForUserMenu() {
        Label usersLabel = new Label("Users");

        ListSelect<String> listWithUsers = new ListSelect<>();
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
        leftPanel.addComponents(usersLabel, listWithUsers, panelWithButtons);
        leftPanel.setComponentAlignment(usersLabel, Alignment.TOP_LEFT);
        leftPanel.setComponentAlignment(panelWithButtons, Alignment.TOP_CENTER);

        leftPanel.addLayoutClickListener(event -> {
            if (event.getMouseEventDetails().isDoubleClick()) {
                Component clickedComponent = event.getClickedComponent();
                if (ListSelect.class.equals(event.getClickedComponent().getClass()) && ((ListSelect)clickedComponent).getSelectedItems().size() == 1) {
                    selectedUserDoubleClick(((ListSelect<String>)clickedComponent).getSelectedItems().iterator().next());
                }
            }
        });

        addCreateAndShowAllItemsPanelOnGlobalPanel();
    }

    private void selectedUserDoubleClick(String selectedUser) {
        Window userDetailsWindow = new Window("User details");

        FormLayout userDetailsLayout = new FormLayout();

        VerticalLayout userDetailsPanel = new VerticalLayout();
        userDetailsPanel.setHeight(100f, Unit.PERCENTAGE);
        userDetailsPanel.setWidth(100f, Unit.PERCENTAGE);

        ListSelect<String> listWithRoles = new ListSelect<>();

        Button saveUserDetailsButton = new Button("Save");
        saveUserDetailsButton.addClickListener((event) -> {

            Set<String> selectedRoles = listWithRoles.getSelectedItems();

            if (selectedRoles.isEmpty()) {
                Notification.show("Should be selected 1 role at least", Notification.Type.ERROR_MESSAGE);
                return;
            }

            AtomicBoolean resultOfOperation = new AtomicBoolean(false);

            if (null != passwordField.getValue() &&
                    passwordField.getValue().equals(confirmPasswordField.getValue())) {

                Window confirmDialogWindow = new YesNoDialog("Confirmation",
                        "Do you really want to change user details: " +
                                StringUtils.collectionToDelimitedString(selectedRoles, ", "),
                        resultIsYes -> {
                            if (resultIsYes) {
                                resultOfOperation.set(userService.changeUserDetails(selectedUser, usernameField.getValue(),
                                        passwordField.getValue(), roleService.findByRoles(selectedRoles)));
                            }
                        });

                confirmDialogWindow.center();
                addWindow(confirmDialogWindow);
            }

            Notification.show("Changes for user \"" + usernameField.getValue() +
                            ((resultOfOperation.get()) ? "\" were saved." : " weren't saved."),
                    Notification.Type.TRAY_NOTIFICATION);
        });

        userDetailsPanel.addComponent(new Label("User credentials"));

        usernameField.setValue("");
        passwordField.setValue("");
        confirmPasswordField.setValue("");

        userDetailsPanel.addComponents(usernameField, passwordField, confirmPasswordField, saveUserDetailsButton);
        userDetailsPanel.setHeight(100f, Unit.PERCENTAGE);

        listWithRoles.setWidth(100f, Unit.PERCENTAGE);
        listWithRoles.setHeight(100f, Unit.PERCENTAGE);

        listWithRoles.setItems(roleService.getAllStringRoles());

        User user = userService.findByUsername(selectedUser);
        for (UserRole role: user.getRoles()) {
            listWithRoles.select(role.getAuthority());
        }

        userDetailsLayout.addComponents(userDetailsPanel, new Label("Roles"), listWithRoles);
        userDetailsLayout.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        userDetailsWindow.setContent(userDetailsLayout);
        userDetailsWindow.center();
        userDetailsWindow.setModal(true);
        userDetailsWindow.addCloseListener(e1 -> createLeftPanelForUserMenu());
        addWindow(userDetailsWindow);
    }

    private void createLeftPanelForRolesOfUsersMenu() {
        Label rolesLabel = new Label("Roles");

        ListSelect<String> listWithRoles = new ListSelect<>();
        listWithRoles.setWidth(100f, Unit.PERCENTAGE);
        listWithRoles.setHeight(100f, Unit.PERCENTAGE);

        listWithRoles.setItems(roleService.getAllStringRoles());

        VerticalLayout panelWithButtons = new VerticalLayout();
        panelWithButtons.setWidth(100f, Unit.PERCENTAGE);

        Button newUserRoleButton = new Button("Create user role", this::createUserRoleButtonClick);
        Button deleteUserRoleButton = new Button("Delete user role", this::deleteUserRoleButtonClick);

        panelWithButtons.addComponents(newUserRoleButton, deleteUserRoleButton);

        panelWithButtons.setComponentAlignment(newUserRoleButton, Alignment.TOP_LEFT);
        panelWithButtons.setComponentAlignment(deleteUserRoleButton, Alignment.TOP_LEFT);

        cleanLeftAndRightPanel();
        leftPanel.addComponents(rolesLabel, listWithRoles, panelWithButtons);
        leftPanel.setComponentAlignment(rolesLabel, Alignment.TOP_LEFT);
        leftPanel.setComponentAlignment(panelWithButtons, Alignment.TOP_CENTER);

        leftPanel.addLayoutClickListener(event -> {
            if (event.getMouseEventDetails().isDoubleClick()) {
                Component clickedComponent = event.getClickedComponent();
                if (ListSelect.class.equals(event.getClickedComponent().getClass()) && ((ListSelect)clickedComponent).getSelectedItems().size() == 1) {
                    selectedRoleDoubleClick(((ListSelect<String>)clickedComponent).getSelectedItems().iterator().next());
                }
            }
        });

        addCreateAndShowAllItemsPanelOnGlobalPanel();
    }

    private void selectedRoleDoubleClick(String selectedRole) {
        Window roleDetailsWindow = new Window("Role details");

        FormLayout roleDetailsLayout = new FormLayout();

        TextField roleNameTextField = new TextField("Role name");

        ListSelect<String> usersForSelectedRole = new ListSelect<>();
        usersForSelectedRole.setWidth(100f, Unit.PERCENTAGE);

        Label usersLabel = new Label("Users");
        Set<User> users = roleService.getUsersByRole(selectedRole);
        usersForSelectedRole.setItems(users.stream().map(User::getUsername).collect(Collectors.toList()));

        Button saveRoleDetailsButton = new Button("Save");

        saveRoleDetailsButton.addClickListener(event -> {
            AtomicBoolean resultOfOperation = new AtomicBoolean(false);

            if (!StringUtils.isEmpty(roleNameTextField.getValue())) {

                Window confirmDialogWindow = new YesNoDialog("Confirmation",
                        "Do you really want to change role details: " +
                                selectedRole,
                        resultIsYes -> {
                            if (resultIsYes) {
                                Set<User> usersWithRole = userService.findByUserNames(usersForSelectedRole.getSelectedItems());
                                resultOfOperation.set(
                                        roleService.changeRoleDetails(selectedRole, roleNameTextField.getValue(), usersWithRole));
                            }
                        });

                confirmDialogWindow.center();
                addWindow(confirmDialogWindow);
            }

            Notification.show("Changes for role \"" + selectedRole +
                            ((resultOfOperation.get()) ? "\" were saved." : " weren't saved."),
                    Notification.Type.TRAY_NOTIFICATION);
        });

        roleDetailsLayout.addComponents(roleNameTextField, usersLabel, usersForSelectedRole, saveRoleDetailsButton);
        roleDetailsLayout.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        roleDetailsWindow.setContent(roleDetailsLayout);
        roleDetailsWindow.center();
        roleDetailsWindow.setModal(true);
        roleDetailsWindow.addCloseListener(e1 -> createLeftPanelForRolesOfUsersMenu());
        addWindow(roleDetailsWindow);
    }

    private void createLeftPanelForBooksMenu() {
        Label booksLabel = new Label("Books");

        ListSelect<Book> listWithBooks = new ListSelect<>();
        listWithBooks.setWidth(100f, Unit.PERCENTAGE);
        listWithBooks.setHeight(100f, Unit.PERCENTAGE);

        listWithBooks.addSelectionListener(event -> {
            Set<Book> selected = event.getAllSelectedItems();
            //Notification.show(selected.size() + " books selected.", Notification.Type.TRAY_NOTIFICATION);
            if (selected.size() == 1) {
                selectedBookDoubleClick(selected.iterator().next());
            }
        });
        listWithBooks.setItems(bookService.getAllBooks());

        VerticalLayout panelWithButtons = new VerticalLayout();
        panelWithButtons.setWidth(100f, Unit.PERCENTAGE);

        Button newBookButton = new Button("Create book", this::createBookButtonClick);
        Button deleteBookButton = new Button("Delete book", this::deleteBookButtonClick);

        panelWithButtons.addComponents(newBookButton, deleteBookButton);

        panelWithButtons.setComponentAlignment(newBookButton, Alignment.TOP_LEFT);
        panelWithButtons.setComponentAlignment(deleteBookButton, Alignment.TOP_LEFT);

        cleanLeftAndRightPanel();
        leftPanel.addComponents(booksLabel, listWithBooks, panelWithButtons);
        leftPanel.setComponentAlignment(booksLabel, Alignment.TOP_LEFT);
        leftPanel.setComponentAlignment(panelWithButtons, Alignment.TOP_CENTER);

        leftPanel.addLayoutClickListener(event -> {
            if (event.getMouseEventDetails().isDoubleClick()) {
                Component clickedComponent = event.getClickedComponent();
                if (ListSelect.class.equals(event.getClickedComponent().getClass())) {

                }
            }
        });

        addCreateAndShowAllItemsPanelOnGlobalPanel();
    }

    private void selectedBookDoubleClick(Book selectedBook) {
        Window bookDetailsWindow = new Window("Book details");

        FormLayout bookDetailsLayout = new FormLayout();

        Label bookLabel = new Label("Book");

        TextField bookTitleTextField = new TextField("Title");
        bookTitleTextField.setWidth(100f, Unit.PERCENTAGE);
        bookTitleTextField.setValue(selectedBook.getBookTitle());
        //bookTitleTextField.setEnabled(false);

        TextField authorsTextField = new TextField("Authors");
        authorsTextField.setWidth(100f, Unit.PERCENTAGE);
        authorsTextField.setValue(StringUtils.collectionToDelimitedString(selectedBook.getAuthors(), ", "));
        //authorsTextField.setEnabled(false);

        TextField numberOfPagesTextField = new TextField("Number of pages");
        numberOfPagesTextField.setWidth(100f, Unit.PERCENTAGE);
        numberOfPagesTextField.setValue((null == selectedBook.getNumberOfPages()) ? "0" : selectedBook.getNumberOfPages().toString());
        //numberOfPagesTextField.setEnabled(false);

        TextField yearTextField = new TextField("Year");
        yearTextField.setWidth(100f, Unit.PERCENTAGE);
        yearTextField.setValue((null == selectedBook.getYear()) ? "0" : selectedBook.getYear().toString());
        //yearTextField.setEnabled(false);

        TextField publishingHouseTextField = new TextField("Publishing house");
        publishingHouseTextField.setWidth(100f, Unit.PERCENTAGE);
        publishingHouseTextField.setValue((null == selectedBook.getPublishingHouse()) ? "" : selectedBook.getPublishingHouse());
        //publishingHouseTextField.setEnabled(false);

        TextField priceTextField = new TextField("Price");
        priceTextField.setWidth(100f, Unit.PERCENTAGE);
        priceTextField.setValue((null == selectedBook.getPrice()) ? "0" : selectedBook.getPrice().toString());
        //priceTextField.setEnabled(false);

        TextField numberOfCopiesTextField = new TextField("Number of copies");
        numberOfCopiesTextField.setWidth(100f, Unit.PERCENTAGE);
        numberOfCopiesTextField.setValue((null == selectedBook.getNumberOfCopies()) ? "0" : selectedBook.getNumberOfCopies().toString());
        //numberOfCopiesTextField.setEnabled(false);

        Image bookCover;

        Cover cover = bookService.getBookCover(selectedBook);
        bookCover = new Image(null, new StreamResource(() -> {
            if (null != cover && cover.isPresented()) {
                ByteArrayInputStream byteArrayInputStream =
                        new ByteArrayInputStream(cover.getPictureOfBookCover());
                return byteArrayInputStream;
            }
            return null;
        }, "cover"));
        bookCover.setWidth(100f, Unit.PERCENTAGE);

        TextField textFieldWithCategories = new TextField("Categories");
        textFieldWithCategories.setWidth(100f, Unit.PERCENTAGE);
        textFieldWithCategories.setHeight(100f, Unit.PERCENTAGE);
        //textFieldWithCategories.setEnabled(false);

        textFieldWithCategories.setValue(StringUtils.collectionToDelimitedString(bookService.getBookCategories(selectedBook), ", "));

        bookDetailsLayout.addComponents(bookLabel, bookTitleTextField, authorsTextField, numberOfPagesTextField,
                yearTextField, publishingHouseTextField, priceTextField, numberOfCopiesTextField, textFieldWithCategories,
                bookCover);
        bookDetailsLayout.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        /*VerticalLayout rightPanel = new VerticalLayout();
        rightPanel.setHeight(100f, Unit.PERCENTAGE);
        rightPanel.setWidth(100f, Unit.PERCENTAGE);*/

        //bookDetailsLayout.addComponents(categoryNameField, booksLabel, booksForSelectedCategory, saveNewCategoryDetailsButton);

        bookDetailsWindow.setContent(bookDetailsLayout);
        bookDetailsWindow.center();
        bookDetailsWindow.setModal(true);
        bookDetailsWindow.addCloseListener(e1 -> createLeftPanelForBooksMenu());
        addWindow(bookDetailsWindow);
    }

    private void createLeftPanelForCategoriesOfBooksMenu() {
        Label categoriesLabel = new Label("Categories");

        ListSelect<String> listWithCategories = new ListSelect<>();
        listWithCategories.setWidth(100f, Unit.PERCENTAGE);
        listWithCategories.setHeight(100f, Unit.PERCENTAGE);

        listWithCategories.setItems(bookCategoryService.getAllStringCategories());

        VerticalLayout panelWithButtons = new VerticalLayout();
        panelWithButtons.setWidth(100f, Unit.PERCENTAGE);

        Button newBookCategoryButton = new Button("Create book category", this::createBookCategoryButtonClick);
        Button deleteBookCategoryButton = new Button("Delete book category", this::deleteBookCategoryButtonClick);

        panelWithButtons.addComponents(newBookCategoryButton, deleteBookCategoryButton);

        panelWithButtons.setComponentAlignment(newBookCategoryButton, Alignment.TOP_LEFT);
        panelWithButtons.setComponentAlignment(deleteBookCategoryButton, Alignment.TOP_LEFT);

        cleanLeftAndRightPanel();
        leftPanel.addComponents(categoriesLabel, listWithCategories, panelWithButtons);
        leftPanel.setComponentAlignment(categoriesLabel, Alignment.TOP_LEFT);
        leftPanel.setComponentAlignment(panelWithButtons, Alignment.TOP_CENTER);


        leftPanel.addLayoutClickListener(event -> {
            if (event.getMouseEventDetails().isDoubleClick()) {
                Component clickedComponent = event.getClickedComponent();
                if (ListSelect.class.equals(event.getClickedComponent().getClass()) && ((ListSelect)clickedComponent).getSelectedItems().size() == 1) {
                    selectedCategoryDoubleClick(((ListSelect<String>)clickedComponent).getSelectedItems().iterator().next());
                }
            }
        });

        addCreateAndShowAllItemsPanelOnGlobalPanel();
    }

    private void selectedCategoryDoubleClick(String selectedCategory) {
        Window categoryDetailsWindow = new Window("Category details");

        FormLayout categoryDetailsLayout = new FormLayout();

        TextField categoryNameField = new TextField("Category name");
        categoryNameField.setWidth(100f, Unit.PERCENTAGE);

        ListSelect<Book> booksForSelectedCategory = new ListSelect<>();
        booksForSelectedCategory.setWidth(100f, Unit.PERCENTAGE);

        Label booksLabel = new Label("Books");
        Set<Book> books = bookCategoryService.getBooksByCategory(selectedCategory);
        booksForSelectedCategory.setItems(new ArrayList<>(books));

        Button saveNewCategoryDetailsButton = new Button("Save");

        saveNewCategoryDetailsButton.addClickListener(event -> {

            AtomicBoolean resultOfOperation = new AtomicBoolean(false);

            if (!StringUtils.isEmpty(categoryNameField.getValue())) {

                Window confirmDialogWindow = new YesNoDialog("Confirmation",
                        "Do you really want to change category details: " +
                                selectedCategory,
                        resultIsYes -> {
                            if (resultIsYes) {
                                Set<Book> booksForSelectedCategorySet = booksForSelectedCategory.getSelectedItems();
                                resultOfOperation.set(
                                        bookCategoryService.changeCategoryDetails(selectedCategory, categoryNameField.getValue(), booksForSelectedCategorySet));
                            }
                        });

                confirmDialogWindow.center();
                addWindow(confirmDialogWindow);
            }

            Notification.show("Changes for category \"" + selectedCategory +
                            ((resultOfOperation.get()) ? "\" were saved." : " weren't saved."),
                    Notification.Type.TRAY_NOTIFICATION);
        });

        categoryDetailsLayout.addComponents(categoryNameField, booksLabel, booksForSelectedCategory, saveNewCategoryDetailsButton);
        categoryDetailsLayout.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        categoryDetailsWindow.setContent(categoryDetailsLayout);
        categoryDetailsWindow.center();
        categoryDetailsWindow.setModal(true);
        categoryDetailsWindow.addCloseListener(e1 -> createLeftPanelForCategoriesOfBooksMenu());
        addWindow(categoryDetailsWindow);
    }

    private void createUserButtonClick(Button.ClickEvent e) {
        Window createUserWindow = new Window("Create user");

        FormLayout createUserLayout = new FormLayout();
        HorizontalSplitPanel createUserSplitLayout = new HorizontalSplitPanel();
        createUserSplitLayout.setHeight(100f, Unit.PERCENTAGE);
        createUserSplitLayout.setWidth(100f, Unit.PERCENTAGE);

        VerticalLayout leftPanelCreateUser = new VerticalLayout();
        leftPanelCreateUser.setHeight(100f, Unit.PERCENTAGE);
        leftPanelCreateUser.setWidth(100f, Unit.PERCENTAGE);

        VerticalLayout rightPanelCreateUser = new VerticalLayout();
        rightPanelCreateUser.setHeight(100f, Unit.PERCENTAGE);
        rightPanelCreateUser.setWidth(100f, Unit.PERCENTAGE);

        ListSelect<String> listWithRoles = new ListSelect<>();

        listWithRoles.setWidth(100f, Unit.PERCENTAGE);
        listWithRoles.setHeight(100f, Unit.PERCENTAGE);

        Button createNewUserButton = new Button("Create");
        createNewUserButton.addClickListener((event) -> {
            Set<String> selectedRoles = listWithRoles.getSelectedItems();

            if (selectedRoles.isEmpty()) {
                Notification.show("Should be selected 1 role at least", Notification.Type.ERROR_MESSAGE);
                return;
            }

            boolean resultOfOperation = false;

            if (null != passwordField.getValue() &&
                    passwordField.getValue().equals(confirmPasswordField.getValue())) {
                resultOfOperation = userService.save(usernameField.getValue(),
                        passwordField.getValue(), roleService.findByRoles(selectedRoles));
            }

            Notification.show("User \"" + usernameField.getValue() + "\" with roles \"" +
                            StringUtils.collectionToDelimitedString(selectedRoles, ", ") +
                            ((resultOfOperation) ? "\" created." : " didn't created."),
                    Notification.Type.TRAY_NOTIFICATION);
        });

        leftPanelCreateUser.addComponent(new Label("User credentials"));

        usernameField.setValue("");
        passwordField.setValue("");
        confirmPasswordField.setValue("");

        leftPanelCreateUser.addComponents(usernameField, passwordField, confirmPasswordField, createNewUserButton);
        leftPanelCreateUser.setHeight(100f, Unit.PERCENTAGE);

        listWithRoles.setItems(roleService.getAllStringRoles());

        rightPanelCreateUser.addComponents(new Label("Choose roles"), listWithRoles);
        rightPanelCreateUser.setHeight(100f, Unit.PERCENTAGE);

        createUserSplitLayout.addComponents(leftPanelCreateUser, rightPanelCreateUser);

        createUserLayout.addComponent(createUserSplitLayout);

        createUserWindow.setContent(createUserLayout);
        createUserWindow.center();
        createUserWindow.setModal(true);
        createUserWindow.addCloseListener(e1 -> createLeftPanelForUserMenu());
        addWindow(createUserWindow);
    }

    private void createUserRoleButtonClick(Button.ClickEvent e) {
        Window window = new Window("Create user role");

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
        createRoleLayout.addComponents(roleField, createNewUserRoleButton);
        createRoleLayout.setComponentAlignment(createNewUserRoleButton, Alignment.TOP_LEFT);

        window.setContent(createRoleLayout);
        window.center();
        window.setModal(true);
        window.addCloseListener(e1 -> createLeftPanelForRolesOfUsersMenu());
        addWindow(window);
    }

    private void createBookButtonClick(Button.ClickEvent e) {
        Window window = new Window("Create book");

        FormLayout createBookLayout = new FormLayout();
        HorizontalSplitPanel createBookSplitLayout = new HorizontalSplitPanel();
        createBookSplitLayout.setHeight(100f, Unit.PERCENTAGE);
        createBookSplitLayout.setWidth(100f, Unit.PERCENTAGE);

        VerticalLayout leftPanel = new VerticalLayout();
        leftPanel.setHeight(100f, Unit.PERCENTAGE);
        leftPanel.setWidth(100f, Unit.PERCENTAGE);

        VerticalLayout rightPanelCreateBook = new VerticalLayout();
        rightPanelCreateBook.setHeight(100f, Unit.PERCENTAGE);
        rightPanelCreateBook.setWidth(100f, Unit.PERCENTAGE);

        //left panel

        leftPanel.addComponent(new Label("Book parameters"));

        TextField bookTitleTextField = new TextField("Title");
        bookTitleTextField.setWidth(100f, Unit.PERCENTAGE);

        TextField authorsTextField = new TextField("Authors");
        authorsTextField.setWidth(100f, Unit.PERCENTAGE);

        TextField numberOfPagesTextField = new TextField("Number of pages");
        numberOfPagesTextField.setWidth(100f, Unit.PERCENTAGE);

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

        TextField publishingHouseTextField = new TextField("Publishing house");
        publishingHouseTextField.setWidth(100f, Unit.PERCENTAGE);

        TextField priceTextField = new TextField("Price");
        priceTextField.setWidth(100f, Unit.PERCENTAGE);

        TextField numberOfCopiesTextField = new TextField("Number of copies");
        numberOfCopiesTextField.setWidth(100f, Unit.PERCENTAGE);

        ImageUploader pictureOfBookCoverImageUploader = new ImageUploader();
        pictureOfBookCoverImageUploader.setWidth(100f, Unit.PERCENTAGE);

        Button createBookButton = new Button("Create", event -> {
            Book createdBook;
            Map<String, Object> bookParameters = new HashMap<>();

            if (bookTitleTextField.getValue().isEmpty()) {
                UserError error = new UserError("Title can't be empty");
                bookTitleTextField.setComponentError(error);
                return;
            } else {
                bookTitleTextField.setComponentError(null);
            }

            if (authorsTextField.getValue().isEmpty()) {
                UserError error = new UserError("Authors can't be empty");
                authorsTextField.setComponentError(error);
                return;
            } else {
                authorsTextField.setComponentError(null);
            }

            ListSelect<String> listWithCategories = (ListSelect<String>) rightPanelCreateBook.getComponent(1);

            if (listWithCategories.getSelectedItems().isEmpty()) {
                UserError error = new UserError("Should be selected 1 category at least");
                listWithCategories.setComponentError(error);
                return;
            } else {
                listWithCategories.setComponentError(null);
            }

            bookParameters.put("bookTitle", bookTitleTextField.getValue());
            bookParameters.put("authors", Arrays.stream(authorsTextField.getValue().split(",")).map(item -> item.trim()).collect(Collectors.toSet()));
            bookParameters.put("categories", bookCategoryService.findByCategories(listWithCategories.getSelectedItems()));
            bookParameters.put("numberOfPages", numberOfPagesTextField.getValue().isEmpty() ? 0 : conversionService.convert(numberOfPagesTextField.getValue(), Integer.class));
            bookParameters.put("year", (null != yearDateField.getValue()) ? (short) yearDateField.getValue().getYear() : (short) 0);
            bookParameters.put("publishingHouse", publishingHouseTextField.getValue());
            bookParameters.put("price", priceTextField.getValue().isEmpty() ? BigDecimal.ZERO : conversionService.convert(priceTextField.getValue(), BigDecimal.class));
            bookParameters.put("numberOfCopies", numberOfCopiesTextField.getValue().isEmpty() ? 0 : conversionService.convert(numberOfCopiesTextField.getValue(), Integer.class));

            if (pictureOfBookCoverImageUploader.getOutputStreamForImage().size() > 0) {
                bookParameters.put("pictureOfBookCover", pictureOfBookCoverImageUploader.getOutputStreamForImage().toByteArray());
            }

            createdBook = bookService.save(bookParameters);

            Notification.show("Book \"" + bookTitleTextField.getValue() + " " + authorsTextField.getValue() +
                            ((null != createdBook) ? "\" created." : " didn't created."),
                    Notification.Type.TRAY_NOTIFICATION);
        });

        ComponentValueValidation.validate(bookTitleTextField,
                new StringLengthValidator("Title can't be empty", 0, 6000), conversionService, String.class);
        //ComponentValueValidation.validate(authorsTextField, );
        //ComponentValueValidation.validate(numberOfPagesTextField, );
        //ComponentValueValidation.validate(yearDateField,
        //        new IntegerRangeValidator("Value should be from 0 to 20000", 0, 20000), conversionService, Integer.class);
        //ComponentValueValidation.validate(publishingHouseTextField, );
        ComponentValueValidation.validate(priceTextField,
                new BigDecimalRangeValidator("Value should have positive decimal format from 0 to 2000000",
                        BigDecimal.valueOf(0l), BigDecimal.valueOf(2000000l)), conversionService, BigDecimal.class);
        //ComponentValueValidation.validate(numberOfCopiesTextField, );

        leftPanel.addComponents(bookTitleTextField, authorsTextField, numberOfPagesTextField,
                yearDateField, publishingHouseTextField, priceTextField, numberOfCopiesTextField,
                pictureOfBookCoverImageUploader, createBookButton);
        leftPanel.setComponentAlignment(createBookButton, Alignment.TOP_LEFT);

        //right panel

        ListSelect<String> listWithCategories = new ListSelect<>();
        listWithCategories.setWidth(100f, Unit.PERCENTAGE);
        listWithCategories.setHeight(100f, Unit.PERCENTAGE);

        listWithCategories.setItems(bookCategoryService.getAllStringCategories());

        rightPanelCreateBook.addComponents(new Label("Choose book categories"), listWithCategories);
        rightPanelCreateBook.setHeight(100f, Unit.PERCENTAGE);

        createBookSplitLayout.addComponents(leftPanel, rightPanelCreateBook);

        createBookLayout.addComponent(createBookSplitLayout);

        window.setContent(createBookLayout);
        window.setSizeFull();
        window.center();
        window.addCloseListener(e1 -> createLeftPanelForBooksMenu());
        addWindow(window);
    }

    private void createBookCategoryButtonClick(Button.ClickEvent e) {
        Window window = new Window("Create book category");

        FormLayout createCategoryLayout = new FormLayout();

        TextField categoryField = new TextField("New category");
        categoryField.setWidth(100f, Unit.PERCENTAGE);

        Button createNewUserRoleButton = new Button("Create", (event) -> {

            boolean resultOfOperation;

            resultOfOperation = bookCategoryService.save(categoryField.getValue());

            Notification.show("Category \"" + categoryField.getValue() +
                            ((resultOfOperation) ? "\" created." : " didn't created."),
                    Notification.Type.TRAY_NOTIFICATION);
        });
        createCategoryLayout.addComponents(categoryField, createNewUserRoleButton);
        createCategoryLayout.setComponentAlignment(createNewUserRoleButton, Alignment.TOP_LEFT);

        window.setContent(createCategoryLayout);
        window.center();
        window.setModal(true);
        window.addCloseListener(e1 -> createLeftPanelForCategoriesOfBooksMenu());
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
                        createLeftPanelForUserMenu();
                        Notification.show(countOfDeletedUsers + " users deleted.",
                                Notification.Type.TRAY_NOTIFICATION);
                    }
                });

        confirmDialogWindow.center();
        addWindow(confirmDialogWindow);
    }

    private void deleteUserRoleButtonClick(Button.ClickEvent e) {
        VerticalLayout panel = (VerticalLayout) createAndShowAllItemsPanel.getFirstComponent();
        ListSelect<String> listWithUsers = (ListSelect<String>) panel.getComponent(0);
        Set<String> selectedItems = listWithUsers.getSelectedItems();

        Window confirmDialogWindow = new YesNoDialog("Confirmation",
                "Do you really want to delete roles: " +
                        StringUtils.collectionToDelimitedString(selectedItems, ", "),
                resultIsYes -> {
                    if (resultIsYes) {
                        int countOfDeletedUsers = roleService.delete(selectedItems);
                        createLeftPanelForRolesOfUsersMenu();
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
                        createLeftPanelForRolesOfUsersMenu();
                        Notification.show(countOfDeletedUsers + " books deleted",
                                Notification.Type.TRAY_NOTIFICATION);
                    }
                });

        confirmDialogWindow.center();
        addWindow(confirmDialogWindow);
    }

    private void deleteBookCategoryButtonClick(Button.ClickEvent e) {
        VerticalLayout panel = (VerticalLayout) createAndShowAllItemsPanel.getFirstComponent();
        ListSelect<String> listWithUsers = (ListSelect<String>) panel.getComponent(0);
        Set<String> selectedItems = listWithUsers.getSelectedItems();

        Window confirmDialogWindow = new YesNoDialog("Confirmation",
                "Do you really want to delete categories of books: " +
                        StringUtils.collectionToDelimitedString(selectedItems, ", "),
                resultIsYes -> {
                    if (resultIsYes) {
                        int countOfDeletedUsers = bookCategoryService.delete(selectedItems);
                        createLeftPanelForCategoriesOfBooksMenu();
                        Notification.show(countOfDeletedUsers + " categories deleted.",
                                Notification.Type.TRAY_NOTIFICATION);
                    }
                });

        confirmDialogWindow.center();
        addWindow(confirmDialogWindow);
    }
}
