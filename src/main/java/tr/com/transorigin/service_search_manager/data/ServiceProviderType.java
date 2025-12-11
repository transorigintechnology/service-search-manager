package tr.com.transorigin.service_search_manager.data;

public enum ServiceProviderType {
    //Create enum with name and google places keywords
    TECHNICAL_SERVICE("Technical Service", "elektrik"),
    CLEANING_SERVICE("Cleaning Service", "temizlik"),
    INSURANCE_SERVICE("Insurance Service", "sigorta"),
    CARRIER ("Carrier", "nakliyat");
    private final String displayName;
    private final String googlePlacesKeywords;
    ServiceProviderType(String displayName, String googlePlacesKeywords) {
        this.displayName = displayName;
        this.googlePlacesKeywords = googlePlacesKeywords;
    }
    public String getDisplayName() {
        return displayName;
    }
    public String getGooglePlacesKeywords() {
        return googlePlacesKeywords;
    }
}
