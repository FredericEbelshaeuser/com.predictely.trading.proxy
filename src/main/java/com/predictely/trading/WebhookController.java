package com.predictely.trading;

import com.predictely.trading.model.WebhookPayload;
import com.predictely.trading.service.ForwardingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    private final ForwardingService forwardingService;

    public WebhookController(ForwardingService forwardingService) {
        this.forwardingService = forwardingService;
    }

    @PostMapping("/process")
    public ResponseEntity<String> receiveWebhook(@RequestBody WebhookPayload payload) {
        logger.info("Received webhook: {}", payload);
        forwardingService.forwardPayload(payload);
        return ResponseEntity.ok("Received and forwarded");
    }
}
