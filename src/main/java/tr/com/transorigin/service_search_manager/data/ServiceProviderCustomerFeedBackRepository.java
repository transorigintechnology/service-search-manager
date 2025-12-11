package tr.com.transorigin.service_search_manager.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ServiceProviderCustomerFeedBackRepository extends JpaRepository<ServiceProviderCustomerFeedBack, UUID> {
}
