package com.example.body.Config;

import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class DataBaseConfig {
    Properties prop = ConfigReader.loadConfig();

    protected String dbHost = prop.getProperty("dbHost");
    protected String dbPort = prop.getProperty("dbPort");
    protected String dbUser = prop.getProperty("dbUser");
    protected String dbPass = prop.getProperty("dbPass");
    protected String dbName = prop.getProperty("dbName");


}
