package com.riskpulse.backend.application.mapper;

import com.riskpulse.backend.api.dto.NotificationItemDto;
import com.riskpulse.backend.persistence.entity.NotificationEntity;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Maps notification entities to DTOs.
 */
public final class NotificationMapper {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    private NotificationMapper() {}

    public static List<NotificationItemDto> toDtoList(List<NotificationEntity> entities) {
        return entities.stream()
                .map(n -> new NotificationItemDto(
                        n.getId(),
                        n.getSourceRef(),
                        n.getMessage(),
                        n.getCreatedAt() != null ? n.getCreatedAt().format(ISO) : null))
                .collect(Collectors.toList());
    }
}
