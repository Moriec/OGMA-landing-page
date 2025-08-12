package ru.ogma.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesSinglton {
    private static Properties properties;

    public static Properties getInstance() throws IOException {
        return properties;
    }

    public static void addLocationConfigFile(String locationConfFile) throws IOException {
        properties = new Properties();
        properties.load(new FileInputStream(locationConfFile));
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
