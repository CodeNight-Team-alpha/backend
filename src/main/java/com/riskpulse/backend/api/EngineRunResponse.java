package com.riskpulse.backend.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EngineRunResponse(String asOfDate, String status) {}