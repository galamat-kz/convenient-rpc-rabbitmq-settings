package kz.galamat.rabbitmqtorequestdispatcher.infrastructure.configurations;

import kz.galamat.rabbitmqtorequestdispatcher.client.handlers.RpcClientMethodHandler;
import kz.galamat.rabbitmqtorequestdispatcher.client.services.RpcRequestService;
import kz.galamat.rabbitmqtorequestdispatcher.server.converters.AmqpMessageToRpcRequestConverter;
import kz.galamat.rabbitmqtorequestdispatcher.server.converters.MessageToRpcRequestConverter;
import kz.galamat.rabbitmqtorequestdispatcher.server.listeners.RpcServerListener;
import kz.galamat.rabbitmqtorequestdispatcher.server.services.RpcForwardAsServletService;
import kz.galamat.rabbitmqtorequestdispatcher.server.services.RpcForwardService;
import kz.galamat.rabbitmqtorequestdispatcher.server.services.RpcResponseProcessService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by Yersin Mukay on 14.10.2022
 */
@Configuration
public class SpringAutoConfiguration {

    @Bean
    public RpcProperties rpcProperties() {
        return new RpcProperties();
    }

    @Bean
    public RpcForwardService forwardRpcToDispatcherRequestService(WebApplicationContext context) {
        return new RpcForwardAsServletService(context);
    }

    @Bean
    public MessageToRpcRequestConverter<Message> messageToRpcRequestConverter() {
        return new AmqpMessageToRpcRequestConverter();
    }

    @Bean
    public RpcResponseProcessService rpcRequestProcessService(RpcForwardService rpcForwardService) {
        return new RpcResponseProcessService(rpcForwardService);
    }

    @Bean
    @ConditionalOnBean(RpcServerMarkerConfiguration.Marker.class)
    public RpcServerListener rpcServerListener(RabbitTemplate rabbitTemplate,
                                               RpcProperties rpcProperties,
                                               RpcResponseProcessService rpcResponseProcessService,
                                               MessageToRpcRequestConverter<Message> toRpcRequestConverter) {
        return new RpcServerListener(rabbitTemplate, rpcProperties, rpcResponseProcessService, toRpcRequestConverter);
    }

    @Bean
    public RpcRequestService rpcRequestService(RabbitTemplate rabbitTemplate,
                                               RpcProperties rpcProperties) {
        return new RpcRequestService(rabbitTemplate, rpcProperties);
    }

    @Bean
    @ConditionalOnBean(RpcClientMarkerConfiguration.Marker.class)
    public RpcClientMethodHandler rpcClientMethodHandler(RpcRequestService rpcRequestService) {
        return new RpcClientMethodHandler(rpcRequestService);
    }

}
