package com.riskpulse.backend.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Placeholder for notification generation (e.g. from challenge awards or badge awards).
 * Engine step exists so orchestrator can call it; no-op for now.
 */
@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public int generateNotifications(LocalDate asOfDate) {
        log.debug("notifications step for asOfDate={} (no-op)", asOfDate);
        return 0;
    }
}
