package ru.alex.bookStore.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;

@SpringUI(path = "/register")
public class RegistrationUI extends BaseUI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setRegistrationUIflag(true);
        createForm("register", vaadinRequest);
    }
}
