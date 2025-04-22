package br.com.microservices.orchestrated.orderservice.core.service;

import br.com.microservices.orchestrated.orderservice.config.exception.ValidationException;
import br.com.microservices.orchestrated.orderservice.core.document.Event;
import br.com.microservices.orchestrated.orderservice.core.dto.EventFilters;
import br.com.microservices.orchestrated.orderservice.core.repository.EventRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@AllArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository repo;

    public void notifyEnding(Event event) {
        event.setOrderId(event.getOrderId());
        event.setCreatedAt(LocalDateTime.now());
        save(event);
        log.info("Order {} salvo! Saga Finalizada! Transacation Id: {}.", event.getOrderId(), event.getTransactionId());
    }

    public Event save(Event event) {
        return repo.save(event);
    }

    public List<Event> findAll() {
        return repo.findAllOrderByCreatedAtDesc();
    }

    public Event findByFilters(EventFilters filters) {

        validateFilters(filters);

        if (!isEmpty(filters.getOrderId())) {
            return repo.findTop1ByOrderIdOrderByCreatedAtDesc(filters.getOrderId())
                    .orElseThrow(() -> new ValidationException("Evento não encontrado por OrderID."));
        } else {
            return repo.findTop1ByOrderIdOrderByCreatedAtDesc(filters.getTransactionId())
                    .orElseThrow(() -> new ValidationException("Evento não encontrado por TransactionID."));
        }
    }

    private void validateFilters(EventFilters filters){
        if (isEmpty(filters.getOrderId()) && isEmpty(filters.getTransactionId())) {
            throw new ValidationException("OrderID ou TrasactionID precisam ser informados.");
        }
    }
}
