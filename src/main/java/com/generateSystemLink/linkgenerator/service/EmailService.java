package com.generateSystemLink.linkgenerator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.Data;

@Service
@Data
public class EmailService {

    @Value("${email.api.url}")
    private String emailApiUrl;

    @Value("${email.api.key}")
    private String emailApiKey;

    public void sendEmail(String to, String subject, String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + emailApiKey);
        headers.set("Content-Type", "application/json");

        RestTemplate restTemplate = new RestTemplate();

        EmailRequest emailRequest = new EmailRequest(to, subject, body);

        HttpEntity<EmailRequest> requestEntity = new HttpEntity<>(emailRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                emailApiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to send email");
        }
    }
    private static class EmailRequest {
        private String to;
        private String subject;
        private String body;

        public EmailRequest(String to, String subject, String body) {
            this.to = to;
            this.subject = subject;
            this.body = body;
        }
    }
}
