package ru.alex.bookStore.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.alex.bookStore.entities.BookCategory;
import org.springframework.security.core.userdetails.User;
import ru.alex.bookStore.repository.BookCategoryRepository;
import ru.alex.bookStore.repository.RoleRepository;

import java.util.Collection;
import java.util.List;

@SpringUI(path = "/main")
public class MainUI extends BaseUI {

    @Autowired
    BookCategoryRepository bookCategoryRepository;
    @Autowired
    RoleRepository roleRepository;

    Button loginButtonBase = new Button("Login", this::loginButtonBaseClick);
    Button registerButtonBase = new Button("Register", this::registerButtonBaseClick);
    Button logoutButtonBase = new Button("Logout", this::logoutButtonClicked);
    Button adminPanelButton = new Button("Admin Panel", this::adminPanelButtonClick);

    private final String anonymousUser = "anonymousUser";

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Boolean isAnonymousUser = false;
        String stringUsername;
        if (principal instanceof String) {
            isAnonymousUser = !anonymousUser.equals(principal);
            stringUsername = (String)principal;
        }
        else {
            stringUsername = ((User)principal).getUsername();
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
        components.addComponent(horizontalPanelForButtons);
        components.setComponentAlignment(horizontalPanelForButtons, Alignment.TOP_RIGHT);

        HorizontalLayout menuLayout = new HorizontalLayout();
        menuLayout.setWidth(100f, Unit.PERCENTAGE);

        MenuBar menuBar = new MenuBar();
        menuBar.setStyleName(ValoTheme.MENUBAR_BORDERLESS);

        Button newButton = new Button("New");
        Button bestsellersButton = new Button("Bestsellers");
        Button aboutUsButton = new Button("About us");

        newButton.setStyleName(ValoTheme.BUTTON_LINK);
        bestsellersButton.setStyleName(ValoTheme.BUTTON_LINK);
        aboutUsButton.setStyleName(ValoTheme.BUTTON_LINK);

        MenuBar.MenuItem categoriesMenuItem = menuBar.addItem("Categories", null, null);

        List<BookCategory> allBookCategories = bookCategoryRepository.findAll();

        for (BookCategory bookCategory : allBookCategories) {
            categoriesMenuItem.addItem(bookCategory.getCategory(), null, null);
        }

        menuLayout.addComponent(newButton);
        menuLayout.addComponent(bestsellersButton);
        menuLayout.addComponent(menuBar);
        menuLayout.addComponent(aboutUsButton);

        menuLayout.setComponentAlignment(newButton, Alignment.MIDDLE_CENTER);
        menuLayout.setComponentAlignment(bestsellersButton, Alignment.MIDDLE_CENTER);
        menuLayout.setComponentAlignment(menuBar, Alignment.MIDDLE_CENTER);
        menuLayout.setComponentAlignment(aboutUsButton, Alignment.MIDDLE_CENTER);

        components.addComponent(menuLayout);

        components.setComponentAlignment(menuLayout, Alignment.TOP_CENTER);

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
