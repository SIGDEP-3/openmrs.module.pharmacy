package org.openmrs.module.pharmacy.models;

import java.util.ArrayList;
import java.util.List;

public class LocationProductQuantity {
    private String locationName;
    private List<ProductQuantity> productQuantities = new ArrayList<>();

    public LocationProductQuantity() {
    }

    public LocationProductQuantity(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public List<ProductQuantity> getProductQuantities() {
        return productQuantities;
    }

    public void setProductQuantities(List<ProductQuantity> productQuantities) {
        this.productQuantities = productQuantities;
    }
}
