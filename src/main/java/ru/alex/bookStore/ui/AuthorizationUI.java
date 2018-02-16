package ru.alex.bookStore.ui;

import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@SpringUI(path = "/login")
@Title("LoginPage")
public class AuthorizationUI extends UI {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;

    Button loginButton = new Button("Login", this::loginButtonClick);
    Button registerButton = new Button("Register", this::registerButtonClick);
    TextField usernameField = new TextField("Login", "enter login");
    PasswordField passwordField = new PasswordField("Password", "enter password");

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Window window = new Window("Authorization");
        final VerticalLayout verticalPanel = new VerticalLayout();
        verticalPanel.addComponent(usernameField);
        verticalPanel.addComponent(passwordField);
        //verticalPanel.addComponent((new Button("Click Me", (Button.ClickListener) clickEvent -> verticalPanel.addComponent(new Label("button was clicked")))));

        HorizontalLayout horizontalPanelForButtons = new HorizontalLayout();

        horizontalPanelForButtons.addComponent(loginButton);
        horizontalPanelForButtons.addComponent(registerButton);

        verticalPanel.addComponent(horizontalPanelForButtons);
        window.setContent(verticalPanel);
        window.center();
        window.setResizable(false);
        window.setClosable(false);
        addWindow(window);
    }

    private void loginButtonClick(Button.ClickEvent e) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(usernameField.getValue());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(usernameField.getValue(), passwordField.getValue(), userDetails.getAuthorities());

        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        //redirect to main page
        getPage().setLocation("/main");
    }

    private void registerButtonClick(Button.ClickEvent e) {
        //redirect to register page
        getPage().setLocation("/register");
    }
}
