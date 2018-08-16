package ru.alex.bookStore.utils.ui;

import com.vaadin.server.Sizeable;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.alex.bookStore.entities.Book;
import ru.alex.bookStore.utils.cover.CoverUtils;
import ru.alex.bookStore.utils.shoppingBasket.ShoppingBasketService;
import ru.alex.bookStore.utils.users.UserService;

import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;

@Service
public class UiUtils {

    @Autowired
    ShoppingBasketService basketService;
    @Autowired
    UserService userService;
    @Autowired
    CoverUtils coverUtils;

    public FormLayout createLayoutForBookDetails(@NotNull Book book, @NotNull AbstractOrderedLayout layout) {
        FormLayout bookDetailsLayout = new FormLayout();

        HorizontalSplitPanel layoutForComponents = new HorizontalSplitPanel();
        layoutForComponents.setSizeFull();
        layoutForComponents.setSplitPosition(15F, Sizeable.Unit.PERCENTAGE);
        layoutForComponents.setResponsive(false);

        //left panel

        VerticalLayout leftPanelWithPicture = new VerticalLayout();
        leftPanelWithPicture.setSizeFull();

        ImageUploader pictureOfBookCoverImageUploader = new ImageUploader();
        pictureOfBookCoverImageUploader.setSizeFull();

        if (null != book.getPictureOfBookCover() &&
                book.getPictureOfBookCover().isPresented()) {
            pictureOfBookCoverImageUploader.setOutputStreamForImage(coverUtils.getPictureOfBookCover(book.getPictureOfBookCover()));
            pictureOfBookCoverImageUploader.resetProgressbar();
            pictureOfBookCoverImageUploader.showImage();

            Image coverImage = new Image();

            StreamResource.StreamSource streamResource = (StreamResource.StreamSource)
                    () -> new ByteArrayInputStream(coverUtils.getPictureOfBookCover(book.getPictureOfBookCover()));

            if (coverImage.getSource() == null) {
                coverImage.setSource(new StreamResource(streamResource, ""));
            } else {
                StreamResource resource = (StreamResource) coverImage.getSource();
                resource.setStreamSource(streamResource);
                resource.setFilename("");
            }

            coverImage.setSizeFull();
            coverImage.markAsDirty();
            leftPanelWithPicture.addComponent(coverImage);
        }

        layoutForComponents.addComponent(leftPanelWithPicture);

        //right panel

        VerticalLayout rightPanelWithDetails = new VerticalLayout();
        rightPanelWithDetails.setSizeFull();

        Label bookTitleLabel = new Label("Title: " + book.getBookTitle());
        bookTitleLabel.setWidth(100f, Sizeable.Unit.PERCENTAGE);
        TextArea authorsTextArea = new TextArea("Authors: ");
        authorsTextArea.setValue(StringUtils.collectionToDelimitedString(book.getAuthors(), " "));
        authorsTextArea.setReadOnly(true);
        authorsTextArea.setWidth(100f, Sizeable.Unit.PERCENTAGE);

        rightPanelWithDetails.addComponents(bookTitleLabel, authorsTextArea);

        if (!StringUtils.isEmpty(book.getDescription())) {
            TextArea descriptionTextArea = new TextArea("Description");
            descriptionTextArea.setValue(book.getDescription());
            descriptionTextArea.setReadOnly(true);
            descriptionTextArea.setWidth(100f, Sizeable.Unit.PERCENTAGE);
            rightPanelWithDetails.addComponent(descriptionTextArea);
        }

        if (book.getYear() != 0) {
            Label yearLabel = new Label("Year: " + book.getYear());
            yearLabel.setWidth(100f, Sizeable.Unit.PERCENTAGE);
            rightPanelWithDetails.addComponent(yearLabel);
        }

        if (!StringUtils.isEmpty(book.getPublishingHouse())) {
            Label publishingHouseLabel = new Label("Publishing house: " + book.getPublishingHouse());
            publishingHouseLabel.setWidth(100f, Sizeable.Unit.PERCENTAGE);
            rightPanelWithDetails.addComponent(publishingHouseLabel);
        }

        if (book.getPrice() != 0.0) {
            Label priceLabel = new Label("Price, $: " + (Math.rint(book.getPrice() * 100) / 100));
            priceLabel.setWidth(100f, Sizeable.Unit.PERCENTAGE);
            rightPanelWithDetails.addComponent(priceLabel);
        }

        if (book.getNumberOfPages() != 0) {
            Label numberOfPagesLabel = new Label("Number of pages: " + book.getNumberOfPages());
            numberOfPagesLabel.setWidth(100f, Sizeable.Unit.PERCENTAGE);
            rightPanelWithDetails.addComponent(numberOfPagesLabel);
        }

        if (book.getNumberOfCopies() != 0) {
            Label numberOfCopiesLabel = new Label("Number of copies: " + book.getNumberOfCopies());
            numberOfCopiesLabel.setWidth(100f, Sizeable.Unit.PERCENTAGE);
            rightPanelWithDetails.addComponent(numberOfCopiesLabel);
        }

        if (null != layout) {
            rightPanelWithDetails.addComponent(layout);
        }

        layoutForComponents.addComponent(rightPanelWithDetails);

        bookDetailsLayout.addComponent(layoutForComponents);
        bookDetailsLayout.setWidth(100f, Sizeable.Unit.PERCENTAGE);

        return bookDetailsLayout;
    }
}
