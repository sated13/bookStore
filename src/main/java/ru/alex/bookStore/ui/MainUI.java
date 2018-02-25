package ru.alex.bookStore.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringUI(path = "/main")
public class MainUI extends UI{

    Button logoutButton = new Button("Logout", this::logoutButtonClicked);

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Window window = new Window("");

        VerticalLayout components = new VerticalLayout();

        components.addComponent(logoutButton);
        components.addComponent(new Label("Main Page"));

        window.setContent(components);
        window.setSizeFull();
        window.setResizable(false);

        addWindow(window);
    }

    private void logoutButtonClicked(Button.ClickEvent e) {
        SecurityContextHolder.clearContext();
        //redirect to login page
        getPage().setLocation("/logout");
    }
}
