package ru.alex.bookStore.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.orderedlayout.HorizontalLayoutState;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.alex.bookStore.entities.BookCategory;
import ru.alex.bookStore.repository.BookCategoryRepository;

import java.util.List;

@SpringUI(path = "/main")
public class MainUI extends BaseUI{

    @Autowired
    BookCategoryRepository bookCategoryRepository;

    Button loginButtonBase = new Button("Login", this::loginButtonBaseClick);
    Button registerButtonBase = new Button("Register", this::registerButtonBaseClick);
    Button logoutButtonBase = new Button("Logout", this::logoutButtonClicked);

    private final String anonymousUser = "anonymousUser";

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        localVaadinRequest = vaadinRequest;

        Boolean isAuthenticated = !anonymousUser.equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        float horizontalPanelSize = 0;

        Window window = new Window();

        VerticalLayout components = new VerticalLayout();
        HorizontalLayout horizontalPanelForButtons = new HorizontalLayout();

        loginButtonBase.setStyleName("link");
        registerButtonBase.setStyleName("link");
        logoutButtonBase.setStyleName("link");

        horizontalPanelForButtons.addComponent(loginButtonBase);
        horizontalPanelSize += loginButtonBase.getWidth();
        horizontalPanelForButtons.addComponent(registerButtonBase);
        horizontalPanelSize += registerButtonBase.getWidth();
        if (isAuthenticated) {
            horizontalPanelForButtons.addComponent(logoutButtonBase);
            horizontalPanelSize += logoutButtonBase.getWidth();
        }

        horizontalPanelForButtons.setWidth(horizontalPanelSize, Unit.PIXELS);
        components.addComponent(horizontalPanelForButtons);
        components.setComponentAlignment(horizontalPanelForButtons, Alignment.TOP_RIGHT);

        MenuBar menuBar = new MenuBar();
        menuBar.setWidth(100f, Unit.PERCENTAGE);

        MenuBar.MenuItem newMenuItem = menuBar.addItem("New", null, null);
        MenuBar.MenuItem bestsellersMenuItem = menuBar.addItem("Bestsellers", null, null);
        MenuBar.MenuItem categoriesMenuItem = menuBar.addItem("Categories", null, null);
        MenuBar.MenuItem abourUsMenuItem = menuBar.addItem("About us", null, null);

        List<BookCategory> allBookCategories = bookCategoryRepository.findAll();

        for(BookCategory bookCategory: allBookCategories) {
            categoriesMenuItem.addItem(bookCategory.getCategory(), null, null);
        }

        components.addComponent(menuBar);
        components.setComponentAlignment(menuBar, Alignment.TOP_CENTER);

        window.setContent(components);
        window.setSizeFull();
        window.setResizable(false);
        window.setClosable(false);
        addWindow(window);
    }

    private void logoutButtonClicked(Button.ClickEvent e) {
        SecurityContextHolder.clearContext();
        //redirect to login page
        getPage().setLocation("/logout");
    }

    private void loginButtonBaseClick(Button.ClickEvent e) {
        createForm("authorization", localVaadinRequest);
    }

    private void registerButtonBaseClick(Button.ClickEvent e) {
        createForm("register", localVaadinRequest);
    }
}
