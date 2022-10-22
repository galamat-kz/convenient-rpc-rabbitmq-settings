package kz.galamat.convenient.rpc.rabbitmq.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Yersin Mukay on 14.10.2022
 */
@Configuration
public class SpringAutoConfiguration {

    @Bean
    public RpcProperties rpcProperties() {
        return new RpcProperties();
    }

}
