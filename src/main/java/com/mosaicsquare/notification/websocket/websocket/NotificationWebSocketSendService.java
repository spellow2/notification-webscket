package com.mosaicsquare.notification.websocket.websocket;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mosaicsquare.notification.websocket.config.NotificationConfig;
import com.mosaicsquare.notification.websocket.notification.NotificationSender;
import com.mosaicsquare.notification.websocket.notification.NotificationService;
import com.mosaicsquare.notification.websocket.websocket.message.SubscriptionMsg;
import com.sun.management.UnixOperatingSystemMXBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.concurrent.Executor;

@EnableAsync(proxyTargetClass=true)
@Service
public class NotificationWebSocketSendService implements NotificationSender {

    final private NotificationConfig notificationConfig;
    final private NotificationService notificationService;

    private Boolean isStart = Boolean.FALSE;
    private SocketIOServer server;
    private ObjectMapper mapper = new ObjectMapper();

    public NotificationWebSocketSendService(NotificationConfig notificationConfig, NotificationService notificationService) {
        this.notificationConfig = notificationConfig;
        this.notificationService = notificationService;

        System.out.println( "====== Server Info =====");
        System.out.println( this.getServerInfo() );
        System.out.println( "====== Server Info =====");

        if ( null == this.serverStart() ) {
            System.out.println( "Server Start Error!!");
            System.exit(0);
        }
    }

    @Bean(name="WebSocketSender")
    public Executor EmailSender() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(notificationConfig.getWebSocketWorkerThread()*2);
        taskExecutor.setMaxPoolSize(notificationConfig.getWebSocketWorkerThread()*10);
        taskExecutor.setQueueCapacity(notificationConfig.getWebSocketWorkerThread()*100);
        taskExecutor.setThreadNamePrefix("WebSocketSender-");
        taskExecutor.initialize();
        return taskExecutor;
    }

    public void broadcastAllSend(String message) {
        for (SocketIOClient client : server.getAllClients()) {
            sendToClient(client, new SubscriptionMsg(client.get("notice"), "notice", message));
        }
    }

    public void broadcastSend(SubscriptionMsg subMsg) {
        String subId = subMsg.getSubId();
        BroadcastOperations subRoom = server.getRoomOperations(subId);

        for (SocketIOClient client : subRoom.getClients()) {
            sendToClient(client, subMsg);
        }

//        if ( 0 < subRoom.getClients().size() ) {
//            subRoom.sendEvent(subId,subMsg);
//        } else {
//            System.out.println("broadcastSend failed. client is not join in Subscription["+subId+"].");
//        }
    }

    public void sendToClient(SocketIOClient client, SubscriptionMsg subMsg) {
        String subId = subMsg.getSubId();
        client.sendEvent(subId, subMsg);
    }

    public Map<String,Object> getServerInfo() {

        Map<String,Object> mapServerConfig = new LinkedHashMap<>();
        final UnixOperatingSystemMXBean osMBean =
                (UnixOperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        Map<String,Object> mapWebsocketStatus = new LinkedHashMap<>();
        mapWebsocketStatus.put("host", notificationConfig.getWebSocketServerHost());
        mapWebsocketStatus.put("port", notificationConfig.getWebSocketServerPort());
        mapWebsocketStatus.put("backlog", notificationConfig.getWebSocketBackLog());
        mapWebsocketStatus.put("bossThread", notificationConfig.getWebSocketBossThread());
        mapWebsocketStatus.put("workerThread", notificationConfig.getWebSocketWorkerThread());
        if ( true == this.isStart ) {
            mapWebsocketStatus.put("connected", server.getAllClients().size());
            mapWebsocketStatus.put("status", "running");

            Map<String,Integer> mapSubscriptionStatus = new LinkedHashMap<>();
            Collection<SocketIOClient> allClients = server.getAllClients();
            for( SocketIOClient curClient : allClients ) {
                Set<String> allRooms = curClient.getAllRooms();
                for ( String subscription : allRooms ) {
                    Integer curCnt = 0;
                    if (true == mapSubscriptionStatus.containsKey(subscription)) {
                        curCnt = mapSubscriptionStatus.get(subscription);
                    }
                    mapSubscriptionStatus.put(subscription,curCnt+1);
                }
            }
            /*
            mapServerConfig.put("subscriptions",listSubscriptionList);
            */
            mapWebsocketStatus.put("subscriptions",mapSubscriptionStatus.size());
        } else {
            mapWebsocketStatus.put("connected", "0");
            mapWebsocketStatus.put("status", "stop");
        }

        mapWebsocketStatus.put("openfds", osMBean.getOpenFileDescriptorCount());
        mapWebsocketStatus.put("maxfds", osMBean.getMaxFileDescriptorCount());
        mapServerConfig.put("websocket",mapWebsocketStatus);

        Map<String,Object> mapKafkaStatus = new LinkedHashMap<>();
        mapKafkaStatus.put("consumerUse", notificationConfig.getKafkaConsumerUse());
        mapKafkaStatus.put("hosts", notificationConfig.getKafkaConsumerHosts());
        mapKafkaStatus.put("kafkaConsumerBrokerCount", notificationConfig.getKafkaConsumerBrokerCount());
        mapKafkaStatus.put("groupId", notificationConfig.getKafkaConsumerGroupId());
        mapKafkaStatus.put("topicName", notificationConfig.getKafkaConsumerTopicName());
        mapKafkaStatus.put("KafkaConsumerGroupDifferentEachProcess", notificationConfig.getKafkaConsumerGroupDifferentEachProcess());
        mapKafkaStatus.put("pollingInterval", notificationConfig.getKafkaConsumerPollingInterval());
        mapServerConfig.put("kafka",mapKafkaStatus);

        return mapServerConfig;
    }

    private Configuration getServerConfig() {
        //https://javadoc.io/static/com.corundumstudio.socketio/netty-socketio/1.7.19/com/corundumstudio/socketio/Configuration.html
        Configuration config = new Configuration();
        config.setHostname(notificationConfig.getWebSocketServerHost());
        config.setPort(notificationConfig.getWebSocketServerPort());
        config.setBossThreads(notificationConfig.getWebSocketBossThread());
        config.setWorkerThreads(notificationConfig.getWebSocketWorkerThread());
        //config.setPingTimeout(0);
        //config.setOrigin("/*");

        System.out.printf("websocket server[%s:%d] starting\n",notificationConfig.getWebSocketServerHost(),notificationConfig.getWebSocketServerPort());
        System.out.printf("  backlog[%d]\n",notificationConfig.getWebSocketBackLog());
        System.out.printf("  bossthread[%d] workerthread[%d]\n",notificationConfig.getWebSocketBossThread(), notificationConfig.getWebSocketWorkerThread());

        config.setAckMode(AckMode.MANUAL);
        config.setPreferDirectBuffer(false);
        String os = System.getProperty("os.name").toLowerCase();
        if ( true == os.contains("linux") ) {
            config.setUseLinuxNativeEpoll(true);
        }

        SocketConfig sockConfig = new SocketConfig();
        sockConfig.setAcceptBackLog(notificationConfig.getWebSocketBackLog());
        sockConfig.setTcpKeepAlive(false);
        sockConfig.setTcpNoDelay(true);
        sockConfig.setSoLinger(0);
        sockConfig.setReuseAddress(true);
        sockConfig.setTcpSendBufferSize(4096);
        sockConfig.setTcpReceiveBufferSize(4096);
        config.setSocketConfig(sockConfig);

        return config;
    }

    private void addServerListener() {
        server.addConnectListener( new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
            }
        });

        server.addEventListener("getServerConfig", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String subId, AckRequest ackReqJoin) {
                client.sendEvent("getServerConfigRes", getServerInfo());
            }
        });

        server.addEventListener("joinSubscription", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String subId, AckRequest ackReqJoin) {
                if ( false == client.getAllRooms().contains(subId) ) {
                    client.joinRoom(subId);
                } else {
                    System.out.println("joinSubscription failed. client is already join in Subscription["+subId+"].");
                }
                client.set("notice",subId);
                client.sendEvent("joinSubscriptionRes", subId);
            }
        });

        server.addEventListener("notifySubscription", SubscriptionMsg.class, new DataListener<SubscriptionMsg>() {
            @Override
            public void onData(SocketIOClient client, SubscriptionMsg subMsg, AckRequest ackReqChat) {
                broadcastSend(subMsg);
            }
        });

        server.addEventListener("notifyAllSubscription", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String noticeMsg, AckRequest ackReqChat) {
                broadcastAllSend(noticeMsg);
            }
        });

        server.addEventListener("getSubscriptionList", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String subId, AckRequest ackReqJoin) {
                client.sendEvent("getSubscriptionListRes", client.getAllRooms());
            }
        });

        server.addEventListener("leaveSubscription", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String subId, AckRequest ackReqLeave) {
                if ( true == client.getAllRooms().contains(subId) ) {
                    client.leaveRoom(subId);
                } else {
                    System.out.println("leaveSubscription failed. client is not join in Subscription["+subId+"].");
                }
                client.sendEvent("leaveSubscriptionRes", subId);
            }
        });

        server.addDisconnectListener( new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient client) {
                System.out.println( "notify server disconnect client : " + client.getSessionId()  );
            }
        });
    }

    public NotificationWebSocketSendService serverStart() {
        try {
            this.serverStop();
            server = new SocketIOServer(this.getServerConfig());
            this.addServerListener();

            //file:///Users/whilte/Desktop/Work/intellj/netty-socketio-client-js/index.html
            if (true == server.startAsync().await().isSuccess()) {
                isStart = true;
                return this;
            }
        } catch ( Exception e ) {
            System.out.println("Start Server Exception : " + e);
        }

        return null;
    }

    public void serverStop() {
        if ( true == isStart ) {
            server.stop();
        }
        isStart = false;
    }

    @Override
    @Async("WebSocketSender")
    public void notificationSend(String msg) {
        //find user list
        try {
            Map<String, String> mapMsg = mapper.readValue(msg, Map.class);
            Map<Long, String> mapMember = notificationService.getNotificationUserList(mapMsg);

            //websocket send subscription to userid
            for (Long memberId : mapMember.keySet()) {
                broadcastSend(new SubscriptionMsg(memberId.toString(), mapMsg.get("msgType"), mapMsg.get("detailMsg")));
            }
        } catch ( Exception e ) {
        }
    }
}
