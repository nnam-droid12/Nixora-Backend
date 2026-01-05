package com.nixora.loan.GoogleCalendarIntegration.services;


import com.nixora.auth.entities.User;
import com.nixora.loan.GoogleCalendarIntegration.entity.GoogleOAuthToken;
import com.nixora.loan.LoanSchedule.entity.LoanScheduleEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoogleCalendarService {

    private final GoogleOAuthService oauth;

    public void pushEvents(User user, UUID loanId, List<LoanScheduleEvent> events) throws Exception {
        GoogleOAuthToken token = oauth.token(user);

        if (token.getExpiry().isBefore(LocalDateTime.now().plusMinutes(1))) {
            oauth.refreshToken(token);
        }

        final com.google.api.client.http.HttpTransport HTTP_TRANSPORT = new com.google.api.client.http.javanet.NetHttpTransport();
        final com.google.api.client.json.JsonFactory JSON_FACTORY = com.google.api.client.json.gson.GsonFactory.getDefaultInstance();

        com.google.api.client.auth.oauth2.Credential credential = new com.google.api.client.auth.oauth2.Credential(
                com.google.api.client.auth.oauth2.BearerToken.authorizationHeaderAccessMethod())
                .setAccessToken(token.getAccessToken());

        com.google.api.services.calendar.Calendar calendarService = new com.google.api.services.calendar.Calendar.Builder(
                HTTP_TRANSPORT,
                JSON_FACTORY,
                credential
        ).setApplicationName("Nixora Loan Calendar").build();

        for (LoanScheduleEvent e : events) {
            if (e.getEventDate() == null) continue;

            com.google.api.services.calendar.model.Event event = new com.google.api.services.calendar.model.Event();
            event.setSummary(e.getType().name().replace("_", " "));
            event.setDescription(e.getDescription());

            com.google.api.client.util.DateTime startDateTime = new com.google.api.client.util.DateTime(e.getEventDate().toString());
            event.setStart(new com.google.api.services.calendar.model.EventDateTime().setDate(startDateTime));

            com.google.api.client.util.DateTime endDateTime = new com.google.api.client.util.DateTime(e.getEventDate().plusDays(1).toString());
            event.setEnd(new com.google.api.services.calendar.model.EventDateTime().setDate(endDateTime));


            com.google.api.services.calendar.Calendar.Events.Insert request =
                    calendarService.events().insert("primary", event);

            request.execute();
        }
    }
}