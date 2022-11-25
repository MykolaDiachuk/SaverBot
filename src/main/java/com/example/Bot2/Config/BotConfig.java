package com.example.Bot2.Config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;



@Data
@Configuration
@Component
public class BotConfig {
    String botName = System.getenv("botName");
    String token = System.getenv("botToken");
}
