package com.riskpulse.backend.application.mapper;

import com.riskpulse.backend.api.dto.NotificationItemDto;
import com.riskpulse.backend.persistence.entity.NotificationEntity;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Maps notification entities to DTOs.
 */
public final class NotificationMapper {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private static final Pattern AWARD_DATE = Pattern.compile("(\\d{4}-\\d{2}-\\d{2})$");

    private NotificationMapper() {}

    public static List<NotificationItemDto> toDtoList(List<NotificationEntity> entities) {
        return entities.stream()
                .map(n -> new NotificationItemDto(
                        n.getId(),
                        n.getSourceRef(),
                        n.getMessage(),
                        resolveCompletedAt(n),
                        n.getCreatedAt() != null ? n.getCreatedAt().format(ISO) : null))
                .collect(Collectors.toList());
    }

    /** completedAt: entity'den veya source_ref (AW-...-yyyy-MM-dd) formatından. */
    static String resolveCompletedAt(NotificationEntity n) {
        if (n.getCompletedAt() != null) {
            return n.getCompletedAt().toString();
        }
        if (n.getSourceRef() != null) {
            var m = AWARD_DATE.matcher(n.getSourceRef());
            if (m.find()) return m.group(1);
        }
        return null;
    }
}
