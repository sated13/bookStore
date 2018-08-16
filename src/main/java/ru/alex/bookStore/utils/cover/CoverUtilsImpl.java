package ru.alex.bookStore.utils.cover;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alex.bookStore.entities.Cover;
import ru.alex.bookStore.utils.PropertiesValuesService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Service
@Slf4j
@NoArgsConstructor
public class CoverUtilsImpl implements CoverUtils {

    @Autowired
    PropertiesValuesService propertiesValuesService;

    @Override
    public byte[] getPictureOfBookCover(Cover cover) {
        byte[] pictureOfBookCover = cover.getPictureOfBookCover();

        if (pictureOfBookCover.length == 0) {
            try {
                String coversLocation = propertiesValuesService.getCoversLocation();
                String pathToFile = coversLocation + cover.getFileName();

                BufferedImage coverImage = ImageIO.read(new File(pathToFile));
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                ImageIO.write(coverImage, "jpg", byteArrayOutputStream);
                pictureOfBookCover = byteArrayOutputStream.toByteArray();
            } catch (IOException e) {
                log.error("Error during getting book cover: {}", e);
            } catch (Exception e) {
                log.error("Error: {}", e);
            }
        }
        return pictureOfBookCover;
    }

    @Override
    public void setPictureOfBookCover(Cover cover, byte[] pictureOfBookCover) {
        cover.setPictureOfBookCover(pictureOfBookCover);

        if (pictureOfBookCover.length != 0) {
            try {
                String coversLocation = propertiesValuesService.getCoversLocation();
                String pathToFile = coversLocation + cover.getFileName();

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pictureOfBookCover);
                BufferedImage coverImage = ImageIO.read(byteArrayInputStream);
                ImageIO.write(coverImage, "jpg", new File(pathToFile));
            } catch (Exception e) {
                log.error("Error during saving book cover: {}", e);
            }
        }
    }

    @Override
    public boolean deletePictureOfBookCover(Cover cover) {
        if (null != cover) {
            String coversLocation = propertiesValuesService.getCoversLocation();
            String pathToFile = coversLocation + cover.getFileName();

            try {
                File coverFile = new File(pathToFile);
                return coverFile.delete();
            } catch (Exception e) {
                log.error("Error during deleting cover {}: {}", cover, e);
            }
        }
        return false;
    }
}
