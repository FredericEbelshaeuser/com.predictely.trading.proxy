package com.predictely.trading.service;

import com.predictely.trading.model.OrderblocksPayload;
import com.predictely.trading.model.WebhookPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PreDestroy;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ForwardingService {

    @Value("${webhook.password}")
    private String webhookPassword;

    private static final Logger logger = LoggerFactory.getLogger(ForwardingService.class);

    private final RestTemplate restTemplate;

    // Run each outbound request on its own virtual thread
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    private final List<String> baseUrls = Arrays.asList(
            "http://localhost:5001",
            "http://localhost:5002",
            "http://localhost:5003",
            "http://localhost:5004"
    );

    public ForwardingService() {
        this.restTemplate = new RestTemplate();
    }

    /** Forward TradingView-style webhook payloads concurrently */
    public void forwardPayload(WebhookPayload payload) {
        final String path = "/webhook/process?password=" + webhookPassword;
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<WebhookPayload> request = new HttpEntity<>(payload, headers);

        // fire-and-forget fan-out
        for (String baseUrl : baseUrls) {
            String url = baseUrl + path;
            sendRequestAsync(url, request);
        }
    }

    /** Forward Orderblocks payloads concurrently */
    public void forwardPayload(OrderblocksPayload payload) {
        final String path = "/webhook/process/orderblocks?password=" + webhookPassword;
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<OrderblocksPayload> request = new HttpEntity<>(payload, headers);

        for (String baseUrl : baseUrls) {
            String url = baseUrl + path;
            sendRequestAsync(url, request);
        }
    }

    /** Forward trend notifications concurrently (no body) */
    public void forwardTrendUp() { forwardTrend("/webhook/process/wave/up"); }

    public void forwardTrendDown() { forwardTrend("/webhook/process/wave/down"); }

    private void forwardTrend(String path) {
        final String fullPath = path + "?password=" + webhookPassword;
        final HttpEntity<Void> request = new HttpEntity<>(null); // no Content-Type needed

        for (String baseUrl : baseUrls) {
            String url = baseUrl + fullPath;
            sendRequestAsync(url, request);
        }
    }

    /** Submit a POST request on a virtual thread; logs result/errors */
    private <T> CompletableFuture<Void> sendRequestAsync(String url, HttpEntity<T> request) {
        return CompletableFuture.runAsync(() -> {
            try {
                ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
                logger.info("Forwarded to {}. Status: {} Response: {}", url, response.getStatusCode(), response.getBody());
            } catch (Exception e) {
                logger.error("Error forwarding request to {}: {}", url, e.getMessage());
            }
        }, executor);
    }

    @PreDestroy
    public void shutdown() {
        executor.shutdown();
    }

	public void forwardOrderblockUpdate(OrderblocksPayload orderBlock) {
        final String path = "/webhook/process/orderblocks/update?password=" + webhookPassword;
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<OrderblocksPayload> request = new HttpEntity<>(orderBlock, headers);

        // fire-and-forget fan-out
        for (String baseUrl : baseUrls) {
            String url = baseUrl + path;
            sendRequestAsync(url, request);
        }		
	}
}
