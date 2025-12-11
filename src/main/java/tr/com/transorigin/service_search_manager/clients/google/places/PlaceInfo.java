package tr.com.transorigin.service_search_manager.clients.google.places;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tr.com.transorigin.service_search_manager.data.ServiceProviderType;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceInfo {
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
    private ServiceProviderType serviceProviderType;
}
