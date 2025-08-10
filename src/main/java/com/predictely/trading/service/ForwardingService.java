package com.predictely.trading.service;

import com.predictely.trading.model.WebhookPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ForwardingService {

    @Value("${webhook.password}")
    private String webhookPassword;

    private static final Logger logger = LoggerFactory.getLogger(ForwardingService.class);

    private final RestTemplate restTemplate;

    private final List<String> baseUrls = Arrays.asList(
            "http://localhost:5001",
            "http://localhost:5002"
    );

    public ForwardingService() {
        this.restTemplate = new RestTemplate();
    }

    public void forwardPayload(WebhookPayload payload) {
        String path = "/webhook/process?password=" + webhookPassword;

        for (String baseUrl : baseUrls) {
            String url = baseUrl + path;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<WebhookPayload> request = new HttpEntity<>(payload, headers);

            sendRequest(url, request);
        }
    }

    public void forwardTrendUp() {
        forwardTrend("/webhook/process/wave/up");
    }

    public void forwardTrendDown() {
        forwardTrend("/webhook/process/wave/down");
    }

    private void forwardTrend(String path) {
        String fullPath = path + "?password=" + webhookPassword;

        for (String baseUrl : baseUrls) {
            String url = baseUrl + fullPath;
            HttpHeaders headers = new HttpHeaders(); // No Content-Type
            HttpEntity<String> request = new HttpEntity<>(null, headers);

            sendRequest(url, request);
        }
    }

    private void sendRequest(String url, HttpEntity<?> request) {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            logger.info("Forwarded to {}. Status: {}, Response: {}", url, response.getStatusCode(), response.getBody());
        } catch (Exception e) {
            logger.error("Error forwarding request to {}: {}", url, e.getMessage());
        }
    }
}
