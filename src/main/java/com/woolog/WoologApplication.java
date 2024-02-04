package com.woolog;

import com.woolog.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class WoologApplication {

    public static void main(String[] args) {
        SpringApplication.run(WoologApplication.class, args);
    }

}
