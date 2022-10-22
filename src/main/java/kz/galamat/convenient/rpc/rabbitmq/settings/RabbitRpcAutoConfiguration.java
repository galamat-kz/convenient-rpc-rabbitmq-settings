package kz.galamat.convenient.rpc.rabbitmq.settings;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Yersin Mukay on 14.10.2022
 */
@Configuration
public class RabbitRpcAutoConfiguration {

    private final RpcProperties rpcProperties;

    public RabbitRpcAutoConfiguration(RpcProperties rpcProperties) {
        this.rpcProperties = rpcProperties;
    }

    /** *
     * Configure the Send Message Queue
     */
    @Bean
    public Queue rpcQueue() {
        return new Queue(rpcProperties.getQueue());
    }

    /** *
     * Return Queue Configuration
     */
    @Bean
    public Queue rpcReplyQueue() {
        return new Queue(rpcProperties.getReplyQueueName());
    }

    /** *
     * Switch setting
     */
    @Bean
    public TopicExchange rpcTopicExchange() {
        return new TopicExchange(rpcProperties.getExchange());
    }

    /** *
     * Queuing and Switch Link Request
     */
    @Bean
    public Binding rpcQueueBinding() {
        return BindingBuilder.bind(rpcQueue())
                .to(rpcTopicExchange())
                .with(rpcProperties.getQueue());
    }

    /** *
     * Back to Queue and Switch Link
     */
    @Bean
    public Binding rpcReplyQueueBinding() {
        return BindingBuilder.bind(rpcReplyQueue())
                .to(rpcTopicExchange())
                .with(rpcProperties.getReplyQueueName());
    }

    /** *
     * Use RabbitTemplate Send and receive messages
     * And set callback queue address
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setReplyAddress(rpcProperties.getReplyQueueName());
        template.setReplyTimeout(rpcProperties.getReplyTimeout());
        template.setMessageConverter(producerJackson2MessageConverter());
        return template;
    }

    /** *
     * Configure listener for return queue
     */
    @Bean
    public SimpleMessageListenerContainer replyContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(rpcProperties.getReplyQueueName());
        container.setMessageListener(rabbitTemplate(connectionFactory));
        container.setReceiveTimeout(rpcProperties.getReplyTimeout());
        return container;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}