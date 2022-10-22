package kz.galamat.rabbitmqtorequestdispatcher.infrastructure.configurations;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.UUID;

@ConfigurationProperties(prefix = "spring.rabbit.rpc")
@Data
public class RpcProperties implements InitializingBean {

    private String queue;
    private String replyQueuePrefix;
    private Boolean appendRandomForReplyQueueName;
    private String exchange;
    @Setter(AccessLevel.NONE)
    private String replyQueueName;
    private long replyTimeout;

    @Override
    public void afterPropertiesSet() {
        assert queue != null : "The property queue must be not null";
        assert replyQueuePrefix != null : "The property reply-queue-prefix must be not null";
        assert exchange != null : "The property exchange must be not null";
        assert replyTimeout > 0 : "The property reply-timeout must be positive";
        replyQueueName = generateRpcReplyQueueName();
    }

    private String generateRpcReplyQueueName() {
        return appendRandomForReplyQueueName ?
                replyQueuePrefix + "-" + UUID.randomUUID() :
                replyQueuePrefix;
    }
}
