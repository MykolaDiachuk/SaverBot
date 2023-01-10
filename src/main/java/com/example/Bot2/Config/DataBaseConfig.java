package com.example.Bot2.Config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class DataBaseConfig {
    protected String dbHost = System.getenv("dbHost");
    protected String dbPort = System.getenv("dbPort");
    protected String dbUser = System.getenv("dbUser");
    protected String dbPass = System.getenv("dbPass");
    protected String dbName = System.getenv("dbName");


}
