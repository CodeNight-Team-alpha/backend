package com.riskpulse.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record NotificationItemDto(Long id, String sourceRef, String message, String createdAt) {}
