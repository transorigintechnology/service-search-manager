package tr.com.transorigin.service_search_manager.producer;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;
import tr.com.transorigin.service_search_manager.dto.ServiceSearchResult;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfferProducer {
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String OFFER_PRODUCER = "prepare-offer";

    public boolean publishPrepareOfferEvent(ServiceSearchResult serviceSearchResult) {
        try {
            String key = serviceSearchResult.getRelocationRequest().getId() == null ? null : String.valueOf(serviceSearchResult.getRelocationRequest().getId());
            String json = objectMapper.writeValueAsString(serviceSearchResult);
            kafkaTemplate.send(OFFER_PRODUCER, key, json).whenComplete((result, ex) -> {
                handleEventPublishError(ex, serviceSearchResult, OFFER_PRODUCER);
            });
            log.info("Published prepare offer event for relocation id={} data:{}", serviceSearchResult.getRelocationRequest().getId(), json);
            return true;
        } catch (Exception e) {
            log.error("Failed to serialize relocation id={} to JSON", serviceSearchResult.getRelocationRequest().getId(), e);
            return false;
        }
    }

    private static void handleEventPublishError(Throwable ex, ServiceSearchResult serviceSearchResult, String topic) {
        if (ex != null) {
            log.error("Failed to send service search for relocation request id={} to topic={}", serviceSearchResult.getRelocationRequest().getId(), topic, ex);
        } else {
            log.info("Sent service search for relocation request id={} to topic={}", serviceSearchResult.getRelocationRequest().getId(), topic);
        }
    }

}
