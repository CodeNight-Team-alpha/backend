package com.riskpulse.backend.application.service;

import com.riskpulse.backend.domain.model.UserState;
import com.riskpulse.backend.persistence.entity.UserStateEntity;
import com.riskpulse.backend.persistence.projection.UserStateMetricsProjection;
import com.riskpulse.backend.persistence.repository.UserStateMetricsRepository;
import com.riskpulse.backend.persistence.repository.UserStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MetricsService {

    private final UserStateMetricsRepository metricsRepository;
    private final UserStateRepository userStateRepository;

    @Transactional
    public int computeAndUpsertUserState(LocalDate asOfDate) {
        LocalDate startDate = asOfDate.minusDays(6);
        List<UserStateMetricsProjection> rows = metricsRepository.findMetricsByDateRange(startDate, asOfDate);

        for (UserStateMetricsProjection row : rows) {
            UserState state = UserState.fromMetrics(
                    row.getSpendToday(),
                    row.getUniqueCategoriesToday(),
                    row.getElectronicsSpendToday(),
                    row.getSpend7d());
            UserStateEntity entity = toEntity(row.getUserId(), asOfDate, state);
            userStateRepository.save(entity);
        }
        return rows.size();
    }

    private static UserStateEntity toEntity(String userId, LocalDate asOfDate, UserState state) {
        return UserStateEntity.of(
                userId,
                asOfDate,
                state.getSpendToday(),
                state.getUniqueCategoriesToday(),
                state.getElectronicsSpendToday(),
                state.getSpend7d());
    }
}
