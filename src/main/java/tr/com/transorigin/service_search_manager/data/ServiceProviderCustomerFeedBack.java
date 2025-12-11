package tr.com.transorigin.service_search_manager.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "service_provider_customer_feedbacks")
public class ServiceProviderCustomerFeedBack {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    private UUID customerId;
    private UUID serviceProviderId;
    private int rating;
    private String comment;
}
