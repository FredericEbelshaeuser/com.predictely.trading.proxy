package com.predictely.trading.service;

import com.predictely.trading.model.WebhookPayload;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ForwardingService {

	@Value("${webhook.password}")
	private String webhookPassword;

	private static final Logger logger = LoggerFactory.getLogger(ForwardingService.class);

	private final RestTemplate restTemplate;

	public ForwardingService() {
		this.restTemplate = new RestTemplate();
	}

	public void forwardPayload(WebhookPayload payload) {

		ArrayList<String> urls = new ArrayList<String>();
		urls.add("http://localhost:5001/webhook/process?password=" + webhookPassword);
//    	urls.add("http://localhost:5002/webhook/process?password="+webhookPassword);

		for (String url : urls) {

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<WebhookPayload> request = new HttpEntity<>(payload, headers);

			try {
				ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
				logger.info("Forwarded to {}. Status: {}, Response: {}", url, response.getStatusCode(),
						response.getBody());
			} catch (Exception e) {
				logger.error("Error forwarding webhook payload: {}", e.getMessage());
			}

		}
	}
}
