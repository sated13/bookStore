package ru.alex.bookStore.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class PropertiesValuesService {

    @Autowired
    private Environment env;
    private String os = System.getProperty("os.name").toLowerCase();

    public PropertiesValuesService() {
    }

    public boolean isWindows() {
        return os.contains("win");
    }

    public boolean isMac() {
        return os.contains("mac");
    }

    public boolean isUnix() {
        return os.contains("nix") || os.contains("nux") || os.contains("aix");
    }

    public boolean isSolaris() {
        return os.contains("sunos");
    }

    public String getCoversLocation() {
        String coversLocation = "";

        if (isWindows()) coversLocation = env.getProperty("ru.alex.pathToCovers.windows");
        if (isUnix() || isSolaris()) coversLocation = env.getProperty("ru.alex.pathToCovers.unix");

        return coversLocation;
    }
}
