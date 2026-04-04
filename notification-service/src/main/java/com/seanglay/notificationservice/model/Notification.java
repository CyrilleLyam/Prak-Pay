package com.seanglay.notificationservice.model;

import com.seanglay.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "notifications", indexes = {@Index(name = "idx_notification_account_id", columnList = "accountId"), @Index(name = "idx_notification_type", columnList = "type"), @Index(name = "idx_notification_status", columnList = "status"), @Index(name = "idx_notification_account_status", columnList = "accountId, status")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID accountId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String message;

    private java.math.BigDecimal amount;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type = NotificationType.ACCOUNT_REGISTERED;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> metadata;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status = NotificationStatus.UNREAD;

    public enum NotificationType {
        // Account events
        ACCOUNT_REGISTERED, ACCOUNT_LOGGED_IN,

        // Transaction events
        DEPOSIT, WITHDRAWAL, TRANSFER_SENT, TRANSFER_RECEIVED, PAYMENT,

        // Security events
        PASSWORD_CHANGED, ACCOUNT_SUSPENDED, ACCOUNT_ACTIVATED
    }

    public enum NotificationStatus {
        UNREAD, READ, SENT
    }
}
