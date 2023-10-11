package com.example.body.Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    public static Properties loadConfig() {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("/home/ubuntu/bot"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
}
