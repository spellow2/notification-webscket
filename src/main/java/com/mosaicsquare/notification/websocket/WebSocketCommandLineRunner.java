package com.mosaicsquare.notification.websocket;

import com.mosaicsquare.notification.websocket.config.NotificationConfig;
import com.mosaicsquare.notification.websocket.websocket.NotificationWebSocketSendService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

@EnableScheduling
@Component
public class WebSocketCommandLineRunner implements CommandLineRunner {

    private final NotificationConfig notificationConfig;
    private final NotificationWebSocketSendService webSocketServerService;


    public WebSocketCommandLineRunner(NotificationConfig notificationConfig, NotificationWebSocketSendService webSocketServerService) {
        this.notificationConfig = notificationConfig;
        this.webSocketServerService = webSocketServerService;
    }

    @Override
    //ToDo 테스트 코드는 로직 개발 완료후 Test로 이동한다.
    public void run(String... args) {
        if ( true == notificationConfig.getWebSocketTest() ) {
            Timer sendTimer = new Timer("WebsocketTest");
            sendTimer.schedule(new TimerTask() {
                public void run() {
                    webSocketServerService.broadcastAllSend("test message send");
                }
            }, 100L, notificationConfig.getWebSocketTestInterval() );
        }
    }
}
