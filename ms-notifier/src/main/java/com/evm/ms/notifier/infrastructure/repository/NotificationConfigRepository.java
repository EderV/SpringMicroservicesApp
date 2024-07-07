package com.evm.ms.notifier.infrastructure.repository;

import com.evm.ms.notifier.infrastructure.dto.entity.NotificationConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationConfigRepository extends JpaRepository<NotificationConfigEntity, UUID> {

}
