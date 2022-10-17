package com.mosaicsquare.notification.websocket.kafka;

import com.mosaicsquare.notification.websocket.config.NotificationConfig;
import lombok.Data;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.concurrent.Executor;

@Data
@EnableAsync(proxyTargetClass=true)
@Service
public class KafkaProducerService {

    private final NotificationConfig notificationConfig;
    private KafkaProducer<String, String> producer = null;

    public KafkaProducerService(NotificationConfig notificationConfig, Environment environment) {
        this.notificationConfig = notificationConfig;

        if ( true == this.notificationConfig.getKafkaProducerUse() ) {
            Properties configs = new Properties();
            configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.notificationConfig.getKafkaProducerHosts());
            configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

            this.producer = new KafkaProducer<>(configs);
        }

    }

    @Bean(name="kafkaProducerSender")
    public Executor kafkaProducer() {
        if ( true == this.notificationConfig.getKafkaProducerUse() ) {
            ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
            taskExecutor.setCorePoolSize(this.notificationConfig.getKafkaProducerBrokerCount()*2);
            taskExecutor.setMaxPoolSize(this.notificationConfig.getKafkaProducerBrokerCount()*10);
            taskExecutor.setQueueCapacity(this.notificationConfig.getKafkaProducerBrokerCount()*100);
            taskExecutor.setThreadNamePrefix("kafkaProducerSender-");
            taskExecutor.initialize();
            return taskExecutor;
        }
        return null;
    }

    @Async("kafkaProducerSender")
    protected void producerTopic(String message) {
        try {
            producer.send(
                    new ProducerRecord<>(this.notificationConfig.getKafkaProducerTopicName(), message),
                    new Callback() {
                        @Override
                        public void onCompletion(RecordMetadata metadata, Exception exception) {
                            if (exception != null) {
                                System.out.println("exception on send : " + exception.toString());
                            } else {
                                //System.out.println("producer success topic send : [" + metadata.topic() + "][" +message+ "]");
                            }
                        }
                    });
        } catch (Exception e) {
            System.out.println("exception on producer : " + e.toString());
        } finally {
            if (null != producer) {
                producer.flush();
            }
        }
    }

    public void sendNotification(String message) {
        if ( null != producer ) {
            producerTopic(message);
        }
    }
}