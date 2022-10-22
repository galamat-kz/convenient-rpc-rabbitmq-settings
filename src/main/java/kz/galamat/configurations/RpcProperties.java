package kz.galamat.configurations;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.UUID;

/**
 * Created by Yersin Mukay on 14.10.2022
 */
@ConfigurationProperties(prefix = "spring.convenient.rpc.rabbitmq")
public class RpcProperties implements InitializingBean {

    private String queue;
    private String replyQueuePrefix;
    private Boolean appendRandomForReplyQueueName;
    private String exchange;
    private String replyQueueName;
    private long replyTimeout;

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getReplyQueuePrefix() {
        return replyQueuePrefix;
    }

    public void setReplyQueuePrefix(String replyQueuePrefix) {
        this.replyQueuePrefix = replyQueuePrefix;
    }

    public Boolean getAppendRandomForReplyQueueName() {
        return appendRandomForReplyQueueName;
    }

    public void setAppendRandomForReplyQueueName(Boolean appendRandomForReplyQueueName) {
        this.appendRandomForReplyQueueName = appendRandomForReplyQueueName;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getReplyQueueName() {
        return replyQueueName;
    }

    public long getReplyTimeout() {
        return replyTimeout;
    }

    public void setReplyTimeout(long replyTimeout) {
        this.replyTimeout = replyTimeout;
    }

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
