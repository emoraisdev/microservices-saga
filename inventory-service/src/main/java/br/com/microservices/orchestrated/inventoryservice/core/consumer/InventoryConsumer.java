package br.com.microservices.orchestrated.inventoryservice.core.consumer;

import br.com.microservices.orchestrated.inventoryservice.core.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class InventoryConsumer {

    private final JsonUtil jsonUtil;

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.inventory-success}"
    )
    public void consumeSucessEvent(String payload) {
        log.info("Recebendo evento {} do tópico inventory-success",
                payload);

        var event = jsonUtil.toEvent(payload);
        log.info(event.toString());
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.inventory-fail}"
    )
    public void consumeFailEvent(String payload) {
        log.info("Recebendo evento {} do tópico inventory-fail",
                payload);

        var event = jsonUtil.toEvent(payload);
        log.info(event.toString());
    }
}
