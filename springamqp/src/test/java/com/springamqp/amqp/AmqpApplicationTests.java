package com.springamqp.amqp;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Week;
import com.springamqp.amqp.producer.RabbitSender;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AmqpApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitSender rabbitSender;

    @Test
    public void rabbitSendMsg() throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        map.put("number", "12345");
        rabbitSender.sendMsg("hello", map);
    }

    @Test
    public void rabbitAdmin() throws Exception {
        //声明三种模式交换机
        rabbitAdmin.declareExchange(new DirectExchange("test.direct", false, false));
        rabbitAdmin.declareExchange(new TopicExchange("test.topic", false, false));
        rabbitAdmin.declareExchange(new FanoutExchange("test.fanout", false, false));
        //声明三种模式的队列
        rabbitAdmin.declareQueue(new Queue("test.direct.queue", false));
        rabbitAdmin.declareQueue(new Queue("test.topic.queue", false));
        rabbitAdmin.declareQueue(new Queue("test.fanout.queue", false));
        //绑定交换机和队列的方法
        //第一种:
        // 参数说明:
        // destination:队列
        // Binding.DestinationType.QUEUE 队列绑定说明
        // exchange: 交换机
        // routingKey : 路由Key
        // arguments: 指定一个空的new HashMap
        rabbitAdmin.declareBinding(new Binding("test.direct.queue", Binding.DestinationType.QUEUE, "test.direct", "direct", new HashMap<>()));
        //第二种(可以直接声明交换机和队列,可舍去声明三种模式交换机和三种模式队列
        rabbitAdmin.declareBinding(
                BindingBuilder.bind(new Queue("test.topic.queue", false)) //直接创建队列
                        .to(new TopicExchange("test.topic", false, false)) //直接创建交换机 建立关联关系
                        .with("user.#")); //指定路由key
        //由于广播交换机无需指定路由key 所以with 可以省略掉
        rabbitAdmin.declareBinding(
                BindingBuilder.bind(new Queue("test.fanout.queue", false)) //直接创建队列
                        .to(new FanoutExchange("test.topic", false, false)));//直接创建交换机 建立关联关系

        //清空队列数据
        rabbitAdmin.purgeQueue("queue001", false);

    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMessage() {
        //1.创建消息
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("desc", "信息描述");
        messageProperties.getHeaders().put("type", "自定义消息类型");
        Message message = new Message("hello mq".getBytes(), messageProperties);
        rabbitTemplate.convertAndSend("topic001", "spring.amqp", message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                System.out.println("添加额为的设置。。。");
                message.getMessageProperties().getHeaders().put("desc", "额外修改消息描述");
                message.getMessageProperties().getHeaders().put("attr", "额为新加的属性");
                return message;
            }
        });
    }


    @Test
    public void testSendMessage2() {
        //1.创建消息
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("test/plain");
        Message message = new Message("hello mq 消息".getBytes(), messageProperties);
        rabbitTemplate.send("topic001", "spring.abc", message);
        rabbitTemplate.convertAndSend("topic001", "spring.amqp", "hello object");
        rabbitTemplate.convertAndSend("topic002", "rabbit.amqp", "hello object");

    }

    public static void main(String[] args) {
        DateUtil dateUtil = new DateUtil();
        String chinese = dateUtil.dayOfWeekEnum(new Date()).toChinese("周");
        System.out.println(chinese);
    }
}
