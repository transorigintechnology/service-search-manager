package tr.com.transorigin.service_search_manager.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tr.com.transorigin.service_search_manager.service.ServiceFinderService;


@Component
@RequiredArgsConstructor
public class ServiceSearchConsumer {

    private final ServiceFinderService serviceFinderService;

    @KafkaListener(topics = "relocation-service-search", groupId = "service-search-manager-group")
    public void listen(String message) {
        serviceFinderService.findRequiredServices(message);
    }


}
