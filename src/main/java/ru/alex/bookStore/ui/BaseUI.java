package ru.alex.bookStore.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import ru.alex.bookStore.entities.User;
import ru.alex.bookStore.utils.SecurityService;
import ru.alex.bookStore.utils.UserService;

public class BaseUI extends UI {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;
    @Autowired
    private SecurityService securityService;

    Button loginButtonBase = new Button("Login", this::loginButtonBaseClick);
    Button registerButtonBase = new Button("Register", this::registerButtonBaseClick);
    Button logoutButtonBase = new Button("Logout", this::logoutButtonClicked);

    Button loginButton = new Button("Login", this::loginButtonClick);
    Button registerButton = new Button("Register", this::registerButtonClick);

    TextField usernameField = new TextField("Login", "");
    PasswordField passwordField = new PasswordField("Password", "");
    PasswordField confirmPasswordField = new PasswordField("Confirm password", "");
    protected VaadinRequest localVaadinRequest;
    private Boolean authorizationUIflag = false;
    private Boolean registrationUIflag = false;
    private final String anonymousUser = "anonymousUser";

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        localVaadinRequest = vaadinRequest;

        Boolean isAuthenticated = !anonymousUser.equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        Window window = new Window();

        VerticalLayout components = new VerticalLayout();

        HorizontalLayout horizontalPanelForButtons = new HorizontalLayout();

        horizontalPanelForButtons.addComponent(loginButtonBase);
        horizontalPanelForButtons.addComponent(registerButtonBase);
        if (isAuthenticated) horizontalPanelForButtons.addComponent(logoutButtonBase);

        components.addComponent(horizontalPanelForButtons);
        window.setContent(components);
        window.setSizeFull();
        window.setResizable(false);
        window.setClosable(false);
        addWindow(window);
    }

    protected void createForm(String formName, VaadinRequest vaadinRequest) {
        localVaadinRequest = vaadinRequest;

        switch (formName) {
            case "register": {
                createRegistrationForm(vaadinRequest);
                break;
            }
            case "authorization": {
                createAuthorizationFrom(vaadinRequest);
                break;
            }
        }
    }

    private AbstractOrderedLayout createAuthorizationForm() {
        FormLayout components = new FormLayout();
        components.addComponent(usernameField);
        components.addComponent(passwordField);

        HorizontalLayout horizontalPanelForButtons = new HorizontalLayout();

        horizontalPanelForButtons.addComponent(loginButton);
        if (authorizationUIflag) horizontalPanelForButtons.addComponent(registerButton);

        components.addComponent(horizontalPanelForButtons);
        components.setSizeUndefined();

        return components;
    }

    private void createAuthorizationFrom(VaadinRequest vaadinRequest) {
        Window window;
        window = (StringUtils.startsWithIgnoreCase(vaadinRequest.getPathInfo(), "/login")) ? new Window("Authorization") :
                new Window();

        window.setContent(createAuthorizationForm());
        window.center();
        window.setResizable(false);
        if (authorizationUIflag) window.setClosable(false);

        addWindow(window);
    }

    private AbstractOrderedLayout createRegistrationForm() {
        FormLayout components = new FormLayout();

        components.addComponent(usernameField);
        components.addComponent(passwordField);
        components.addComponent(confirmPasswordField);
        components.addComponent(registerButton);
        components.setSizeUndefined();

        return components;
    }

    private void createRegistrationForm(VaadinRequest vaadinRequest) {
        localVaadinRequest = vaadinRequest;

        Window window;
        window = (StringUtils.startsWithIgnoreCase(vaadinRequest.getPathInfo(), "/register")) ? new Window("Register new user") :
                new Window();

        window.setContent(createRegistrationForm());
        window.center();
        window.setResizable(false);
        if (registrationUIflag) window.setClosable(false);

        addWindow(window);
    }

    private void logoutButtonClicked(Button.ClickEvent e) {
        SecurityContextHolder.clearContext();
        //redirect to login page
        getPage().setLocation("/logout");
    }

    private void loginButtonBaseClick(Button.ClickEvent e) {
        createAuthorizationFrom(localVaadinRequest);
    }

    protected void loginButtonClick(Button.ClickEvent e) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(usernameField.getValue());
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;

            if (passwordEncoder.matches(passwordField.getValue(), userDetails.getPassword())) {
                usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(usernameField.getValue(), passwordField.getValue(), userDetails.getAuthorities());
                authenticationManager.authenticate(usernamePasswordAuthenticationToken);

                if (usernamePasswordAuthenticationToken.isAuthenticated()) {
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }

                //redirect to main page
                getPage().setLocation("/main");
            } else {
                //make proper validation of password
                getPage().setLocation("/accessDenied");
            }
        }
        catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    private void registerButtonBaseClick(Button.ClickEvent e) {
        createRegistrationForm(localVaadinRequest);
    }

    protected void registerButtonClick(Button.ClickEvent e) {

        if (null != passwordField.getValue() && passwordField.getValue().equals(confirmPasswordField.getValue())) {

            User user = new User();
            user.setUsername(usernameField.getValue());
            user.setPassword(passwordField.getValue());

            userService.save(user);
            securityService.autoLogin(usernameField.getValue(), passwordField.getValue());

            //redirect to main page
            getPage().setLocation("/main");
        } else {
            //error message
        }
    }

    public Boolean isAuthorizationUI() { return authorizationUIflag; }

    public void setAuthorizationUIflag (Boolean flag) { authorizationUIflag = flag; }

    public Boolean isRegistrationUI() { return registrationUIflag; }

    public void setRegistrationUIflag (Boolean flag) { registrationUIflag = flag; }
}
