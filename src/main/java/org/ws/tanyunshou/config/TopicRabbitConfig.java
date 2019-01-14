package org.ws.tanyunshou.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ws.tanyunshou.mq.RabbitConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yinan
 * @date 18-12-26
 */
@Configuration
public class TopicRabbitConfig {
    private static Map<String, Object> args = new HashMap<>();

    @Bean
    public Queue amountQueue() {
        args.put("x-max-length", 20);
        return new Queue(RabbitConstant.AMOUNT_QUEUE, true, false, false, args);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(RabbitConstant.EXCHANGE);
    }

    @Bean
    public Binding bindingExchangeQueue(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitConstant.AMOUNT_ROUTING_KEY);
    }
}
