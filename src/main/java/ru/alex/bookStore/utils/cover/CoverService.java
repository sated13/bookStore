package ru.alex.bookStore.utils.cover;

import ru.alex.bookStore.entities.Cover;

public interface CoverService {

    boolean save(Cover cover);

    boolean delete(Cover cover);

    Cover createEmptyCover();

    void setBookId(Cover cover, Long bookId);
}
