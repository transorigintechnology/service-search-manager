package tr.com.transorigin.service_search_manager.clients.google.places;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tr.com.transorigin.service_search_manager.data.ServiceProviderType;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
public class GooglePlaceService {

    @Value("${google.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<PlaceInfo> findDetailedPlaces(double lat, double lng, int radiusInKm, ServiceProviderType serviceProviderType) {

        log.info("Searching for places of type {} near location ({}, {}) within radius {} km", serviceProviderType.getGooglePlacesKeywords(), lat, lng, radiusInKm);

        int radiusInMeter = radiusInKm * 1000;
        String encodedKeyword = URLEncoder.encode(serviceProviderType.getGooglePlacesKeywords(), StandardCharsets.UTF_8);
        String nearbyUrl = String.format(
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%f,%f&radius=%d&keyword=%s&key=%s",
                lat, lng, radiusInMeter, encodedKeyword, apiKey
        );

        Map<String, Object> nearbyResponse = restTemplate.getForObject(nearbyUrl, Map.class);
        List<Map<String, Object>> results = (List<Map<String, Object>>) nearbyResponse.get("results");

        List<PlaceInfo> places = new ArrayList<>();

        for (Map<String, Object> result : results) {
            String placeId = (String) result.get("place_id");
            String detailsUrl = String.format(
                    "https://maps.googleapis.com/maps/api/place/details/json?place_id=%s&fields=name,address_component,formatted_address,formatted_phone_number,website,rating,user_ratings_total&key=%s",
                    placeId, apiKey
            );

            Map<String, Object> detailsResponse = restTemplate.getForObject(detailsUrl, Map.class);
            Map<String, Object> detail = (Map<String, Object>) detailsResponse.get("result");

            PlaceInfo info = new PlaceInfo();
            info.setServiceProviderType(serviceProviderType);
            info.setPlaceId(placeId);
            List<Map<String, Object>> addressComponents = (List<Map<String, Object>>) detail.get("address_components");
            for (Map<String, Object> component : addressComponents) {
                List<String> types = (List<String>) component.get("types");
                if (types.contains("country")) {
                    info.setCountry((String) component.get("long_name"));
                } else if (types.contains("administrative_area_level_1")) {
                    info.setCity((String) component.get("long_name"));
                } else if (types.contains("administrative_area_level_2")) {
                    info.setTown((String) component.get("long_name"));
                }
            }
            info.setName((String) detail.get("name"));
            info.setAddress((String) detail.get("formatted_address"));
            info.setPhoneNumber((String) detail.get("formatted_phone_number"));
            info.setWebsite((String) detail.get("website"));
            info.setRating(detail.get("rating") != null ? ((Number) detail.get("rating")).doubleValue() : 0.0);
            info.setUserRatingsTotal(detail.get("user_ratings_total") != null ? ((Number) detail.get("user_ratings_total")).intValue() : 0);

            if(info.getPhoneNumber() != null && info.getPhoneNumber().startsWith("05")) {
                places.add(info);
            }
        }

        return places;
    }
}
