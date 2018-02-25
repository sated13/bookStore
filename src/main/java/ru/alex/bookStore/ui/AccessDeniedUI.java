package ru.alex.bookStore.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;

@SpringUI(path = "/accessDenied")
public class AccessDeniedUI extends UI{

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Window window = new Window("Authorization");

        final VerticalLayout verticalPanel = new VerticalLayout();
        verticalPanel.addComponent(new Label( "Access denied"));

        window.center();
        window.setResizable(false);
        window.setClosable(false);
        addWindow(window);
    }
}
