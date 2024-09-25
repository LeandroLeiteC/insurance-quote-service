package br.com.itau.insurance_quote.infrastructure.configuration;

import br.com.itau.insurance_quote.infrastructure.configuration.annotations.InsurancePolicyCreatedQueue;
import br.com.itau.insurance_quote.infrastructure.configuration.annotations.InsuranceQuoteEvents;
import br.com.itau.insurance_quote.infrastructure.configuration.annotations.InsuranceQuoteReceivedQueue;
import br.com.itau.insurance_quote.infrastructure.configuration.properties.amqp.QueueProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

    @Bean
    @ConfigurationProperties(prefix = "amqp.queues.insurance-quote-received")
    @InsuranceQuoteReceivedQueue
    public QueueProperties insuranceQuoteReceivedQueueProperties() {
        return new QueueProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "amqp.queues.insurance-policy-created")
    @InsurancePolicyCreatedQueue
    public QueueProperties insurancePolicyCreatedQueueProperties() {
        return new QueueProperties();
    }

    @Configuration
    static class Admin {

        @Bean
        @InsuranceQuoteEvents
        public Exchange insuranceQuoteEventsExchange(@InsuranceQuoteReceivedQueue QueueProperties props) {
            return new DirectExchange(props.getExchange());
        }

        @Bean
        @InsuranceQuoteReceivedQueue
        public Queue insuranceQuoteReceivedQueue(@InsuranceQuoteReceivedQueue QueueProperties props) {
            return new Queue(props.getQueue());
        }

        @Bean
        @InsuranceQuoteReceivedQueue
        public Binding insuranceQuoteReceivedBinding(
                @InsuranceQuoteEvents DirectExchange exchange,
                @InsuranceQuoteReceivedQueue Queue queue,
                @InsuranceQuoteReceivedQueue QueueProperties props
        ) {
            return BindingBuilder.bind(queue).to(exchange).with(props.getRoutingKey());
        }

        @Bean
        @InsurancePolicyCreatedQueue
        public Queue insurancePolicyCreatedQueue(@InsurancePolicyCreatedQueue QueueProperties props) {
            return new Queue(props.getQueue());
        }

        @Bean
        @InsurancePolicyCreatedQueue
        public Binding insurancePolicyCreateddBinding(
                @InsuranceQuoteEvents DirectExchange exchange,
                @InsurancePolicyCreatedQueue Queue queue,
                @InsurancePolicyCreatedQueue QueueProperties props
        ) {
            return BindingBuilder.bind(queue).to(exchange).with(props.getRoutingKey());
        }
    }
}
