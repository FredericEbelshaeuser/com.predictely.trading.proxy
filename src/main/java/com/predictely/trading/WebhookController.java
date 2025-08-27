package com.predictely.trading;

import com.predictely.trading.model.OrderblocksPayload;
import com.predictely.trading.model.WebhookPayload;
import com.predictely.trading.service.ForwardingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);
    private static final String EXPECTED_PASSWORD = "U2pQcv9g2XLevRTWPGTf";

    private final ForwardingService forwardingService;
    private final RestTemplate restTemplate = new RestTemplate();

    public WebhookController(ForwardingService forwardingService) {
        this.forwardingService = forwardingService;
    }

    @PostMapping("/process")
    public ResponseEntity<String> receiveWebhook(@RequestBody WebhookPayload payload, @RequestParam(name = "password") String password) {
        if (!EXPECTED_PASSWORD.equals(password)) {
            logger.warn("Rejected UP request due to invalid password");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid password");
        }

        logger.info("Received webhook: {}", payload);
        forwardingService.forwardPayload(payload);
        return ResponseEntity.ok("Received and forwarded");
    }

    @PostMapping("/process/orderblocks")
    public ResponseEntity<String> receiveWebhookOrderblock(@RequestBody OrderblocksPayload payload, @RequestParam(name = "password") String password) {
        if (!EXPECTED_PASSWORD.equals(password)) {
            logger.warn("Rejected UP request due to invalid password");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid password");
        }

        logger.info("Received webhook: {}", payload);
        forwardingService.forwardPayload(payload);
        return ResponseEntity.ok("Received and forwarded");
    }

    @PostMapping("/process/orderblocks/update")
    public String receiveWebhookOrderblocksUpdate(@RequestBody OrderblocksPayload orderBlock, @RequestParam(name = "password") String password) {
        forwardingService.forwardOrderblockUpdate(orderBlock);
        return "Orderblock updated";
    }

    @PostMapping(value = "/process/wave/up")
    public ResponseEntity<String> receiveWebhookUptrend(@RequestParam(name = "password") String password) {
        if (!EXPECTED_PASSWORD.equals(password)) {
            logger.warn("Rejected UP request due to invalid password");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid password");
        }

        forwardingService.forwardTrendUp();
        return ResponseEntity.ok("received");
    }

    @PostMapping(value = "/process/wave/down")
    public ResponseEntity<String> receiveWebhookDowntrend(@RequestParam(name = "password") String password) {
        if (!EXPECTED_PASSWORD.equals(password)) {
            logger.warn("Rejected DOWN request due to invalid password");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid password");
        }

        forwardingService.forwardTrendDown();
        return ResponseEntity.ok("received");
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    // ✅ New GET endpoint
    @GetMapping("/getTunnelPwd")
    public ResponseEntity<String> getTunnelPassword() {
        try {
            String response = restTemplate.getForObject("https://loca.lt/mytunnelpassword", String.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to fetch tunnel password", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching tunnel password");
        }
    }
}
