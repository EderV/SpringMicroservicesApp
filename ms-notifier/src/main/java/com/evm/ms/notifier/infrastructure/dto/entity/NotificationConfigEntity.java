package com.evm.ms.notifier.infrastructure.dto.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification_config")
public class NotificationConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String category;

    @OneToMany(mappedBy = "notificationConfig", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EmailConfigEntity> emails;

    @OneToMany(mappedBy = "notificationConfig", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AndroidConfigEntity> androids;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "updated_by", nullable = false)
    private UUID updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    @Column(name = "updated_at")
    private Date updatedAt;

}
