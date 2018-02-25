package ru.alex.bookStore.utils;

public interface SecurityService {

    String findLoggedInUsername();

    void autoLogin(String username, String password);
}
