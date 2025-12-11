package tr.com.transorigin.service_search_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tr.com.transorigin.service_search_manager.clients.google.places.PlaceInfo;
import tr.com.transorigin.service_search_manager.clients.relocation.RelocationRequest;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceSearchResult {
    private RelocationRequest relocationRequest;
    private List<PlaceInfo> serviceProviders;
}
