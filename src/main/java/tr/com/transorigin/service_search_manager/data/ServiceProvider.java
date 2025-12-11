package tr.com.transorigin.service_search_manager.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "service_providers")
public class ServiceProvider {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String name;
    private String address;
    private String placeId;
    private String country;
    private String city;
    private String town;
    private String phoneNumber;
    private String website;
    private double rating;
    private int userRatingsTotal;
    private String serviceProviderType;
    private boolean isActive;
    private Date createdAt;
    private Date bannedAt;
    private String banReason;
}
