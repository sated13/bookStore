package ru.alex.bookStore.utils;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class LogService {

    @Autowired
    private Environment environment;

    public void updateLogConfiguration() {

        /*if (Files.exists(Paths.get(getLogConfigFilePath()))) {
            PropertyConfigurator.configure(getLogConfigFilePath());
        }*/
    }

    public String getLogConfiguration() {
        /*File file = new File(getLogConfigFilePath());
        StringBuffer config = new StringBuffer();
        try {
            if (file.exists()) {
                for (String line : Files.readAllLines(file.toPath())) {
                    config.append(line)
                            .append(System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config.toString();*/
        return "empty log configuration";
    }

    public String getLogConfigFilePath() {
        //return System.getProperty("user.dir")
        //        + File.separator + environment.getProperty("ru.alex.bookStore.logConfigurationFileName");
        return "";
    }
}
