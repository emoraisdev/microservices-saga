package br.com.microservices.orchestrated.orchestratorservice.core.producer;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class SagaOrchestratorProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendEvent(String topic, String payload) {

        try {
            log.info("Enviando evento para o Tópico {} com os dados {}",
                    topic, payload);

            kafkaTemplate.send(topic, payload);

        } catch (Exception e){
            log.error("Erro ao enviar dados para o Topico {} com os dados {}",
                    topic, payload, e);
        }
    }
}
