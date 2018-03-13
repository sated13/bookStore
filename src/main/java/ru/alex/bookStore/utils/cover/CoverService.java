package ru.alex.bookStore.utils.cover;

import ru.alex.bookStore.entities.Cover;

public interface CoverService {

    boolean save(byte[] cover);

    boolean save(Cover cover);

    boolean delete(byte[] cover);
}
