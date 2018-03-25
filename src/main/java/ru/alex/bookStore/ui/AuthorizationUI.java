package ru.alex.bookStore.ui;

import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;

@SpringUI(path = "/login")
@Title("LoginPage")
public class AuthorizationUI extends BaseUI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setAuthorizationUIFlag(true);
        createForm("authorization", vaadinRequest);
    }
}
