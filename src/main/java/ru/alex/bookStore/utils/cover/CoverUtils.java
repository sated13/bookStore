package ru.alex.bookStore.utils.cover;

import ru.alex.bookStore.entities.Cover;

public interface CoverUtils {

    byte[] getPictureOfBookCover(Cover cover);

    void setPictureOfBookCover(Cover cover, byte[] pictureOfBookCover);

    boolean deletePictureOfBookCover(Cover cover);
}
