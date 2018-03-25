package ru.alex.bookStore.utils.security;

public interface SecurityService {

    String findLoggedInUsername();

    void autoLogin(String username, String password);

    void getAllSessions();
}
