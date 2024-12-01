package com.lx.framework.tool.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xin.liu
 * @description TODO
 * @date 2024-03-17  11:33
 * @Version 1.0
 */
@Slf4j
@Configuration
public class RabbitmqConfig implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    /**
     * 死信队列
     */
    public static final String DEAD_EXCHANGE = "dead-exchange";
    public static final String DEAD_QUEUE = "dead-queue";
    public static final String DEAD_ROUTING_KEY = "dead-rout";

    @Autowired
    private ConnectionFactory connectionFactory;

    @Bean
    public RabbitAdmin rabbitAdmin() {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    /**
     * 序列化bean
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        //序列化不设置的话消费者业务对象转化会报错
        rabbitTemplate.setMessageConverter(messageConverter());
        rabbitTemplate.setConfirmCallback(this);
        //设置exchange对失败消息的处理方式，默认为false直接丢弃失败数据，true将失败消息发送给回调函数
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnsCallback(this);
        return rabbitTemplate;
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (!ack) {
            ReturnedMessage returned = correlationData.getReturned();
            log.info(new String(returned.getMessage().getBody()));
            log.error("confirm==>发送到broker失败 correlationData:{}，ack:{}，cause:{}", correlationData, ack, cause);
        } else {
            log.info("confirm==>发送到broker成功");
        }
    }

    /**
     * @description 此方法只有在设置工作模式为Return，并且设置exchange对失败消息的处理为发送给ReturnCallBack，消息发送给Exchange后，Exchange路由到Queue失败时会执行该方法
     * @param returnedMessage
     * @return: void
     * @author xin.liu
     * @date  0:31
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.info("returnedMessage==>message={},replyCode={},replyText={},exchange={},routingKey={}",
                new String(returnedMessage.getMessage().getBody()), returnedMessage.getReplyCode(), returnedMessage.getReplyText(), returnedMessage.getExchange(), returnedMessage.getRoutingKey());
    }

    /**
     * 队列名称：directQueue
     *
     * @return
     */
    @Bean
    public Queue directQueue() {
        // durable: 是否持久化，默认是false。持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在
        // exclusive: 默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete: 是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        // 一般设置一下队列的持久化就好，其余两个就是默认false
        Queue queue = new Queue("directQueue", true);
        // 声明队列，启动时自动创建队列
        rabbitAdmin().declareQueue(queue);
        return queue;
    }

    /**
     * Direct交换机名称：directExchange
     * durable 设置为true开启交换机持久化
     * @return
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("directExchange", true, false);
    }

    /**
     * 将队列和交换机绑定, 并设置匹配路由键：directRouting
     *
     * @return
     */
    @Bean
    public Binding bindingDirectExchange() {
        return BindingBuilder.bind(directQueue()).to(directExchange()).with("directRouting");
    }

    /********************************主题订阅*************************************/
    @Bean
    public Queue topicFirstQueue() {
//        Queue queue = new Queue("topicFirstQueue", true);
//        // 声明队列，启动时自动创建队列
//        rabbitAdmin().declareQueue(queue);

        Queue queue = QueueBuilder.durable("topicFirstQueue")
                .deadLetterExchange(DEAD_EXCHANGE)
                .deadLetterRoutingKey(DEAD_ROUTING_KEY)
                //给队列中所有消息设置过期时间 当消息不被消费时，会进入到死信队列
//                .ttl(5000)
                //设置队列最大长度，超过长度则进入死信队列
                .maxLength(3)
                .build();
                // 声明队列，启动时自动创建队列
        rabbitAdmin().declareQueue(queue);
        return queue;
    }

    @Bean
    public Queue topicSecondQueue() {
        Queue queue = new Queue("topicSecondQueue", true);
        // 声明队列，启动时自动创建队列
        rabbitAdmin().declareQueue(queue);
        return queue;
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topicExchange", true, false);
    }

    @Bean
    public Binding bindingTopicFirstExchange() {
        return BindingBuilder.bind(topicFirstQueue()).to(topicExchange()).with("topic.first");
    }

    @Bean
    public Binding bindingTopicSecondExchange() {
        return BindingBuilder.bind(topicSecondQueue()).to(topicExchange()).with("topic.second");
    }

    /**
     * 死信队列
     * @return
     */
    @Bean
    public Queue deadQueue(){
        Queue queue = new Queue(DEAD_QUEUE , true);
        // 声明队列，启动时自动创建队列
        rabbitAdmin().declareQueue(queue);
        return queue;
    }

    /**
     * 死信交换机
     * @return
     */
    @Bean
    public DirectExchange deadExchange(){
        return new DirectExchange(DEAD_EXCHANGE, true, false);
    }

    /**
     * 死信队列绑定死信交换机
     * @return
     */
    @Bean
    public Binding dlcBinding() {
        return BindingBuilder.bind(deadQueue()).to(deadExchange()).with(DEAD_ROUTING_KEY);
    }

    /********************************消息广播*************************************/
    @Bean
    public Queue fanoutFirstQueue() {
        Queue queue = new Queue("fanoutFirstQueue", true);
        // 声明队列，启动时自动创建队列
        rabbitAdmin().declareQueue(queue);
        return queue;
    }

    @Bean
    public Queue fanoutSecondQueue() {
        Queue queue = new Queue("fanoutSecondQueue", true);
        // 声明队列，启动时自动创建队列
        rabbitAdmin().declareQueue(queue);
        return queue;
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanoutExchange", true, false);
    }

    @Bean
    public Binding bindingFanoutFirstExchange() {
        return BindingBuilder.bind(fanoutFirstQueue()).to(fanoutExchange());
    }

    @Bean
    public Binding bindingFanoutSecondExchange() {
        return BindingBuilder.bind(fanoutSecondQueue()).to(fanoutExchange());
    }

}