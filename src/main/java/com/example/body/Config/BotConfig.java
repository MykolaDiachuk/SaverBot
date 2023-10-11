package com.example.body.Config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Properties;


@Data
@Configuration
@Component
public class BotConfig {
    Properties prop = ConfigReader.loadConfig();
    String botName = prop.getProperty("botName");
    String token = prop.getProperty("botToken");

}
