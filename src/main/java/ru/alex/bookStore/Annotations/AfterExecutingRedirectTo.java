package ru.alex.bookStore.Annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AfterExecutingRedirectTo {
    public String pathToRedirect();
}
