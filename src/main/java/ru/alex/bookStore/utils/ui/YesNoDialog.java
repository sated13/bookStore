package ru.alex.bookStore.utils.ui;

import com.vaadin.ui.*;

public class YesNoDialog extends Window implements Button.ClickListener {

    private Callback callback;
    Button yes = new Button("Yes", this);
    Button no = new Button("No", this);

    public YesNoDialog(String caption, String question, Callback callback) {
        super(caption);

        this.callback = callback;
        this.setResizable(false);
        this.setModal(true);

        VerticalLayout panel = new VerticalLayout();

        if (question != null) {
            panel.addComponent(new Label(question));
        }

        HorizontalLayout yesNoButtonsPanel = new HorizontalLayout();
        yesNoButtonsPanel.addComponent(yes);
        yesNoButtonsPanel.addComponent(no);

        panel.addComponent(yesNoButtonsPanel);
        panel.setComponentAlignment(yesNoButtonsPanel, Alignment.BOTTOM_RIGHT);
        setContent(panel);
    }

    public void buttonClick(Button.ClickEvent event) {
        this.close();
        callback.onDialogResult(event.getSource() == yes);
    }

    public interface Callback {

        public void onDialogResult(boolean resultIsYes);
    }
}
