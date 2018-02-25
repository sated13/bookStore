package ru.alex.bookStore.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.alex.bookStore.entities.User;
import ru.alex.bookStore.utils.SecurityService;
import ru.alex.bookStore.utils.UserService;

@SpringUI(path = "/register")
public class RegistrationUI extends UI {

    @Autowired
    UserService userService;
    @Autowired
    private SecurityService securityService;

    Button registerButton = new Button("Register", this::registerButtonClick);
    TextField usernameField = new TextField("User name", "");
    PasswordField passwordField = new PasswordField("Password", "");
    PasswordField confirmPasswordField = new PasswordField("Confirm password", "");

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Window window = new Window("Register new user");
        final VerticalLayout verticalPanel = new VerticalLayout();

        verticalPanel.addComponent(usernameField);
        verticalPanel.addComponent(passwordField);
        verticalPanel.addComponent(confirmPasswordField);
        verticalPanel.addComponent(registerButton);

        window.setContent(verticalPanel);
        window.center();
        window.setResizable(false);
        window.setClosable(false);
        addWindow(window);
    }

    private void registerButtonClick(Button.ClickEvent e) {

        if (null != passwordField.getValue() && passwordField.getValue().equals(confirmPasswordField.getValue())) {

            User user = new User();
            user.setUsername(usernameField.getValue());
            user.setPassword(passwordField.getValue());

            userService.save(user);
            securityService.autoLogin(usernameField.getValue(), passwordField.getValue());

            //redirect to main page
            getPage().setLocation("/main");
        }
        else {
            //error message
        }
    }
}
