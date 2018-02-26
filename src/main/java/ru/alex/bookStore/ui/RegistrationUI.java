package ru.alex.bookStore.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import ru.alex.bookStore.Annotations.AfterExecutingRedirectTo;

@SpringUI(path = "/register")
public class RegistrationUI extends BaseUI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        createForm("register", vaadinRequest);
    }

    @AfterExecutingRedirectTo(pathToRedirect = "/main")
    @Override
    protected void registerButtonClick(Button.ClickEvent e) {
        super.registerButtonClick(e);
    }
}
