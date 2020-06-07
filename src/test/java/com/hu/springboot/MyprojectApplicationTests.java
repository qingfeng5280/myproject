package com.hu.springboot;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MyprojectApplicationTests {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisTemplate redisTemplate;

    public static final String ACTIVEMQ_URL = "tcp://10.211.55.3:61616";
    public static final String QUEUE_NAME = "queue01";

    @Test
    public void demo(){
        System.out.println("success");
    }


    @Test
    public void redisTest(){
        //stringRedisTemplate.opsForValue().append("k1","haha");
        String k1 = stringRedisTemplate.opsForValue().get("k1");
        System.out.println(k1);
    }

    @Test
    public void activemqTest() throws JMSException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(QUEUE_NAME);
        //创建消息生产者
        MessageProducer messageProducer = session.createProducer(queue);
        for (int i = 1; i <=3 ; i++) {
            TextMessage textMessage = session.createTextMessage("msg"+i);
            messageProducer.send(textMessage);
        }
        messageProducer.close();
        session.close();
        connection.close();

        System.out.println("消息发布到MQ完成");
    }


    @Test
    public void contextLoads() {
    }

}
