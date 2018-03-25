package ru.alex.bookStore.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;

@SpringUI(path = "/register")
public class RegistrationUI extends BaseUI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setRegistrationUIFlag(true);
        createForm("register", vaadinRequest);
    }
}
