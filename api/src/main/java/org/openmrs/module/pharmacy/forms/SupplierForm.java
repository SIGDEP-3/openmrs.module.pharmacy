package org.openmrs.module.pharmacy.forms;

import org.openmrs.api.context.Context;
import org.openmrs.module.pharmacy.ProductSupplier;

import java.util.UUID;

public class SupplierForm {
    private Integer productSupplierId;
    private String name;
    private String shortName;
    private String description;
    private String phoneNumber;
    private String email;
    private Integer locationId;
    private String uuid = UUID.randomUUID().toString();

    public SupplierForm() {
    }

    public Integer getProductSupplierId() {
        return productSupplierId;
    }

    public void setProductSupplierId(Integer productSupplierId) {
        this.productSupplierId = productSupplierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setProductSupplier(ProductSupplier productSupplier) {
        setProductSupplierId(productSupplier.getProductSupplierId());
        setName(productSupplier.getName());
        setDescription(productSupplier.getDescription());
        setPhoneNumber(productSupplier.getPhoneNumber());
        setEmail(productSupplier.getEmail());
        setLocationId(productSupplier.getLocation().getLocationId());
        setUuid(productSupplier.getUuid());
    }

    public ProductSupplier getProductSupplier() {
        ProductSupplier supplier = new ProductSupplier();
        supplier.setProductSupplierId(getProductSupplierId());
        supplier.setName(getName());
        supplier.setDescription(getDescription());
        supplier.setPhoneNumber(getPhoneNumber());
        supplier.setEmail(getEmail());
        if (getLocationId() != null) {
            supplier.setLocation(Context.getLocationService().getLocation(getLocationId()));
            supplier.setName(Context.getLocationService().getLocation(getLocationId()).getName());
        }
        supplier.setUuid(getUuid());

        return supplier;
    }

}
