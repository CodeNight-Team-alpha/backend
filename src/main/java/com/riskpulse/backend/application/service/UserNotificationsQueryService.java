package com.riskpulse.backend.application.service;

import com.riskpulse.backend.api.dto.NotificationItemDto;
import com.riskpulse.backend.application.mapper.NotificationMapper;
import com.riskpulse.backend.persistence.entity.NotificationEntity;
import com.riskpulse.backend.persistence.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Application service: loads notifications from repo and uses mapper to build DTO list.
 */
@Service
@RequiredArgsConstructor
public class UserNotificationsQueryService {

    private final NotificationRepository notificationRepository;

    public List<NotificationItemDto> getNotifications(String userId) {
        List<NotificationEntity> entities = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return NotificationMapper.toDtoList(entities);
    }
}
