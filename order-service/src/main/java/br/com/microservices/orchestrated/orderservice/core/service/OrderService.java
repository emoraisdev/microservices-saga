package br.com.microservices.orchestrated.orderservice.core.service;

import br.com.microservices.orchestrated.orderservice.core.document.Event;
import br.com.microservices.orchestrated.orderservice.core.document.Order;
import br.com.microservices.orchestrated.orderservice.core.dto.OrderRequest;
import br.com.microservices.orchestrated.orderservice.core.producer.SagaProducer;
import br.com.microservices.orchestrated.orderservice.core.repository.OrderRepository;
import br.com.microservices.orchestrated.orderservice.core.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final String TRANSACTION_ID_PATTERN = "%s_%s";

    private final OrderRepository repo;
    private final JsonUtil jsonUtil;
    private final SagaProducer sagaProducer;
    private final EventService eventService;

    public Order create(OrderRequest request){

        var order = Order.builder()
                .products(request.getPoducts())
                .createdAt(LocalDateTime.now())
                .transactionId(
                        String.format(TRANSACTION_ID_PATTERN,
                                Instant.now().toEpochMilli(),
                                UUID.randomUUID())
                ).build();

        repo.save(order);

        sagaProducer.sendEvent(jsonUtil.toJson(createPayload(order)));

        return order;
    }

    private Event createPayload(Order order){

        var event = Event.builder()
                .payload(order)
                .orderId(order.getId())
                .transactionId(order.getTransactionId())
                .createdAt(LocalDateTime.now()).build();

        return eventService.save(event);
    }
}
