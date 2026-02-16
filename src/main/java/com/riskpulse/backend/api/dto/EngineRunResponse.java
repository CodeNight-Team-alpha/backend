package com.riskpulse.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EngineRunResponse(String asOfDate, String status) {}
