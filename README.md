# notification-webscket

service-notification for websocket notification<br>

- develop enviroment<br>
```ini
java openjdk 11.0.14.1
mysql 5.7
spring boot 2.6.7
apache spring-kafka
java mail 1.4.7
netty-socketio 1.7.17
```

- email sender<br>
```ini
webSocketServerHost=0.0.0.0 //server ip 
webSocketServerPort=19092 //server port
webSocketBossThread=32 //accept thread
webSocketWorkerThread=32 //recv thread
webSocketBackLog=128  //back log
```

- docker <br>
```ini
./Dockerfile
FROM openjdk:11
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} notification-email.jar
ENTRYPOINT ["java","-XX:-MaxFDLimit","-jar","/notification-email.jar"]
```

- test kafka setting<br>
```ini
ex> use docker ( zookeeper 1 / kafka 3 ) docker-compose.yml
hostip 192.168.0.58
zookeeper outcomming port=2181
topic name=notificationMsg
partition count=3 (equal broker count?)
replica count=1
kafka1 outcomming ip=${hostip}
kafka1 outcomming port=9092
kafka2 outcomming ip=${hostip}
kafka1 outcomming port=9093
kafka3 outcomming ip=${hostip}
kafka1 outcomming port=9094
version: '2'
services:
  zookeeper:
    image: wurstmeister/zookeeper
    ports:
      - "<zookeeper outcomming port>:2181"
    restart: unless-stopped

  kafka1:
    build: .
    ports:
      - "<kafka1 outcomming port>:9092"
    environment:
      DOCKER_API_VERSION: 1.22
      KAFKA_CREATE_TOPICS: "<topic name>:<partition count>:<replica count>"      
      KAFKA_ADVERTISED_HOST_NAME: <kafka1 outcomming ip>
      KAFKA_ZOOKEEPER_CONNECT: <zookeeper outcomming port>:2181
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
    restart: unless-stopped

  kafka2:
    build: .
    ports:
      - "<kafka2 outcomming port>:9092"
    environment:
      DOCKER_API_VERSION: 1.22
      KAFKA_CREATE_TOPICS: "<topic name>:<partition count>:<replica count>"      
      KAFKA_ADVERTISED_HOST_NAME: <kafka2 outcomming ip>
      KAFKA_ZOOKEEPER_CONNECT: <zookeeper outcomming port>:2181
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
    restart: unless-stopped

  kafka3:
    build: .
    ports:
      - "<kafka3 outcomming port>:9092"
    environment:
      DOCKER_API_VERSION: 1.22
      KAFKA_CREATE_TOPICS: "<topic name>:<partition count>:<replica count>"      
      KAFKA_ADVERTISED_HOST_NAME: <kafka3 outcomming ip>
      KAFKA_ZOOKEEPER_CONNECT: <zookeeper outcomming port>:2181
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
    restart: unless-stopped

```

- deploy setting<br>
  properies file : ./conf/notification-email.properties
```ini
#database
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.name=datasource
spring.datasource.url=jdbc:mysql://<aws database host:port>/<aws database>?autoReconnect=true
spring.datasource.username=<aws database id>
spring.datasource.password=<aws database password>

spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.generate-ddl=true

#Email
emailHost=<email smtp server host>
emailHostPort=<smtp server port>
emailHostAccountId=<smtp server id>
emailHostAccountName=MosaicSquare
emailHostAccountPassword=<smtp server password>
emailHostUseSSL=true

#kafka
#Kafka consumer setting
kafkaConsumerUse=true
kafkaConsumerBrokerCount=<broker count>
kafkaConsumerHosts=<kafka host -splite,->
## ex> 192.168.0.58:9092,192.168.0.58:9093,192.168.0.58:9094
kafkaConsumerTopicName=<topic name>
kafkaConsumerGroupDifferentEachProcess=<different groupid each consumer>
## true  : each all consumer recv same new topic
## false : only one consumer recv same new topic
kafkaConsumerGroupId=<groupid>
kafkaConsumerPollingInterval=<consumer polling interval>

#Kakfa producer setting
kafkaProducerUse=false
kafkaProducerBrokerCount=<broker count>
kafkaProducerHosts=<kafka host -splite,->
## ex> 192.168.0.58:9092,192.168.0.58:9093,192.168.0.58:9094
kafkaProducerTopicName=<topic name>
```

