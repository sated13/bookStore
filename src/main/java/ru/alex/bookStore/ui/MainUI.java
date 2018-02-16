package ru.alex.bookStore.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

@SpringUI(path = "/main")
public class MainUI extends UI{

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Window window = new Window("");

        window.setContent(new TextArea("Main Page"));

        window.setSizeFull();
        window.setResizable(false);
        addWindow(window);
    }
}
