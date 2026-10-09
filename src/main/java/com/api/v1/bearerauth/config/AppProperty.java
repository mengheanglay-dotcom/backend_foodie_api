package com.api.v1.bearerauth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperty {
    private final Jwt jwt = new Jwt();

    @Data
    public static class Jwt {
        private Long accessExpiration;
        private Long refreshExpiration;
        private String secret;
    }
}
