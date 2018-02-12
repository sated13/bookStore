package ru.alex.bookStore.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;

@SpringUI(path = "/login")
public class AuthorizationPage extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Window window = new Window("Authorization");
        final VerticalLayout verticalPanel = new VerticalLayout();
        verticalPanel.addComponent(new TextField("Login", "enter login"));
        verticalPanel.addComponent(new PasswordField("Password", "enter password"));
        verticalPanel.addComponent((new Button("Click Me", (Button.ClickListener) clickEvent -> verticalPanel.addComponent(new Label("button was clicked")))));
        window.setContent(verticalPanel);
        window.center();
        window.setResizable(false);
        window.setClosable(false);
        addWindow(window);
    }
}
