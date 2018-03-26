package ru.alex.bookStore.ui;

import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import ru.alex.bookStore.entities.Book;
import ru.alex.bookStore.entities.BookCategory;
import org.springframework.security.core.userdetails.User;
import ru.alex.bookStore.repository.CategoryRepository;
import ru.alex.bookStore.utils.book.BookService;
import ru.alex.bookStore.utils.ui.ImageUploader;

import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@SpringUI(path = "/main")
public class MainUI extends BaseUI {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    BookService bookService;

    Button loginButtonBase = new Button("Login", this::loginButtonBaseClick);
    Button registerButtonBase = new Button("Register", this::registerButtonBaseClick);
    Button logoutButtonBase = new Button("Logout", this::logoutButtonClicked);
    Button adminPanelButton = new Button("Admin Panel", this::adminPanelButtonClick);

    VerticalLayout layoutWithBooks = new VerticalLayout();

    private final String anonymousUser = "anonymousUser";

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Boolean isAnonymousUser = false;
        String stringUsername;
        if (principal instanceof String) {
            isAnonymousUser = !anonymousUser.equals(principal);
            stringUsername = (String) principal;
        } else {
            stringUsername = ((User) principal).getUsername();
        }

        float horizontalPanelSize = 0;

        Window window = new Window();

        VerticalLayout components = new VerticalLayout();
        HorizontalLayout horizontalPanelForButtons = new HorizontalLayout();
        horizontalPanelForButtons.setStyleName(ValoTheme.LAYOUT_WELL);

        loginButtonBase.setStyleName(ValoTheme.BUTTON_LINK);
        registerButtonBase.setStyleName(ValoTheme.BUTTON_LINK);
        logoutButtonBase.setStyleName(ValoTheme.BUTTON_LINK);
        adminPanelButton.setStyleName(ValoTheme.BUTTON_LINK);

        if (isAnonymousUser) {
            if (doesUserHaveAdminRole(stringUsername)) {
                horizontalPanelForButtons.addComponent(adminPanelButton);
                horizontalPanelSize += adminPanelButton.getWidth();
            }

            horizontalPanelForButtons.addComponent(logoutButtonBase);
            horizontalPanelSize += logoutButtonBase.getWidth();
        } else {
            horizontalPanelForButtons.addComponent(loginButtonBase);
            horizontalPanelSize += loginButtonBase.getWidth();
            horizontalPanelForButtons.addComponent(registerButtonBase);
            horizontalPanelSize += registerButtonBase.getWidth();
        }

        horizontalPanelForButtons.setWidth(horizontalPanelSize, Unit.PIXELS);

        HorizontalLayout menuLayout = new HorizontalLayout();
        menuLayout.setWidth(100f, Unit.PERCENTAGE);

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
            categoriesMenuItem.addItem(bookCategory.getCategory(), null, new MenuBar.Command() {
                @Override
                public void menuSelected(MenuBar.MenuItem selectedItem) {
                    categoryClick(bookCategory);
                }
            });
        }

        menuLayout.addComponent(newButton);
        menuLayout.addComponent(bestsellersButton);
        menuLayout.addComponent(menuBar);
        menuLayout.addComponent(aboutUsButton);

        menuLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        layoutWithBooks.setSizeFull();

        components.addComponent(horizontalPanelForButtons);
        components.addComponent(menuLayout);
        components.addComponent(layoutWithBooks);

        components.setComponentAlignment(horizontalPanelForButtons, Alignment.TOP_RIGHT);
        components.setComponentAlignment(menuLayout, Alignment.TOP_CENTER);

        window.setContent(components);
        window.setSizeFull();
        window.setResizable(false);
        window.setClosable(false);
        addWindow(window);
    }

    private void newButtonClick(Button.ClickEvent event) {
        Set<Book> top10NewBooks = bookService.findTop10BooksOrderedByAddingDay();
        layoutWithBooks.removeAllComponents();

        for (Book book : top10NewBooks) {
            layoutWithBooks.addComponent(createLayoutForBookDetails(book));
        }
        layoutWithBooks.markAsDirty();
    }

    private void bestsellersButtonClick(Button.ClickEvent event) {
        Set<Book> bestsellers = bookService.findTop10BooksOrderedByCountOfSoldItems();
        layoutWithBooks.removeAllComponents();

        for (Book book : bestsellers) {
            layoutWithBooks.addComponent(createLayoutForBookDetails(book));
        }
        layoutWithBooks.markAsDirty();
    }

    private void categoryClick(BookCategory category) {
        Set<Book> booksWithSelectedCategory = bookService.findBooksByCategoriesContains(category);
        layoutWithBooks.removeAllComponents();

        layoutWithBooks.addComponent(new Label("Books with category: " + booksWithSelectedCategory.size()));

        for (Book book : booksWithSelectedCategory) {
            layoutWithBooks.addComponent(createLayoutForBookDetails(book));
        }
        layoutWithBooks.markAsDirty();
    }

    private void aboutUsButtonClick(Button.ClickEvent event) {
        layoutWithBooks.removeAllComponents();

        layoutWithBooks.addComponent(new Label("E-mail: belov.aleksandr@bk.ru"));
    }

    private FormLayout createLayoutForBookDetails(@NotNull Book book) {
        FormLayout bookDetailsPanel = new FormLayout();

        HorizontalSplitPanel layoutForComponents = new HorizontalSplitPanel();
        layoutForComponents.setSizeFull();
        layoutForComponents.setSplitPosition(15F, Unit.PERCENTAGE);
        layoutForComponents.setResponsive(false);

        //left panel

        VerticalLayout leftPanelWithPicture = new VerticalLayout();
        leftPanelWithPicture.setSizeFull();

        ImageUploader pictureOfBookCoverImageUploader = new ImageUploader();
        pictureOfBookCoverImageUploader.setSizeFull();

        if (null != book.getPictureOfBookCover() &&
                book.getPictureOfBookCover().isPresented()) {
            pictureOfBookCoverImageUploader.setOutputStreamForImage(book.getPictureOfBookCover().getPictureOfBookCover());
            pictureOfBookCoverImageUploader.resetProgressbar();
            pictureOfBookCoverImageUploader.showImage();

            Image coverImage = new Image();

            StreamResource.StreamSource streamResource = (StreamResource.StreamSource)
                    () -> new ByteArrayInputStream(book.getPictureOfBookCover().getPictureOfBookCover());

            if (coverImage.getSource() == null) {
                coverImage.setSource(new StreamResource(streamResource, ""));
            } else {
                StreamResource resource = (StreamResource) coverImage.getSource();
                resource.setStreamSource(streamResource);
                resource.setFilename("");
            }

            coverImage.setSizeFull();
            coverImage.markAsDirty();
            leftPanelWithPicture.addComponent(coverImage);
        }

        layoutForComponents.addComponent(leftPanelWithPicture);

        //right panel

        VerticalLayout rightPanelWithDetails = new VerticalLayout();
        rightPanelWithDetails.setSizeFull();

        Label bookTitleLabel = new Label("Title: " + book.getBookTitle());
        bookTitleLabel.setWidth(100f, Unit.PERCENTAGE);
        TextArea authorsTextArea = new TextArea("Authors: ");
        authorsTextArea.setValue(StringUtils.collectionToDelimitedString(book.getAuthors(), " "));
        authorsTextArea.setReadOnly(true);
        authorsTextArea.setWidth(100f, Unit.PERCENTAGE);

        rightPanelWithDetails.addComponents(bookTitleLabel, authorsTextArea);

        if (!StringUtils.isEmpty(book.getDescription())) {
            TextArea descriptionTextArea = new TextArea("Description");
            descriptionTextArea.setValue(book.getDescription());
            descriptionTextArea.setReadOnly(true);
            rightPanelWithDetails.addComponent(descriptionTextArea);
        }

        if (book.getYear() != 0) {
            Label yearLabel = new Label("Year: " + book.getYear());
            yearLabel.setWidth(100f, Unit.PERCENTAGE);
            rightPanelWithDetails.addComponent(yearLabel);
        }

        if (!StringUtils.isEmpty(book.getPublishingHouse())) {
            Label publishingHouseLabel = new Label("Publishing house: " + book.getPublishingHouse());
            publishingHouseLabel.setWidth(100f, Unit.PERCENTAGE);
            rightPanelWithDetails.addComponent(publishingHouseLabel);
        }

        if (book.getPrice() != BigDecimal.ZERO) {
            Label priceLabel = new Label("Price: " + book.getPrice().toString());
            priceLabel.setWidth(100f, Unit.PERCENTAGE);
            rightPanelWithDetails.addComponent(priceLabel);
        }

        if (book.getNumberOfPages() != 0) {
            Label numberOfPagesLabel = new Label("Number of pages: " + book.getNumberOfPages());
            numberOfPagesLabel.setWidth(100f, Unit.PERCENTAGE);
            rightPanelWithDetails.addComponent(numberOfPagesLabel);
        }

        if (book.getNumberOfCopies() != 0) {
            Label numberOfCopiesLabel = new Label("Number of copies: " + book.getNumberOfCopies());
            numberOfCopiesLabel.setWidth(100f, Unit.PERCENTAGE);
            rightPanelWithDetails.addComponent(numberOfCopiesLabel);
        }

        layoutForComponents.addComponent(rightPanelWithDetails);

        bookDetailsPanel.addComponent(layoutForComponents);
        bookDetailsPanel.setWidth(100f, Unit.PERCENTAGE);

        return bookDetailsPanel;
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

    private boolean doesUserHaveAdminRole(String username) {
        return userService.isAdmin(username);
    }
}
