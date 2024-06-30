package com.evm.ms.notifier.infrastructure.dto.entity;

import com.evm.ms.notifier.domain.NotificationConfig;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "email")
public class EmailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "notification_config_id", nullable = false)
    private NotificationConfigEntity notificationConfig;

    @Column(nullable = false, length = 255)
    private String email;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "updated_by", nullable = false)
    private UUID updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

}
