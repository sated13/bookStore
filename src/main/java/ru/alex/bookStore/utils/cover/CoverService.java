package ru.alex.bookStore.utils.cover;

public interface CoverService {

    boolean save(byte[] cover);

    boolean delete(byte[] cover);
}
