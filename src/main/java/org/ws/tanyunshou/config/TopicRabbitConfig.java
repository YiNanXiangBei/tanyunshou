package org.ws.tanyunshou.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ws.tanyunshou.mq.RabbitConstant;

/**
 * @author yinan
 * @date 18-12-26
 */
@Configuration
public class TopicRabbitConfig {

    @Bean(name = "amountQueue")
    public Queue amountQueue() {
        return new Queue(RabbitConstant.AMOUNT_QUEUE);
    }

    @Bean(name = "moneyQueue")
    public Queue moneyQueue() {
        return new Queue(RabbitConstant.MONEY_QUEUE);
    }

    @Bean(name = "serialNoQueue")
    public Queue serialNoQueue() {
        return new Queue(RabbitConstant.SERIAL_NO_QUEUE);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(RabbitConstant.EXCHANGE);
    }

    @Bean
    public Binding bindingAmountExchangeQueue(@Qualifier("amountQueue") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitConstant.AMOUNT_ROUTING_KEY);
    }

    @Bean
    public Binding bindingMoneyExchangeQueue(@Qualifier("moneyQueue") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitConstant.MONEY_ROUTING_KEY);
    }

    @Bean
    public Binding bindingSerialNoExQueue(@Qualifier("serialNoQueue") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitConstant.SERIAL_ROUTING_KEY);
    }




}
