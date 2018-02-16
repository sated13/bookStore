package ru.alex.bookStore.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.alex.bookStore.entities.User;
import ru.alex.bookStore.utils.UserService;

@SpringUI(path = "/register")
public class RegistrationUI extends UI {

    @Autowired
    UserService userService;

    Button registerButton = new Button("Register", this::registerButtonClick);
    TextField usernameTextField = new TextField("User name", "");
    PasswordField passwordField = new PasswordField("Password", "");
    PasswordField confirmPasswordField = new PasswordField("Confirm password", "");

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Window window = new Window("Register new user");
        final VerticalLayout verticalPanel = new VerticalLayout();

        verticalPanel.addComponent(usernameTextField);
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
            user.setUsername(usernameTextField.getValue());
            user.setPassword(passwordField.getValue());

            userService.save(user);

            //redirect to main page
            getPage().setLocation("/login");
        }
        else {
            //error message
        }
    }
}
