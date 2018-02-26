package ru.alex.bookStore.ui;

import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import ru.alex.bookStore.Annotations.AfterExecutingRedirectTo;

@SpringUI(path = "/login")
@Title("LoginPage")
public class AuthorizationUI extends BaseUI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        createForm("authorization", vaadinRequest);
    }

    @AfterExecutingRedirectTo(pathToRedirect = "/main")
    @Override
    protected void loginButtonClick(Button.ClickEvent e) {
        super.loginButtonClick(e);
    }
}
