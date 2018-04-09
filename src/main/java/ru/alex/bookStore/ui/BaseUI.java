package ru.alex.bookStore.ui;

import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.alex.bookStore.entities.UserRole;
import ru.alex.bookStore.utils.roles.RoleService;
import ru.alex.bookStore.utils.security.SecurityService;
import ru.alex.bookStore.utils.users.UserService;

import java.util.HashSet;
import java.util.Set;

class BaseUI extends UI {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    protected SecurityService securityService;

    private Button loginButton = new Button("Login", this::loginButtonClick);
    private Button registerButton = new Button("Register", this::registerButtonClick);
    private Button resisterOnAuthorizationUIButton = new Button("Register", this::resisterOnAuthorizationUIButtonClick);

    TextField usernameField = new TextField("Login", "");
    private PasswordField passwordFieldAuthorization = new PasswordField("Password", "");
    PasswordField passwordFieldRegistration = new PasswordField("Password", "");
    PasswordField confirmPasswordField = new PasswordField("Confirm password", "");
    VaadinRequest localVaadinRequest;
    private Boolean authorizationUIFlag = false;
    private Boolean registrationUIFlag = false;

    private final String customerRole = "customer";

    @Override
    protected void init(VaadinRequest vaadinRequest) {
    }

    void createForm(String formName, VaadinRequest vaadinRequest) {
        localVaadinRequest = vaadinRequest;

        switch (formName) {
            case "register": {
                createRegistrationForm(vaadinRequest);
                break;
            }
            case "authorization": {
                createAuthorizationForm(vaadinRequest);
                break;
            }
        }
    }

    private AbstractOrderedLayout createAuthorizationForm() {
        FormLayout components = new FormLayout();
        components.addComponent(usernameField);
        components.addComponent(passwordFieldAuthorization);

        if (passwordFieldAuthorization.getListeners(ShortcutListener.class).isEmpty()) {
            passwordFieldAuthorization.addShortcutListener(new ShortcutListener("Enter", ShortcutAction.KeyCode.ENTER, null) {
                @Override
                public void handleAction(Object sender, Object target) {
                    loginButton.click();
                }
            });
        }

        HorizontalLayout horizontalPanelForButtons = new HorizontalLayout();

        horizontalPanelForButtons.addComponent(loginButton);
        if (authorizationUIFlag) horizontalPanelForButtons.addComponent(resisterOnAuthorizationUIButton);

        components.addComponent(horizontalPanelForButtons);
        components.setSizeUndefined();

        return components;
    }

    private void createAuthorizationForm(VaadinRequest vaadinRequest) {
        Window window;
        window = (AuthorizationUI.class.equals(getPage().getUI().getClass())) ? new Window("Authorization") :
                new Window();

        window.setContent(createAuthorizationForm());
        window.center();
        window.setResizable(false);
        window.setModal(true);

        if (authorizationUIFlag) window.setClosable(false);

        addWindow(window);
    }

    private AbstractOrderedLayout createRegistrationForm() {
        FormLayout components = new FormLayout();

        components.addComponent(usernameField);
        components.addComponent(passwordFieldRegistration);
        components.addComponent(confirmPasswordField);

        if (confirmPasswordField.getListeners(ShortcutListener.class).isEmpty()) {
            confirmPasswordField.addShortcutListener(new ShortcutListener("Enter", ShortcutAction.KeyCode.ENTER, null) {
                @Override
                public void handleAction(Object sender, Object target) {
                    registerButton.click();
                }
            });
        }

        components.addComponent(registerButton);
        components.setSizeUndefined();

        return components;
    }

    private void createRegistrationForm(VaadinRequest vaadinRequest) {
        localVaadinRequest = vaadinRequest;

        Window window;
        window = (RegistrationUI.class.equals(getPage().getUI().getClass())) ? new Window("Register new user") :
                new Window();

        window.setContent(createRegistrationForm());
        window.center();
        window.setResizable(false);
        window.setModal(true);

        if (registrationUIFlag) window.setClosable(false);

        addWindow(window);
    }

    private void loginButtonClick(Button.ClickEvent e) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(usernameField.getValue());
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;

            if (userService.passwordIsCorrect(passwordFieldAuthorization.getValue(), userDetails.getPassword())) {
                usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(usernameField.getValue(),
                                passwordFieldAuthorization.getValue(),
                                userDetails.getAuthorities());

                authenticationManager.authenticate(usernamePasswordAuthenticationToken);
                usernamePasswordAuthenticationToken.eraseCredentials();

                if (usernamePasswordAuthenticationToken.isAuthenticated()) {
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }

                //redirect to main page
                getPage().setLocation("/main");
            } else {
                Notification.show("Wrong credentials!", Notification.Type.ERROR_MESSAGE);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
            Notification.show("Wrong credentials!", Notification.Type.ERROR_MESSAGE);
            //ToDo: add logging
            exp.printStackTrace();
        }
    }

    private void registerButtonClick(Button.ClickEvent e) {

        if (null != passwordFieldRegistration.getValue() && passwordFieldRegistration.getValue().equals(confirmPasswordField.getValue())) {
            UserRole customerUserRole = roleService.findByRole(customerRole);
            Set<UserRole> roles = new HashSet<>();
            roles.add(customerUserRole);

            userService.save(usernameField.getValue(), passwordFieldRegistration.getValue(), roles);
            securityService.autoLogin(usernameField.getValue(), passwordFieldRegistration.getValue());

            //redirect to main page
            getPage().setLocation("/main");
        } else {
            //ToDo: add logging
        }
    }

    private void resisterOnAuthorizationUIButtonClick(Button.ClickEvent e) {
        getPage().setLocation("/register");
    }

    public Boolean isAuthorizationUI() {
        return authorizationUIFlag;
    }

    void setAuthorizationUIFlag(Boolean flag) {
        authorizationUIFlag = flag;
    }

    public Boolean isRegistrationUI() {
        return registrationUIFlag;
    }

    void setRegistrationUIFlag(Boolean flag) {
        registrationUIFlag = flag;
    }
}
