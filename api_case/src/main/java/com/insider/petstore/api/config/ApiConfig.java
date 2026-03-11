package com.insider.petstore.api.config;

import java.io.InputStream;
import java.util.Properties;

public class ApiConfig {

    private static final String CONFIG_FILE = "api.properties";
    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try {
            InputStream inputStream = ApiConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
            if (inputStream != null) {
                properties.load(inputStream);
                inputStream.close();
            } else {
                throw new IllegalStateException("Properties file not found: " + CONFIG_FILE);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load properties file: " + CONFIG_FILE, e);
        }
    }

    public static String getBaseUrl() {
        return properties.getProperty("api.base.url");
    }
}
