package com.nixora.auth.service;

import com.nixora.auth.utils.MailBody;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final Resend resend;

    public EmailService() {
        this.resend = new Resend(System.getenv("RESEND_API_KEY"));
    }

    public void sendMessage(MailBody mailBody) throws ResendException {

        CreateEmailOptions options = CreateEmailOptions.builder()
                .from("Nixora <onboarding@resend.dev>") // REQUIRED on free plan
                .to(mailBody.to())
                .subject(mailBody.subject())
                .html("<p>" + mailBody.text() + "</p>")
                .build();

        CreateEmailResponse response = resend.emails().send(options);

        System.out.println("Email sent. ID = " + response.getId());
    }
}
