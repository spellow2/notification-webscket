package com.mosaicsquare.notification.websocket.notification;

import com.mosaicsquare.notification.websocket.config.NotificationConfig;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

@Service
@Transactional
public class NotificationService {
    public Map<Long,String> getNotificationUserList(Map<String,String> mapMsg) {
        Map<Long, String> mapNotificationMemberInfo = new HashMap<>();

        //ToDo 메시지를 받아야 하는 유저를 DB에서 검색한다.

        return mapNotificationMemberInfo;
    }
}
