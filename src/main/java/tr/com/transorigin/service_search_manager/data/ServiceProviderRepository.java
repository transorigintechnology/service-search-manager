package tr.com.transorigin.service_search_manager.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, UUID> {
    @Query("SELECT sp FROM ServiceProvider sp WHERE sp.serviceProviderType = :serviceType AND sp.country = :country AND sp.city = :city AND sp.isActive = true")
    List<ServiceProvider> getServiceProviders(String serviceType, String country, String city);
}
