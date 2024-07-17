package com.evm.ms.notifier.infrastructure.repository;

import com.evm.ms.notifier.infrastructure.dto.entity.NotificationConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationConfigRepository extends JpaRepository<NotificationConfigEntity, UUID> {

    @Query("SELECT n FROM NotificationConfigEntity n " +
            "LEFT JOIN FETCH n.androids a " +
            "LEFT JOIN FETCH n.emails e " +
            "WHERE n.userId = ?1 " +
            "ORDER BY n.updatedAt DESC")
    Optional<NotificationConfigEntity> findLastSavedConfigByUserUUID(UUID uuid);

}
