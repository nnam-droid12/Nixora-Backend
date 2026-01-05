package com.nixora.loan.TrelloIntegration.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class TrelloClient {

    private final TrelloConfig config;
    private final RestTemplate rest = new RestTemplate();

    public void createCard(String userToken, String listId, String title, String desc) {

        String url = "https://api.trello.com/1/cards"
                + "?key=" + config.getApiKey()
                + "&token=" + userToken
                + "&idList=" + listId
                + "&name=" + title
                + "&desc=" + desc;

        rest.postForObject(url, null, String.class);
    }
}
