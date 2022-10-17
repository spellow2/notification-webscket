package com.mosaicsquare.notification.websocket.database.notification.dto;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="member_id", nullable = false)
    private Long memberId;

    @Column(name="event_message", columnDefinition="TEXT")
    private String eventMessage;

    @Column(name="is_read", columnDefinition="BIT(1)")
    private Boolean isRead;

    @Column(name="create_date")
    private Timestamp createDate;

    @Column(name="update_date")
    private Timestamp updateDdate;
}
