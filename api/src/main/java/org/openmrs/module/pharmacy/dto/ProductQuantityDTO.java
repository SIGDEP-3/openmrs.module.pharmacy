package org.openmrs.module.pharmacy.dto;

public class ProductQuantityDTO {
    private String code;
    private String retailName;
    private String wholesaleName;
    private Integer retailQuantity;
    private Integer wholesaleQuantity;
    private Integer locationId;

    public ProductQuantityDTO() {
    }

    public ProductQuantityDTO(String code, String retailName, String wholesaleName, Integer retailQuantity, Integer wholesaleQuantity) {
        this.code = code;
        this.retailName = retailName;
        this.wholesaleName = wholesaleName;
        this.retailQuantity = retailQuantity;
        this.wholesaleQuantity = wholesaleQuantity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRetailName() {
        return retailName;
    }

    public void setRetailName(String retailName) {
        this.retailName = retailName;
    }

    public String getWholesaleName() {
        return wholesaleName;
    }

    public void setWholesaleName(String wholesaleName) {
        this.wholesaleName = wholesaleName;
    }

    public Integer getRetailQuantity() {
        return retailQuantity;
    }

    public void setRetailQuantity(Integer retailQuantity) {
        this.retailQuantity = retailQuantity;
    }

    public Integer getWholesaleQuantity() {
        return wholesaleQuantity;
    }

    public void setWholesaleQuantity(Integer wholesaleQuantity) {
        this.wholesaleQuantity = wholesaleQuantity;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }
}
