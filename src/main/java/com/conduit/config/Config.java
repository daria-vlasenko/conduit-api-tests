package com.conduit.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Config {

    private static final String PROPERTIES_FILE = "application.properties";
    private static final Properties PROPERTIES = load();

    private Config() {
    }

    public static String baseUrl() {
        return get("base.url");
    }

    public static String basePath() {
        return get("base.path");
    }

    public static String defaultPassword() {
        return get("default.password");
    }

    private static String get(String key) {
        String value = System.getProperty(key);
        if (isBlank(value)) {
            value = System.getenv(toEnvName(key));
        }
        if (isBlank(value)) {
            value = PROPERTIES.getProperty(key);
        }
        if (isBlank(value)) {
            throw new IllegalStateException("Missing configuration property: " + key);
        }
        return value.trim();
    }

    private static String toEnvName(String key) {
        return key.toUpperCase().replace('.', '_');
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static Properties load() {
        Properties properties = new Properties();
        try (InputStream stream = Config.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (stream == null) {
                throw new IllegalStateException(PROPERTIES_FILE + " not found on classpath");
            }
            properties.load(stream);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read " + PROPERTIES_FILE, e);
        }
        return properties;
    }
}
