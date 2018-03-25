package ru.alex.bookStore.utils.users;

import org.springframework.stereotype.Component;
import ru.alex.bookStore.entities.User;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

@Component
public class LoggedUser implements HttpSessionBindingListener {

    private User user;
    private ActiveUserRepository activeUserRepository;

    public LoggedUser() { }

    public LoggedUser(User user, ActiveUserRepository activeUserRepository) {
        this.user = user;
        this.activeUserRepository = activeUserRepository;
    }

    @Override
    public void valueBound(HttpSessionBindingEvent httpSessionBindingEvent) {
        User user = (User) httpSessionBindingEvent.getValue();
        activeUserRepository.addActiveUsers(user);
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent httpSessionBindingEvent) {
        User user = (User) httpSessionBindingEvent.getValue();
        activeUserRepository.removeActiveUser(user);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ActiveUserRepository getActiveUserRepository() {
        return activeUserRepository;
    }

    public void setActiveUserRepository(ActiveUserRepository activeUserRepository) {
        this.activeUserRepository = activeUserRepository;
    }
}
