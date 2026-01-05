package com.nixora.loan.TrelloIntegration.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class TrelloConfig {

    @Value("${trello.api-key}")
    private String apiKey;

    @Value("${trello.token}")
    private String token;

    @Value("${trello.base-url}")
    private String baseUrl;
}

