package org.openmrs.module.pharmacy.dto;

import java.util.ArrayList;
import java.util.List;

public class LocationProductQuantityDTO {
    private String locationName;
    private List<ProductQuantityDTO> productQuantities = new ArrayList<>();

    public LocationProductQuantityDTO() {
    }

    public LocationProductQuantityDTO(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public List<ProductQuantityDTO> getProductQuantities() {
        return productQuantities;
    }

    public void setProductQuantities(List<ProductQuantityDTO> productQuantities) {
        this.productQuantities = productQuantities;
    }
}
