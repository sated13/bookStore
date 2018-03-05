package ru.alex.bookStore.ui;

import com.vaadin.data.Result;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.datefield.DateResolution;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import ru.alex.bookStore.entities.Book;
import ru.alex.bookStore.utils.book.BookService;
import ru.alex.bookStore.utils.bookCategory.BookCategoryService;
import ru.alex.bookStore.utils.roles.RoleService;
import ru.alex.bookStore.utils.ui.YesNoDialog;
import ru.alex.bookStore.utils.users.UserService;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
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
    HorizontalSplitPanel createAndShowAllItemsPanel = new HorizontalSplitPanel();
    VerticalLayout globalPanel = new VerticalLayout();
    VerticalLayout leftPanel = new VerticalLayout();

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
                (selectedMenuItem) -> createLeftPanelForUsersRolesMenu());
        MenuBar.MenuItem booksMenuItem = menuBar.addItem("Books",
                (selectedMenuItem) -> createLeftPanelForBooksMenu());
        MenuBar.MenuItem categoriesOfBooksMenuItem = menuBar.addItem("Categories of books",
                (selectedMenuItem) -> createLeftPanelForBooksCategoriesMenu());

        createAndShowAllItemsPanel.removeAllComponents();
        createAndShowAllItemsPanel.addComponent(leftPanel);

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

    private void addCreateAndShowAllItemsPanelOnGlobalPanel() {
        if (globalPanel.getComponentIndex(createAndShowAllItemsPanel) == -1) {
            globalPanel.addComponent(createAndShowAllItemsPanel);
        }
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

        panelWithButtons.addComponents(newUserButton, deleteUserButton);

        panelWithButtons.setComponentAlignment(newUserButton, Alignment.TOP_LEFT);
        panelWithButtons.setComponentAlignment(deleteUserButton, Alignment.TOP_LEFT);

        leftPanel.removeAllComponents();
        leftPanel.addComponents(listWithUsers, panelWithButtons);
        leftPanel.setComponentAlignment(panelWithButtons, Alignment.TOP_CENTER);

        addCreateAndShowAllItemsPanelOnGlobalPanel();
    }

    private void createLeftPanelForUsersRolesMenu() {
        ListSelect<String> listWithRoles = new ListSelect<>();
        listWithRoles.setWidth(100f, Unit.PERCENTAGE);
        listWithRoles.setHeight(100f, Unit.PERCENTAGE);

        listWithRoles.addSelectionListener(event -> {
            Set<String> selected = event.getNewSelection();
            Notification.show(selected.size() + " roles of users selected.", Notification.Type.TRAY_NOTIFICATION);
        });
        listWithRoles.setItems(roleService.getAllStringRoles());

        VerticalLayout panelWithButtons = new VerticalLayout();
        panelWithButtons.setWidth(100f, Unit.PERCENTAGE);

        Button newUserRoleButton = new Button("Create user role", this::createUserRoleButtonClick);
        Button deleteUserRoleButton = new Button("Delete user role", this::deleteUserRoleButtonClick);

        panelWithButtons.addComponents(newUserRoleButton, deleteUserRoleButton);

        panelWithButtons.setComponentAlignment(newUserRoleButton, Alignment.TOP_LEFT);
        panelWithButtons.setComponentAlignment(deleteUserRoleButton, Alignment.TOP_LEFT);

        leftPanel.removeAllComponents();
        leftPanel.addComponents(listWithRoles, panelWithButtons);
        leftPanel.setComponentAlignment(panelWithButtons, Alignment.TOP_CENTER);

        addCreateAndShowAllItemsPanelOnGlobalPanel();
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

        VerticalLayout panelWithButtons = new VerticalLayout();
        panelWithButtons.setWidth(100f, Unit.PERCENTAGE);

        Button newBookButton = new Button("Create book", this::createBookButtonClick);
        Button deleteBookButton = new Button("Delete book");//, this::deleteBookButtonClick);

        panelWithButtons.addComponents(newBookButton, deleteBookButton);

        panelWithButtons.setComponentAlignment(newBookButton, Alignment.TOP_LEFT);
        panelWithButtons.setComponentAlignment(deleteBookButton, Alignment.TOP_LEFT);

        leftPanel.removeAllComponents();
        leftPanel.addComponents(listWithBooks, panelWithButtons);
        leftPanel.setComponentAlignment(panelWithButtons, Alignment.TOP_CENTER);

        addCreateAndShowAllItemsPanelOnGlobalPanel();
    }

    private void createLeftPanelForBooksCategoriesMenu() {
        ListSelect<String> listWithCategories = new ListSelect<>();
        listWithCategories.setWidth(100f, Unit.PERCENTAGE);
        listWithCategories.setHeight(100f, Unit.PERCENTAGE);

        listWithCategories.addSelectionListener(event -> {
            Set<String> selected = event.getNewSelection();
            Notification.show(selected.size() + " categories of books selected.", Notification.Type.TRAY_NOTIFICATION);
        });
        listWithCategories.setItems(bookCategoryService.getAllStringCategories());

        VerticalLayout panelWithButtons = new VerticalLayout();
        panelWithButtons.setWidth(100f, Unit.PERCENTAGE);

        Button newBookCategoryButton = new Button("Create book category", this::createBookCategoryButtonClick);
        Button deleteBookCategoryButton = new Button("Delete book category", this::deleteBookCategoryButtonClick);

        panelWithButtons.addComponents(newBookCategoryButton, deleteBookCategoryButton);

        panelWithButtons.setComponentAlignment(newBookCategoryButton, Alignment.TOP_LEFT);
        panelWithButtons.setComponentAlignment(deleteBookCategoryButton, Alignment.TOP_LEFT);

        leftPanel.removeAllComponents();
        leftPanel.addComponents(listWithCategories, panelWithButtons);
        leftPanel.setComponentAlignment(panelWithButtons, Alignment.TOP_CENTER);

        addCreateAndShowAllItemsPanelOnGlobalPanel();
    }

    private void createUserButtonClick(Button.ClickEvent e) {
        Window window = new Window("Create user");

        FormLayout createUserLayout = new FormLayout();
        HorizontalSplitPanel createUserSplitLayout = new HorizontalSplitPanel();
        createUserSplitLayout.setHeight(100f, Unit.PERCENTAGE);
        createUserSplitLayout.setWidth(100f, Unit.PERCENTAGE);

        VerticalLayout leftPanel = new VerticalLayout();
        leftPanel.setHeight(100f, Unit.PERCENTAGE);
        leftPanel.setWidth(100f, Unit.PERCENTAGE);

        Button createNewUserButton = new Button("Create");
        createNewUserButton.addClickListener((event) -> {
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

        leftPanel.addComponents(usernameField, passwordField, confirmPasswordField, createNewUserButton);
        leftPanel.setHeight(100f, Unit.PERCENTAGE);

        VerticalLayout rightPanel = new VerticalLayout();
        rightPanel.setHeight(100f, Unit.PERCENTAGE);
        rightPanel.setWidth(100f, Unit.PERCENTAGE);

        ListSelect<String> listWithRoles = new ListSelect<>();
        listWithRoles.setWidth(100f, Unit.PERCENTAGE);
        listWithRoles.setHeight(100f, Unit.PERCENTAGE);

        listWithRoles.setItems(roleService.getAllStringRoles());

        rightPanel.addComponents(new Label("Choose roles"), listWithRoles);
        rightPanel.setHeight(100f, Unit.PERCENTAGE);

        createUserSplitLayout.addComponents(leftPanel, rightPanel);

        createUserLayout.addComponent(createUserSplitLayout);

        window.setContent(createUserLayout);
        window.center();
        window.setModal(true);
        window.addCloseListener(e1 -> createLeftPanelForUserMenu());
        addWindow(window);
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
        window.addCloseListener(e1 -> createLeftPanelForUsersRolesMenu());
        addWindow(window);
    }

    private void createBookButtonClick(Button.ClickEvent e) {
        Window window = new Window("Create book");

        FormLayout createBookLayout = new FormLayout();
        HorizontalSplitPanel createUserSplitLayout = new HorizontalSplitPanel();
        createUserSplitLayout.setHeight(100f, Unit.PERCENTAGE);
        createUserSplitLayout.setWidth(100f, Unit.PERCENTAGE);

        VerticalLayout leftPanel = new VerticalLayout();
        leftPanel.setHeight(100f, Unit.PERCENTAGE);
        leftPanel.setWidth(100f, Unit.PERCENTAGE);

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

        ByteArrayOutputStream outputStreamForImage = new ByteArrayOutputStream(10240);

        class ImageUploader extends CustomComponent implements Upload.Receiver, Upload.SucceededListener,
                Upload.FailedListener, Upload.ProgressListener {

            String filename;
            ProgressBar progressBar = new ProgressBar(0.0f);
            Image coverImage = new Image("Cover");
            final HashSet<String> imageMimeTypes = new HashSet<>(Arrays.asList("image/gif", "image/png", "image/jpeg", "image/bmp"));
            Upload uploadComponent = new Upload("Upload cover", null);
            boolean isErrorFlag = false;

            public ImageUploader() {
                uploadComponent.setReceiver(this);
                uploadComponent.addFailedListener(this);
                uploadComponent.addSucceededListener(this);
                uploadComponent.addProgressListener(this);

                Panel panel = new Panel();
                VerticalLayout content = new VerticalLayout();
                content.setSpacing(true);
                panel.setContent(content);

                content.addComponents(uploadComponent, progressBar, coverImage);

                progressBar.setVisible(false);
                progressBar.setWidth(100f, Unit.PERCENTAGE);
                coverImage.setVisible(false);

                setCompositionRoot(panel);
            }

            public OutputStream receiveUpload(String filename,
                                              String mimeType){
                if (imageMimeTypes.contains(mimeType)) {
                    this.filename = filename;
                    outputStreamForImage.reset();
                    isErrorFlag = false;
                    return outputStreamForImage;
                }
                else {
                    isErrorFlag = true;
                    uploadComponent.interruptUpload();
                }
                return outputStreamForImage;
            }

            public void uploadSucceeded(Upload.SucceededEvent event) {
                if (!isErrorFlag) {
                    coverImage.setVisible(true);
                    coverImage.setCaption("Uploaded cover: " + filename);

                    StreamResource.StreamSource streamResource = (StreamResource.StreamSource)
                            () -> new ByteArrayInputStream(outputStreamForImage.toByteArray());

                    if (coverImage.getSource() == null) {
                        coverImage.setSource(new StreamResource(streamResource, filename));
                    } else {
                        StreamResource resource = (StreamResource) coverImage.getSource();
                        resource.setStreamSource(streamResource);
                        resource.setFilename(filename);
                    }

                    coverImage.markAsDirty();
                }
            }

            @Override
            public void uploadFailed(Upload.FailedEvent event) {
                Notification.show("Upload failed", Notification.Type.ERROR_MESSAGE);
            }

            @Override
            public void updateProgress(long readBytes, long contentLength) {
                if (!isErrorFlag) {
                    progressBar.setVisible(true);
                    if (contentLength == -1) {
                        progressBar.setIndeterminate(true);
                    } else {
                        progressBar.setValue((float) readBytes / (float) contentLength);
                    }
                }
            }
        }

        ImageUploader pictureOfBookCoverImageUploader = new ImageUploader();
        pictureOfBookCoverImageUploader.setWidth(100f, Unit.PERCENTAGE);

        Button createBookButton = new Button("Create");

        leftPanel.addComponents(bookTitleTextField, authorsTextField, numberOfPagesTextField,
                yearDateField, publishingHouseTextField, priceTextField, numberOfCopiesTextField,
                pictureOfBookCoverImageUploader, createBookButton);
        leftPanel.setComponentAlignment(createBookButton, Alignment.TOP_LEFT);

        VerticalLayout rightPanel = new VerticalLayout();
        rightPanel.setHeight(100f, Unit.PERCENTAGE);
        rightPanel.setWidth(100f, Unit.PERCENTAGE);

        ListSelect<String> listWithCategories = new ListSelect<>();
        listWithCategories.setWidth(100f, Unit.PERCENTAGE);
        listWithCategories.setHeight(100f, Unit.PERCENTAGE);

        listWithCategories.setItems(bookCategoryService.getAllStringCategories());

        rightPanel.addComponents(new Label("Choose book categories"), listWithCategories);
        rightPanel.setHeight(100f, Unit.PERCENTAGE);

        createUserSplitLayout.addComponents(leftPanel, rightPanel);

        createBookLayout.addComponent(createUserSplitLayout);

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

        Button createNewUserRoleButton = new Button("Create");
        createNewUserRoleButton.addClickListener((event) -> {

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
        window.addCloseListener(e1 -> createLeftPanelForBooksCategoriesMenu());
        addWindow(window);
    }

    private void deleteUserButtonClick(Button.ClickEvent e) {
        VerticalLayout panel = (VerticalLayout) createAndShowAllItemsPanel.getFirstComponent();
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
        VerticalLayout panel = (VerticalLayout) createAndShowAllItemsPanel.getFirstComponent();
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
        VerticalLayout panel = (VerticalLayout) createAndShowAllItemsPanel.getFirstComponent();
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
