package tr.com.transorigin.service_search_manager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tools.jackson.databind.ObjectMapper;
import tr.com.transorigin.service_search_manager.clients.google.places.GooglePlaceService;
import tr.com.transorigin.service_search_manager.clients.google.places.PlaceInfo;
import tr.com.transorigin.service_search_manager.clients.relocation.RelocationRequest;
import tr.com.transorigin.service_search_manager.data.ServiceProvider;
import tr.com.transorigin.service_search_manager.data.ServiceProviderRepository;
import tr.com.transorigin.service_search_manager.data.ServiceProviderType;
import tr.com.transorigin.service_search_manager.dto.ServiceSearchResult;
import tr.com.transorigin.service_search_manager.producer.OfferProducer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceFinderService {

    private final ObjectMapper mapper;
    private final GooglePlaceService googlePlaceService;
    private final OfferProducer offerProducer;
    private final ServiceProviderRepository serviceProviderRepository;

    public void findRequiredServices(String message) {
        message = new String(message.getBytes(), StandardCharsets.UTF_8);
        ServiceSearchResult serviceSearchResult = new ServiceSearchResult();
        serviceSearchResult.setServiceProviders(new ArrayList<>());
        log.info("Processing service search for relocation request: {}", message);

        RelocationRequest request = mapper.readValue(message, RelocationRequest.class);
        serviceSearchResult.setRelocationRequest(request);


        searchServiceProvider(request, serviceSearchResult, ServiceProviderType.CARRIER);

        if(request.getTechnicalService().equals("true")) {
            searchServiceProvider(request, serviceSearchResult, ServiceProviderType.TECHNICAL_SERVICE);
        }
        if (request.getCleaningService().equals("true")) {
            searchServiceProvider(request, serviceSearchResult, ServiceProviderType.CLEANING_SERVICE);
        }
        offerProducer.publishPrepareOfferEvent(serviceSearchResult);
    }

    private void searchServiceProvider(RelocationRequest request, ServiceSearchResult serviceSearchResult, ServiceProviderType serviceProviderType) {
        log.info("Finding {} service providers for relocation ID: {}", serviceProviderType.getDisplayName(), request.getId());
        String country = serviceProviderType == ServiceProviderType.CARRIER ? request.getFromAddressCountry() : request.getToAddressCountry();
        String city = serviceProviderType == ServiceProviderType.CARRIER ? request.getFromAddressCity() : request.getToAddressCity();
        double latitude = serviceProviderType == ServiceProviderType.CARRIER ? Double.parseDouble(request.getFromAddressLat()) : Double.parseDouble(request.getToAddressLat());
        double longitude = serviceProviderType == ServiceProviderType.CARRIER ? Double.parseDouble(request.getFromAddressLong()) : Double.parseDouble(request.getToAddressLong());

        log.info("Searching existing {} service providers in database for country: {}, city: {} for relocation ID: {}", serviceProviderType.name(), country, city, request.getId());
        List<ServiceProvider> availableServiceProviders = serviceProviderRepository.getServiceProviders(
                serviceProviderType.name(),
                country,
                city
        );
        log.info("Found {} existing {} service providers from database for relocation ID: {}", availableServiceProviders.size(), serviceProviderType.getDisplayName(), request.getId());


        if(CollectionUtils.isEmpty(availableServiceProviders)) {
            List<PlaceInfo> googlePlacesServiceProviders = googlePlaceService.findDetailedPlaces(
                    latitude,
                    longitude,
                    15,
                    serviceProviderType
            );
            log.info("Found {} {} service providers from Google Places for relocation ID: {}", googlePlacesServiceProviders.size(), serviceProviderType.getDisplayName(), request.getId());
            serviceSearchResult.getServiceProviders().addAll(googlePlacesServiceProviders);
            serviceProviderRepository.saveAll(toDataList(googlePlacesServiceProviders));
        }
        else {
            log.info("Found {} existing {} service providers from database for relocation ID: {}", availableServiceProviders.size(), serviceProviderType.getDisplayName(), request.getId());
            serviceSearchResult.getServiceProviders().addAll(toDtoList(availableServiceProviders));
        }
    }



    private List<ServiceProvider> toDataList(List<PlaceInfo> googlePlacesServiceProviders) {
        List<ServiceProvider> dataList = new ArrayList<>();
        for (PlaceInfo placeInfo : googlePlacesServiceProviders) {
            ServiceProvider serviceProvider = new ServiceProvider();
            BeanUtils.copyProperties(placeInfo, serviceProvider);
            serviceProvider.setServiceProviderType(placeInfo.getServiceProviderType().name());
            serviceProvider.setActive(true);
            dataList.add(serviceProvider);
        }
        return dataList;
    }


    private List<PlaceInfo> toDtoList(List<ServiceProvider> dataList) {
        List<PlaceInfo> placeInfos = new ArrayList<>();
        for (ServiceProvider serviceProvider : dataList) {
            PlaceInfo placeInfo = new PlaceInfo();
            BeanUtils.copyProperties(serviceProvider, placeInfo);
            placeInfos.add(placeInfo);
        }
        return placeInfos;
    }

}
