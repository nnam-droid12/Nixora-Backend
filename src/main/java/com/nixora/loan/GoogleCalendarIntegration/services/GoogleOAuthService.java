package com.nixora.loan.GoogleCalendarIntegration.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nixora.auth.entities.User;
import com.nixora.loan.GoogleCalendarIntegration.entity.GoogleOAuthToken;
import com.nixora.loan.GoogleCalendarIntegration.repository.GoogleOAuthRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

    private final GoogleOAuthRepository repo;

    @Getter
    @Value("${google.client-id}")
    private String clientId;

    @Getter
    @Value("${google.client-secret}")
    private String clientSecret;

    @Value("${google.redirect-uri}")
    private String redirectUri;

    public String authUrl(String state) {
        return "https://accounts.google.com/o/oauth2/v2/auth" +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&scope=https://www.googleapis.com/auth/calendar.events" +
                "&access_type=offline" +
                "&prompt=consent" +
                "&state=" + state;
    }

    public void exchange(String code, Integer userId) throws Exception {

        var req = HttpRequest.newBuilder()
                .uri(new URI("https://oauth2.googleapis.com/token"))
                .POST(HttpRequest.BodyPublishers.ofString(
                        "client_id=" + clientId +
                                "&client_secret=" + clientSecret +
                                "&code=" + code +
                                "&grant_type=authorization_code" +
                                "&redirect_uri=" + redirectUri
                ))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        var res = HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());
        var json = new ObjectMapper().readTree(res.body());

        GoogleOAuthToken token = new GoogleOAuthToken();
        token.setUserId(userId);
        token.setAccessToken(json.get("access_token").asText());
        token.setRefreshToken(json.get("refresh_token").asText());
        token.setExpiry(LocalDateTime.now().plusSeconds(json.get("expires_in").asInt()));

        repo.save(token);
    }


    public GoogleOAuthToken token(User u) {
        return repo.findById(u.getUserId())
                .orElseThrow(() -> new RuntimeException("Google OAuth token not found"));
    }


    public String refreshToken(GoogleOAuthToken token) throws Exception {
        var req = HttpRequest.newBuilder()
                .uri(new URI("https://oauth2.googleapis.com/token"))
                .POST(HttpRequest.BodyPublishers.ofString(
                        "client_id=" + clientId +
                                "&client_secret=" + clientSecret +
                                "&refresh_token=" + token.getRefreshToken() +
                                "&grant_type=refresh_token"
                ))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        var res = HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());
        var json = new ObjectMapper().readTree(res.body());

        if (json.has("access_token")) {
            String newAccessToken = json.get("access_token").asText();
            token.setAccessToken(newAccessToken);
            token.setExpiry(LocalDateTime.now().plusSeconds(json.get("expires_in").asInt()));
            repo.save(token);
            return newAccessToken;
        }
        throw new RuntimeException("Failed to refresh Google token");
    }

}

