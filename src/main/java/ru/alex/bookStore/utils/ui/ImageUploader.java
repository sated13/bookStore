package ru.alex.bookStore.utils.ui;

import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;

public class ImageUploader extends CustomComponent implements Upload.Receiver, Upload.SucceededListener,
        Upload.FailedListener, Upload.ProgressListener {

    String filename;
    ByteArrayOutputStream outputStreamForImage = new ByteArrayOutputStream(10240);
    ProgressBar progressBar = new ProgressBar(0.0f);
    Image coverImage = new Image("Cover");
    final HashSet<String> imageMimeTypes = new HashSet<>(Arrays.asList("image/gif", "image/png", "image/jpeg", "image/bmp"));
    Upload uploadComponent = new Upload("", null);
    Button resetImageButton = new Button("Reset", this::resetButtonClick);
    Label uploadCoverLabel = new Label("Upload cover");
    boolean isErrorFlag = false;

    public ImageUploader() {
        uploadComponent.setReceiver(this);
        uploadComponent.addFailedListener(this);
        uploadComponent.addSucceededListener(this);
        uploadComponent.addProgressListener(this);

        Panel panel = new Panel();
        VerticalLayout content = new VerticalLayout();
        HorizontalLayout horizontalLayoutForButtons = new HorizontalLayout();

        content.setSpacing(true);
        panel.setContent(content);

        horizontalLayoutForButtons.addComponents(uploadComponent, resetImageButton);
        horizontalLayoutForButtons.setComponentAlignment(resetImageButton, Alignment.BOTTOM_CENTER);

        content.addComponents(uploadCoverLabel, horizontalLayoutForButtons, progressBar, coverImage);
        content.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        progressBar.setVisible(false);
        progressBar.setWidth(100f, Unit.PERCENTAGE);
        coverImage.setVisible(false);

        setCompositionRoot(panel);
    }

    public OutputStream receiveUpload(String filename,
                                      String mimeType) {
        if (imageMimeTypes.contains(mimeType)) {
            this.filename = filename;
            outputStreamForImage.reset();
            isErrorFlag = false;
            return outputStreamForImage;
        } else {
            isErrorFlag = true;
            uploadComponent.interruptUpload();
        }
        return outputStreamForImage;
    }

    public void uploadSucceeded(Upload.SucceededEvent event) {
        if (!isErrorFlag) {
            showImage();
        }
    }

    @Override
    public void uploadFailed(Upload.FailedEvent event) {
        Notification.show("Upload failed", Notification.Type.ERROR_MESSAGE);
    }

    @Override
    public void updateProgress(long readBytes, long contentLength) {
        if (!isErrorFlag) {
            progressBar.setVisible(true);
            if (contentLength == -1) {
                progressBar.setIndeterminate(true);
            } else {
                progressBar.setValue((float) readBytes / (float) contentLength);
            }
        }
    }

    private void resetButtonClick(Button.ClickEvent event) {
        resetProgressbar();
        coverImage.setVisible(false);
        outputStreamForImage.reset();
    }

    public ByteArrayOutputStream getOutputStreamForImage() {
        return outputStreamForImage;
    }

    public void setOutputStreamForImage(byte[] bytes) {
        try {
            outputStreamForImage.reset();
            outputStreamForImage.write(bytes);
        } catch (IOException e) {
            //ToDo" add logging
            e.printStackTrace();
        }
    }

    public void resetProgressbar() {
        progressBar.setVisible(false);
        progressBar.setValue(0f);
    }

    public void showImage() {
        coverImage.setVisible(true);
        coverImage.setCaption("Uploaded cover: " + filename);

        StreamResource.StreamSource streamResource = (StreamResource.StreamSource)
                () -> new ByteArrayInputStream(outputStreamForImage.toByteArray());

        if (coverImage.getSource() == null) {
            coverImage.setSource(new StreamResource(streamResource, filename));
        } else {
            StreamResource resource = (StreamResource) coverImage.getSource();
            resource.setStreamSource(streamResource);
            resource.setFilename(filename);
        }

        coverImage.markAsDirty();
    }
}
