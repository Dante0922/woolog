package com.woolog.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Base64;

@Data
@Getter
@ConfigurationProperties("woolog")
public class AppConfig {
    private byte[] jwtKey;

    public void setJwtKey(String jwtKey) {
        this.jwtKey = Base64.getDecoder().decode(jwtKey);
    }
}
