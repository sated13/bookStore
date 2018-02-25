package ru.alex.bookStore.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringUI(path = "/login")
@Title("LoginPage")
public class AuthorizationUI extends UI {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    Button loginButton = new Button("Login", this::loginButtonClick);
    Button registerButton = new Button("Register", this::registerButtonClick);
    TextField usernameField = new TextField("Login", "");
    PasswordField passwordField = new PasswordField("Password", "");

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Window window = new Window("Authorization");
        VerticalLayout components = new VerticalLayout();
        components.addComponent(usernameField);
        components.addComponent(passwordField);
        //components.addComponent((new Button("Click Me", (Button.ClickListener) clickEvent -> verticalPanel.addComponent(new Label("button was clicked")))));

        HorizontalLayout horizontalPanelForButtons = new HorizontalLayout();

        horizontalPanelForButtons.addComponent(loginButton);
        horizontalPanelForButtons.addComponent(registerButton);

        components.addComponent(horizontalPanelForButtons);
        window.setContent(components);
        window.center();
        window.setResizable(false);
        window.setClosable(false);
        addWindow(window);
    }

    private void loginButtonClick(Button.ClickEvent e) {
        System.out.println("------------------------");
        System.out.println("step_1");
        System.out.println(usernameField.getValue());
        System.out.println("------------------------");
        System.out.println("step_1");
        UserDetails userDetails = userDetailsService.loadUserByUsername(usernameField.getValue());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = null;
        System.out.println("------------------------");
        System.out.println("step_1");
        System.out.println(userDetails.getPassword());
        System.out.println(passwordField.getValue());
        System.out.println(passwordEncoder.matches(passwordField.getValue(), userDetails.getPassword()));
        System.out.println("------------------------");
        if (passwordEncoder.matches(passwordField.getValue(), userDetails.getPassword())) {
            usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(usernameField.getValue(), passwordField.getValue(), userDetails.getAuthorities());
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            if (usernamePasswordAuthenticationToken.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

            //redirect to main page
            getPage().setLocation("/main");
        }
        else {
            //make proper validation of password
            getPage().setLocation("/accessDenied");
        }
    }

    private void registerButtonClick(Button.ClickEvent e) {
        //redirect to register page
        getPage().setLocation("/register");
    }
}
