package tr.com.transorigin.service_search_manager.clients.relocation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelocationRequest {
    private String id;
    private String name;
    private String phone;
    private String email;
    private String fromAddress;
    private String fromAddressCountry;
    private String fromAddressCity;
    private String fromAddressTown;
    private String fromAddressLat;
    private String fromAddressLong;
    private String toAddress;
    private String toAddressCountry;
    private String toAddressCity;
    private String toAddressTown;
    private String toAddressLat;
    private String toAddressLong;
    private String date;
    private Integer volume;
    private String insuranceService;
    private String cleaningService;
    private String technicalService;
    private String additionalNotes;
}
