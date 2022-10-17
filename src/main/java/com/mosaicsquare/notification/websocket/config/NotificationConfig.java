package com.mosaicsquare.notification.websocket.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource(value = "file:./conf/notification-websocket.properties", ignoreResourceNotFound = true)})
public class NotificationConfig {
    @Value("${webSocketTest:false}")
    private Boolean webSocketTest;
    @Value("${webSocketTestInterval:0}")
    private Integer webSocketTestInterval;

    @Value("${webSocketServerHost}")
    private String webSocketServerHost;
    @Value("${webSocketServerPort}")
    private Integer webSocketServerPort;
    @Value("${webSocketBossThread}")
    private Integer webSocketBossThread;
    @Value("${webSocketWorkerThread}")
    private Integer webSocketWorkerThread;
    @Value("${webSocketBackLog}")
    private Integer webSocketBackLog;

    @Value("${kafkaConsumerUse}")
    private Boolean kafkaConsumerUse;
    @Value("${kafkaConsumerBrokerCount}")
    private Integer kafkaConsumerBrokerCount;
    @Value("${kafkaConsumerHosts}")
    private String kafkaConsumerHosts;
    @Value("${kafkaConsumerTopicName}")
    private String kafkaConsumerTopicName;
    @Value("${kafkaConsumerGroupDifferentEachProcess}")
    private Boolean kafkaConsumerGroupDifferentEachProcess;
    @Value("${kafkaConsumerGroupId}")
    private String kafkaConsumerGroupId;
    @Value("${kafkaConsumerPollingInterval}")
    private Integer kafkaConsumerPollingInterval;

    @Value("${kafkaProducerUse}")
    private Boolean kafkaProducerUse;
    @Value("${kafkaProducerBrokerCount}")
    private Integer kafkaProducerBrokerCount;
    @Value("${kafkaProducerHosts}")
    private String kafkaProducerHosts;
    @Value("${kafkaProducerTopicName}")
    private String kafkaProducerTopicName;
}