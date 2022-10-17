package com.mosaicsquare.notification.websocket.database.notification;


import com.mosaicsquare.notification.websocket.database.notification.dto.Notification;
import org.springframework.data.repository.CrudRepository;

public interface NotificationRepository extends CrudRepository<Notification, Long> {
}
