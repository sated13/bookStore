package ru.alex.bookStore.ui;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import ru.alex.bookStore.utils.LogService;

@SpringUI(path = "/logConfigurator")
public class LogConfiguratorUI extends UI {

    @Autowired
    private LogService logService;

    @Override
    protected void init(VaadinRequest request) {
        Window contentWindow = new Window();
        contentWindow.setSizeFull();

        int componentsHeight = 0;

        FormLayout contentLayout = new FormLayout();

        Label pathToLogConfigFile = new Label("Path to log config file: " + logService.getLogConfigFilePath());
        componentsHeight += pathToLogConfigFile.getHeight();

        Button loadLogConfigurationButton = new Button("Load log configuration", this::loadLogConfiguration);
        componentsHeight += loadLogConfigurationButton.getHeight();

        Label logConfigurationLabel = new Label("Log configuration");
        componentsHeight += logConfigurationLabel.getHeight();

        TextArea logConfigurationTextArea = new TextArea();
        logConfigurationTextArea.setReadOnly(true);
        logConfigurationTextArea.setWidth(100f, Unit.PERCENTAGE);
        logConfigurationTextArea.setHeight((Page.getCurrent().getBrowserWindowHeight() - componentsHeight - 250) + "px");

        logConfigurationTextArea.setValue(logService.getLogConfiguration());

        contentLayout.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        contentLayout.addComponents(pathToLogConfigFile, loadLogConfigurationButton, logConfigurationLabel, logConfigurationTextArea);
        contentLayout.setSizeFull();
        contentLayout.setStyleName(ValoTheme.LAYOUT_CARD);

        contentWindow.setContent(contentLayout);
        contentWindow.setClosable(false);
        contentWindow.setResizable(false);
        contentWindow.center();

        addWindow(contentWindow);
    }

    private void loadLogConfiguration(Button.ClickEvent event) {
        logService.updateLogConfiguration();
        Page.getCurrent().reload();
    }
}
