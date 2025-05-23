package br.com.microservices.orchestrated.inventoryservice.core.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topic.orchestrator}")
    private String orchestratorTopic;

    public void sendEvent(String payload) {

        try {
            log.info("Enviando evento para o Tópico {} com os dados {}",
                    orchestratorTopic, payload);

            kafkaTemplate.send(orchestratorTopic, payload);

        } catch (Exception e){
            log.error("Erro ao enviar dados para o Topico {} com os dados {}",
                    orchestratorTopic, payload, e);
        }
    }
}
