package com.mosaicsquare.notification.websocket.kafka;

import com.mosaicsquare.notification.websocket.config.NotificationConfig;
import com.mosaicsquare.notification.websocket.notification.NotificationSender;
import lombok.Data;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Data
@Service
public class KafkaConsumerService {

    private final NotificationConfig notificationConfig;
    private final NotificationSender notificationSender;

    public KafkaConsumerService(NotificationConfig notificationConfig, NotificationSender notificationSender) {
        this.notificationConfig = notificationConfig;
        this.notificationSender = notificationSender;

        if ( true == this.notificationConfig.getKafkaConsumerUse() && 0 < this.notificationConfig.getKafkaConsumerBrokerCount() ) {
            Integer randName = new Random().nextInt(40960);
            for (Integer i = 0; i < this.notificationConfig.getKafkaConsumerBrokerCount(); i++) {
                Thread curThread = new Thread(new KafkaConsumerEventLisenerThread(randName));
                curThread.start();
            }
        }
    }

    public class KafkaConsumerEventLisenerThread implements Runnable {
        private KafkaConsumer<String, String> consumer = null;

        public KafkaConsumerEventLisenerThread(Integer randName) {
            Properties configs = new Properties();
            configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, notificationConfig.getKafkaConsumerHosts());
            configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            if (true == notificationConfig.getKafkaConsumerGroupDifferentEachProcess()) {
                configs.put(ConsumerConfig.GROUP_ID_CONFIG, notificationConfig.getKafkaConsumerGroupId() + randName);
            } else {
                configs.put(ConsumerConfig.GROUP_ID_CONFIG, notificationConfig.getKafkaConsumerGroupId());
            }
            configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
            configs.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000);

            consumer = new KafkaConsumer(configs);
        }

        @Override
        public void run() {
            try {
                consumer.subscribe(Arrays.asList(notificationConfig.getKafkaConsumerTopicName()));

                while (true) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(notificationConfig.getKafkaConsumerPollingInterval()));
                    for (ConsumerRecord<String, String> record : records) {
                        String kafkaMsg = record.value();

                        try {
                            //ToDo 로직 테스트 검증은 kafka producer cli로 직접 메시지를 생성하여 진행한다.
                            System.out.println("***** notificationSender call");
                            notificationSender.notificationSend(kafkaMsg);
                            System.out.println("***** notificationSender return");
//                            Map<String, String> mapKafkaMsg = mapper.readValue(kafkaMsg, Map.class);
//                            Map<String, String> mapDetailMsg = mapper.readValue(mapKafkaMsg.get("detailMsg"), Map.class);
//                            Map<Long, String> mapEmailNotification = notificationService.getNotificationUserList(mapKafkaMsg);
//
//                            //websocket send subscription to userid
//                            for (Long memberId : mapEmailNotification.keySet()) {
//                                nettyServer.broadcastSend(new SubscriptionMsg(memberId.toString(), mapKafkaMsg.get("msgType"), mapDetailMsg));
//                            }
                        } catch (Exception e) {
                            System.out.println("kafka consumer sendmail exception : " + e.toString());
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("kafka consumer wait loop exception : " + e.toString());
            }
        }
    }
}